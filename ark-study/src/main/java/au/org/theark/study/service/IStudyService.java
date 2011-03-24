package au.org.theark.study.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.SiteVO;
import au.org.theark.core.vo.SubjectVO;




public interface IStudyService {
	/**
	 * This interface must be accessible to only super administrator role
	 * @param studyEntity
	 * @param selectedApplications
	 * @throws ArkSystemException
	 */
	public void createStudy(Study studyEntity, Set<String> selectedApplications) throws EntityExistsException,UnAuthorizedOperation, ArkSystemException;
	
	public void updateStudy(Study studyEntity,Set<String> selectedApplications) throws EntityCannotBeRemoved,EntityExistsException,UnAuthorizedOperation, ArkSystemException;
	
	/**
	 * Fetch the list of applications/modules the study is currently associated with from LDAP.
	 * @param studyNameCN
	 * @return
	 * @throws ArkSystemException
	 */
	public Set<String> getModulesLinkedToStudy(String studyNameCN) throws ArkSystemException;
	
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN, boolean isForDisplay) throws ArkSystemException;
	
	public void archiveStudy(Study studyEntity) throws UnAuthorizedOperation,StatusNotAvailableException, ArkSystemException;
	
	public void createSite(SiteVO siteVo) throws EntityExistsException,ArkSystemException; 
	
	public List<SiteVO> getSite(SiteVO siteVo);
	
	public void updateSite(SiteVO siteVo) throws ArkSystemException;
	
	/**
	 * Search for Study components with a certain criteria.
	 * @param studyCompCriteria
	 * @return
	 */
	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria) throws ArkSystemException;
	
	
	public void create(StudyComp sc) throws UnAuthorizedOperation, ArkSystemException;
	
	public void update(StudyComp studyComponent)throws UnAuthorizedOperation, ArkSystemException;
	
	public void create(Phone phone) throws ArkSystemException;
	
	public void update(Phone phone) throws ArkSystemException;

	public void delete(Phone phone) throws ArkSystemException;
	
	/**
	 * A method to create a Subject.
	 * @param subjectVO
	 */
	public void createSubject(SubjectVO subjectVO) throws ArkUniqueException;
	
	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException;
	
	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can represent
	 * a subject or contact.
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException;
	
	/**
	 * Look up the phones connected with the person(subject or contact)
	 * @param personId
	 * @return List<Phone>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId) throws EntityNotFoundException, ArkSystemException;
	
	/**
	 * Looks up the phones linked to a person and applies any filter supplied with the phone object.Used in Search Phone functionality.
	 * One can look up base don area code, phone type, phone number
	 * @param personId
	 * @param phone
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Phone> getPersonPhoneList(Long personId,Phone phone) throws EntityNotFoundException,ArkSystemException;
	
	/**
	 * Looks up the addresses linked to a person and applies any filter supplied with the address object.Used in Search Address functionality.
	 * @param personId
	 * @param address
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Address> getPersonAddressList(Long personId, Address address) throws EntityNotFoundException,ArkSystemException;
	
	
	public void create(Address address) throws ArkSystemException;
	
	public void update(Address address) throws ArkSystemException;
	
	public void delete(Address address) throws ArkSystemException;
	
	public void create(Consent consent) throws ArkSystemException;
	
	public void update(Consent consent) throws ArkSystemException, EntityNotFoundException;
	
	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException;
	
	public Consent getConsent(Long id) throws ArkSystemException;
	
	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException,ArkSystemException;
	
	public List<Consent> searchConsent(ConsentVO consentVO) throws EntityNotFoundException,ArkSystemException;
	
	public List<SubjectCustmFld> searchStudyFields(SubjectCustmFld subjectCustmFld);
	
	/**
	 * 
	 * @param consentFile
	 * @throws ArkSystemException
	 */
	public void create(ConsentFile consentFile) throws ArkSystemException;
	public void update(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException;
	public void delete(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException;
	public List<ConsentFile> searchConsentFile(ConsentFile consentFile)  throws EntityNotFoundException,ArkSystemException;
	
	public void create(Correspondences correspondence) throws ArkSystemException;
	public void update(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException;
	public void delete(Correspondences correspondence) throws ArkSystemException, EntityNotFoundException;
	public List<Correspondences> getPersonCorrespondenceList(Long personId, Correspondences correspondence) throws EntityNotFoundException, ArkSystemException;
	
	public List<CorrespondenceStatusType> getCorrespondenceStatusTypes();
	public List<CorrespondenceModeType> getCorrespondenceModeTypes();
	public List<CorrespondenceDirectionType> getCorrespondenceDirectionTypes();
	public List<CorrespondenceOutcomeType> getCorrespondenceOutcomeTypes();
	
	public void createPersonLastnameHistory(Person person);
	public void updatePersonLastnameHistory(Person person);
	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory);
	public String getPreviousLastname(Person person);
	public String getCurrentLastname(Person person);
	public List<PersonLastnameHistory> getLastnameHistory(Person person);

	public boolean personHasPreferredMailingAddress(Person person,Long currentAddressId);
	
	/**
	 * 
	 * @param subjectFile
	 * @throws ArkSystemException
	 */
	public void create(SubjectFile subjectFile) throws ArkSystemException;
	public void update(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException;
	public void delete(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException;
	public List<SubjectFile> searchSubjectFile(SubjectFile subjectFile)  throws EntityNotFoundException,ArkSystemException;
	
	public void delete(StudyComp studyComp) throws ArkSystemException, EntityCannotBeRemoved;
	
}
