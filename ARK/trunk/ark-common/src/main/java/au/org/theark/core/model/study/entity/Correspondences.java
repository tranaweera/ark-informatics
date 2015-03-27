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
package au.org.theark.core.model.study.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.Constants;
import au.org.theark.core.model.worktracking.entity.BillableItem;

@Entity
@Table(name = "correspondences", schema = Constants.STUDY_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Correspondences implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	//private Person person;
	private LinkSubjectStudy lss;
	private ArkUser operator;
	private Date date;
	private String time;
	private String reason;
	private CorrespondenceModeType correspondenceModeType;
	private CorrespondenceDirectionType correspondenceDirectionType;
	private CorrespondenceOutcomeType correspondenceOutcomeType;
	private String details;
	private String comments;
	private String attachmentFilename;
	private byte[] attachmentPayload;
	
	private String attachementChecksum;
	private String attachementFileId;
	
	private BillableItem billableItem; 

	@Id
	@SequenceGenerator(name = "correspondences_generator", sequenceName = "CORRESPONDENCES_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "correspondences_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLss() {
		return lss;
	}

	public void setLss(LinkSubjectStudy lss) {
		this.lss = lss;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ARK_USER_ID")
	public ArkUser getOperator() {
		return operator;
	}

	public void setOperator(ArkUser operator) {
		this.operator = operator;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE", length = 7)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "TIME", length = 255)
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Column(name = "REASON", length = 4096)
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MODE_TYPE_ID")
	public CorrespondenceModeType getCorrespondenceModeType() {
		return correspondenceModeType;
	}

	public void setCorrespondenceModeType(
			CorrespondenceModeType correspondenceModeType) {
		this.correspondenceModeType = correspondenceModeType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DIRECTION_TYPE_ID")
	public CorrespondenceDirectionType getCorrespondenceDirectionType() {
		return correspondenceDirectionType;
	}

	public void setCorrespondenceDirectionType(
			CorrespondenceDirectionType correspondenceDirectionType) {
		this.correspondenceDirectionType = correspondenceDirectionType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "OUTCOME_TYPE_ID")
	public CorrespondenceOutcomeType getCorrespondenceOutcomeType() {
		return correspondenceOutcomeType;
	}

	public void setCorrespondenceOutcomeType(
			CorrespondenceOutcomeType correspondenceOutcomeType) {
		this.correspondenceOutcomeType = correspondenceOutcomeType;
	}

	@Column(name = "DETAILS", length = 4096)
	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	@Column(name = "COMMENTS", length = 4096)
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "ATTACHMENT_FILENAME", length = 255)
	public String getAttachmentFilename() {
		return attachmentFilename;
	}

	public void setAttachmentFilename(String attachmentFilename) {
		this.attachmentFilename = attachmentFilename;
	}

	@Lob
	@Column(name = "ATTACHMENT_PAYLOAD")
	public byte[] getAttachmentPayload() {
		return attachmentPayload;
	}

	public void setAttachmentPayload(byte[] attachmentPayload) {
		this.attachmentPayload = attachmentPayload;
	}

	@OneToOne
	@JoinColumn(name = "BILLABLE_ITEM_ID")
	public BillableItem getBillableItem() {
		return billableItem;
	}

	public void setBillableItem(BillableItem billableItem) {
		this.billableItem = billableItem;
	}

	@Column(name = "ATTACHMENT_CHECKSUM")
	public String getAttachementChecksum() {
		return attachementChecksum;
	}

	public void setAttachementChecksum(String attachementChecksum) {
		this.attachementChecksum = attachementChecksum;
	}

	@Column(name = "ATTACHMENT_FILE_ID")
	public String getAttachementFileId() {
		return attachementFileId;
	}

	public void setAttachementFileId(String attachementFileId) {
		this.attachementFileId = attachementFileId;
	}
}
