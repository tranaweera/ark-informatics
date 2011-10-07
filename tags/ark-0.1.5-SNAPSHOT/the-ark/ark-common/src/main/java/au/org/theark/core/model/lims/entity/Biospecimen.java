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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;

/**
 * Biospecimen generated by hbm2java
 */
@Entity
@Table(name = "biospecimen", schema = Constants.LIMS_TABLE_SCHEMA)
public class Biospecimen implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= -6811160809915149538L;
	private Long						id;
	private String						timestamp;
	private BioCollection			bioCollection;
	private String						biospecimenUid;
	private Study						study;
	private Long						substudyId;
	private LinkSubjectStudy		linkSubjectStudy;
	private Long						parentId;
	private String						parentUid;
	private Long						oldId;
	private Boolean					deleted;
	private String						otherid;
	private String						storedIn;
	private Date						sampleTime;
	private String						grade;
	private Long						depth;
	private Date						sampleDate;
	private Date						extractedTime;
	private String						location;
	private BioSampletype			sampleType;
	private String						species;
	private Double						qtyCollected;
	private Date						dateextracted;
	private Double						qtyRemoved;
	private Double						gestat;
	private String						comments;
	private Date						datedistributed;
	private String						collaborator;
	private Double						dnaconc;
	private Double						purity;
	private String						anticoag;
	private String						protocol;
	private Long						dnaBank;
	private Double						quantity;
	private Unit						unit;
	private String						quality;
	private Long						withdrawn;
	private String						status;
	private Boolean					barcoded;
	private TreatmentType			treatmentType;
	
	private Set<BioTransaction>	bioTransactions	= new HashSet<BioTransaction>(0);

	public Biospecimen() {
	}

	public Biospecimen(Long id, String biospecimenUid, Long sampletypeId, BioSampletype sampleType, TreatmentType treatmentType) {
		this.id = id;
		this.biospecimenUid = biospecimenUid;
		this.sampleType = sampleType;
		this.treatmentType = treatmentType;
	}

	public Biospecimen(Long id, BioCollection bioCollection, String biospecimenUid, Study study, Long substudyId, LinkSubjectStudy linkSubjectStudy, Long parentId, String parentUid, Long oldId,
			Boolean deleted, String otherid, String storedIn, Date sampleTime, String grade, InvCell invCell, Long depth, Date sampleDate, Date extractedTime, String location, Long sampleTypeId,
			BioSampletype sampleType, String samplesubtype, String subtypedesc, String species, Double qtyCollected, Date dateextracted, Double qtyRemoved, Double gestat, String comments,
			Date datedistributed, String collaborator, Double dnaconc, Double purity, String anticoag, String protocol, Long dnaBank, Double quantity, Unit unit, String quality, Long withdrawn,
			String status, Boolean barcoded, TreatmentType treatmentType, Set<BioTransaction> bioTransactions) {
		this.id = id;
		this.bioCollection = bioCollection;
		this.biospecimenUid = biospecimenUid;
		this.study = study;
		this.substudyId = substudyId;
		this.linkSubjectStudy = linkSubjectStudy;
		this.parentId = parentId;
		this.parentUid = parentUid;
		this.oldId = oldId;
		this.deleted = deleted;
		this.otherid = otherid;
		this.storedIn = storedIn;
		this.sampleTime = sampleTime;
		this.grade = grade;
		this.depth = depth;
		this.sampleDate = sampleDate;
		this.extractedTime = extractedTime;
		this.location = location;
		this.sampleType = sampleType;
		this.species = species;
		this.qtyCollected = qtyCollected;
		this.dateextracted = dateextracted;
		this.qtyRemoved = qtyRemoved;
		this.gestat = gestat;
		this.comments = comments;
		this.datedistributed = datedistributed;
		this.collaborator = collaborator;
		this.dnaconc = dnaconc;
		this.purity = purity;
		this.anticoag = anticoag;
		this.protocol = protocol;
		this.dnaBank = dnaBank;
		this.quantity = quantity;
		this.unit = unit;
		this.quality = quality;
		this.withdrawn = withdrawn;
		this.status = status;
		this.barcoded = barcoded;
		this.treatmentType = treatmentType;
	}

	@Id
	@SequenceGenerator(name = "biospecimen_generator", sequenceName = "BIOSPECIMEN_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biospecimen_generator")
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID")
	public BioCollection getBioCollection() {
		return this.bioCollection;
	}

	public void setBioCollection(BioCollection bioCollection) {
		this.bioCollection = bioCollection;
	}

	@Column(name = "BIOSPECIMEN_ID", nullable = false)
	public String getBiospecimenUid() {
		return this.biospecimenUid;
	}

	public void setBiospecimenUid(String biospecimenUid) {
		this.biospecimenUid = biospecimenUid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@Column(name = "SUBSTUDY_ID")
	public Long getSubstudyId() {
		return this.substudyId;
	}

	public void setSubstudyId(Long substudyId) {
		this.substudyId = substudyId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy() {
		return this.linkSubjectStudy;
	}

	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy) {
		this.linkSubjectStudy = linkSubjectStudy;
	}

	@Column(name = "PARENT_ID")
	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Column(name = "PARENTID", length = 50)
	public String getParentUid() {
		return this.parentUid;
	}

	public void setParentUid(String parentUid) {
		this.parentUid = parentUid;
	}

	@Column(name = "OLD_ID")
	public Long getOldId() {
		return this.oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	@Column(name = "DELETED")
	public Boolean getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	@Column(name = "OTHERID", length = 50)
	public String getOtherid() {
		return this.otherid;
	}

	public void setOtherid(String otherid) {
		this.otherid = otherid;
	}

	@Column(name = "STORED_IN", length = 50)
	public String getStoredIn() {
		return this.storedIn;
	}

	public void setStoredIn(String storedIn) {
		this.storedIn = storedIn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAMPLE_TIME", length = 19)
	public Date getSampleTime() {
		return this.sampleTime;
	}

	public void setSampleTime(Date sampleTime) {
		this.sampleTime = sampleTime;
	}

	@Column(name = "GRADE")
	public String getGrade() {
		return this.grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Column(name = "DEPTH")
	public Long getDepth() {
		return this.depth;
	}

	public void setDepth(Long depth) {
		this.depth = depth;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAMPLEDATE", length = 19)
	public Date getSampleDate() {
		return this.sampleDate;
	}

	public void setSampleDate(Date sampleDate) {
		this.sampleDate = sampleDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXTRACTED_TIME", length = 19)
	public Date getExtractedTime() {
		return this.extractedTime;
	}

	public void setExtractedTime(Date extractedTime) {
		this.extractedTime = extractedTime;
	}

	@Column(name = "LOCATION")
	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAMPLETYPE_ID")
	public BioSampletype getSampleType() {
		return this.sampleType;
	}

	public void setSampleType(BioSampletype sampleType) {
		this.sampleType = sampleType;
	}

	@Column(name = "SPECIES")
	public String getSpecies() {
		return this.species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	@Column(name = "QTY_COLLECTED", precision = 22, scale = 0)
	public Double getQtyCollected() {
		return this.qtyCollected;
	}

	public void setQtyCollected(Double qtyCollected) {
		this.qtyCollected = qtyCollected;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATEEXTRACTED", length = 19)
	public Date getDateextracted() {
		return this.dateextracted;
	}

	public void setDateextracted(Date dateextracted) {
		this.dateextracted = dateextracted;
	}

	@Column(name = "QTY_REMOVED", precision = 22, scale = 0)
	public Double getQtyRemoved() {
		return this.qtyRemoved;
	}

	public void setQtyRemoved(Double qtyRemoved) {
		this.qtyRemoved = qtyRemoved;
	}

	@Column(name = "GESTAT", precision = 22, scale = 0)
	public Double getGestat() {
		return this.gestat;
	}

	public void setGestat(Double gestat) {
		this.gestat = gestat;
	}

	@Column(name = "COMMENTS", length = 65535)
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATEDISTRIBUTED", length = 19)
	public Date getDatedistributed() {
		return this.datedistributed;
	}

	public void setDatedistributed(Date datedistributed) {
		this.datedistributed = datedistributed;
	}

	@Column(name = "COLLABORATOR", length = 100)
	public String getCollaborator() {
		return this.collaborator;
	}

	public void setCollaborator(String collaborator) {
		this.collaborator = collaborator;
	}

	@Column(name = "DNACONC", precision = 22, scale = 0)
	public Double getDnaconc() {
		return this.dnaconc;
	}

	public void setDnaconc(Double dnaconc) {
		this.dnaconc = dnaconc;
	}

	@Column(name = "PURITY", precision = 22, scale = 0)
	public Double getPurity() {
		return this.purity;
	}

	public void setPurity(Double purity) {
		this.purity = purity;
	}

	@Column(name = "ANTICOAG", length = 100)
	public String getAnticoag() {
		return this.anticoag;
	}

	public void setAnticoag(String anticoag) {
		this.anticoag = anticoag;
	}

	@Column(name = "PROTOCOL", length = 100)
	public String getProtocol() {
		return this.protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	@Column(name = "DNA_BANK")
	public Long getDnaBank() {
		return this.dnaBank;
	}

	public void setDnaBank(Long dnaBank) {
		this.dnaBank = dnaBank;
	}

	@Column(name = "QUANTITY")
	public Double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNIT_ID")
	public Unit getUnit() {
		return this.unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	@Column(name = "QUALITY", length = 100)
	public String getQuality() {
		return this.quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	@Column(name = "WITHDRAWN")
	public Long getWithdrawn() {
		return this.withdrawn;
	}

	public void setWithdrawn(Long withdrawn) {
		this.withdrawn = withdrawn;
	}

	@Column(name = "STATUS", length = 20)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the barcoded
	 */
	@Column(name = "BARCODED")
	public Boolean getBarcoded() {
		return barcoded;
	}

	/**
	 * @param barcoded the barcoded to set
	 */
	public void setBarcoded(Boolean barcoded) {
		this.barcoded = barcoded;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TREATMENT_TYPE_ID")
	public TreatmentType getTreatmentType() {
		return this.treatmentType;
	}

	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "biospecimen")
	public Set<BioTransaction> getBioTransactions() {
		return this.bioTransactions;
	}

	public void setBioTransactions(Set<BioTransaction> bioTransactions) {
		this.bioTransactions = bioTransactions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bioCollection == null) ? 0 : bioCollection.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Biospecimen other = (Biospecimen) obj;
		if (bioCollection == null) {
			if (other.bioCollection != null)
				return false;
		}
		else if (!bioCollection.equals(other.bioCollection))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
}
