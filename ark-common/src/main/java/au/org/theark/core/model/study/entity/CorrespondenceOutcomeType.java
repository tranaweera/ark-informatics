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
@Table(name = "correspondence_outcome_type", schema = Constants.STUDY_SCHEMA)
public class CorrespondenceOutcomeType implements Serializable {

	private Long	id;
	private String	name;
	private String	description;

	@Id
	@SequenceGenerator(name = "correspondence_outcome_type_generator", sequenceName = "CORRESPONDENCE_OUTCOME_TYPE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "correspondence_outcome_type_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 255)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", length = 4096)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
