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
import javax.persistence.Version;

import au.org.theark.core.model.Constants;

/**
 * Biospecimen generated by hbm2java
 */
@Entity
@Table(name = "biospecimen", schema = Constants.LIMS_TABLE_SCHEMA)
public class Biospecimen implements java.io.Serializable
{

	private int							id;
	private String						timestamp;
	private Collection				collection;
	private int							biospecimenId;
	private Integer					studyId;
	private Integer					substudyId;
	private Integer					patientId;
	private Integer					parentId;
	private String						parentid;
	private Integer					oldId;
	private Integer					deleted;
	private String						otherid;
	private String						storedIn;
	private Date						sampleTime;
	private String						grade;
	private int							cellId;
	private Integer					depth;
	private Date						sampledate;
	private Date						extractedTime;
	private String						location;
	private int							sampletypeId;
	private String						sampletype;
	private String						samplesubtype;
	private String						subtypedesc;
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
	private Integer					dnaBank;
	private Integer					quantity;
	private String						units;
	private String						quality;
	private Integer					withdrawn;
	private String						status;
	private String						treatment;
	private Set<InvCell>				invCells				= new HashSet<InvCell>(0);
	private Set<BioTransaction>	bioTransactions	= new HashSet<BioTransaction>(0);

	public Biospecimen()
	{
	}

	public Biospecimen(int id, int biospecimenId, int cellId, int sampletypeId, String sampletype)
	{
		this.id = id;
		this.biospecimenId = biospecimenId;
		this.cellId = cellId;
		this.sampletypeId = sampletypeId;
		this.sampletype = sampletype;
	}

	public Biospecimen(int id, Collection collection, int biospecimenId, Integer studyId, Integer substudyId, Integer patientId, Integer parentId, String parentid, Integer oldId, Integer deleted,
			String otherid, String storedIn, Date sampleTime, String grade, int cellId, Integer depth, Date sampledate, Date extractedTime, String location, int sampletypeId, String sampletype,
			String samplesubtype, String subtypedesc, String species, Double qtyCollected, Date dateextracted, Double qtyRemoved, Double gestat, String comments, Date datedistributed,
			String collaborator, Double dnaconc, Double purity, String anticoag, String protocol, Integer dnaBank, Integer quantity, String units, String quality, Integer withdrawn, String status,
			String treatment, Set<InvCell> invCells, Set<BioTransaction> bioTransactions)
	{
		this.id = id;
		this.collection = collection;
		this.biospecimenId = biospecimenId;
		this.studyId = studyId;
		this.substudyId = substudyId;
		this.patientId = patientId;
		this.parentId = parentId;
		this.parentid = parentid;
		this.oldId = oldId;
		this.deleted = deleted;
		this.otherid = otherid;
		this.storedIn = storedIn;
		this.sampleTime = sampleTime;
		this.grade = grade;
		this.cellId = cellId;
		this.depth = depth;
		this.sampledate = sampledate;
		this.extractedTime = extractedTime;
		this.location = location;
		this.sampletypeId = sampletypeId;
		this.sampletype = sampletype;
		this.samplesubtype = samplesubtype;
		this.subtypedesc = subtypedesc;
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
		this.units = units;
		this.quality = quality;
		this.withdrawn = withdrawn;
		this.status = status;
		this.treatment = treatment;
		this.invCells = invCells;
		this.bioTransactions = bioTransactions;
	}

