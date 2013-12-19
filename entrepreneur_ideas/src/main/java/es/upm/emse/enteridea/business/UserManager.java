package es.upm.emse.enteridea.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.upm.emse.enteridea.business.exception.BusinessException;
import es.upm.emse.enteridea.persistence.dao.GenericDAO;
import es.upm.emse.enteridea.persistence.dao.imp.GenericDAOImp;
import es.upm.emse.enteridea.persistence.entity.User;
import es.upm.emse.enteridea.persistence.exception.DaoOperationException;

public class UserManager {

	// logger
	private static Logger logger = LogManager.getLogger(UserManager.class);

	public void createUser(String firstname, String lastname, String nickname,
			String password, String email) throws BusinessException {
		GenericDAO<User, Long> daoUser = new GenericDAOImp<User, Long>(
				User.class);
		User user = new User(firstname, lastname, nickname, password, email,
				null, null);
		try {
			daoUser.create(user);
		} catch (DaoOperationException e) {
			logger.error(BusinessException.CREATE_USER_ERROR, e);
			throw new BusinessException(BusinessException.CREATE_USER_ERROR, e);
		}
	}

	public List<User> listUsers() throws BusinessException {
		GenericDAO<User, Long> daoUser = new GenericDAOImp<User, Long>(
				User.class);
		try {
			return daoUser.getAll();
		} catch (DaoOperationException e) {
			logger.error(BusinessException.RETRIEVE_USER_ERROR, e);
			throw new BusinessException(BusinessException.RETRIEVE_USER_ERROR,
					e);
		}
	}

	/**
	 * From a given username or nickname, return the user associated
	 * 
	 * @param username
	 *            String
	 * @return {@link User}
	 * @throws BusinessException
	 *             if can not retrieve the user by the given username
	 */
	public User getUserByUserName(String username) throws BusinessException {

		GenericDAO<User, Long> daoUser = new GenericDAOImp<User, Long>(
				User.class);

		String hqlQuery = "FROM User u WHERE nickname = :username";
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", username);
		User user = null;
		try {
			List<User> result = daoUser.getByQuery(hqlQuery, params, null, 1);

			if (result != null && result.size() == 1) {
				user = result.get(0);
			} else {
				logger.error(BusinessException.RETRIEVE_USER_ERROR
						+ " because the result from the DB is null or have more than one result (have to be only one)");
				throw new BusinessException(
						BusinessException.RETRIEVE_USER_ERROR
								+ " because the result from the DB is null or have more than one result (have to be only one)");
			}

		} catch (DaoOperationException e) {
			logger.error(BusinessException.RETRIEVE_USER_ERROR, e);
			throw new BusinessException(BusinessException.RETRIEVE_USER_ERROR,
					e);
		}

		return user;
	}

	/**
	 * From a given username or nickname, delete the associated user
	 * 
	 * @param username
	 *            String
	 * @return void
	 * @throws BusinessException
	 *             if the user cannot be retrieved or deleted.
	 */
	public void deleteUser(String username) throws BusinessException {

		User user = getUserByUserName(username);

		if (null != user) {
			Long userId = user.getUserId();
			GenericDAO<User, Long> daoUser = new GenericDAOImp<User, Long>(
					User.class);
			try {
				daoUser.delete(userId);
			} catch (DaoOperationException e) {
				logger.error(BusinessException.DELETE_USER_ERROR, e);
				throw new BusinessException(
						BusinessException.DELETE_USER_ERROR, e);
			}
		} else {
			String errorCause = BusinessException.DELETE_USER_ERROR
					+ " because the result from the DB is null.";
			logger.error(errorCause);
			throw new BusinessException(errorCause);
		}
	}

}
