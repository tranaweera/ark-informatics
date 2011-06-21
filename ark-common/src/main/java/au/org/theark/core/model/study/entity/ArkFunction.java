package au.org.theark.core.model.study.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;

@Entity
@Table(name = "ARK_FUNCTION", schema = Constants.STUDY_SCHEMA)
public class ArkFunction implements Serializable{
	
	private Long id;
	private String name;
	private String description;
	private String resourceKey;
	
	
	@Id
	@SequenceGenerator(name="ark_function_generator", sequenceName="ARK_FUNCTION_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "ark_function_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="RESOURCE_KEY")
	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
	}	
	

}
