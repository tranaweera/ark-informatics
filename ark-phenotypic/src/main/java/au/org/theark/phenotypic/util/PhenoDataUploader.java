package au.org.theark.phenotypic.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.service.IPhenotypicService;

import com.csvreader.CsvReader;

/**
 * PhenotypicImport provides support for importing phenotypic matrix-formatted files. It features state-machine behaviour to allow an external class
 * to deal with how to store the data pulled out of the files.
 * 
 * @author cellis
 */
@SuppressWarnings("unused")
public class PhenoDataUploader
{
	private String						fieldName;
	private long						subjectCount;
	private long						fieldCount;
	private double						speed;
	private long						curPos;
	private long						srcLength					= -1;															// -1 means nothing being processed
	private StopWatch					timer							= null;
	private char						phenotypicDelimChr		= Constants.IMPORT_DELIM_CHAR_COMMA;					// default phenotypic file delimiter: COMMA
	private String						fileFormat;
	private IPhenotypicDao			phenotypicDao				= null;
	private Person						person;
	private PhenoCollection			phenoCollection;
	private List<Field>				fieldList;
	private Study						study;
	static Logger						log							= LoggerFactory.getLogger(PhenoDataUploader.class);
	java.util.Collection<String>	fileValidationMessages	= null;
	java.util.Collection<String>	dataValidationMessages	= null;
	private IPhenotypicService		iPhenoService			= null;
	private IArkCommonService		iArkCommonService			= null;
	private StringBuffer				uploadReport				= null;

	/**
	 * PhenotypicImport constructor
	 * 
	 * @param phenotypicDao
	 *           data access object perform select/insert/updates to the database
	 * @param studyId
	 *           study identifier in context
	 * @param collection
	 *           phenotypic collection in context
	 */
	public PhenoDataUploader(IPhenotypicService iPhenoService, Study study, PhenoCollection collection, IArkCommonService iArkCommonService, String fileFormat, char delimiterChar)
	{
		this.iPhenoService = iPhenoService;
		this.study = study;
		this.phenoCollection = collection;
		this.fileValidationMessages = new ArrayList<String>();
		this.dataValidationMessages = new ArrayList<String>();
		this.iArkCommonService = iArkCommonService;
		this.fileFormat = fileFormat;
		this.phenotypicDelimChr = delimiterChar;
	}

