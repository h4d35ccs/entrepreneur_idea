/**
 * 
 */
package es.upm.emse.enteridea.business;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import es.upm.emse.enteridea.persistence.dao.GenericDAO;
import es.upm.emse.enteridea.persistence.dao.imp.GenericDAOImp;
import es.upm.emse.enteridea.persistence.entity.User;
import es.upm.emse.enteridea.persistence.exception.DaoOperationException;

/**
 * @author ottoabreu
 * 
 */
public class SecurityManagerRealm extends AuthorizingRealm {

	private static Logger logger = LogManager
			.getLogger(SecurityManagerRealm.class);
	private final GenericDAO<User, Long> userDao = new GenericDAOImp<User, Long>(
			User.class);

	private static final String HQL_QUERY = "FROM User u Where u.nickname = :nickname";

	private CredentialsMatcher matcher = new SimpleCredentialsMatcher();

	/**
	 * 
	 */
	public SecurityManagerRealm() {
		this.setCredentialsMatcher(matcher);

	}

	/**
	 * @param cacheManager
	 */
	public SecurityManagerRealm(CacheManager cacheManager) {
		super(cacheManager);
		this.setCredentialsMatcher(matcher);
	}

	/**
	 * @param matcher
	 */
	public SecurityManagerRealm(CredentialsMatcher matcher) {
		if (matcher == null) {
			this.setCredentialsMatcher(this.matcher);
		} else {
			this.setCredentialsMatcher(matcher);
		}
	}

	/**
	 * @param cacheManager
	 * @param matcher
	 */
	public SecurityManagerRealm(CacheManager cacheManager,
			CredentialsMatcher matcher) {
		if (matcher == null) {
			this.setCredentialsMatcher(this.matcher);

		} else {
			this.setCredentialsMatcher(matcher);
		}
		this.setCacheManager(cacheManager);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache
	 * .shiro.subject.PrincipalCollection)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		SimpleAuthorizationInfo info = null;
		Set<String> roleNames = new LinkedHashSet<String>();
		Map<String, String> params = new HashMap<String, String>();
		try {
			String username = (String) getAvailablePrincipal(principals);

			params.put("nickname", username);
			List<User> result = this.userDao.getByQuery(HQL_QUERY, params,
					null, 1);

			if (result != null && result.size() == 1) {
				User userDB = result.get(0);
				if (userDB.isModerator()) {
					roleNames.add("moderator");
				}
			} else {
				logger.warn("there is no user that match :" + username
						+ "can not set the rol");
			}
			info = new SimpleAuthorizationInfo(roleNames);

		} catch (DaoOperationException e) {
			logger.warn("Can not set the rol to the user due an DB error", e);

		} catch (Exception e) {
			logger.warn("Can not set the to the user due a general error", e);

		}
		return info;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org
	 * .apache.shiro.authc.AuthenticationToken)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

		AuthenticationInfo info = null;
		try {
			UsernamePasswordToken upToken = (UsernamePasswordToken) token;
			String username = upToken.getUsername();
			Map<String, String> params = new HashMap<String, String>();
			params.put("nickname", username);
			List<User> result = this.userDao.getByQuery(HQL_QUERY, params,
					null, 1);

			if (result != null && result.size() == 1) {
				User userDB = result.get(0);
				info = new SimpleAuthenticationInfo(username, userDB
						.getPassword().toCharArray(), getName());

			} else {
				logger.error("No match found in the DB for user: " + username);
				throw new UnknownAccountException(
						"No match found in the DB for user: " + username);
			}

		} catch (DaoOperationException e) {
			logger.error("Can not authenticate user due a DB error", e);
			throw new AuthenticationException(
					"Can not authenticate user due a DB error", e);
		} catch (Exception e) {
			if(e instanceof UnknownAccountException){
				logger.debug("throws:"+e);
				throw (UnknownAccountException)e;
			}
			logger.error("Can not authenticate user due a general error", e);
			throw new AuthenticationException(
					"Can not authenticate user due a general error", e);
		}

		return info;
	}

}
