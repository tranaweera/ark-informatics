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
 * BioTransaction generated by hbm2java
 */
@Entity
@Table(name = "bio_transaction", schema = Constants.LIMS_TABLE_SCHEMA)
public class BioTransaction implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private Long						id;
	private Biospecimen				biospecimen;
	private Date						transactionDate;
	private Double						quantity;
	private String						reason;
	private String						recorder;
	private BioTransactionStatus	status;
	private AccessRequest			accessRequest;

	public BioTransaction() {
	}

	public BioTransaction(Long id, Biospecimen biospecimen, Date transactionDate, Double quantity) {
		this.id = id;
		this.biospecimen = biospecimen;
		this.transactionDate = transactionDate;
		this.quantity = quantity;
	}

	public BioTransaction(Long id, Biospecimen biospecimen, Date transactionDate, Double quantity, 
			String reason, String recorder, BioTransactionStatus status, AccessRequest accessRequest) {
		this.id = id;
		this.biospecimen = biospecimen;
		this.transactionDate = transactionDate;
		this.quantity = quantity;
		this.reason = reason;
		this.recorder = recorder;
		this.status = status;
		this.accessRequest = accessRequest;
	}

	@Id
	@SequenceGenerator(name = "biotransaction_generator", sequenceName = "BIOTRANSACTION_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biotransaction_generator")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_ID")
	public Biospecimen getBiospecimen() {
		return this.biospecimen;
	}

	public void setBiospecimen(Biospecimen biospecimen) {
		this.biospecimen = biospecimen;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRANSACTION_DATE", nullable = false, length = 19)
	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Column(name = "QUANTITY", nullable = false)
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@Column(name = "REASON", length = 65535)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "RECORDER")
	public String getRecorder() {
		return this.recorder;
	}

	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID")
	public BioTransactionStatus getStatus() {
		return status;
	}

	public void setStatus(BioTransactionStatus status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REQUEST_ID")
	public AccessRequest getAccessRequest() {
		return accessRequest;
	}

	public void setAccessRequest(AccessRequest accessRequest) {
		this.accessRequest = accessRequest;
	}

}
