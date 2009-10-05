/*
 * IPatientIDGenerator.java
 *
 * Copyright (C) Neuragenix Pty Ltd, 2005
 *
 * Description : Interface for the generation of Patient IDs
 *
 */

package neuragenix.bio.utilities;

import java.util.Vector;
import neuragenix.security.AuthToken;

/**
 *
 * @author  Seena Parappat
 */
public interface IAdmissionIDGenerator {
    
    /**
     *  In cases where a prefix is displayed to the user, this should be returned here
     */
    
    public String getAdmissionIDPrefix();
    
    /**
     * Provides the Sequence Key and the data associated with the new biospecimen
     */
    
    public String getAdmissionID(int intSequenceKey,  int intStudyKey, int intSubStudyKey, AuthToken authToken);
    
    
    
}
