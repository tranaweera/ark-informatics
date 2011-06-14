package au.org.theark.core.model.lims.entity;

// Generated Jun 14, 2011 3:39:29 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BiodataUnit generated by hbm2java
 */
@Entity
@Table(name = "biodata_unit", catalog = "lims")
public class BiodataUnit implements java.io.Serializable {

	private int id;
	private String unitname;
	private String description;

	public BiodataUnit() {
	}

	public BiodataUnit(int id, String unitname) {
		this.id = id;
		this.unitname = unitname;
	}

	public BiodataUnit(int id, String unitname, String description) {
		this.id = id;
		this.unitname = unitname;
		this.description = description;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "UNITNAME", nullable = false, length = 50)
	public String getUnitname() {
		return this.unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
