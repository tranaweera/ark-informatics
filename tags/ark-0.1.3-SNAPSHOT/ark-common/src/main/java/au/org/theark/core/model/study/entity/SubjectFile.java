package au.org.theark.core.model.study.entity;

import java.sql.Blob;

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

import au.org.theark.core.Constants;

/**
 * SubjectFile entity. 
 * @author cellis
 */
@Entity
@Table(name = "SUBJECT_FILE", schema = Constants.STUDY_SCHEMA)
public class SubjectFile implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -3611814204230766317L;
	private Long id;
	private LinkSubjectStudy linkSubjectStudy;
	private StudyComp studyComp;
	private String filename;
	private Blob payload;
	private String checksum;
	private String userId;
	private String comments;

	// Constructors

	/** default constructor */
	public SubjectFile() {
	}

	/** minimal constructor */
	public SubjectFile(Long id, LinkSubjectStudy linkSubjectStudy, StudyComp studyComp,	String filename, String userId) {
		this.id = id;
		this.linkSubjectStudy = linkSubjectStudy;
		this.studyComp = studyComp;
		this.filename = filename;
		this.userId = userId;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="SubjectFile_PK_Seq",sequenceName="SUBJECTFILE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO,generator="SubjectFile_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @param linkSubjectStudy the linkSubjectStudy to set
	 */
	public void setLinkSubjectStudy(LinkSubjectStudy linkSubjectStudy)
	{
		this.linkSubjectStudy = linkSubjectStudy;
	}

	/**
	 * @return the linkSubjectStudy
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LINK_SUBJECT_STUDY_ID")
	public LinkSubjectStudy getLinkSubjectStudy()
	{
		return linkSubjectStudy;
	}

	/**
	 * @param studyComp the studyComp to set
	 */
	public void setStudyComp(StudyComp studyComp)
	{
		this.studyComp = studyComp;
	}

	/**
	 * @return the studyComp
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_COMP_ID")
	public StudyComp getStudyComp()
	{
		return studyComp;
	}
	
	@Column(name = "FILENAME", length = 260)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Column(name = "PAYLOAD")
	public Blob getPayload() {
		return this.payload;
	}

	public void setPayload(Blob payload) {
		this.payload = payload;
	}
	
	@Column(name = "CHECKSUM")
	public String getChecksum() {
		return checksum;
	}
	
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	@Column(name = "USER_ID", nullable = false, length = 100)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments)
	{
		this.comments = comments;
	}

	/**
	 * @return the comments
	 */
	@Column(name = "COMMENTS", length = 500)
	public String getComments()
	{
		return comments;
	}
}