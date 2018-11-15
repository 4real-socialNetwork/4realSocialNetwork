package com.example.marko.areyou4real.model;

import android.widget.ImageView;

import com.google.firebase.firestore.DocumentReference;

public class FriendRequest {
    String senderId;
    boolean accepted;
    String senderName;
    DocumentReference senderDocRef;
    String friendRequestRef;
    String usersFriendsGroup;

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


    public DocumentReference getSenderDocRef() {
        return senderDocRef;
    }

    public void setSenderDocRef(DocumentReference senderDocRef) {
        this.senderDocRef = senderDocRef;
    }

    public FriendRequest(String senderId, boolean accepted,String senderName,DocumentReference senderDocRef,String friendRequestRef,String usersFriendsGroup) {
        this.senderId = senderId;
        this.accepted = accepted;
        this.senderName = senderName;
        this.senderDocRef = senderDocRef;
        this.friendRequestRef = friendRequestRef;
        this.usersFriendsGroup = usersFriendsGroup;
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
