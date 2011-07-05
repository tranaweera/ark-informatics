package au.org.theark.core.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;

import au.org.theark.core.Constants;

/**
 * Global common class that determines permissions of particular action
 * @author cellis
 *
 */
public class ArkSecurity
{
	/**
	 * Determines whether a particular action is permitted by the user in context (eg Save, Edit, Delete)
	 * @param actionType
	 * @return true if action is permitted
	 */
	public static boolean isActionPermitted(String actionType)
	{
		boolean actionPermitted = false;
		
		SecurityManager securityManager = ThreadContext.getSecurityManager();
		Subject currentUser = SecurityUtils.getSubject();

		if (actionType.equalsIgnoreCase(Constants.SEARCH))
		{
			actionPermitted = hasSearchPermission(securityManager, currentUser);	
		}
		else if (actionType.equalsIgnoreCase(Constants.SAVE) || actionType.equalsIgnoreCase(Constants.NEW))
		{
			actionPermitted = hasSavePermission(securityManager, currentUser);
		}
		else if (actionType.equalsIgnoreCase(Constants.EDIT))
		{
			actionPermitted = hasEditPermission(securityManager, currentUser);
		}
		else if (actionType.equalsIgnoreCase(Constants.DELETE))
		{
			actionPermitted = hasDeletePermission(securityManager, currentUser);	
		}
		
		return actionPermitted;
	}
	
	/**
	 * Determines if current user has Search permissions
	 * @param securityManager
	 * @param currentUser
	 * @return true if READ permission allowed
	 */
	private static boolean hasSearchPermission(SecurityManager securityManager, Subject currentUser)
	{
		boolean flag = false;
		
		// Search allowed if user has READ permission
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.READ))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
		return flag;
	}

	/**
	 * Determines if current user has Save permissions
	 * @param securityManager
	 * @param currentUser
	 * @return true if CREATE or UPDATE permission allowed
	 */
	public static boolean hasSavePermission(SecurityManager securityManager, Subject currentUser)
	{
		boolean flag = false;
		
		// Save allowed if user has CREATE or UPDATE permissions
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.CREATE) || 
				securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
		return flag;
	}
	
	/**
	 * Determines if current user has Edit permissions
	 * @param securityManager
	 * @param currentUser
	 * @return true if UPDATE permission allowed
	 */
	public static boolean hasEditPermission(SecurityManager securityManager, Subject currentUser)
	{
		boolean flag = false;
	
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.UPDATE))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
		
		return flag;
	}
	
	/**
	 * Determines if current user has Delete permissions
	 * @param securityManager
	 * @param currentUser
	 * @return true if DELETE permission allowed
	 */
	public static boolean hasDeletePermission(SecurityManager securityManager, Subject currentUser)
	{
		boolean flag = false;
		
		if (securityManager.isPermitted(currentUser.getPrincipals(), PermissionConstants.DELETE))
		{
			flag = true;
		}
		else
		{
			flag = false;
		}
	
		return flag;
	}	
}