package au.org.theark.lims.service;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.lims.model.vo.LimsVO;

public interface ILimsService
{
	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can represent
	 * a subject or contact.
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Delete a LIMS collection based on the supplied LimsVO
	 * @param modelObject the LimsVO object
	 */
	public void deleteBioCollection(LimsVO modelObject);

	/**
	 * Update a LIMS collection based on the supplied LimsVO
	 * @param modelObject the LimsVO object
	 */
	public void updateBioCollection(LimsVO modelObject);

	/**
	 * Create a LIMS collection based on the supplied LimsVO
	 * @param modelObject the LimsVO object
	 */
	public void createBioCollection(LimsVO modelObject);

	/**
	 * Search the database for a list of BioCollections based on the supplied BioCollection
	 * @param bioCollection the bioCollection object to be matched against
	 * @return List of BioCollections
	 * @throws ArkSystemException
	 */
	public List<au.org.theark.core.model.lims.entity.BioCollection> searchBioCollection(au.org.theark.core.model.lims.entity.BioCollection bioCollection) throws ArkSystemException;

	/**
	 * Search the database for a BioCollection based on the supplied id
	 * @param id the unique id of the BioCollection
	 * @return BioCollection
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public au.org.theark.core.model.lims.entity.BioCollection getBioCollection(Long id) throws EntityNotFoundException, ArkSystemException;
	
	/**
	 * Look up a LIMS biospecimen based on the supplied Long id that represents the primary key
	 * @param id
	 * @return Biospecimen
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Biospecimen getBiospecimen(Long id) throws EntityNotFoundException, ArkSystemException;
	
	/**
	 * Look up a List of LIMS Biospecimen(s) based on the supplied biospecimen object
	 * @param biospecimen
	 * @return List<au.org.theark.core.model.lims.entity.Biospecimen>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<au.org.theark.core.model.lims.entity.Biospecimen> searchBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen) throws ArkSystemException;
	
	/**
	 * Create a LIMS biospecimen based on the supplied LimsVO
	 * @param modelObject the LimsVO object
	 */
	public void createBiospecimen(LimsVO modelObject);
	
	/**
	 * Update a LIMS biospecimen based on the supplied LimsVO
	 * @param modelObject the LimsVO object
	 */
	public void updateBiospecimen(LimsVO modelObject);
	
	/**
	 * Delete a LIMS biospecimen based on the supplied LimsVO
	 * @param modelObject the LimsVO object
	 */
	public void deleteBiospecimen(LimsVO modelObject);
	
	/**
	 * Look up a LIMS bioTransaction based on the supplied Long id that represents the primary key
	 * @param id
	 * @return BioTransaction
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public BioTransaction getBioTransaction(Long id) throws EntityNotFoundException, ArkSystemException;
	
	/**
	 * Look up a List of LIMS BioTransaction(s) based on the supplied bioTransaction object
	 * @param bioTransaction
	 * @return List<au.org.theark.core.model.lims.entity.BioTransaction>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<au.org.theark.core.model.lims.entity.BioTransaction> searchBioTransaction(au.org.theark.core.model.lims.entity.BioTransaction bioTransaction) throws ArkSystemException;
	
	/**
	 * Create a LIMS bioTransaction based on the supplied LimsVO
	 * @param modelObject the LimsVO object
	 */
	public void createBioTransaction(LimsVO modelObject);
	
	/**
	 * Update a LIMS bioTransaction based on the supplied LimsVO
	 * @param modelObject the LimsVO object
	 */
	public void updateBioTransaction(LimsVO modelObject);
	
	/**
	 * Delete a LIMS bioTransaction based on the supplied LimsVO
	 * @param modelObject the LimsVO object
	 */
	public void deleteBioTransaction(LimsVO modelObject);

	/**
	 * Get a list of all sampleTypes
	 * @return List
	 */
	public List<BioSampletype> getBioSampleTypes();

	/**
	 * Determine if provided linkSubjectStudy has any BioCollections associated
	 * @return true if provided linkSubjectStudy has one or more BioCollections
	 */
	public Boolean hasBioCollections(LinkSubjectStudy linkSubjectStudy);

}
