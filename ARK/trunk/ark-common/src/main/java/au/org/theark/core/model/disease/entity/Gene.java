package au.org.theark.core.model.disease.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.Study;

@Entity
@Table(name = "GENE", schema = Constants.DISEASE_SCHEMA)
public class Gene implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Study study;
	private Set<Position> positions = new HashSet<Position>();
	private Set<Disease> diseases = new HashSet<Disease>();
	
	public Gene() {

	}

	public Gene(Long id) {
		this.id = id;
	}

	@Id
	@SequenceGenerator(name = "gene_generator", sequenceName = "GENE_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "gene_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "NAME", length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_ID")
	public Study getStudy() {
		return this.study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "gene")
	public Set<Position> getPositions() {
		return this.positions;
	}

	public void setPositions(Set<Position> positions) {
		this.positions = positions;
	}

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "genes")
	public Set<Disease> getDiseases() {
		return this.diseases;
	}
	
	public void setDiseases(Set<Disease> diseases) {
		this.diseases = diseases;
	}
	
	@Override
	public String toString() {
		return "Gene [id=" + id + ", name=" + name + ", study=" + study
				+ ", positions=" + positions 
				+ ", diseases=" + diseases + "]";
	}
	
	
}