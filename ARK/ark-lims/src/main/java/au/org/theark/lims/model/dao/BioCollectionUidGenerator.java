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
package au.org.theark.lims.model.dao;

/*** 
 * @author cellis
 */

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.model.lims.entity.BioCollectionUidSequence;

@Repository("biocollectionUidGenerator")
public class BioCollectionUidGenerator extends HibernateSessionDao {

	private static Logger		log	= LoggerFactory.getLogger(BioCollectionUidGenerator.class);
	
	/**
	 * get Id and increment sequence table
	 * @param studyNameKey
	 * @return
	 */
	public Serializable getId(String studyNameKey) {
		return getUidAndIncrement(studyNameKey, 1);
	}


	/**
	 * TODO this should use a real fkey
	 * @param studyNameKy
	 * @return
	 */
	public Integer getUidAndIncrement(String studyNameKy, int numToInsert) {
		Criteria criteria = getSession().createCriteria(BioCollectionUidSequence.class);
		criteria.add(Restrictions.eq("studyNameId", studyNameKy));
		BioCollectionUidSequence seqData = (BioCollectionUidSequence) criteria.uniqueResult();
		if(seqData==null){
			log.error("sequence does not exist...creating");
			BioCollectionUidSequence seq = new BioCollectionUidSequence();
			seq.setInsertLock(false);
			seq.setStudyNameId(studyNameKy);
			seq.setUidSequence(numToInsert);
			getSession().persist(seq);
			getSession().flush();
			return new Integer(0);
		}
		else{
			log.warn("so we hav a seq");
			int currentSeqNumber = seqData.getUidSequence();
			seqData.setUidSequence((currentSeqNumber + numToInsert));
			getSession().update(seqData);
			getSession().flush();
			return currentSeqNumber;//TODO ...perhaps this should be handled transactionally in one class, and probably with generators...although this isnt really even a key
		}
	}
}
