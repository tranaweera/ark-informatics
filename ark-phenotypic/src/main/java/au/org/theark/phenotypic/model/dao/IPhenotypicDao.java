package au.org.theark.phenotypic.model.dao;

import java.util.Date;

import org.hibernate.Session;

import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.phenotypic.model.entity.CollectionImport;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldDataLog;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.entity.Status;
import au.org.theark.phenotypic.model.entity.Upload;
import au.org.theark.phenotypic.model.entity.UploadCollection;

/**
 * Interface for all select/insert/update/delete operations on the backend database.
 * 
 */
public interface IPhenotypicDao {

	// Collection
	public PhenoCollection getPhenotypicCollection(Long id);
	public java.util.Collection<PhenoCollection> getPhenotypicCollection();
	public java.util.Collection<PhenoCollection> searchPhenotypicCollection(PhenoCollection collectionToMatch);
	public void createPhenoCollection(PhenoCollection collection);
	public void updatePhenoCollection(PhenoCollection collection);
	public void deletePhenoCollection(PhenoCollection collection);
	
	// Collection Import
	public CollectionImport getCollectionImport(Long id);
	public java.util.Collection<CollectionImport> getCollectionImport();
	public java.util.Collection<CollectionImport> searchCollectionImport(CollectionImport collectionImportToMatch);
	public void createCollectionImport(CollectionImport collectionImport);
	public void updateCollectionImport(CollectionImport collectionImport);
	public void deleteCollectionImport(CollectionImport collectionImport);
	
	// Status
	public Status getStatus(Long statusId);
	public Status getStatusByName(String statusName);
	public java.util.Collection<Status> getStatus();
	public void createStatus(Status status);
	public void updateStatus(Status status);
	
	// Field
	public Field getField(Long fieldId);
	public java.util.Collection<Field> getField();
	public Field getFieldByNameAndStudy(String fieldName, Study study);
	public java.util.Collection<Field> searchField(Field field);
	public void createField(Field field);
	public void updateField(Field field);
	public void deleteField(Field field);
	
	// Field Type
	public FieldType getFieldType(Long id);
	public FieldType getFieldTypeByName(String fieldTypeName);
	public java.util.Collection<FieldType> getFieldTypes();
	public void createFieldType(FieldType fieldType);
	public void updateFieldType(FieldType fieldType);

	// Field Data
	public FieldData getFieldData(Long id);
	public java.util.Collection<FieldData> getFieldDataByCollectionAndField(PhenoCollection phenoCollection, Field field);
	public FieldData getFieldData(PhenoCollection phenoCollection, LinkSubjectStudy linkSubjectStudy, Field field, Date dateCollected, String value);
	public void createFieldData(FieldData fieldData);
	public void updateFieldData(FieldData fieldData);
	public void deleteFieldData(FieldData fieldData);
	
	// Field Data Log
	public FieldDataLog getFieldDataLog(Long id);
	public java.util.Collection<FieldDataLog> getFieldDataLogByField(Field field);
	public void createFieldDataLog(FieldDataLog fieldDataLog);
	
	// Upload
	public Upload getUpload(Long id);
	public java.util.Collection<Upload> getUploadByFileName(String fileName);
	public void createUpload(Upload upload);
	public void updateUpload(Upload upload);
	public void deleteUpload(Upload upload);
	
	// Upload Collection
	public UploadCollection getUploadCollection(Long id);
	public java.util.Collection<UploadCollection> getUploadCollectionByCollection(PhenoCollection phenoCollection);
	public void createUploadCollection(UploadCollection uploadCollection);
	public void updateUploadCollection(UploadCollection uploadCollection);
	public void deleteUploadCollection(UploadCollection uploadCollection);
}
