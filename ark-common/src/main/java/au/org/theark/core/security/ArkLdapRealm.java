package au.org.theark.core.security;

import java.util.Collection;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.Sha256CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.ArkUserVO;


/**
 * @author nivedann
 *
 */

@Component
public class ArkLdapRealm extends AuthorizingRealm{
	
	static final Logger log = LoggerFactory.getLogger(ArkLdapRealm.class);
	/*Interface to Core*/
	protected IArkCommonService iArkCommonService;
	
	@Autowired
	public void setiArkCommonService(IArkCommonService iArkCommonService) {
		this.iArkCommonService = iArkCommonService;
	}
	
	public ArkLdapRealm() {
		log.info("Common in TestLdapRealm constructor");
        setName("arkLdapRealm"); //This name must match the name in the User class's getPrincipals() method
        setCredentialsMatcher(new Sha256CredentialsMatcher());
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
    	//System.out.println("\n -----In TestLdapRealm");
    	log.info(" in  doGetAuthenticationInfo()");
    	SimpleAuthenticationInfo sai = null;
    	ArkUserVO userVO = null;
    	UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        try{
        	userVO = iArkCommonService.getUser(token.getUsername().trim());//Example to use core services to get user
        	if(userVO != null){
        		sai = new SimpleAuthenticationInfo(userVO.getUserName(), userVO.getPassword(),getName());
        	}
        
        }catch(ArkSystemException etaSystem){
        	log.error("Exception occured while the Realm invoked Ldap Interface to look up person" + etaSystem.getMessage());
        }
        return sai;
    }

    /**
     * This method will check for roles.Wicket calls this method.
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    	log.debug("Inside doGetAuthorizationInfo ");
    	
    	SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    	boolean isAdministator = false;
    	boolean isSuperAdministator = false;
    	
    	String ldapUserName = (String) principals.fromRealm(getName()).iterator().next();
    	
    	Long sessionStudyId = (Long)SecurityUtils.getSubject().getSession().getAttribute("studyId");
    	
    	if(sessionStudyId != null){
    		log.info("There is a study in context. Now we can look up the subject's roles for the study");
    		//Get the roles for the study in context
    	}else{
    		log.info("There is no study in context. User has logged in first time.Load roles of type Administator if there is one.");
    		//
        	try{
        		//Check if the logged in user has SuperAdmin Role or Admin role
        		isAdministator = iArkCommonService.isAdministator(ldapUserName);
        		isSuperAdministator = iArkCommonService.isSuperAdministrator(ldapUserName);
            	//TODO: Check if user is Super Administrator ark_user_role for the module in context
            	if( isSuperAdministator || isAdministator ){
            		log.info("The logged in user is of Type of Administator");
            		Collection<String> roleCollection  = iArkCommonService.getUserAdminRoles(ldapUserName);
            		info.addRoles(roleCollection);
            
            	}else{
            		log.info("The logged in user is an ordinary user. Don't yet add roles defer until a study is in context.");
            		//Normal user
            	}
        	}catch(EntityNotFoundException userNotFound){
        		log.error("The logged in user was not located in the ArkUser Table.The user was not set up correctly.");
        	}
    	}
    	log.info("\n --- Inside doGetAuthorizationInfo invoked ----");
        return info;
    }
    


}
