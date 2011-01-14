package au.org.theark.phenotypic.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import au.org.theark.core.model.study.entity.Study;
import au.org.theark.phenotypic.service.Constants;

/**
 * fieldField entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "FIELD_COLLECTION", schema = Constants.TABLE_SCHEMA)
public class FieldPhenoCollection implements java.io.Serializable {

	// Fields
	private Long id;
	private Study study;
	private Field field;
	private PhenoCollection phenoCollection;
	
	/*
	private String userId;
	private Date insertTime;
	private String updateUserId;
	private Date updateTime;
	*/
	
	// Constructors

	/** default constructor */
	public FieldPhenoCollection() {
	}
	
	/** minimal constructor */
	public FieldPhenoCollection(Long id, Study study, Field field, PhenoCollection phenoCollection) {
		this.id = id;
		this.study = study;
		this.field = field;
		this.phenoCollection = phenoCollection;
	}

	// Property accessors
	@Id
	//@SequenceGenerator(name="FieldCollection_PK_Seq",sequenceName="PHENOTYPIC.FIELD_COLLECTION_PK_SEQ")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param study the study to set
	 */
	public void setStudy(Study study)
	{
		this.study = study;
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
	 * @param field the field to set
	 */
	public void setField(Field field)
	{
		this.field = field;
	}

	/**
	 * @return the field
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FIELD_ID", nullable = false)
	public Field getField()
	{
		return field;
	}

	/**
	 * @param phenoCollection the phenoCollection to set
	 */
	public void setPhenoCollection(PhenoCollection phenoCollection)
	{
		this.phenoCollection = phenoCollection;
	}

	/**
	 * @return the phenoCollection
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLLECTION_ID", nullable = false)
	public PhenoCollection getPhenoCollection()
	{
		return phenoCollection;
	}
}
