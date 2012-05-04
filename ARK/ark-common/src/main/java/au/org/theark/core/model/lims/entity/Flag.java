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
package au.org.theark.core.model.lims.entity;

// Generated 15/06/2011 1:22:58 PM by Hibernate Tools 3.3.0.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * Flag generated by hbm2java
 */
@Entity
@Table(name = "flag", schema = Constants.LIMS_TABLE_SCHEMA)
public class Flag implements java.io.Serializable {


	private static final long serialVersionUID = 1L;
	private Long		id;
	private String		timestamp;
	private Integer	deleted;
	private String		domain;
	private int			referenceId;
	private String		user;

	public Flag() {
	}

	public Flag(Long id, String domain, int referenceId, String user) {
		this.id = id;
		this.domain = domain;
		this.referenceId = referenceId;
		this.user = user;
	}

	public Flag(Long id, Integer deleted, String domain, int referenceId, String user) {
		this.id = id;
		this.deleted = deleted;
		this.domain = domain;
		this.referenceId = referenceId;
		this.user = user;
	}

	@Id
	@SequenceGenerator(name = "flag_generator", sequenceName = "FLAG_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "flag_generator")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TIMESTAMP", length = 55)
	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Column(name = "DELETED")
	public Integer getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Column(name = "DOMAIN", nullable = false, length = 50)
	public String getDomain() {
		return this.domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Column(name = "REFERENCE_ID", nullable = false)
	public int getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(int referenceId) {
		this.referenceId = referenceId;
	}

	@Column(name = "USER", nullable = false, length = 100)
	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
