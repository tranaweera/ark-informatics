/**
 *
 *  BiospecimenCore.java
 *  Copyright (C) Neuragenix Pty Ltd, 2005
 *  Description : Fresh implementation of the Biospecimen Channel
 *                Provides CORE functionality of the Biospecimen Domain
 *
 *  Note : Only Save Bio/Update/Etc should appear in this class.
 *
 */

package neuragenix.bio.biospecimen;

/**
 *
 * @author  Daniel Murley
 */


import neuragenix.dao.*;
import neuragenix.security.AuthToken;
import neuragenix.common.*;

import java.util.*;

import java.text.SimpleDateFormat;
import neuragenix.bio.utilities.*;

import java.sql.ResultSet;

import javax.swing.tree.*;

// JA-SIG uPortal
import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;
import org.jasig.portal.PropertiesManager;


public class BiospecimenCore {
    //Anton: Properties used in this class is loaded when the class is loaded for the first time
    public static String requiredQuantity = null;
    // determine if the delete needs to be cascaded to the parent level
    static boolean blcascadeUpdate = false;
    private static boolean blUseBatchCreating = false;
    public static boolean blUseSubTypeLR = false;
    private static boolean blAlwaysDispalyUnits = false;
    private static boolean showQuantityForTopLevelBio = true;
    private static boolean showInventoryForTopLevelBio = true;
    
    //Load the properties needed in this class when the class is loaded for the first time
    static {
        try {
            showInventoryForTopLevelBio = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showInventoryForTopLevelBiospecimen");
        } catch(Exception e) {
            System.err.println("[BiospecimenCore] : property not set (neuragenix.bio.Biospecimen.showInventoryForTopLevelBiospecimen) and will be defaulted to true");
        }
        
        
        try {
            showQuantityForTopLevelBio = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showQuantityForTopLevelBiospecimen");
        } catch(Exception e) {
            System.err.println("[BiospecimenCore] : property not set (neuragenix.bio.Biospecimen.showQuantityForTopLevelBiospecimen) and will be defaulted to true " +
                    " => will show quantity at the top level");
        }
        
        try {
            requiredQuantity = PropertiesManager.getProperty("neuragenix.bio.biotransactions.requiredQuantity");
        } catch (Exception e) {
            System.err.println("[BiospecimenCore] : property not set (neuragenix.bio.biotransactions.requiredQuantity)=> no restrictions of unit used");
        }
        try {
            blcascadeUpdate = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.transaction.cascadedTransactions");
        } catch (Exception e) {
            System.err.println("[BiospecimenCore] : Unable to retreive property - neuragenix.bio.transaction.cascadedTransactions");
        }
        try {
            blUseBatchCreating = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.useBatchCreating");
        } catch (Exception e){
            System.err.println("Cannot find property neuragenix.bio.Biospecimen.useBatchCreating");
        }
        try {
            blUseSubTypeLR = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.useSubTypeLR");
        } catch (Exception e){
            System.err.println("Cannot find property neuragenix.bio.Biospecimen.useSubTypeLR");
        }
        try {
            blAlwaysDispalyUnits = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.transactions.alwaysDispalyUnits");
        } catch (Exception e){
            System.err.println("Cannot find property neuragenix.bio.transactions.alwaysDispalyUnits");
        }
    }
    
    //status of biospecimen transaction
    public static final String TRANSACTION_STATUS_DELIVERED = "delivered";
    public static final String TRANSACTION_STATUS_ALLOCATED = "allocated";
    // actions
    public static final String ACTION_BIOSPECIMEN_VIEW_NOTES = "biospecimen_notes";
    public static final String ACTION_BIOSPECIMEN_ADD = "biospecimen_add";
    public static final String ACTION_BIOSPECIMEN_CLONE = "biospecimen_clone";
    public static final String ACTION_BIOSPECIMEN_UPDATE = "biospecimen_update";
    public static final String ACTION_BIOSPECIMEN_DELETE = "biospecimen_delete";
    public static final String ACTION_BIOSPECIMEN_SEARCH = "biospecimen_search";
    public static final String ACTION_BIOSPECIMEN_RESULTS = "biospecimen_results";
    public static final String ACTION_BIOSPECIMEN_VIEW = "biospecimen_view";
    public static final String ACTION_SECURITY_ERROR = "securityerror";
    public static final String ACTION_VIAL_CALCULATION = "vial_calculation";
    public static final String ACTION_VIAL_HISTORY = "vial_history";
    public static final String ACTION_AVAILABLE_TRAY_LIST = "available_tray_list";
    public static final String ACTION_CREATE_SUB_SPECIMEN = "create_sub_specimen";
    
    public static final String PERMISSION_DELIVER_QUANTITY = "biospecimen_deliver_transactions";
    public static final String PERMISSION_ADD_QUANTITY = "biospecimen_add_transactions";
    public static final String PERMISSION_DELETE_QUANTITY = "biospecimen_delete_transactions";
    public static final String PERMISSION_VIEW_QUANTITY = "biospecimen_view_transactions";
    public static final String PERMISSION_CHANGE_STUDY = "biospecimen_change_study";
    
    public static final String XSL_BIOSPECIMEN_VIEW = "biospecimen_view";
    public static final String XSL_BIOSPECIMEN_TREE_VIEW = "biospecimen_tree_view";
    public static final String XSL_FATAL_ERROR = "biospecimen_fatal_error";
    
    public static final String PROP_USE_COLLECTION_TYPE = "neuragenix.bio.Biospecimen.useCollectionType";
    public static final String PROP_COLLECTION_TYPE_NAME ="neuragenix.bio.Biospecimen.collectionTypeName";
    
    public boolean isRefresh = false;
    
    
    
    
    public static final int FAILURE = -1;
    
    
    // XXX: I dont like the following being here. if anything, these should be read in to a commons class from a properties file
    
    // Following static final strings used to define the location of the system
    public static final String SYSTEM_TIMEZONE = PropertiesManager.getProperty("neuragenix.genix.timezone");
    public static final String SYSTEM_LANGUAGE = PropertiesManager.getProperty("neuragenix.genix.languageCode");
    public static final String SYSTEM_COUNTRY = PropertiesManager.getProperty("neuragenix.genix.countryCode");
    
    
    public static final String HOSPITAL_SITE = PropertiesManager.getProperty("neuragenix.bio.HospitalSite");
    
    public static final String CLIENT = PropertiesManager.getProperty("neuragenix.bio.Client");
    
    
    
    public AuthToken authToken = null;
    
    
    private static final String DELETE_SPECIMEN_HAS_CHILDREN = "Unable to delete this biospecimen as it has sub specimens";
    private static final String SPECIMEN_UPDATE_FAILURE = "A system failure has prevented this action.  Please contact your system administrator.";
    
    
    // tree objects
    
    private DefaultMutableTreeNode treeSearchResults = null;
    
    
    
    // search objects
    
    private int intCurrentSearchAmount = 0;
    
    
    private int intLastBiospecimenAddedID = -1;
    
    private int intLastViewedBiospecimen = -1;
    
    
    // domain definition
    
    public static final int DOMAIN_BIOSPECIMEN = 0;
    public static final int DOMAIN_PATIENT = 1;
    public static final int DOMAIN_STUDY = 2;
    public static final int DOMAIN_STUDYALLOC_ALLOC = 3;
    public static final int DOMAIN_STUDYALLOC_DELIV = 4;
    
    
    
    
    
    /** Creates a new instance of BiospecimenCore */
    public BiospecimenCore(AuthToken authToken) {
        this.authToken = authToken;
    }
    
    public int getLastBiospecimenAddedID() {
        return intLastBiospecimenAddedID;
    }
    
    public int getLastViewedBiospecimenID() {
        return intLastViewedBiospecimen;
    }
    
    public void resetLastViewed()
    {
        intLastViewedBiospecimen = -1;
    }
    // permission handling should not occur here -- should be at the event manager level
    
    // may need an event return object or alike, something that can carry both the stylesheet and the XML
    
    // TODO: Exception handling
    // TODO: Locking
    
    /**
     *   Saves a biospecimen back to the db.
     *
     *   As such this function will now return a success/error code.
     */
    
