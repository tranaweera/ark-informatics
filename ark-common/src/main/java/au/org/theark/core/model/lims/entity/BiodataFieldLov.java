package au.org.theark.core.model.lims.entity;

// Generated 15/06/2011 1:22:58 PM by Hibernate Tools 3.3.0.GA

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.model.Constants;

/**
 * BiodataFieldLov generated by hbm2java
 */
@Entity
@Table(name = "biodata_field_lov", schema = Constants.LIMS_TABLE_SCHEMA)
public class BiodataFieldLov implements java.io.Serializable {

	private Long	id;
	private int		listId;
	private String	value;
	private int		order;

	public BiodataFieldLov() {
	}

	public BiodataFieldLov(Long id, int listId, String value, int order) {
		this.id = id;
		this.listId = listId;
		this.value = value;
		this.order = order;
	}

	@Id
	@SequenceGenerator(name = "biodatafieldlov_generator", sequenceName = "BIODATAFIELDLOV_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "biodatafieldlov_generator")
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "LIST_ID", nullable = false)
	public int getListId() {
		return this.listId;
	}

	public void setListId(int listId) {
		this.listId = listId;
	}

	@Column(name = "VALUE", nullable = false, length = 50)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "ORDER", nullable = false)
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
