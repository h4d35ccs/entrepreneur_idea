package es.upm.emse.enteridea.persistence.exception;
/**
 * Exception that indicate an error while performing an Database Operation
 * @author ottoabreu
 *
 */
public class DaoOperationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * public static final String CREATE_ERROR = "Can not save the object into the database due an error: ";
	 */
	public static final String CREATE_ERROR = "Can not SAVE the object into the database due an error: ";
	/**
	 * UPDATE_ERROR = "Can not UPDATE the object into the database due an error: ";
	 */
	public static final String UPDATE_ERROR = "Can not UPDATE the object into the database due an error: ";
	/**
	 *  DELETE_ERROR = "Can not DELETE the object into the database due an error: ";
	 */
	public static final String DELETE_ERROR = "Can not DELETE the object into the database due an error: ";
	
	/**
	 * CONNECTION_ERROR = "Can not open a conection or transaction due an error: ";
	 */
	public static final String CONNECTION_ERROR = "Can not open a conection or transaction due an error: ";
	
	/**
	 * READ_ERROR = "Can not get the result from the DB due an error: ";
	 */
	public static final String READ_ERROR = "Can not get the result from the DB due an error: ";
	/**
	 * READ_TOTAL_RECORDS_ERROR = "Can not get the number total records from the DB due an error: ";
	 */
	public static final String READ_TOTAL_RECORDS_ERROR = "Can not get the number total records from the DB due an error: ";
	
	
	

	public DaoOperationException(String string) {
		// TODO Auto-generated constructor stub
	}

	

	public DaoOperationException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
