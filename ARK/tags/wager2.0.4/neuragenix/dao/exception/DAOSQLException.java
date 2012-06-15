/*

 * DAOException.java

 *

 * Created on October 28, 2002, 2:51 PM

 */



package neuragenix.dao.exception;



/**

 *

 * @author  Administrator

 */

public class DAOSQLException extends java.lang.Exception {

    

    /**

     * Creates a new instance of <code>DAOException</code> without detail message.

     */

    public DAOSQLException() {

    }

    

    

    /**

     * Constructs an instance of <code>DAOException</code> with the specified detail message.

     * @param msg the detail message.

     */

    public DAOSQLException(String msg) {

        super(msg);

    }

    /**

     * Constructs an instance of <code>DAOException</code> with the specified detail message.

     * @param msg the detail message.

     */

    public DAOSQLException(String msg, Exception e) {

        super(msg, e);

    }

}

