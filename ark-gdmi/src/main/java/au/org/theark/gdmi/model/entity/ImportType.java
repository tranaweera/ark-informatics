package au.org.theark.gdmi.model.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ImportType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "IMPORT_TYPE", schema = "GDMI")
public class ImportType implements java.io.Serializable {

	// Fields

	private long id;
	private String name;
	private Set<CollectionImport> collectionImports = new HashSet<CollectionImport>(
			0);

	// Constructors

	/** default constructor */
	public ImportType() {
	}

	/** minimal constructor */
	public ImportType(long id) {
		this.id = id;
	}

	/** full constructor */
	public ImportType(long id, String name,
			Set<CollectionImport> collectionImports) {
		this.id = id;
		this.name = name;
		this.collectionImports = collectionImports;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "importType")
	public Set<CollectionImport> getCollectionImports() {
		return this.collectionImports;
	}

	public void setCollectionImports(Set<CollectionImport> collectionImports) {
		this.collectionImports = collectionImports;
	}

}