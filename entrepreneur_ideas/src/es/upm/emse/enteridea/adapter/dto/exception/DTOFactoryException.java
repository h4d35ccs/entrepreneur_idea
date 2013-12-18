package es.upm.emse.enteridea.adapter.dto.exception;


/**
 * Exception that indicate an error during the creation of a DTO
 * 
 * @author ottoabreu
 * 
 */
public class DTOFactoryException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * INSTANTIATE_DTO_ERROR ="Can not instantiate a new DTO due an error: ";
	 */
	public static final String INSTANTIATE_DTO_ERROR = "Can not instantiate a new DTO due an error: ";

	/**
	 * EXECUTE_SETMETHOD_SECURITY_ERROR
	 * ="Can not execute the set method of the DTO due an security error: ";
	 */
	public static final String EXECUTE_SETMETHOD_SECURITY_ERROR = "Can not execute the set method of the DTO due an security error: ";
	/**
	 * EXECUTE_SETMETHOD_NOMETHOD_ERROR =
	 * "Can not execute the set method because there is none that fits, check the DTO class: "
	 * ;
	 */
	public static final String EXECUTE_SETMETHOD_NOMETHOD_ERROR = "Can not execute the set method because there is none that fits, check the DTO class: ";
	/**
	 * EXECUTE_SETMETHOD_ILEGALACCES_ERROR =
	 * "Can not execute the set method of the DTO because the  get method is inaccessible"
	 * ;
	 */
	public static final String EXECUTE_SETMETHOD_ILEGALACCES_ERROR = "Can not execute the set method of the DTO because the  get method is inaccessible";
	/**
	 * EXECUTE_SETMETHOD_ILEGALARGUMENT_ERROR = "Can not execute the set method has received a specified object argument is not an instance of the class or interface declaring the underlying method ";
	 */
	public static final String EXECUTE_SETMETHOD_ILEGALARGUMENT_ERROR = "Can not execute the set method has received a specified object argument is not an instance of the class or interface declaring the underlying method ";
	/**
	 * EXECUTE_SETMETHOD_IVOTGT_ERROR = "Can not execute the set method of the DTO because it has thrown an exception during the invocation";
	 */
	public static final String EXECUTE_SETMETHOD_IVOTGT_ERROR = "Can not execute the set method of the DTO because it has thrown an exception during the invocation";

	/**
	 * EXECUTE_GETMETHOD_SECURITY_ERROR
	 * ="Can not execute the get method of the DTO due an security error: ";
	 */
	public static final String EXECUTE_GETMETHOD_SECURITY_ERROR = "Can not execute the get method of the DTO due an security error: ";
	/**
	 * EXECUTE_GETMETHOD_NOMETHOD_ERROR =
	 * "Can not execute the get method because there is none that fits, check the DTO class: "
	 * ;
	 */
	public static final String EXECUTE_GETMETHOD_NOMETHOD_ERROR = "Can not execute the get method because there is none that fits, check the DTO class: ";
	/**
	 * EXECUTE_GETMETHOD_ILEGALACCES_ERROR =
	 * "Can not execute the get method of the DTO because the  get method is inaccessible"
	 * ;
	 */
	public static final String EXECUTE_GETMETHOD_ILEGALACCES_ERROR = "Can not execute the get method of the DTO because the  get method is inaccessible";
	/**
	 * EXECUTE_GETMETHOD_ILEGALARGUMENT_ERROR = "Can not execute the get method has received a specified object argument is not an instance of the class or interface declaring the underlying method ";
	 */
	public static final String EXECUTE_GETMETHOD_ILEGALARGUMENT_ERROR = "Can not execute the get method has received a specified object argument is not an instance of the class or interface declaring the underlying method ";
	/**
	 * EXECUTE_GETMETHOD_IVOTGT_ERROR = "Can not execute the get method of the DTO because it has thrown an exception during the invocation";
	 */
	public static final String EXECUTE_GETMETHOD_IVOTGT_ERROR = "Can not execute the get method of the DTO because it has thrown an exception during the invocation";
	/**
	 * GENERAL_ERROR = "An general error occurs and can not continue to the creation of the DTO: ";
	 */
	public static final String GENERAL_ERROR = "An general error occurs and can not continue to the creation of the DTO: ";
	
	public DTOFactoryException() {
		// TODO Auto-generated constructor stub
	}

	public DTOFactoryException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public DTOFactoryException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public DTOFactoryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
