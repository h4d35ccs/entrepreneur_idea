package test.es.upm.emse.enteridea.business;

import junit.framework.TestCase;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Test;

import es.upm.emse.enteridea.persistence.dao.GenericDAO;
import es.upm.emse.enteridea.persistence.dao.imp.GenericDAOImp;
import es.upm.emse.enteridea.persistence.entity.User;

/**
 * The class <code>SecurityManagerRealmTest</code> contains tests for the class
 * {@link <code>SecurityManagerRealm</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro at 2/12/13 1:12
 * 
 * @author ottoabreu
 * 
 * @version $Revision$
 */
public class SecurityManagerRealmTest extends TestCase {

	private GenericDAO<User, Long> daoUser;
	private Subject currentUser;
	private User createdUser;

	/**
	 * Construct new test instance
	 * 
	 * @param name
	 *            the test name
	 */
	public SecurityManagerRealmTest(String name) {
		super(name);
	}

	/**
	 * Perform pre-test initialization
	 * 
	 * @throws Exception
	 * 
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		this.currentUser = SecurityUtils.getSubject();
		this.daoUser = new GenericDAOImp<User, Long>(User.class);
		this.createdUser =  new User("Security name", "Security lastname", "shiro",
				"1234", "shiro@apache.com", null, null);
		daoUser.create(createdUser);
	}

	/**
	 * Perform post-test clean up
	 * 
	 * @throws Exception
	 * 
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		daoUser.delete(this.createdUser.getUserId());
	}

	@Test
	public void testLogin() {

		if (!currentUser.isAuthenticated()) {

			UsernamePasswordToken token = new UsernamePasswordToken("shiro",
					"1234");
			try {
				currentUser.login(token);
			} catch (UnknownAccountException uae) {
				uae.printStackTrace();
				fail(uae.getMessage());
			} catch (IncorrectCredentialsException ice) {
				ice.printStackTrace();
				fail(ice.getMessage());
			} catch (AuthenticationException ae) {
				ae.printStackTrace();
				fail(ae.getMessage());
			}catch(Exception e){
				e.printStackTrace();
				fail(e.getMessage());
			}

		}
	}
	
	@Test(expected = IncorrectCredentialsException.class)
	public void testFailedLogin() {

		if (!currentUser.isAuthenticated()) {

			UsernamePasswordToken token = new UsernamePasswordToken("shiro",
					"12345");
			
				currentUser.login(token);
			

		}
	}
	
	@Test(expected = UnknownAccountException.class)
	public void testNoUser() {

		if (!currentUser.isAuthenticated()) {

			UsernamePasswordToken token = new UsernamePasswordToken("shiro",
					"12345");
			try {
				currentUser.login(token);
			} catch (UnknownAccountException uae) {
				fail(uae.getMessage());
			} catch (AuthenticationException ae) {
				if(ae instanceof UnknownAccountException){
					throw ae;
				}else{
					fail(ae.getMessage());
				}
			}

		}
	}
}

/*
 * $CPS$ This comment was generated by CodePro. Do not edit it. patternId =
 * com.instantiations.assist.eclipse.pattern.testCasePattern strategyId =
 * com.instantiations.assist.eclipse.pattern.testCasePattern.junitTestCase
 * additionalTestNames = assertTrue = false callTestMethod = true createMain =
 * false createSetUp = true createTearDown = true createTestFixture = false
 * createTestStubs = false methods = package =
 * test.es.upm.emse.enteridea.business package.sourceFolder =
 * entrepreneur_ideas/src superclassType = junit.framework.TestCase testCase =
 * SecurityManagerRealmTest testClassType =
 * es.upm.emse.enteridea.business.SecurityManagerRealm
 */