package test.es.upm.emse.enteridea.spock

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import es.upm.emse.enteridea.business.IdeaManager;
import es.upm.emse.enteridea.business.TopicManager;
import es.upm.emse.enteridea.business.UserManager;
import es.upm.emse.enteridea.business.exception.BusinessException;
import es.upm.emse.enteridea.persistence.PersistenceManager;
import es.upm.emse.enteridea.persistence.entity.Idea;
import es.upm.emse.enteridea.persistence.entity.IdeaComment;


class IdeaTest  extends spock.lang.Specification {

	private IdeaManager ideaManager;
	private static final String TEST_IDEA_TITLE = "Title";
	private static final String TEST_IDEA_DESCRIPTION = "Description";

	private TopicManager topicManager;
	private static final String TEST_TOPIC_NAME = "Topic";

	private UserManager userManager;
	private static final String TEST_USER_FIRST_NAME = "Jane";
	private static final String TEST_USER_LAST_NAME = "McTester";
	private static final String TEST_USER_USER_NAME = "___mctesty___"+new Date().time;
	private static final String TEST_USER_PASSWORD = "testicles123";
	private static final String TEST_USER_EMAIL = "jane.mctester@usertest.com";

	private static final String TEST_USER_USER_NAME_CREATE = TEST_USER_USER_NAME + "_CREATE_";
	private static final String TEST_USER_USER_NAME_LIST = TEST_USER_USER_NAME + "_LIST_";
	private static final String TEST_USER_USER_NAME_EDIT = TEST_USER_USER_NAME + "_EDIT_";
	private static final String TEST_USER_USER_NAME_DELETE = TEST_USER_USER_NAME + "_DELETE_";
	private static final String TEST_USER_USER_NAME_DELETE_UNAUTHORIZED = TEST_USER_USER_NAME + "_DELETE_UNAUTHORIZED_";
	private static final String TEST_USER_USER_NAME_COMMENT_AUTHOR = TEST_USER_USER_NAME + "_AUTHOR_";
	private static final String TEST_USER_USER_NAME_COMMENT_ADD = TEST_USER_USER_NAME + "_ADD_";
	private static final String TEST_USER_USER_NAME_COMMENT_GET = TEST_USER_USER_NAME + "_GET_";
	private static final String TEST_USER_USER_NAME_COMMENT_DELETE = TEST_USER_USER_NAME + "_COMMENT_DELETE_";

	private static final String TEST_COMMENT_TEXT = "My Comment";

	def setup(){
		ideaManager = new IdeaManager();
		topicManager = new TopicManager();
		userManager = new UserManager();
	}

	def cleanup() {
		ideaManager = null;
		topicManager = null;
		userManager = null;
		cleanDB();
	}
	
	def cleanDB(){
		try {
			Session session = PersistenceManager.getSessionFactory().openSession();
			Transaction tx = session.beginTransaction();
			session.createQuery("DELETE FROM User WHERE lastName ='"+TEST_USER_LAST_NAME+"'").executeUpdate();
			session.createQuery("DELETE FROM Topic WHERE topicDescription ='"+TEST_TOPIC_NAME+"'").executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	def "Create test idea and verify its creation in the database"(){

		given: "a valid user creates a idea "

		Long ownerId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		TEST_USER_USER_NAME_CREATE,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);

		and:"a valid topic must exit"
		Long topicId = createTestTopic(TEST_TOPIC_NAME);

		when:"the user creates an idea"
		Long ideaId = createTestIdea(
		ownerId.toString(),
		topicId.toString(),
		TEST_IDEA_TITLE,
		TEST_IDEA_DESCRIPTION);

		then:"the idea must be saved in the data base"
		Idea idea = ideaManager.getIdea(ideaId.toString());
		idea != null &&
		ownerId == idea.getIdeaOwner().getUserId() &&
		topicId == idea.getTopic().getTopicId() &&
		TEST_IDEA_TITLE.equalsIgnoreCase(idea.getTitle()) &&
		TEST_IDEA_DESCRIPTION.equalsIgnoreCase(idea.getDescription());

		cleanup:"the added content is removed"
		
		cleanupCreateIdea(topicId,ideaId,TEST_USER_USER_NAME_CREATE);
	}

	def "Create test idea, read list of ideas from the databas and verify it is not empty"(){

		setup:"THe user must be a valid user amd must exist a topic"

		Long topicId = createTestTopic(TEST_TOPIC_NAME);
		Long ownerId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		TEST_USER_USER_NAME_LIST,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);

		and:"at least must exist an idea"
		Long ideaId = createTestIdea(
		ownerId.toString(),
		topicId.toString(),
		TEST_IDEA_TITLE,
		TEST_IDEA_DESCRIPTION);
		when:"The user select list ideas"
		List<Idea> ideas = ideaManager.listIdea();

		then:"the list must be returned"
		ideas.size() > 0;

		cleanup:"the added content is removed"
		cleanupCreateIdea(topicId,ideaId,TEST_USER_USER_NAME_LIST);
	}

