package es.upm.emse.enteridea.persistence.dao.imp;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.upm.emse.enteridea.persistence.PersistenceManager;
import es.upm.emse.enteridea.persistence.dao.GenericDAO;
import es.upm.emse.enteridea.persistence.exception.DaoOperationException;

/**
 * Implementation of the genericDAO.
 * 
 * @author ottoabreu
 * 
 */
public class GenericDAOImp<T extends Object, PK extends Serializable>
		implements GenericDAO<T, PK> {
	/**
	 * entity class
	 */
	protected Class<T> entityClass;
	private Session session;
	private Transaction tx;

	public static final int UPDATE = 0;
	public static final int SAVE = 1;

	// logger
	private static Logger logger = LogManager.getLogger(GenericDAOImp.class);

	/**
	 * Constructor of the class
	 */

	public GenericDAOImp(Class<T> classT) {
		this.entityClass = classT;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.upm.emse.enteridea.persistence.dao.GenericDAO#create(java.lang.Object)
	 */
	@Override
	public void create(T t) throws DaoOperationException {

		try {
			this.executeSaveUpdate(t, GenericDAOImp.SAVE);
			logger.info("object saved");
		} catch (HibernateException e) {
			logger.error(DaoOperationException.CREATE_ERROR, e);
			throw new DaoOperationException(DaoOperationException.CREATE_ERROR,
					e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.upm.emse.enteridea.persistence.dao.GenericDAO#read(java.io.Serializable
	 * )
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T read(PK id) throws DaoOperationException {
		T objectToReturn = null;
		this.init();
		try {
			objectToReturn = (T) this.session.get(this.entityClass, id);
		} catch (HibernateException e) {

			logger.error(DaoOperationException.READ_ERROR, e);
			throw new DaoOperationException(DaoOperationException.READ_ERROR, e);

		} finally {
			this.sessionClose();
		}
		return objectToReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.upm.emse.enteridea.persistence.dao.GenericDAO#update(java.lang.Object)
	 */
	@Override
	public void update(T t) throws DaoOperationException {
		try {
			this.executeSaveUpdate(t, GenericDAOImp.UPDATE);
			logger.info("object Updated");
		} catch (HibernateException e) {
			logger.error(DaoOperationException.UPDATE_ERROR, e);
			throw new DaoOperationException(DaoOperationException.UPDATE_ERROR,
					e);
		}
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.upm.emse.enteridea.persistence.dao.GenericDAO#delete(java.lang.Object)
	 */
	public void delete(PK id) throws DaoOperationException {
		try {
			this.init();
			@SuppressWarnings("unchecked")
			T entity = (T) this.session.get(this.entityClass, id);
			this.session.delete(entity);
			this.tx.commit();
		} catch (Exception e) {
			logger.error(DaoOperationException.DELETE_ERROR, e);
			throw new DaoOperationException(DaoOperationException.DELETE_ERROR,
					e);
		} finally {
			this.sessionClose();
		}
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see es.upm.emse.enteridea.persistence.dao.GenericDAO#getAll()
	 */
	public List<T> getAll() throws DaoOperationException {
		List<T> resultset = null;
		this.init();
		try {
			
			String query = "FROM " + this.entityClass.getSimpleName() + " e";
			// Query queryHQL = this.session.createQuery(query);
			// resultset = queryHQL.list();
			resultset = this.executeListQuery(query, null, null, null);
		} catch (HibernateException e) {
			logger.error(DaoOperationException.READ_ERROR, e);
			throw new DaoOperationException(DaoOperationException.READ_ERROR, e);
		} finally {
			this.sessionClose();
		}

		return resultset;

	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.upm.emse.enteridea.persistence.dao.GenericDAO#getByQuery(java.lang
	 * .String, java.util.Map)
	 */
	public List<T> getByQuery(String query, Map<String, ?> params,
			Integer firstResult, Integer maxResul) throws DaoOperationException {
		List<T> resultset = null;
		this.init();
		try {

			resultset = this.executeListQuery(query, params, firstResult,
					maxResul);

		} catch (HibernateException e) {
			logger.error(DaoOperationException.READ_ERROR, e);
			throw new DaoOperationException(DaoOperationException.READ_ERROR, e);
		} finally {
			this.sessionClose();
		}

		return resultset;
	}
	  
	@Override
	/*
	 * (non-Javadoc)
	 * @see es.upm.emse.enteridea.persistence.dao.GenericDAO#getTotalRecords()
	 */
	public Integer getTotalRecords() throws DaoOperationException {

		Integer totalRecords = null;
		this.init();
		try {

			totalRecords = ((Long) this.session
					.createQuery(
							"SELECT count(*) FROM "
									+ this.getTableNameFromEntity()).iterate()
					.next()).intValue();
			logger.debug("Total records: " + totalRecords);
		} catch (HibernateException e) {
			logger.error(DaoOperationException.READ_TOTAL_RECORDS_ERROR, e);
			throw new DaoOperationException(
					DaoOperationException.READ_TOTAL_RECORDS_ERROR, e);
		} finally {
			this.sessionClose();
		}

		return totalRecords;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.upm.emse.enteridea.persistence.dao.GenericDAO#getTableNameFromEntity()
	 */
	public String getTableNameFromEntity() {
		Table table = this.entityClass.getAnnotation(Table.class);
		return table.name();
	}

	/**
	 * Method that initiates the session to execute transactions
	 * 
	 * @throws DaoOperationException
	 *             if can't open the connection
	 */
	private void init() throws DaoOperationException {
		try {
			this.session = PersistenceManager.getSessionFactory().openSession();
			this.tx = this.session.beginTransaction();
		} catch (HibernateException e) {
			logger.error(DaoOperationException.CONNECTION_ERROR, e);
			throw new DaoOperationException(
					DaoOperationException.CONNECTION_ERROR, e);

		}
	}

	/**
	 * Method that performs a rollback
	 */
	private void rollback() {
		this.tx.rollback();

	}

	/**
	 * Close the connection
	 */
	private void sessionClose() {
		if (this.session.isOpen()) {
			this.session.close();
		}
	}

	/**
	 * Executes the logic of save or update. use the constant defined in this
	 * class to select the operation<br>
	 * Example this.executeSaveUpdate(t, SAVE);
	 * 
	 * @param t
	 *            T object to be saved or updates
	 * @param operation
	 *            int
	 * @throws DaoOperationException
	 *             if can't save, update or open the connection
	 */
	private void executeSaveUpdate(T t, int operation)
			throws DaoOperationException {
		this.init();

		try {
			switch (operation) {
			case UPDATE:
				logger.debug("executing update");
				this.session.update(t);
				break;

			case SAVE:
				logger.debug("executing save");
				this.session.save(t);
				break;
			default:
				logger.error("the option have to be either UPDATE(0) or SAVE(1), received: "
								+ operation);
				throw new DaoOperationException(
						"the option have to be either UPDATE(0) or SAVE(1), received: "
								+ operation);
			}
			this.tx.commit();
		} catch (HibernateException e) {
			this.rollback();
			throw e;
		} finally {
			this.sessionClose();
		}

	}

	/**
	 * Execute a query and returns the result
	 * 
	 * @param query
	 *            String in HQL-SQL format
	 * @param params
	 *            Map with the argument in the query
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	private List<T> executeListQuery(String query, Map<String, ?> params,
			Integer firstResult, Integer maxResult) {

		Query queryHQL = this.session.createQuery(query);

		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, ?> param : params.entrySet()) {
				queryHQL.setString(param.getKey().toString(), param.getValue()
						.toString());

			}
		}

		if (firstResult != null || maxResult != null) {
			if (firstResult != null) {
				queryHQL.setFirstResult(firstResult);
			}
			if (maxResult != null) {
				queryHQL.setMaxResults(maxResult);
			}
		}
		return queryHQL.list();
	}
}
