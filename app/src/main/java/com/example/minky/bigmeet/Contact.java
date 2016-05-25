package com.example.minky.bigmeet;

/**
 * Created by minky on 23/05/2016.
 */
public class Contact {

    private int id;
    private String name;
    private String emailAddress;

    public Contact(){
        //default constructor
    }

    public Contact(String name, int id, String emailAddress){
        this.id = id;
        this.name = name;
        this.emailAddress = emailAddress;
    }

    public Contact(String name, String emailAddress){
        this.name = name;
        this.emailAddress = emailAddress;
    }

    public Contact(String name, int id){
        this.id = id;
        this.name = name;
    }

    public Contact(int id, String emailAddress) {
        this.id = id;
        this.emailAddress = emailAddress;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getEmailAddress(){
        return emailAddress;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmailAddress(String emailAddress){
        this.emailAddress = emailAddress;
    }
}
