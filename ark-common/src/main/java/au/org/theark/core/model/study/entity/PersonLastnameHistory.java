package au.org.theark.core.model.study.entity;

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

import au.org.theark.core.Constants;

/**
 * PersonSurnameHistory entity.
 * 
 * @author cellis
 */
@Entity
@Table(name = "PERSON_LASTNAME_HISTORY", schema = Constants.STUDY_SCHEMA)
public class PersonLastnameHistory implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -568162910323332654L;
	// Fields
	private Long					id;
	private Person					person;
	private String					lastName;

	// Constructors

	/** default constructor */
	public PersonLastnameHistory() {
	}

	/** minimal constructor */
	public PersonLastnameHistory(Long id) {
		this.id = id;
	}

	/** full constructor */
	public PersonLastnameHistory(Long id, Person person, String lastName) {
		this.id = id;
		this.person = person;
		this.lastName = lastName;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name = "person_lastname_history_generator", sequenceName = "PERSON_LASTNAME_HISTORY_SEQUENCE")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "person_lastname_history_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param person
	 *           the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return the person
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_ID")
	public Person getPerson() {
		return person;
	}

	/**
	 * @return the lastname
	 */
	@Column(name = "LASTNAME", unique = true, nullable = false, length = 50)
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param person
	 *           the surname to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}