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
package au.org.theark.core.dao;

import java.util.List;

import org.hibernate.envers.AuditReader;

import au.org.theark.core.model.audit.entity.AuditEntity;
import au.org.theark.core.model.audit.entity.AuditField;
import au.org.theark.core.model.audit.entity.AuditPackage;
import au.org.theark.core.model.audit.entity.ConsentHistory;
import au.org.theark.core.model.audit.entity.LssConsentHistory;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;


/**
 * Interface that provides CRUD and accessor methods to Audit entities
 * @author cellis
 */
public interface IAuditDao {
	/**
	 * Return a list of LinkSubjectStudy ConsentHistory entities
	 * @param linkSubjectStudy
	 * @return
	 */
	public List<LssConsentHistory> getLssConsentHistoryList(LinkSubjectStudy linkSubjectStudy);
	
	/**
	 * Return a list of ConsentHistory entities
	 * @param consent
	 * @return
	 */
	public List<ConsentHistory> getConsentHistoryList(Consent consent);

	/**
	 * Creates a new audit history log
	 * @param lssConsentHistory
	 */
	public void createLssConsentHistory(LssConsentHistory lssConsentHistory);
	
	/**
	 * Creates a new audit history log
	 * @param consentHistory
	 */
	public void createConsentHistory(ConsentHistory consentHistory);

	public AuditReader getAuditReader();

	public List<AuditEntity> getAuditEntityList();

	public List<AuditPackage> getAuditPackageList();

	public List<AuditField> getAuditFieldList();
}