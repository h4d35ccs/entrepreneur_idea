package test.es.upm.emse.enteridea.persistence.dao.imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import es.upm.emse.enteridea.persistence.dao.GenericDAO;
import es.upm.emse.enteridea.persistence.dao.imp.GenericDAOImp;
import es.upm.emse.enteridea.persistence.entity.Idea;
import es.upm.emse.enteridea.persistence.entity.IdeaComment;
import es.upm.emse.enteridea.persistence.entity.Topic;
import es.upm.emse.enteridea.persistence.entity.User;
import es.upm.emse.enteridea.persistence.entity.Vote;
import es.upm.emse.enteridea.persistence.exception.DaoOperationException;

/**
 * The class <code>GenericDAOImpTest</code> contains tests for the class {@link <code>GenericDAOImp</code>}
 * CREATE USER AND IDEA 1
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro at 19/11/13 3:45
 * 
 * @author ottoabreu
 * 
 * @version $Revision$
 */
public class GenericDAOImpTest extends TestCase {

	private GenericDAO<Idea, Long> daoIdea;
	private GenericDAO<User, Long> daoUser;
	private GenericDAO<Topic, Long> daoTopic;
	private GenericDAO<IdeaComment, Long> daoComment;
	private GenericDAO<Vote, Long> daoVote;

	/**
	 * Construct new test instance
	 * 
	 * @param name
	 *            the test name
	 */
	public GenericDAOImpTest(String name) {
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
		this.daoIdea = new GenericDAOImp<Idea, Long>(Idea.class);
		this.daoUser = new GenericDAOImp<User, Long>(User.class);
		this.daoTopic = new GenericDAOImp<Topic, Long>(Topic.class);
		this.daoComment = new GenericDAOImp<IdeaComment, Long>(
				IdeaComment.class);
		this.daoVote = new GenericDAOImp<Vote, Long>(Vote.class);
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
		this.daoIdea = null;
		this.daoTopic = null;
		this.daoUser = null;
	}

