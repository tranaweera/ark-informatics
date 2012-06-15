package au.org.theark.core.model.pheno.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * DelimiterType entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity(name = "au.org.theark.phenotypic.model.entity.DelimiterType")
@Table(name = "DELIMITER_TYPE", schema = Constants.PHENO_TABLE_SCHEMA)
public class DelimiterType implements java.io.Serializable
{

	// Fields

	private Long			id;
	private String			name;
	private char delimiterCharacter;
	private Set<PhenoUpload>	collectionUploads	= new HashSet<PhenoUpload>(0);

	// Constructors

	/** default constructor */
	public DelimiterType()
	{
	}

	/** minimal constructor */
	public DelimiterType(Long id)
	{
		this.id = id;
	}

	/** full constructor */
	public DelimiterType(Long id, String name, Set<PhenoUpload> collectionUploads)
	{
		this.id = id;
		this.name = name;
		this.collectionUploads = collectionUploads;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@Column(name = "NAME", length = 50)
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	@Column(name = "DELIMITER_CHARACTER")
	public char getDelimiterCharacter()
	{
		return delimiterCharacter;
	}

	public void setDelimiterCharacter(char delimiterCharacter)
	{
		this.delimiterCharacter = delimiterCharacter;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "delimiterType")
	public Set<PhenoUpload> getCollectionImports()
	{
		return this.collectionUploads;
	}

	public void setCollectionImports(Set<PhenoUpload> collectionUploads)
	{
		this.collectionUploads = collectionUploads;
	}
}