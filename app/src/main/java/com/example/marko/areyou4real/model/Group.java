package com.example.marko.areyou4real.model;

import android.widget.ImageView;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private ArrayList<String> listOfUsersInGroup = new ArrayList<>();
    private int numberOfusersInGroup;
    private ImageView groupPicture;
    private String groupId;

    public Group() {
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<String> getListOfUsersInGroup() {
        return listOfUsersInGroup;
    }

    public void setListOfUsersInGroup(ArrayList<String> listOfUsersInGroup) {
        this.listOfUsersInGroup = listOfUsersInGroup;
    }

    public int getNumberOfusersInGroup() {
        return numberOfusersInGroup;
    }

    public void setNumberOfusersInGroup(int numberOfusersInGroup) {
        this.numberOfusersInGroup = numberOfusersInGroup;
    }

    public ImageView getGroupPicture() {
        return groupPicture;
    }

    public void setGroupPicture(ImageView groupPicture) {
        this.groupPicture = groupPicture;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Group(String groupName, ArrayList<String> listOfUsersInGroup, int numberOfusersInGroup, ImageView groupPicture, String groupId) {
        this.groupName = groupName;
        this.listOfUsersInGroup = listOfUsersInGroup;
        this.numberOfusersInGroup = numberOfusersInGroup;
        this.groupPicture = groupPicture;
        this.groupId = groupId;
    }

    public void addUserToGroup(String userId) {
        listOfUsersInGroup.add(userId);
    }

    public void exitGroup(String userId) {
        listOfUsersInGroup.remove(userId);
    }
}
