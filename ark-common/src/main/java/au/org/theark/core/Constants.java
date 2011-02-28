package au.org.theark.core;

/*
 * Common constants that apply to
 *  all of the Ark modules
 */
public class Constants {
	/* Module Role Display Names*/
	public static final String DISPLAY_ROLE_STUDY_ADMIN="Study Admin";
	public static final String DISPLAY_ROLE_ORDINARY_USER="Ordinary User";
	public static final String DISPLAY_ROLE_POWER_USER="Power User";
	public static final String DISPLAY_ROLE_LAB_PERSON="Lab Person";
	public static final String DISPLAY_ROLE_WADB_ADMNIN="WADB Admin";
	public static final String DISPLAY_ROLE_WADB_PERSON="WADB Person";
	public static final String DISPLAY_ROLE_SUPER_ADMIN="Super Admin";
	
	public static final String ROLE_STUDY_ADMIN="study_administrator";
	public static final String ROLE_SUPER_ADMIN="super_administrator";
	public static final String ROLE_ORDINARY_USER="ordinary_user";
	public static final String ROLE_POWER_USER ="power_user";
	public static final String ROLE_LAB_PERSON="lab_person";
	public static final String ROLE_WADB_ADMINISTRATOR="wadb_administrator";
	public static final String ROLE_WADB_PERSON="wadb_person";
	
	/* Module Names for display */
	public static final String DISP_MODULE_STUDY_MANAGER="Study Manager";
	public static final String DISP_MODULE_ARK="ARK";
	public static final String DISP_MODULE_WAGER_LAB="LIMS";
	public static final String DISP_MODULE_GWAS ="GWAS";
	public static final String DISP_MODULE_DATA_ANALYSIS ="Data Analysis";

	/* Module Names stored in the system*/
	public static final String MODULE_STUDY_MANAGER="study_manager";
	public static final String MODULE_ARK="ark";
	public static final String MODULE_WAGER_LAB="wager_lab";
	public static final String MODULE_GWAS ="gwas";
	public static final String MODULE_DATA_ANALYSIS ="data_analysis";
	
	/* Common Service Names */
	public static final String ARK_COMMON_SERVICE = "arkCommonService";
	
	/* Global constants */
	public static final String STUDY_CONTEXT_ID="studyId";
	public static final String PERSON_CONTEXT_ID="personId";
	public static final String PERSON_TYPE="personType";
	public static final String PERSON_CONTEXT_TYPE_SUBJECT= "subject";
	public static final String PERSON_CONTEXT_TYPE_CONTACT= "contact";
	public static final String PERSON_CONTEXT_CONSENT_ID = "consentId";
	
	
	public static final String TAB_SUBJECT_DETAIL="Subject Management";
	public static final String TAB_PERSON_PHONE="Phone";
	public static final String TAB_PERSON_ADDRESS="Address";
	public static final String TAB_MODULE_PERSON_PHONE="tab.module.person.phone";
	public static final String TAB_MODULE_PERSON_ADDRESS="tab.module.person.address";
	
	public static final String TAB_SUBJECT_CONSENT="Consent";
	public static final String TAB_MODULE_SUBJECT_CONSENT="tab.module.subject.consent";
	
	public static final String TAB_SUBJECT_CONSENT_FILE = "Consent File";
	public static final String TAB_MODULE_SUBJECT_CONSENT_FILE = "tab.module.subject.consentFile";
	
	public static final String TAB_MODULE_SUBJECT_DETAIL ="tab.module.subject.detail";
	public static final String DD_MM_YYYY="dd/MM/yyyy";	//cap M is for month, low m is for minute 
	public static final String MENU_SUBJECT_SUBMENU="subjectSubMenus";
	/*Jquery/Wiquey Date Picker Format */
	public static final String DATE_PICKER_DD_MM_YY="dd/mm/yy";
	
	
	public static final String SUBJECTUID="SUBJECTUID";
	public static final String DATE_COLLECTED="DATE_COLLECTED";
	
