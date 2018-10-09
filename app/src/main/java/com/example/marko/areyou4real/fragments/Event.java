package com.example.marko.areyou4real.fragments;

public class Event {
    private String name;
    private String activity;
    private String eventDescription;
    private int time;
    private int latitude;
    private int langitude;
    private int usersNeeded;
    private int usersEntered;
    private String eventId;

    public  Event (){

    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Event(String name, String activity, int time, int latitude, int langitude, int usersNeeded, String eventDescription) {
        this.name = name;
        this.activity = activity;
        this.time = time;
        this.latitude = latitude;
        this.langitude = langitude;
        this.usersNeeded = usersNeeded;
        this.eventDescription=eventDescription;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getLangitude() {
        return langitude;
    }

    public void setLangitude(int langitude) {
        this.langitude = langitude;
    }

    public int getUsersNeeded() {
        return usersNeeded;
    }

    public void setUsersNeeded(int usersNeeded) {
        this.usersNeeded = usersNeeded;
    }

    public int getUsersEntered() {
        return usersEntered;
    }

    public void setUsersEntered(int usersEntered) {
        this.usersEntered = usersEntered;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
