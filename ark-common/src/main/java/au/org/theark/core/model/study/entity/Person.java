package au.org.theark.core.model.study.entity;

import java.util.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.org.theark.core.Constants;

/**
 * Person entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PERSON", schema = Constants.STUDY_SCHEMA)
public class Person implements java.io.Serializable {

	// Fields
	private Long id;
	private String firstName;
	private String middleName;
	private String lastName;
	private String preferredName;
	private GenderType genderType;
	private VitalStatus vitalStatus;
	private TitleType titleType;
	private MaritalStatus maritalStatus;
	private Date dateOfBirth;
	
	
	private Set<LinkSubjectStudy> linkSubjectStudies = new HashSet<LinkSubjectStudy>(
			0);
	private Set<Phone> phones = new HashSet<Phone>(0);
	private Set<LinkSubjectStudycomp> linkSubjectStudycomps = new HashSet<LinkSubjectStudycomp>(
			0);
	private Set<LinkSubjectContact> linkSubjectContactsForContactKey = new HashSet<LinkSubjectContact>(
			0);
	private Set<LinkSiteContact> linkSiteContacts = new HashSet<LinkSiteContact>(
			0);
	private Set<Address> addresses = new HashSet<Address>(0);
	private Set<LinkSubjectContact> linkSubjectContactsForSubjectKey = new HashSet<LinkSubjectContact>(
			0);

	// Constructors

	/** default constructor */
	public Person() {
	}

	/** minimal constructor */
	public Person(Long id) {
		this.id = id;
	}

	/** full constructor */
	public Person(Long id, String firstName, String middleName,
			String lastName, String preferredName, 	Date dateOfBirth, VitalStatus vitalStatus, TitleType titleType,GenderType genderType,
			Set<LinkSubjectStudy> linkSubjectStudies,
			Set<Phone> phones, Set<LinkSubjectStudycomp> linkSubjectStudycomps,
			Set<LinkSubjectContact> linkSubjectContactsForContactKey,
			Set<LinkSiteContact> linkSiteContacts, Set<Address> addresses,
			Set<LinkSubjectContact> linkSubjectContactsForSubjectKey) {
		this.id = id;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.preferredName = preferredName;
		this.dateOfBirth = dateOfBirth;
		this.vitalStatus = vitalStatus;
		this.titleType = titleType;
		this.genderType = genderType;
		this.linkSubjectStudies = linkSubjectStudies;
		this.phones = phones;
		this.linkSubjectStudycomps = linkSubjectStudycomps;
		this.linkSubjectContactsForContactKey = linkSubjectContactsForContactKey;
		this.linkSiteContacts = linkSiteContacts;
		this.addresses = addresses;
		this.linkSubjectContactsForSubjectKey = linkSubjectContactsForSubjectKey;
	}

	// Property accessors
	@Id
	@SequenceGenerator(name="person_generator", sequenceName="PERSON_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "person_generator")
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FIRST_NAME", length = 50)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "MIDDLE_NAME", length = 50)
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "LAST_NAME", length = 50)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "PREFERRED_NAME", length = 150)
	public String getPreferredName() {
		return this.preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "GENDER_TYPE_ID")
	public GenderType getGenderType() {
		return this.genderType;
	}

	public void setGenderType(GenderType genderType){
		this.genderType = genderType;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_BIRTH", length = 7)
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VITAL_STATUS_ID")
	public VitalStatus getVitalStatus() {
		return this.vitalStatus;
	}

	public void setVitalStatus(VitalStatus vitalStatus) {
		this.vitalStatus = vitalStatus;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TITLE_TYPE_ID")
	public TitleType getTitleType(){
		return this.titleType;
	}
	
	public void setTitleType(TitleType titleType){
		this.titleType = titleType;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MARITAL_STATUS_ID")
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<LinkSubjectStudy> getLinkSubjectStudies() {
		return this.linkSubjectStudies;
	}

	public void setLinkSubjectStudies(Set<LinkSubjectStudy> linkSubjectStudies) {
		this.linkSubjectStudies = linkSubjectStudies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<Phone> getPhones() {
		return this.phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<LinkSubjectStudycomp> getLinkSubjectStudycomps() {
		return this.linkSubjectStudycomps;
	}

	public void setLinkSubjectStudycomps(
			Set<LinkSubjectStudycomp> linkSubjectStudycomps) {
		this.linkSubjectStudycomps = linkSubjectStudycomps;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "personByContactId")
	public Set<LinkSubjectContact> getLinkSubjectContactsForContactKey() {
		return this.linkSubjectContactsForContactKey;
	}

	public void setLinkSubjectContactsForContactKey(
			Set<LinkSubjectContact> linkSubjectContactsForContactKey) {
		this.linkSubjectContactsForContactKey = linkSubjectContactsForContactKey;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<LinkSiteContact> getLinkSiteContacts() {
		return this.linkSiteContacts;
	}

	public void setLinkSiteContacts(Set<LinkSiteContact> linkSiteContacts) {
		this.linkSiteContacts = linkSiteContacts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<Address> getAddresses() {
		return this.addresses;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "personBySubjectId")
	public Set<LinkSubjectContact> getLinkSubjectContactsForSubjectKey() {
		return this.linkSubjectContactsForSubjectKey;
	}

	public void setLinkSubjectContactsForSubjectKey(
			Set<LinkSubjectContact> linkSubjectContactsForSubjectKey) {
		this.linkSubjectContactsForSubjectKey = linkSubjectContactsForSubjectKey;
	}

}