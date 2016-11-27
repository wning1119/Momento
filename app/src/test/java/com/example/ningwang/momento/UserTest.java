import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Songyan Xie on 2016/11/25.
 */
public class UserTest extends TestCase {
    private static User user;

    public UserTest(String name)
    {
        super(name);
    }

    @BeforeClass
    public void setUp()
    {
        user = new User();
    }

    @Test
    public void testUserInfo()
    {
        user.setUserInfo(1, "a");
        assertEquals(1, user.getUser_id());
        assertEquals("a", user.getUser_name());
    }
}
