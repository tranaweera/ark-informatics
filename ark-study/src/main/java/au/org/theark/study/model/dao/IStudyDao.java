package au.org.theark.study.model.dao;

import java.util.Collection;
import java.util.List;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.SubjectVO;


public interface IStudyDao {

	public void create(Study study);
	
	public void create(StudyComp studyComponent) throws ArkSystemException;
	
	public void update(StudyComp studyComponent) throws ArkSystemException;
	
	/**
	 * Interface to get a list of Study Status reference data from the backend.
	 * These study status' are no associated with a study as such but can be used for
	 * displaying a list of options for a particular study.
	 * @return
	 */
	public List<StudyStatus> getListOfStudyStatus();
	
	public Study getStudy(Long id);
	
	public void updateStudy(Study study);
	
	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException;
	
	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria);
	
	/**
	 * A look up that returns a list of All Phone Types. Mobile, Land etc
	 * In the event that there is a database/runtime error it is wrapped into a ArkSystemException and returned
	 * @return List<PhoneType>
	 */
	public List<PhoneType> getListOfPhoneType();
	
	public void create(Phone phone) throws ArkSystemException;
	
	public void update(Phone phone) throws ArkSystemException;;
	
	public Collection<TitleType> getTitleType();
	
	public Collection<VitalStatus> getVitalStatus();
	
	public Collection<GenderType> getGenderType();
	
	public Collection<SubjectStatus> getSubjectStatus();
	
	public void createSubject(SubjectVO subjectVO);
	
	public void updateSubject(SubjectVO subjectVO);
	
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
	
	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException,ArkSystemException;
	
	public List<Consent> searchConsent(ConsentVO consentVo) throws EntityNotFoundException,ArkSystemException;
	
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

	
}