	@Id
	@SequenceGenerator(name="biospecimen_generator", sequenceName="BIOSPECIMEN_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "biospecimen_generator")
	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@Version
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
	@JoinColumn(name = "COLLECTION_ID")
	public Collection getCollection()
	{
		return this.collection;
	}

	public void setCollection(Collection collection)
	{
		this.collection = collection;
	}

	@Column(name = "BIOSPECIMEN_ID", nullable = false)
	public int getBiospecimenId()
	{
		return this.biospecimenId;
	}

	public void setBiospecimenId(int biospecimenId)
	{
		this.biospecimenId = biospecimenId;
	}

	@Column(name = "STUDY_ID")
	public Integer getStudyId()
	{
		return this.studyId;
	}

	public void setStudyId(Integer studyId)
	{
		this.studyId = studyId;
	}

	@Column(name = "SUBSTUDY_ID")
	public Integer getSubstudyId()
	{
		return this.substudyId;
	}

	public void setSubstudyId(Integer substudyId)
	{
		this.substudyId = substudyId;
	}

	@Column(name = "PATIENT_ID")
	public Integer getPatientId()
	{
		return this.patientId;
	}

	public void setPatientId(Integer patientId)
	{
		this.patientId = patientId;
	}

	@Column(name = "PARENT_ID")
	public Integer getParentId()
	{
		return this.parentId;
	}

	public void setParentId(Integer parentId)
	{
		this.parentId = parentId;
	}

	@Column(name = "PARENTID", length = 50)
	public String getParentid()
	{
		return this.parentid;
	}

	public void setParentid(String parentid)
	{
		this.parentid = parentid;
	}

	@Column(name = "OLD_ID")
	public Integer getOldId()
	{
		return this.oldId;
	}

	public void setOldId(Integer oldId)
	{
		this.oldId = oldId;
	}

	@Column(name = "DELETED")
	public Integer getDeleted()
	{
		return this.deleted;
	}

	public void setDeleted(Integer deleted)
	{
		this.deleted = deleted;
	}

	@Column(name = "OTHERID", length = 50)
	public String getOtherid()
	{
		return this.otherid;
	}

	public void setOtherid(String otherid)
	{
		this.otherid = otherid;
	}

	@Column(name = "STORED_IN", length = 50)
	public String getStoredIn()
	{
		return this.storedIn;
	}

	public void setStoredIn(String storedIn)
	{
		this.storedIn = storedIn;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAMPLE_TIME", length = 19)
	public Date getSampleTime()
	{
		return this.sampleTime;
	}

	public void setSampleTime(Date sampleTime)
	{
		this.sampleTime = sampleTime;
	}

	@Column(name = "GRADE")
	public String getGrade()
	{
		return this.grade;
	}

	public void setGrade(String grade)
	{
		this.grade = grade;
	}

	@Column(name = "CELL_ID", nullable = false)
	public int getCellId()
	{
		return this.cellId;
	}

	public void setCellId(int cellId)
	{
		this.cellId = cellId;
	}

	@Column(name = "DEPTH")
	public Integer getDepth()
	{
		return this.depth;
	}

	public void setDepth(Integer depth)
	{
		this.depth = depth;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAMPLEDATE", length = 19)
	public Date getSampledate()
	{
		return this.sampledate;
	}

	public void setSampledate(Date sampledate)
	{
		this.sampledate = sampledate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXTRACTED_TIME", length = 19)
	public Date getExtractedTime()
	{
		return this.extractedTime;
	}

	public void setExtractedTime(Date extractedTime)
	{
		this.extractedTime = extractedTime;
	}

	@Column(name = "LOCATION")
	public String getLocation()
	{
		return this.location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	@Column(name = "SAMPLETYPE_ID", nullable = false)
	public int getSampletypeId()
	{
		return this.sampletypeId;
	}

	public void setSampletypeId(int sampletypeId)
	{
		this.sampletypeId = sampletypeId;
	}

	@Column(name = "SAMPLETYPE", nullable = false)
	public String getSampletype()
	{
		return this.sampletype;
	}

	public void setSampletype(String sampletype)
	{
		this.sampletype = sampletype;
	}

	@Column(name = "SAMPLESUBTYPE")
	public String getSamplesubtype()
	{
		return this.samplesubtype;
	}

	public void setSamplesubtype(String samplesubtype)
	{
		this.samplesubtype = samplesubtype;
	}

	@Column(name = "SUBTYPEDESC")
	public String getSubtypedesc()
	{
		return this.subtypedesc;
	}

	public void setSubtypedesc(String subtypedesc)
	{
		this.subtypedesc = subtypedesc;
	}

	@Column(name = "SPECIES")
	public String getSpecies()
	{
		return this.species;
	}

	public void setSpecies(String species)
	{
		this.species = species;
	}

	@Column(name = "QTY_COLLECTED", precision = 22, scale = 0)
	public Double getQtyCollected()
	{
		return this.qtyCollected;
	}

	public void setQtyCollected(Double qtyCollected)
	{
		this.qtyCollected = qtyCollected;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATEEXTRACTED", length = 19)
	public Date getDateextracted()
	{
		return this.dateextracted;
	}

	public void setDateextracted(Date dateextracted)
	{
		this.dateextracted = dateextracted;
	}

	@Column(name = "QTY_REMOVED", precision = 22, scale = 0)
	public Double getQtyRemoved()
	{
		return this.qtyRemoved;
	}

	public void setQtyRemoved(Double qtyRemoved)
	{
		this.qtyRemoved = qtyRemoved;
	}

	@Column(name = "GESTAT", precision = 22, scale = 0)
	public Double getGestat()
	{
		return this.gestat;
	}

	public void setGestat(Double gestat)
	{
		this.gestat = gestat;
	}

	@Column(name = "COMMENTS", length = 65535)
	public String getComments()
	{
		return this.comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATEDISTRIBUTED", length = 19)
	public Date getDatedistributed()
	{
		return this.datedistributed;
	}

	public void setDatedistributed(Date datedistributed)
	{
		this.datedistributed = datedistributed;
	}

	@Column(name = "COLLABORATOR", length = 100)
	public String getCollaborator()
	{
		return this.collaborator;
	}

	public void setCollaborator(String collaborator)
	{
		this.collaborator = collaborator;
	}

	@Column(name = "DNACONC", precision = 22, scale = 0)
	public Double getDnaconc()
	{
		return this.dnaconc;
	}

	public void setDnaconc(Double dnaconc)
	{
		this.dnaconc = dnaconc;
	}

	@Column(name = "PURITY", precision = 22, scale = 0)
	public Double getPurity()
	{
		return this.purity;
	}

	public void setPurity(Double purity)
	{
		this.purity = purity;
	}

	@Column(name = "ANTICOAG", length = 100)
	public String getAnticoag()
	{
		return this.anticoag;
	}

	public void setAnticoag(String anticoag)
	{
		this.anticoag = anticoag;
	}

	@Column(name = "PROTOCOL", length = 100)
	public String getProtocol()
	{
		return this.protocol;
	}

	public void setProtocol(String protocol)
	{
		this.protocol = protocol;
	}

	@Column(name = "DNA_BANK")
	public Integer getDnaBank()
	{
		return this.dnaBank;
	}

	public void setDnaBank(Integer dnaBank)
	{
		this.dnaBank = dnaBank;
	}

	@Column(name = "QUANTITY")
	public Integer getQuantity()
	{
		return this.quantity;
	}

	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}

	@Column(name = "UNITS", length = 10)
	public String getUnits()
	{
		return this.units;
	}

	public void setUnits(String units)
	{
		this.units = units;
	}

	@Column(name = "QUALITY", length = 100)
	public String getQuality()
	{
		return this.quality;
	}

	public void setQuality(String quality)
	{
		this.quality = quality;
	}

	@Column(name = "WITHDRAWN")
	public Integer getWithdrawn()
	{
		return this.withdrawn;
	}

	public void setWithdrawn(Integer withdrawn)
	{
		this.withdrawn = withdrawn;
	}

	@Column(name = "STATUS", length = 20)
	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	@Column(name = "TREATMENT", length = 50)
	public String getTreatment()
	{
		return this.treatment;
	}

	public void setTreatment(String treatment)
	{
		this.treatment = treatment;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "biospecimen")
	public Set<InvCell> getInvCells()
	{
		return this.invCells;
	}

	public void setInvCells(Set<InvCell> invCells)
	{
		this.invCells = invCells;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "biospecimen")
	public Set<BioTransaction> getBioTransactions()
	{
		return this.bioTransactions;
	}

	public void setBioTransactions(Set<BioTransaction> bioTransactions)
	{
		this.bioTransactions = bioTransactions;
	}

}
