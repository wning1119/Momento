package com.example.ningwang.momento;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;

/**
 * Created by Songyan Xie on 2016/11/13.
 */

// Post Class
@DynamoDBTable(tableName = "Posts")
public class Post {
    // private fields
    private int favorite;
    private int timeout;
    private String subject;
    private String detail;
    private Timestamp timestamp;
    private double longitude;
    private double latitude;
    private ArrayList<String> category;
    private ArrayList<int> replyIds;
    private ArrayList<Reply> replies;
    private String ownerId;
    private int id;

    @DynamoDBHashKey(attributeName = "postId")
    // get id
    public int getId() { return id; }
    // set id
    public void setId(int id) { this.id = id; }

    @DynamoDBAttribute(attributeName = "Timestamp")
    // get timestamp
    public Timestamp getTimestamp() { return timestamp; }
    // set timestamp
    public void setTimestamp()
    {
        Date date = new Date();
        tp = new Timestamp(date.getTime());
        this.timestamp = tp;
    }

    @DynamoDBAttribute(attributeName = "category")
    // get category
    public ArrayList<String> getCategory() { return category; }
    public void setCategory(ArrayList<String> category) { this.category = category; }
    // add contents to category
    public void addToCategory(String content)
    {
        ArrayList<String> currCategories = getCategory();
        currCategories.add(content);
        setCategory(currCategories);
    }

    @DynamoDBAttribute(attributeName = "detail")
    // get detail
    public String getDetail() { return detail; }
    // set detail
    public void setDetail(String detail) { this.detail = detail; }

    @DynamoDBAttribute(attributeName = "favorite")
    // get # of favorites
    public int getFavorite() { return favorite; }
    // set # of favorites
    public void setFavorite(int favorite) { this.favorite = favorite; }
    // increase # of favorites
    public void increaseFavorite()
    {
        int currFav = getFavorite();
        currFav++;
        setFavorite(currFav);
    }
    // decrease # of favorites
    public void decreaseFavorite()
    {
        int currFav = getFavorite();
        if (currFav > 0)
            currFav--;
        setFavorite(currFav);
    }

    @DynamoDBAttribute(attributeName = "latitude")
    // get latitude
    public double getLatitude() { return latitude; }
    // set longitude
    public void setLatitude(double latitude) { this.latitude = latitude; }

    @DynamoDBAttribute(attributeName = "longitude")
    // get longitude
    public double getLongitude() { return longitude; }
    // set longitude
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @DynamoDBAttribute(attributeName = "ownerId")
    public String getOwner() { return ownerId; }
    public void setOwner(String ownerId) { this.ownerId = ownerId; }

    @DynamoDBAttribute(attributeName = "replyIds")
    public ArrayList<int> getReplyIds() { return replyIds; }
    public void setReplyIds() { this.replyIds = new ArrayList<>(); }
    public int addReplyId(int id)
    {
        ArrayList<int> currIds = getReplyIds();
        currIds.add(id);
        setReplyIds(currIds);
    }

    // get replies
    public ArrayList<Reply> getReplies() { return replies; }
    public void setReplies() {this.replies = new ArrayList<>(); }
    // add replies
    public void addToReplies(Reply reply)
    {
        replies.add(reply);
        addReplyId(reply.getId());
    }

    @DynamoDBAttribute(attributeName = "subject")
    // get subject
    public String getSubject() { return subject; }
    // set subject
    public void setSubject(String subject) { this.subject = subject; }

    @DynamoDBAttribute(attributeName = "timeout")
    // get timeout
    public int getTimeout() { return timeout; }
    // set timeout
    public void setTimeout(int timeout) { this.timeout = timeout; }

    // increase timeout
    public void increaseTimeout()
    {
        int currTimeout = getTimeout();
        currTimeout++;
        setTimeout(currTimeout);
    }

    // decrease timeout
    public void decreaseTimeout()
    {
        int currTimeout = getTimeout();
        if (currTimeout > 0)
            currTimeout--;
        setTimeout(currTimeout);
    }
}
