package es.upm.emse.enteridea.persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
/**
 * Class that creates the serviceFactory needed to obtain the connection to the database
 * @author ottoabreu
 *
 */
public final class PersistenceManager {


	private static SessionFactory sessionFactory;
	private static ServiceRegistry serviceRegistry;
	//logger
	static Logger logger = LogManager.getLogger(PersistenceManager.class);

	static {
		try {
			Configuration configuration = new Configuration().configure();
			serviceRegistry = new ServiceRegistryBuilder().applySettings(
					configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		} catch (HibernateException he) {
			logger.error("Can not generate the SessionFactory object due an error",he);
			throw new ExceptionInInitializerError("Can not generate the SessionFactory object due an error: "+he.getMessage());
		}
	}
	/**
	 * Returns the SessionFactory object
	 * @return SessionFactory
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

}
