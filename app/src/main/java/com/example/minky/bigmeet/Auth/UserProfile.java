package com.example.minky.bigmeet.Auth;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

/**
 * Created by Shan on 6/23/2016.
 */
@IgnoreExtraProperties
public class UserProfile {

    public String email;
    HashMap<String, String> notificationToken=new HashMap<String, String>();

    public UserProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserProfile(String email, HashMap<String, String> notificationTokenMap) {
        this.email = email;
        this.notificationToken = notificationTokenMap;
    }
}
