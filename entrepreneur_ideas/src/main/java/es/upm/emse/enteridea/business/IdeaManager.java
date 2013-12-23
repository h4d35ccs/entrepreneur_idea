package es.upm.emse.enteridea.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.upm.emse.enteridea.business.exception.BusinessException;
import es.upm.emse.enteridea.persistence.dao.GenericDAO;
import es.upm.emse.enteridea.persistence.dao.imp.GenericDAOImp;
import es.upm.emse.enteridea.persistence.entity.Idea;
import es.upm.emse.enteridea.persistence.entity.IdeaComment;
import es.upm.emse.enteridea.persistence.entity.Topic;
import es.upm.emse.enteridea.persistence.entity.User;
import es.upm.emse.enteridea.persistence.exception.DaoOperationException;

public class IdeaManager {
	// assume an existing topic. If desired topic doesnt't exist it must be
	// created first
	// logger
	private static Logger logger = LogManager.getLogger(IdeaManager.class);

	public void createIdea(String ownerId, String topicId, String title,
			String description) throws BusinessException {
		try {

			GenericDAO<Idea, Long> daoIdea = new GenericDAOImp<Idea, Long>(
					Idea.class);
			GenericDAO<Topic, Long> daoTopic = new GenericDAOImp<Topic, Long>(
					Topic.class);
			GenericDAO<User, Long> daoUser = new GenericDAOImp<User, Long>(
					User.class);

			Idea idea = new Idea();
			idea.setCreationDate(new Date());
			idea.setIdeaComments(null);
			idea.setIdeaOwner(daoUser.read(new Long(ownerId)));
			idea.setTopic(daoTopic.read(new Long(topicId)));

			idea.setTitle(title);
			idea.setDescription(description);

			daoIdea.create(idea);
		} catch (DaoOperationException e) {
			logger.error(BusinessException.CREATE_IDEA_ERROR, e);
			throw new BusinessException(BusinessException.CREATE_IDEA_ERROR, e);
		} catch (Exception e) {
			logger.error(BusinessException.CREATE_IDEA_ERROR, e);
			throw new BusinessException(BusinessException.CREATE_IDEA_ERROR, e);
		}
	}

	public List<Idea> listIdea() throws BusinessException {
		GenericDAO<Idea, Long> daoIdea = new GenericDAOImp<Idea, Long>(
				Idea.class);
		try {
			return daoIdea.getAll();
		} catch (DaoOperationException e) {
			logger.error(BusinessException.RETRIEVE_IDEA_ERROR, e);
			throw new BusinessException(BusinessException.RETRIEVE_IDEA_ERROR,
					e);
		}
	}

	/**
	 * Edit an existing idea. Will only edit if one of the params (title or
	 * description) are not null<b> For adding a empty description or title use
	 * "" ( blank string)
	 * 
	 * @param idIdea
	 *            String with the id of the idea to be edited
	 * @param title
	 *            String with the new title, can be null ( null = title will not
	 *            be updated)
	 * @param description
	 *            String with the new description, can be null ( null =
	 *            description will not be updated)
	 * @throws BusinessException
	 *             if can not edit the given idea
	 */
	public void editIdea(String idIdea, String title, String description,
			String userEditing) throws BusinessException {

		Idea idea = this.getIdea(idIdea);
		UserManager um = new UserManager();
		logger.debug("editing user:" + userEditing);
		User userEditingIdea = um.getUserByUserName(userEditing);
		// verify is the user trying to edit is the owner or if a moderator
		if (idea.getIdeaOwner().getNickname().equals(userEditing)
				|| userEditingIdea.isModerator()) {

			if (title != null || description != null) {

				if (title != null) {
					idea.setTitle(title);
				}
				if (description != null) {
					idea.setDescription(description);
				}
				idea.setModificationDate((new Date()));

				GenericDAO<Idea, Long> daoIdea = new GenericDAOImp<Idea, Long>(
						Idea.class);

				try {
					daoIdea.update(idea);
				} catch (DaoOperationException e) {
					logger.error(DaoOperationException.UPDATE_ERROR, e);
					throw new BusinessException(
							BusinessException.UPDATE_IDEA_ERROR, e);
				}
			} else {
				logger.warn("Will not update idea since all values are null, id idea:"
						+ idIdea);
			}
		} else {
			logger.error(BusinessException.EDITING_PERMISION_ERROR);
			throw new BusinessException(
					BusinessException.EDITING_PERMISION_ERROR);
		}
	}

