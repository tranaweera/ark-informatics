/*

 * DAOQueryInvalidDomain.java

 *

 * Created on October 28, 2002, 3:29 PM

 */



package neuragenix.dao.exception;



/**

 *

 * @author  Administrator

 */

public class DAOQueryInvalidFieldValue extends DAOException{

    

    /**

     * Creates a new instance of <code>DAOQueryInvalidDomain</code> without detail message.

     */

    public DAOQueryInvalidFieldValue() {

    }

    

    

    /**

     * Constructs an instance of <code>DAOQueryInvalidDomain</code> with the specified detail message.

     * @param msg the detail message.

     */

    public DAOQueryInvalidFieldValue(String msg) {

        super(msg);

    }

}

