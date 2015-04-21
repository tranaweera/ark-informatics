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
import java.util.LinkedList;
import java.util.List;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;

import au.org.theark.core.audit.annotations.ArkAuditDisplay;
import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;

/**
 * Biospecimen generated by hbm2java
 */
@Entity
@Table(name = "biospecimen", schema = Constants.LIMS_TABLE_SCHEMA)
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Biospecimen implements java.io.Serializable {


	private static final long			serialVersionUID	= -6811160809915149538L;
	private Long							id;
	private String							biospecimenUid;
	private Study							study;
	private Long							substudyId;
	private LinkSubjectStudy			linkSubjectStudy;
	private BioCollection				bioCollection;
	private BioSampletype				sampleType;
	private InvCell						invCell;
	private String							parentUid;
	private Long							oldId;
	private Boolean						deleted				= false;
	private String							timestamp;
	private String							otherid;
	private BiospecimenStorage			storedIn;
	private Long							depth;
	private BiospecimenGrade			grade;
	private Date							sampleDate;
	private Date							sampleTime;
	private Date							processedDate;
	private Date							processedTime;
	private BiospecimenSpecies			species;
	private Double							qtyCollected;
	private Double							qtyRemoved;
	private String							comments;
	private Double							quantity;
	private Unit							unit;
	private TreatmentType				treatmentType;
	private Boolean						barcoded				= false;
	private BiospecimenQuality			quality;
	private BiospecimenAnticoagulant	anticoag;
	private BiospecimenStatus			status;
	private Double							concentration;
	private Double							amount;
	private BiospecimenProtocol		biospecimenProtocol;
	private Double							purity;
	

	private Set<BioTransaction>		bioTransactions	= new HashSet<BioTransaction>(0);

	private Biospecimen parent;
	private List<Biospecimen> children = new LinkedList<Biospecimen>();

	public Biospecimen() {
	}

	public Biospecimen(Long id, String biospecimenUid, Long sampletypeId, BioSampletype sampleType, TreatmentType treatmentType) {
		this.id = id;
		this.biospecimenUid = biospecimenUid;
		this.sampleType = sampleType;
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
	
	//TODO ASAP invetigate onetoone....relationships should work still because there will only be one   --- NOW FIGURE OUT CASCADING RULES!!!!
	//@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "biospecimen")
	
	

	@OneToOne(fetch = FetchType.LAZY, mappedBy="biospecimen")
	public InvCell getInvCell() {
		return this.invCell;
	}

	
	public void setInvCell(InvCell invCell) {
		this.invCell = invCell;
	}

	
	//TODO:  evaluate EAGER vs LAZY
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "BIOCOLLECTION_ID")
	@NotAudited
	public BioCollection getBioCollection() {
		return this.bioCollection;
	}

	public void setBioCollection(BioCollection bioCollection) {
		this.bioCollection = bioCollection;
	}

	@ArkAuditDisplay
	@Column(name = "BIOSPECIMEN_UID", nullable = false)
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAMPLETYPE_ID")
	public BioSampletype getSampleType() {
		return this.sampleType;
	}

	public void setSampleType(BioSampletype sampleType) {
		this.sampleType = sampleType;
	}

//	@Column(name = "PARENT_ID")
//	public Long getParentId() {
//		return this.parentId;
//	}
//
//	public void setParentId(Long parentId) {
//		this.parentId = parentId;
//	}

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_STORAGE_ID")
	public BiospecimenStorage getStoredIn() {
		return this.storedIn;
	}

	public void setStoredIn(BiospecimenStorage storedIn) {
		this.storedIn = storedIn;
	}

	@Column(name = "DEPTH")
	public Long getDepth() {
		return this.depth;
	}

	public void setDepth(Long depth) {
		this.depth = depth;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_GRADE_ID")
	public BiospecimenGrade getGrade() {
		return this.grade;
	}

	public void setGrade(BiospecimenGrade grade) {
		this.grade = grade;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAMPLE_DATE", length = 19)
	public Date getSampleDate() {
		return this.sampleDate;
	}

	public void setSampleDate(Date sampleDate) {
		this.sampleDate = sampleDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAMPLE_TIME", length = 19)
	public Date getSampleTime() {
		return this.sampleTime;
	}

	public void setSampleTime(Date sampleTime) {
		this.sampleTime = sampleTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESSED_DATE", length = 19)
	public Date getProcessedDate() {
		return this.processedDate;
	}

	public void setProcessedDate(Date processedDate) {
		this.processedDate = processedDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROCESSED_TIME", length = 19)
	public Date getProcessedTime() {
		return this.processedTime;
	}

	public void setProcessedTime(Date processedTime) {
		this.processedTime = processedTime;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_SPECIES_ID")
	public BiospecimenSpecies getSpecies() {
		return this.species;
	}

	public void setSpecies(BiospecimenSpecies species) {
		this.species = species;
	}

	@Column(name = "QTY_COLLECTED", precision = 22, scale = 0)
	public Double getQtyCollected() {
		return this.qtyCollected;
	}

	public void setQtyCollected(Double qtyCollected) {
		this.qtyCollected = qtyCollected;
	}

	@Column(name = "QTY_REMOVED", precision = 22, scale = 0)
	public Double getQtyRemoved() {
		return this.qtyRemoved;
	}

	public void setQtyRemoved(Double qtyRemoved) {
		this.qtyRemoved = qtyRemoved;
	}

	@Column(name = "COMMENTS", length = 65535)
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TREATMENT_TYPE_ID")
	public TreatmentType getTreatmentType() {
		return this.treatmentType;
	}

	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}

	@Column(name = "BARCODED", nullable = false)
	public Boolean getBarcoded() {
		return barcoded;
	}

	public void setBarcoded(Boolean barcoded) {
		this.barcoded = barcoded;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_QUALITY_ID")
	public BiospecimenQuality getQuality() {
		return this.quality;
	}

	public void setQuality(BiospecimenQuality quality) {
		this.quality = quality;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_STATUS_ID")
	public BiospecimenStatus getStatus() {
		return this.status;
	}

	public void setStatus(BiospecimenStatus status) {
		this.status = status;
	}
		
	@Column(name = "CONCENTRATION")
	public Double getConcentration() {
		return concentration;
	}

	public void setConcentration(Double concentration) {
		this.concentration = concentration;
	}

	/**
	 * @return the biospecimenProtocol
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_PROTOCOL_ID")
	public BiospecimenProtocol getBiospecimenProtocol() {
		return biospecimenProtocol;
	}
	
	/**
	 * @param biospecimenProtocol the biospecimenProtocol to set
	 */
	public void setBiospecimenProtocol(BiospecimenProtocol biospecimenProtocol) {
		this.biospecimenProtocol = biospecimenProtocol;
	}

	@Transient
	public Double getAmount() {
		return ((quantity == null) ? 0 : quantity) * ((concentration == null) ? 0 : concentration);
	}
	
	@Column(name = "PURITY")
	public Double getPurity() {
		return purity;
	}

	public void setPurity(Double purity) {
		this.purity = purity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_ANTICOAGULANT_ID")
	public BiospecimenAnticoagulant getAnticoag() {
		return this.anticoag;
	}

	public void setAnticoag(BiospecimenAnticoagulant anticoag) {
		this.anticoag = anticoag;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "biospecimen")
	public Set<BioTransaction> getBioTransactions() {
		return this.bioTransactions;
	}

	public void setBioTransactions(Set<BioTransaction> bioTransactions) {
		this.bioTransactions = bioTransactions;
	}
		
	/**
	 * @param parent the parent to set
	 */
	public void setParent(Biospecimen parent) {
		this.parent = parent;
	}

	/**
	 * @return the parent
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	public Biospecimen getParent() {
		return parent;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Biospecimen> children) {
		this.children = children;
	}

	/**
	 * @return the children
	 */
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumn(name = "PARENT_ID")
	public List<Biospecimen> getChildren() {
		return children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
//		result = prime * result + ((bioCollection == null) ? 0 : bioCollection.hashCode());
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
		/*if (bioCollection == null) {
			if (other.bioCollection != null)
				return false;
		}
		else if (!bioCollection.equals(other.bioCollection))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else*/ 
		
		if (id == null) {
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
}