	public Idea getIdea(String idIdea) throws BusinessException {
		Idea idea = null;
		GenericDAO<Idea, Long> daoIdea = new GenericDAOImp<Idea, Long>(
				Idea.class);
		try {

			idea = daoIdea.read(new Long(idIdea));
		
		} catch (NumberFormatException e) {
			logger.error("The Id is not a number.", e);
			throw new BusinessException(BusinessException.ID_NOT_NUMBER, e);

		} catch (DaoOperationException e) {
			logger.error(DaoOperationException.READ_ERROR, e);
			throw new BusinessException(BusinessException.RETRIEVE_IDEA_ERROR,
					e);
		}
		return idea;
	}

	/**
	 * Adds a comment to a given idea
	 * 
	 * @param idIdea
	 *            String with the id of the idea that will have the new comment
	 * @param idUser
	 *            String with the id of the user who <b>MAKES THE COMMENT</B>
	 *            not necessary the owner of the idea
	 * @param text
	 *            String with the comment to be added
	 * @throws BusinessException
	 *             if can not add the comment into the idea
	 */
	public IdeaComment createComment(String idIdea, String idUser, String text)
			throws BusinessException {

		Idea idea = null;
		User user = null;
		IdeaComment comment = null;

		GenericDAO<Idea, Long> daoIdea = new GenericDAOImp<Idea, Long>(
				Idea.class);

		GenericDAO<User, Long> daoUser = new GenericDAOImp<User, Long>(
				User.class);

		GenericDAO<IdeaComment, Long> daoComment = new GenericDAOImp<IdeaComment, Long>(
				IdeaComment.class);

		try {
			idea = daoIdea.read(new Long(idIdea));
			user = daoUser.read(new Long(idUser));
			comment = new IdeaComment();
			comment.setCommentOwner(user);
			comment.setIdea(idea);
			comment.setComment(text);
			daoComment.create(comment);

			return comment;

		} catch (NumberFormatException e) {
			logger.error(BusinessException.ID_NOT_NUMBER, e);
			throw new BusinessException(BusinessException.ID_NOT_NUMBER, e);

		} catch (DaoOperationException e) {
			logger.error(BusinessException.CREATE_COMMENT_ERROR, e);
			throw new BusinessException(BusinessException.CREATE_COMMENT_ERROR,
					e);
		}

	}

	public List<IdeaComment> getComments(String idIdea)
			throws BusinessException {

		Idea idea = null;
		GenericDAO<Idea, Long> daoIdea = new GenericDAOImp<Idea, Long>(
				Idea.class);
		List<IdeaComment> listComments = null;

		try {
			idea = daoIdea.read(new Long(idIdea));
			listComments = new ArrayList<IdeaComment>(idea.getIdeaComments());

		} catch (NumberFormatException e) {
			logger.error(BusinessException.ID_NOT_NUMBER, e);
			throw new BusinessException(BusinessException.ID_NOT_NUMBER, e);
		} catch (DaoOperationException e) {
			logger.error(BusinessException.RETRIEVE_COMMENT_ERROR, e);
			throw new BusinessException(
					BusinessException.RETRIEVE_COMMENT_ERROR, e);
		}

		return listComments;
	}

