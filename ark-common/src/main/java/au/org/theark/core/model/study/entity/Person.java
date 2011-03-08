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
	private PersonContactMethod personContactMethod;
	private String preferredEmail;
	private String otherEmail;
	
	private Set<LinkSubjectStudy> linkSubjectStudies = new HashSet<LinkSubjectStudy>(0);
	private Set<Phone> phones = new HashSet<Phone>(0);
	private Set<LinkSubjectStudycomp> linkSubjectStudycomps = new HashSet<LinkSubjectStudycomp>(0);
	private Set<LinkSubjectContact> linkSubjectContactsForContactKey = new HashSet<LinkSubjectContact>(0);
	private Set<LinkSiteContact> linkSiteContacts = new HashSet<LinkSiteContact>(0);
	private Set<PersonAddress> personAddresses = new HashSet<PersonAddress>(0);
	private Set<LinkSubjectContact> linkSubjectContactsForSubjectKey = new HashSet<LinkSubjectContact>(0);

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
			Set<LinkSiteContact> linkSiteContacts, Set<PersonAddress> personAddresses,
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
		this.personAddresses = personAddresses;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "personBySubjectId")
	public Set<LinkSubjectContact> getLinkSubjectContactsForSubjectKey() {
		return this.linkSubjectContactsForSubjectKey;
	}

	public void setLinkSubjectContactsForSubjectKey(
			Set<LinkSubjectContact> linkSubjectContactsForSubjectKey) {
		this.linkSubjectContactsForSubjectKey = linkSubjectContactsForSubjectKey;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public Set<PersonAddress> getPersonAddresses() {
		return personAddresses;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "person")
	public void setPersonAddresses(Set<PersonAddress> personAddresses) {
		this.personAddresses = personAddresses;
	}

	/**
	 * @param personContactMethod the personContactMethod to set
	 */
	public void setPersonContactMethod(PersonContactMethod personContactMethod)
	{
		this.personContactMethod = personContactMethod;
	}

	/**
	 * @return the personContactMethod
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERSON_CONTACT_METHOD_ID")
	public PersonContactMethod getPersonContactMethod()
	{
		return personContactMethod;
	}

	/**
	 * @param preferredEmail the preferredEmail to set
	 */
	public void setPreferredEmail(String preferredEmail)
	{
		this.preferredEmail = preferredEmail;
	}

	/**
	 * @return the preferredEmail
	 */
	@Column(name = "PREFERRED_EMAIL", length = 150)
	public String getPreferredEmail()
	{
		return preferredEmail;
	}

	/**
	 * @param otherEmail the otherEmail to set
	 */
	public void setOtherEmail(String otherEmail)
	{
		this.otherEmail = otherEmail;
	}

	/**
	 * @return the otherEmail
	 */
	@Column(name = "OTHER_EMAIL", length = 150)
	public String getOtherEmail()
	{
		return otherEmail;
	}
}