package au.org.theark.study.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.apache.commons.lang.time.StopWatch;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.service.IStudyService;

import com.csvreader.CsvReader;

/**
 * SubjectUploader provides support for uploading subject matrix-formatted files. It features state-machine behaviour to allow an external class to
 * deal with how to store the data pulled out of the files.
 * 
 * @author cellis
 */
public class SubjectUploader
{
	private long					subjectCount;
	private long					insertCount;
	private long					updateCount;
	private long					curPos;
	private long					srcLength				= -1;																				// -1 means nothing being
																																							// processed
	private StopWatch				timer						= null;
	private char					delimiterCharacter	= Constants.DEFAULT_DELIMITER_CHARACTER;									// default
	// delimiter:
	// COMMA
	private Study					study;
	static Logger					log						= LoggerFactory.getLogger(SubjectUploader.class);
	private IArkCommonService	iArkCommonService		= null;
	private IStudyService		iStudyService			= null;
	private StringBuffer			uploadReport			= null;
	private SimpleDateFormat	simpleDateFormat		= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private Collection<SubjectVO> insertSubjects = new ArrayList<SubjectVO>();
	private Collection<SubjectVO> updateSubjects = new ArrayList<SubjectVO>();

	/**
	 * SubjectUploader constructor
	 * 
	 * @param study
	 *           study identifier in context
	 * @param iArkCommonService
	 *           common ARK service to perform select/insert/updates to the database
	 * @param iStudyService
	 *           study service to perform select/insert/updates to the study database
	 */
	public SubjectUploader(Study study, IArkCommonService iArkCommonService, IStudyService iStudyService)
	{
		this.study = study;
		this.iArkCommonService = iArkCommonService;
		this.iStudyService = iStudyService;
		simpleDateFormat.setLenient(false);
	}

