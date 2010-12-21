package au.org.theark.phenotypic.model.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.phenotypic.service.Constants;

/**
 * Status entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name="au.org.theark.phenotypic.model.entity.Status")
@Table(name = "STATUS", schema = Constants.TABLE_SCHEMA)
public class Status implements java.io.Serializable {

	// Fields

	private Long id;
	private String name;
	private Set<PhenoCollection> collections = new HashSet<PhenoCollection>(0);

	// Constructors

	/** default constructor */
	public Status() {
	}

	/** minimal constructor */
	public Status(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Status(Long id, String name, Set<PhenoCollection> collections) {
		this.id = id;
		this.name = name;
		this.collections = collections;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "status")
	public Set<PhenoCollection> getCollections() {
		return this.collections;
	}

	public void setCollections(Set<PhenoCollection> collections) {
		this.collections = collections;
	}

}
