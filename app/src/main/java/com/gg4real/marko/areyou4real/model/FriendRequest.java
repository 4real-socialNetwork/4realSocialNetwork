package com.gg4real.marko.areyou4real.model;

public class FriendRequest {
    String senderId;
    boolean accepted;
    String senderName;
    String senderDocRef;
    String friendRequestRef;
    String usersFriendsGroup;
    String reciverDocRef;
    String reciverId;
     String requestTimeInMili;

    public String getRequestTimeInMili() {
        return requestTimeInMili;
    }

    public void setRequestTimeInMili(String requestTimeInMili) {
        this.requestTimeInMili = requestTimeInMili;
    }

    public String getReciverId() {
        return reciverId;
    }

    public void setReciverId(String reciverId) {
        this.reciverId = reciverId;
    }

    public String getReciverDocRef() {
        return reciverDocRef;
    }

    public void setReciverDocRef(String reciverDocRef) {
        this.reciverDocRef = reciverDocRef;
    }

    public String getUsersFriendsGroup() {
        return usersFriendsGroup;
    }

    public void setUsersFriendsGroup(String usersFriendsGroup) {
        this.usersFriendsGroup = usersFriendsGroup;
    }

    public String getFriendRequestRef() {
        return friendRequestRef;
    }

    public void setFriendRequestRef(String friendRequestRef) {
        this.friendRequestRef = friendRequestRef;
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

    public FriendRequest(String senderId, boolean accepted,String senderName,String senderDocRef,String friendRequestRef,String reciverDocRef,
                         String reciverId, String requestTimeInMili) {
        this.senderId = senderId;
        this.accepted = accepted;
        this.senderName = senderName;
        this.senderDocRef = senderDocRef;
        this.friendRequestRef = friendRequestRef;
        this.reciverDocRef = reciverDocRef;
        this.reciverId = reciverId;
        this.requestTimeInMili = requestTimeInMili;
    }

    public FriendRequest() {
    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
