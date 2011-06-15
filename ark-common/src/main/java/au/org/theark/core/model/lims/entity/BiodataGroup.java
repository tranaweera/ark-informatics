package au.org.theark.core.model.lims.entity;

// Generated 15/06/2011 1:22:58 PM by Hibernate Tools 3.3.0.GA

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * BiodataGroup generated by hbm2java
 */
@Entity
@Table(name = "biodata_group", schema = Constants.LIMS_TABLE_SCHEMA)
public class BiodataGroup implements java.io.Serializable
{

	private int									id;
	private String								groupName;
	private String								domain;
	private Set<BiodataGroupCriteria>	biodataGroupCriterias	= new HashSet<BiodataGroupCriteria>(0);
	private Set<BiodataFieldGroup>		biodataFieldGroups		= new HashSet<BiodataFieldGroup>(0);

	public BiodataGroup()
	{
	}

	public BiodataGroup(int id, String groupName)
	{
		this.id = id;
		this.groupName = groupName;
	}

	public BiodataGroup(int id, String groupName, String domain, Set<BiodataGroupCriteria> biodataGroupCriterias, Set<BiodataFieldGroup> biodataFieldGroups)
	{
		this.id = id;
		this.groupName = groupName;
		this.domain = domain;
		this.biodataGroupCriterias = biodataGroupCriterias;
		this.biodataFieldGroups = biodataFieldGroups;
	}

	@Id
	@SequenceGenerator(name="biodatagroup_generator", sequenceName="BIODATAGROUP_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "biodatagroup_generator")
	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@Column(name = "GROUP_NAME", nullable = false, length = 100)
	public String getGroupName()
	{
		return this.groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "biodataGroup")
	public Set<BiodataGroupCriteria> getBiodataGroupCriterias()
	{
		return this.biodataGroupCriterias;
	}

	public void setBiodataGroupCriterias(Set<BiodataGroupCriteria> biodataGroupCriterias)
	{
		this.biodataGroupCriterias = biodataGroupCriterias;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "biodataGroup")
	public Set<BiodataFieldGroup> getBiodataFieldGroups()
	{
		return this.biodataFieldGroups;
	}

	public void setBiodataFieldGroups(Set<BiodataFieldGroup> biodataFieldGroups)
	{
		this.biodataFieldGroups = biodataFieldGroups;
	}

}
