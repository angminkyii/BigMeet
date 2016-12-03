package com.example.minky.bigmeet;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by minky on 23/05/2016.
 */
public class Group{
    private int id;
    private String name;
    private ArrayList<Contact> members;
    private byte[] groupPhoto;

    public Group(){
        //default constructor
    }

    public Group(String name, int id, ArrayList<Contact> members, byte[] groupPhoto){
        this.id = id;
        this.name = name;
        this.members = members;
        this.groupPhoto = groupPhoto;
    }

    public Group(String name, ArrayList<Contact> members,  byte[] groupPhoto){
        this.name = name;
        this.members = members;
        this.groupPhoto = groupPhoto;
    }

    public Group(String name, int id){
        this.id = id;
        this.name = name;
    }

    public Group(int id, ArrayList<Contact> members) {
        this.id = id;
        this.members = members;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public ArrayList<Contact> getMembers(){
        return this.members;
    }

    public List<String> getMembersInString(){
        ArrayList<String> membersString = new ArrayList<>();
        for(int i=0;i<members.size();i++){
            membersString.add(members.get(i).getName());
        }
        return membersString;
    }

    public void setMembers(ArrayList<Contact> members){
        this.members = members;
    }

    public byte[] getGroupPhoto(){
        return groupPhoto;
    }

    public void setGroupPhoto(byte[] groupPhoto) {
        this.groupPhoto = groupPhoto;
    }
}
