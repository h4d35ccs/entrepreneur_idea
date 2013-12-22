package test.es.upm.emse.enteridea.spock
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;

import es.upm.emse.enteridea.persistence.dao.GenericDAO;
import es.upm.emse.enteridea.persistence.dao.imp.GenericDAOImp;
import es.upm.emse.enteridea.persistence.entity.User;
import spock.lang.Specification



class SecurityRealmTest extends Specification {
	
	private GenericDAO<User, Long> daoUser;
	private Subject currentUser;
	private User createdUser;

	def setup(){
		Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
		SecurityManager securityManager = factory.getInstance();
		SecurityUtils.setSecurityManager(securityManager);
		this.currentUser = SecurityUtils.getSubject();
		this.daoUser = new GenericDAOImp<User, Long>(User.class);
		this.createdUser =  new User("Security name", "Security lastname", "shiroTest",
				"1234", "shiro@apache.com", null, null);
		daoUser.create(createdUser);
	}
	
	def cleanup(){
		daoUser.delete(this.createdUser.getUserId());
	}
	
	def "test that a register user can perform the login"(){
		
		given:"A user that is not autenticated"
		!currentUser.isAuthenticated()
		
		when:"the user enter the login and password"
		UsernamePasswordToken token = new UsernamePasswordToken("shiroTest",
			"1234");
		currentUser.login(token);
		
		then:"the user is authenticated"
		currentUser.isAuthenticated()
		
		cleanup:
		currentUser.logout();
	}
	
	def "test that a user with wrongs credentials can not perform a login"(){
		given:"A user that is not autenticated"
		!currentUser.isAuthenticated()
		
		when:"the user enter the login and wrong password"
		UsernamePasswordToken token = new UsernamePasswordToken("shiroTest",
			"12364");
		currentUser.login(token);
		then:"the user is not authenticated"
		thrown(IncorrectCredentialsException);
		
	}
	
	def "test that a user that is not register in the bd can not perdorm a login"(){
		given:"A user that is not autenticated"
		!currentUser.isAuthenticated()
		
		when:"the user enter the login and password"
		UsernamePasswordToken token = new UsernamePasswordToken("shiroTestnotexist",
			"1234");
		currentUser.login(token);
		then:"the user is not authenticated"
		thrown(UnknownAccountException);
		
	}
}

