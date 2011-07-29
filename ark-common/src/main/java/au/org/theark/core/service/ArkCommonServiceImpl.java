package au.org.theark.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.IArkAuthorisation;
import au.org.theark.core.dao.ILdapPersonDao;
import au.org.theark.core.dao.IStudyDao;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.ArkModuleRole;
import au.org.theark.core.model.study.entity.ArkRole;
import au.org.theark.core.model.study.entity.ArkRolePolicyTemplate;
import au.org.theark.core.model.study.entity.ArkUser;
import au.org.theark.core.model.study.entity.ArkUserRole;
import au.org.theark.core.model.study.entity.AuditHistory;
import au.org.theark.core.model.study.entity.ConsentAnswer;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CountryState;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkStudyArkModule;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.PhoneStatus;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.StudyComp;
import au.org.theark.core.model.study.entity.StudyCompStatus;
import au.org.theark.core.model.study.entity.StudyStatus;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.SubjectUidPadChar;
import au.org.theark.core.model.study.entity.SubjectUidToken;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.model.study.entity.YesNo;
import au.org.theark.core.vo.ArkModuleVO;
import au.org.theark.core.vo.ArkUserVO;
import au.org.theark.core.vo.CustomFieldVO;
import au.org.theark.core.vo.SubjectVO;

/**
 * The implementation of IArkCommonService. We want to auto-wire and hence use the @Service annotation.
 * 
 * @author nivedann
 * @param <T>
 * 
 */


@Transactional
@Service(Constants.ARK_COMMON_SERVICE)
public class ArkCommonServiceImpl<T> implements IArkCommonService {

	private static Logger		log	= LoggerFactory.getLogger(ArkCommonServiceImpl.class);
	
	private IArkAuthorisation	arkAuthorisationDao;
	private IStudyDao				studyDao;
	private ILdapPersonDao		ldapInterface;

	public IArkAuthorisation getArkAuthorisationDao() {
		return arkAuthorisationDao;
	}

	@Autowired
	public void setArkAuthorisationDao(IArkAuthorisation arkAuthorisationDao) {
		this.arkAuthorisationDao = arkAuthorisationDao;
	}

	public IStudyDao getStudyDao() {
		return studyDao;
	}

