package com.example.ningwang.momento;

import java.sql.Timestamp;
import java.util.Date;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;


/**
 * Created by Songyan Xie on 2016/11/13.
 */
// Reply Class

@DynamoDBTable(tableName = "Replies")
public class Reply{
    private String ownerId;
    private String content;
    private Timestamp timestamp;
    private int id;

    @DynamoDBHashKey(attributeName = "replyId")
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @DynamoDBAttribute(attributeName = "content")
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    @DynamoDBAttribute(attributeName = "ownerId")
    public String getOwner() { return ownerId; }
    public void setOwner(String ownerId) { this.ownerId = ownerId; }

    @DynamoDBAttribute(attributeName = "Timestamp")
    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp()
    {
        Date date = new Date();
        tp = new Timestamp(date.getTime());
        this.timestamp = tp;
    }
}
