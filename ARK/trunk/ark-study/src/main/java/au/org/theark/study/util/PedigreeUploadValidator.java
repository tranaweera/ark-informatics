package au.org.theark.study.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.collections.ListUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.util.collections.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.XLStoCSV;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.worksheet.ArkGridCell;

import com.csvreader.CsvReader;
/**
 * The PED file is a white-space (space or tab) delimited file.The first six columns are mandatory. 
 * <p>
 * Validator checks the input file has satisfied the required file format 
 * @author thilina
 *
 */
public class PedigreeUploadValidator {
	private static Logger			log							= LoggerFactory.getLogger(SubjectConsentUploadValidator.class);

	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService;
	private Long						studyId;
	private Study						study;
	java.util.Collection<String>	fileValidationMessages	= new java.util.ArrayList<String>();
	java.util.Collection<String>	dataValidationMessages	= new java.util.ArrayList<String>();
	private HashSet<Integer>		existantSubjectUIDRows;
	private HashSet<Integer>		nonExistantUIDs;
	private HashSet<ArkGridCell>	errorCells;
	private SimpleDateFormat		simpleDateFormat			= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private char						delimiterCharacter		= au.org.theark.core.Constants.DEFAULT_DELIMITER_CHARACTER;
	private String						fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;
	private int						row							= 1;
	
	public PedigreeUploadValidator() {
		
	}
	
