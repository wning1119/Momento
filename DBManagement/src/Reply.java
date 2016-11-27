import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Songyan Xie on 2016/11/13.
 */
// Reply Class
public class Reply{
    public Reply(String user_id, String content)
    {
        this.ownerId = user_id;
        this.content = content;
        Date date = new Date();
        timestamp = new Timestamp(date.getTime());
        id = -1;
    }

    public Reply()
    {
        this.ownerId = null;
        this.content = null;
        Date date = new Date();
        timestamp = new Timestamp(date.getTime());
        id = -1;
    }

    public void setTimestamp()
    {
        Date date = new Date();
        timestamp = new Timestamp(date.getTime());
    }

    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    public void setOwner(String ownerId) { this.ownerId = ownerId; }

    public String getOwner() { return ownerId; }

    public String getContent() { return content; }

    public Timestamp getTimestamp() { return timestamp; }

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    private String ownerId;
    private String content;
    private Timestamp timestamp;
    private int id;
}
