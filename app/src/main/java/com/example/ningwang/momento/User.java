import java.util.ArrayList;

/**
 * Created by Songyan Xie on 2016/11/13.
 */
// User Class
public class User{
    public User()
    {
        user_id = 0;
        user_name = null;
        favorite_posts = new ArrayList<>();
        my_posts = new ArrayList<>();
    }

    public int getUser_id()
    {
        return user_id;
    }

    public String getUser_name()
    {
        return user_name;
    }

    public void setUserInfo(int user_id, String user_name)
    {
        this.user_id = user_id;
        this.user_name = user_name;
    }

    public Post createNewPost(String content)
    {
        Post post = new Post();
        post.setDetail(content);
        post.setTimestamp();
        post.setOwner(this);
        my_posts.add(post);
        return post;
    }

    public void favoritePost(Post post)
    {
        post.increaseFavorite();
        post.increaseTimeout();
        favorite_posts.add(post);
    }

    public Feedback submitFeedback(String content)
    {
        return new Feedback(content);
    }

    public void replyToPost(Post post, String content)
    {
        Reply reply = new Reply(this, content);
        post.addToReplies(reply);
    }

    public ArrayList<Post> getMy_posts()
    {
        return my_posts;
    }

    public ArrayList<Post> getFavorite_posts()
    {
        return favorite_posts;
    }

    private int user_id;
    private String user_name;
    private ArrayList<Post> favorite_posts;
    private ArrayList<Post> my_posts;
}

