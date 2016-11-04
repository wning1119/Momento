
import org.junit.framework.*;
import java.lang.Object;
import java.util.*;
import org.junit.Test;
import org.junit.BeforeClass

public class MomentoTest extends TestCase
{
	private static LogInActivity login;
	
	// create new test
	public MomentoTest(String name)
	{
		super(name);
	}
	
	@BeforeClass
	public void setUp() throws Exception
	{
		login.initialize();
	}

	// test if a username exists
	@Test
	public void testUserExistence()
	{
		User user = login.getUser();
		// if not null, the user exists
		assertNotNUll(Object, user);
	}
	
	// test if a feedback is empty
	@Test
	public void testFeedbackNotEmpty()
	{
		Feedback feedback = login.getUser().getFeedback();
		if (feedback != null)
		{
			String content = feedback.getContent();
			// if not null, the content of the feedback exists
			assertNotNUll(Object, content);
		}
	}
	
	// test if the content of a post is empty
	@Test
	public void testPostNotEmpty()
	{
		Post post = login.getUser.getPost();
		if (post != null)
		{
			String details = post.getDetails();
			// if not null, the details of a post exists
			assertNotNull(Object, details);
		}
	}
	
	// test if the replies of a post are empty
	@Test
	public void testReplyNotEmpty()
	{
		Post post = login.getUser.getPost();
		if (post != null)
		{
			ArrayList<Reply> reply = post.getReply();
			if (reply.size() != 0)
			{
				// if none of the replies' content is null,
				// the replies of a post are not empty
				for (int i = 0; i < reply.size(); i++)
				{
					String content = reply.get(i).getContent();
					assertNotNull(Object, content);
				}
			}
		}
	}
	
	// test if the username matches the password
	@Test
	public void testUserPasswordMatch()
	{
		boolean match = login.isMatch();
		if (match != null)
		{
			// if true, the username matches the password
			assertTrue(match);
		}
	}
}