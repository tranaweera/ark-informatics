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
package au.org.theark.lims.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import au.org.theark.core.model.lims.entity.Biospecimen;

/**
 * Represents a Batch of Biospecimen's one VO.
 * 
 * @author cellis
 * 
 */
public class BatchBiospecimenVO implements Serializable {
	private static final long	serialVersionUID	= 1L;
	protected int				numberToCreate;
	protected Biospecimen	biospecimen;
	protected List<BatchBiospecimenVO> batchBiospecimenList = new ArrayList<BatchBiospecimenVO>(1);
	
	/**
	 * @return the numberToCreate
	 */
	public int getNumberToCreate() {
		return numberToCreate;
	}
	/**
	 * @param numberToCreate the numberToCreate to set
	 */
	public void setNumberToCreate(int numberToCreate) {
		this.numberToCreate = numberToCreate;
	}
	/**
	 * @return the biospecimen
	 */
	public Biospecimen getBiospecimen() {
		return biospecimen;
	}
	/**
	 * @param biospecimen the biospecimen to set
	 */
	public void setBiospecimen(Biospecimen biospecimen) {
		this.biospecimen = biospecimen;
	}
	/**
	 * @return the batchBiospecimenList
	 */
	public List<BatchBiospecimenVO> getBatchBiospecimenList() {
		return batchBiospecimenList;
	}
	/**
	 * @param batchBiospecimenList the batchBiospecimenList to set
	 */
	public void setBatchBiospecimenList(List<BatchBiospecimenVO> batchBiospecimenList) {
		this.batchBiospecimenList = batchBiospecimenList;
	}	
}