	public PedigreeUploadValidator(IArkCommonService iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		this.study = iArkCommonService.getStudy(studyId);
		this.existantSubjectUIDRows = new HashSet<Integer>();
		this.nonExistantUIDs = new HashSet<Integer>();
		this.errorCells = new HashSet<ArkGridCell>();
		simpleDateFormat.setLenient(false);
	}

	

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,STUDY_COMPONENT,STUDY_COMPONENT_STATUS...
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validatePedigreeFileFormat(UploadVO uploadVo) {
		java.util.Collection<String> validationMessages = null;
		try {
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			String filename = uploadVo.getFileUpload().getClientFileName();
			fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
			validationMessages = validatePedigreeFileFormat(inputStream, fileFormat, delimiterCharacter);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}
	
	
	/**
	 * Validates the file in the default "matrix" file data assumed: SUBJECTUID,STUDY_COMPONENT,STUDY_COMPONENT_STATUS...
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validatePedigreeFileData(UploadVO uploadVo, List<String> uidsToUpdateReference) {
		java.util.Collection<String> validationMessages = null;
//		try {
//			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
//			String filename = uploadVo.getFileUpload().getClientFileName();
//			fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
//			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
//
//			// If Excel, convert to CSV for validation
//			if (fileFormat.equalsIgnoreCase("XLS")) {
//				Workbook w;
//				try {
//					w = Workbook.getWorkbook(inputStream);
//					delimiterCharacter = ',';
//					XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
//					inputStream = xlsToCsv.convertXlsToCsv(w);
//					inputStream.reset();
//				}
//				catch (BiffException e) {
//					log.error(e.getMessage());
//				}
//			}
//
//			validationMessages = validatePedigreeFileData(inputStream, fileFormat, delimiterCharacter, uidsToUpdateReference);
//		}
//		catch (IOException e) {
//			log.error(e.getMessage());
//		}
		if(!fileFormat.equalsIgnoreCase("PED")){
			fileValidationMessages.add("The input file has to be a PED file");
			validationMessages = fileValidationMessages;
		}
		else{
			
		}
		return validationMessages;
	}
	
	
	public Collection<String> validatePedigreeFileData(InputStream inputStream, String fileFormat, char delimChar, List<String> uidsToUpdateReference) {
		java.util.Collection<String> validationMessages = null;

		try {
			validationMessages = validateMatrixCustomFileData(inputStream, inputStream.toString().length(), fileFormat, delimChar, Long.MAX_VALUE, uidsToUpdateReference);
		}
		catch (FileFormatException ffe) {
			log.error(au.org.theark.study.web.Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(au.org.theark.study.web.Constants.ARK_BASE_EXCEPTION + abe);
		}
		return validationMessages;
	}
	
	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,STUDY_COMPONENT,STUDY_COMPONENT_STATUS ...
	 * 
	 * TODO:  remove globals unless their is a legit reason
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @param rowsToValidate
	 *           validate the number of rows specified (or as many as exist, if that number is greater).
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 * @return a collection of data validation messages
	 */
	@SuppressWarnings("unchecked")
	public java.util.Collection<String> validateMatrixCustomFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr, long rowsToValidate, List<String> uidsToUpdateReference) throws FileFormatException, ArkSystemException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 1;
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			String[] stringLineArray;
			if (inLength <= 0) {
				throw new FileFormatException("The input files' size was not greater than 0.  Actual length reported: " + inLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			csvReader.readHeaders();

			List<String> subjectUIDsAlreadyExisting = iArkCommonService.getAllSubjectUIDs(study);
			
			MultiMap<String,String> consentUidStudyComponentMultiMap = new MultiMap<String, String>();
			
			List<StudyComp> studyComList = iArkCommonService.getStudyComponentByStudy(study);
			Map<String,StudyComp > studyCompMap = new HashMap<String,StudyComp>();
			for(StudyComp  studuComp:studyComList){
				studyCompMap.put(studuComp.getName().toUpperCase(), studuComp);
			}
			
			List<StudyCompStatus> studyCompStatusList = iArkCommonService.getStudyComponentStatus();
			Map<String, StudyCompStatus> studyCompStatusMap = new HashMap<String,StudyCompStatus>();
			for(StudyCompStatus studyCompStatus:studyCompStatusList){
				studyCompStatusMap.put(studyCompStatus.getName().toUpperCase(), studyCompStatus);
			}
			
			List<ConsentType> consentTypeList = iArkCommonService.getConsentType();
			Map<String,ConsentType> consentTypeMap= new HashMap<String,ConsentType>();
			for(ConsentType consentType : consentTypeList){
				consentTypeMap.put(consentType.getName().toUpperCase(), consentType);
			}
			
			List<ConsentStatus> consentStatusList = iArkCommonService.getConsentStatus();
			Map<String, ConsentStatus> consentStatusMap = new HashMap<String,ConsentStatus>();
			for(ConsentStatus consentStatus:consentStatusList){
				consentStatusMap.put(consentStatus.getName().toUpperCase(), consentStatus);
			}
			
			List<YesNo> consentDownloadedList = iArkCommonService.getYesNoList();
			Map<String, YesNo> consentDownloadedMap=new HashMap<String,YesNo>();
			for(YesNo consentDownloaded: consentDownloadedList){
				consentDownloadedMap.put(consentDownloaded.getName().toUpperCase(), consentDownloaded);
			}
			
			
			while (csvReader.readRecord()) {
				stringLineArray = csvReader.getValues();//i might still need this or might not now that i am evaluating by name ... TODO evaluate
				String subjectUID = stringLineArray[0];	// First/0th column should be the SubjectUID
				if(!subjectUIDsAlreadyExisting.contains(subjectUID)){
					nonExistantUIDs.add(row);//TODO test and compare array.
					errorCells.add(new ArkGridCell(0, row));
				}
				else{					
					this.validateRequiredConsentField(studyCompMap,consentUidStudyComponentMultiMap,subjectUID,csvReader.getIndex("STUDY_COMPONENT"), csvReader.get("STUDY_COMPONENT"), "STUDY_COMPONENT");
					this.validateRequiredConsentField(studyCompStatusMap, csvReader.getIndex("STUDY_COMPONENT_STATUS"), csvReader.get("STUDY_COMPONENT_STATUS"), "STUDY_COMPONENT_STATUS");
					this.validateRequiredConsentField(consentTypeMap, csvReader.getIndex("CONSENT_TYPE"), csvReader.get("CONSENT_TYPE"), "CONSENT_TYPE");
					this.validateRequiredConsentField(consentStatusMap, csvReader.getIndex("CONSENT_STATUS"), csvReader.get("CONSENT_STATUS"), "CONSENT_STATUS");
					this.validateRequiredConsentField(consentDownloadedMap, csvReader.getIndex("CONSENT_DOWNLOADED"), csvReader.get("CONSENT_DOWNLOADED"), "CONSENT_DOWNLOADED");
					
					if("Completed".equalsIgnoreCase(csvReader.get("STUDY_COMPONENT_STATUS"))){
						try{
							simpleDateFormat.parse(csvReader.get("COMPLETED_DATE"));
						}catch(Exception e){
							errorCells.add(new ArkGridCell(csvReader.getIndex("COMPLETED_DATE"), row));
							dataValidationMessages.add("valid COMPLETED_DATE is required to upload subject consent");
						}
					}
					
					String consentDate=csvReader.get("CONSENT_DATE");
					if(consentDate!=null && consentDate.trim().length()>0){
						try{
							simpleDateFormat.parse(consentDate);
						}catch(Exception e){
							errorCells.add(new ArkGridCell(csvReader.getIndex("CONSENT_DATE"), row));
							dataValidationMessages.add("Invalid CONSENT_DATE format");
						}	
					}					
				}
				row++;
			}
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally {
			if (csvReader != null) {
				try {
					csvReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
		}

		//TODO:  test hashset this i.intvalue or left hashset value??
		for (Iterator<Integer> iterator = nonExistantUIDs.iterator(); iterator.hasNext();) {
			Integer i = (Integer) iterator.next();
			dataValidationMessages.add("Subject on row " + i.intValue() + " does not exist in the database.  Please remove this row and retry or run upload/create this subject first.");
		}
		return dataValidationMessages;
	}
	
	private void validateRequiredConsentField(final Map<String,?> map, final MultiMap<String, String> existingItemMap, final String uid, final int col, final String cellValue, final String field){
		if(cellValue != null && cellValue.trim().length()>0){	
			if(map.get(cellValue.toUpperCase().trim())!=null){
				List<String>  values = existingItemMap.get(uid);
				if( values != null && values.contains(cellValue.toUpperCase().trim())){
					errorCells.add(new ArkGridCell(col, row));
					dataValidationMessages.add(cellValue +" is already exist in the uploading list");
				}
				else{
					existingItemMap.addValue(uid, cellValue.toUpperCase().trim());
				}
			}else{
				errorCells.add(new ArkGridCell(col, row));
				dataValidationMessages.add(cellValue +" is not exist in the system");
			}
		}else{
			errorCells.add(new ArkGridCell(col, row));
			dataValidationMessages.add(field+ " is required to upload subject consent");
		}
	}
	
	private void validateRequiredConsentField(final Map<String,?> map,final int col, final String cellValue, final String field){
		if(cellValue != null && cellValue.trim().length()>0){	
			if(map.get(cellValue.toUpperCase().trim())==null){
				errorCells.add(new ArkGridCell(col, row));
				dataValidationMessages.add(cellValue +" is not exist in the system");	
			}
		}else{
			errorCells.add(new ArkGridCell(col, row));
			dataValidationMessages.add(field+ " is required to upload subject consent");
		}
	}

	

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,STUDY_COMPONENT,STUDY_COMPONENT_STATUS....
	 * 
	 * @param inputStream
	 *           is the input stream of the file
	 * @param fileFormat
	 *           is the file format (eg txt)
	 * @param delimChar
	 *           is the delimiter character of the file (eg comma)
	 * @return a collection of validation messages
	 */
	public Collection<String> validatePedigreeFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;

		if(!fileFormat.equalsIgnoreCase("PED")){
			fileValidationMessages.add("The input file has to be a PED file");
			validationMessages = fileValidationMessages;
		}
		else{
			
		}
		return validationMessages;

	}

	/**
	 * Validates the file in the custom field list.
	 * 
	 * Requires.  SubjectUID specified in row one.  And all Fields must be valid for its type
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
	 * @return a collection of file format validation messages
	 */
	public java.util.Collection<String> validatePedigreeMatrixFileFormat(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkBaseException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 0;
		long srcLength = -1;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			// Set field list (note 2th column to Nth column) // SUBJECTUID DATE_COLLECTED F1 F2 FN // 0 1 2 3 N
			csvReader.readHeaders();
			srcLength = inLength - csvReader.getHeaders().toString().length();
			
			List<String> actualheaderColumnList= Arrays.asList(csvReader.getHeaders());
			
			List<String> requiredheaderColumnList = Arrays.asList(au.org.theark.study.web.Constants.SUBJECT_CONSENT_TEMPLATE_HEADER);

			List<String> diffColumnList=new ArrayList<String>();
			diffColumnList.addAll(ListUtils.subtract(requiredheaderColumnList, actualheaderColumnList));
			diffColumnList.addAll(ListUtils.subtract(actualheaderColumnList,requiredheaderColumnList ));
			
			if(diffColumnList.size()>0){
				StringBuffer sb = new StringBuffer();
				sb.append("Error: The specified file does not appear to conform to the expected file format.\n");
				sb.append("Please refer to the template, as seen on step one, for the correct format. \n");
				fileValidationMessages.add(sb.toString());
				for(String diffColumn:diffColumnList){
					if(requiredheaderColumnList.contains(diffColumn)){
						fileValidationMessages.add("Error: the column name " + diffColumn + " is not specified.");
					}
					else{
						fileValidationMessages.add("Error: the column name " + diffColumn + " is not a valid column name.");
					}
				}
			}
			row = 1;
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally {

			if (csvReader != null) {
				try {
					csvReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null) {
				try {
					// TODO ASAP : re-evaluate below
					inputStreamReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
		}

		return fileValidationMessages;
	}
	
	public HashSet<Integer> getInsertRows() {
		return existantSubjectUIDRows;
	}

	public void setInsertRows(HashSet<Integer> insertRows) {
		this.existantSubjectUIDRows = insertRows;
	}

	public HashSet<Integer> getUpdateRows() {
		return nonExistantUIDs;
	}

	public void setUpdateRows(HashSet<Integer> updateRows) {
		this.nonExistantUIDs = updateRows;
	}

	public HashSet<ArkGridCell> getErrorCells() {
		return errorCells;
	}

	public void setErrorCells(HashSet<ArkGridCell> errorCells) {
		this.errorCells = errorCells;
	}

}