    public String saveBiospecimen(ChannelRuntimeData runtimeData ) {
        
        String strErrors = null;
        
        Vector vtSaveBiospecimenFields = null;
        vtSaveBiospecimenFields = DatabaseSchema.getFormFields("cbiospecimen_save_biospecimen");
        
        DALSecurityQuery dsqSaveBiospecimen = null;
        
        
        // check whether this user has permission to add new bio
        try {
            dsqSaveBiospecimen = new DALSecurityQuery("biospecimen_add", authToken);
        } catch (Exception e) {
            System.err.println("[BiospecimenCore] : Failure when saving biospecimen");
            e.printStackTrace();
            return "Failed trying to save a biospecimen";
        }
        
        
        
        
        // Start the Database Transaction
        //dsqSaveBiospecimen.setManualCommit(true);
        ////System.out.println("Set Manual Commit");
        
        try {
            dsqSaveBiospecimen.setDomain("BIOSPECIMEN", null, null, null);
            
            // Organise all the date fields
            
            // TODO: Change this so it only builds the dates that are part of the save form fields
            // OR new function in query channel "cleanRuntimeDataForSave
            
            QueryChannel.updateDateValuesInRuntimeData(vtSaveBiospecimenFields, runtimeData);
            QueryChannel.updateTimeValuesInRuntimeData(vtSaveBiospecimenFields, runtimeData);
            
            if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA"))) {
                neuragenix.bio.utilities.CCIAUtilities.calculateClinicalAge(vtSaveBiospecimenFields, runtimeData);
            }
            
            
            int intDepth = 1;
            //if got parent, get the parent depth and add 1
            if (runtimeData.getParameter("BIOSPECIMEN_intParentID")!= null && runtimeData.getParameter("BIOSPECIMEN_intParentID").toString().length()>0) {
                String strInternalParentKey = runtimeData.getParameter("BIOSPECIMEN_intParentID");
                intDepth = BiospecimenUtilities.getParentDepthFromParentKey(strInternalParentKey) + 1;
            }
            
            runtimeData.setParameter("BIOSPECIMEN_intDepth", intDepth+"");
            
            // Obtain the admission key corresponding to the selected encounter
            if ((runtimeData.getParameter("BIOSPECIMEN_strEncounter") != null) && !runtimeData.getParameter("BIOSPECIMEN_strEncounter").equals("")) {
                DALQuery dqGetEncounterDetails = new DALQuery();
                dqGetEncounterDetails.setDomain("ADMISSIONS", null, null, null);
                dqGetEncounterDetails.setField("ADMISSIONS_intAdmissionkey", null);
                dqGetEncounterDetails.setWhere(null, 0, "ADMISSIONS_intPatientID", "=", (runtimeData.getParameter("BIOSPECIMEN_intPatientID").toString()) + "", 0, DALQuery.WHERE_HAS_VALUE);
                dqGetEncounterDetails.setWhere("AND", 0, "ADMISSIONS_strAdmissionID", "=", (runtimeData.getParameter("BIOSPECIMEN_strEncounter").toString()), 0, DALQuery.WHERE_HAS_VALUE);
                dqGetEncounterDetails.setWhere("AND", 0, "ADMISSIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsEncounterDetails = dqGetEncounterDetails.executeSelect();
                if (rsEncounterDetails.next()) {
                    String strAdmissionKey = rsEncounterDetails.getString("ADMISSIONS_intAdmissionkey");
                    runtimeData.setParameter("BIOSPECIMEN_intAdmissionKey", strAdmissionKey);
                }
            }
            
            if ( ((strErrors = QueryChannel.validateData(vtSaveBiospecimenFields, runtimeData)) == null)
            &&  ((strErrors = QueryChannel.checkRequiredFields(vtSaveBiospecimenFields, runtimeData)) == null)
            ) {
                
                dsqSaveBiospecimen.setFields(vtSaveBiospecimenFields, runtimeData);
                String strInternalBiospecimenID = null;
                
                String strBiospecimenID =  runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID");
                strInternalBiospecimenID = runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
                
                strErrors = QueryChannel.checkDuplicatesWhenUpdate(vtSaveBiospecimenFields, runtimeData, "BIOSPECIMEN_strBiospecimenID", strBiospecimenID);
                if (strErrors != null) {
                    
                    return strErrors;
                }
                
                // if strInternalBiospecimenID is not null then we have an update
                if(strInternalBiospecimenID != null && strErrors == null) {
                    
                    
                    // got an update action
                    dsqSaveBiospecimen.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strInternalBiospecimenID , 0, DALQuery.WHERE_HAS_VALUE);
                    
                    //get the result from update query
                    boolean blResult = dsqSaveBiospecimen.executeUpdate();
                    if ( blResult) {
                        
                        //Update Flag Manager
                        String strIsFlagged = null;
                        String strWasFlagged = runtimeData.getParameter("wasFlagged")+"";
                        if (runtimeData.getParameter("isFlagged") != null) {
                            strIsFlagged = runtimeData.getParameter("isFlagged");
                            
                        } else {
                            strIsFlagged = "false";
                        }
                        
                        
                        
                        // if the flag changed
                        if ((strWasFlagged.equalsIgnoreCase("true") && strIsFlagged.equalsIgnoreCase("false"))
                        || ( strWasFlagged.equalsIgnoreCase("false") && strIsFlagged.equalsIgnoreCase("true"))) {
                            
                            FlagManager.toggleFlag( Integer.parseInt(strInternalBiospecimenID), DOMAIN_BIOSPECIMEN,  authToken.getUserIdentifier());
                        }
                        
                        this.intLastBiospecimenAddedID = Integer.parseInt(strInternalBiospecimenID);
                    }
                    
                    
                    //link smarform asscociated with the study to the biospecimen
                    int intStudyKey = -1;
                    try {
                        if (runtimeData.getParameter("BIOSPECIMEN_intStudyKey") != null)
                            intStudyKey = Integer.parseInt(runtimeData.getParameter("BIOSPECIMEN_intStudyKey"));
                    } catch (Exception e) {
                        // no need to display - we dont care.
                    }
                    if (SmartformManager.checkForBiospecimenParticipant(this.intLastBiospecimenAddedID, intStudyKey) == false) {  //System.out.println("ANTON 2XXXXXXXXXXXXXXXXX: checking in ther is a bio_sf: No so SmartformManager.addBiospecimenSmartformParticipant()");
                        SmartformManager.addBiospecimenSmartformParticipant(intStudyKey, this.intLastBiospecimenAddedID , authToken);
                    }//else System.out.println("ANTON 2XXXXXXXXXXXXXXXXX: checking in ther is a bio_sf: Yes");
                    
                    
                    
                }
                
                
                // adding new biospecimen
                else if ((strErrors = QueryChannel.checkDuplicates(vtSaveBiospecimenFields, runtimeData)) == null) {
                    
                    
                    //save a new specimen
                    dsqSaveBiospecimen.setManualCommit(true);
                    boolean blSaveResult = dsqSaveBiospecimen.executeInsert();
                    
                    if (blSaveResult == false) {
                        System.err.println("[BiospecimenCore] Unable to save biospecimen - Database did not insert");
                        dsqSaveBiospecimen.cancelTransaction();
                        return "Unable to save biospecimen!";
                    }
                    
                    
                    this.intLastBiospecimenAddedID = dsqSaveBiospecimen.getInsertedRecordKey();
                    
                    
                    // grab the property for autogeneration
                    boolean blAutoGenerateID = false;
                    try {
                        blAutoGenerateID = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.AutoGenerateBiospecimenID");
                    } catch (Exception e) {
                        System.err.println("[BiospecimenCore] : Unable to retreive property - neuragenix.bio.AutoGenerateBiospecimenID");
                        e.printStackTrace();
                    }
                    
                    
                    
                    boolean blFromVialCal = false;
                    
                    if ( runtimeData.getParameter("blFromVialCalc") != null){
                        blFromVialCal = true;
                    }
                    
                    // if using auto generateID and make sure not overwrite existing strID e.g from vial calculation
                    if (blAutoGenerateID == true && blFromVialCal == false) {
                        // get the biospecimen ID.
                        String strNewBiospecimenID = "";
                        IBiospecimenIDGenerator idgGenerator = IDGenerationFactory.getBiospecimenIDGenerationInstance();
                        
                        strNewBiospecimenID = idgGenerator.getBiospecimenID(intLastBiospecimenAddedID, dsqSaveBiospecimen, authToken);
                        
                        // ensure we actually got a valid id
                        if (strNewBiospecimenID != null) {
                            String strValidation = BiospecimenUtilities.checkForDuplicateIDs(dsqSaveBiospecimen, strNewBiospecimenID, BiospecimenUtilities.BIOSPECIMENID);
                            
                            if (strValidation != null) {
                                dsqSaveBiospecimen.cancelTransaction();
                                System.err.println("[BiospecimenCore] Update has failed as Duplicate ID was returned by ID Generator");
                                return "Unable to generate an appropriate ID for this specimen";
                            }
                            
                            
                            
                            
                            
                            dsqSaveBiospecimen.reset();
                            dsqSaveBiospecimen.setDomain("BIOSPECIMEN", null, null, null);
                            dsqSaveBiospecimen.setField("BIOSPECIMEN_strBiospecimenID", strNewBiospecimenID);
                            dsqSaveBiospecimen.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intLastBiospecimenAddedID + "", 0, DALQuery.WHERE_HAS_VALUE);
                            blSaveResult = dsqSaveBiospecimen.executeUpdate();
                            if (blSaveResult == false) {
                                dsqSaveBiospecimen.cancelTransaction();
                                System.err.println("[BiospecimenCore] Unable to generate Biospecimen ID");
                                return "Unable to generate biospecimen ID";
                            } else {
                                dsqSaveBiospecimen.commitTransaction();
                            }
                        } else {
                            // invalid id returned from generator
                            System.err.println("[BiospecimenCore] Unable to create biospecimen ID");
                            dsqSaveBiospecimen.cancelTransaction();
                            return "Unable to generate biospecimen ID";
                        }
                        
                        
                    } else {
                        dsqSaveBiospecimen.commitTransaction();
                    }
                    
                    
                    //set the flag if possible
                    if ( runtimeData.getParameter("isFlagged") != null) {
                        
                        FlagManager.toggleFlag( this.intLastBiospecimenAddedID, DOMAIN_BIOSPECIMEN,  authToken.getUserIdentifier());
                    }
                    
                    
                    //link smarform asscociated with the study to the biospecimen
                    int intStudyKey = -1;
                    try {
                        if (runtimeData.getParameter("BIOSPECIMEN_intStudyKey") != null)
                            intStudyKey = Integer.parseInt(runtimeData.getParameter("BIOSPECIMEN_intStudyKey"));
                    } catch (Exception e) {
                        // no need to display - we dont care.
                    }
                    
                    
                    
                    if (SmartformManager.checkForBiospecimenParticipant(this.intLastBiospecimenAddedID, intStudyKey) == false) {  //System.out.println("ANTON XXXXXXXXXXXXXXXXX: checking in ther is a bio_sf: No so SmartformManager.addBiospecimenSmartformParticipant()");
                        SmartformManager.addBiospecimenSmartformParticipant(intStudyKey, this.intLastBiospecimenAddedID , authToken);
                    }//else System.out.println("ANTON XXXXXXXXXXXXXXXXX: checking in ther is a bio_sf: Yes");
                    
                    return null; //everything ok
                }
                
                
                
                // TODO: smartform participant code for studies/
                
                // otherwise - no worries
                
            }
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return strErrors;
    }
    
    
    /** Clone a biospecimen details, except the inventory information
     *  return null of sucessfully, otherwise return non tagged error message
     *
     */
    
    public String cloneBiospecimen(int intStudyID, int intBiospecimenID) {
        
        Vector vtViewBiospecimenFormFields = DatabaseSchema.getFormFields("cbiospecimen_clone_biospecimen");
        Hashtable htBiospecimenDetails = new Hashtable();
        
        try {
            
            
            DALSecurityQuery query = new DALSecurityQuery(ACTION_BIOSPECIMEN_ADD, authToken);
            
            
            query.clearDomains();
            query.clearFields();
            query.clearWhere();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setFields(vtViewBiospecimenFormFields, null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenID + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND",0,"BIOSPECIMEN_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResultSet = query.executeSelect();
            
            if (rsResultSet.first()) {
                for (int i = 0; i < vtViewBiospecimenFormFields.size(); i++) {
                    String strFieldName = (String) vtViewBiospecimenFormFields.get(i);
                    String strFieldValue = rsResultSet.getString(strFieldName);
                    if (strFieldValue != null) {
                        if (DatabaseSchema.getDBMSType() == DBMSTypes.ORACLE){
                            DBField field = null;
                            field = (DBField) DatabaseSchema.getFields().get(strFieldName);
                            java.sql.Date dtObject = null;
                            
                            if (field.getDataType() == DBMSTypes.DATE_TYPE ){
                                dtObject = rsResultSet.getDate(strFieldName);
                                if (dtObject != null) {
                                    SimpleDateFormat dfFormatter = new SimpleDateFormat("dd/MM/yyyy");
                                    htBiospecimenDetails.put(strFieldName, dfFormatter.format(dtObject));
                                }
                            } else if (field.getDataType() == DBMSTypes.TIME_TYPE ){
                                dtObject = rsResultSet.getDate(strFieldName);
                                if (dtObject != null) {
                                    SimpleDateFormat dfFormatter = new SimpleDateFormat("hh:mm a");
                                    htBiospecimenDetails.put(strFieldName, dfFormatter.format(dtObject));
                                }
                            } else
                                htBiospecimenDetails.put(strFieldName, strFieldValue);
                            
                        } else // if the type is not ORACLE then no problem
                            htBiospecimenDetails.put(strFieldName, strFieldValue);
                    }
                }
            }
            
            rsResultSet.close();
            
            int intIDSuffix = 0;
            // grab the property for autogeneration
            boolean blAutoGenerateID = false;
            try {
                blAutoGenerateID = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.AutoGenerateBiospecimenID");
            } catch (Exception e) {
                System.err.println("[BiospecimenCore] : Unable to retreive property - neuragenix.bio.AutoGenerateBiospecimenID");
                e.printStackTrace();
            }
            
            // Put in a temp value for the ID, until updated by the ID generation process
            if ( blAutoGenerateID == true) {
                htBiospecimenDetails.put("BIOSPECIMEN_strBiospecimenID", Integer.toString(intLastBiospecimenAddedID));
            }
            
            query.setManualCommit(true);
            query.clearDomains();
            query.clearFields();
            query.clearWhere();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setFields(vtViewBiospecimenFormFields, htBiospecimenDetails);
            query.executeInsert();
            intLastBiospecimenAddedID = query.getInsertedRecordKey();
            
            
            String strNewBiospecimenID;
            
            if ( blAutoGenerateID == true){
                // get the biospecimen ID.
                strNewBiospecimenID = "";
                IBiospecimenIDGenerator idgGenerator = IDGenerationFactory.getBiospecimenIDGenerationInstance();
                strNewBiospecimenID = idgGenerator.getBiospecimenID(intLastBiospecimenAddedID, query, authToken);
                
                // ensure we actually got a valid id
                if (strNewBiospecimenID != null && !strNewBiospecimenID.equalsIgnoreCase("-1") ) {
                    String strValidation = BiospecimenUtilities.checkForDuplicateIDs(query, strNewBiospecimenID, BiospecimenUtilities.BIOSPECIMENID);
                    
                    if (strValidation != null ) {
                        query.cancelTransaction();
                        System.err.println("[BiospecimenCore] Cloning has failed as Duplicate ID was returned by ID Generator");
                        return "Unable to generate an appropriate ID for this specimen";
                    }
                    
                    
                    
                } else {
                    query.cancelTransaction();
                    return " Unable to generate an appropriate ID for this specimen";
                }
            } else
                //get the parent string ID
                strNewBiospecimenID = BiospecimenUtilities.getNewSubBiospecimenStringID(query, BiospecimenUtilities.getUserBiospecimenID(intBiospecimenID),intIDSuffix);
            
            
            //update with new strID
            
            query.reset();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_strBiospecimenID", strNewBiospecimenID);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intLastBiospecimenAddedID + "", 0, DALQuery.WHERE_HAS_VALUE);
            boolean blSaveResult = query.executeUpdate();
            if (blSaveResult == false) {
                query.cancelTransaction();
                System.err.println("[BiospecimenCore] Unable to generate Biospecimen ID");
                return "Unable to generate biospecimen ID";
            } else {
                query.commitTransaction();
            }
            
            
            
            SmartformManager.addBiospecimenSmartformParticipant(intStudyID, intLastBiospecimenAddedID, authToken);
            
            return null;
            
            
        } catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error with cloning in Biospecimen Channel - " + e.toString(), e);
            e.printStackTrace();
            return "Error when cloning a biospecimen";
        }
        
    }
    
    public String deleteVialCalculation(int intBiospecimenKey, ChannelRuntimeData runtimeData)
    
    {
        // TODO: this needs to be made transactional
        
        // check if specimen has children -- if it does, no deletion may occur.
        //if (BiospecimenUtilities.getChildCount(intBiospecimenKey) > 0) {
        //	return DELETE_SPECIMEN_HAS_CHILDREN;
        //}
        
        // otherwise, set it to -1.
        //Vector vtKeys = new Vector();
        String strBiospecimenKeys = runtimeData.getParameter("VIAL_CALCULATION_strBiospecimenKeys");
        StringTokenizer tok = new StringTokenizer(strBiospecimenKeys, ",");
        LogService.instance().log(LogService.INFO,"xxxxxxxxxxxxxxxxxxx:::"+strBiospecimenKeys);
        
        //create a vector of keys
        
        try {
            
            // also delete the flags associated with this biospecimen
            //FlagManager.deleteFlag(intBiospecimenKey, FlagManager.DOMAIN_BIOSPECIMEN, null);
            
            
            
            
            DALSecurityQuery dsqDeleteVialCalculation = new DALSecurityQuery("biospecimen_delete", authToken);
            dsqDeleteVialCalculation.setDomain("VIAL_CALCULATION", null, null, null);
            dsqDeleteVialCalculation.setField("VIAL_CALCULATION_intDeleted", "1");
            dsqDeleteVialCalculation.setWhere(null, 0, "VIAL_CALCULATION_intBiospecimenID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            dsqDeleteVialCalculation.setWhere("AND", 0, "VIAL_CALCULATION_intDeleted", "=", 0 + "", 0, DALQuery.WHERE_HAS_VALUE);
            //LogService.instance().log(LogService.INFO,"DELETE VIAL CALCULATION:::"dsqDeleteVialCalculation.convertSelectQueryToString());
            boolean blUpdateResult = dsqDeleteVialCalculation.executeUpdate();
            
            if (blUpdateResult != true)
                return SPECIMEN_UPDATE_FAILURE;
            //delete from ix_biospecimen
            DALSecurityQuery dsqDeleteBiospecimen = new DALSecurityQuery("biospecimen_delete", authToken);
            dsqDeleteBiospecimen.setDomain("BIOSPECIMEN", null, null, null);
            dsqDeleteBiospecimen.setField("BIOSPECIMEN_intDeleted", "1");
            
            int counter=0;
            while (tok.hasMoreElements()) {
                String theToken = tok.nextToken();
                LogService.instance().log(LogService.INFO,"ttttttttttttttttttttttttt:::"+theToken);
                int intBiospecimenSubKey=0;
                if (counter ==0) {
                    dsqDeleteBiospecimen.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", theToken + "", 0, DALQuery.WHERE_HAS_VALUE);
                    
                } else {
                    //LogService.instance().log(LogService.INFO,"FIRST");
                    dsqDeleteBiospecimen.setWhere("OR", 0, "BIOSPECIMEN_intBiospecimenID", "=", theToken + "", 0, DALQuery.WHERE_HAS_VALUE);
                    
                }
                
                try {
                    intBiospecimenSubKey = Integer.parseInt(theToken);
                } catch(NumberFormatException e) {
                    LogService.instance().log(LogService.ERROR, "BiospecimenCore::() : Method deleteVialCalculation Can not convert string to number", e);
                }
                
                InventoryUtilities.updateInventoryWhenDeleteAndUnallocateBiospecimen(intBiospecimenSubKey, authToken);
                counter ++;
            }
            //LogService.instance().log(LogService.INFO,"QUERY DELETE:::"+dsqDeleteBiospecimen.convertSelectQueryToString());
            boolean blUpdateResult1 = dsqDeleteBiospecimen.executeUpdate();
            
            if (blUpdateResult1 == true)
                return null;
            else
                return SPECIMEN_UPDATE_FAILURE;
        }
        
        catch(Exception e) {
            System.err.println("[BiospecimenCore] An error has occured whilst trying to delete a vial Calculation");
            e.printStackTrace(System.err);
        }
        
        // also delete the smartform participants associated with this specimen
        //SmartformManager.deleteSmartformParticipants(null, intBiospecimenKey + "", authToken);
        
        
        
        
        return null;
    }
    /**
     *
     *
     * @returns null if specimen sucessfully deleted, otherwise non tagged error message
     *
     */
    
    public String deleteBiospecimen(int intBiospecimenKey)
    
    {
        // TODO: this needs to be made transactional
        
        // check if specimen has children -- if it does, no deletion may occur.
        if (BiospecimenUtilities.getChildCount(intBiospecimenKey) > 0) {
            return DELETE_SPECIMEN_HAS_CHILDREN;
        }
        
        // otherwise, set it to -1.
        
        try {
            
            // also delete the flags associated with this biospecimen
            FlagManager.deleteFlag(intBiospecimenKey, FlagManager.DOMAIN_BIOSPECIMEN, null);
            
            // also delete the inventory allocations for this specimen
            InventoryUtilities.updateInventoryWhenDeleteAndUnallocateBiospecimen(intBiospecimenKey, authToken);
            
            
            DALSecurityQuery dsqDeleteBiospecimen = new DALSecurityQuery("biospecimen_delete", authToken);
            dsqDeleteBiospecimen.setDomain("BIOSPECIMEN", null, null, null);
            dsqDeleteBiospecimen.setField("BIOSPECIMEN_intDeleted", "-1");
            dsqDeleteBiospecimen.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            boolean blUpdateResult = dsqDeleteBiospecimen.executeUpdate();
            
            if (blUpdateResult == true)
                return null;
            else
                return SPECIMEN_UPDATE_FAILURE;
            
        }
        
        catch(Exception e) {
            System.err.println("[BiospecimenCore] An error has occured whilst trying to delete a biospecimen");
            e.printStackTrace(System.err);
        }
        
        // also delete the smartform participants associated with this specimen
        SmartformManager.deleteSmartformParticipants(null, intBiospecimenKey + "", authToken);
        
        
        
        
        return null;
    }
    
    /**
     *
     *  Method to upgrade a transaction from allocated to delivered
     *    -- Only for single upgrades
     *
     * @returns null if transaction is successfull, otherwise an error message
     */
    
    
    public String doTransactionUpgrade(int intBiospecimenTransactionID) {
        
        String strErrorMessage = null;
        
        
        if (intBiospecimenTransactionID == -1) {
            strErrorMessage = "Transaction ID was not found";
            return strErrorMessage;
        }
        
        
        try {
            if (!authToken.hasActivity(PERMISSION_DELIVER_QUANTITY)) {
                strErrorMessage = "You do not have permission to deliver this biospecimen quantity";
                return strErrorMessage;
            } else {
                DALSecurityQuery query = new DALSecurityQuery(PERMISSION_DELIVER_QUANTITY, authToken);
                query.setDomain("BIOSPECIMEN_TRANSACTIONS", null, null, null);
                query.setField("BIOSPECIMEN_TRANSACTIONS_dtDeliveryDate", Utilities.getDateTimeStampAsString("dd/MM/yyyy"));
                query.setField("BIOSPECIMEN_TRANSACTIONS_strStatus", "Delivered");
                query.setWhere(null, 0, "BIOSPECIMEN_TRANSACTIONS_intBioTransactionID", "=", intBiospecimenTransactionID + "", 0, DALQuery.WHERE_HAS_VALUE);
                
                boolean result = query.executeUpdate();
                if (result == false) {
                    strErrorMessage = "Unable to upgrade the transaction status";
                    return strErrorMessage;
                }
                return null;
            }
        } catch (Exception e) {
            
            System.err.println("[CBiospecimen] - An unknown error has occured when attempting to upgrade a transaction");
            e.printStackTrace(System.err);
            return strErrorMessage =  "Unable to upgrade transaction";
            
        }
        
        
    }
    
    
    /** Method to delete a transaction of a biospecimen
     * return null if sucessfully, otherwise return non tagged error message
     *
     *
     *
     */
    public String doDeleteTransaction(int intBiospecimenKey, int intBiospecimenTransactionKey) {
        
        String strErrorMessage = null;
        try {
            
            // Check for delete rights
            
            
            
            if (!authToken.hasActivity(PERMISSION_DELETE_QUANTITY)) {
                strErrorMessage = "You do not have permission to delete the biospecimen quantity";
                return strErrorMessage;
            } else {
                // calculate the new quantity
                Vector vtUpdateQuantity = DatabaseSchema.getFormFields("cbiospecimen_update_biospecimen_quantity");
                DALSecurityQuery query = new DALSecurityQuery(ACTION_BIOSPECIMEN_VIEW, authToken);
                query.setDomain("BIOSPECIMEN", null, null, null);
                query.setFields(vtUpdateQuantity, null);
                query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
                
                ResultSet rsResult = query.executeSelect();
                rsResult.next();
                double dbCurrentQuantity = rsResult.getDouble(1);
                double dbRemoved = rsResult.getDouble(2);
                
                Vector vtViewTransactionQuantity = DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen_transaction_quantity");
                query.reset();
                query.setDomain("BIOSPECIMEN_TRANSACTIONS", null, null, null);
                query.setFields(vtViewTransactionQuantity, null);
                query.setWhere(null, 0, "BIOSPECIMEN_TRANSACTIONS_intBioTransactionID", "=", intBiospecimenTransactionKey + "", 0, DALQuery.WHERE_HAS_VALUE);
                rsResult = query.executeSelect();
                rsResult.next();
                
                
                
                //get transaction quantity
                double dbTransactionQuantity = rsResult.getDouble(1);
                
                double dbNewQuantity = dbCurrentQuantity - dbTransactionQuantity + dbRemoved;
                
                
                
                // only do this transaction if the new quantity is positive
                
                rsResult.close();
                
                // if (lrBiospecLock.lockWrites()) {
                if (dbNewQuantity >= 0) {
                    // delete the transaction
                    query.reset();
                    query.setDomain("BIOSPECIMEN_TRANSACTIONS", null, null, null);
                    query.setField("BIOSPECIMEN_TRANSACTIONS_intDeleted", "-1");
                    query.setWhere(null, 0, "BIOSPECIMEN_TRANSACTIONS_intBioTransactionID", "=", intBiospecimenTransactionKey + "", 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                    
                    
                    // update bio quantity
                    query.reset();
                    query.setDomain("BIOSPECIMEN", null, null, null);
                    
                    if( dbTransactionQuantity > 0 )
                        query.setField("BIOSPECIMEN_flNumberCollected", String.valueOf(dbCurrentQuantity - dbTransactionQuantity));
                    else
                        query.setField("BIOSPECIMEN_flNumberRemoved", String.valueOf(dbRemoved - dbTransactionQuantity));
                    
                    query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
                    
                    query.executeUpdate();
                    
                    // determine if the delete needs to be cascaded to the parent level
                    //moved up to be static
                    //boolean blcascadeUpdate = false;
                    //try {
                    //blcascadeUpdate = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.transaction.cascadedTransactions");
                    //}
                    //catch (Exception e) {
                    //System.err.println("[BiospecimenCore] : Unable to retreive property - neuragenix.bio.transaction.cascadedTransactions");
                    //e.printStackTrace();
                    //}
                    
                    if (blcascadeUpdate) {
                        TransactionManager tm = new TransactionManager(intBiospecimenKey, authToken, false, dbTransactionQuantity, dbCurrentQuantity,  dbRemoved);
                        tm.cascadedUpdateTransactions();
                        tm = null;
                    }
                    
                    // lrBiospecLock.unlockWrites();
                } else {
                    strErrorMessage = "This action was cancelled since it makes the quantity becomes negative";
                }
                //}
                // else {
                //     strLockError = "Delete failed since the record is being viewed by other users.";
                // }
                
            }// End of authorization else
            
            // XXX: view biospecimen was being called here
            // viewBiospecimen(strBiospecimenID);
        } catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
        }
        return strErrorMessage;
    }
    
    
    /** Method used for building the biospecimen form --
     *  This is used commonly by add new bio specimen from patient channel or add sub bio from bispecimen channel
     *
     * @return XML representation of the page
     */
    
    
    
    public String buildAddBiospecimenXML(String strInternalPatientKey, String strBiospecimenParentKey, ChannelRuntimeData runtimeData) {
        ////System.out.println ("add bio called");
        StringBuffer strXML = new StringBuffer();
        try {
            int intPatientKey = 0;
            String strBioParentKey = strBiospecimenParentKey;
            
            if (strInternalPatientKey == null && runtimeData != null) {
                String strTempPatientKey = runtimeData.getParameter("PATIENT_intInternalPatientID");
                intPatientKey = Integer.parseInt(strTempPatientKey);
            } else if (strInternalPatientKey != null ){
                intPatientKey = Integer.parseInt(strInternalPatientKey);
            }
            
            
            Vector vtViewBiospecimen = (Vector) DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen").clone();
            strXML.append(PatientUtilities.getPatientDetailsXML(intPatientKey + "", authToken));
            strXML.append(QueryChannel.buildFormLabelXMLFile(vtViewBiospecimen));
            
            if (runtimeData != null) {
                // if runtime data not null then fill the form
                String strSampleType = null;
                if (runtimeData.getParameter("BIOSPECIMEN_strSampleType") != null) {
                    strSampleType = runtimeData.getParameter("BIOSPECIMEN_strSampleType");
                }
                //Check property for prefix bio ID
                if(PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.displayIDPrefix") == true) {
                    if (runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID") == null) {
                        IBiospecimenIDGenerator idgen = IDGenerationFactory.getBiospecimenIDGenerationInstance();
                        strXML.append("<BIOSPECIMEN_strBiospecimenID>" + idgen.getBiospecimenIDPrefix() + "</BIOSPECIMEN_strBiospecimenID>");
                    }
                }
                
                // calculate Clinical Age if client is CCIA
                if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA"))) {
                    neuragenix.bio.utilities.CCIAUtilities.calculateClinicalAge(vtViewBiospecimen, runtimeData);
                }
                
                
                if (strSampleType == null ) {
                    //try to get the default sample value
                    DBField field = (DBField)DatabaseSchema.getFields().get("BIOSPECIMEN_strSampleType");
                    
                    String strLOVType = field.getLOVType();
                    
                    //get the first LOV of type strLOVType
                    if (strLOVType != null) {
                        DALSecurityQuery query = new DALSecurityQuery();
                        query.setDomain("LOV", null, null,null);
                        query.setField("LOV_strLOVValue", null);
                        query.setWhere(null, 0, "LOV_strLOVType", "=", strLOVType, 0,  DALQuery.WHERE_HAS_VALUE);
                        query.setWhere("AND", 0, "LOV_intDeleted", "=", 0+"", 0,  DALQuery.WHERE_HAS_VALUE);
                        query.setWhere("AND", 0, "LOV_intLOVSortOrder", "=", 1+"", 0,  DALQuery.WHERE_HAS_VALUE);
                        
                        ResultSet rs = query.executeSelect();
                        if (rs.first()) {
                            strSampleType = rs.getString("LOV_strLOVValue");
                        }
                    }
                    
                }
                
                
                if ( strSampleType != null) {
                    String strCurrentSubType = "";
                    
                    if (runtimeData.getParameter("BIOSPECIMEN_strSampleSubType") != null && runtimeData.getParameter("newTypeSelected").equals("false")) {
                        strCurrentSubType = runtimeData.getParameter("BIOSPECIMEN_strSampleSubType");
                    }
                    
                    //build Subtype list
                    strXML.append(QueryChannel.buildLOVXMLFromParent("BIOSPECIMEN_strSampleSubType",strCurrentSubType , strSampleType));
                    
                    String strSubTypeLR = "";
                    
                    if (runtimeData.getParameter("BIOSPECIMEN_strSubTypeLR") != null && runtimeData.getParameter("newTypeSelected").equals("false")) {
                        strSubTypeLR = runtimeData.getParameter("BIOSPECIMEN_strSubTypeLR");
                    }
                    
                    //build SubType LR
                    if (blUseSubTypeLR) {
                        strXML.append(QueryChannel.buildLOVXMLFromParent("BIOSPECIMEN_strSubTypeLR", strSubTypeLR, strSampleType));
                    }
                    
                }
                
                vtViewBiospecimen.remove("BIOSPECIMEN_strSampleSubType");
                if (blUseSubTypeLR) {
                    vtViewBiospecimen.remove("BIOSPECIMEN_strSubTypeLR");
                }
                strXML.append(QueryChannel.buildViewXMLFile(vtViewBiospecimen, runtimeData));
                
                QueryChannel.updateDateValuesInRuntimeData(vtViewBiospecimen,runtimeData);
                QueryChannel.updateTimeValuesInRuntimeData(vtViewBiospecimen,runtimeData);
                strXML.append(buildUseCollectionXML(strSampleType,strSampleType));
                
                if (runtimeData.getParameter("isFlagged")!= null) {
                    strXML.append("<flag>true</flag>");
                } else strXML.append("<flag>false</flag>");
                
                
                
                if (strBioParentKey == null) {
                    strBioParentKey = runtimeData.getParameter("BIOSPECIMEN_intParentID");
                }
                
                
            } else {
                //tricky part here remove SampleSubType for avoid these fields get refilled
                vtViewBiospecimen.remove("BIOSPECIMEN_strSampleSubType");
                vtViewBiospecimen.remove("BIOSPECIMEN_strBiospecimenID");
                strXML.append(QueryChannel.buildAddFormXMLFile(vtViewBiospecimen));
                
                
            }
            
            // output the study details
            boolean blShowNonConsentedStudies = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showNonConsentedStudies");
            boolean blShowExpiredStudies = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showExpiredStudies");
            strXML.append(StudyUtilities.getListOfStudiesXML(blShowExpiredStudies, blShowNonConsentedStudies, intPatientKey, StudyUtilities.DOMAIN_PATIENT, null));
            
            
            strXML.append(PatientUtilities.getPatientEncountersXML(intPatientKey));
            
            if (strBioParentKey != null && !strBioParentKey.equalsIgnoreCase("-1")) {
                strXML.append("<BIOSPECIMEN_intParentID>");
                strXML.append(strBioParentKey);
                strXML.append("</BIOSPECIMEN_intParentID>");
                
                strXML.append("<BIOSPECIMEN_strParentID>");
                strXML.append(strBioParentKey);
                strXML.append("</BIOSPECIMEN_strParentID>");
                
                
                
            } else {
                strXML.append("<BIOSPECIMEN_intParentID>");
                strXML.append("-1");
                strXML.append("</BIOSPECIMEN_intParentID>");
                
                strXML.append("<BIOSPECIMEN_strParentID>");
                strXML.append("N/A");
                strXML.append("</BIOSPECIMEN_strParentID>");
                
            }
            if (runtimeData.getParameter("PATIENT_intInternalPatientID")!= null)
                strXML.append("<PATIENT_intInternalPatientID>" + runtimeData.getParameter("PATIENT_intInternalPatientID") + "</PATIENT_intInternalPatientID>");
            
            
            boolean blAutoGenerateID = false;
            try {
                blAutoGenerateID = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.AutoGenerateBiospecimenID");
            } catch (Exception e) {
                System.err.println("[BiospecimenCore] Missing Property - neuragenix.bio.AutoGenerateBiospecimenID");
                e.printStackTrace();
            }
            
            if (blAutoGenerateID == true) {
                strXML.append("<BiospecimenIDAutoGeneration tempValue=\"" + authToken.getSessionUniqueID() + "\">true</BiospecimenIDAutoGeneration>");
            }
            
            // Set the permission whether the biospecimen study may be changed by the user
            if (authToken.hasActivity(PERMISSION_CHANGE_STUDY)) {
                strXML.append("<PermissionChangeStudy> + true + </PermissionChangeStudy>");
            }
            
            return strXML.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
    
    
    /**
     *
     *  Method to build the xml for ONLY the biospecimen details.
     *  Associated patient details, transaction details, or inventory details do not go here.
     *
     *  @param intBiospecimenKey Internal key of the biospecimen for which we want the details.
     *
     */
    
    public String getBiospecimenDetailsXML(int intBiospecimenKey, ChannelRuntimeData runtimeData) {
        
        
        StringBuffer strXML = new StringBuffer();
        
        if (intBiospecimenKey < 0) {
            return null;
        }
        
        
        
        try {
            // TODO : proper try/catch block around this code, to pick up security failures
            // TODO : LOCKING
            // TODO : Vial Calculation support
            
            
            int intPatientKey = PatientUtilities.getPatientKey(intBiospecimenKey);
            Vector vtViewBiospecimen = (Vector)DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen").clone();
            
            
            DALSecurityQuery dsqBiospecimenView = new DALSecurityQuery(ACTION_BIOSPECIMEN_VIEW, authToken);
            dsqBiospecimenView.setDomain("BIOSPECIMEN", null, null, null);
            dsqBiospecimenView.setFields(vtViewBiospecimen, null);
            dsqBiospecimenView.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            dsqBiospecimenView.setWhere("AND", 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            
            ResultSet rs = dsqBiospecimenView.executeSelect();
            
            if (!rs.first()) {
                rs.close();
                return null;
            }
            
            
            
            if (FlagManager.checkFlag(intBiospecimenKey, FlagManager.DOMAIN_BIOSPECIMEN) == true) {
                strXML.append("<flag>true</flag>");
            } else strXML.append("<flag>false</flag>");
            
            Vector vtEncounterField = DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_encounter");
            
            // Get the the list of encounters linked to the patient
            DALSecurityQuery encounterQuery = new DALSecurityQuery();
            encounterQuery.setDomain("ADMISSIONS", null, null, null);
            encounterQuery.setFields(vtEncounterField, null);
            encounterQuery.setWhere(null, 0, "ADMISSIONS_intPatientID", "=", intPatientKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            encounterQuery.setWhere("AND", 0, "ADMISSIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            encounterQuery.setOrderBy("ADMISSIONS_strAdmissionID", "ASC");
            ResultSet rsBiospecimenEncounterList = encounterQuery.executeSelect();
            
            
            strXML.append(QueryChannel.buildFormLabelXMLFile(vtViewBiospecimen));
            
            if(isRefresh) {
                vtViewBiospecimen =  DatabaseSchema.getFormFields("cbiospecimen_refresh_view_biospecimen");
                
                QueryChannel.updateDateValuesInRuntimeData(vtViewBiospecimen,runtimeData);
                QueryChannel.updateTimeValuesInRuntimeData(vtViewBiospecimen,runtimeData);
                strXML.append(QueryChannel.buildViewXMLFile(vtViewBiospecimen, runtimeData));
                String strLastSelected = runtimeData.getParameter("BIOSPECIMEN_strSampleType")+"";
                strXML.append(buildUseCollectionXML(strLastSelected,strLastSelected ));
                //build up subtype LR if needed
                if (runtimeData.getParameter("BIOSPECIMEN_strSampleType") != null) {
                    String strSubType = "";
                    if (runtimeData.getParameter("BIOSPECIMEN_strSampleSubType") != null && runtimeData.getParameter("newTypeSelected").equals("false")) {
                        strSubType = runtimeData.getParameter("BIOSPECIMEN_strSampleSubType");
                    }
                    
                    strXML.append( QueryChannel.buildLOVXMLFromParent("BIOSPECIMEN_strSampleSubType", strSubType, runtimeData.getParameter("BIOSPECIMEN_strSampleType")));
                    
                    if (blUseSubTypeLR) {
                        String strSubTypeLR = "";
                        if (runtimeData.getParameter("BIOSPECIMEN_strSubTypeLR") != null && runtimeData.getParameter("newTypeSelected").equals("false")) {
                            strSubTypeLR = runtimeData.getParameter("BIOSPECIMEN_strSubTypeLR");
                        }
                        strXML.append(QueryChannel.buildLOVXMLFromParent("BIOSPECIMEN_strSubTypeLR", strSubTypeLR, runtimeData.getParameter("BIOSPECIMEN_strSampleType")));
                    }
                    
                }
            } else {
                vtViewBiospecimen.remove("BIOSPECIMEN_strSampleSubType");
                if (blUseSubTypeLR) {
                    vtViewBiospecimen.remove("BIOSPECIMEN_strSubTypeLR");
                }
                strXML.append(QueryChannel.buildViewFromKeyXMLFile(vtViewBiospecimen, "BIOSPECIMEN_intBiospecimenID", intBiospecimenKey));
                String strSampleType = rs.getString("BIOSPECIMEN_strSampleType");
                String strSampleSubType = rs.getString("BIOSPECIMEN_strSampleSubType");
                String strSampleSubTypeLR = null;
                if (blUseSubTypeLR) {
                    strSampleSubTypeLR = rs.getString("BIOSPECIMEN_strSubTypeLR");
                }
                
                if (strSampleType != null) {
                    // build subtype list
                    if (strSampleSubType != null) {
                        strXML.append(QueryChannel.buildLOVXMLFromParent("BIOSPECIMEN_strSampleSubType", strSampleSubType, strSampleType));
                    }
                    strXML.append(buildUseCollectionXML(strSampleType, ""));
                    //build up subtype LR if needed
                    
                    if (blUseSubTypeLR) {
                        strXML.append(QueryChannel.buildLOVXMLFromParent("BIOSPECIMEN_strSubTypeLR", strSampleSubTypeLR, strSampleType));
                    }
                    
                    
                } else {
                    // otherwise no list should display at all.
                }
                
                
            }
            strXML.append(QueryChannel.buildSearchXMLFile("search_encounter_list", rsBiospecimenEncounterList, vtEncounterField));
            ////System.out.println(strXML.toString());
            rsBiospecimenEncounterList.close();
            
            rs.close();
        } catch(Exception e) {
            e.printStackTrace(System.err);
            LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
        }
        
        return strXML.toString();
    }
    
    /** @param strLastSelected used to check where the collectionName has been selected before,
     *   if yes, it has been added by BuildViewXMLFile
     *    @param strValue if you want the collection to be selected
     */
    
    
    public String  buildUseCollectionXML(String strValue, String strLastSelected) {
        
        StringBuffer sbResult = new StringBuffer();
        
        
        boolean blUseCollectionType = false;
        String strCollectionName = null ;
        
        
        try {
            blUseCollectionType =  PropertiesManager.getPropertyAsBoolean(PROP_USE_COLLECTION_TYPE);
            strCollectionName = PropertiesManager.getProperty(PROP_COLLECTION_TYPE_NAME);
            
        } catch (Exception e) {
            e.printStackTrace();
            LogService.log(LogService.ERROR, "Can not get useCollectionType or collectionTypeName from portal.properties");
            
        }
        if (blUseCollectionType && strCollectionName != null && !strLastSelected.equalsIgnoreCase(strCollectionName)) {
            sbResult.append("<BIOSPECIMEN_strSampleType selected=\"");
            
            if (strValue != null && strValue.equalsIgnoreCase(strCollectionName))
                sbResult.append("1");
            else sbResult.append("0");
            sbResult.append("\">"+ strCollectionName +"</BIOSPECIMEN_strSampleType>");
            
        }
        
        return sbResult.toString();
    }
    
    
    
    
    
    public String getSearchCriteriaXML() {
        StringBuffer strXML = new StringBuffer();
        Vector vtFields = DatabaseSchema.getFormFields("cbiospecimen_biospecimen_search");
        
        try {
            strXML.append(QueryChannel.buildFormLabelXMLFile(vtFields));
            strXML.append(QueryChannel.buildAddFormXMLFile(vtFields));
            strXML.append(StudyUtilities.getListOfStudiesXML(true));
        } catch (Exception e) {
            ////System.out.println ("Get search criteria XML failed");
            e.printStackTrace();
        }
        // build study
        
        return strXML.toString();
    }
    
    
    public String buildViewBiospecimenXML(int intBiospecimenKey, ChannelRuntimeData runtimeData) {
        StringBuffer strXML = new StringBuffer();
        //DALSecurityQuery dsqViewBiospecimen = null;
        // ResultSet rs = null;
        
        boolean blShowTransactions = false;
        boolean blShowTransactionsForCollections = false;
        boolean blShowInventory = false;
        boolean blShowInventoryForCollections = false;
        
        // verify we have access to view a biospecimen
        
        try {
            if (!authToken.hasActivity("biospecimen_view")) {
                System.err.println("User : " + authToken.getUserIdentifier() + " tried to illegally access a biospecimen");
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        
        // properties calls
        
        try {
            blShowTransactions = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showTransactions");
            blShowInventory = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showInventory");
            blShowInventoryForCollections = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showInventoryForCollections");
            blShowTransactionsForCollections = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showTransactionsForCollections");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        // check we have access to the other biospecimen functions
        try {
            blShowTransactions = authToken.hasActivity(PERMISSION_VIEW_QUANTITY);
            blShowInventory = authToken.hasActivity("biospecimen_view_inventory");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        strXML.append(getBiospecimenDetailsXML(intBiospecimenKey,runtimeData));
        int depth  = BiospecimenUtilities.getDepthFromKey(intBiospecimenKey+"", null );
        
        try {
            //check whether user has access to transaction
            // ouput the transaction details
            if (blShowTransactionsForCollections && blShowTransactions ) {
                
                //if the depth = 1 and show trans for the top level, or depth != 1
                if ((depth == 1 && showQuantityForTopLevelBio) || depth != 1){
                    
                    strXML.append(getTransactionDetailsXML(intBiospecimenKey));
                    strXML.append("<showTransactions>true</showTransactions>");
                }
                
            }
        } catch(Exception e) {
            e.printStackTrace(System.err);
        }
        
        
        
        if (blShowInventoryForCollections && blShowInventory) {
            // if the depth = 1 and show inv for the top level, or depth != 1
            if ((depth == 1 && showInventoryForTopLevelBio) || depth != 1){
                
                strXML.append("<showInventory>true</showInventory>");
                // output the inventory details (neuragenix.bio.Biospecimen.allowInlineInventoryUpdates)
                strXML.append(InventoryUtilities.getInventoryXML(intBiospecimenKey,authToken));
            }
        }
        
        // output the available module details
        strXML.append(getModuleLinkXML());
        
        //get the Vial Cal key if already get one
        //Tom change for ccia
        String strVialKey = BiospecimenUtilities.getVialCalculationKey(intBiospecimenKey);
        //LogService.instance().log(LogService.INFO,"THIS IS BIOKEY:::"+intBiospecimenKey);
        //LogService.instance().log(LogService.INFO,"THIS IS strVialKey:::"+strVialKey);
        if (strVialKey.compareToIgnoreCase("-1")== 0) {
            //LogService.instance().log(LogService.INFO,"THIS IS INSIDE strVialKey:::"+strVialKey);
            int parentKey = BiospecimenUtilities.getBiospecimenParentKey(intBiospecimenKey);
            //LogService.instance().log(LogService.INFO,"THIS IS INSIDE parentKey:::"+parentKey);
            if (parentKey != -1) {
                strVialKey = BiospecimenUtilities.getVialCalculationKey(parentKey)+"";
            }
        }
        //End Tom change for ccia
        strXML.append("<VIAL_CALCULATION_intVialKey>" + strVialKey + "</VIAL_CALCULATION_intVialKey>");
        
        // output the study details
        boolean blShowNonConsentedStudies = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showNonConsentedStudies");
        boolean blShowExpiredStudies = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showExpiredStudies");
        strXML.append(StudyUtilities.getListOfStudiesXML(blShowExpiredStudies, blShowNonConsentedStudies, intBiospecimenKey, StudyUtilities.DOMAIN_BIOSPECIMEN, "biospecimen"));
        
        // output the patient details if allowed to do so
        try {
            if (authToken.hasActivity("patient_view"))
                strXML.append("<patient_details>" + PatientUtilities.getPatientDetailsXML(PatientUtilities.getPatientKey(intBiospecimenKey) + "", authToken) + "</patient_details>" );
        } catch (Exception ex) {
            System.err.println("[BiospecimenCore] Activity denied");
            ex.printStackTrace(System.err);
        }
        // output the side tree if we have a system which is supporting it....
        BiospecimenSideTree bst = null;
        
        boolean blUseSideTree = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.useSideTree");
        
        if (blUseSideTree == true) {
            try {
                bst = new BiospecimenSideTree(authToken);
                
                bst.buildBiospecimenTree(intBiospecimenKey + "");
                strXML.append(bst.buildXMLForBiospecimenSideTree(intBiospecimenKey + ""));
            } catch (Exception e) {
                ////System.out.println ("Error building side tree");
                e.printStackTrace();
            }
        }
        this.intLastViewedBiospecimen = intBiospecimenKey;
        boolean blShowNotes = false;
        try {
            blShowNotes = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.showNotes");
        } catch (Exception exc) {
            exc.printStackTrace(System.err);
        }
        
        if (blShowNotes) {
            strXML.append(AttachmentsAndNotes.buildViewNotesXML(runtimeData));
            strXML.append("<viewnotes>"+runtimeData.getParameter("viewnotes")+"</viewnotes>");
            //strXML.append("<addnotes>"+runtimeData.getParameter("addnotes")+"</addnotes>");
            //strXML.append("<deletenotes>"+runtimeData.getParameter("deletenotes")+"</deletenotes>");
            
        }
        
        
        try {
            // Set the permission whether the biospecimen study may be changed by the user
            if (authToken.hasActivity(PERMISSION_CHANGE_STUDY)) {
                strXML.append("<PermissionChangeStudy> + true + </PermissionChangeStudy>");
            }
        } catch (Exception exc) {
            exc.printStackTrace(System.err);
        }
        //This part posted from Old CBiospecimen.java in previous version
        String age = calculateDetailedBioAge(intBiospecimenKey + "");
        if(age != null) {
            strXML.append("<BIOSPECIMEN_age>"+age+"</BIOSPECIMEN_age>");
        }
        return strXML.toString();
        
    }
    
    
    
    
    
    
    
    /**
     *  Returns XML for the stylesheet to decide which modules the user, and the system actually has access to
     *
     */
    
    
    private String getModuleLinkXML() {
        StringBuffer strXML = new StringBuffer();
        
        strXML.append("<biospecimen_modules>");
        
        try {
            strXML.append("<bioanalysis>");
            strXML.append(PropertiesManager.getProperty("neuragenix.bio.Biospecimen.useBioanalysis"));
            strXML.append("</bioanalysis>");
            
            strXML.append("<studySmartforms>");
            strXML.append(PropertiesManager.getProperty("neuragenix.bio.Biospecimen.useStudySmartforms"));
            strXML.append("</studySmartforms>");
            
            strXML.append("<batchCloning>");
            strXML.append(PropertiesManager.getProperty("neuragenix.bio.Biospecimen.useBatchCloning"));
            strXML.append("</batchCloning>");
            
            strXML.append("<quantityAllocations>");
            strXML.append(PropertiesManager.getProperty("neuragenix.bio.Biospecimen.useQuantityAllocations"));
            strXML.append("</quantityAllocations>");
            
            strXML.append("<vialCalculation>");
            strXML.append(PropertiesManager.getProperty("neuragenix.bio.Biospecimen.useVialCalculation"));
            strXML.append("</vialCalculation>");
            
            
            
            strXML.append("<batchCreating>");
            strXML.append(blUseBatchCreating+"");
            strXML.append("</batchCreating>");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        strXML.append("</biospecimen_modules>");
        
        return strXML.toString();
        
        
        
        
        
        
    }
    
    private Vector vtSearchResultsIndex = new Vector();
    private Hashtable htSearchResultsIndex = new Hashtable();
    
    private void rebuildSearchIndex() {
        Enumeration enum1 = treeSearchResults.preorderEnumeration();
        DefaultMutableTreeNode tempTreeNode = null;
        vtSearchResultsIndex = new Vector(100, 100);
        int nodeCounter = -1;
        
        while (enum1.hasMoreElements()) {
            nodeCounter++;
            tempTreeNode = (DefaultMutableTreeNode)enum1.nextElement();
            
            vtSearchResultsIndex.add(nodeCounter, tempTreeNode);
            
            Hashtable htData = (Hashtable) tempTreeNode.getUserObject();
            if (htData != null) {
                String strInternalBioID = (String) htData.get("BIOSPECIMEN_intBiospecimenID");
                if (strInternalBioID != null) {
                    htSearchResultsIndex.put(strInternalBioID, htData);
                }
            }
            
        }
        
        return;
        
    }
    
    
    
    public static final int TRANSACTIONS_QUERY_ALLOCATED = 1;
    public static final int TRANSACTIONS_QUERY_DELIVERED = 2;
    
    public DALQuery getBiospecimenTransactionsQuery(int intStudyKey, String strType, String strSubType, int type) {
        
        DALQuery query = null;
        
        try {
            query = new DALQuery();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setDomain("BIOSPECIMEN_TRANSACTIONS", "BIOSPECIMEN_TRANSACTIONS_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID", "INNER JOIN");
            query.setField("BIOSPECIMEN_intBiospecimenID", null);
            query.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            if (strType != null && (!strType.equals(""))) {
                query.setWhere("AND", 0, "BIOSPECIMEN_strSampleType", "=", strType, 0, DALQuery.WHERE_HAS_VALUE);
                
                if (strSubType != null && (!strSubType.equals(""))) {
                    query.setWhere("AND", 0, "BIOSPECIMEN_strSampleSubType", "=", strSubType, 0, DALQuery.WHERE_HAS_VALUE);
                }
            }
            
            switch (type) {
                case TRANSACTIONS_QUERY_ALLOCATED:
                    query.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_strStatus", "=", "Allocated", 0, DALQuery.WHERE_HAS_VALUE);
                    break;
                    
                case TRANSACTIONS_QUERY_DELIVERED:
                    query.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_strStatus", "=", "Delivered", 0, DALQuery.WHERE_HAS_VALUE);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return query;
    }
    
    
    
    public String getBiospecimenSearchResultsXML(int intCallingDomain, Hashtable htUserSearchCriteria, boolean blNewSearch, int intPageNumber, int intAmount, boolean blExcess, int intExpandID) {
        return getBiospecimenSearchResultsXML(intCallingDomain, htUserSearchCriteria, blNewSearch, intPageNumber, intAmount, blExcess, intExpandID, false);
    }
    
    
    
    
    
    /**
     *
     *  Produces the XML for a set of biospecimen search results
     *
     *  @param intCallingDomain The domain that is requesting the data -- Backwards compatibility for legacy channels
     *  @param htSearchCriteria Criteria based on form field names that we're searching on
     *  @param intPageNumber Result page number that we're interested in
     *  @param intAmount amount of specimens to display, if -1, default is assumed
     *  @returns String - The XML, stylesheet can be decided by the caller
     *
     */
    
    // new property neuragenix.bio.Biospecimen.maxSearchDepth
    // new property neuragenix.bio.Biospecimen.searchCaseSensitive
    // new property neuragenix.bio.Biospecimen.searchLeftLike
    // new property neuragenix.bio.Biospecimen.searchRightLike
    
    
    
    public String getBiospecimenSearchResultsXML(int intCallingDomain, Hashtable htUserSearchCriteria, boolean blNewSearch, int intPageNumber, int intAmount, boolean blExcess, int intExpandID, boolean isTransactionSearch) {
        
        StringBuffer strXML = new StringBuffer();
        DALQuery dsqQuery = null;
        ResultSet rs = null;
        Vector vtSearchFields = DatabaseSchema.getFormFields("cbiospecimen_biospecimen_search");
        
        Hashtable htSearchCriteria = (Hashtable) htUserSearchCriteria.clone();
        Hashtable htKeyCriteria = new Hashtable();
        
        // TODO: make this code more intelligent... likes only on text data -- no ints!.
        
        if (htSearchCriteria.containsKey("PATIENT_strPatientID")) {
            String strPatientID = (String) htSearchCriteria.get("PATIENT_strPatientID");
            int intInternalPatientKey = PatientUtilities.getPatientKeyFromUserID(strPatientID);
            htSearchCriteria.remove("PATIENT_strPatientID");
            htSearchCriteria.put("BIOSPECIMEN_intPatientID", intInternalPatientKey + "");
        }
        
        
        // split key criteria and text criteria
        if (htSearchCriteria.containsKey("BIOSPECIMEN_intPatientID")) {
            htKeyCriteria.put("BIOSPECIMEN_intPatientID", htSearchCriteria.get("BIOSPECIMEN_intPatientID"));
            htSearchCriteria.remove("BIOSPECIMEN_intPatientID");
        }
        
        if (htSearchCriteria.containsKey("BIOSPECIMEN_intStudyKey")) {
            htKeyCriteria.put("BIOSPECIMEN_intStudyKey", htSearchCriteria.get("BIOSPECIMEN_intStudyKey"));
            htSearchCriteria.remove("BIOSPECIMEN_intStudyKey");
        }
        
        if (htSearchCriteria.containsKey("BIOSPECIMEN_dtSampleDate")) {
            htKeyCriteria.put("BIOSPECIMEN_dtSampleDate", htSearchCriteria.get("BIOSPECIMEN_dtSampleDate"));
            htSearchCriteria.remove("BIOSPECIMEN_dtSampleDate");
        }
        
        if (htSearchCriteria.containsKey("BIOSPECIMEN_dtExtractedDate")) {
            htKeyCriteria.put("BIOSPECIMEN_dtExtractedDate", htSearchCriteria.get("BIOSPECIMEN_dtExtractedDate"));
            htSearchCriteria.remove("BIOSPECIMEN_dtExtractedDate");
        }
        
        
        /* ------------ get transaction search query ----------- */
        
        
        DALQuery dalTransQuery = null;
        
        if (isTransactionSearch == true) {
            
            try // if this block fails, then we dont care about transactions
            {
                
                
                int intStudyKey = Integer.parseInt((String) htKeyCriteria.get("BIOSPECIMEN_intStudyKey"));
                String strBiospecimenType = (String) htSearchCriteria.get("BIOSPECIMEN_strSampleType");
                String strBiospecimenSubType = (String) htSearchCriteria.get("BIOSPEICMEN_strSampleSubType");
                int intTransType = Integer.parseInt((String) htSearchCriteria.get("BIOSPECIMEN_TRANSACTIONS_searchType"));
                htSearchCriteria.remove("BIOSPECIMEN_TRANSACTIONS_searchType");
                
                htSearchCriteria.remove("BIOSPECIMEN_TRANSACTIONS_searchType");
                htKeyCriteria.remove("BIOSPECIMEN_intStudyKey");
                
                dalTransQuery = getBiospecimenTransactionsQuery(intStudyKey, strBiospecimenType, strBiospecimenSubType, intTransType);
                dalTransQuery.setCaseSensitive(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        
        // node expansion detection
        
        Vector vtFormFields = DatabaseSchema.getFormFields("cbiospecimen_biospecimen_search_results");
        strXML.append(QueryChannel.buildFormLabelXMLFile(vtFormFields));
        
        if (intExpandID != -1 && blNewSearch == false) {
            // the user wants to expand a node... isnt that wonderful. No
            
            // set up a query to get the children of the expanded node, provided they meet the requirements.
            
            DALQuery dalChildQuery = new DALQuery();
            DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) vtSearchResultsIndex.get(intExpandID);
            Hashtable htBiospecimenDetails = (Hashtable) tempNode.getUserObject();
            
            String strInternalBiospecimenKey = (String) htBiospecimenDetails.get("BIOSPECIMEN_intBiospecimenID");
            
            String strEqualityCondition = getSearchEqualityCondition();
            try {
                dalChildQuery.setDomain("BIOSPECIMEN", null, null, null);
                if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA"))) {
                    dalChildQuery.setDomain("PATIENT", "PATIENT_intInternalPatientID", "BIOSPECIMEN_intPatientID", "INNER JOIN");
                }
                dalChildQuery.setFields(vtFormFields, null);
                dalChildQuery.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                dalChildQuery.setWhereMultiField("AND", "AND", 0, strEqualityCondition, 0, DALQuery.WHERE_HAS_VALUE, htSearchCriteria);
                dalChildQuery.setWhereMultiField("AND", "AND", 0, "=", 0, DALQuery.WHERE_HAS_VALUE, htKeyCriteria);
                dalChildQuery.setWhere("AND", 0, "BIOSPECIMEN_intParentID", "=", strInternalBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
                
                if (dalTransQuery != null) {
                    dalChildQuery.setWhere("AND", 0, "BIOSPECIMEN_intBiospecimenID", "IN", dalTransQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY);
                }
                
                dalChildQuery.setCaseSensitive(false);
                rs = dalChildQuery.executeSelect();
                
                rs.beforeFirst();
                Hashtable htChildrenCheck = new Hashtable();
                
                
                while (rs.next()) {
                    Hashtable htResult = QueryChannel.convertCurrentRecordToHashtable(vtFormFields, rs);
                    
                    // TODO : fix this - back to ints!
                    htChildrenCheck.put((String) htResult.get("BIOSPECIMEN_strBiospecimenID"), htResult);
                    
                    if (htResult != null) {
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(htResult);
                        tempNode.add(newNode);
                    }
                    
                }
                
                establishSearchChildren(htChildrenCheck, htSearchCriteria, htKeyCriteria);
                
                
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else {
            
            ////System.out.println ("\n\n");
            ////System.out.println ("---- No Expansion! ------- SEARCH VALUES :");
            Enumeration enumHt = htSearchCriteria.keys();
            while (enumHt.hasMoreElements()) {
                String key = (String) enumHt.nextElement();
                String value = (String) htSearchCriteria.get(key);
                
            }
            
            
            // get the properties
            int intMaxSearchDepth = PropertiesManager.getPropertyAsInt("neuragenix.bio.Biospecimen.maxSearchDepth");
            
            
            
            if (!(intMaxSearchDepth > 0))
                return null;
            
            
            try {
                //    dsqQuery = new DALSecurityQuery(ACTION_BIOSPECIMEN_SEARCH, authToken);
                dsqQuery = new DALQuery();
                dsqQuery.setCaseSensitive(false);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            
            try {
                dsqQuery.setDomain("BIOSPECIMEN", null, null, null);
                if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA"))) {
                    dsqQuery.setDomain("PATIENT", "PATIENT_intInternalPatientID", "BIOSPECIMEN_intPatientID", "INNER JOIN");
                }
                //dsqQuery.setDomain("CELL", "CELL_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID", "LEFT JOIN");
                dsqQuery.setFields(vtFormFields, null);
                
                
                dsqQuery.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                
                // reset the patient key with the internal key
                
                String strEqualityCondition = getSearchEqualityCondition();
                
                
                dsqQuery.setWhereMultiField("AND", "AND", 0, strEqualityCondition, 0, DALQuery.WHERE_HAS_VALUE, htSearchCriteria);
                dsqQuery.setWhereMultiField("AND", "AND", 0, "=", 0, DALQuery.WHERE_HAS_VALUE, htKeyCriteria);
                
                // dsqQuery.setFetchSize(intRecordPerPage);
                if (blExcess == true) {
                    // dsqQuery.setWhere("AND", 0, "BIOSPECIMEN_flExtraNum1", ">", "BIOSPECIMEN_flExtraNum2", 0, DALQuery.WHERE_BOTH_FIELDS);
                }
                
                // set the initial depth value
                dsqQuery.setWhere("AND", 0, "BIOSPECIMEN_intDepth", "=", "1", 0, DALQuery.WHERE_HAS_VALUE);
                
                if (dalTransQuery != null) {
                    dsqQuery.setWhere("AND", 0, "BIOSPECIMEN_intBiospecimenID", "IN", dalTransQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY);
                }
                
                
                DALQuery dalPrevQuery = dsqQuery;
                DALQuery dalPrevCloneQuery = new DALQuery(dsqQuery);
                
                for (int i = 2; i <= intMaxSearchDepth; i++) {
                    DALQuery dalTempClone = null;
                    DALQuery dalDepthQuery = new DALQuery();
                    dalDepthQuery.setCaseSensitive(false);
                    dalDepthQuery.setDomain("BIOSPECIMEN", null, null, null);
                    if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA"))) {
                        dalDepthQuery.setDomain("PATIENT", "PATIENT_intInternalPatientID", "BIOSPECIMEN_intPatientID", "INNER JOIN");
                    }
                    dalDepthQuery.setFields(vtFormFields, null);
                    dalDepthQuery.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    dalDepthQuery.setWhereMultiField("AND", "AND", 0, strEqualityCondition, 0, DALQuery.WHERE_HAS_VALUE, htSearchCriteria);
                    dalDepthQuery.setWhereMultiField("AND", "AND", 0, "=", 0, DALQuery.WHERE_HAS_VALUE, htKeyCriteria);
                    dalDepthQuery.setWhere("AND", 0, "BIOSPECIMEN_intDepth", "=", i + "", 0, DALQuery.WHERE_HAS_VALUE);
                    if (dalTransQuery != null) {
                        dalDepthQuery.setWhere("AND", 0, "BIOSPECIMEN_intBiospecimenID", "IN", dalTransQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY);
                    }
                    
                    dalTempClone = new DALQuery(dalDepthQuery);
                    
                    dalPrevCloneQuery.clearFields();
                    dalPrevCloneQuery.setField("BIOSPECIMEN_intBiospecimenID", null);
                    dalDepthQuery.setWhere("AND", 0, "BIOSPECIMEN_intParentID", "NOT IN", dalPrevCloneQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY);
                    dalPrevQuery.setSiblingQuery("UNION", dalDepthQuery);
                    
                    dalPrevQuery = dalDepthQuery;
                    
                    dalPrevCloneQuery = dalTempClone;
                    
                }
                
                ////System.out.println ("int amount : " + intAmount);
                ////System.out.println ("intPageNumber : " + intPageNumber);
                
                
                // paging here is safe, because we're only looking at top level search results.
                // although if issues arise, page from tree.
                
                
                
                dsqQuery.setLimitOffset(intAmount, intPageNumber * intAmount);
                if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA"))) {
                    dsqQuery.setOrderByFunction("cast(trim( trailing '-' from substring (\"BIOSPECIMENID\" || '-' from 1 for position('-' in \"BIOSPECIMENID\" || '-'))) as int)", "ASC");
                }
                //dsqQuery.setOrderBy("BIOSPECIMEN_strBiospecimenID", "ASC", true);
                //LogService.instance().log(LogService.INFO, dsqQuery.convertSelectQueryToString());
                
                
                // neuro customisation
                dsqQuery.setCaseSensitive(false);
                // -- DM
                
                rs = dsqQuery.executeSelect();
                
                
                // put the data, within the paging set into a tree
                if (treeSearchResults != null || blNewSearch == true) {
                    
                    treeSearchResults = null;
                    htSearchResultsIndex = new Hashtable(100);
                    treeSearchResults = new DefaultMutableTreeNode();
                }
                
                
                
                Hashtable htChildrenCheck = new Hashtable();
                
                
                
                while (rs.next()) {
                    
                    Hashtable htResult = QueryChannel.convertCurrentRecordToHashtable(vtFormFields, rs);
                    
                    if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA"))) {
                        htResult = neuragenix.bio.utilities.CCIAUtilities.convertToAusDateFormat(htResult);
                    }
                    
                    // TODO : fix this - back to ints!
                    htChildrenCheck.put((String) htResult.get("BIOSPECIMEN_strBiospecimenID"), htResult);
                    if (htResult != null) {
                        // check doesnt already exist in the tree
                        
                        String strGetID = "";
                        if (htResult != null) {
                            strGetID = (String) htResult.get("BIOSPECIMEN_intBiospecimenID");
                            //System.out.println ("strGetID : " + strGetID);
                        } else {
                            ////System.out.println ("WHOOOOA SEARCH RESULT IS NULL!!");
                        }
                        Object tmpObject = htSearchResultsIndex.get(strGetID);
                        
                        
                        if (tmpObject == null) {
                            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(htResult);
                            treeSearchResults.add(newNode);
                        } else {
                            ////System.out.println ("Tried to add existing biospecimen : " + strGetID);
                        }
                        
                        
                    } else {
                        ////System.out.println("Got null from the query channel the for the hash of results.");
                    }
                }
                
                rs.close();
                
                dsqQuery.clearLimitOffset();
                
                //counting the number of result
                
                
                // neuro customisation - DM
                
                dsqQuery.setCaseSensitive(false);
                
                ResultSet rsCountResult = dsqQuery.executeSelect();
                
                rsCountResult.last();
                intCurrentSearchAmount = rsCountResult.getRow();
                rsCountResult.close();
                
                establishSearchChildren(htChildrenCheck, htSearchCriteria, htKeyCriteria);
                
                
                
                
                // the below will check for children for the query
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
        this.rebuildSearchIndex();
        
        ////System.out.println ("point 2");
        
        Enumeration enumTreeStructure = treeSearchResults.preorderEnumeration();
        
        
        try {
            
            int intOutputCounter = 0;
            int intMaxOutput = intAmount * (intPageNumber + 1);
            int intBeginOutput = intAmount * (intPageNumber) ;
            
            if (intPageNumber > 0)
                //intBeginOutput++;
                intOutputCounter = intBeginOutput;
            
            //System.out.println ("intmaxoutput : " + intMaxOutput);
            //System.out.println ("intbeginoutput : " + intBeginOutput);
            //System.out.println ("intpagenumber : " + intPageNumber);
            //System.out.println ("intamount : " + intAmount);
            
            int totCnt = 0;
            
            while (enumTreeStructure.hasMoreElements()) {
                totCnt++;
                
                
                DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode) enumTreeStructure.nextElement();
                if (tempNode.getLevel() == 1) {
                    // top level node, increment count
                    intOutputCounter++;
                    ////System.out.println ("output counter inc to " + intOutputCounter);
                }
                
                if ((intBeginOutput <= intOutputCounter) && (intMaxOutput >= intOutputCounter)) {
                    
                    
                    
                    if (tempNode.isRoot() == false) {
                        
                        Hashtable htValues = (Hashtable) tempNode.getUserObject();
                        Enumeration enumValues = htValues.keys();
                        boolean blOpenNode = false;
                        if (tempNode.getChildCount() > 0) {
                            blOpenNode = true;
                        }
                        
                        strXML.append("<node level=\"" + tempNode.getLevel() + "\" openNode=\"" + blOpenNode + "\" internalIndex=\"" + vtSearchResultsIndex.indexOf(tempNode) + "\">");
                        
                        
                        
                        
                        while (enumValues.hasMoreElements()) {
                            String strKey = (String) enumValues.nextElement();
                            String strValue = (String) htValues.get(strKey);
                            strXML.append("<" + strKey + ">");
                            strXML.append(strValue);
                            strXML.append("</" + strKey + ">");
                            //if ( strKey.equalsIgnoreCase("BIOSPECIMEN_intBiospecimenID")){
                            //   System.out.println("ID :"+ strValue);
                            //}
                        }
                        
                        
                        strXML.append("</node>");
                    }
                    
                    
                    
                }
                
                
            }
            
            // append the paging details
            
            // TODO: discover a way under the new dal to get the total number of pages.
            
            int intTotalPages = intCurrentSearchAmount / intAmount;
            //This to avoid the current page overpass the the total pages
            if ( intPageNumber > intTotalPages) intPageNumber = intTotalPages;
            
            strXML.append("<intMaxPage>" + intTotalPages + "</intMaxPage>");
            strXML.append("<intRecordPerPage>" + intAmount + "</intRecordPerPage>");
            strXML.append("<intCurrentPage>" + intPageNumber + "</intCurrentPage>");
            strXML.append( "<noOfResults>" + intCurrentSearchAmount + "</noOfResults>" );
            
            // append the source domain details
            
            switch (intCallingDomain) {
                case DOMAIN_PATIENT:
                    strXML.append("<callingDomain>patient</callingDomain>");
                    strXML.append(PatientUtilities.getPatientDetailsXML((String)htKeyCriteria.get("BIOSPECIMEN_intPatientID"), authToken));
                    break;
                case DOMAIN_STUDYALLOC_ALLOC:
                    strXML.append("<callingDomain>study</callingDomain>");
                    break;
                    
                case DOMAIN_STUDYALLOC_DELIV:
                    strXML.append("<callingDomain>study</callingDomain>");
                    break;
            }
            
            ////System.out.println ("tot : " + totCnt);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        // save the resultSet and the DAL
        
        
        ////System.out.println("point 4");
        
        // output the tree
        
        // //System.out.println("the new xml : " + strXML.toString());
        
        
        
        
        
        
        
        return strXML.toString();
        
    }
    
    
    
    /**
     *
     * establishSearchChildren -- updates search tree with biospecimen children based on criteria set
     *
     * Warning - This method operates by reference, and WILL update the children hashtable
     *
     * @param htChildrenCheck - Hashtable of <biospecimen key>, <hashtable that has been used as the user object in tree>
     * @param htSearchCriteria - Hashtable of <search field>, <criteria>.  Equality based on system properties
     * @param htKeyCriteria - Hashtable the same as search criteria, however as it has keys, equality rules are "=" only
     *
     */
    
    private void establishSearchChildren(Hashtable htChildrenCheck, Hashtable htSearchCriteria, Hashtable htKeyCriteria) {
        DALQuery dalChildrenCheck = new DALQuery();
        dalChildrenCheck.setCaseSensitive(false);
        String strEqualityCondition = getSearchEqualityCondition();
        
        try {
            dalChildrenCheck.setDomain("BIOSPECIMEN", null, null, null);
            dalChildrenCheck.setCountField("BIOSPECIMEN_strParentID", false);
            dalChildrenCheck.setField("BIOSPECIMEN_strParentID", null);
            dalChildrenCheck.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            
            int intOpenBrackets = 0;
            int intCloseBrackets = 0;
            String strConnector = "AND";
            
            Enumeration enumChildrenCheck = htChildrenCheck.elements();
            
            int intIterationCount = 0;
            int intTotalValues = htChildrenCheck.size();
            
            
            while (enumChildrenCheck.hasMoreElements()) {
                Hashtable htHandle = (Hashtable) enumChildrenCheck.nextElement();
                
                String strInternalBiospecimenID = (String) htHandle.get("BIOSPECIMEN_intBiospecimenID");
                ////System.out.println ("Doing Id : " + strInternalBiospecimenID);
                
                if (htChildrenCheck.size() == 1) {
                    strConnector = "AND";
                    intOpenBrackets = 0;
                    intCloseBrackets = 0;
                } else {
                    if (intIterationCount == 0) {
                        strConnector = "AND";
                        intOpenBrackets = 1;
                        intCloseBrackets = 0;
                    } else if (intIterationCount == (intTotalValues - 1)) {
                        strConnector = "OR";
                        intOpenBrackets = 0;
                        intCloseBrackets = 1;
                    } else {
                        strConnector = "OR";
                        intOpenBrackets = 0;
                        intCloseBrackets = 0;
                    }
                }
                intIterationCount++;
                
                dalChildrenCheck.setWhere(strConnector, intOpenBrackets, "BIOSPECIMEN_intParentID", "=", strInternalBiospecimenID, intCloseBrackets, DALQuery.WHERE_HAS_VALUE);
                
                
            }
            dalChildrenCheck.setWhereMultiField("AND", "AND", 0, strEqualityCondition, 0, DALQuery.WHERE_HAS_VALUE, htSearchCriteria);
            dalChildrenCheck.setWhereMultiField("AND", "AND", 0, "=", 0, DALQuery.WHERE_HAS_VALUE, htKeyCriteria);
            dalChildrenCheck.setGroupBy("BIOSPECIMEN_strParentID");
            
            ////System.out.println ("query for children : " + dalChildrenCheck.convertSelectQueryToString());
            dalChildrenCheck.setCaseSensitive(false);
            ResultSet rsChildSet = dalChildrenCheck.executeSelect();
            
            rsChildSet.beforeFirst();
            
            while (rsChildSet.next()) {
                ////System.out.println ("ChildSet It!");
                
                String strParentKey = rsChildSet.getString("BIOSPECIMEN_strParentID");
                int intCount = rsChildSet.getInt("COUNT_BSPC_strParentID");
                
                // //System.out.println ("Parent Key : " + strParentKey + ", int count " + intCount );
                
                if (intCount > 0) {
                    ////System.out.println("Finders Keepers");
                    Hashtable htSpecimenHandle = (Hashtable) htChildrenCheck.get(strParentKey);
                    
                    if (htSpecimenHandle == null)
                        System.out.println ("Specimen handler is null");
                    else
                        htSpecimenHandle.put("HASCHILDREN", "true");
                }
            }
            
            rsChildSet.close();
        } catch (Exception e) {
            System.err.println("[BiospecimenCore] : Blocking error when reading from database");
            e.printStackTrace();
            
        }
    }
    
    
    
    
    
    public String getSearchEqualityCondition() {
        String strEqualityCondition = "";
        boolean blSearchCaseSensitive = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.searchCaseSensitive");
        boolean blSearchLeftLike = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.searchLeftLike");
        boolean blSearchRightLike = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.Biospecimen.searchRightLike");
        
        if (blSearchLeftLike == false && blSearchRightLike == false) {
            strEqualityCondition = "=";
        } else if (blSearchLeftLike == true && blSearchRightLike == false) {
            strEqualityCondition = "LEFT LIKE";
        } else if (blSearchLeftLike == false && blSearchRightLike == true) {
            strEqualityCondition = "RIGHT LIKE";
        } else if (blSearchLeftLike == true && blSearchRightLike == true) {
            strEqualityCondition = "LIKE";
        }
        
        return strEqualityCondition;
    }
    
    public String getAddBiospecimenXML(int intPatientKey, int intBiospecimenParentKey)
    
    {
        String strStylesheet = ACTION_BIOSPECIMEN_ADD; // set the default to addbiospecimen
        StringBuffer strXML = new StringBuffer();
        Vector vtAddBiospecimenFields = DatabaseSchema.getFormFields("cbiospecimen_biospecimen_add");
        
        // Build the default BiospecimenID consisting of the year and hospital site (e.g. 04MH)
        // This is to be updated by the user with the id number
        
        // Define the Timezone
        java.util.TimeZone tz = java.util.TimeZone.getTimeZone(SYSTEM_TIMEZONE);
        // Define the locality information
        java.util.Locale loc = new java.util.Locale(SYSTEM_LANGUAGE, SYSTEM_COUNTRY);
        // Setup the calender
        java.util.Calendar theCalendar = java.util.Calendar.getInstance(tz,loc);
        // get the year
        String Year = new String(Integer.toString(theCalendar.get(java.util.Calendar.YEAR)).substring(2));
        String strBioIDDefault = Year + HOSPITAL_SITE;
        
        
        
        strXML.append(QueryChannel.buildAddFormXMLFile(vtAddBiospecimenFields));
        strXML.append("<PATIENT_intPatientKey>" + intPatientKey + "</PATIENT_intPatientKey>");
        
        
        
        
        
        // why does it need the cell id?
        // obj_base_channel.setXMLFile("<intInvCellID>" + runtimeData.getParameter("intInvCellID") + "</intInvCellID>");
        
        
        // This means only get the studies who's end date is after today!
        
        // need to output the list of studies.  This is probably also only the studies that this patient has consented to.
        
        Vector vtEncounterField = DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_encounter");
        
        // Get the the list of encounters linked to the patient
        DALSecurityQuery encounterQuery = new DALSecurityQuery();
        ResultSet rsBiospecimenEncounterList = null;
        
        //only search for patient if the key is valid
        if (intPatientKey > 0){
            try {
                encounterQuery.setDomain("ADMISSIONS", null, null, null);
                encounterQuery.setFields(vtEncounterField, null);
                encounterQuery.setWhere(null, 0, "ADMISSIONS_intPatientID", "=", intPatientKey + "", 0, DALQuery.WHERE_HAS_VALUE);
                encounterQuery.setWhere("AND", 0, "ADMISSIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                encounterQuery.setOrderBy("ADMISSIONS_strAdmissionID", "ASC");
                rsBiospecimenEncounterList = encounterQuery.executeSelect();
                
                if (rsBiospecimenEncounterList != null) {
                    strXML.append(QueryChannel.buildSearchXMLFile("search_encounter_list", rsBiospecimenEncounterList, vtEncounterField));
                }
                rsBiospecimenEncounterList.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ////System.out.println("End");
        
        
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?><biospecimen>" + "<strBiospecimenID>" + strBioIDDefault + "</strBiospecimenID>" + strXML.toString() + "</biospecimen>";
        
        
    }
    
    public String getTransactionDetailsXML(int intBiospecimenKey) {
        StringBuffer strXML = new StringBuffer();
        
        // Get the required form fields
        Vector vtAddTransaction = DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_transaction");
        Vector vtSearchPersonDir = DatabaseSchema.getFormFields("ccoredomains_search_up_person_dir");
        Vector vtSearchBioTransactions = DatabaseSchema.getFormFields("cbiospecimen_search_biospecimen_transaction");
        Vector vtUpdateBioQuantity = DatabaseSchema.getFormFields("cbiospecimen_update_biospecimen_quantity");
        
        strXML.append(QueryChannel.buildFormLabelXMLFile(vtAddTransaction) + QueryChannel.buildAddFormXMLFile(vtAddTransaction));
        strXML.append("<BIOSPECIMEN_TRANSACTIONS_strRecordedBy>" + authToken.getUserIdentifier() + "</BIOSPECIMEN_TRANSACTIONS_strRecordedBy>");
        
        if(blAlwaysDispalyUnits) {
            strXML.append("<BIOSPECIMEN_TRANSACTIONS_alwaysDispalyUnits>1</BIOSPECIMEN_TRANSACTIONS_alwaysDispalyUnits>");
        } else {
            strXML.append("<BIOSPECIMEN_TRANSACTIONS_alwaysDispalyUnits>0</BIOSPECIMEN_TRANSACTIONS_alwaysDispalyUnits>");
        }
        
        DALSecurityQuery query = null;
        ResultSet rs = null;
        try {
            query = new DALSecurityQuery(ACTION_BIOSPECIMEN_VIEW, authToken);
            query.setDomain("USERDETAILS", null, null, null);
            query.setFields(vtSearchPersonDir, null);
            query.setOrderBy("USERDETAILS_strUserName", "ASC");
            rs = query.executeSelect();
            strXML.append(QueryChannel.buildSearchXMLFile("search_user", rs, vtSearchPersonDir));
            rs.close();
            
            
            query.reset();
            query.setDomain("BIOSPECIMEN_TRANSACTIONS", null, null, null);
            query.setFields(vtSearchBioTransactions, null);
            query.setWhere(null, 0, "BIOSPECIMEN_TRANSACTIONS_intBiospecimenID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "BIOSPECIMEN_TRANSACTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            
            rs = query.executeSelect();
            strXML.append(QueryChannel.buildSearchXMLFile("search_trans", rs, vtSearchBioTransactions));
            rs.close();
            
            
            query.reset();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setFields(vtUpdateBioQuantity, null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            
            
            if (rsResult.first() == true) {
                strXML.append("<BIOSPECIMEN_flNumberCollected>" + String.valueOf( rsResult.getDouble(1) + rsResult.getDouble(2) ) + "</BIOSPECIMEN_flNumberCollected>");
            }
            
            rsResult.close();
            
            // To load the Study dropdown in Quantity
            strXML.append(StudyUtilities.getListOfCurrentStudiesXML());
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return strXML.toString();
        
    }
    
    // add/remove quantity
    public String doSaveQuantity(String strBiospecimenID, ChannelRuntimeData runtimeData) {
        String strErrorMessage = null;
        try {
            String strInvalidUnitError = "Please ensure that a valid units is used.";
            
            if (!authToken.hasActivity(PERMISSION_ADD_QUANTITY)) {
                strErrorMessage = "You do not have permission to add a biospecimen quantity";
                return strErrorMessage;
            } else {
                // try and get the property which defines if only vials are being used
                boolean blValidUnit = true;
                boolean blUsingVialsOnly = false;
                //moved up to become static
                //String requiredQuantity = null;
                
                //try {
                //movved to static
                //requiredQuantity = PropertiesManager.getProperty("neuragenix.bio.biotransactions.requiredQuantity");
                
                // Get the unit being used
                String strUnit = runtimeData.getParameter("BIOSPECIMEN_TRANSACTIONS_strUnit");
                
                if ((requiredQuantity == null) || (requiredQuantity.length()==0)) {
                    // The client does require a quantity
                    blValidUnit = true;
                }
                if ((requiredQuantity!=null) && (strUnit!=null) && !(strUnit.equalsIgnoreCase(requiredQuantity))) {
                    // Client requires a quantity && it does not matches the current selection
                    blValidUnit = false;
                }
                //}
                //catch (Exception e) {
                //System.err.println("[BiospecimenCore] : property not set (neuragenix.bio.biotransactions.requiredQuantity)=> no restrictions of unit used");
                //e.printStackTrace();
                //}
                
                
                Vector vtAddQuantity = DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_transaction");
                QueryChannel.updateDateValuesInRuntimeData(vtAddQuantity, runtimeData);
                
                // check for required fields
                String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddQuantity, runtimeData);
                
                // TO DO change this to specific unit, dont hard code this like this
                if (blValidUnit) {
                    
                    if (strCheckRequiredFields == null) {
                        // check for data validation
                        String strValidation = QueryChannel.validateData(vtAddQuantity, runtimeData);
                        String strStatus = runtimeData.getParameter("BIOSPECIMEN_TRANSACTIONS_strStatus");
                        
                        if (strValidation == null) {
                            double dbAddedQuantity = Double.parseDouble(runtimeData.getParameter("BIOSPECIMEN_TRANSACTIONS_flQuantity"));
                            
                            // If the status is set to delivered and the quantity entered is greater than zero
                            // multiply by -1 so that the entered quantity is subtracted from the current
                            // available quantity
                            if ( runtimeData.getParameter("BIOSPECIMEN_TRANSACTIONS_strStatus") != null && strStatus.equalsIgnoreCase(TRANSACTION_STATUS_DELIVERED)) {
                                if (dbAddedQuantity > 0) {
                                    dbAddedQuantity *= -1;
                                    runtimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_flQuantity", Double.toString(dbAddedQuantity));
                                }
                            }
                            
                            // If the status is set to allocated and the quantity entered is greater than zero
                            // multiply by -1 so that the entered quantity is subtracted from the current
                            // available quantity
                            if ( runtimeData.getParameter("BIOSPECIMEN_TRANSACTIONS_strStatus") != null && strStatus.equalsIgnoreCase(TRANSACTION_STATUS_ALLOCATED)) {
                                if (dbAddedQuantity > 0) {
                                    dbAddedQuantity *= -1;
                                    runtimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_flQuantity", Double.toString(dbAddedQuantity));
                                }
                            }
                            
                            
                            // calculate the new quantity
                            Vector vtUpdateQuantity = DatabaseSchema.getFormFields("cbiospecimen_update_biospecimen_quantity");
                            DALSecurityQuery query = new DALSecurityQuery(ACTION_BIOSPECIMEN_VIEW, authToken);
                            query.setDomain("BIOSPECIMEN", null, null, null);
                            query.setFields(vtUpdateQuantity, null);
                            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
                            ResultSet rsResult = query.executeSelect();
                            rsResult.next();
                            double oldCollected = rsResult.getDouble(1);
                            double oldRemoved = rsResult.getDouble(2);
                            rsResult.close();
                            
                            double dbNewQuantity = oldCollected + dbAddedQuantity + oldRemoved;
                            
                            
                            
                            //if (lrBiospecLock.lockWrites()) {
                            // only save this transaction if the new quantity is positive
                            if (dbNewQuantity >= 0) {
                                // only set the strUnit to the previous ones only if you are not always displaying units
                                if (!blAlwaysDispalyUnits) {
                                    query.reset();
                                    query.setDomain("BIOSPECIMEN_TRANSACTIONS", null, null, null);
                                    query.setFields(vtAddQuantity, null);
                                    query.setWhere(null, 0, "BIOSPECIMEN_TRANSACTIONS_intBiospecimenID", "=", strBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
                                    rsResult = query.executeSelect();
                                    
                                    if (rsResult.next()) {
                                        runtimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_strUnit", rsResult.getString("BIOSPECIMEN_TRANSACTIONS_strUnit"));
                                    }
                                    rsResult.close();
                                }
                                
                                
                                //insert to the BIOSPECIMEN
                                query.reset();
                                query.setDomain("BIOSPECIMEN_TRANSACTIONS", null, null, null);
                                
                                // if status is Delivery, save the delivery date as today
                                if ( runtimeData.getParameter("BIOSPECIMEN_TRANSACTIONS_strStatus") != null &&
                                        strStatus.equalsIgnoreCase(TRANSACTION_STATUS_DELIVERED)) {
                                    SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
                                    runtimeData.setParameter("BIOSPECIMEN_TRANSACTIONS_dtDeliveryDate", fm.format(new java.util.Date()));
                                }
                                query.setFields(vtAddQuantity, runtimeData);
                                query.executeInsert();
                                
                                query.reset();
                                query.setDomain("BIOSPECIMEN", null, null, null);
                                //query.setField("BIOSPECIMEN_flExtraNum1", String.valueOf(dbNewQuantity));
                                // Long fixing removed and collected
                                
                                if( dbAddedQuantity > 0 ) {
                                    
                                    query.setField( "BIOSPECIMEN_flNumberCollected" , String.valueOf( oldCollected + dbAddedQuantity ) );
                                    
                                }else {
                                    
                                    query.setField( "BIOSPECIMEN_flNumberRemoved" , String.valueOf( oldRemoved + dbAddedQuantity ) );
                                    
                                }
                                
                                if ((CLIENT != null) && (CLIENT.equalsIgnoreCase("CCIA"))) {
                                    query.setField( "BIOSPECIMEN_flNumberRemaining" , String.valueOf( dbNewQuantity ) );
                                }
                                
                                query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
                                query.executeUpdate();
                                
                                
                                // determine if the delete needs to be cascaded to the parent level
                                //moved up to become static
                                //boolean blcascadeUpdate = false;
                                //try {
                                //blcascadeUpdate = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.transaction.cascadedTransactions");
                                //}
                                //catch (Exception e) {
                                //System.err.println("[BiospecimenCore] : Unable to retreive property - neuragenix.bio.transaction.cascadedTransactions");
                                //e.printStackTrace();
                                //}
                                
                                if (blcascadeUpdate) {
                                    TransactionManager tm = new TransactionManager(Integer.parseInt(strBiospecimenID), authToken, true, dbAddedQuantity, oldCollected,  oldRemoved);
                                    tm.cascadedUpdateTransactions();
                                    tm = null;
                                }
                                
                                //lrBiospecLock.unlockWrites();
                            } else {
                                strErrorMessage = "Please correct the quantity as it is greater than what is currently available.";
                            }
                            // }
                            //else {
                            //  strLockError = "Insert failed since the record is being viewed by other users.";
                            // }
                        } else {
                            strErrorMessage = strValidation;
                        }
                    } else {
                        strErrorMessage = strCheckRequiredFields;
                    }
                } else {
                    strErrorMessage = strInvalidUnitError;
                }
            } // end of check for authorisation to addd quantity
        } catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
        }
        
        return strErrorMessage;
    }
    
    public boolean getIsRefresh() {
        return this.isRefresh;
    }
    
    public void setIsRefresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }
    //This function is posted from Old CBiospecimen.java of previous version
    private String calculateDetailedBioAge(String intBiospecimenID) {
        String age = null;
        String sampleDate = null;
        String intPatientID = null;
        try {
            DALSecurityQuery query = new DALSecurityQuery(ACTION_BIOSPECIMEN_VIEW, authToken);
            query.reset();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_dtExtractedDate", null);
            query.setField("BIOSPECIMEN_intPatientID", null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsage = query.executeSelect();
            if(rsage.next()) {
                sampleDate = rsage.getString("BIOSPECIMEN_dtExtractedDate");
                if (sampleDate != null) {
                    intPatientID = rsage.getString("BIOSPECIMEN_intPatientID");
                    query.reset();
                    query.setDomain("PATIENT",null,null,null);
                    query.setField("PATIENT_dtDob", null);
                    query.setWhere(null, 0, "PATIENT_intInternalPatientID", "=", intPatientID, 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rsDob = query.executeSelect();
                    if(rsDob.next()) {
                        String Dob = rsDob.getString("PATIENT_dtDob");
                        Date dtSample = Utilities.convertStringToDate(sampleDate,"yyyy-MM-dd");
                        Date dtDob = Utilities.convertStringToDate(Dob,"yyyy-MM-dd");
                        Calendar calSample = Calendar.getInstance();
                        Calendar calDob = Calendar.getInstance();
                        
                        calSample.setTime(dtSample);
                        calDob.setTime(dtDob);
                        
                        ElapsedTime elapsedTime = new ElapsedTime();
/*                        int monthsElapsed = elapsedTime.getMonths((GregorianCalendar)calSample, (GregorianCalendar)calDob);
                        int monthsOut = 0;
                        int daysOut = 0;
                        int yearsOut = 0;
 
                        if (monthsElapsed > 0) {
                            if (monthsElapsed >= 12) {
                                yearsOut = monthsElapsed / 12;
                                monthsOut = monthsElapsed - (yearsOut * 12);
                            }
                            else {
                                monthsOut = monthsElapsed;
                            }
                        }
                        daysOut = elapsedTime.getDayDifference();
 */
                        Vector vtAge = elapsedTime.getDateDifference(dtDob,dtSample);
                        if(vtAge != null && vtAge.size() > 0) {
                            age = vtAge.get(0) + "yrs " + vtAge.get(1) + "months " + vtAge.get(2) + "days ";
                        } else if(vtAge != null && vtAge.size() == 0) {
                            age = "The DOB greater than the extraction date";
                        }
//                        age = yearsOut + " years, " + monthsOut + " months, " + daysOut + " days";
                        
                    }
                } else
                    
                    
                {
                    age = "No date of extraction";
                }
            }
            return age;
        } catch(Exception e) {
            e.printStackTrace();
            return age;
        }
    }
}
