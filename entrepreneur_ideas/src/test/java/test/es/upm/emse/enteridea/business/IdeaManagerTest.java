package test.es.upm.emse.enteridea.business;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.upm.emse.enteridea.business.IdeaManager;
import es.upm.emse.enteridea.business.TopicManager;
import es.upm.emse.enteridea.business.UserManager;
import es.upm.emse.enteridea.business.exception.BusinessException;
import es.upm.emse.enteridea.persistence.entity.Idea;
import es.upm.emse.enteridea.persistence.entity.IdeaComment;

public class IdeaManagerTest {

	private IdeaManager ideaManager;
	private static final String TEST_IDEA_TITLE = "Title";
	private static final String TEST_IDEA_DESCRIPTION = "Description";
	
	private TopicManager topicManager;
	private static final String TEST_TOPIC_NAME = "Topic";
	
	private UserManager userManager;
	private static final String TEST_USER_FIRST_NAME = "Jane";
	private static final String TEST_USER_LAST_NAME = "McTester";
	private static final String TEST_USER_USER_NAME = "___mctesty___"+new Date().getTime();
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

	@Before
	public void setUp() throws Exception {
		ideaManager = new IdeaManager();
		topicManager = new TopicManager();
		userManager = new UserManager();
	}

	@After
	public void tearDown() throws Exception {
		ideaManager = null;
		topicManager = null;
		userManager = null;
	}

	@Test
	public void testCreateIdea() throws BusinessException {

		/*
		 * Create test idea and verify its creation in the database.
		 * 
		 * NOTE:
		 *  - A test topic must be created in order to be able to create
		 *    the idea with an existing topic.
		 *  - A test user must be created in order to be able to assign
		 *    the idea to an existing user.
		 * 
		 * Do not forget to delete all 3 test elements (test idea, user and topic)
		 * afterwards!
		 */
		try {
			Long topicId = createTestTopic(TEST_TOPIC_NAME);
			Long ownerId = createTestUser(
					TEST_USER_FIRST_NAME,
					TEST_USER_LAST_NAME,
					TEST_USER_USER_NAME_CREATE,
					TEST_USER_PASSWORD,
					TEST_USER_EMAIL);
			Long ideaId = createTestIdea(
					ownerId.toString(),
					topicId.toString(),
					TEST_IDEA_TITLE,
					TEST_IDEA_DESCRIPTION);
			
			Idea idea = ideaManager.getIdea(ideaId.toString());
			assertTrue(null != idea);
			assertTrue(ownerId == idea.getIdeaOwner().getUserId());
			assertTrue(topicId == idea.getTopic().getTopicId());
			assertTrue(TEST_IDEA_TITLE.equalsIgnoreCase(idea.getTitle()));
			assertTrue(TEST_IDEA_DESCRIPTION.equalsIgnoreCase(idea.getDescription()));

			ideaManager.deleteIdea(ideaId.toString(), TEST_USER_USER_NAME_CREATE);
			userManager.deleteUser(TEST_USER_USER_NAME_CREATE);
			topicManager.deleteTopic(topicId);
			
			
		} catch (BusinessException e) {
			fail("Failure creating the list.");
		}

	}

	@Test
	public void testListIdea() {
		/*
		 * Create test idea, read list of ideas from the database
		 * and verify it is not empty.
		 * 
		 * NOTE:
		 *  - A test topic must be created in order to be able to create
		 *    the idea with an existing topic.
		 *  - A test user must be created in order to be able to assign
		 *    the idea to an existing user.
		 * 
		 * Do not forget to delete all 3 test elements (test idea, user and topic)
		 * afterwards!
		 */
		try {
			Long topicId = createTestTopic(TEST_TOPIC_NAME);
			Long ownerId = createTestUser(
					TEST_USER_FIRST_NAME,
					TEST_USER_LAST_NAME,
					TEST_USER_USER_NAME_LIST,
					TEST_USER_PASSWORD,
					TEST_USER_EMAIL);
			Long ideaId = createTestIdea(
					ownerId.toString(),
					topicId.toString(),
					TEST_IDEA_TITLE,
					TEST_IDEA_DESCRIPTION);
			
			List<Idea> ideas = ideaManager.listIdea();
			assertTrue(!ideas.isEmpty());

			ideaManager.deleteIdea(ideaId.toString(), TEST_USER_USER_NAME_LIST);
			userManager.deleteUser(TEST_USER_USER_NAME_LIST);
			topicManager.deleteTopic(topicId);
			// TODO(PS, Functionality for deleting topic does not exist.")

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Failure getting the list of the ideas.");
		}

	}

