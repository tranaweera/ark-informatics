package au.org.theark.study.model.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.dao.HibernateSessionDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.Consent;
import au.org.theark.core.model.study.entity.CorrespondenceDirectionType;
import au.org.theark.core.model.study.entity.CorrespondenceModeType;
import au.org.theark.core.model.study.entity.CorrespondenceOutcomeType;
import au.org.theark.core.model.study.entity.CorrespondenceStatusType;
import au.org.theark.core.model.study.entity.Correspondences;
import au.org.theark.core.model.study.entity.ConsentFile;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.LinkSubjectStudycomp;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectCustmFld;
import au.org.theark.core.model.study.entity.SubjectFile;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.vo.ConsentVO;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.study.service.Constants;

@Repository("studyDao")
public class StudyDao extends HibernateSessionDao implements IStudyDao {


	private static Logger log = LoggerFactory.getLogger(StudyDao.class);
	private Subject	currentUser;
	private Date		dateNow;

	public void create(Study study) {
		getSession().save(study);
	}

	public List<StudyStatus> getListOfStudyStatus() {

		Example studyStatus = Example.create(new StudyStatus());
		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatus);
		return   studyStatusCriteria.list();
	}
	
	/**
	 * Given a status name will return the StudyStatus object.
	 */
	public StudyStatus getStudyStatus(String statusName) throws StatusNotAvailableException{
		StudyStatus studyStatus = new StudyStatus();
		studyStatus.setName("Archive");
		Example studyStatusExample = Example.create(studyStatus);
		
		Criteria studyStatusCriteria = getSession().createCriteria(StudyStatus.class).add(studyStatusExample);
		if(studyStatusCriteria != null && studyStatusCriteria.list() != null && studyStatusCriteria.list().size() > 0){
			return (StudyStatus)studyStatusCriteria.list().get(0);	
		}else{
			log.error("Study Status Table maybe out of synch. Please check if it has an entry for Archive status");
			System.out.println("Cannot locate a study status with " + statusName + " in the database");
			throw new StatusNotAvailableException();
		}
	}
	
	public Study getStudy(Long id){
		Study study =  (Study)getSession().get(Study.class, id);
		return study;
	}
	
	public void updateStudy(Study studyEntity){
		// session.update and session.flush required as Blob read/writes are used, and InputStream may cause NullPointers when closed incorrectly
		Session session = getSession();
		session.update(studyEntity);
		session.flush();
		session.refresh(studyEntity);
	}

	/* (non-Javadoc)
	 * @see au.org.theark.study.model.dao.IStudyDao#create(au.org.theark.study.model.entity.StudyComp)
	 */
	public void create(StudyComp studyComponent) throws ArkSystemException {
		try{
			getSession().save(studyComponent);	
		}catch(HibernateException hibException){
			log.error("A hibernate exception occured. Cannot create the study component ID: " + studyComponent.getName() + " Cause " + hibException.getStackTrace());
			throw new ArkSystemException("Cannot create Study component");
		}
		
		
	}
	
	public void update(StudyComp studyComponent) throws ArkSystemException{
		try{
			getSession().update(studyComponent);	
		}catch(HibernateException hibException){
			log.error("A hibernate exception occured. Cannot update the study component ID: " + studyComponent.getId() + " Cause " + hibException.getStackTrace());
			throw new ArkSystemException("Cannot update Study component due to system error");
		}
	}
	
	public void delete(StudyComp studyComp) throws ArkSystemException, EntityCannotBeRemoved{
		try{
			if(!isStudyComponentUsed(studyComp)){
				getSession().delete(studyComp);	
			}else{
				throw new EntityCannotBeRemoved("The Study component is used and cannot be removed.");
			}
		}catch(HibernateException hibException){
			log.error("A hibernate exception occured. Cannot detele the study component ID: " + studyComp.getId() + " Cause " + hibException.getStackTrace());
			throw new ArkSystemException("Cannot update Study component due to system error");
		}
		
	}
	
	public boolean isStudyComponentUsed(StudyComp studyComp){
		boolean flag = false;
		Criteria criteria = getSession().createCriteria(Consent.class);
		criteria.add(Restrictions.eq("studyComp", studyComp));
		criteria.setProjection(Projections.rowCount());
		Integer i  = (Integer)criteria.list().get(0);
		if(i > 0){
			flag = true;
		}
		return flag;
	}
	
	public List<StudyComp> searchStudyComp(StudyComp studyCompCriteria){
		
		Criteria criteria = getSession().createCriteria(StudyComp.class);
		
		if(studyCompCriteria.getId() != null){
			criteria.add(Restrictions.eq(Constants.ID,studyCompCriteria.getId()));	
		}
		
		if(studyCompCriteria.getName() != null){
			criteria.add(Restrictions.eq(Constants.STUDY_COMP_NAME,studyCompCriteria.getName()));
		}
		
		if(studyCompCriteria.getKeyword() != null){
		
			criteria.add(Restrictions.ilike(Constants.STUDY_COMP_KEYWORD,studyCompCriteria.getKeyword(),MatchMode.ANYWHERE));
		}
		List<StudyComp> list =  criteria.list();
		return list;
	}
	
	public List<PhoneType> getListOfPhoneType() {
		Example phoneTypeExample = Example.create(new PhoneType());
		Criteria criteria = getSession().createCriteria(PhoneType.class).add(phoneTypeExample);
		return   criteria.list();
	}
	
	public void create(Phone phone) throws ArkSystemException{
		try{
			getSession().save(phone);	
		}catch(HibernateException hibException){
			log.error("A hibernate exception occured. Cannot create the Phone record. Cause: " + hibException.getStackTrace());
			throw new ArkSystemException("Unable to create a Phone record.");
		}
	}
	
	
	public void update(Phone phone) throws ArkSystemException{
		try{
			getSession().update(phone);	
		}catch(HibernateException hibException){
			log.error("A hibernate exception occured. Cannot update the Phone record. Cause: " + hibException.getStackTrace());
			throw new ArkSystemException("Unable to create a Phone record.");
		}
	}
	
	public void delete(Phone phone) throws ArkSystemException {
		try{
			getSession().delete(phone);			
		}catch(HibernateException someHibernateException){
			log.error("An Exception occured while trying to delete this Phone record. Cause: " + someHibernateException.getStackTrace());
			throw new ArkSystemException("Unable to delete a Phone record.");
		}
	}
	
	public Collection<TitleType> getTitleType(){
		Example example = Example.create(new TitleType());
		Criteria criteria = getSession().createCriteria(TitleType.class).add(example);
		return criteria.list();
	}
	public Collection<VitalStatus> getVitalStatus(){
		Example example = Example.create(new VitalStatus());
		Criteria criteria = getSession().createCriteria(VitalStatus.class).add(example);
		return criteria.list();
	}
	
	public Collection<GenderType> getGenderType(){
		Example example = Example.create(new GenderType());
		Criteria criteria = getSession().createCriteria(GenderType.class).add(example);
		return criteria.list();
	}
	
	public void createSubject(SubjectVO subjectVO) throws ArkUniqueException{
			//Add Business Validations here as well apart from UI validation
		if(isSubjectUIDUnique(subjectVO.getSubjectStudy().getSubjectUID(),subjectVO.getSubjectStudy().getStudy().getId(), "Insert")){
			Session session = getSession();
			Person person  = subjectVO.getSubjectStudy().getPerson();
			session.save(person);
			
			PersonLastnameHistory personLastNameHistory = new PersonLastnameHistory();
			if(person.getLastName() != null)
			{
				personLastNameHistory.setPerson(person);
				personLastNameHistory.setLastName(person.getLastName());
				session.save(personLastNameHistory);	
			}
			
			// Update subjectPreviousLastname
			subjectVO.setSubjectPreviousLastname(getPreviousLastname(person));
			
			LinkSubjectStudy linkSubjectStudy = subjectVO.getSubjectStudy();
			session.save(linkSubjectStudy);//The hibernate session is the same. This should be automatically bound with Spring's OpenSessionInViewFilter
			
			// Auto-generate SubjectUID
			if(subjectVO.getSubjectStudy().getStudy().getAutoGenerateSubjectUid())
			{
				String subjectUID = getNextGeneratedSubjectUID(subjectVO.getSubjectStudy().getStudy());
				linkSubjectStudy.setSubjectUID(subjectUID);
				session.update(linkSubjectStudy);
			}
		}
		else
		{
			throw new ArkUniqueException("Subject UID must be unique");
		}
	}

	public void updateSubject(SubjectVO subjectVO) throws ArkUniqueException{
	
		// TODO: Needed?
		if(true){			
			Session session = getSession();
			Person person  = subjectVO.getSubjectStudy().getPerson();
			session.update(person);// Update Person and associated Phones
			
			PersonLastnameHistory personLastNameHistory = new PersonLastnameHistory();
			if(person.getLastName() != null)
			{
				personLastNameHistory.setPerson(person);
				personLastNameHistory.setLastName(person.getLastName());
				session.save(personLastNameHistory);	
			}
			
			String currentLastName = getCurrentLastname(person);
			
			if(currentLastName == null || (currentLastName != null && !currentLastName.equalsIgnoreCase(person.getLastName())))
				session.save(personLastNameHistory);
			
			// Update subjectPreviousLastname
			subjectVO.setSubjectPreviousLastname(getPreviousLastname(person));
			
			LinkSubjectStudy linkSubjectStudy = subjectVO.getSubjectStudy();
			session.update(linkSubjectStudy);
		}else{
			throw new ArkUniqueException("Subject UID must be unique");
		}		
	}
	
	public String getNextGeneratedSubjectUID(Study study)
	{
		String subjectUidPrefix = new String("");
		String subjectUidToken = new String("");
		String subjectUidPaddedIncrementor = new String("");
		String subjectUidPadChar = new String("0");
		String nextIncrementedsubjectUid = new String("");
		String subjectUid = new String("");
		
		if(study.getId() != null && study.getAutoGenerateSubjectUid() != null)
		{
			if(study.getSubjectUidPrefix() != null)
				subjectUidPrefix = study.getSubjectUidPrefix();
			
			if(study.getSubjectUidToken() != null)
				subjectUidToken = study.getSubjectUidToken();
			
			if(study.getSubjectUidPadChar() != null)
			{
				subjectUidPadChar = study.getSubjectUidPadChar().getName().trim();	
			}
			
			if(study.getSubjectUidStart() != null)
			{
				Long subjectUidStart = study.getSubjectUidStart();
				//TODO: Work out a safer method of getting next incremented number
				Long incrementedValue = subjectUidStart +  getSubjectCount(study)-1;
				nextIncrementedsubjectUid = incrementedValue.toString();
			}
			
			int size = Integer.parseInt(subjectUidPadChar);
			subjectUidPaddedIncrementor = StringUtils.leftPad(nextIncrementedsubjectUid, size, "0");
			subjectUid = subjectUidPrefix + subjectUidToken + subjectUidPaddedIncrementor;
		}
		else
		{
			subjectUid = null;
		}
		return subjectUid;
	}
	
	public Long getSubjectCount(Study study)
	{
		Long subjectCount = new Long(0);
	   if(study.getId() != null)
	   {
		   Criteria criteria = getSession().createCriteria(LinkSubjectStudy.class);
			criteria.add(Restrictions.eq("study", study));
			
			List<LinkSubjectStudy> listOfSubjects =  (List<LinkSubjectStudy>) criteria.list();
			subjectCount = new Long(listOfSubjects.size());
	   }
	   
		return subjectCount;
	}
	
	public Collection<SubjectStatus> getSubjectStatus(){
		
		Example example = Example.create(new SubjectStatus());
		Criteria criteria = getSession().createCriteria(SubjectStatus.class).add(example);
		return criteria.list();
	
	}

	public LinkSubjectStudy getLinkSubjectStudy(Long id) throws EntityNotFoundException{
		
		Criteria linkSubjectStudyCriteria =  getSession().createCriteria(LinkSubjectStudy.class);
		linkSubjectStudyCriteria.add(Restrictions.eq(Constants.ID,id));
		List<LinkSubjectStudy> listOfSubjects = linkSubjectStudyCriteria.list();
		if(listOfSubjects != null && listOfSubjects.size() > 0){
			return listOfSubjects.get(0);
		}else{
			throw new EntityNotFoundException("The entity with id" + id.toString() +" cannot be found.");
		}
	}

	/**
	 * Look up a Person based on the supplied Long ID that represents a Person primary key. This id is the primary key of the Person table that can represent
	 * a subject or contact.
	 * @param personId
	 * @return
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public Person getPerson(Long personId) throws EntityNotFoundException, ArkSystemException{
		
		Criteria personCriteria =  getSession().createCriteria(Person.class);
		personCriteria.add(Restrictions.eq(Constants.ID,personId));
		List<Person> listOfPerson = personCriteria.list();
		if(listOfPerson != null && listOfPerson.size() > 0){
			return listOfPerson.get(0);
		}else{
			throw new EntityNotFoundException("The entity with id" + personId.toString() +" cannot be found.");
		}
	}
	
	public List<Phone> getPersonPhoneList(Long personId) throws EntityNotFoundException, ArkSystemException{
		Criteria phoneCriteria =  getSession().createCriteria(Phone.class);
		phoneCriteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		List<Phone> personPhoneList = phoneCriteria.list();
		log.info("Number of phones fetched " + personPhoneList.size() + "  Person Id" + personId.intValue());
		if(personPhoneList == null && personPhoneList.size() == 0){
			throw new EntityNotFoundException("The entity with id" + personId.toString() +" cannot be found.");
		}
		log.info("Number of phone items retrieved for person Id " + personId + " " + personPhoneList.size());
		return personPhoneList;
	}
	
	public List<Phone> getPersonPhoneList(Long personId,Phone phone) throws EntityNotFoundException,ArkSystemException{
		
		Criteria phoneCriteria =  getSession().createCriteria(Phone.class);
	
		
		if(personId != null){
			phoneCriteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		}
		
		if(phone != null){
			
			if( phone.getId() != null){
				phoneCriteria.add(Restrictions.eq(Constants.PHONE_ID, phone.getId()));
			}

			if( phone.getPhoneNumber() != null){
				phoneCriteria.add(Restrictions.ilike(Constants.PHONE_NUMBER, phone.getPhoneNumber()));
			}

			if( phone.getPhoneType() != null){
				phoneCriteria.add(Restrictions.eq(Constants.PHONE_TYPE, phone.getPhoneType()));
			}
			
			if( phone.getAreaCode() != null){
				phoneCriteria.add(Restrictions.eq(Constants.AREA_CODE, phone.getAreaCode()));
			}
			
		}
		
		List<Phone> personPhoneList = phoneCriteria.list();
		log.info("Number of phones fetched " + personPhoneList.size() + "  Person Id" + personId.intValue());
		if(personPhoneList == null && personPhoneList.size() == 0){
			throw new EntityNotFoundException("The entity with id" + personId.toString() +" cannot be found.");
		}
		return personPhoneList;
	}
	
	/**
	 * Looks up all the addresses for a person.
	 * @param personId
	 * @param address
	 * @return List<Address>
	 * @throws EntityNotFoundException
	 * @throws ArkSystemException
	 */
	public List<Address> getPersonAddressList(Long personId, Address address) throws EntityNotFoundException,ArkSystemException{
		
		Criteria criteria =  getSession().createCriteria(Address.class);
		
		if(personId != null){
			criteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		}
		
		if(address != null){
			//Add criteria for address
			if(address.getStreetAddress() != null){
				criteria.add(Restrictions.ilike(Constants.STREET_ADDRESS, address.getStreetAddress(),MatchMode.ANYWHERE));
			}
			
			if(address.getCountry() != null){
				criteria.add(Restrictions.eq(Constants.COUNTRY_NAME, address.getCountry()));
			}
			
			if(address.getPostCode() != null){
				criteria.add(Restrictions.eq(Constants.POST_CODE, address.getPostCode()));
			}
			
			if(address.getCity() != null){
				criteria.add(Restrictions.ilike(Constants.CITY, address.getCity()));
			}
			
			if(address.getCountryState() != null ){
				criteria.add(Restrictions.eq(Constants.COUNTRY_STATE_NAME, address.getCountryState()));
			}
			
			if(address.getAddressType() != null){
				criteria.add(Restrictions.eq(Constants.ADDRESS_TYPE, address.getAddressType()));
			}
		}
		
		List<Address> personAddressList = criteria.list();
		if(personAddressList == null && personAddressList.size() == 0){
			throw new EntityNotFoundException("The entity with id" + personId.toString() +" cannot be found.");		
		}
		return personAddressList;
	}
	
	
	public void create(Address address) throws ArkSystemException{
		Session session = getSession();
		session.save(address);
	}
	
	public void update(Address address) throws ArkSystemException{
		Session session = getSession();
		session.update(address);
	}
	
	public void delete(Address address) throws ArkSystemException{
		
		getSession().delete(address);
	}
	
	
	public void create(Consent consent) throws ArkSystemException{
		try{
			Session session = getSession();
			session.save(consent);	

		}catch(HibernateException hibException){
			log.error("An exception occured while creating a consent " + hibException.getStackTrace());
			throw new ArkSystemException("Could not create the consent.");
		}
	}
	
	public void update(Consent consent) throws ArkSystemException,EntityNotFoundException{
		try{
			Session session = getSession();
			session.update(consent);	
		}catch(HibernateException someHibernateException){
			log.error("An Exception occured while trying to update this consent " + someHibernateException.getStackTrace());
		}catch(Exception e){
			log.error("An Exception occured while trying to update this consent " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}
		
	}
	
	/**
	 * If a consent is not in a state where it can be deleted then remove it. It can be in a different status before it can be removed.
	 * @param consent
	 * @throws ArkSystemException
	 */
	public void delete(Consent consent) throws ArkSystemException, EntityNotFoundException{
		try{
			Session session = getSession();
			consent = (Consent)session.get(Consent.class,consent.getId());	
			if(consent != null){
				getSession().delete(consent);	
			}else{
				throw new EntityNotFoundException("The Consent record you tried to remove does not exist in the Ark System");
			}
			
		}catch(HibernateException someHibernateException){
			log.error("An Exception occured while trying to delete this consent " + someHibernateException.getStackTrace());
		}catch(Exception e){
			log.error("An Exception occured while trying to delete this consent " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}
	}
	
	public List<Consent> searchConsent(Consent consent) throws EntityNotFoundException,ArkSystemException{
		
		Criteria criteria =  getSession().createCriteria(Consent.class);
		if(consent != null){
			
			criteria.add(Restrictions.eq("subject.id", consent.getSubject().getId()));
			criteria.add(Restrictions.eq("study.id", consent.getStudy().getId()));
			
			if(consent.getStudyComp() != null){
				criteria.add(Restrictions.eq("studyComp", consent.getStudyComp()));	
			}
			
			if(consent.getStudyComponentStatus() != null){
				criteria.add(Restrictions.eq("studyComponentStatus", consent.getStudyComponentStatus()));
			}
			
			if(consent.getConsentedBy() != null){
				criteria.add(Restrictions.ilike("consentedBy", consent.getConsentedBy(),MatchMode.ANYWHERE));
			}
			
			if(consent.getConsentStatus() != null){
				criteria.add(Restrictions.eq("consentStatus", consent.getConsentStatus()));
			}
		
			if(consent.getConsentDate() != null){
				criteria.add(Restrictions.eq("consentDate", consent.getConsentDate()));
			}
			
			if(consent.getConsentType() != null){
				criteria.add(Restrictions.eq("consentType", consent.getConsentType()));
			}
			
		}
		
		return criteria.list();
	}
	
	public List<Consent> searchConsent(ConsentVO consentVO) throws EntityNotFoundException,ArkSystemException{
		
		Criteria criteria =  getSession().createCriteria(Consent.class);
		if(consentVO != null){
			
			criteria.add(Restrictions.eq("subject.id", consentVO.getConsent().getSubject().getId()));
			criteria.add(Restrictions.eq("study.id",  consentVO.getConsent().getStudy().getId()));
			
			if( consentVO.getConsent().getStudyComp() != null){
				criteria.add(Restrictions.eq("studyComp",  consentVO.getConsent().getStudyComp()));	
			}
			
			if( consentVO.getConsent().getStudyComponentStatus() != null){
				criteria.add(Restrictions.eq("studyComponentStatus",  consentVO.getConsent().getStudyComponentStatus()));
			}
			
			if( consentVO.getConsent().getConsentedBy() != null){
				criteria.add(Restrictions.ilike("consentedBy",  consentVO.getConsent().getConsentedBy(),MatchMode.ANYWHERE));
			}
			
			if( consentVO.getConsent().getConsentStatus() != null){
				criteria.add(Restrictions.eq("consentStatus",  consentVO.getConsent().getConsentStatus()));
			}
		
			if( consentVO.getConsent().getConsentDate() != null){
				criteria.add(Restrictions.between("consentDate",  consentVO.getConsent().getConsentDate(), consentVO.getConsentDateEnd()));
			}
			
			if( consentVO.getConsent().getConsentType() != null){
				criteria.add(Restrictions.eq("consentType",  consentVO.getConsent().getConsentType()));
			}
			
		}
		List<Consent> list = criteria.list();
		return list;
	}
	
	public List<SubjectCustmFld> searchStudyFields(SubjectCustmFld subjectCustmFld){
		
		Criteria criteria = getSession().createCriteria(SubjectCustmFld.class);
		if(subjectCustmFld != null){
			
		}
		List<SubjectCustmFld> list = criteria.list();
		return list;
	}


	public void create(Correspondences correspondence)
			throws ArkSystemException {
		
		try{
			getSession().save(correspondence);
		}catch(HibernateException ex) {
			log.error("A Hibernate exception occurred when creating a correspondence record. Cause: " + ex.getStackTrace());
			throw new ArkSystemException("Unable to create a correspondence record.");
		}
		
	}

	
	public void update(Correspondences correspondence)
			throws ArkSystemException, EntityNotFoundException {

		try{
			getSession().update(correspondence);
		}catch(HibernateException ex) {
			log.error("A Hibernate exception occurred when updating a correspondence record. Cause: " + ex.getStackTrace());
			throw new ArkSystemException("Unable to update a correspondence record.");
		}
		
	}
	
	
	public void delete(Correspondences correspondence)
			throws ArkSystemException, EntityNotFoundException {
		
		try{
			getSession().update(correspondence);
		}catch(HibernateException ex) {
			log.error("A Hibernate exception occurred when deleting a correspondence record. Cause: " + ex.getStackTrace());
			throw new ArkSystemException("Unable to delete a correspondence record.");
		}
		
	}


	public List<Correspondences> getPersonCorrespondenceList(Long personId,
			Correspondences correspondence) throws ArkSystemException,
			EntityNotFoundException {
		
		Criteria criteria = getSession().createCriteria(Correspondences.class);
		
		if(personId != null) {
			criteria.add(Restrictions.eq(Constants.PERSON_PERSON_ID, personId));
		}
		
		if(correspondence != null) {
			
			if(correspondence.getCorrespondenceDirectionType() != null) {
				criteria.add(Restrictions.eq("correspondenceDirectionType", correspondence.getCorrespondenceDirectionType()));
			}
			if(correspondence.getCorrespondenceModeType() != null) {
				criteria.add(Restrictions.eq("correspondenceModeType", correspondence.getCorrespondenceModeType()));
			}
			if(correspondence.getCorrespondenceOutcomeType() != null) {
				criteria.add(Restrictions.eq("correspondenceOutcomeType", correspondence.getCorrespondenceOutcomeType()));
			}
			if(correspondence.getCorrespondenceStatusType() != null) {
				criteria.add(Restrictions.eq("correspondenceStatusType", correspondence.getCorrespondenceStatusType()));
			}
			if(correspondence.getDate() != null) {
				criteria.add(Restrictions.eq("date", correspondence.getDate()));
			}
			if(correspondence.getTime() != null) {
				criteria.add(Restrictions.eq("time", correspondence.getTime()));
			}
			if(correspondence.getDetails() != null) {
				criteria.add(Restrictions.ilike("details", correspondence.getDetails(), MatchMode.ANYWHERE));
			}
			if(correspondence.getReason() != null) {
				criteria.add(Restrictions.ilike("reason", correspondence.getDetails(), MatchMode.ANYWHERE));
			}
			if(correspondence.getComments() != null) {
				criteria.add(Restrictions.ilike("comments", correspondence.getComments(), MatchMode.ANYWHERE));
			}
			if(correspondence.getStudyManager() != null) {
				criteria.add(Restrictions.ilike("studyManager", correspondence.getStudyManager()));
			}
		}
		
		List<Correspondences> personCorrespondenceList = criteria.list();
		if(personCorrespondenceList != null && personCorrespondenceList.size() == 0) {
			throw new EntityNotFoundException("The entity with id " + personId.toString() + " cannot be found.");
		}
		
		return personCorrespondenceList;
	}


	public List<CorrespondenceDirectionType> getCorrespondenceDirectionTypes() {
		
		Example directionTypeExample = Example.create(new CorrespondenceDirectionType());
		Criteria criteria = getSession().createCriteria(CorrespondenceDirectionType.class).add(directionTypeExample);
		return criteria.list();
	}

	
	public List<CorrespondenceModeType> getCorrespondenceModeTypes() {

		Example modeTypeExample = Example.create(new CorrespondenceModeType());
		Criteria criteria = getSession().createCriteria(CorrespondenceModeType.class).add(modeTypeExample);
		return criteria.list();
	}


	public List<CorrespondenceOutcomeType> getCorrespondenceOutcomeTypes() {

		Example outcomeTypeExample = Example.create(new CorrespondenceOutcomeType());
		Criteria criteria = getSession().createCriteria(CorrespondenceOutcomeType.class).add(outcomeTypeExample);
		return criteria.list();
	}


	public List<CorrespondenceStatusType> getCorrespondenceStatusTypes() {

		Example statusTypeExample = Example.create(new CorrespondenceStatusType());
		Criteria criteria = getSession().createCriteria(CorrespondenceStatusType.class).add(statusTypeExample);
		return criteria.list();
	}

	public Consent getConsent(Long id) throws ArkSystemException {
		Consent consent = (Consent)getSession().get(Consent.class, id);
		return consent;
	}
	
	public void create(ConsentFile consentFile) throws ArkSystemException
	{
		Session session = getSession();
		
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		consentFile.setInsertTime(dateNow);
		consentFile.setUserId(currentUser.getPrincipal().toString());
		
		session.save(consentFile);
	}
	
	public void update(ConsentFile consentFile) throws ArkSystemException,EntityNotFoundException{
		Session session = getSession();
		
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		consentFile.setUserId(currentUser.getPrincipal().toString());
		consentFile.setUpdateTime(dateNow);
		
		if((ConsentFile)session.get(ConsentFile.class,consentFile.getId()) != null){
			session.update(consentFile);	
		}else{
			throw new EntityNotFoundException("The Consent file record you tried to update does not exist in the Ark System");
		}
		
	}
	
	/**
	 * If a consentFile is not in a state where it can be deleted then remove it. It can be in a different status before it can be removed.
	 * @param consentFile
	 * @throws ArkSystemException
	 */
	public void delete(ConsentFile consentFile) throws ArkSystemException, EntityNotFoundException{
		try{
			Session session = getSession();
			consentFile = (ConsentFile)session.get(ConsentFile.class,consentFile.getId());	
			if(consentFile != null){
				getSession().delete(consentFile);	
			}else{
				throw new EntityNotFoundException("The Consent file record you tried to remove does not exist in the Ark System");
			}
			
		}catch(HibernateException someHibernateException){
			log.error("An Exception occured while trying to delete this consent file " + someHibernateException.getStackTrace());
		}catch(Exception e){
			log.error("An Exception occured while trying to delete this consent file " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}
	}
	
	public List<ConsentFile> searchConsentFile(ConsentFile consentFile)
			throws EntityNotFoundException, ArkSystemException {
		Criteria criteria =  getSession().createCriteria(ConsentFile.class);
		if(consentFile != null){
			
			if(consentFile.getId() != null){
				criteria.add(Restrictions.eq("id", consentFile.getId()));
			}
			
			if(consentFile.getConsent() != null){
				criteria.add(Restrictions.eq("consent", consentFile.getConsent()));
			}
			
			if(consentFile.getFilename() != null){
				criteria.add(Restrictions.ilike("filename", consentFile.getFilename(),MatchMode.ANYWHERE));
			}
		}
		criteria.addOrder(Order.desc("id"));
		
		@SuppressWarnings("unchecked")
		List<ConsentFile> list = criteria.list();
		return list;
	}
	
	
	private boolean isSubjectUIDUnique(String subjectUID, Long studyId,String action){
		boolean isUnique = true;
		Session session = getSession();
		Criteria criteria =  session.createCriteria(LinkSubjectStudy.class);
		criteria.add(Restrictions.eq("subjectUID", subjectUID));
		criteria.add(Restrictions.eq("study.id", studyId));
		if(action.equalsIgnoreCase(au.org.theark.core.Constants.ACTION_INSERT)){
			if (criteria.list().size() > 0){
				isUnique = false;
			}
		}else if(action.equalsIgnoreCase(au.org.theark.core.Constants.ACTION_UPDATE)){
			if (criteria.list().size() > 1 ){
				isUnique = false;
			}
		}
		return isUnique;
	}
	

	private YesNo getYesNo(String value){

		Criteria criteria = getSession().createCriteria(YesNo.class);
		criteria.add(Restrictions.ilike("name", value));
		return (YesNo)criteria.list().get(0);
	}
	public boolean personHasPreferredMailingAddress(Person person, Long currentAddressId){
		
		boolean hasPreferredMailing = false;

			Criteria criteria = getSession().createCriteria(Address.class);
			
			YesNo yes = getYesNo("Yes");
			criteria.add(Restrictions.eq("person.id",person.getId()));
			criteria.add(Restrictions.eq("preferredMailingAddress",yes));
			if(currentAddressId != null){
				criteria.add(Restrictions.ne("id", currentAddressId));
			}
			
			List list  = criteria.list();
			if(list.size() > 0){
				hasPreferredMailing = true;
			}
		return hasPreferredMailing;
	}

	
	public PersonLastnameHistory getPreviousSurnameHistory(PersonLastnameHistory personSurnameHistory){
		PersonLastnameHistory personLastnameHistoryToReturn = null;
		
		Example example = Example.create(personSurnameHistory);
		
		Criteria criteria = getSession().createCriteria(PersonLastnameHistory.class).add(example);
		if(criteria != null && criteria.list() != null && criteria.list().size() > 0){
			personLastnameHistoryToReturn =  (PersonLastnameHistory)criteria.list().get(0);	
		}
		
		return personLastnameHistoryToReturn;
	}
	
	public void createPersonLastnameHistory(Person person){
		PersonLastnameHistory personLastNameHistory = new PersonLastnameHistory();
		personLastNameHistory.setPerson(person);
		personLastNameHistory.setLastName(person.getLastName());
		
		getSession().save(personLastNameHistory);
	}
	
	public void updatePersonLastnameHistory(Person person){
		PersonLastnameHistory personLastnameHistory = new PersonLastnameHistory();
		personLastnameHistory.setPerson(person);
		personLastnameHistory.setLastName(person.getLastName());
		
		String currentLastName = getCurrentLastname(person);
		
		if(currentLastName == null || (currentLastName != null && !currentLastName.equalsIgnoreCase(person.getLastName())))
			getSession().save(personLastnameHistory);
	}

	public String getPreviousLastname(Person person)
	{
		PersonLastnameHistory personLastameHistory = new PersonLastnameHistory();
		
		// Only get previous lastname if person in context
		if(person.getId() != null && person.getLastName() != null){
			Criteria criteria =  getSession().createCriteria(PersonLastnameHistory.class);
			criteria.add(Restrictions.eq(au.org.theark.core.Constants.PERSON_SURNAME_HISTORY_PERSON,person));
			criteria.addOrder(Order.desc("id"));
			if(!criteria.list().isEmpty()){
				if (criteria.list().size() > 1)
					personLastameHistory = (PersonLastnameHistory) criteria.list().get(1);
			}
		}
		
		return personLastameHistory.getLastName();
	}
	
	public String getCurrentLastname(Person person)
	{
		Criteria criteria =  getSession().createCriteria(PersonLastnameHistory.class);
		
		if(person.getId() != null){
			criteria.add(Restrictions.eq(au.org.theark.core.Constants.PERSON_SURNAME_HISTORY_PERSON,person));	
		}
		criteria.addOrder(Order.desc("id"));
		PersonLastnameHistory personLastnameHistory = new PersonLastnameHistory(); 
		if(!criteria.list().isEmpty()){
			personLastnameHistory = (PersonLastnameHistory) criteria.list().get(0);
		}
		
		return personLastnameHistory.getLastName();
	}

	public List<PersonLastnameHistory> getLastnameHistory(Person person)
	{
		Criteria criteria =  getSession().createCriteria(PersonLastnameHistory.class);
		
		if(person.getId() != null){
			criteria.add(Restrictions.eq(au.org.theark.core.Constants.PERSON_SURNAME_HISTORY_PERSON,person));	
		}
		
		return criteria.list();
	}
	
	public void create(SubjectFile subjectFile) throws ArkSystemException
	{
		Session session = getSession();
		currentUser = SecurityUtils.getSubject();
		subjectFile.setUserId(currentUser.getPrincipal().toString());
		
		session.save(subjectFile);
	}

	public void update(SubjectFile subjectFile) throws ArkSystemException,EntityNotFoundException{
		Session session = getSession();
		
		currentUser = SecurityUtils.getSubject();
		dateNow = new Date(System.currentTimeMillis());

		subjectFile.setUserId(currentUser.getPrincipal().toString());
		
		if((ConsentFile)session.get(ConsentFile.class,subjectFile.getId()) != null){
			session.update(subjectFile);	
		}else{
			throw new EntityNotFoundException("The Subject file record you tried to update does not exist in the Ark System");
		}
		
	}
	
	/**
	 * If a subjectFile is not in a state where it can be deleted then remove it. It can be in a different status before it can be removed.
	 * @param subjectFile
	 * @throws ArkSystemException
	 */
	public void delete(SubjectFile subjectFile) throws ArkSystemException, EntityNotFoundException{
		try{
			Session session = getSession();
			subjectFile = (SubjectFile)session.get(SubjectFile.class,subjectFile.getId());	
			if(subjectFile != null){
				getSession().delete(subjectFile);	
			}else{
				throw new EntityNotFoundException("The Consent file record you tried to remove does not exist in the Ark System");
			}
			
		}catch(HibernateException someHibernateException){
			log.error("An Exception occured while trying to delete this consent file " + someHibernateException.getStackTrace());
		}catch(Exception e){
			log.error("An Exception occured while trying to delete this consent file " + e.getStackTrace());
			throw new ArkSystemException("A System Error has occured. We wil have someone contact you regarding this issue");
		}
	}

	public List<SubjectFile> searchSubjectFile(SubjectFile subjectFile) throws EntityNotFoundException, ArkSystemException
	{
		Criteria criteria =  getSession().createCriteria(SubjectFile.class);
		if(subjectFile != null){
			
			if(subjectFile.getId() != null){
				criteria.add(Restrictions.eq("id", subjectFile.getId()));
			}
			
			if(subjectFile.getLinkSubjectStudy() != null){
				criteria.add(Restrictions.eq("linkSubjectStudy", subjectFile.getLinkSubjectStudy()));
			}
			
			if(subjectFile.getStudyComp() != null){
				criteria.add(Restrictions.eq("studyComp", subjectFile.getStudyComp()));
			}
			
			if(subjectFile.getFilename() != null){
				criteria.add(Restrictions.ilike("filename", subjectFile.getFilename(),MatchMode.ANYWHERE));
			}
		}
		criteria.addOrder(Order.desc("id"));
		
		@SuppressWarnings("unchecked")
		List<SubjectFile> list = criteria.list();
		return list;
	}
}
