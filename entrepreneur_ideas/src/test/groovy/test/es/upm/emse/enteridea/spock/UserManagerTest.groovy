package test.es.upm.emse.enteridea.spock

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import es.upm.emse.enteridea.business.UserManager;
import es.upm.emse.enteridea.business.exception.BusinessException;
import es.upm.emse.enteridea.persistence.entity.User;
import spock.lang.Specification;

class UserManagerTest extends Specification {

	private UserManager userManager;
	private static final String TEST_USER_FIRST_NAME = "Joe";
	private static final String TEST_USER_LAST_NAME = "Tester";
	private static final String TEST_USER_USER_NAME = "___testy___"+new Date().getTime();
	private static final String TEST_USER_PASSWORD = "testicles123";
	private static final String TEST_USER_EMAIL = "joe.tester@usertest.com";

	private static final String TEST_USER_USER_NAME_CREATE = TEST_USER_USER_NAME + "CREATE_";
	private static final String TEST_USER_USER_NAME_LIST = TEST_USER_USER_NAME + "LIST_";
	private static final String TEST_USER_USER_NAME_DELETE = TEST_USER_USER_NAME + "DELETE_";

	def setup(){
		userManager = new UserManager();
	}

	def cleanup(){
		userManager = null;
	}

	def "Create test user and verify its creation in the database"(){
		when:"a user is created in the DB"
		userManager.createUser(
				TEST_USER_FIRST_NAME,
				TEST_USER_LAST_NAME,
				TEST_USER_USER_NAME_CREATE,
				TEST_USER_PASSWORD,
				TEST_USER_EMAIL);
		and:"the created user is retreived"
		User user = userManager.getUserByUserName(TEST_USER_USER_NAME_CREATE);

		then:" the user must exist"
		user != null &&
				TEST_USER_FIRST_NAME.equalsIgnoreCase(user.getFirstName()) &&
				TEST_USER_LAST_NAME.equalsIgnoreCase(user.getLastName()) &&
				TEST_USER_USER_NAME_CREATE.equalsIgnoreCase(user.getNickname())&&
				TEST_USER_PASSWORD.equalsIgnoreCase(user.getPassword())&&
				TEST_USER_EMAIL.equalsIgnoreCase(user.getEmail());

		cleanup:"delete the test user"
		userManager.deleteUser(TEST_USER_USER_NAME_CREATE);
	}

	def"Create test user, read list of users from the database"(){

		setup:"at least une user must exist"
		userManager.createUser(
				TEST_USER_FIRST_NAME,
				TEST_USER_LAST_NAME,
				TEST_USER_USER_NAME_LIST,
				TEST_USER_PASSWORD,
				TEST_USER_EMAIL);
		when:"the list of user is solicitated"
		List<User> userList = userManager.listUsers();
		then:"the list must not be empty"
		!userList.isEmpty();
		cleanup:"delete test user"
		userManager.deleteUser(TEST_USER_USER_NAME_LIST);
	}

	def "Create and delete test user and read list of users from the database"(){
		setup:"at least une user must exist"
		userManager.createUser(
				TEST_USER_FIRST_NAME,
				TEST_USER_LAST_NAME,
				TEST_USER_USER_NAME_DELETE,
				TEST_USER_PASSWORD,
				TEST_USER_EMAIL);
	
		when:"the user is deleted"
		userManager.deleteUser(TEST_USER_USER_NAME_DELETE);
		User user = userManager.getUserByUserName(TEST_USER_USER_NAME_DELETE);
		then:"the user should not exist"
		thrown(BusinessException);
	}
}
