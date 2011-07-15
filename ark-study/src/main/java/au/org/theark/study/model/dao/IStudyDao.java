package au.org.theark.study.model.dao;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.CannotRemoveArkModuleException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.CorrespondenceAttachment;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.study.entity.DelimiterType;
import au.org.theark.core.model.study.entity.FileFormat;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.StudyUpload;
import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.SubjectVO;

public interface IStudyDao {

	public void create(Study study);

	public void create(Study study, Collection<ArkModule> selectedApplications);

	public void updateStudy(Study study, Collection<ArkModule> selectedApplications) throws CannotRemoveArkModuleException;

	public void create(StudyComp studyComponent) throws ArkSystemException, EntityExistsException;

	public void update(StudyComp studyComponent) throws ArkSystemException, EntityExistsException;

	public void delete(StudyComp studyComp) throws ArkSystemException, EntityCannotBeRemoved;

	/**
	 * Interface to get a list of Study Status reference data from the backend. These study status' are no associated with a study as such but can be
	 * used for displaying a list of options for a particular study.
	 * 
	 * @return
	 */
	public List<StudyStatus> getListOfStudyStatus();

	public Study getStudy(Long id);

	public void updateStudy(Study study);

	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException;

	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria);

	/**
	 * A look up that returns a list of All Phone Types. Mobile, Land etc In the event that there is a database/runtime error it is wrapped into a
	 * ArkSystemException and returned
	 * 
	 * @return List<PhoneType>
	 */
	public List<PhoneType> getListOfPhoneType();

	public void create(Phone phone);

	public void update(Phone phone);

	public void delete(Phone phone);

	public Collection<TitleType> getTitleType();

	public Collection<VitalStatus> getVitalStatus();

	public Collection<GenderType> getGenderType();

	public Collection<SubjectStatus> getSubjectStatus();

	public void createSubject(SubjectVO subjectVO) throws ArkUniqueException, ArkSubjectInsertException;

	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException;

	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can
	 * represent a subject or contact.
	 * 
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Look up the phones connected with the person(subject or contact)
	 * 
	 * @param personId
	 * @return List<Phone>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Looks up the phones linked to a person and applies any filter supplied with the phone object.Used in Search Phone functionality. One can look up
	 * base don area code, phone type, phone number
	 * 
	 * @param personId
	 * @param phone
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId, Phone phone) throws EntityNotFoundException, ArkSystemException;

	/**
	 * Looks up the addresses linked to a person and applies any filter supplied with the address object.Used in Search Address functionality.
	 * 
	 * @param personId
	 * @param address
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Address> getPersonAddressList(Long personId, Address address) throws EntityNotFoundException, ArkSystemException;

	/**
	 * 
	 * @param address
	 * @throws ArkSystemException
	 */
	public void create(Address address) throws ArkSystemException;

	/**
	 * 
	 * @param address
	 * @throws ArkSystemException
	 */
	public void update(Address address) throws ArkSystemException;

	/**
	 * 
	 * @param address
	 * @throws ArkSystemException
	 */
	public void delete(Address address) throws ArkSystemException;

	/**
	 * 
	 * @param consent
	 * @throws ArkSystemException
	 */
	public void create(Consent consent) throws ArkSystemException;

	public void update(Consent consent) throws ArkSystemException, EntityNotFoundException;

	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException;

	public Consent getConsent(Long id) throws ArkSystemException;

	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException, ArkSystemException;

	public List<Consent> searchConsent(ConsentVO consentVo) throws EntityNotFoundException, ArkSystemException;

	public List<SubjectCustmFld> searchStudyFields(SubjectCustmFld subjectCustmFld);

	/**
	 * 
	 * @param consentFile
	 * @throws ArkSystemException
	 */
	public void create(ConsentFile consentFile) throws ArkSystemException;

	public void update(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException;

	public void delete(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException;

	public List<ConsentFile> searchConsentFile(ConsentFile consentFile) throws EntityNotFoundException, ArkSystemException;

	public void createPersonLastnameHistory(Person person);

	public void updatePersonLastnameHistory(Person person);

	/**
	 * Returns previousSurnameHistory
	 * 
	 * @return
	 */
	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory);

	/**
	 * Returns previous surname
	 * 
	 * @return
	 */
	public String getPreviousLastname(Person person);

	/**
	 * Returns current surname
	 * 
	 * @return
	 */
	public String getCurrentLastname(Person person);

	public boolean personHasPreferredMailingAddress(Person person, Long currentAddressId);

	/**
	 * Returns a list of PersonSurnameHistory
	 * 
	 * @return
	 */
	public List<PersonLastnameHistory> getLastnameHistory(Person person);

	/**
	 * 
	 * @param subjectFile
	 * @throws ArkSystemException
	 */
	public void create(SubjectFile subjectFile) throws ArkSystemException;

	public void update(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException;

	public void delete(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException;

	public List<SubjectFile> searchSubjectFile(SubjectFile subjectFile) throws EntityNotFoundException, ArkSystemException;

	public void create(Correspondences correspondence) throws ArkSystemException;

	public void update(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException;

	public void delete(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException;

	public List<Correspondences> getPersonCorrespondenceList(Long id, Correspondences correspondence) throws ArkSystemException, EntityNotFoundException;

	public void create(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException;

	public void update(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException, EntityNotFoundException;

	public void delete(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException, EntityNotFoundException;

	public List<CorrespondenceAttachment> searchCorrespondenceAttachment(CorrespondenceAttachment correspondenceAttachment) throws ArkSystemException, EntityNotFoundException;

	public List<CorrespondenceStatusType> getCorrespondenceStatusTypes();

	public List<CorrespondenceModeType> getCorrespondenceModeTypes();

	public List<CorrespondenceDirectionType> getCorrespondenceDirectionTypes();

	public List<CorrespondenceOutcomeType> getCorrespondenceOutcomeTypes();

	public Collection<FileFormat> getFileFormats();

	public Collection<DelimiterType> getDelimiterTypes();

	public Collection<StudyUpload> searchUpload(StudyUpload searchUpload);

	public void createUpload(StudyUpload studyUpload);

	public void deleteUpload(StudyUpload studyUpload);

	public void updateUpload(StudyUpload studyUpload);

	public void batchInsertSubjects(Collection<SubjectVO> subjectVoCollection) throws ArkUniqueException, ArkSubjectInsertException;

	public void batchUpdateSubjects(Collection<SubjectVO> subjectVoCollection) throws ArkUniqueException, ArkSubjectInsertException;

	/**
	 * Returns a Collection of ArkUser entities who are linked to a particular study.
	 * 
	 * @param study
	 * @return Collection<ArkUser>
	 */
	public Collection<ArkUser> lookupArkUser(Study study);

	public LinkSubjectStudy getSubjectLinkedToStudy(Long personId, Study study) throws EntityNotFoundException, ArkSystemException;

	public DelimiterType getDelimiterType(Long id);

	public FileFormat getFileFormatByName(String fileFormatName);
}