	/**
	 * Validates the phenotypic file in the default "matrix" file format assumed: SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 */
	public java.util.Collection<String> validateMatrixPhenoFileFormat(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException
	{
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		/*
		 * FieldData table requires: COLLECTION_ID PERSON_ID DATE_COLLECTED FIELD_ID USER_ID INSERT_TIME
		 */

		Date dateCollected = new Date();
		Field field = null;

		try
		{
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, phenotypicDelimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
			fieldCount = fieldNameArray.length - 2;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				if (csvReader.getColumnCount() < 2 || fieldCount < 1 || !fieldNameArray[0].equalsIgnoreCase(Constants.SUBJECTUID) || !fieldNameArray[1].equalsIgnoreCase(Constants.DATE_COLLECTED))
				{
					// Invalid file format
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("The specified file does not appear to conform to the expected phenotypic file format.\n");
					stringBuffer.append("The specified file format was: " + fileFormat + "\n");
					stringBuffer.append("The specified delimiter was: " + phenotypicDelimChr + "\n");
					stringBuffer.append("The default format is as follows:\n");
					stringBuffer.append(Constants.SUBJECTUID + phenotypicDelimChr + Constants.DATE_COLLECTED + phenotypicDelimChr + "FIELDNAME1" + phenotypicDelimChr + "FIELDNAME2" + phenotypicDelimChr + "FIELDNAME3" + phenotypicDelimChr + "FIELDNAMEX\n");
					stringBuffer.append("[subjectUid]" + phenotypicDelimChr + "[dateCollected]" + phenotypicDelimChr + "[field1value]" + phenotypicDelimChr + "[field2value]" + phenotypicDelimChr + "[field3value]" + phenotypicDelimChr + "[fieldXvalue]\n");
					stringBuffer.append("[.." + phenotypicDelimChr + "]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]\n");

					fileValidationMessages.add(stringBuffer.toString());
					break;
				}
				else
				{
					// Loop through columns in current row in file, starting from the 2th position
					for (int i = 0; i < stringLineArray.length; i++)
					{
						// Check each line has same number of columns as header
						if (stringLineArray.length < fieldNameArray.length)
						{
							fileValidationMessages.add("Error at line " + i + ", the line has missing cells");
						}

						// Update progress
						curPos += stringLineArray[i].length() + 1; // update progress
					}
				}

				subjectCount++;
			}

			if (fileValidationMessages.size() > 0)
			{
				for (Iterator<String> iterator = fileValidationMessages.iterator(); iterator.hasNext();)
				{
					String errorMessage = iterator.next();
					log.debug(errorMessage);
				}
			}
			else
			{
				log.debug("Validation is ok");
			}
		}
		catch (IOException ioe)
		{
			log.error("processMatrixPhenoFile IOException stacktrace:", ioe);
			throw new PhenotypicSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
		}
		catch (Exception ex)
		{
			log.error("processMatrixPhenoFile Exception stacktrace:", ex);
			throw new PhenotypicSystemException("Unexpected exception occurred when trying to process phenotypic data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			// fileValidationMessages.add("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			// fileValidationMessages.add("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");

			if (timer != null)
				timer = null;
			if (csvReader != null)
			{
				try
				{
					csvReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null)
			{
				try
				{
					inputStreamReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
			// Restore the state of variables
			srcLength = -1;
		}

		// if(subjectCount * fieldCount > 0)
		// fileValidationMessages.add("Validated " + subjectCount + " rows of data");

		return fileValidationMessages;
	}

	/**
	 * Validates the phenotypic data file in the default "matrix" file format assumed: SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 */
	public java.util.Collection<String> validateMatrixPhenoFileData(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException
	{
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		/*
		 * FieldData table requires: COLLECTION_ID PERSON_ID DATE_COLLECTED FIELD_ID USER_ID INSERT_TIME
		 */

		Date dateCollected = new Date();
		Field field = null;

		try
		{
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, phenotypicDelimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
			fieldCount = fieldNameArray.length - 2;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				
				// Fist columne should be SubjectUID
				String subjectUid = stringLineArray[0];
				// Second/1th column should be date collected
				String dateCollectedStr = stringLineArray[1];
				
				try
				{
					DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
					dateFormat.setLenient(false);
					dateCollected = dateFormat.parse(dateCollectedStr);
				}
				catch (ParseException pe)
				{
					dataValidationMessages.add(PhenotypicValidationMessage.dateCollectedNotValidDate(subjectUid, dateCollectedStr));	
				}

				// Loop through columns in current row in file, starting from the 2th position
				for (int i = 2; i < stringLineArray.length; i++)
				{
					try
					{
						FieldData fieldData = new FieldData();

						fieldData.setCollection(this.phenoCollection);

						// First/0th column should be the Subject UID
						// If no Subject UID found, caught by exception catch
						fieldData.setLinkSubjectStudy(iArkCommonService.getSubjectByUID(subjectUid));
						
						// Set field
						field = new Field();
						field = iPhenoService.getFieldByNameAndStudy(fieldNameArray[i], study);
						fieldData.setField(field);
						
						// Other/ith columns should be the field data value
						fieldData.setValue(stringLineArray[i]);
						
						// Validate the field data
						PhenotypicValidator.validateFieldData(fieldData, dataValidationMessages);
					}
					// TODO handle via ArkSystemExcpetion
					catch (au.org.theark.core.exception.EntityNotFoundException enfe)
					{
						log.error("Error with Subject UID: " + enfe.getMessage());
					}

					// Update progress
					curPos += stringLineArray[i].length() + 1; // update progress

					// Debug only - Show progress and speed
					log.debug("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
				}

				log.debug("\n");
				subjectCount++;
			}

			if (dataValidationMessages.size() > 0)
			{
				log.debug("Validation messages: " + dataValidationMessages.size());
				for (Iterator<String> iterator = dataValidationMessages.iterator(); iterator.hasNext();)
				{
					String errorMessage = iterator.next();
					log.debug(errorMessage);
				}
			}
			else
			{
				log.debug("Validation is ok");
			}
		}
		catch (IOException ioe)
		{
			log.error("processMatrixPhenoFile IOException stacktrace:", ioe);
			throw new PhenotypicSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
		}
		catch (Exception ex)
		{
			log.error("processMatrixPhenoFile Exception stacktrace:", ex);
			throw new PhenotypicSystemException("Unexpected exception occurred when trying to process phenotypic data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			log.debug("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			log.debug("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
			if (timer != null)
				timer = null;
			if (csvReader != null)
			{
				try
				{
					csvReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null)
			{
				try
				{
					inputStreamReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
			// Restore the state of variables
			srcLength = -1;
		}
		log.debug("Validated " + subjectCount * fieldCount + " rows of data");

		// if(subjectCount * fieldCount > 0)
		// dataValidationMessages.add("Validated " + subjectCount * fieldCount + " rows of data");

		return dataValidationMessages;
	}

	/**
	 * Imports the phenotypic data file to the database tables Assumes the file is in the default "matrix" file format:
	 * SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN... 1,01/01/1900,99.99,99.99,, ...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 */
	public void importMatrixPhenoFile(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException
	{
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		Date dateCollected = new Date();
		Field field = null;

		try
		{
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, phenotypicDelimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
			fieldCount = fieldNameArray.length - 2;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				// Loop through columns in current row in file, starting from the 2th position
				for (int i = 0; i < stringLineArray.length; i++)
				{
					// Field data actually the 2th colum onward
					if (i > 1)
					{
						// Print out column details
						log.debug(fieldNameArray[i] + "\t" + stringLineArray[i]);

						try
						{
							log.debug("Creating new field data for: " + Constants.SUBJECTUID + ": " + stringLineArray[0] + "\t" + Constants.DATE_COLLECTED + ": " + stringLineArray[1] + "\tFIELD: "
									+ fieldNameArray[i] + "\tVALUE: " + stringLineArray[i]);

							FieldData fieldData = new FieldData();

							fieldData.setCollection(this.phenoCollection);

							// First/0th column should be the Subject UID
							// If no Subject UID found, caught by exception catch
							fieldData.setLinkSubjectStudy(iArkCommonService.getSubjectByUID(stringLineArray[0]));

							// Second/1th column should be date collected
							DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
							fieldData.setDateCollected(dateFormat.parse(stringLineArray[1]));

							// Set field
							field = new Field();
							field = iPhenoService.getFieldByNameAndStudy(fieldNameArray[i], study);
							fieldData.setField(field);

							// Other/ith columns should be the field data value
							fieldData.setValue(stringLineArray[i]);

							// Try to create the field data
							iPhenoService.createFieldData(fieldData);
						}
						// TODO handle via ArkSystemExcpetion
						catch (au.org.theark.core.exception.EntityNotFoundException enfe)
						{
							log.error("Error with Subject UID: " + enfe.getMessage());
						}
					}

					// Update progress
					curPos += stringLineArray[i].length() + 1; // update progress

					// Debug only - Show progress and speed
					log.debug("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
				}

				log.debug("\n");
				subjectCount++;
			}
		}
		catch (IOException ioe)
		{
			log.error("processMatrixPhenoFile IOException stacktrace:", ioe);
			throw new PhenotypicSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
		}
		catch (Exception ex)
		{
			log.error("processMatrixPhenoFile Exception stacktrace:", ex);
			throw new PhenotypicSystemException("Unexpected exception occurred when trying to process phenotypic data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			log.debug("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			log.debug("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
			if (timer != null)
				timer = null;
			if (csvReader != null)
			{
				try
				{
					csvReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null)
			{
				try
				{
					inputStreamReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
			// Restore the state of variables
			srcLength = -1;
		}
		log.debug("Inserted " + subjectCount * fieldCount + " rows of data");
	}

	/**
	 * Imports the phenotypic data file to the database tables, and creates report on the process Assumes the file is in the default "matrix" file
	 * format: SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN... 1,01/01/1900,99.99,99.99,, ...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 * @return the import report detailing the import process
	 */
	public StringBuffer importAndReportMatrixPhenoFile(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException
	{
		uploadReport = new StringBuffer();
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		Date dateCollected = new Date();
		Field field = null;

		try
		{
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, phenotypicDelimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				uploadReport.append("The input size was not greater than 0.  Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
			fieldCount = fieldNameArray.length - 2;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				// Loop through columns in current row in file, starting from the 2th position
				for (int i = 0; i < stringLineArray.length; i++)
				{
					// Field data actually the 2th column onward
					if (i > 1)
					{
						// Print out column details
						log.debug(fieldNameArray[i] + "\t" + stringLineArray[i]);

						try
						{
							uploadReport.append("Creating new field data for: ");
							uploadReport.append(Constants.SUBJECTUID);
							uploadReport.append(": ");
							uploadReport.append(stringLineArray[0]);
							uploadReport.append("\t");
							uploadReport.append(Constants.DATE_COLLECTED);
							uploadReport.append(": ");
							uploadReport.append(stringLineArray[1]);
							uploadReport.append("\tFIELD: ");
							uploadReport.append(fieldNameArray[i]);
							uploadReport.append("\tVALUE: ");
							uploadReport.append(stringLineArray[i]);
							uploadReport.append("\n");

							FieldData fieldData = new FieldData();

							fieldData.setCollection(this.phenoCollection);

							// First/0th column should be the Subject UID
							// If no Subject UID found, caught by exception catch
							fieldData.setLinkSubjectStudy(iArkCommonService.getSubjectByUID(stringLineArray[0]));

							// Second/1th column should be date collected
							DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
							fieldData.setDateCollected(dateFormat.parse(stringLineArray[1]));

							// Set field
							field = new Field();
							field = iPhenoService.getFieldByNameAndStudy(fieldNameArray[i], study);
							fieldData.setField(field);

							// Other/ith columns should be the field data value
							fieldData.setValue(stringLineArray[i]);
							
							// Flag data that failed validation, but was overridden and imported
							fieldData.setPassedQualityControl(PhenotypicValidator.fieldDataPassesQualityControl(fieldData, null));

							// Try to create the field data
							iPhenoService.createFieldData(fieldData);
						}
						// TODO handle via ArkSystemExcpetion
						catch (au.org.theark.core.exception.EntityNotFoundException enfe)
						{
							uploadReport.append("Critical error during data import\n");
							log.error("Error with Subject UID: " + enfe.getMessage());
						}
					}

					// Update progress
					curPos += stringLineArray[i].length() + 1;

					// Debug only - Show progress and speed
					log.debug("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
				}

				log.debug("\n");
				subjectCount++;
			}
		}
		catch (IOException ioe)
		{
			uploadReport.append("Unexpected I/O exception whilst reading the phenotypic data file\n");
			log.error("processMatrixPhenoFile IOException stacktrace:", ioe);
			throw new PhenotypicSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
		}
		catch (Exception ex)
		{
			uploadReport.append("Unexpected exception whilst reading the phenotypic data file\n");
			log.error("processMatrixPhenoFile Exception stacktrace:", ex);
			throw new PhenotypicSystemException("Unexpected exception occurred when trying to process phenotypic data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			uploadReport.append("Total elapsed time: ");
			uploadReport.append(timer.getTime());
			uploadReport.append(" ms or ");
			uploadReport.append(decimalFormat.format(timer.getTime() / 1000.0));
			uploadReport.append(" s");
			uploadReport.append("\n");
			uploadReport.append("Total file size: ");
			uploadReport.append(srcLength);
			uploadReport.append(" B or ");
			uploadReport.append(decimalFormat.format(srcLength / 1024.0 / 1024.0));
			uploadReport.append(" MB");
			uploadReport.append("\n");

			if (timer != null)
				timer = null;

			if (csvReader != null)
			{
				try
				{
					csvReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null)
			{
				try
				{
					inputStreamReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
			// Restore the state of variables
			srcLength = -1;
		}
		uploadReport.append("Inserted ");
		uploadReport.append(subjectCount * fieldCount);
		uploadReport.append(" rows of data");
		uploadReport.append("\n");

		return uploadReport;
	}
	
	/**
	 * Return the progress of the current process in %
	 * 
	 * @return if a process is actively running, then progress in %; or if no process running, then returns -1
	 */
	public double getProgress()
	{
		double progress = -1;

		if (srcLength > 0)
			progress = curPos * 100.0 / srcLength; // %

		return progress;
	}

	/**
	 * Return the speed of the current process in KB/s
	 * 
	 * @return if a process is actively running, then speed in KB/s; or if no process running, then returns -1
	 */
	public double getSpeed()
	{
		double speed = -1;

		if (srcLength > 0)
			speed = curPos / 1024 / (timer.getTime() / 1000.0); // KB/s

		return speed;
	}
}
