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
import javax.persistence.Version;

import au.org.theark.core.model.Constants;

/**
 * InvSite generated by hbm2java
 */
@Entity
@Table(name = "inv_site", schema = Constants.LIMS_TABLE_SCHEMA)
public class InvSite implements java.io.Serializable
{

	private int				id;
	private String			timestamp;
	private Integer		deleted;
	private String			contact;
	private String			address;
	private String			name;
	private String			phone;
	private String			ldapGroup;
	private Set<InvTank>	invTanks	= new HashSet<InvTank>(0);

	public InvSite()
	{
	}

	public InvSite(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	public InvSite(int id, Integer deleted, String contact, String address, String name, String phone, String ldapGroup, Set<InvTank> invTanks)
	{
		this.id = id;
		this.deleted = deleted;
		this.contact = contact;
		this.address = address;
		this.name = name;
		this.phone = phone;
		this.ldapGroup = ldapGroup;
		this.invTanks = invTanks;
	}

	@Id
	@SequenceGenerator(name="invsite_generator", sequenceName="INVSITE_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "invsite_generator")
	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@Version
	@Column(name = "TIMESTAMP", length = 55)
	public String getTimestamp()
	{
		return this.timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	@Column(name = "DELETED")
	public Integer getDeleted()
	{
		return this.deleted;
	}

	public void setDeleted(Integer deleted)
	{
		this.deleted = deleted;
	}

	@Column(name = "CONTACT", length = 50)
	public String getContact()
	{
		return this.contact;
	}

	public void setContact(String contact)
	{
		this.contact = contact;
	}

	@Column(name = "ADDRESS", length = 65535)
	public String getAddress()
	{
		return this.address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(name = "PHONE", length = 50)
	public String getPhone()
	{
		return this.phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	@Column(name = "LDAP_GROUP", length = 50)
	public String getLdapGroup()
	{
		return this.ldapGroup;
	}

	public void setLdapGroup(String ldapGroup)
	{
		this.ldapGroup = ldapGroup;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "invSite")
	public Set<InvTank> getInvTanks()
	{
		return this.invTanks;
	}

	public void setInvTanks(Set<InvTank> invTanks)
	{
		this.invTanks = invTanks;
	}

}
