package au.org.theark.lims.model.dao;

import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.lims.entity.Biospecimen;

public interface IBiospecimenDao
{
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
	 * Create a LIMS Biospecimen based on the supplied biospecimen
	 * @param modelObject
	 */
	public void createBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen);
	
	/**
	 * Update a LIMS Biospecimen based on the supplied biospecimen
	 * @param modelObject
	 */
	public void updateBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen);
	
	/**
	 * Delete a LIMS Biospecimen based on the supplied biospecimen
	 * @param modelObject
	 */
	public void deleteBiospecimen(au.org.theark.core.model.lims.entity.Biospecimen biospecimen);

}
