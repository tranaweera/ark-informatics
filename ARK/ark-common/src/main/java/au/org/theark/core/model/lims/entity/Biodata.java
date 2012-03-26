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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;

/**
 * Biodata generated by hbm2java
 */
@Entity
@Table(name = "biodata", schema = Constants.LIMS_TABLE_SCHEMA)
public class Biodata implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long			id;
	private BiodataField	biodataField;
	private Integer		domainId;
	private Date			dateCollected;
	private String			stringValue;
	private Integer		numberValue;
	private Date			dateValue;

	public Biodata() {
	}

	public Biodata(Long id, BiodataField biodataField, Date dateCollected) {
		this.id = id;
		this.biodataField = biodataField;
		this.dateCollected = dateCollected;
	}

	public Biodata(Long id, BiodataField biodataField, Integer domainId, Date dateCollected, String stringValue, Integer numberValue, Date dateValue) {
		this.id = id;
		this.biodataField = biodataField;
		this.domainId = domainId;
		this.dateCollected = dateCollected;
		this.stringValue = stringValue;
		this.numberValue = numberValue;
		this.dateValue = dateValue;
	}

	@Id
	@SequenceGenerator(name = "biodata_generator", sequenceName = "BIODATA_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biodata_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_ID", nullable = false)
	public BiodataField getBiodataField() {
		return this.biodataField;
	}

	public void setBiodataField(BiodataField biodataField) {
		this.biodataField = biodataField;
	}

	@Column(name = "DOMAIN_ID")
	public Integer getDomainId() {
		return this.domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_COLLECTED", nullable = false, length = 19)
	public Date getDateCollected() {
		return this.dateCollected;
	}

	public void setDateCollected(Date dateCollected) {
		this.dateCollected = dateCollected;
	}

	@Column(name = "STRING_VALUE", length = 65535)
	public String getStringValue() {
		return this.stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	@Column(name = "NUMBER_VALUE")
	public Integer getNumberValue() {
		return this.numberValue;
	}

	public void setNumberValue(Integer numberValue) {
		this.numberValue = numberValue;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE_VALUE", length = 19)
	public Date getDateValue() {
		return this.dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

}