	@Autowired
	public void setStudyDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}

	@Autowired
	public void setLdapInterface(ILdapPersonDao ldapInterface) {
		this.ldapInterface = ldapInterface;
	}

	public ArkUserVO getUser(String name) throws ArkSystemException {
		return ldapInterface.getUser(name);
	}

	public List<String> getUserRole(String userName) throws ArkSystemException {
		return ldapInterface.getUserRole(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getListOfStudyStatus()
	 */
	public List<StudyStatus> getListOfStudyStatus() {
		return studyDao.getListOfStudyStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getStudy(au.org.theark.core.model.study.entity.Study)
	 */
	public List<Study> getStudy(Study study) {
		return studyDao.getStudy(study);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getStudy(java.lang.Long)
	 */
	public Study getStudy(Long id) {

		return studyDao.getStudy(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getSubject(au.org.theark.core.vo.SubjectVO)
	 */
	public Collection<SubjectVO> getSubject(SubjectVO subjectVO) {

		return studyDao.getSubject(subjectVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getGenderType()
	 */
	public Collection<GenderType> getGenderType() {

		return studyDao.getGenderType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getListOfPhoneType()
	 */
	public List<PhoneType> getListOfPhoneType() {

		return studyDao.getListOfPhoneType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getSubjectStatus()
	 */
	public List<SubjectStatus> getSubjectStatus() {

		return studyDao.getSubjectStatus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getTitleType()
	 */
	public Collection<TitleType> getTitleType() {

		return studyDao.getTitleType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getVitalStatus()
	 */
	public Collection<VitalStatus> getVitalStatus() {

		return studyDao.getVitalStatus();
	}

	public LinkSubjectStudy getSubjectByUID(String subjectUID) throws EntityNotFoundException {

		return studyDao.getSubjectByUID(subjectUID);
	}

	public Collection<MaritalStatus> getMaritalStatus() {
		return studyDao.getMaritalStatus();
	}

	public List<Country> getCountries() {
		return studyDao.getCountries();
	}

	public List<CountryState> getStates(Country country) {
		return studyDao.getStates(country);
	}

	public List<AddressType> getAddressTypes() {
		return studyDao.getAddressTypes();
	}

	public List<AddressStatus> getAddressStatuses() {
		return studyDao.getAddressStatuses();
	}

	public List<ConsentStatus> getConsentStatus() {
		return studyDao.getConsentStatus();
	}

	public List<ConsentStatus> getRecordableConsentStatus() {
		return studyDao.getRecordableConsentStatus();
	}

	public List<StudyComp> getStudyComponent() {
		return studyDao.getStudyComponent();
	}

	public List<StudyComp> getStudyComponentByStudy(Study study) {
		return studyDao.getStudyComponentByStudy(study);
	}

	public List<ConsentType> getConsentType() {
		return studyDao.getConsentType();
	}

	public List<StudyCompStatus> getStudyComponentStatus() {
		return studyDao.getStudyComponentStatus();
	}

	public List<ConsentAnswer> getConsentAnswer() {
		return studyDao.getConsentAnswer();
	}

	public List<YesNo> getYesNoList() {
		return studyDao.getYesNoList();
	}

	public void createAuditHistory(AuditHistory auditHistory) {
		studyDao.createAuditHistory(auditHistory);
	}

	public List<PersonContactMethod> getPersonContactMethodList() {
		return studyDao.getPersonContactMethodList();
	}

	public boolean isSubjectConsentedToComponent(StudyComp studyComponent, Person subject, Study study) {
		return studyDao.isSubjectConsentedToComponent(studyComponent, subject, study);
	}

	public LinkSubjectStudy getSubject(Long personId) throws EntityNotFoundException {
		return studyDao.getSubject(personId);
	}

	public List<SubjectUidPadChar> getListOfSubjectUidPadChar() {
		return studyDao.getListOfSubjectUidPadChar();
	}

	public String getSubjectUidExample(Study study) {
		return studyDao.getSubjectUidExample(study);
	}

	public List<SubjectUidToken> getListOfSubjectUidToken() {
		return studyDao.getListOfSubjectUidToken();
	}

	public Country getCountry(String countryCode) {
		return studyDao.getCountry(countryCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#isAdministator(java.lang.String)
	 */
	public boolean isAdministator(String userName) throws EntityNotFoundException {

		return arkAuthorisationDao.isAdministator(userName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#isSuperAdmin(java.lang.String)
	 */
	public boolean isSuperAdministrator(String userName) throws EntityNotFoundException {
		return arkAuthorisationDao.isSuperAdministrator(userName);
	}

	public GenderType getGenderType(String name) {
		return studyDao.getGenderType(name);
	}

	public VitalStatus getVitalStatus(String name) {
		return studyDao.getVitalStatus(name);
	}

	public TitleType getTitleType(String name) {
		return studyDao.getTitleType(name);
	}

	public MaritalStatus getMaritalStatus(String name) {
		return studyDao.getMaritalStatus(name);
	}

	public PersonContactMethod getPersonContactMethod(String name) {
		return studyDao.getPersonContactMethod(name);
	}

	public SubjectStatus getSubjectStatus(String name) {
		return studyDao.getSubjectStatus(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getUserAdminRoles(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getUserAdminRoles(String ldapUserName) throws EntityNotFoundException {

		return arkAuthorisationDao.getUserAdminRoles(ldapUserName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.org.theark.core.service.IArkCommonService#getUserRoleForStudy(java.lang.String, au.org.theark.core.model.study.entity.Study)
	 */
	public String getUserRoleForStudy(String ldapUserName, Study study) throws EntityNotFoundException {
		return arkAuthorisationDao.getUserRoleForStudy(ldapUserName, study);
	}

	public ArkFunction getArkFunctionByName(String functionName) {
		return arkAuthorisationDao.getArkFunctionByName(functionName);
	}

	public ArkModule getArkModuleByName(String moduleName) {
		return arkAuthorisationDao.getArkModuleByName(moduleName);
	}

	public String getUserRole(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule, Study study) throws EntityNotFoundException {
		return arkAuthorisationDao.getUserRole(ldapUserName, arkFunction, arkModule, study);
	}

	public ArkFunction getArkFunctionById(Long functionId) {
		return arkAuthorisationDao.getArkFunctionById(functionId);
	}

	public ArkModule getArkModuleById(Long moduleId) {
		return arkAuthorisationDao.getArkModuleById(moduleId);
	}

	/**
	 * Returns All Permissions as collection of Strings
	 * 
	 * @return Collection<String> that represents ArkPermission
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> getArkPermission() {
		return arkAuthorisationDao.getArkPermission();
	}

	public boolean isSuperAdministator(String ldapUserName, ArkFunction arkFunction, ArkModule arkModule) throws EntityNotFoundException {
		return arkAuthorisationDao.isSuperAdministator(ldapUserName, arkFunction, arkModule);
	}

	public ArkUser getArkUser(String ldapUserName) throws EntityNotFoundException {
		return arkAuthorisationDao.getArkUser(ldapUserName);
	}

	@SuppressWarnings("unchecked")
	public Collection<Class<T>> getEntityList(Class aClass) {
		return arkAuthorisationDao.getEntityList(aClass);
	}

	public int getStudySubjectCount(SubjectVO subjectVoCriteria) {
		return studyDao.getStudySubjectCount(subjectVoCriteria);
	}

	public List<SubjectVO> searchPageableSubjects(SubjectVO subjectVoCriteria, int first, int count) {
		return studyDao.searchPageableSubjects(subjectVoCriteria, first, count);
	}

	public Collection getArkRolePermission(ArkFunction arkFunction, String userRole, ArkModule arkModule) throws EntityNotFoundException {

		return arkAuthorisationDao.getArkRolePermission(arkFunction, userRole, arkModule);
	}

	public Collection getArkRolePermission(String userRole) throws EntityNotFoundException {

		return arkAuthorisationDao.getArkRolePermission(userRole);
	}

	public Collection<ArkModuleRole> getArkModuleAndLinkedRoles() {
		return arkAuthorisationDao.getArkModuleAndLinkedRoles();
	}

	public Collection<ArkModuleVO> getArkModulesLinkedToStudy(Study study) {
		return arkAuthorisationDao.getArkModulesLinkedToStudy(study);
	}

	public ArrayList<ArkRole> getArkRoleLinkedToModule(ArkModule arkModule) {
		return arkAuthorisationDao.getArkRoleLinkedToModule(arkModule);
	}

	public Collection<ArkModule> getArkModulesLinkedWithStudy(Study study) {
		return arkAuthorisationDao.getArkModulesLinkedWithStudy(study);
	}

	public List<ArkFunction> getModuleFunction(ArkModule arkModule) {
		return studyDao.getModuleFunction(arkModule);
	}

	public List<ArkUserRole> getArkUserLinkedModule(Study study, ArkModule arkModule) {
		return arkAuthorisationDao.getArkUserLinkedModule(study, arkModule);
	}

	public List<LinkStudyArkModule> getLinkStudyArkModulesList(Study study) {
		return arkAuthorisationDao.getLinkStudyArkModulesList(study);
	}

	public List<PhoneStatus> getPhoneStatus() {
		return studyDao.getPhoneStatus();
	}

	public Boolean studyHasSubjects(Study study) {
		return studyDao.studyHasSubjects(study);
	}
	
	public List<Study> getStudiesForUser(ArkUser arkUser, Study study){
		return studyDao.getStudiesForUser(arkUser, study);
	}

	public int getCustomFieldCount(CustomField customFieldCriteria) {
		return studyDao.getCustomFieldCount(customFieldCriteria);
	}

	public List<CustomField> searchPageableCustomFields(CustomField customFieldCriteria, int first, int count) {
		return studyDao.searchPageableCustomFields(customFieldCriteria, first, count);
	}

	public List<FieldType> getFieldTypes() {
		return studyDao.getFieldTypes();
	}

	public List<String> getUnitTypeNames(UnitType unitTypeCriteria, int maxResults) {
		return studyDao.getUnitTypeNames(unitTypeCriteria, maxResults);
	}

	public List<UnitType> getUnitTypes(UnitType unitTypeCriteria) {
		return studyDao.getUnitTypes(unitTypeCriteria);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void createCustomField(CustomFieldVO customFieldVO) throws  ArkSystemException, ArkUniqueException{
		try{
			AuditHistory ah = new AuditHistory();
			studyDao.createCustomField(customFieldVO.getCustomField());
			customFieldVO.getCustomFieldDisplay().setCustomField(customFieldVO.getCustomField());
			studyDao.createCustomFieldDisplay(customFieldVO.getCustomFieldDisplay());
			//Custom Field History
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("Created Custom " + customFieldVO.getCustomField().getName());
			ah.setEntityId(customFieldVO.getCustomField().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD);
			createAuditHistory(ah);
			//Custom Field Display History
			ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_CREATED);
			ah.setComment("Created Custom Field Display" + customFieldVO.getCustomField().getName());
			ah.setEntityId(customFieldVO.getCustomField().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_DISPLAY);
			createAuditHistory(ah);
			
		}catch (ConstraintViolationException cvex) {
			log.error("Custom Field Already Exists.: " + cvex);
			throw new ArkUniqueException("A Custom Field already exits.");
		}
		catch (Exception ex) {
			log.error("Problem creating Custom Field: " + ex);
			throw new ArkSystemException("Problem creating Custom Field: " + ex.getMessage());
		}
	}
	/**
	 * Update  a Custom Field if it is not yet any data and update the Custom Field display
	 * details.
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateCustomField(CustomFieldVO customFieldVO) throws  ArkSystemException, ArkUniqueException{
		
		boolean isUnique = studyDao.isCustomFieldUnqiue(customFieldVO.getCustomField().getName(), customFieldVO.getCustomField().getStudy(), customFieldVO.getCustomField());
		if(!isUnique){
			log.error("Custom Field Already Exists.: " );
			throw new ArkUniqueException("A Custom Field already exits.");
		}
		try{
			
			
			if(!customFieldVO.getCustomField().getCustomFieldHasData()){
			
				studyDao.updateCustomField(customFieldVO.getCustomField());
				customFieldVO.getCustomFieldDisplay().setCustomField(customFieldVO.getCustomField());
				studyDao.updateCustomFieldDisplay(customFieldVO.getCustomFieldDisplay());
				//Custom Field History
				AuditHistory ah = new AuditHistory();
				ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
				ah.setComment("Updated Custom Field " + customFieldVO.getCustomField().getName());
				ah.setEntityId(customFieldVO.getCustomField().getId());
				ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD);
				createAuditHistory(ah);
			}else{
				studyDao.updateCustomFieldDisplay(customFieldVO.getCustomFieldDisplay());
			}
			
			//Custom Field Display History
			AuditHistory ah = new AuditHistory();
			ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_UPDATED);
			ah.setComment("Updated Custom Field Display " + customFieldVO.getCustomField().getName());
			ah.setEntityId(customFieldVO.getCustomField().getId());
			ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_DISPLAY);
			createAuditHistory(ah);
			
		}
		catch (ConstraintViolationException cvex) {
			log.error("Custom Field Already Exists.: " + cvex);
			throw new ArkUniqueException("A Custom Field already exits.");
		}
		catch (Exception ex) {
			log.error("Problem updating Custom Field: " + ex);
			throw new ArkSystemException("Problem updating Custom Field: " + ex.getMessage());
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void deleteCustomField(CustomFieldVO customFieldVO) throws ArkSystemException,EntityCannotBeRemoved{
		
		try{		
			
			if(!customFieldVO.getCustomField().getCustomFieldHasData()){
				studyDao.deleteCustomField(customFieldVO.getCustomField());
				studyDao.deleteCustomDisplayField(customFieldVO.getCustomFieldDisplay());
				String fieldName  =customFieldVO.getCustomField().getName();
				AuditHistory ah = new AuditHistory();
				ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
				ah.setComment("Deleted Custom Field " + customFieldVO.getCustomField().getName());
				ah.setEntityId(customFieldVO.getCustomField().getId());
				ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD);
				createAuditHistory(ah);
				//History for Custom Display Field Display
				ah = new AuditHistory();
				ah.setActionType(au.org.theark.core.Constants.ACTION_TYPE_DELETED);
				ah.setComment("Deleted Custom Display Field For Custom Field " + fieldName);
				ah.setEntityId(customFieldVO.getCustomFieldDisplay().getId());
				ah.setEntityType(au.org.theark.core.Constants.ENTITY_TYPE_CUSTOM_FIELD_DISPLAY);
				createAuditHistory(ah);
			}else{
				throw new EntityCannotBeRemoved("Custom Field cannot be removed, it is used in the system");
			}	
		}
		catch (Exception ex) {
			log.error("Unable to delete CustomField. " + ex);
			throw new ArkSystemException("Unable to delete Custom Field: " + ex.getMessage());
		}
		
	}
	
	
	public FieldType getFieldTypeById(Long filedTypeId){
		return studyDao.getFieldTypeById(filedTypeId);
	}

	
	public CustomField getCustomField(Long id ){
		return studyDao.getCustomField(id);
	}

	public List<Study> getStudyListForUser(ArkUserVO arkUserVo, Study searchStudy) {
		return arkAuthorisationDao.getStudyListForUser(arkUserVo, searchStudy);
	}

	public List<ArkUserRole> getArkRoleListByUser(ArkUserVO arkUserVo) {
		return arkAuthorisationDao.getArkRoleListByUser(arkUserVo);
	}

	public List<ArkRolePolicyTemplate> getArkRolePolicyTemplate(ArkRole arkRole, ArkModule arkModule) {
		return arkAuthorisationDao.getArkRolePolicyTemplate(arkRole, arkModule);
	}

}