	/**
	 * Imports the subject data file to the database tables, and creates report on the process Assumes the file is in the default "matrix" file format:
	 * SUBJECTUID,FIELD1,FIELD2,FIELDN... 1,01/01/1900,99.99,99.99,, ...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 * @return the upload report detailing the upload process
	 */
	public StringBuffer uploadAndReportMatrixSubjectFile(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkSystemException
	{
		delimiterCharacter = inDelimChr;
		uploadReport = new StringBuffer();
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		try
		{
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				uploadReport.append("The input size was not greater than 0. Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0. Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTUID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			int index = 0;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				String subjectUID = stringLineArray[0];

				SubjectVO subjectVo = new SubjectVO();
				LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
				linkSubjectStudy.setStudy(study);

				try
				{
					linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID);
				}
				catch (EntityNotFoundException enf)
				{
					// New subject
					linkSubjectStudy.setSubjectUID(subjectUID);
					linkSubjectStudy.setStudy(study);
				}
				Person person = linkSubjectStudy.getPerson();
				subjectVo.setSubjectStudy(linkSubjectStudy);

				if (linkSubjectStudy.getId() == null && linkSubjectStudy.getPerson().getId() == null)
				{
					person = new Person();
				}
				else
				{
					person = linkSubjectStudy.getPerson();
				}

				if (csvReader.getIndex("FIRST_NAME") > 0)
					person.setFirstName(stringLineArray[csvReader.getIndex("FIRST_NAME")]);

				if (csvReader.getIndex("MIDDLE_NAME") > 0)
					person.setMiddleName(stringLineArray[csvReader.getIndex("MIDDLE_NAME")]);

				if (csvReader.getIndex("LAST_NAME") > 0)
					person.setLastName(stringLineArray[csvReader.getIndex("LAST_NAME")]);

				if (csvReader.getIndex("PREFERRED_NAME") > 0)
					person.setPreferredName(stringLineArray[csvReader.getIndex("PREFERRED_NAME")]);

				if (csvReader.getIndex("GENDER_TYPE") > 0 || csvReader.getIndex("GENDER") > 0 || csvReader.getIndex("SEX") > 0)
				{
					GenderType genderType;
					if (csvReader.getIndex("GENDER_TYPE") > 0)
					{
						index = csvReader.getIndex("GENDER_TYPE");
					}
					else if (csvReader.getIndex("GENDER") > 0)
					{
						index = csvReader.getIndex("GENDER");
					}
					else
					{
						index = csvReader.getIndex("SEX");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0)
					{
						genderType = iArkCommonService.getGenderType(stringLineArray[index]);
						person.setGenderType(genderType);
					}
				}

				if (csvReader.getIndex("DATE_OF_BIRTH") > 0 || csvReader.getIndex("DOB") > 0)
				{
					Date dateOfBirth = new Date();

					if (csvReader.getIndex("DATE_OF_BIRTH") > 0)
					{
						index = csvReader.getIndex("DATE_OF_BIRTH");
					}
					else
					{
						index = csvReader.getIndex("DOB");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0)
					{
						dateOfBirth = simpleDateFormat.parse(stringLineArray[index]);
						person.setDateOfBirth(dateOfBirth);
					}
				}

				if (csvReader.getIndex("DATE_OF_DEATH") > 0 || csvReader.getIndex("DODEATH") > 0)
				{
					Date dateOfDeath = new Date();
					if (csvReader.getIndex("DATE_OF_DEATH") > 0)
					{
						index = csvReader.getIndex("DATE_OF_DEATH");
					}
					else
					{
						index = csvReader.getIndex("DODEATH");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0)
					{
						dateOfDeath = simpleDateFormat.parse(stringLineArray[index]);
						person.setDateOfDeath(dateOfDeath);
					}
				}

				if (csvReader.getIndex("CAUSE_OF_DEATH") > 0 || csvReader.getIndex("CODEATH") > 0)
				{
					if (csvReader.getIndex("CAUSE_OF_DEATH") > 0)
					{
						index = csvReader.getIndex("CAUSE_OF_DEATH");
					}
					else
					{
						index = csvReader.getIndex("CODEATH");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0)
					{
						person.setCauseOfDeath(stringLineArray[index]);
					}
				}

				if (csvReader.getIndex("VITAL_STATUS") > 0)
				{
					String vitalStatusStr = (stringLineArray[csvReader.getIndex("VITAL_STATUS")]);
					VitalStatus vitalStatus = iArkCommonService.getVitalStatus(vitalStatusStr);
					person.setVitalStatus(vitalStatus);
				}

				if (csvReader.getIndex("PREFERRED_EMAIL") > 0)
				{
					person.setPreferredEmail(stringLineArray[csvReader.getIndex("PREFERRED_EMAIL")]);
				}

				if (csvReader.getIndex("OTHER_EMAIL") > 0)
				{
					person.setPreferredEmail(stringLineArray[csvReader.getIndex("OTHER_EMAIL")]);
				}

				if (csvReader.getIndex("TITLE") > 0)
				{
					String titleStr = (stringLineArray[csvReader.getIndex("TITLE")]);
					TitleType titleType = iArkCommonService.getTitleType(titleStr);
					person.setTitleType(titleType);
				}

				if (csvReader.getIndex("MARITAL_STATUS") > 0)
				{
					String maritalStatusStr = (stringLineArray[csvReader.getIndex("MARITAL_STATUS")]);
					MaritalStatus maritalStatus = iArkCommonService.getMaritalStatus(maritalStatusStr);
					person.setMaritalStatus(maritalStatus);
				}

				if (csvReader.getIndex("PERSON_CONTACT_METHOD") > 0 || csvReader.getIndex("CONTACT_METHOD") > 0)
				{
					String personContactMethodStr = null;
					if (csvReader.getIndex("PERSON_CONTACT_METHOD") > 0)
					{
						personContactMethodStr = (stringLineArray[csvReader.getIndex("PERSON_CONTACT_METHOD")]);
					}
					else
					{
						personContactMethodStr = (stringLineArray[csvReader.getIndex("CONTACT_METHOD")]);
					}
					PersonContactMethod personContactMethod = iArkCommonService.getPersonContactMethod(personContactMethodStr);
					person.setPersonContactMethod(personContactMethod);
				}

				if (csvReader.getIndex("STATUS") > 0)
				{
					String statusStr = (stringLineArray[csvReader.getIndex("STATUS")]);
					SubjectStatus subjectStatus = iArkCommonService.getSubjectStatus(statusStr);
					linkSubjectStudy.setSubjectStatus(subjectStatus);
				}

				linkSubjectStudy.setPerson(person);
				subjectVo.setSubjectStudy(linkSubjectStudy);

				if (subjectVo.getSubjectStudy().getId() == null || subjectVo.getSubjectStudy().getPerson().getId() == 0)
				{
					//iStudyService.createSubject(subjectVo);
					insertSubjects.add(subjectVo);
					StringBuffer sb = new StringBuffer();
					sb.append("Subject UID: ");
					sb.append(subjectVo.getSubjectStudy().getSubjectUID());
					sb.append(" has been created successfully and linked to the study: ");
					sb.append(study.getName());
					sb.append("\n");
					uploadReport.append(sb);
					insertCount++;
				}
				else
				{
					//iStudyService.updateSubject(subjectVo);
					updateSubjects.add(subjectVo);
					StringBuffer sb = new StringBuffer();
					sb.append("Subject UID: ");
					sb.append(subjectVo.getSubjectStudy().getSubjectUID());
					sb.append(" has been updated successfully and linked to the study: ");
					sb.append(study.getName());
					sb.append("\n");
					uploadReport.append(sb);
					updateCount++;
				}

				log.debug("\n");
				subjectCount++;
			}
		}
		catch (IOException ioe)
		{
			uploadReport.append("Unexpected I/O exception whilst reading the subject data file\n");
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex)
		{
			uploadReport.append("Unexpected exception whilst reading the subject data file\n");
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			uploadReport.append("\n");
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
		uploadReport.append("Processed ");
		uploadReport.append(subjectCount);
		uploadReport.append(" subjects.");
		uploadReport.append("\n");
		uploadReport.append("Inserted ");
		uploadReport.append(insertCount);
		uploadReport.append(" subjects.");
		uploadReport.append("\n");
		uploadReport.append("Updated ");
		uploadReport.append(updateCount);
		uploadReport.append(" subjects.");
		uploadReport.append("\n");

		// Batch insert/update
		try
		{
			iStudyService.batchInsertSubjects(insertSubjects);
		}
		catch (ArkUniqueException e)
		{
			e.printStackTrace();
		}
		catch (ArkSubjectInsertException e)
		{
			e.printStackTrace();
		}
		try
		{
			iStudyService.batchUpdateSubjects(updateSubjects);
		}
		catch (ArkUniqueException e)
		{
			e.printStackTrace();
		}
		catch (ArkSubjectInsertException e)
		{
			e.printStackTrace();
		}
		
		return uploadReport;
	}

	public org.apache.wicket.util.file.File convertXlsToCsv(File file, char delimChr)
	{
		delimiterCharacter = delimChr;

		// File to store data in form of CSV
		org.apache.wicket.util.file.File f = new org.apache.wicket.util.file.File(file.getAbsoluteFile() + ".csv");
		try
		{
			OutputStream os = (OutputStream) new FileOutputStream(f);
			String encoding = "UTF8";
			OutputStreamWriter osw = new OutputStreamWriter(os, encoding);
			BufferedWriter bw = new BufferedWriter(osw);

			// Excel document to be imported
			String filename = file.getAbsolutePath();
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			Workbook w = Workbook.getWorkbook(new File(filename), ws);

			// Gets first sheet from workbook
			Sheet s = w.getSheet(0);

			Cell[] row = null;

			// Gets the cells from sheet
			for (int i = 0; i < s.getRows(); i++)
			{
				row = s.getRow(i);

				if (row.length > 0)
				{
					bw.write(row[0].getContents());
					for (int j = 1; j < row.length; j++)
					{
						bw.write(delimiterCharacter);
						bw.write(row[j].getContents());
					}
				}
				bw.newLine();
			}

			bw.flush();
			bw.close();
		}
		catch (UnsupportedEncodingException e)
		{
			System.err.println(e.toString());
		}
		catch (IOException e)
		{
			System.err.println(e.toString());
		}
		catch (Exception e)
		{
			System.err.println(e.toString());
		}
		return f;
	}

	public InputStream convertXlsToCsv(Workbook w)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try
		{
			OutputStreamWriter osw = new OutputStreamWriter(out);

			// Gets first sheet from workbook
			Sheet s = w.getSheet(0);

			Cell[] row = null;

			// Gets the cells from sheet
			for (int i = 0; i < s.getRows(); i++)
			{
				row = s.getRow(i);

				if (row.length > 0)
				{
					osw.write(row[0].getContents());
					for (int j = 1; j < row.length; j++)
					{
						osw.write(delimiterCharacter);
						osw.write(row[j].getContents());
					}
				}
				osw.write("\n");
			}

			osw.flush();
			osw.close();
		}
		catch (UnsupportedEncodingException e)
		{
			System.err.println(e.toString());
		}
		catch (IOException e)
		{
			System.err.println(e.toString());
		}
		catch (Exception e)
		{
			System.err.println(e.toString());
		}
		return new ByteArrayInputStream(out.toByteArray());
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