	@Test
	public void testEditIdea() throws BusinessException {
		
		/*
		 * Create test idea, then edit idea and verify the change has been saved.
		 * 
		 * NOTE:
		 *  - A test topic must be created in order to be able to create
		 *    the idea with an existing topic.
		 *  - A test user must be created in order to be able to assign
		 *    the idea to an existing user.
		 * 
		 * Do not forget to delete all 3 test elements (test idea, user and topic)
		 * afterwards!
		 */
		try {
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
			
			String title2 = "hola titulo";
			ideaManager.editIdea(ideaId.toString(), title2, "description", TEST_USER_USER_NAME_EDIT);
			Idea theIdea2 = ideaManager.getIdea(ideaId.toString());
			String title = theIdea2.getTitle();
			assertTrue(title2.equals(title));

			ideaManager.deleteIdea(ideaId.toString(), TEST_USER_USER_NAME_EDIT);
			userManager.deleteUser(TEST_USER_USER_NAME_EDIT);
			topicManager.deleteTopic(topicId);

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Failure editing idea.");
		}

	}

	@Test
	public void testDeleteIdea() throws BusinessException {
		
		/*
		 * Create test idea, attempt to delete idea with an unauthorized user
		 * and verify no changes have been made. Then attempt to delete idea
		 * with an authorized user and ensure it succeeds.
		 * 
		 * NOTE:
		 *  - A test topic must be created in order to be able to create
		 *    the idea with an existing topic.
		 *  - A test user must be created in order to be able to assign
		 *    the idea to an existing user.
		 * 
		 * Do not forget to delete all 3 test elements (test idea, user and topic)
		 * afterwards!
		 */
		try {
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
			
			int numIdeas = ideaManager.listIdea().size();

			try {
				ideaManager.deleteIdea(
						ideaId.toString(),
						TEST_USER_USER_NAME_DELETE_UNAUTHORIZED);
				
				/*
				 * If test reaches this line, it means an unauthorized user
				 * was able to delete the idea.
				 */
				fail("Failure deleting idea the unauthorized user have deleted the idea");
			} catch (BusinessException e) {
				assertTrue(ideaManager.listIdea().size() == numIdeas);
			}
			
			ideaManager.deleteIdea(ideaId.toString(), TEST_USER_USER_NAME_DELETE);
			assertTrue(ideaManager.listIdea().size() == numIdeas - 1);
			
			try {
			
				Idea idea = ideaManager.getIdea(ideaId.toString());
				
				// If test reaches this line, it means idea was not deleted.
				assertTrue(idea == null);
			} catch (BusinessException e) {
				assertTrue(false);
			}
			
			userManager.deleteUser(TEST_USER_USER_NAME_DELETE);
			userManager.deleteUser(TEST_USER_USER_NAME_DELETE_UNAUTHORIZED);
			topicManager.deleteTopic(topicId);
		

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Failure deleting idea."+ e);
		}

	}

	@Test
	public void testCreateComment() throws BusinessException {

		/*
		 * Create test idea, then add comment and verify the comment has been saved.
		 * 
		 * NOTE:
		 *  - A test topic must be created in order to be able to create
		 *    the idea with an existing topic.
		 *  - A test user must be created in order to be able to assign
		 *    the idea to an existing user.
		 * 
		 * Do not forget to delete all 4 test elements (test idea, user, topic,
		 * and comment) afterwards!
		 */
		try {
			Long topicId = createTestTopic(TEST_TOPIC_NAME);
			Long ownerId = createTestUser(
					TEST_USER_FIRST_NAME,
					TEST_USER_LAST_NAME,
					TEST_USER_USER_NAME_COMMENT_ADD,
					TEST_USER_PASSWORD,
					TEST_USER_EMAIL);
			Long authorId = createTestUser(
					TEST_USER_FIRST_NAME,
					TEST_USER_LAST_NAME,
					TEST_USER_USER_NAME_COMMENT_AUTHOR,
					TEST_USER_PASSWORD,
					TEST_USER_EMAIL);
			Long ideaId = createTestIdea(
					ownerId.toString(),
					topicId.toString(),
					TEST_IDEA_TITLE,
					TEST_IDEA_DESCRIPTION);
			
			String myComment = "mi comment";

			Long commentId = createTestComment(
					ideaId.toString(),
					authorId.toString(),
					myComment);
			
			List<IdeaComment> comments = ideaManager.getComments(ideaId.toString());

			for (IdeaComment comment : comments) {
				String commmentID = "" + comment.getCommentId();

				if ((!commmentID.equals(commentId.toString())) ||
					(!comment.getComment().equals(myComment))) {
					fail("Failure creating comment.");
				}
			}

			// Deleting idea also delete any related comments.
			ideaManager.deleteIdea(ideaId.toString(), TEST_USER_USER_NAME_COMMENT_ADD);
			userManager.deleteUser(TEST_USER_USER_NAME_COMMENT_AUTHOR);
			userManager.deleteUser(TEST_USER_USER_NAME_COMMENT_ADD);
			topicManager.deleteTopic(topicId);

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Failure creating comment.");
		}
	}

