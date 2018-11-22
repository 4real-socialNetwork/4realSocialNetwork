package com.example.marko.areyou4real.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class TextMessage {
    private String mName;
    private String mMessage;
    private String mUid;
    private long mTimestamp;
    private String idOfEventOfMessage;
    private String idOfGroupOfMessage;

    public TextMessage() { } // Needed for Firebase

    public TextMessage(String name, String message, String uid,String idOfEventOfMessage,String idOfGroupOfMessage,long mTimestamp) {
        this.mName = name;
        this.mMessage = message;
        this.mUid = uid;
        this.idOfEventOfMessage = idOfEventOfMessage;
        this.idOfGroupOfMessage = idOfGroupOfMessage;
        this.mTimestamp = mTimestamp;
    }

    public String getIdOfGroupOfMessage() {
        return idOfGroupOfMessage;
    }

    public void setIdOfGroupOfMessage(String idOfGroupOfMessage) {
        this.idOfGroupOfMessage = idOfGroupOfMessage;
    }

    public String getIdOfEventOfMessage() {
        return idOfEventOfMessage;
    }

    public void setIdOfEventOfMessage(String idOfEventOfMessage) {
        this.idOfEventOfMessage = idOfEventOfMessage;
    }

    public String getName() { return mName; }

    public void setName(String name) { mName = name; }

    public String getMessage() { return mMessage; }

    public void setMessage(String message) { mMessage = message; }

    public String getUid() { return mUid; }

    public void setUid(String uid) { mUid = uid; }

    public long getTimestamp() { return mTimestamp; }

    public void setTimestamp(long timestamp) { mTimestamp = timestamp; }
}
