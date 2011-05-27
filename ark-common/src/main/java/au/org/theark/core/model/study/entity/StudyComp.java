package au.org.theark.core.model.study.entity;

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
import javax.persistence.UniqueConstraint;

import au.org.theark.core.Constants;

/**
 * StudyComp entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDY_COMP", schema = Constants.STUDY_SCHEMA, uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class StudyComp implements java.io.Serializable {

	// Fields

	private Long id;
	private Study study;
	private String name;
	private String description;
	private String keyword;
	
	private Set<LinkSubjectStudycomp> linkSubjectStudycomps = new HashSet<LinkSubjectStudycomp>(
			0);
	private Set<LinkStudyStudycomp> linkStudyStudycomps = new HashSet<LinkStudyStudycomp>(
			0);

	// Constructors

	/** default constructor */
	public StudyComp() {
	}

	/** minimal constructor */
	public StudyComp(Long id) {
		this.id = id;
	}

	/** full constructor */
	public StudyComp(Long id, Study study, String name,
			String description,String keyword,
			Set<LinkSubjectStudycomp> linkSubjectStudycomps,
			Set<LinkStudyStudycomp> linkStudyStudycomps) {
		this.id = id;
		this.study = study;
		this.name = name;
		this.description = description;
		this.keyword = keyword;
		this.linkSubjectStudycomps = linkSubjectStudycomps;
		this.linkStudyStudycomps = linkStudyStudycomps;
	}

	@Id
	@SequenceGenerator(name="studycomp_generator", sequenceName="STUDYCOMP_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "studycomp_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@Column(name = "NAME", unique = true, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="KEYWORD")
	public String getKeyword(){
		return this.keyword;
	}
	
	public void setKeyword(String keyword){
		this.keyword = keyword;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "studyComp")
	public Set<LinkSubjectStudycomp> getLinkSubjectStudycomps() {
		return this.linkSubjectStudycomps;
	}

	public void setLinkSubjectStudycomps(
			Set<LinkSubjectStudycomp> linkSubjectStudycomps) {
		this.linkSubjectStudycomps = linkSubjectStudycomps;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "studyComp")
	public Set<LinkStudyStudycomp> getLinkStudyStudycomps() {
		return this.linkStudyStudycomps;
	}

	public void setLinkStudyStudycomps(
			Set<LinkStudyStudycomp> linkStudyStudycomps) {
		this.linkStudyStudycomps = linkStudyStudycomps;
	}

}