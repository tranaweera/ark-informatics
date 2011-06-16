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
import javax.persistence.Version;

import au.org.theark.core.model.Constants;
import au.org.theark.core.model.study.entity.Study;

/**
 * BioTransaction generated by hbm2java
 */
@Entity
@Table(name = "bio_transaction", schema = Constants.LIMS_TABLE_SCHEMA)
public class BioTransaction implements java.io.Serializable
{

	private Long				id;
	private String			timestamp;
	private Biospecimen	biospecimen;
	private Study		study;
	private Long		deleted;
	private String			treatment;
	private Long		sourcestudyId;
	private String			unit;
	private Date			deliverydate;
	private String			fixationtime;
	private Date			transactiondate;
	private Long			quantity;
	private String			owner;
	private String			reason;
	private String			status;
	private String			studyname;
	private String			collaborator;
	private String			recorder;
	private String			destination;
	private String			action;
	private String			type;

	public BioTransaction()
	{
	}

	public BioTransaction(Long id, Biospecimen biospecimen, Date transactiondate, Long quantity, Study study)
	{
		this.id = id;
		this.biospecimen = biospecimen;
		this.transactiondate = transactiondate;
		this.quantity = quantity;
		this.study = study;
	}

	public BioTransaction(Long id, Biospecimen biospecimen, Study study, Long deleted, String treatment, Long sourcestudyId, String unit, Date deliverydate, String fixationtime,
			Date transactiondate, Long quantity, String owner, String reason, String status, String studyname, String collaborator, String recorder, String destination, String action, String type)
	{
		this.id = id;
		this.biospecimen = biospecimen;
		this.study = study;
		this.deleted = deleted;
		this.treatment = treatment;
		this.sourcestudyId = sourcestudyId;
		this.unit = unit;
		this.deliverydate = deliverydate;
		this.fixationtime = fixationtime;
		this.transactiondate = transactiondate;
		this.quantity = quantity;
		this.owner = owner;
		this.reason = reason;
		this.status = status;
		this.studyname = studyname;
		this.collaborator = collaborator;
		this.recorder = recorder;
		this.destination = destination;
		this.action = action;
		this.type = type;
	}

	@Id
	@SequenceGenerator(name = "biotransaction_generator", sequenceName = "BIOTRANSACTION_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biotransaction_generator")
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	
	@Column(name = "TIMESTAMP", length = 55)
	public String getTimestamp()
	{
		return this.timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BIOSPECIMEN_ID")
	public Biospecimen getBiospecimen()
	{
		return this.biospecimen;
	}

	public void setBiospecimen(Biospecimen biospecimen)
	{
		this.biospecimen = biospecimen;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy()
	{
		return this.study;
	}

	public void setStudy(Study study)
	{
		this.study = study;
	}

	@Column(name = "DELETED")
	public Long getDeleted()
	{
		return this.deleted;
	}

	public void setDeleted(Long deleted)
	{
		this.deleted = deleted;
	}

	@Column(name = "TREATMENT")
	public String getTreatment()
	{
		return this.treatment;
	}

	public void setTreatment(String treatment)
	{
		this.treatment = treatment;
	}

	@Column(name = "SOURCESTUDY_ID")
	public Long getSourcestudyId()
	{
		return this.sourcestudyId;
	}

	public void setSourcestudyId(Long sourcestudyId)
	{
		this.sourcestudyId = sourcestudyId;
	}

	@Column(name = "UNIT", length = 50)
	public String getUnit()
	{
		return this.unit;
	}

	public void setUnit(String unit)
	{
		this.unit = unit;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELIVERYDATE", length = 19)
	public Date getDeliverydate()
	{
		return this.deliverydate;
	}

	public void setDeliverydate(Date deliverydate)
	{
		this.deliverydate = deliverydate;
	}

	@Column(name = "FIXATIONTIME", length = 50)
	public String getFixationtime()
	{
		return this.fixationtime;
	}

	public void setFixationtime(String fixationtime)
	{
		this.fixationtime = fixationtime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRANSACTIONDATE", nullable = false, length = 19)
	public Date getTransactiondate()
	{
		return this.transactiondate;
	}

	public void setTransactiondate(Date transactiondate)
	{
		this.transactiondate = transactiondate;
	}

	@Column(name = "QUANTITY", nullable = false, precision = 22, scale = 0)
	public Long getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(Long quantity)
	{
		this.quantity = quantity;
	}

	@Column(name = "OWNER")
	public String getOwner()
	{
		return this.owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	@Column(name = "REASON", length = 65535)
	public String getReason()
	{
		return this.reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	@Column(name = "STATUS", length = 50)
	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	@Column(name = "STUDY", length = 50)
	public String getStudyName()
	{
		return this.studyname;
	}

	public void setStudyName(String studyname)
	{
		this.studyname = studyname;
	}

	@Column(name = "COLLABORATOR")
	public String getCollaborator()
	{
		return this.collaborator;
	}

	public void setCollaborator(String collaborator)
	{
		this.collaborator = collaborator;
	}

	@Column(name = "RECORDER")
	public String getRecorder()
	{
		return this.recorder;
	}

	public void setRecorder(String recorder)
	{
		this.recorder = recorder;
	}

	@Column(name = "DESTINATION")
	public String getDestination()
	{
		return this.destination;
	}

	public void setDestination(String destination)
	{
		this.destination = destination;
	}

	@Column(name = "ACTION", length = 50)
	public String getAction()
	{
		return this.action;
	}

	public void setAction(String action)
	{
		this.action = action;
	}

	@Column(name = "TYPE", length = 100)
	public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

}
