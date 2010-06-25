package au.org.theark.study.model.entity;

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
import javax.persistence.UniqueConstraint;

/**
 * Study entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "STUDY", schema = "ETA", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
public class Study implements java.io.Serializable {

	// Fields

	private Long studyKey;
	private StudyStatus studyStatus;
	private String name;
	private String description;
	private Date dateOfApplication;
	private Long estimatedYearOfCompletion;
	private String chiefInvestigator;
	private String coInvestigator;
	private boolean autoGenerateSubjectKey;
	private Long subjectKeyStart;
	private String subjectIdPrefix;
	private String contactPerson;
	private String contactPersonPhone;
	private String ldapGroupName;
	private boolean autoConsent;
	private String subStudyBiospecimenPrefix;
	private Set<LinkStudySubstudy> linkStudySubstudiesForStudyKey = new HashSet<LinkStudySubstudy>(
			0);
	private Set<LinkStudyStudysite> linkStudyStudysites = new HashSet<LinkStudyStudysite>(
			0);
	private Set<StudyComp> studyComps = new HashSet<StudyComp>(0);
	private Set<SubjectCustmFld> subjectCustmFlds = new HashSet<SubjectCustmFld>(
			0);
	private Set<LinkSubjectStudycomp> linkSubjectStudycomps = new HashSet<LinkSubjectStudycomp>(
			0);
	private Set<LinkSubjectStudy> linkSubjectStudies = new HashSet<LinkSubjectStudy>(
			0);
	private Set<LinkSubjectContact> linkSubjectContacts = new HashSet<LinkSubjectContact>(
			0);
	private Set<LinkStudyStudycomp> linkStudyStudycomps = new HashSet<LinkStudyStudycomp>(
			0);
	private Set<LinkStudySubstudy> linkStudySubstudiesForSubstudyKey = new HashSet<LinkStudySubstudy>(
			0);

	// Constructors

	/** default constructor */
	public Study() {
	}

	/** minimal constructor */
	public Study(Long studyKey) {
		this.studyKey = studyKey;
	}

	/** full constructor */
	public Study(Long studyKey, StudyStatus studyStatus, String name,
			String description, Date dateOfApplication,
			Long estimatedYearOfCompletion, String chiefInvestigator,
			String coInvestigator, boolean autoGenerateSubjectKey,
			Long subjectKeyStart, String subjectIdPrefix, String contactPerson,
			String contactPersonPhone, String ldapGroupName,
			boolean autoConsent, String subStudyBiospecimenPrefix,
			Set<LinkStudySubstudy> linkStudySubstudiesForStudyKey,
			Set<LinkStudyStudysite> linkStudyStudysites,
			Set<StudyComp> studyComps, Set<SubjectCustmFld> subjectCustmFlds,
			Set<LinkSubjectStudycomp> linkSubjectStudycomps,
			Set<LinkSubjectStudy> linkSubjectStudies,
			Set<LinkSubjectContact> linkSubjectContacts,
			Set<LinkStudyStudycomp> linkStudyStudycomps,
			Set<LinkStudySubstudy> linkStudySubstudiesForSubstudyKey) {
		this.studyKey = studyKey;
		this.studyStatus = studyStatus;
		this.name = name;
		this.description = description;
		this.dateOfApplication = dateOfApplication;
		this.estimatedYearOfCompletion = estimatedYearOfCompletion;
		this.chiefInvestigator = chiefInvestigator;
		this.coInvestigator = coInvestigator;
		this.autoGenerateSubjectKey = autoGenerateSubjectKey;
		this.subjectKeyStart = subjectKeyStart;
		this.subjectIdPrefix = subjectIdPrefix;
		this.contactPerson = contactPerson;
		this.contactPersonPhone = contactPersonPhone;
		this.ldapGroupName = ldapGroupName;
		this.autoConsent = autoConsent;
		this.subStudyBiospecimenPrefix = subStudyBiospecimenPrefix;
		this.linkStudySubstudiesForStudyKey = linkStudySubstudiesForStudyKey;
		this.linkStudyStudysites = linkStudyStudysites;
		this.studyComps = studyComps;
		this.subjectCustmFlds = subjectCustmFlds;
		this.linkSubjectStudycomps = linkSubjectStudycomps;
		this.linkSubjectStudies = linkSubjectStudies;
		this.linkSubjectContacts = linkSubjectContacts;
		this.linkStudyStudycomps = linkStudyStudycomps;
		this.linkStudySubstudiesForSubstudyKey = linkStudySubstudiesForSubstudyKey;
	}

	// Property accessors
	
	
	@Id
	@SequenceGenerator(name="study_generator", sequenceName="STUDY_SEQUENCE")
	@GeneratedValue(strategy=GenerationType.AUTO, generator = "study_generator")
	@Column(name = "STUDY_KEY", unique = true, nullable = false, precision = 22, scale = 0)
	public Long getStudyKey() {
		return this.studyKey;
	}

	public void setStudyKey(Long studyKey) {
		this.studyKey = studyKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STUDY_STATUS_KEY")
	public StudyStatus getStudyStatus() {
		return this.studyStatus;
	}

	public void setStudyStatus(StudyStatus studyStatus) {
		this.studyStatus = studyStatus;
	}

	@Column(name = "NAME", unique = true, length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_OF_APPLICATION", length = 7)
	public Date getDateOfApplication() {
		return this.dateOfApplication;
	}

	public void setDateOfApplication(Date dateOfApplication) {
		this.dateOfApplication = dateOfApplication;
	}

	@Column(name = "ESTIMATED_YEAR_OF_COMPLETION", precision = 22, scale = 0)
	public Long getEstimatedYearOfCompletion() {
		return this.estimatedYearOfCompletion;
	}

	public void setEstimatedYearOfCompletion(Long estimatedYearOfCompletion) {
		this.estimatedYearOfCompletion = estimatedYearOfCompletion;
	}

	@Column(name = "CHIEF_INVESTIGATOR", length = 50)
	public String getChiefInvestigator() {
		return this.chiefInvestigator;
	}

	public void setChiefInvestigator(String chiefInvestigator) {
		this.chiefInvestigator = chiefInvestigator;
	}

	@Column(name = "CO_INVESTIGATOR", length = 50)
	public String getCoInvestigator() {
		return this.coInvestigator;
	}

	public void setCoInvestigator(String coInvestigator) {
		this.coInvestigator = coInvestigator;
	}

	@Column(name = "AUTO_GENERATE_SUBJECT_KEY", precision = 1, scale = 0)
	public boolean getAutoGenerateSubjectKey() {
		return this.autoGenerateSubjectKey;
	}

	public void setAutoGenerateSubjectKey(boolean autoGenerateSubjectKey) {
		this.autoGenerateSubjectKey = autoGenerateSubjectKey;
	}

	@Column(name = "SUBJECT_KEY_START", precision = 22, scale = 0)
	public Long getSubjectKeyStart() {
		return this.subjectKeyStart;
	}

	public void setSubjectKeyStart(Long subjectKeyStart) {
		this.subjectKeyStart = subjectKeyStart;
	}

	@Column(name = "SUBJECT_ID_PREFIX", length = 20)
	public String getSubjectIdPrefix() {
		return this.subjectIdPrefix;
	}

	public void setSubjectIdPrefix(String subjectIdPrefix) {
		this.subjectIdPrefix = subjectIdPrefix;
	}

	@Column(name = "CONTACT_PERSON", length = 50)
	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Column(name = "CONTACT_PERSON_PHONE", length = 20)
	public String getContactPersonPhone() {
		return this.contactPersonPhone;
	}

	public void setContactPersonPhone(String contactPersonPhone) {
		this.contactPersonPhone = contactPersonPhone;
	}

	@Column(name = "LDAP_GROUP_NAME", length = 100)
	public String getLdapGroupName() {
		return this.ldapGroupName;
	}

	public void setLdapGroupName(String ldapGroupName) {
		this.ldapGroupName = ldapGroupName;
	}

	@Column(name = "AUTO_CONSENT", precision = 1, scale = 0)
	public boolean getAutoConsent() {
		return this.autoConsent;
	}

	public void setAutoConsent(boolean autoConsent) {
		this.autoConsent = autoConsent;
	}

	@Column(name = "SUB_STUDY_BIOSPECIMEN_PREFIX", length = 20)
	public String getSubStudyBiospecimenPrefix() {
		return this.subStudyBiospecimenPrefix;
	}

	public void setSubStudyBiospecimenPrefix(String subStudyBiospecimenPrefix) {
		this.subStudyBiospecimenPrefix = subStudyBiospecimenPrefix;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "studyByStudyKey")
	public Set<LinkStudySubstudy> getLinkStudySubstudiesForStudyKey() {
		return this.linkStudySubstudiesForStudyKey;
	}

	public void setLinkStudySubstudiesForStudyKey(
			Set<LinkStudySubstudy> linkStudySubstudiesForStudyKey) {
		this.linkStudySubstudiesForStudyKey = linkStudySubstudiesForStudyKey;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<LinkStudyStudysite> getLinkStudyStudysites() {
		return this.linkStudyStudysites;
	}

	public void setLinkStudyStudysites(
			Set<LinkStudyStudysite> linkStudyStudysites) {
		this.linkStudyStudysites = linkStudyStudysites;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<StudyComp> getStudyComps() {
		return this.studyComps;
	}

	public void setStudyComps(Set<StudyComp> studyComps) {
		this.studyComps = studyComps;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<SubjectCustmFld> getSubjectCustmFlds() {
		return this.subjectCustmFlds;
	}

	public void setSubjectCustmFlds(Set<SubjectCustmFld> subjectCustmFlds) {
		this.subjectCustmFlds = subjectCustmFlds;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<LinkSubjectStudycomp> getLinkSubjectStudycomps() {
		return this.linkSubjectStudycomps;
	}

	public void setLinkSubjectStudycomps(
			Set<LinkSubjectStudycomp> linkSubjectStudycomps) {
		this.linkSubjectStudycomps = linkSubjectStudycomps;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<LinkSubjectStudy> getLinkSubjectStudies() {
		return this.linkSubjectStudies;
	}

	public void setLinkSubjectStudies(Set<LinkSubjectStudy> linkSubjectStudies) {
		this.linkSubjectStudies = linkSubjectStudies;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<LinkSubjectContact> getLinkSubjectContacts() {
		return this.linkSubjectContacts;
	}

	public void setLinkSubjectContacts(
			Set<LinkSubjectContact> linkSubjectContacts) {
		this.linkSubjectContacts = linkSubjectContacts;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "study")
	public Set<LinkStudyStudycomp> getLinkStudyStudycomps() {
		return this.linkStudyStudycomps;
	}

	public void setLinkStudyStudycomps(
			Set<LinkStudyStudycomp> linkStudyStudycomps) {
		this.linkStudyStudycomps = linkStudyStudycomps;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "studyBySubstudyKey")
	public Set<LinkStudySubstudy> getLinkStudySubstudiesForSubstudyKey() {
		return this.linkStudySubstudiesForSubstudyKey;
	}

	public void setLinkStudySubstudiesForSubstudyKey(
			Set<LinkStudySubstudy> linkStudySubstudiesForSubstudyKey) {
		this.linkStudySubstudiesForSubstudyKey = linkStudySubstudiesForSubstudyKey;
	}

}