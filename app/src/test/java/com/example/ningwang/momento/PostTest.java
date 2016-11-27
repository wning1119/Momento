import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Songyan Xie on 2016/11/25.
 */

public class PostTest extends TestCase {
    private static Post post;

    public PostTest(String name) {
        super(name);
    }

    @BeforeClass
    public void setUp() throws Exception {
        post = new Post();
    }


    @Test
    public void testFavorite()
    {
        post.setFavorite(3);
        assertEquals(3, post.getFavorite());
    }

    @Test
    public void testTimeout()
    {
        post.setTimeout(7);
        assertEquals(7, post.getTimeout());
    }

    @Test
    public void testSubject()
    {
        post.setSubject("class");
        assertEquals("class", post.getSubject());
    }

    @Test
    public void testDetail()
    {
        post.setDetail("CS130 project");
        assertEquals("CS130 project", post.getDetail());
    }

    @Test
    public void testLocation()
    {
        post.setLocation(11.01, 12.02);
        assertEquals(11.01, post.getLongitude());
        assertEquals(12.02, post.getLatitude());
    }

    @Test
    public void testOwnerId()
    {
        post.setOwner(3);
        assertEquals(3, post.getOwner());
    }

    @Test
    public void testId()
    {
        post.setId(2);
        assertEquals(2, post.getId());
    }

    @Test
    public void testCategory()
    {
        post.addToCategory("cs");
        post.addToCategory("project");
        assertEquals("cs", post.getCategory().get(0));
        assertEquals("project", post.getCategory().get(1));
    }
}
