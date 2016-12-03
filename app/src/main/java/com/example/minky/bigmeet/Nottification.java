package com.example.minky.bigmeet;

import android.app.Notification;

/**
 * Created by minky on 29/06/2016.
 */
public class Nottification {
    private String title;
    private String body;

    public Nottification(String title,String body){
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
