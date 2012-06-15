package au.org.theark.core.model.study.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import au.org.theark.core.Constants;

/**
 * FileFormat entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name="au.org.theark.phenotypic.model.study.entity.FileFormat")
@Table(name = "FILE_FORMAT", schema = Constants.STUDY_SCHEMA)
public class FileFormat implements java.io.Serializable {

	// Fields
	private Long id;
	private String name;

	// Constructors

	/** default constructor */
	public FileFormat() {
	}

	/** minimal constructor */
	public FileFormat(Long id) {
		this.id = id;
	}

	/** full constructor */
	public FileFormat(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}