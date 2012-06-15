/*
 * IBiospecimenIDGenerator.java
 *
 * Copyright (C) Neuragenix Pty Ltd, 2005
 *
 * Description : Interface for the generation of Biospecimen IDs
 *
 */

package neuragenix.bio.utilities;

import java.util.Vector;
import neuragenix.security.AuthToken;
import neuragenix.dao.*;

/**
 *
 * @author  Daniel Murley
 */
public interface IBiospecimenIDGenerator {
    
    /**
     *  In cases where a prefix is displayed to the user, this should be returned here
     */
    
    public String getBiospecimenIDPrefix();
    
    /**
     * Provides the Sequence Key and the data associated with the new biospecimen
     */
    
    public String getBiospecimenID(int intSequenceKey, DALSecurityQuery query,  AuthToken authToken);
    

    public String getBiospecimenID(int intSequenceKey, DALQuery query,  AuthToken authToken);
    
    
}
