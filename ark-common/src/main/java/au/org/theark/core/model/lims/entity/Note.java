package au.org.theark.core.model.lims.entity;

// Generated 15/06/2011 1:22:58 PM by Hibernate Tools 3.3.0.GA

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.model.Constants;

/**
 * Note generated by hbm2java
 */
@Entity(name = "au.org.theark.lims.model.entity.Note")
@Table(name = "note", schema = Constants.LIMS_TABLE_SCHEMA)
public class Note implements java.io.Serializable
{

	private Long		id;
	private String		timestamp;
	private Integer	deleted;
	private String		name;
	private int			elementId;
	private String		type;
	private String		filename;
	private String		domain;
	private String		description;
	private Date		date;

	public Note()
	{
	}

	public Note(Long id, int elementId, Date date)
	{
		this.id = id;
		this.elementId = elementId;
		this.date = date;
	}

	public Note(Long id, Integer deleted, String name, int elementId, String type, String filename, String domain, String description, Date date)
	{
		this.id = id;
		this.deleted = deleted;
		this.name = name;
		this.elementId = elementId;
		this.type = type;
		this.filename = filename;
		this.domain = domain;
		this.description = description;
		this.date = date;
	}

	@Id
	@SequenceGenerator(name = "note_generator", sequenceName = "NOTE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "note_generator")
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

	@Column(name = "DELETED")
	public Integer getDeleted()
	{
		return this.deleted;
	}

	public void setDeleted(Integer deleted)
	{
		this.deleted = deleted;
	}

	@Column(name = "NAME", length = 100)
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Column(name = "ELEMENT_ID", nullable = false)
	public int getElementId()
	{
		return this.elementId;
	}

	public void setElementId(int elementId)
	{
		this.elementId = elementId;
	}

	@Column(name = "TYPE", length = 50)
	public String getType()
	{
		return this.type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@Column(name = "FILENAME", length = 50)
	public String getFilename()
	{
		return this.filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
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

	@Column(name = "DESCRIPTION", length = 65535)
	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATE", nullable = false, length = 19)
	public Date getDate()
	{
		return this.date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

}
