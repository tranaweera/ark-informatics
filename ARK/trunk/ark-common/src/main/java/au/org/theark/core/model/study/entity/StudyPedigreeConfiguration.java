package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "STUDY_PEDIGREE_CONFIG", schema = Constants.STUDY_SCHEMA)
public class StudyPedigreeConfiguration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Study study;
	private CustomField customField;
	private Boolean dobAllowed;
	
	@Id
	@SequenceGenerator(name = "study_pedigree_config_generator", sequenceName = "STUDYPEDIGREECONFIG_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "study_pedigree_config_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return study;
	}
	public void setStudy(Study study) {
		this.study = study;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CUSTOM_FIELD_ID")
	public CustomField getCustomField() {
		return customField;
	}
	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}
	
	@Column(name = "DOB_ALLOWED")
	public Boolean isDobAllowed() {
		return dobAllowed;
	}
	public void setDobAllowed(Boolean dobAllowed) {
		this.dobAllowed = dobAllowed;
	}
	
	

}