	@Test
	public void testCreateIdea() {
		String nickname = "nickname " + new Date().getTime();
		User user = new User("test name", "test lastname", nickname, "1234",
				"test@test.com", null, null);
		Topic topic = new Topic();
		topic.setTopicDescription("descripcion del topico");
		Idea idea = new Idea();
		idea.setCreationDate(new Date());
		idea.setIdeaComments(null);
		idea.setIdeaOwner(user);
		idea.setTopic(topic);
		idea.setTitle("this is the tittle");
		try {
			this.daoUser.create(user);
			this.daoTopic.create(topic);
			this.daoIdea.create(idea);
		} catch (DaoOperationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			try {
				this.daoUser.delete(user.getUserId());
				this.daoIdea.delete(idea.getIdeaiId());
				this.daoTopic.delete(topic.getTopicId());
			} catch (DaoOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testRead() {
		Idea idea = new Idea();
		try {
			User user = daoUser.read((long) 1);
			Topic topic = daoTopic.read((long) 1);
			idea = new Idea();
			idea.setCreationDate(new Date());
			idea.setIdeaComments(null);
			idea.setIdeaOwner(user);
			idea.setTopic(topic);
			idea.setTitle("this is the tittle");

			this.daoIdea.create(idea);

			idea = this.daoIdea.read(idea.getIdeaiId());
			assertTrue(idea != null);
		} catch (DaoOperationException e) {

			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			try {

				this.daoIdea.delete(idea.getIdeaiId());

			} catch (DaoOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testGetAll() {
		Idea idea = new Idea();
		try {
			User user = daoUser.read((long) 1);
			Topic topic = daoTopic.read((long) 1);
			idea = new Idea();
			idea.setCreationDate(new Date());
			idea.setIdeaComments(null);
			idea.setIdeaOwner(user);
			idea.setTopic(topic);
			idea.setTitle("the title!");
			this.daoIdea.create(idea);
			
			List<Idea> result = this.daoIdea.getAll();
			assertTrue(result != null && result.size() > 0);
		} catch (DaoOperationException e) {

			e.printStackTrace();
			fail(e.getMessage());
		}finally {
			try {

				this.daoIdea.delete(idea.getIdeaiId());
			} catch (DaoOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testCreateIdeaExistingUserAndTopic() {
		Idea idea = new Idea();
		try {
			User user = daoUser.read((long) 1);
			Topic topic = daoTopic.read((long) 1);
			idea = new Idea();
			idea.setCreationDate(new Date());
			idea.setIdeaComments(null);
			idea.setIdeaOwner(user);
			idea.setTopic(topic);
			idea.setTitle("the title!");
			this.daoIdea.create(idea);
		} catch (DaoOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				this.daoIdea.delete(idea.getIdeaiId());
			} catch (DaoOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testCheckIdeaFetchRelationships() {

		Idea idea = new Idea();
		try {
			User user = daoUser.read((long) 1);
			Topic topic = daoTopic.read((long) 1);
			idea.setCreationDate(new Date());
			idea.setIdeaComments(null);
			idea.setIdeaOwner(user);
			idea.setTopic(topic);
			idea.setTitle("the title!");
			this.daoIdea.create(idea);
			idea = daoIdea.read(idea.getIdeaiId());
			assertTrue(idea.getIdeaOwner() != null);
			assertTrue(idea.getTopic() != null);
		} catch (DaoOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}finally {
			try {

				this.daoIdea.delete(idea.getIdeaiId());
			} catch (DaoOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testInsertComment() {
		IdeaComment comment = new IdeaComment();
		Idea idea = new Idea();
		try {
			User user = daoUser.read((long) 1);
			Topic topic = daoTopic.read((long) 1);
			idea.setCreationDate(new Date());
			idea.setIdeaComments(null);
			idea.setIdeaOwner(user);
			idea.setTopic(topic);
			idea.setTitle("the title!");
			this.daoIdea.create(idea);
			idea = daoIdea.read(idea.getIdeaiId());
			

			comment.setCommentOwner(user);
			comment.setIdea(idea);
			comment.setComment("este es mi comentario");
			this.daoComment.create(comment);

		} catch (DaoOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			try {
				this.daoIdea.delete(idea.getIdeaiId());
			} catch (DaoOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testUpdateIdea() {
		Idea idea = new Idea();
		try {
			
			User user = daoUser.read((long) 1);
			Topic topic = daoTopic.read((long) 1);
			idea = new Idea();
			idea.setCreationDate(new Date());
			idea.setIdeaComments(null);
			idea.setIdeaOwner(user);
			idea.setTopic(topic);
			idea.setTitle("this is the tittle");
			this.daoIdea.create(idea);
			idea = daoIdea.read(idea.getIdeaiId());
			idea.setCreationDate(new Date());
			this.daoIdea.update(idea);
		} catch (DaoOperationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}finally{
			try {
				this.daoIdea.delete(idea.getIdeaiId());
			} catch (DaoOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testDeleteIdea() {
		try {
			User user = daoUser.read((long) 1);
			Topic topic = daoTopic.read((long) 1);
			Idea idea = new Idea();
			idea.setCreationDate(new Date());
			idea.setIdeaComments(null);
			idea.setIdeaOwner(user);
			idea.setTopic(topic);
			idea.setTitle("the super title");
			this.daoIdea.create(idea);
			assertTrue(idea.getIdeaiId() > 0);
			this.daoIdea.delete(idea.getIdeaiId());
			Idea fetch = this.daoIdea.read(idea.getIdeaiId());
			assertTrue(fetch == null);

		} catch (DaoOperationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testVoteIdea() {
		Idea idea = new Idea();
		try {
			User user = daoUser.read((long) 1);
			Topic topic = daoTopic.read((long) 1);
			idea.setCreationDate(new Date());
			idea.setIdeaComments(null);
			idea.setIdeaOwner(user);
			idea.setTopic(topic);
			idea.setTitle("this is the tittle");
			this.daoIdea.create(idea);
			
			idea = daoIdea.read(idea.getIdeaiId());
			Vote vote1 = new Vote();
			Vote vote2 = new Vote();
			Vote vote3 = new Vote();

			vote1.setVoteType(Vote.VoteTypes.NEGATIVE.getValue());
			vote1.setIdea(idea);
			vote2.setVoteType(Vote.VoteTypes.NEUTRAL.getValue());
			vote2.setIdea(idea);
			vote3.setVoteType(Vote.VoteTypes.POSITIVE.getValue());
			vote3.setIdea(idea);

			daoVote.create(vote1);
			daoVote.create(vote2);
			daoVote.create(vote3);

		} catch (DaoOperationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}finally{
			try {
				this.daoIdea.delete(idea.getIdeaiId());
			} catch (DaoOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testExecuteQuery() {
		String title = "the new title!!!!! :) ";
		Idea idea = new Idea();
		try {
			User user = daoUser.read((long) 1);
			Topic topic = daoTopic.read((long) 1);

			idea.setCreationDate(new Date());
			idea.setIdeaComments(null);
			idea.setIdeaOwner(user);
			idea.setTopic(topic);
			idea.setTitle(title);
			this.daoIdea.create(idea);

			String query = "FROM " + this.daoIdea.getTableNameFromEntity()
					+ " e WHERE e.title = :title";

			Map<String, String> params = new HashMap<String, String>();
			params.put("title", title);

			List<Idea> list = this.daoIdea
					.getByQuery(query, params, null, null);
			assertTrue((list != null && !list.isEmpty())
					&& list.get(0).getTitle().equals(title));

		} catch (DaoOperationException e) {
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			try {
				this.daoIdea.delete(idea.getIdeaiId());
			} catch (DaoOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testReturnCount() {
		List<Idea> result = null;
		try {
			result = this.daoIdea.getAll();
			int totalSize = result.size();
			assertTrue(totalSize == this.daoIdea.getTotalRecords().intValue());

		} catch (DaoOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Fail returning the total size");
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
 * test.es.upm.emse.enteridea.persistence.dao.imp package.sourceFolder =
 * EnterIdea/src superclassType = junit.framework.TestCase testCase =
 * GenericDAOImpTest testClassType =
 * es.upm.emse.enteridea.persistence.dao.imp.GenericDAOImp
 */