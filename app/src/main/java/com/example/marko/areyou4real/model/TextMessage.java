package com.example.marko.areyou4real.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class TextMessage {
    private String mName;
    private String mMessage;
    private String mUid;
    private Date mTimestamp;

    public TextMessage() { } // Needed for Firebase

    public TextMessage(String name, String message, String uid) {
        mName = name;
        mMessage = message;
        mUid = uid;
    }

    public String getName() { return mName; }

    public void setName(String name) { mName = name; }

    public String getMessage() { return mMessage; }

    public void setMessage(String message) { mMessage = message; }

    public String getUid() { return mUid; }

    public void setUid(String uid) { mUid = uid; }

    @ServerTimestamp
    public Date getTimestamp() { return mTimestamp; }

    public void setTimestamp(Date timestamp) { mTimestamp = timestamp; }
}