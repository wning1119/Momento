package com.example.ningwang.momento;

import java.lang.reflect.Array;
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
        longitude = 0.0;
        latitude = 0.0;
        category = new ArrayList<>();
        replies = new ArrayList<>();
        ownerId = "";
        id = -1;
    }

    public Post(int favorite, int timeout, String subject, String detail,
                Timestamp timestamp, double longitude, double latitude,
                ArrayList<String> category, ArrayList<Reply> replies,
                String ownerId, int id)
    {
        this.favorite = favorite;
        this.timeout = timeout;
        this.subject = subject;
        this.detail = detail;
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
        this.replies = replies;
        this.ownerId = ownerId;
        this.id = id;
    }

    public void setOwner(String ownerId)
    {
        this.ownerId = ownerId;
    }

    public String getOwner() { return ownerId; }

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

    public void setFavorite(int favorite) { this.favorite = favorite; }

    public void setCategory(ArrayList<String> category) { this.category = category; }

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
    public void setLocation(double longitude, double latitude)
    {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // get longitude
    public double getLongitude() { return longitude; }

    // get latitude
    public double getLatitude() { return latitude; }

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
    private double longitude;
    private double latitude;
    private ArrayList<String> category;
    private ArrayList<Reply> replies;
    private String ownerId;
    private int id;

}
