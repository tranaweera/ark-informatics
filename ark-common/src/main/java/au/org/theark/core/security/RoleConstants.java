package au.org.theark.core.security;

public class RoleConstants {
	
	public static final String SUPER_ADMIN="super_administrator";
	public static final String STUDY_ADMIN="study_administrator";
	public static final String GENERAL_USER="ordinary_person";
	/* Module based roles, module name prefix and role name suffix*/
	public static final String ARK_SUPER_ADMIN="ARK_super_administrator";
	public static final String ARK_ORDINARY_USER="ARK_ordinary_user";
	public static final String LIMS_SUPER_ADMIN=	"wager_lab_super_administrator";
	public static final String LIMS_ORDINARY_USER=	"wager_lab_ordinary_user";
	public static final String GWAS_SUPER_ADMIN	="gwas_super_administrator";
	public static final String STUDY_MANAGER_SUPER_ADMIN	="study_manager_super_administrator";
	public static final String STUDY_MANAGER_ORDINARY_USER = "study_manager_ordinary_user";
	
	/**
	 * These Role Constants and Permissions are for the new user management. 
	 * The role names here should match the string one Role_Name table
	 */
	public static final String ARK_ROLE_SUPER_ADMINISTATOR = "Super Administrator";
	public static final String ARK_ROLE_ADMINISTATOR="Administrator";
	public static final String ARK_ROLE_ORDINARY_USER="Ordinary User";

}
