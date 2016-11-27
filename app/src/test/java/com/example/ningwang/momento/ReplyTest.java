import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Songyan Xie on 2016/11/25.
 */
public class ReplyTest extends TestCase {
    private static Reply reply;

    public ReplyTest(String name)
    {
        super(name);
    }

    @BeforeClass
    public void setUp() {
        reply = new Reply();
    }

    @Test
    public void testOwnerId() {
        reply.setOwner(3);
        assertEquals(3, reply.getOwner());
    }

    @Test
    public void testContent()
    {
        reply.setContent("time consuming");
        assertEquals("time consuming", reply.getContent());
    }

    @Test
    public void testId()
    {
        reply.setId(1);
        assertEquals(1, reply.getId());
    }
}
