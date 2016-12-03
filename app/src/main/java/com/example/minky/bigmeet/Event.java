package com.example.minky.bigmeet;

import java.util.HashMap;
import java.util.List;

public class Event {

    private String eventName;
    private String status;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String duration;
    private String organizerUID ;
    private String groupGkey;
    private String eventICS ;
    private String location;
    private List<Attendees> attendees;

    public Event(HashMap<String,String> hashMap){
        setDuration(hashMap.get("eventDuration"));
        setEventName(hashMap.get("eventName"));
        setEndDate(hashMap.get("endDate"));
        setStartDate(hashMap.get("startDate"));
        setStartTime(hashMap.get("startTime"));
        setEndTime(hashMap.get("endTime"));
        setLocation(hashMap.get("venue"));
        setStatus("NEW");
        setEventICS("");
        setOrganizerUID("");
        setGroupGkey(hashMap.get("groupGKey"));
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOrganizerUID() {
        return organizerUID;
    }

    public void setOrganizerUID(String organizerUID) {
        this.organizerUID = organizerUID;
    }

    public String getGroupGkey() {
        return groupGkey;
    }

    public void setGroupGkey(String groupGkey) {
        this.groupGkey = groupGkey;
    }

    public String getEventICS() {
        return eventICS;
    }

    public void setEventICS(String eventICS) {
        this.eventICS = eventICS;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Attendees> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<Attendees> attendees) {
        this.attendees = attendees;
    }
}
