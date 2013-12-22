package es.upm.emse.enteridea.business.exception;

public class BusinessException extends Exception {

	private static final long serialVersionUID = 1L;
	public static final String CREATE_IDEA_ERROR = "Failed creating idea";
	public static final String RETRIEVE_IDEA_ERROR = "Failed retrieving idea";
	public static final String UPDATE_IDEA_ERROR = "Failed updating idea";
	public static final String CREATE_TOPIC_ERROR = "Failed creating topic";
	public static final String DELETE_TOPIC_ERROR = "Failed deleting topic";
	public static final String RETRIEVE_TOPIC_ERROR = "Failed retrieving topic";
	public static final String CREATE_USER_ERROR = "Failed creating user";
	public static final String RETRIEVE_USER_ERROR = "Failed retrieving user";
	public static final String DELETE_USER_ERROR = "Failed deleting user";
	public static final String ID_NOT_NUMBER = "The Id is not a number";
	public static final String CREATE_COMMENT_ERROR = "Failed creating comment";
	public static final String RETRIEVE_COMMENT_ERROR = "Failed retrieving comments";
	public static final String EDITING_PERMISION_ERROR = "The user that is trying to edit is not the owner of the object or is not a moderator";
	public static final String DELETE_IDEA_ERROR = "Failed deleting idea";
	public static final String DELETE_COMMENT_ERROR = "Failed deleting comment";

	public BusinessException(String msg, Throwable e) {
		super(msg, e);
	}

	public BusinessException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
