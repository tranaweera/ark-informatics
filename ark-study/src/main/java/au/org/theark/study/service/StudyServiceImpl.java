package au.org.theark.study.service;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityCannotBeRemoved;
import au.org.theark.core.exception.EntityExistsException;
import au.org.theark.core.exception.StatusNotAvailableException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.security.RoleConstants;
import au.org.theark.study.model.dao.ILdapUserDao;
import au.org.theark.study.model.dao.IStudyDao;
import au.org.theark.study.model.entity.Study;
import au.org.theark.study.model.entity.StudyStatus;
import au.org.theark.study.web.Constants;

@Transactional
@Service(Constants.STUDY_SERVICE)
public class StudyServiceImpl implements IStudyService{
	
	private IStudyDao studyDao;
	private ILdapUserDao iLdapUserDao;

	public ILdapUserDao getiLdapUserDao() {
		return iLdapUserDao;
	}
	
	@Autowired
	public void setiLdapUserDao(ILdapUserDao iLdapUserDao) {
		this.iLdapUserDao = iLdapUserDao;
	}

	/*To access Hibernate Study Dao */
	@Autowired
	public void setStudyDao(IStudyDao studyDao) {
		this.studyDao = studyDao;
	}

	public IStudyDao getStudyDao() {
		return studyDao;
	}

	
	public List<Study> getStudy(Study study){
		return studyDao.getStudy(study);
	}
	
	public List<StudyStatus> getListOfStudyStatus(){
		return studyDao.getListOfStudyStatus();
	}

	public void createStudy(Study studyEntity, Set<String> selectedApplications) throws ArkSystemException, EntityExistsException, UnAuthorizedOperation{
		try{
			//Create the study group in the LDAP for the selected applications and also add the roles to each of the application.
			SecurityManager securityManager =  ThreadContext.getSecurityManager();
			Subject currentUser = SecurityUtils.getSubject();
			
			if(securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN)){
				studyDao.create(studyEntity);
				iLdapUserDao.createStudy(studyEntity.getName(), selectedApplications, au.org.theark.study.service.Constants.ARK_SYSTEM_USER);
			}else{
			 throw new UnAuthorizedOperation("The logged in user does not have the permission to create a study.");//Throw an exception
			}
		}catch(ArkSystemException arkSystemException){
			throw arkSystemException;
		}
		
	}
	
	public void updateStudy(Study studyEntity,Set<String> selectedApplications) throws EntityCannotBeRemoved, UnAuthorizedOperation, ArkSystemException, EntityExistsException{

		SecurityManager securityManager =  ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();
		if(securityManager.hasRole(currentUser.getPrincipals(), RoleConstants.ARK_SUPER_ADMIN)){
			studyDao.updateStudy(studyEntity);
			iLdapUserDao.updateStudyApplication(studyEntity.getName(), selectedApplications,au.org.theark.study.service.Constants.ARK_SYSTEM_USER);
		}else{
		 throw new UnAuthorizedOperation("The logged in user does not have the permission to create a study.");//Throw an exception
		}
	}
	
	
	private boolean validateDeleteStudy(Study studyEntity, StringBuffer messageBuffer){
		int validationErrors =0;
		boolean isDeletable = true;
		/*Check if there are study components linked to the study */
		if(studyEntity.getLinkStudyStudycomps().size() > 0){
			messageBuffer.append("\nThe study is linked to Study Components.");
			validationErrors++;
		}
		
		/* Check if the study has Sites linked to the study in LDAP or Backend*/
		if(studyEntity.getLinkStudyStudysites().size() > 0 ){
			messageBuffer.append("\n The study is linked to Study Sites.");
			validationErrors++;
		}
		
		/*Check if the study has subjects linked via the backend database */
		if(studyEntity.getLinkSubjectStudies().size() > 0){
			messageBuffer.append("\n There are Subjects linked to the Study.");
			validationErrors++;
		}
		
		/* Check if the study is linked with Subjects via Contacts */
		if(studyEntity.getLinkSubjectContacts().size() > 0){
			messageBuffer.append("\n There are Contacts  linked to the Study via Subjects.");
			validationErrors++;
		}
		
		/* Check if the study has Sub-Study linked to the study*/
		if(studyEntity.getLinkStudySubstudiesForStudyKey().size() > 0){
			messageBuffer.append("\n There are sub-studies refering to this Registry study.");
			validationErrors++;
		}
		
		if(validationErrors > 0){
			isDeletable = false;
		}
		return isDeletable;
	}
	
	public Study getStudy(Long id){
		return studyDao.getStudy(id);
	}
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN) throws ArkSystemException{
		
		return iLdapUserDao.getModulesLinkedToStudy(studyNameCN);
	}
	
	public Set<String> getModulesLinkedToStudy(String studyNameCN, boolean isForDisplay) throws ArkSystemException{
		return iLdapUserDao.getModulesLinkedToStudy(studyNameCN, isForDisplay);
	}
	/**
	 * This will mark the study as archived.
	 */
	public void archiveStudy(Study studyEntity) throws UnAuthorizedOperation, StatusNotAvailableException, ArkSystemException
	{
		//For archive, set the status to Archived and then issue an update
		StudyStatus status = studyDao.getStudyStatus("Archive");
		studyEntity.setStudyStatus(status);
		studyDao.updateStudy(studyEntity);
	}
}
