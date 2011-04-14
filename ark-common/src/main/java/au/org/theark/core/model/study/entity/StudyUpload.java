package au.org.theark.core.model.study.entity;

import java.sql.Blob;
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

import au.org.theark.core.Constants;

/**
 * Upload entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name = "au.org.theark.phenotypic.model.study.entity.Upload")
@Table(name = "UPLOAD", schema = Constants.STUDY_SCHEMA)
public class StudyUpload implements java.io.Serializable
{

	// Fields
	private Long				id;
	private Study				study;
	private FileFormat		fileFormat;
	private DelimiterType	delimiterType;
	private String				filename;
	private Blob				payload;
	private String				checksum;
	private Date				startTime;
	private Date				finishTime;
	private Blob				uploadReport;
	private String				userId;

	// Constructors
	/** default constructor */
	public StudyUpload()
	{
	}

	/** minimal constructor */
	public StudyUpload(Long id, FileFormat fileFormat, DelimiterType delimiterType, String filename, Blob uploadReport)
	{
		this.id = id;
		this.fileFormat = fileFormat;
		this.delimiterType = delimiterType;
		this.filename = filename;
		this.uploadReport = uploadReport;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "SubjectUpload_PK_Seq", sequenceName = "STUDY.UPLOAD_PK_SEQ")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SubjectUpload_PK_Seq")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * @return the study
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy()
	{
		return study;
	}
	
	/**
	 * @param study the study to set
	 */
	public void setStudy(Study study)
	{
		this.study = study;
	}

	/**
	 * @return the fileFormat
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FILE_FORMAT_ID", nullable = false)
	public FileFormat getFileFormat()
	{
		return this.fileFormat;
	}

	public void setFileFormat(FileFormat fileFormat)
	{
		this.fileFormat = fileFormat;
	}

	/**
	 * @param delimiterType
	 *           the delimiterType to set
	 */
	public void setDelimiterType(DelimiterType delimiterType)
	{
		this.delimiterType = delimiterType;
	}

	/**
	 * @return the delimiterType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DELIMITER_TYPE_ID", nullable = false)
	public DelimiterType getDelimiterType()
	{
		return delimiterType;
	}

	/**
	 * @return the filename
	 */
	@Column(name = "FILENAME", length = 260)
	public String getFilename()
	{
		return this.filename;
	}

	/**
	 * @param filename
	 *           the filename to set
	 */
	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	/**
	 * @return the payload
	 */
	@Column(name = "PAYLOAD")
	public Blob getPayload()
	{
		return this.payload;
	}

	/**
	 * @param payload
	 *           the payload to set
	 */
	public void setPayload(Blob payload)
	{
		this.payload = payload;
	}

	/**
	 * @return the checksum
	 */
	@Column(name = "CHECKSUM", nullable = false, length = 50)
	public String getChecksum()
	{
		return checksum;
	}

	/**
	 * @param checksum
	 *           the checksum to set
	 */
	public void setChecksum(String checksum)
	{
		this.checksum = checksum;
	}
	
	/**
	 * @return the startTime
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", nullable = false)
	public Date getStartTime()
	{
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	/**
	 * @return the finishTime
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINISH_TIME")
	public Date getFinishTime()
	{
		return finishTime;
	}
	
	/**
	 * @param finishTime the finishTime to set
	 */
	public void setFinishTime(Date finishTime)
	{
		this.finishTime = finishTime;
	}

	/**
	 * @param uploadReport the uploadReport to set
	 */
	public void setUploadReport(Blob uploadReport)
	{
		this.uploadReport = uploadReport;
	}

	/**
	 * @return the uploadReport
	 */
	@Column(name = "UPLOAD_REPORT")
	public Blob getUploadReport()
	{
		return uploadReport;
	}

	/**
	 * @return the userId
	 */
	@Column(name = "USER_ID", nullable = false, length = 100)
	public String getUserId()
	{
		return this.userId;
	}

	/**
	 * @param userId
	 *           the userId to set
	 */
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
}