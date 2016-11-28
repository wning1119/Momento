package com.example.sungyup.cs130;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Sungyup on 11/3/2016.
 */

@SuppressWarnings("serial")
public class MyPosts implements Serializable {
    private int favorite;
    private int timeout;
    private String subject;
    private String detail;
    private long timestamp;
    private double longitude;
    private double latitude;
    private ArrayList<String> category;
    private ArrayList<Integer> replyIds;
    private ArrayList<Reply> replies;
    private String ownerId;
    private int id;

    public MyPosts(int favorite, int timeout, String subject, String detail, long timestamp, double longitude, double latitude, ArrayList<String> category, ArrayList<Integer> replyIds, ArrayList<Reply> replies, String ownerId, int id) {
        this.favorite = favorite;
        this.timeout = timeout;
        this.subject = subject;
        this.detail = detail;
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
        this.category = category;
        this.replyIds = replyIds;
        this.replies = replies;
        this.ownerId = ownerId;
        this.id = id;
    }
    public int getFavorite() {
        return favorite;
    }

    public int getTimeout() {
        return timeout;
    }

    public String getSubject() {
        return subject;
    }

    public String getDetail() {
        return detail;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public ArrayList<Integer> getReplyIds() {
        return replyIds;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public int getId() {
        return id;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public void setReplyIds(ArrayList<Integer> replyIds) {
        this.replyIds = replyIds;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setId(int id) {
        this.id = id;
    }
}