	@Test
	public void testGetComments() throws BusinessException {

		/*
		 * Create test idea and test comment, then read list of comments
		 * from the database and verify it is not empty.
		 * 
		 * NOTE:
		 *  - A test topic must be created in order to be able to create
		 *    the idea with an existing topic.
		 *  - A test user must be created in order to be able to assign
		 *    the idea to an existing user.
		 * 
		 * Do not forget to delete all 4 test elements (test idea, user, topic,
		 * and comment) afterwards!
		 */
		try {
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
					TEST_USER_USER_NAME_COMMENT_AUTHOR,
					TEST_USER_PASSWORD,
					TEST_USER_EMAIL);
			Long ideaId = createTestIdea(
					ownerId.toString(),
					topicId.toString(),
					TEST_IDEA_TITLE,
					TEST_IDEA_DESCRIPTION);

			@SuppressWarnings("unused")
			Long commentId = createTestComment(
					ideaId.toString(),
					authorId.toString(),
					TEST_COMMENT_TEXT);
			
			List<IdeaComment> commentList = ideaManager.getComments(ideaId.toString());

			assertTrue(!commentList.isEmpty());

			// Deleting idea also delete any related comments.
			ideaManager.deleteIdea(ideaId.toString(), TEST_USER_USER_NAME_COMMENT_GET);
			userManager.deleteUser(TEST_USER_USER_NAME_COMMENT_AUTHOR);
			userManager.deleteUser(TEST_USER_USER_NAME_COMMENT_GET);
			topicManager.deleteTopic(topicId);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Failure getting comments.");
		}

	}

	@Test
	public void testDeleteComment() throws BusinessException {
		
		/*
		 * Create test idea and test comment, attempt to delete comment with
		 * an unauthorized user and verify no changes have been made. Then
		 * attempt to delete idea with an authorized user and ensure it succeeds.
		 * 
		 * NOTE:
		 *  - A test topic must be created in order to be able to create
		 *    the idea with an existing topic.
		 *  - A test user must be created in order to be able to assign
		 *    the idea to an existing user.
		 * 
		 * Do not forget to delete all 4 test elements (test idea, user, topic,
		 * and comment) afterwards!
		 */
		try {
			
			Long topicId = createTestTopic(TEST_TOPIC_NAME);
			Long ownerId = createTestUser(
					TEST_USER_FIRST_NAME,
					TEST_USER_LAST_NAME,
					TEST_USER_USER_NAME_COMMENT_DELETE,
					TEST_USER_PASSWORD,
					TEST_USER_EMAIL);
			Long authorId = createTestUser(
					TEST_USER_FIRST_NAME,
					TEST_USER_LAST_NAME,
					TEST_USER_USER_NAME_COMMENT_AUTHOR,
					TEST_USER_PASSWORD,
					TEST_USER_EMAIL);
			Long ideaId = createTestIdea(
					ownerId.toString(),
					topicId.toString(),
					TEST_IDEA_TITLE,
					TEST_IDEA_DESCRIPTION);
			Long commentId = createTestComment(
					ideaId.toString(),
					authorId.toString(),
					TEST_COMMENT_TEXT);
			
			int numComments = ideaManager.getComments(ideaId.toString()).size();

			try {
				ideaManager.deleteComment(
						commentId.toString(),
						TEST_USER_USER_NAME_DELETE_UNAUTHORIZED);
				
				/*
				 * If test reaches this line, it means an unauthorized user
				 * was able to delete the comment.
				 */
				fail("Failure deleting comment.");
			} catch (BusinessException e) {
				assertTrue(ideaManager.getComments(ideaId.toString()).size() == numComments);
			}
			
			ideaManager.deleteComment(commentId.toString(), TEST_USER_USER_NAME_COMMENT_AUTHOR);
			assertTrue(ideaManager.getComments(ideaId.toString()).size() == numComments - 1);
			
			ideaManager.deleteIdea(ideaId.toString(), TEST_USER_USER_NAME_COMMENT_DELETE);
			userManager.deleteUser(TEST_USER_USER_NAME_COMMENT_AUTHOR);
			userManager.deleteUser(TEST_USER_USER_NAME_COMMENT_DELETE);
			topicManager.deleteTopic(topicId);

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Failure deleting comment.");
		}

	}
	
	private Long createTestIdea(String ownerId, String topicId, String title,
			String description) throws BusinessException {
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
	
	private Long createTestTopic(String name) throws BusinessException {
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
			String password, String email) throws BusinessException {
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
	
	private Long createTestComment(String ideaId, String userId, String text)
			throws BusinessException {
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
