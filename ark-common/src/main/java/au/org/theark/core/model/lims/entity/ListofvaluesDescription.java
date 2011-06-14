package au.org.theark.core.model.lims.entity;

// Generated Jun 14, 2011 3:39:29 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * ListofvaluesDescription generated by hbm2java
 */
@Entity
@Table(name = "listofvalues_description", catalog = "lims")
public class ListofvaluesDescription implements java.io.Serializable {

	private int id;
	private String timestamp;
	private Integer deleted;
	private String type;
	private String description;
	private String descId;

	public ListofvaluesDescription() {
	}

	public ListofvaluesDescription(int id, String type, String description) {
		this.id = id;
		this.type = type;
		this.description = description;
	}

	public ListofvaluesDescription(int id, Integer deleted, String type,
			String description, String descId) {
		this.id = id;
		this.deleted = deleted;
		this.type = type;
		this.description = description;
		this.descId = descId;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Version
	@Column(name = "TIMESTAMP", length = 55)
	public String getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	@Column(name = "DELETED")
	public Integer getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	@Column(name = "TYPE", nullable = false)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "DESCRIPTION", nullable = false, length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "DESC_ID", length = 55)
	public String getDescId() {
		return this.descId;
	}

	public void setDescId(String descId) {
		this.descId = descId;
	}

}
