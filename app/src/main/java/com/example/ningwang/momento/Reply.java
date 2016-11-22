import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Songyan Xie on 2016/11/13.
 */
// Reply Class
public class Reply{
    public Reply(User owner, String content)
    {
        this.owner = owner;
        this.content = content;
        Date date = new Date();
        timestamp = new Timestamp(date.getTime());
        id = -1;
    }

    public void printReply()
    {
        System.out.println("reply information");
        System.out.print("owner is: " + owner.getUser_id() + " " + owner.getUser_name() + ". ");
        System.out.print("content is: " + content + ". ");
        System.out.println("timestamp is: " + timestamp + ".");
    }

    public User getOwner() { return owner; }

    public String getContent() { return content; }

    public Timestamp getTimestamp() { return timestamp; }

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    private User owner;
    private String content;
    private Timestamp timestamp;
    private int id;
}
