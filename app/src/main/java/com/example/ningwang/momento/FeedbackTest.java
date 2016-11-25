import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by Think on 2016/11/25.
 */
public class FeedbackTest extends TestCase {
    private static Feedback feedback;

    @BeforeClass
    public void setUp()
    {
        feedback = new Feedback("a");
    }

    @Test
    public void testContent()
    {
        assertEquals("a", feedback.getContent());
    }
}
