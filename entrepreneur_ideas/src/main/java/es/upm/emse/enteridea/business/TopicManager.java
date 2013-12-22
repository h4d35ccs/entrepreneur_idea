package es.upm.emse.enteridea.business;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.upm.emse.enteridea.business.exception.BusinessException;
import es.upm.emse.enteridea.persistence.dao.GenericDAO;
import es.upm.emse.enteridea.persistence.dao.imp.GenericDAOImp;
import es.upm.emse.enteridea.persistence.entity.Topic;
import es.upm.emse.enteridea.persistence.exception.DaoOperationException;

public class TopicManager {
	// logger
	private static Logger logger = LogManager.getLogger(TopicManager.class);

	public void createTopic(String name) throws BusinessException {
		GenericDAO<Topic, Long> daoTopic = new GenericDAOImp<Topic, Long>(
				Topic.class);
		Topic topic = new Topic();
		topic.setTopicDescription(name);
		topic.setIdeas(null);
		try {
			daoTopic.create(topic);
		} catch (DaoOperationException e) {
			logger.error(BusinessException.CREATE_TOPIC_ERROR, e);
			throw new BusinessException(BusinessException.CREATE_TOPIC_ERROR, e);
		}
	}

	public List<Topic> listTopics() throws BusinessException {
		GenericDAO<Topic, Long> daoTopic = new GenericDAOImp<Topic, Long>(
				Topic.class);
		try {
			return daoTopic.getAll();
		} catch (DaoOperationException e) {
			logger.error(BusinessException.RETRIEVE_TOPIC_ERROR, e);
			throw new BusinessException(BusinessException.RETRIEVE_TOPIC_ERROR,
					e);
		}
	}
	
	public void deleteTopic(Long id) throws BusinessException{
		GenericDAO<Topic, Long> daoTopic = new GenericDAOImp<Topic, Long>(
				Topic.class);
		try {
			daoTopic.delete(id);
		} catch (DaoOperationException e) {
			logger.error(BusinessException.RETRIEVE_TOPIC_ERROR, e);
			throw new BusinessException(BusinessException.CREATE_TOPIC_ERROR,
					e);
		}
	}
}
