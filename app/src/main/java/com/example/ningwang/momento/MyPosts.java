package com.example.ningwang.momento;

/**
 * Created by Sungyup on 11/3/2016.
 */

public class MyPosts {
    private String subject;
    private String content;
    private String date;

    public MyPosts(String subject, String content, String date){
        this.subject = subject;
        this.content = content;
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public String getSubject() {
        return subject;
    }

    public String getDate(){
        return date;
    }
}