	/* Tab Panel Keys */
	public static final String TAB_MODULE_STUDY_DETAIL ="tab.module.study.details";
	public static final String STUDY_DETAIL ="Study Detail";
	public static final String SITE = "Site";
	public static final String TAB_MODULE_SITE ="tab.module.sites";
	public static final String STUDY_COMPONENT="Study Component";
	public static final String TAB_MODULE_STUDY_COMPONENT ="tab.module.study.components";
	public static final String USER="User";
	public static final String TAB_MODULE_USER ="tab.module.users";
	public static final String MY_DETAIL ="My Detail";
	public static final String TAB_MODULE_MY_DETAIL="tab.module.mydetails";
	public static final String SUBJECT="Subject";
	public static final String TAB_MODULE_SUBJECT="tab.module.subject";
	public static final String MENU_STUDY_SUBMENU ="studySubMenus";
	public static final String CUSTOM_FIELD="Custom Field";
	public static final String TAB_CUSTOM_FIELD="tab.custom.field";
	public static final String MENU_REGISTRY_SUBMENU="registrySubMenus";
	
	/*
	 * People Menu
	 */
	public static final String PEOPLE = "People";
	public static final String TAB_PEOPLE_FIELD="tab.people";
	public static final String STUDY_KEY="id";
	
	public static final String STUDY_NAME="name";
	public static final String DATE_OF_APPLICATION="dateOfApplication";
	public static final String EST_YEAR_OF_COMPLETION="estimatedYearOfCompletion";
	public static final String CHIEF_INVESTIGATOR ="chiefInvestigator";
	public static final String CONTACT_PERSON ="contactPerson";
	public static final String STUDY_STATUS="studyStatus";
	public static final String STUDY_STATUS_ARCHIVE="Archive";
	
	public static final String STUDY_SERVICE= "studyService";
	public static final String STUDY_DAO ="studyDao";
	
	public static final String FIELD_TYPE_NUMBER ="NUMBER";	
	public static final String FIELD_TYPE_CHARACTER ="CHARACTER";
	public static final String FIELD_TYPE_DATE ="DATE";
	public static final String DISCRETE_RANGE_TOKEN =",";
	public static final String ENCODED_VALUES_TOKEN = ";";
	public static final String ENCODED_VALUES_SEPARATOR = "=";
	
	/* Button names/labels */
	public static final String SEARCH="search";
	public static final String NEW="new";
	public static final String RESET="reset";
	public static final String SAVE ="save";
	public static final String CANCEL ="cancel";
	public static final String DELETE="delete";
	public static final String	EDIT	= "edit";
	public static final String	EDIT_CANCEL	= "editCancel";
	public static final String	OK	= "ok";
	public static final String	PREVIOUS	= "previous";
	public static final String	NEXT	= "next";
	public static final String	LAST	= "last";
	public static final String	FINISH	= "finish";
	public static final String	DONE	= "done";
	
	/* Delete confirmation */
	public static final String	DELETE_CONFIRM_TITLE	= "Delete confirmation";
	public static final String	DELETE_CONFIRM_MESSAGE	= "Are you sure you want to delete?";
	
	public static final String SEARCH_FORM ="searchForm";
	
	/* Form modes (view/edit) */
	public static final int MODE_NEW = 1;
	public static final int MODE_EDIT =2;
	public static final int MODE_READ =3;
	
	/* Common import field delimiters */
	public static final char IMPORT_DELIM_CHAR_COMMA	= ',';
	public static final char IMPORT_DELIM_CHAR_TAB	= '	';
	
	/* Search result list rows per page */
	public static final int	ROWS_PER_PAGE	= 10;
	public static final String	RESULT_LIST	= "resultList";
	
	/*Study Schema */
	public static final String STUDY_SCHEMA="study";
	
	/* Default Country */
	public static final String DEFAULT_COUNTRY="Australia";
}