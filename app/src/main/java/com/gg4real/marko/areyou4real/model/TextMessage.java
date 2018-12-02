package com.gg4real.marko.areyou4real.model;

import java.util.ArrayList;

public class TextMessage {
    private String mName;
    private String mMessage;
    private String mUid;
    private long mTimestamp;
    private String idOfEventOfMessage;
    private String idOfGroupOfMessage;
    private ArrayList<String> usersInChat;

    public TextMessage() { } // Needed for Firebase

    public TextMessage(String name, String message, String uid,String idOfEventOfMessage,String idOfGroupOfMessage,long mTimestamp,ArrayList<String> usersInChat) {
        this.mName = name;
        this.mMessage = message;
        this.mUid = uid;
        this.idOfEventOfMessage = idOfEventOfMessage;
        this.idOfGroupOfMessage = idOfGroupOfMessage;
        this.mTimestamp = mTimestamp;
        this.usersInChat = usersInChat;
    }

    public ArrayList<String> getUsersInChat() {
        return usersInChat;
    }

    public void setUsersInChat(ArrayList<String> usersInChat) {
        this.usersInChat = usersInChat;
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
