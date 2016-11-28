package com.example.ningwang.momento;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Songyan Xie on 2016/11/13.
 */
// Reply Class
public class Reply{
    public Reply(int ownerId, String content)
    {
        this.ownerId = ownerId;
        this.content = content;
        Date date = new Date();
        timestamp = new Timestamp(date.getTime());
        id = -1;
    }

    public Reply()
    {
        this.ownerId = -1;
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

    public void setOwner(int ownerId) { this.ownerId = ownerId; }

    public int getOwner() { return ownerId; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public Timestamp getTimestamp() { return timestamp; }

    public void setId(int id) { this.id = id; }

    public int getId() { return id; }

    private int ownerId;
    private String content;
    private Timestamp timestamp;
    private int id;
}