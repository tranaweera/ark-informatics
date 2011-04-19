package au.org.theark.phenotypic.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
import au.org.theark.phenotypic.model.entity.DelimiterType;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.FileFormat;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.PhenoCollectionUpload;
import au.org.theark.phenotypic.model.entity.PhenoUpload;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.util.PhenoDataUploader;

@Transactional
@Service("phenotypicService")
public class PhenotypicServiceImpl implements IPhenotypicService
{
	final Logger				log	= LoggerFactory.getLogger(PhenotypicServiceImpl.class);
	
	private IArkCommonService iArkCommonService;
	private IPhenotypicDao	phenotypicDao;
	private Long studyId;
	private Study study;
	
	public IArkCommonService getiArkCommonService()
	{
		return iArkCommonService;
	}

	@Autowired
	public void setiArkCommonService(IArkCommonService iArkCommonService)
	{
		this.iArkCommonService = iArkCommonService;
	}
	
	@Autowired
	public void setPhenotypicDao(IPhenotypicDao phenotypicDao)
	{
		this.phenotypicDao = phenotypicDao;
	}
	
	public IPhenotypicDao getPhenotypicDao()
	{
		return phenotypicDao;
	}

	/**
    * A Phenotypic collection is the data storage or grouping of a particular set set of data,
    * containing subjects with fields with field data values for a particular date collected 
    *
    * @param col  the collection object to be created
    */
	public void createCollection(PhenoCollection col)
	{
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);

		// Newly created collections must start with a Created status
		Status status = phenotypicDao.getStatusByName(Constants.STATUS_CREATED);
		
		col.setStatus(status);
		col.setStudy(iArkCommonService.getStudy(studyId));

		phenotypicDao.createPhenoCollection(col);
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Pheno Collection " + col.getName());
		ah.setEntityId(col.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}
	
