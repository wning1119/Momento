import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Songyan Xie on 2016/11/13.
 */

// Post Class
public class Post {
    public Post()
    {
        favorite = 0;
        timeout = 1;
        subject = null;
        detail = null;
        timestamp = null;
        longitude = null;
        latitude = null;
        category = new ArrayList<>();
        replies = new ArrayList<>();
        owner = null;
        id = -1;
    }

    public void printPost()
    {
        System.out.println("Owner is: " + owner.getUser_name() + " " + owner.getUser_id());
        System.out.println("Favorite # is: " + favorite);
        System.out.println("Timeout is: " + timeout);
        System.out.println("Detail is: " + detail);
        System.out.println("Timestamp is: " + timestamp);
        for (int i = 0; i < replies.size(); i++)
        {
            Reply reply = replies.get(i);
            reply.printReply();
        }
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }

    // get # of favorites
    public int getFavorite() { return favorite; }

    // increase # of favorites
    public void increaseFavorite() { favorite++; }

    // decrease # of favorites
    public void decreaseFavorite()
    {
        if (favorite > 0)
            favorite--;
    }

    // set timeout
    public void setTimeout(int timeout) { this.timeout = timeout; }

    // get timeout
    public int getTimeout() { return timeout; }

    // increase timeout
    public void increaseTimeout() { timeout++; }

    // decrease timeout
    public void decreaseTimeout()
    {
        if (timeout > 0)
            timeout--;
    }

    // set subject
    public void setSubject(String subject) { this.subject = subject; }

    // get subject
    public String getSubject() { return subject; }

    // set detail
    public void setDetail(String detail) { this.detail = detail; }

    // get detail
    public String getDetail() { return detail; }

    // set timestamp
    public void setTimestamp()
    {
        Date date = new Date();
        timestamp = new Timestamp(date.getTime());
    }

    // get timestamp
    public Timestamp getTimestamp() { return timestamp; }

    // set location
    public void setLocation(String longitude, String latitude)
    {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // get longitude
    public String getLongitude() { return longitude; }

    // get latitude
    public String getLatitude() { return latitude; }

    // add contents to category
    public void addToCategory(String content)
    {
        category.add(content);
    }

    // get category
    public ArrayList<String> getCategory()
    {
        return category;
    }

    // add replies
    public void addToReplies(Reply reply)
    {
        replies.add(reply);
    }

    // get replies
    public ArrayList<Reply> getReplies()
    {
        return replies;
    }

    // set id
    public void setId(int id) { this.id = id; }

    // get id
    public int getId() { return id; }

    // private fields
    private int favorite;
    private int timeout;
    private String subject;
    private String detail;
    private Timestamp timestamp;
    private String longitude;
    private String latitude;
    private ArrayList<String> category;
    private ArrayList<Reply> replies;
    private User owner;
    private int id;

}