	def "Create test idea, then edit idea and verify the change has been saved."(){

		setup:"the idea must exist in order to edit it"

		Long topicId = createTestTopic(TEST_TOPIC_NAME);
		Long ownerId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		TEST_USER_USER_NAME_EDIT,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		Long ideaId = createTestIdea(
		ownerId.toString(),
		topicId.toString(),
		TEST_IDEA_TITLE,
		TEST_IDEA_DESCRIPTION);

		and:" a new title is entered"
		String title2 = "hola titulo";
		String description =" new description";

		when:"the idea is edited"
		ideaManager.editIdea(ideaId.toString(), title2, description, TEST_USER_USER_NAME_EDIT);
		Idea theIdea2 = ideaManager.getIdea(ideaId.toString());

		then:" the changes must be present"
		theIdea2.title == title2 &&  theIdea2.description == description;

		cleanup:"the added content is removed"
		cleanupCreateIdea(topicId,ideaId,TEST_USER_USER_NAME_EDIT);
	}
	
	def "Create test idea, attempt to delete idea  and verify that changes have been made"(){
		setup:"the idea must exist in order to delete it"
		Long topicId = createTestTopic(TEST_TOPIC_NAME);
		Long ownerId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		TEST_USER_USER_NAME_DELETE,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		
		Long ideaId = createTestIdea(
		ownerId.toString(),
		topicId.toString(),
		TEST_IDEA_TITLE,
		TEST_IDEA_DESCRIPTION);
		and:"the number of existing ideas must be the same in the end"
		int numIdeas = ideaManager.listIdea().size();

		when:"an user tries to delete an idea "
		ideaManager.deleteIdea(
		ideaId.toString(),
		TEST_USER_USER_NAME_DELETE);

		then:"the idea must be deleted"
		
		ideaManager.listIdea().size() < numIdeas;

		cleanup:
		userManager.deleteUser(TEST_USER_USER_NAME_DELETE);
		topicManager.deleteTopic(topicId);
		
	}

	def "Create test idea, attempt to delete idea with an unauthorized user and verify no changes have been made"(){

		setup:"the idea must exist in order to delete it"
		Long topicId = createTestTopic(TEST_TOPIC_NAME);
		Long ownerId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		TEST_USER_USER_NAME_DELETE,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		Long unauth = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		TEST_USER_USER_NAME_DELETE_UNAUTHORIZED,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		Long ideaId = createTestIdea(
		ownerId.toString(),
		topicId.toString(),
		TEST_IDEA_TITLE,
		TEST_IDEA_DESCRIPTION);
		and:"the number of existing ideas must be the same in the end"
		int numIdeas = ideaManager.listIdea().size();

		when:"an unauthorized user tries to delete an idea "
		ideaManager.deleteIdea(
		ideaId.toString(),
		TEST_USER_USER_NAME_DELETE_UNAUTHORIZED);

		then:"the idea must not be deleted"
		
		ideaManager.listIdea().size() == numIdeas;
		thrown(BusinessException) ;

		cleanup:
		cleanupCreateIdea(topicId,ideaId,TEST_USER_USER_NAME_DELETE);
		userManager.deleteUser(TEST_USER_USER_NAME_DELETE_UNAUTHORIZED);
	}

	def "Create test idea, then add comment and verify the comment has been saved"(){

		setup:"the idea must exist in order to coment it"
		Long topicId = createTestTopic(TEST_TOPIC_NAME);
		String author= TEST_USER_USER_NAME_COMMENT_AUTHOR+new Date().time;
		Long ownerId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		TEST_USER_USER_NAME_COMMENT_ADD,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		Long authorId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		author,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		Long ideaId = createTestIdea(
		ownerId.toString(),
		topicId.toString(),
		TEST_IDEA_TITLE,
		TEST_IDEA_DESCRIPTION);

		and:"the coment is made"
		String myComment = "mi comment";
		when:" the user creates a comment"
		Long commentId = createTestComment(
		ideaId.toString(),
		authorId.toString(),
		myComment);
		and:"the comment was saved "
		List<IdeaComment> comments = ideaManager.getComments(ideaId.toString());

		then:"The coment should exist"
		comments[0].comment == myComment;

		cleanup:
		userManager.deleteUser(author);
		cleanupCreateIdea(topicId,ideaId,TEST_USER_USER_NAME_COMMENT_ADD);
		
	}

	def "Create test idea and test comment, then read list of comments"(){

		setup:"the idea must exist in order to coment it"
		String author= TEST_USER_USER_NAME_COMMENT_AUTHOR+new Date().time;
		Long topicId = createTestTopic(TEST_TOPIC_NAME);
		Long ownerId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		TEST_USER_USER_NAME_COMMENT_GET,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		Long authorId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		author,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		Long ideaId = createTestIdea(
		ownerId.toString(),
		topicId.toString(),
		TEST_IDEA_TITLE,
		TEST_IDEA_DESCRIPTION);

		when:"a coment is made"
		Long commentId = createTestComment(
		ideaId.toString(),
		authorId.toString(),
		TEST_COMMENT_TEXT);
		and:"the coment was saved"
		List<IdeaComment> commentList = ideaManager.getComments(ideaId.toString());

		then:"the idea must have a coment"
		!commentList.isEmpty();
		cleanup:
		userManager.deleteUser(author);
		cleanupCreateIdea(topicId,ideaId,TEST_USER_USER_NAME_COMMENT_GET);
		
	}