	/**
    * A Phenotypic collection is the data storage or grouping of a particular set set of data,
    * containing subjects with fields with field data values for a particular date collected 
    *
    * @param phenoVo the collectionVo object to be created
    */
	public void createCollection(PhenoCollectionVO phenoVo)
	{
		phenotypicDao.createPhenoCollection(phenoVo);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Pheno Collection " + phenoVo.getPhenoCollection().getName());
		ah.setEntityId(phenoVo.getPhenoCollection().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}
	
	public void deleteCollection(PhenoCollectionVO phenoVo)
	{
		phenotypicDao.deletePhenoCollection(phenoVo);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Pheno Collection " + phenoVo.getPhenoCollection().getName());
		ah.setEntityId(phenoVo.getPhenoCollection().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	public void updateCollection(PhenoCollectionVO phenoVo)
	{
		phenotypicDao.updatePhenoCollection(phenoVo);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Pheno Collection " + phenoVo.getPhenoCollection().getName());
		ah.setEntityId(phenoVo.getPhenoCollection().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	/**
    * A Phenotypic collection import is the job that runs to import the data into the database.
    * It contains relevant metadata about the import, such as start time and finish time
    *
    * @param colImport  the collection import object to be created
    */
	public void createCollectionImport(PhenoCollectionUpload colImport)
	{
		phenotypicDao.createCollectionUpload(colImport);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Pheno Collection Upload " + colImport.getId());
		ah.setEntityId(colImport.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}
	
	public void updateCollection(PhenoCollection colEntity)
	{
		phenotypicDao.updatePhenoCollection(colEntity);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Pheno Collection " + colEntity.getName());
		ah.setEntityId(colEntity.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}

	public void createField(Field field)
	{
		phenotypicDao.createField(field);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Field " + field.getName());
		ah.setEntityId(field.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD);
		iArkCommonService.createAuditHistory(ah);
	}

	public FieldType getFieldTypeByName(String fieldTypeName)
	{
		return phenotypicDao.getFieldTypeByName(fieldTypeName);
	}

	public Status getStatusByName(String statusName)
	{
		return phenotypicDao.getStatusByName(statusName);
	}
	
	public java.util.Collection<Status> getStatus(){
		return phenotypicDao.getStatus();
	}

	public Field getField(Long fieldId)
	{
		return phenotypicDao.getField(fieldId);
	}

	public PhenoCollection getPhenoCollection(Long id)
	{
		return phenotypicDao.getPhenotypicCollection(id);
	}
	
	public PhenoCollectionVO getPhenoCollectionAndFields(Long id)
	{
		return phenotypicDao.getPhenoCollectionAndFields(id);
	}
	
	public Collection<PhenoCollection> getPhenoCollectionByStudy(Study study)
	{
		return phenotypicDao.getPhenotypicCollectionByStudy(study);
	}
	
	public void createFieldData(FieldData fieldData)
	{
		phenotypicDao.createFieldData(fieldData);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created Field Data For Field " + fieldData.getField().getName());
		ah.setEntityId(fieldData.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD_DATA);
		iArkCommonService.createAuditHistory(ah);
	}

	public java.util.Collection<FieldType> getFieldTypes()
	{
		return phenotypicDao.getFieldTypes();
	}

	public void deleteCollection(PhenoCollection collection)
	{
		phenotypicDao.createPhenoCollection(collection);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Pheno Collection " + collection.getName());
		ah.setEntityId(collection.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION);
		iArkCommonService.createAuditHistory(ah);
	}


	public void deleteField(Field field)
	{
		phenotypicDao.deleteField(field);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Field " + field.getName());
		ah.setEntityId(field.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void deleteFieldData(FieldData fieldData)
	{
		phenotypicDao.deleteFieldData(fieldData);	
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted Field Data for Field " + fieldData.getField().getName());
		ah.setEntityId(fieldData.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD_DATA);
		iArkCommonService.createAuditHistory(ah);
	}

	public PhenoCollectionUpload getCollectionImport(Long id)
	{
		return phenotypicDao.getCollectionUpload(id);
	}

	public FieldType getFieldType(Long id)
	{
		return phenotypicDao.getFieldType(id);
	}

	public java.util.Collection<Field> searchField(Field field)
	{
		return phenotypicDao.searchField(field);
	}

	public Field getFieldByNameAndStudy(String fieldName, Study study)
	{
		return phenotypicDao.getFieldByNameAndStudy(fieldName, study);
	}
	
	public void updateField(Field field)
	{
		phenotypicDao.updateField(field);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Field " + field.getName());
		ah.setEntityId(field.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void updateFieldData(FieldData fieldData)
	{
		phenotypicDao.updateFieldData(fieldData);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated Field Data for Field " + fieldData.getField().getName());
		ah.setEntityId(fieldData.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_FIELD_DATA);
		iArkCommonService.createAuditHistory(ah);
	}

	public java.util.Collection<PhenoCollection> searchPhenotypicCollection(PhenoCollection phenotypicCollection)
	{
		return phenotypicDao.searchPhenotypicCollection(phenotypicCollection);
	}

	public Collection<FieldData> searchFieldDataByField(Field field)
	{
		return phenotypicDao.searchFieldDataByField(field);
	}
	
	public Collection<FieldData> searchFieldData(FieldData fieldData)
	{
		return phenotypicDao.searchFieldData(fieldData);
	}

	public void createUpload(PhenoUpload upload)
	{
		phenotypicDao.createUpload(upload);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created PhenoUpload for File " + upload.getFilename());
		ah.setEntityId(upload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}
	
	public void createUpload(UploadVO uploadVo)
	{
		phenotypicDao.createUpload(uploadVo);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created PhenoUpload for File " + uploadVo.getUpload().getFilename());
		ah.setEntityId(uploadVo.getUpload().getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}
	
	public void updateUpload(PhenoUpload upload)
	{
		phenotypicDao.updateUpload(upload);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Created PhenoUpload for File " + upload.getFilename());
		ah.setEntityId(upload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void deleteUpload(PhenoUpload upload)
	{
		phenotypicDao.deleteUpload(upload);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted PhenoUpload " + upload.getFilename());
		ah.setEntityId(upload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public Collection<FileFormat> getFileFormats()
	{
		return phenotypicDao.getFileFormats();
	}

	public PhenoCollectionVO getPhenoCollectionAndUploads(Long id)
	{
		return phenotypicDao.getPhenoCollectionAndUploads(id);
	}
	
	public PhenoCollectionVO getPhenoCollectionAndUploads(PhenoCollection phenoCollection)
	{
		return phenotypicDao.getPhenoCollectionAndUploads(phenoCollection);
	}

	public PhenoUpload getUpload(Long id)
	{
		return phenotypicDao.getUpload(id);
	}

	public Collection<PhenoUpload> searchUpload(PhenoUpload upload)
	{
		return phenotypicDao.searchUpload(upload);
	}

	public Collection<DelimiterType> getDelimiterTypes()
	{
		return phenotypicDao.getDelimiterTypes();
	}

	public Collection<PhenoUpload> searchUploadByCollection(PhenoCollection phenoCollection)
	{
		return phenotypicDao.searchUploadByCollection(phenoCollection);
	}

	public void createPhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload)
	{
		phenotypicDao.createCollectionUpload(phenoCollectionUpload);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
		ah.setComment("Created PhenoCollectionUpload " + phenoCollectionUpload.getId());
		ah.setEntityId(phenoCollectionUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public void deletePhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload)
	{
		phenotypicDao.deleteCollectionUpload(phenoCollectionUpload);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
		ah.setComment("Deleted PhenoCollectionUpload " + phenoCollectionUpload.getId());
		ah.setEntityId(phenoCollectionUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public PhenoCollectionUpload getPhenoCollectionUpload(Long id)
	{
		return phenotypicDao.getPhenoCollectionUpload(id);
	}

	public Collection<PhenoCollectionUpload> searchPhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload)
	{
		return phenotypicDao.searchPhenoCollectionUpload(phenoCollectionUpload);
	}

	public void updatePhenoCollectionUpload(PhenoCollectionUpload phenoCollectionUpload)
	{
		phenotypicDao.updateCollectionUpload(phenoCollectionUpload);
		
		AuditHistory ah = new AuditHistory();
		ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
		ah.setComment("Updated PhenoCollectionUpload " + phenoCollectionUpload.getId());
		ah.setEntityId(phenoCollectionUpload.getId());
		ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_PHENO_COLLECTION_UPLOAD);
		iArkCommonService.createAuditHistory(ah);
	}

	public int getCountOfFieldsInStudy(Study study) {
		return phenotypicDao.getCountOfFieldsInStudy(study);
	}

	public int getCountOfFieldsWithDataInStudy(Study study) {
		return phenotypicDao.getCountOfFieldsWithDataInStudy(study);
	}

	public java.util.Collection<String> validateMatrixPhenoFileFormat(org.apache.wicket.util.file.File file, String fileFormat, char delimiterChar)
	{
		java.util.Collection<String> validationMessages = null;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		
		Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenotypicCollection(sessionCollectionId);
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);
	
		try
		{	
			log.debug("Validating pheno file format");
			InputStream is = new FileInputStream(file);
			validationMessages = pi.validateMatrixPhenoFileFormat(is, file.length());
		}
		catch (IOException ioe)
		{
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe)
		{
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse)
		{
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
		return validationMessages;
	}
	
	public java.util.Collection<String> validateMatrixPhenoFileData(org.apache.wicket.util.file.File file, String fileFormat, char delimiterChar)
	{
		java.util.Collection<String> validationMessages = null;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		
		Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenotypicCollection(sessionCollectionId);
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);
	
		try
		{	
			log.debug("Importing file");
			InputStream is = new FileInputStream(file);
			validationMessages = pi.validateMatrixPhenoFileData(is, file.length());
		}
		catch (IOException ioe)
		{
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe)
		{
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse)
		{
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
		return validationMessages;
	}
		
	public void importPhenotypicDataFile(org.apache.wicket.util.file.File file, String fileFormat, char delimiterChar)
	{
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		
		Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenotypicCollection(sessionCollectionId);	
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);;
	
		try
		{
			InputStream is = new FileInputStream(file);
			
			log.debug("Importing file");
			pi.importMatrixPhenoFile(is, file.length());
		}
		catch (IOException ioe)
		{
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe)
		{
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse)
		{
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
	}
	
	public StringBuffer uploadAndReportPhenotypicDataFile(org.apache.wicket.util.file.File file, String fileFormat, char delimiterChar)
	{
		StringBuffer importReport = null;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		
		Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenotypicCollection(sessionCollectionId);
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);
	
		try
		{
			InputStream is = new FileInputStream(file);
			
			log.debug("Importing file");
			importReport = pi.importAndReportMatrixPhenoFile(is, file.length());
		}
		catch (IOException ioe)
		{
			log.error(Constants.IO_EXCEPTION + ioe);
		}
		catch (FileFormatException ffe)
		{
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse)
		{
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
		return importReport;
	}
	
	public Collection<String> validateMatrixPhenoFileFormat(InputStream inputStream, String fileFormat, char delimiterChar) {
		java.util.Collection<String> validationMessages = null;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		
		Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenotypicCollection(sessionCollectionId);
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);
	
		try
		{	
			log.debug("Validating pheno file format");
			validationMessages = pi.validateMatrixPhenoFileFormat(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe)
		{
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse)
		{
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
		return validationMessages;
	}

	public Collection<String> validateMatrixPhenoFileData(InputStream inputStream, String fileFormat, char delimiterChar) {
		java.util.Collection<String> validationMessages = null;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		
		Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenotypicCollection(sessionCollectionId);
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);
	
		try
		{	
			log.debug("Validating pheno file data");
			validationMessages = pi.validateMatrixPhenoFileData(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe)
		{
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse)
		{
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
		return validationMessages;
	}

	public void importPhenotypicDataFile(InputStream inputStream, String fileFormat, char delimiterChar) {
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		
		Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenotypicCollection(sessionCollectionId);	
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);;
	
		try
		{
			log.debug("Importing pheno data file");
			pi.importMatrixPhenoFile(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe)
		{
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse)
		{
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
	}

	public StringBuffer uploadAndReportPhenotypicDataFile(InputStream inputStream, String fileFormat, char delimiterChar) {
		StringBuffer uploadReport = null;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		study = iArkCommonService.getStudy(studyId);
		
		Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		PhenoCollection phenoCollection = phenotypicDao.getPhenotypicCollection(sessionCollectionId);
		PhenoDataUploader pi = new PhenoDataUploader(this, study, phenoCollection, iArkCommonService, fileFormat, delimiterChar);;
	
		try
		{
			log.info("Importing pheno file");
			uploadReport = pi.importAndReportMatrixPhenoFile(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe)
		{
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (PhenotypicSystemException pse)
		{
			log.error(Constants.PHENOTYPIC_SYSTEM_EXCEPTION + pse);
		}
		return uploadReport;
	}
}