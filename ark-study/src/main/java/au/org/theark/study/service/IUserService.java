package au.org.theark.study.service;

import java.util.List;

import javax.naming.InvalidNameException;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.PersonNotFoundException;
import au.org.theark.core.exception.UnAuthorizedOperation;
import au.org.theark.core.exception.UserNameExistsException;
import au.org.theark.core.vo.EtaUserVO;
import au.org.theark.core.vo.ModuleVO;
import au.org.theark.study.model.entity.EtaUser;
import au.org.theark.study.model.entity.Person;

public interface IUserService {
	
	public EtaUser getUser(String userName) throws ArkSystemException;
	
	public EtaUserVO getUserRole(String username) throws ArkSystemException;
	
	public void getUserRole(EtaUserVO etaUserVO, List<ModuleVO> listOfAllModules) throws ArkSystemException;
	
	public void getUserRole(EtaUserVO etaUserVO, String moduleName) throws ArkSystemException;

	public List<ModuleVO> getUserRoles(EtaUserVO etaUserVO, String studyName) throws ArkSystemException;
	
	public Person createPerson(Person personEntity);
	
	public void createUser(String userName, String password);
	/**
	 * Interface to create/add a User into the LDAP system and also associate the user to
	 * applications and roles.
	 * @param EtaUserVO etaUserVO
	 * @throws InvalidNameException
	 * @throws UserNameExistsException
	 * @throws Exception
	 */
	public void createLdapUser(EtaUserVO etaUserVO) throws ArkSystemException, UserNameExistsException, Exception;
	/**
	 * Interface to update a person's details including their password. It does not at present
	 * modify or update the Groups and roles. The modification will only apply to Person attributes.
	 * 
	 * @param EtaUserVO etaUserVO
	 * @throws InvalidNameException
	 */
	public void updateLdapUser(EtaUserVO etaUserVO) throws ArkSystemException;
	
	public void deleteLdapUser(EtaUserVO etaUserVO) throws UnAuthorizedOperation, ArkSystemException;

	public EtaUser getCurrentUser();
	
	public List<ModuleVO> getModules(boolean isForDisplay) throws ArkSystemException;
	
	public List<String> getModuleRoles(String moduleId) throws ArkSystemException;
	/**
	 * User this method to lookup a user in LDAP. The user object acts as a filter condition
	 * that is applied for the search.
	 * @param EtaUserVO
	 * @return List<EtaUserVO> 
	 * @throws InvalidNameException
	 */
	public List<EtaUserVO> searchUser(EtaUserVO user) throws ArkSystemException;
	/**
	 * Look up persons based on the search criteria and return a list of people who 
	 * match the criteria
	 * If a match was not found throw a business exception to indicate the same.
	 * @param personVO
	 * @return
	 */
	public List<Person> searchPerson(Person personVO) throws PersonNotFoundException;
	
	

}
