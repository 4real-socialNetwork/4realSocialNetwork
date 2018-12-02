package com.gg4real.marko.areyou4real.model;

public class EventRequest {
    private String eventId;
    private String eventActivity;
    private String senderName;
    private String senderDocRef;
    private String reciverDocRef;
    private boolean isAnswered;
    private long eventTime;
    private String eventRequestDocRef;
    private String requestTimeInMili;

    public EventRequest(String eventId,String eventRequestDocRef ,String eventActivity, long eventTime,String senderName, String senderDocRef,
                        String reciverDocRef, boolean isAnswered,String requestTimeInMili) {
        this.eventId = eventId;
        this.eventActivity = eventActivity;
        this.senderName = senderName;
        this.senderDocRef = senderDocRef;
        this.reciverDocRef = reciverDocRef;
        this.isAnswered = isAnswered;
        this.eventTime = eventTime;
        this.eventRequestDocRef = eventRequestDocRef;
        this.requestTimeInMili = requestTimeInMili;
    }

    public String getRequestTimeInMili() {
        return requestTimeInMili;
    }

    public void setRequestTimeInMili(String requestTimeInMili) {
        this.requestTimeInMili = requestTimeInMili;
    }

    public String getEventRequestDocRef() {
        return eventRequestDocRef;
    }

    public void setEventRequestDocRef(String eventRequestDocRef) {
        this.eventRequestDocRef = eventRequestDocRef;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }



    public EventRequest() {
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventActivity() {
        return eventActivity;
    }

    public void setEventActivity(String eventActivity) {
        this.eventActivity = eventActivity;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderDocRef() {
        return senderDocRef;
    }

    public void setSenderDocRef(String senderDocRef) {
        this.senderDocRef = senderDocRef;
    }

    public String getReciverDocRef() {
        return reciverDocRef;
    }

    public void setReciverDocRef(String reciverDocRef) {
        this.reciverDocRef = reciverDocRef;
    }
}
