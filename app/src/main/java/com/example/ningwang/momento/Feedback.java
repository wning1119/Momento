package com.example.ningwang.momento;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Songyan Xie on 2016/11/13.
 */
// Feedback Class
public class Feedback{
    public Feedback(String content)
    {
        this.content = content;
        Date date = new Date();
        timestamp = new Timestamp(date.getTime());
    }

    public String getContent()
    {
        return content;
    }

    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    private String content;
    private Timestamp timestamp;
}