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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * InvBox generated by hbm2java
 */
@Entity
@Table(name = "inv_tray", schema = Constants.LIMS_TABLE_SCHEMA)
public class InvTray implements java.io.Serializable
{

	private Long				id;
	private String			timestamp;
	private InvTank		invTank;
	private Integer		deleted;
	private String			name;
	private Integer		available;
	private String			description;
	private Integer		capacity;
	private Set<InvBox>	invBoxes	= new HashSet<InvBox>(0);

	public InvTray()
	{
	}

	public InvTray(Long id, InvTank invTank, String name)
	{
		this.id = id;
		this.invTank = invTank;
		this.name = name;
	}

	public InvTray(Long id, InvTank invTank, Integer deleted, String name, Integer available, String description, Integer capacity, Set<InvBox> invBoxes)
	{
		this.id = id;
		this.invTank = invTank;
		this.deleted = deleted;
		this.name = name;
		this.available = available;
		this.description = description;
		this.capacity = capacity;
		this.invBoxes = invBoxes;
	}

	@Id
	@SequenceGenerator(name = "invbox_generator", sequenceName = "INVBOX_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "invbox_generator")
	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	
	@Column(name = "TIMESTAMP", length = 55)
	public String getTimestamp()
	{
		return this.timestamp;
	}

	public void setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TANK_ID", nullable = false)
	public InvTank getInvTank()
	{
		return this.invTank;
	}

	public void setInvTank(InvTank invTank)
	{
		this.invTank = invTank;
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

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(name = "AVAILABLE")
	public Integer getAvailable()
	{
		return this.available;
	}

	public void setAvailable(Integer available)
	{
		this.available = available;
	}

	@Column(name = "DESCRIPTION", length = 65535)
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@Column(name = "CAPACITY")
	public Integer getCapacity()
	{
		return this.capacity;
	}

	public void setCapacity(Integer capacity)
	{
		this.capacity = capacity;
	}

	/*
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "invBox")
	public Set<InvBox> getInvBoxes()
	{
		return this.invBoxes;
	}

	public void setInvBoxes(Set<InvBox> invBoxes)
	{
		this.invBoxes = invBoxes;
	}
	*/

}