	/**
	 * Deletes idea and all of its comments
	 * 
	 * @param idIdea
	 *            String with the id of the idea that will be deleted
	 * 
	 * @param userDeleting
	 *            User that is deleting the idea
	 * 
	 * @throws BusinessException
	 *             if can not delete the idea
	 */
	public void deleteIdea(String idIdea, String userDeleting)
			throws BusinessException {

		UserManager um = new UserManager();
		Idea idea = this.getIdea(idIdea);
		logger.debug("user trying to delete:" + userDeleting);
		User userDeletingIdea = um.getUserByUserName(userDeleting);

		// verify is the user trying to delete is the owner or if a moderator
		if (idea.getIdeaOwner().getNickname().equals(userDeleting)
				|| userDeletingIdea.isModerator()) {

			try {

				GenericDAO<Idea, Long> daoIdea = new GenericDAOImp<Idea, Long>(
						Idea.class);

				this.deleteCommentsOfIdea(idIdea);

				daoIdea.delete(new Long(idIdea));
				logger.info("idea deleted:"+idIdea);

			} catch (NumberFormatException e) {
				logger.error("The Id is not a number.", e);
				throw new BusinessException(BusinessException.ID_NOT_NUMBER, e);
			} catch (DaoOperationException e) {
				logger.error(DaoOperationException.DELETE_ERROR, e);
				throw new BusinessException(
						BusinessException.DELETE_IDEA_ERROR, e);
			}

		} else {
			logger.error(BusinessException.EDITING_PERMISION_ERROR);
			throw new BusinessException(
					BusinessException.EDITING_PERMISION_ERROR);
		}
	}

	/**
	 * Deletes all comments of an Idea
	 * 
	 * @param idIdea
	 *            String with the id of the idea
	 * 
	 * @throws BusinessException
	 */
	private void deleteCommentsOfIdea(String idIdea) throws BusinessException {
		{
			List<IdeaComment> comments = this.getComments(idIdea);

			for (IdeaComment comment : comments) {

				String commmentID = "" + comment.getCommentId();

				GenericDAO<IdeaComment, Long> daoComment = new GenericDAOImp<IdeaComment, Long>(
						IdeaComment.class);
				try {

					daoComment.delete(new Long(commmentID));

				} catch (NumberFormatException e) {
					logger.error("The Id is not a number.", e);
					throw new BusinessException(
							BusinessException.ID_NOT_NUMBER, e);

				} catch (DaoOperationException e) {
					logger.error(DaoOperationException.DELETE_ERROR, e);
					throw new BusinessException(
							BusinessException.DELETE_COMMENT_ERROR, e);
				}
			}
		}
	}

	/**
	 * Deletes comments
	 * 
	 * @param idComment
	 *            String with the id of the comment that will be deleted
	 * 
	 * @param userDeleting
	 *            User that is deleting the comment
	 * 
	 * @throws BusinessException
	 *             if can not delete the comment
	 */
	public void deleteComment(String idComment, String userDeleting)
			throws BusinessException {

		UserManager um = new UserManager();
		User userDeletingComment = um.getUserByUserName(userDeleting);
		GenericDAO<IdeaComment, Long> daoComment = new GenericDAOImp<IdeaComment, Long>(
				IdeaComment.class);
		Map<String, Long> params = new HashMap<String, Long>();
		params.put("commentId", new Long(idComment));

		try {

//			List<IdeaComment> result = daoComment.getByQuery(
//					"FROM IdeaComment WHERE commentId=:commentId", params, null, 1);
			IdeaComment comentToDelete = daoComment.read(new Long(idComment));
//			if (!result.isEmpty()) {
			if(comentToDelete != null){
//				IdeaComment comentToDelete = result.get(0);

				// verify is the user trying to delete a moderator
				if (userDeletingComment.isModerator()
						|| comentToDelete.getCommentOwner().getNickname()
								.equals(userDeleting)) {

					daoComment.delete(new Long(idComment));
				} else {
					logger.error(BusinessException.EDITING_PERMISION_ERROR);
					throw new BusinessException(
							BusinessException.EDITING_PERMISION_ERROR);
				}

			} else {
				logger.error("The comment does not exit in the BD");
				throw new BusinessException(
						"The comment does not exit in the BD");
			}
		} catch (NumberFormatException e) {
			logger.error("The Id is not a number.", e);
			throw new BusinessException(BusinessException.ID_NOT_NUMBER, e);

		} catch (DaoOperationException e) {
			logger.error(DaoOperationException.DELETE_ERROR, e);
			throw new BusinessException(BusinessException.DELETE_COMMENT_ERROR,
					e);
		}
	}
}
