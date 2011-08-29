/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.lims.service;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioCollectionCustomFieldData;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.lims.model.vo.LimsVO;

public interface ILimsService {
	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can
	 * represent a subject or contact.
	 * 
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Delete a LIMS collection based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteBioCollection(LimsVO modelObject);

	/**
	 * Update a LIMS collection based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateBioCollection(LimsVO modelObject);

	/**
	 * Create a LIMS collection based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createBioCollection(LimsVO modelObject);

	/**
	 * Search the database for a list of BioCollections based on the supplied BioCollection
	 * 
	 * @param bioCollection
	 *           the bioCollection object to be matched against
	 * @return List of BioCollections
	 * @throws ArkSystemException
	 */
	public List<au.org.theark.core.model.lims.entity.BioCollection> searchBioCollection(au.org.theark.core.model.lims.entity.BioCollection bioCollection) throws ArkSystemException;

	/**
	 * Search the database for a BioCollection based on the supplied id
	 * 
	 * @param id
	 *           the unique id of the BioCollection
	 * @return BioCollection
	 * @throws EntityNotFoundException
	 */
	public au.org.theark.core.model.lims.entity.BioCollection getBioCollection(Long id) throws EntityNotFoundException;

	/**
	 * Look up a LIMS biospecimen based on the supplied Long id that represents the primary key
	 * 
	 * @param id
	 * @return Biospecimen
	 * @throws EntityNotFoundException
	 */
	public Biospecimen getBiospecimen(Long id) throws EntityNotFoundException;

	/**
	 * Look up a List of LIMS Biospecimen(s) based on the supplied biospecimen object
	 * 
	 * @param biospecimen
	 * @return List<au.org.theark.core.model.lims.entity.Biospecimen>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<au.org.theark.core.model.lims.entity.Biospecimen> searchBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen) throws ArkSystemException;

	/**
	 * Create a LIMS biospecimen based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createBiospecimen(LimsVO modelObject);

	/**
	 * Update a LIMS biospecimen based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateBiospecimen(LimsVO modelObject);

	/**
	 * Delete a LIMS biospecimen based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteBiospecimen(LimsVO modelObject);

	/**
	 * Look up a LIMS bioTransaction based on the supplied Long id that represents the primary key
	 * 
	 * @param id
	 * @return BioTransaction
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public BioTransaction getBioTransaction(Long id) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Look up a List of LIMS BioTransaction(s) based on the supplied bioTransaction object
	 * 
	 * @param bioTransaction
	 * @return List<au.org.theark.core.model.lims.entity.BioTransaction>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<au.org.theark.core.model.lims.entity.BioTransaction> searchBioTransaction(au.org.theark.core.model.lims.entity.BioTransaction bioTransaction) throws ArkSystemException;

	/**
	 * Create a LIMS bioTransaction based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void createBioTransaction(LimsVO modelObject);

	/**
	 * Update a LIMS bioTransaction based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void updateBioTransaction(LimsVO modelObject);

	/**
	 * Delete a LIMS bioTransaction based on the supplied LimsVO
	 * 
	 * @param modelObject
	 *           the LimsVO object
	 */
	public void deleteBioTransaction(LimsVO modelObject);

	/**
	 * Get a list of all sampleTypes
	 * 
	 * @return List
	 */
	public List<BioSampletype> getBioSampleTypes();

	/**
	 * Determine if provided linkSubjectStudy has any BioCollections associated
	 * 
	 * @return true if provided linkSubjectStudy has one or more BioCollections
	 */
	public Boolean hasBioCollections(LinkSubjectStudy linkSubjectStudy);

	/**
	 * Determine if provided bioCollection has any biospecimens associated
	 * 
	 * @return true if provided bioCollection has one or more Biospecimens
	 */
	public Boolean hasBiospecimens(BioCollection bioCollection);

	/**
	 * Get count of the BioCollections given the criteria
	 * 
	 * @param BioCollection
	 *           criteria
	 * @return counts
	 */
	public int getBioCollectionCount(BioCollection bioCollectionCriteria);

	/**
	 * A generic interface that will return a list BioCollections specified by a particular criteria, and a paginated reference point
	 * 
	 * @param BioCollection
	 *           criteria
	 * @return Collection of BioCollection
	 */
	public List<BioCollection> searchPageableBioCollections(BioCollection bioCollectionCriteria, int first, int count);

	/**
	 * Get count of the Biospecimens given the criteria
	 * 
	 * @param Biospecimens
	 *           criteria
	 * @return counts
	 */
	public int getBiospecimenCount(Biospecimen biospecimenCriteria);

	/**
	 * A generic interface that will return a list Biospecimens specified by a particular criteria, and a paginated reference point
	 * 
	 * @param Biospecimens
	 *           criteria
	 * @return Collection of Biospecimen
	 */
	public List<Biospecimen> searchPageableBiospecimens(Biospecimen biospecimenCriteria, int first, int count);

	/**
	 * Get a Biospecimen entity based on a specified BiospecimenUid
	 * @param biospecimenUid
	 * @return
	 */
	public Biospecimen getBiospecimenByUid(String biospecimenUid);

	public int getBioCollectionCustomFieldDataCount(BioCollection criteria, ArkFunction arkFunction);
	
	public List<BioCollectionCustomFieldData> getBioCollectionCustomFieldDataList(BioCollection bioCollectionCriteria, ArkFunction arkFunction, int first, int count);
	
	/**
	 * Allows to Save(Insert) or Update  BioCollectionCustomFieldData. If there are BioCollectionCustomFieldData
	 * with no data value then it will discard it from the save/update process.
	 * @param bioCollectionCFDataList - List of BioCollectionCustomFieldData to commit to database.
	 * @return a List of BioCollectionCustomFieldData that failed to save (Hibernate caught some exception).
	 */
	public List<BioCollectionCustomFieldData> createOrUpdateBioCollectionCustomFieldData(List<BioCollectionCustomFieldData> bioCollectionCFDataList);

	/**
	 * Get the count of biospecimen(s) based on a LimsVO criteria
	 * @param limsVo
	 * @return
	 */
	public int getBiospecimenCount(LimsVO limsVo);

	/**
	 * Get a list of Biospecimen(s) based on a LimsVO criteria
	 * @param limsVo
	 * @param first
	 * @param count
	 * @return
	 */
	public List<Biospecimen> searchPageableBiospecimens(LimsVO limsVo, int first, int count);

}