	def "Create test idea and test comment, attempt to delete comment"(){
		setup:"the idea must exist in order to coment it"
		Long topicId = createTestTopic(TEST_TOPIC_NAME);
		Long ownerId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		TEST_USER_USER_NAME_COMMENT_DELETE,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		Long ideaId = createTestIdea(
		ownerId.toString(),
		topicId.toString(),
		TEST_IDEA_TITLE,
		TEST_IDEA_DESCRIPTION);
		and:"the coment is inserted"
		Long commentId = createTestComment(
		ideaId.toString(),
		ownerId.toString(),
		TEST_COMMENT_TEXT);
		int numComments = ideaManager.getComments(ideaId.toString()).size();

		when:"the user deletes the comment"
		ideaManager.deleteComment(
		commentId.toString(),
		TEST_USER_USER_NAME_COMMENT_DELETE);

		then:"the comment must have been deleted"
		ideaManager.getComments(ideaId.toString()).size() < numComments;
		cleanup:
		cleanupCreateIdea(topicId,ideaId,TEST_USER_USER_NAME_COMMENT_DELETE);
		
	}

	def "Create test idea and test comment, attempt to delete comment with an unauthorized user"(){
		setup:
		String author= TEST_USER_USER_NAME_COMMENT_AUTHOR+new Date().time;
		Long topicId = createTestTopic(TEST_TOPIC_NAME);
		Long ownerId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		author,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		
	   Long ideaId = createTestIdea(
		ownerId.toString(),
		topicId.toString(),
		TEST_IDEA_TITLE,
		TEST_IDEA_DESCRIPTION);
		Long unAuthorId = createTestUser(
		TEST_USER_FIRST_NAME,
		TEST_USER_LAST_NAME,
		TEST_USER_USER_NAME_COMMENT_DELETE,
		TEST_USER_PASSWORD,
		TEST_USER_EMAIL);
		and:"the coment is inserted"
		Long commentId = createTestComment(
		ideaId.toString(),
		ownerId.toString(),
		TEST_COMMENT_TEXT);
		int numComments = ideaManager.getComments(ideaId.toString()).size();

		when:"the user deletes the comment"
		ideaManager.deleteComment(
		commentId.toString(),
		TEST_USER_USER_NAME_COMMENT_DELETE);

		then:"the comment must exist"
		ideaManager.getComments(ideaId.toString()).size() == numComments;
		thrown(BusinessException) ;
		
		cleanup:
		userManager.deleteUser(TEST_USER_USER_NAME_COMMENT_DELETE);
		cleanupCreateIdea(topicId,ideaId,author);
	}



	private def cleanupCreateIdea(Long topicId,Long ideaId, ideaOwner){
		
		ideaManager.deleteIdea(ideaId.toString(), ideaOwner);
		userManager.deleteUser(ideaOwner);
		topicManager.deleteTopic(topicId);
	}

	private Long createTestIdea(String ownerId, String topicId, String title,
	String description) {
		/*
		 *  Find out the number of existing ideas before creating the test
		 *  idea so we can access it by its index.
		 */
		int testIdeaIndex = ideaManager.listIdea().size();
		ideaManager.createIdea(
		ownerId,
		topicId,
		title,
		description);
		Long ideaId =
		ideaManager.listIdea().get(testIdeaIndex).getIdeaiId();

		return ideaId;
	}

	private Long createTestTopic(String name) {
		/*
		 *  Find out the number of existing topics before creating the test
		 *  topic so we can access it by its index.
		 */
		int testTopicIndex = topicManager.listTopics().size();
		topicManager.createTopic(name);
		Long topicId =
		topicManager.listTopics().get(testTopicIndex).getTopicId();

		return topicId;
	}

	private Long createTestUser(String firstname, String lastname, String nickname,
	String password, String email)  {
		// Add test user to database.
		userManager.createUser(
		firstname,
		lastname,
		nickname,
		password,
		email);
		Long ownerId =
		userManager.getUserByUserName(nickname).getUserId();

		return ownerId;
	}

	private Long createTestComment(String ideaId, String userId, String text) {
		/*
		 *  Find out the number of existing comments about the idea before
		 *  creating the test comment so we can access it by its index.
		 */
		int testCommentIndex = ideaManager.getComments(ideaId).size();
		ideaManager.createComment(ideaId, userId, text);
		Long commentId =
		ideaManager.getComments(ideaId).get(testCommentIndex).getCommentId();

		return commentId;
	}
}
