package es.upm.emse.enteridea.business;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.upm.emse.enteridea.business.UserManager;
import es.upm.emse.enteridea.business.exception.BusinessException;
import es.upm.emse.enteridea.persistence.entity.User;

public class UserManagerTest {

	private UserManager userManager;
	private static final String TEST_USER_FIRST_NAME = "Joe";
	private static final String TEST_USER_LAST_NAME = "Tester";
	private static final String TEST_USER_USER_NAME = "___testy___"+new Date().getTime();
	private static final String TEST_USER_PASSWORD = "testicles123";
	private static final String TEST_USER_EMAIL = "joe.tester@usertest.com";
	
	private static final String TEST_USER_USER_NAME_CREATE = TEST_USER_USER_NAME + "CREATE_";
	private static final String TEST_USER_USER_NAME_LIST = TEST_USER_USER_NAME + "LIST_";
	private static final String TEST_USER_USER_NAME_DELETE = TEST_USER_USER_NAME + "DELETE_";

	@Before
	public void setUp() throws Exception {
		userManager = new UserManager();
	}

	@After
	public void tearDown() throws Exception {
		userManager = null;
	}

	@Test
	public void testCreateUser() {

		/*
		 * Create test user and verify its creation in the database.
		 * 
		 * NOTE: Do not forget to delete the test user afterwards!
		 */
		try {
			userManager.createUser(
					TEST_USER_FIRST_NAME,
					TEST_USER_LAST_NAME,
					TEST_USER_USER_NAME_CREATE,
					TEST_USER_PASSWORD,
					TEST_USER_EMAIL);
			
			User user = userManager.getUserByUserName(TEST_USER_USER_NAME_CREATE);
			assertTrue(null != user);
			assertTrue(TEST_USER_FIRST_NAME.equalsIgnoreCase(user.getFirstName()));
			assertTrue(TEST_USER_LAST_NAME.equalsIgnoreCase(user.getLastName()));
			assertTrue(TEST_USER_USER_NAME_CREATE.equalsIgnoreCase(user.getNickname()));
			assertTrue(TEST_USER_PASSWORD.equalsIgnoreCase(user.getPassword()));
			assertTrue(TEST_USER_EMAIL.equalsIgnoreCase(user.getEmail()));
			
			userManager.deleteUser(TEST_USER_USER_NAME_CREATE);
		} catch (BusinessException e) {
			e.printStackTrace();
			fail("Failure creating user.");
		}
	}

	@Test
	public void testListUsers() {
		/*
		 * Create test user, read list of users from the database
		 * and verify it is not empty.
		 * 
		 * NOTE: Do not forget to delete the test user afterwards!
		 */
		try {
			// Add test user to database.
			userManager.createUser(
					TEST_USER_FIRST_NAME,
					TEST_USER_LAST_NAME,
					TEST_USER_USER_NAME_LIST,
					TEST_USER_PASSWORD,
					TEST_USER_EMAIL);
			
			List<User> userList = userManager.listUsers();
			assertTrue(!userList.isEmpty());
			
			userManager.deleteUser(TEST_USER_USER_NAME_LIST);
		} catch (BusinessException e) {
			e.printStackTrace();
			fail("Failure listing users.");
		}
	}

	@Test
	public void testDeleteUser() {

		/*
		 * Create and delete test user and read list of users from the database
		 * to verify its correct operation.
		 */
		try {
			// Add test user to database.
			userManager.createUser(
					TEST_USER_FIRST_NAME,
					TEST_USER_LAST_NAME,
					TEST_USER_USER_NAME_DELETE,
					TEST_USER_PASSWORD,
					TEST_USER_EMAIL);
			
			int numUsers = userManager.listUsers().size();
			
			userManager.deleteUser(TEST_USER_USER_NAME_DELETE);
			
			assertTrue(userManager.listUsers().size() == numUsers - 1);
			
			try {
				@SuppressWarnings("unused")
				User user = userManager.getUserByUserName(TEST_USER_USER_NAME_DELETE);
				
				// If test reaches this line, it means user was not deleted.
				fail("Failure deleting user.");
			} catch (BusinessException e) {
				// Test passed.
			}
		} catch (BusinessException e) {
			e.printStackTrace();
			fail("Failure deleting user.");
		}
	}
}
