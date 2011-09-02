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
import java.util.Set;

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
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;

/**
 * Collection generated by hbm2java
 */
@Entity
@Table(name = "collection", schema = Constants.LIMS_TABLE_SCHEMA)
public class BioCollection implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7384213608019152409L;
	private Long					id;
	private String					timestamp;
	private String					name;
	private LinkSubjectStudy	linkSubjectStudy;
	private Study					study;
	private Date					collectionDate;
	private Integer				deleted;
	private String					comments;
	private String					hospital;
	private Date					surgeryDate;
	private String					diagCategory;
	private String					refDoctor;
	private Integer				patientage;
	private Date					dischargeDate;
	private String					hospitalUr;
	private Date					diagDate;
	private Integer				collectiongroupId;
	private String					episodeNum;
	private String					episodeDesc;
	private String					collectiongroup;
	private String					tissuetype;
	private String					tissueclass;
	private String					pathlabno;

	public BioCollection() {
	}

	public BioCollection(Long id, String name, LinkSubjectStudy linkSubjectStudy) {
		this.id = id;
		this.name = name;
		this.linkSubjectStudy = linkSubjectStudy;
	}

	public BioCollection(Long id, String name, LinkSubjectStudy linkSubjectStudy, Study study, Date collectionDate, Integer deleted, String comments, String hospital, Date surgeryDate,
			String diagCategory, String refDoctor, Integer patientage, Date dischargeDate, String hospitalUr, Date diagDate, Integer collectiongroupId, String episodeNum, String episodeDesc,
			String collectiongroup, String tissuetype, String tissueclass, String pathlabno, Set<Biospecimen> biospecimens) {
		this.id = id;
		this.name = name;
		this.linkSubjectStudy = linkSubjectStudy;
		this.study = study;
		this.collectionDate = collectionDate;
		this.deleted = deleted;
		this.comments = comments;
		this.hospital = hospital;
		this.surgeryDate = surgeryDate;
		this.diagCategory = diagCategory;
		this.refDoctor = refDoctor;
		this.patientage = patientage;
		this.dischargeDate = dischargeDate;
		this.hospitalUr = hospitalUr;
		this.diagDate = diagDate;
		this.collectiongroupId = collectiongroupId;
		this.episodeNum = episodeNum;
		this.episodeDesc = episodeDesc;
		this.collectiongroup = collectiongroup;
		this.tissuetype = tissuetype;
		this.tissueclass = tissueclass;
		this.pathlabno = pathlabno;
	}

	@Id
	@SequenceGenerator(name = "Collection_PK_Seq", sequenceName = Constants.LIMS_COLLECTION_PK_SEQ)
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Collection_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
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

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return this.linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "COLLECTIONDATE", length = 19)
	public Date getCollectionDate() {
		return this.collectionDate;
	}

	public void setCollectionDate(Date collectionDate) {
		this.collectionDate = collectionDate;
	}

	@Column(name = "DELETED")
	public Integer getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Column(name = "COMMENTS", length = 65535)
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Column(name = "HOSPITAL", length = 50)
	public String getHospital() {
		return this.hospital;
	}

	public void setHospital(String hospital) {
		this.hospital = hospital;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SURGERYDATE", length = 19)
	public Date getSurgeryDate() {
		return this.surgeryDate;
	}

	public void setSurgeryDate(Date surgeryDate) {
		this.surgeryDate = surgeryDate;
	}

	@Column(name = "DIAG_CATEGORY", length = 50)
	public String getDiagCategory() {
		return this.diagCategory;
	}

	public void setDiagCategory(String diagCategory) {
		this.diagCategory = diagCategory;
	}

	@Column(name = "REF_DOCTOR", length = 50)
	public String getRefDoctor() {
		return this.refDoctor;
	}

	public void setRefDoctor(String refDoctor) {
		this.refDoctor = refDoctor;
	}

	@Column(name = "PATIENTAGE")
	public Integer getPatientage() {
		return this.patientage;
	}

	public void setPatientage(Integer patientage) {
		this.patientage = patientage;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DISCHARGEDATE", length = 19)
	public Date getDischargeDate() {
		return this.dischargeDate;
	}

	public void setDischargeDate(Date dischargeDate) {
		this.dischargeDate = dischargeDate;
	}

	@Column(name = "HOSPITAL_UR", length = 50)
	public String getHospitalUr() {
		return this.hospitalUr;
	}

	public void setHospitalUr(String hospitalUr) {
		this.hospitalUr = hospitalUr;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DIAG_DATE", length = 19)
	public Date getDiagDate() {
		return this.diagDate;
	}

	public void setDiagDate(Date diagDate) {
		this.diagDate = diagDate;
	}

	@Column(name = "COLLECTIONGROUP_ID")
	public Integer getCollectiongroupId() {
		return this.collectiongroupId;
	}

	public void setCollectiongroupId(Integer collectiongroupId) {
		this.collectiongroupId = collectiongroupId;
	}

	@Column(name = "EPISODE_NUM", length = 50)
	public String getEpisodeNum() {
		return this.episodeNum;
	}

	public void setEpisodeNum(String episodeNum) {
		this.episodeNum = episodeNum;
	}

	@Column(name = "EPISODE_DESC", length = 50)
	public String getEpisodeDesc() {
		return this.episodeDesc;
	}

	public void setEpisodeDesc(String episodeDesc) {
		this.episodeDesc = episodeDesc;
	}

	@Column(name = "COLLECTIONGROUP", length = 50)
	public String getCollectiongroup() {
		return this.collectiongroup;
	}

	public void setCollectiongroup(String collectiongroup) {
		this.collectiongroup = collectiongroup;
	}

	@Column(name = "TISSUETYPE", length = 50)
	public String getTissuetype() {
		return this.tissuetype;
	}

	public void setTissuetype(String tissuetype) {
		this.tissuetype = tissuetype;
	}

	@Column(name = "TISSUECLASS", length = 50)
	public String getTissueclass() {
		return this.tissueclass;
	}

	public void setTissueclass(String tissueclass) {
		this.tissueclass = tissueclass;
	}

	@Column(name = "PATHLABNO", length = 50)
	public String getPathlabno() {
		return this.pathlabno;
	}

	public void setPathlabno(String pathlabno) {
		this.pathlabno = pathlabno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((linkSubjectStudy == null) ? 0 : linkSubjectStudy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BioCollection other = (BioCollection) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (linkSubjectStudy == null) {
			if (other.linkSubjectStudy != null)
				return false;
		}
		else if (!linkSubjectStudy.equals(other.linkSubjectStudy))
			return false;
		return true;
	}
}
