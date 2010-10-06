package au.org.theark.gdmi.model.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
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

/**
 * Marker entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "MARKER", schema = "GDMI")
public class Marker implements java.io.Serializable {

	// Fields

	private Long id;
	private MarkerGroup markerGroup;
	private String name;
	private String description;
	private String chromosome;
	private Long position;
	private String gene;
	private String majorAllele;
	private String minorAllele;
	private String userId;
	private String insertTime;
	private String updateUserId;
	private String updateTime;
	private Set<SubjectMarkerMetaData> subjectMarkerMetaDatas = new HashSet<SubjectMarkerMetaData>(
			0);
	private Set<MarkerMetaData> markerMetaDatas = new HashSet<MarkerMetaData>(0);
	private Set<DecodeMask> decodeMasks = new HashSet<DecodeMask>(0);

	// Constructors

	/** default constructor */
	public Marker() {
	}

	/** minimal constructor */
	public Marker(Long id, MarkerGroup markerGroup, String chromosome,
			String userId, String insertTime) {
		this.id = id;
		this.markerGroup = markerGroup;
		this.chromosome = chromosome;
		this.userId = userId;
		this.insertTime = insertTime;
	}

	/** full constructor */
	public Marker(Long id, MarkerGroup markerGroup, String name,
			String description, String chromosome, Long position, String gene,
			String majorAllele, String minorAllele, String userId,
			String insertTime, String updateUserId, String updateTime,
			Set<SubjectMarkerMetaData> subjectMarkerMetaDatas,
			Set<MarkerMetaData> markerMetaDatas, Set<DecodeMask> decodeMasks) {
		this.id = id;
		this.markerGroup = markerGroup;
		this.name = name;
		this.description = description;
		this.chromosome = chromosome;
		this.position = position;
		this.gene = gene;
		this.majorAllele = majorAllele;
		this.minorAllele = minorAllele;
		this.userId = userId;
		this.insertTime = insertTime;
		this.updateUserId = updateUserId;
		this.updateTime = updateTime;
		this.subjectMarkerMetaDatas = subjectMarkerMetaDatas;
		this.markerMetaDatas = markerMetaDatas;
		this.decodeMasks = decodeMasks;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="Marker_PK_Seq",sequenceName="GDMI.MARKER_PK_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="Marker_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MARKER_GROUP_ID", nullable = false)
	public MarkerGroup getMarkerGroup() {
		return this.markerGroup;
	}

	public void setMarkerGroup(MarkerGroup markerGroup) {
		this.markerGroup = markerGroup;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "CHROMOSOME", nullable = false, length = 50)
	public String getChromosome() {
		return this.chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	@Column(name = "POSITION", precision = 22, scale = 0)
	public Long getPosition() {
		return this.position;
	}

	public void setPosition(Long position) {
		this.position = position;
	}

	@Column(name = "GENE", length = 100)
	public String getGene() {
		return this.gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	@Column(name = "MAJOR_ALLELE", length = 10)
	public String getMajorAllele() {
		return this.majorAllele;
	}

	public void setMajorAllele(String majorAllele) {
		this.majorAllele = majorAllele;
	}

	@Column(name = "MINOR_ALLELE", length = 10)
	public String getMinorAllele() {
		return this.minorAllele;
	}

	public void setMinorAllele(String minorAllele) {
		this.minorAllele = minorAllele;
	}

	@Column(name = "USER_ID", nullable = false, length = 50)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "INSERT_TIME", nullable = false)
	public String getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "UPDATE_USER_ID", length = 50)
	public String getUpdateUserId() {
		return this.updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	@Column(name = "UPDATE_TIME")
	public String getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "marker")
	public Set<SubjectMarkerMetaData> getSubjectMarkerMetaDatas() {
		return this.subjectMarkerMetaDatas;
	}

	public void setSubjectMarkerMetaDatas(
			Set<SubjectMarkerMetaData> subjectMarkerMetaDatas) {
		this.subjectMarkerMetaDatas = subjectMarkerMetaDatas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "marker")
	public Set<MarkerMetaData> getMarkerMetaDatas() {
		return this.markerMetaDatas;
	}

	public void setMarkerMetaDatas(Set<MarkerMetaData> markerMetaDatas) {
		this.markerMetaDatas = markerMetaDatas;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "marker")
	public Set<DecodeMask> getDecodeMasks() {
		return this.decodeMasks;
	}

	public void setDecodeMasks(Set<DecodeMask> decodeMasks) {
		this.decodeMasks = decodeMasks;
	}

}