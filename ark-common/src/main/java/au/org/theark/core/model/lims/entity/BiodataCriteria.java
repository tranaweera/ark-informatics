package au.org.theark.core.model.lims.entity;

// Generated 14/06/2011 1:11:20 PM by Hibernate Tools 3.3.0.GA

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * BiodataCriteria generated by hbm2java
 */
@Entity
@Table(name = "biodata_criteria", catalog = "lims")
public class BiodataCriteria implements java.io.Serializable
{

	private int									id;
	private Integer							studyId;
	private String								domain;
	private String								field;
	private String								value;
	private String								description;
	private Set<BiodataGroupCriteria>	biodataGroupCriterias	= new HashSet<BiodataGroupCriteria>(0);

	public BiodataCriteria()
	{
	}

	public BiodataCriteria(int id)
	{
		this.id = id;
	}

	public BiodataCriteria(int id, Integer studyId, String domain, String field, String value, String description, Set<BiodataGroupCriteria> biodataGroupCriterias)
	{
		this.id = id;
		this.studyId = studyId;
		this.domain = domain;
		this.field = field;
		this.value = value;
		this.description = description;
		this.biodataGroupCriterias = biodataGroupCriterias;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@Column(name = "STUDY_ID")
	public Integer getStudyId()
	{
		return this.studyId;
	}

	public void setStudyId(Integer studyId)
	{
		this.studyId = studyId;
	}

	@Column(name = "DOMAIN", length = 50)
	public String getDomain()
	{
		return this.domain;
	}

	public void setDomain(String domain)
	{
		this.domain = domain;
	}

	@Column(name = "FIELD", length = 50)
	public String getField()
	{
		return this.field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	@Column(name = "VALUE")
	public String getValue()
	{
		return this.value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "biodataCriteria")
	public Set<BiodataGroupCriteria> getBiodataGroupCriterias()
	{
		return this.biodataGroupCriterias;
	}

	public void setBiodataGroupCriterias(Set<BiodataGroupCriteria> biodataGroupCriterias)
	{
		this.biodataGroupCriterias = biodataGroupCriterias;
	}

}
