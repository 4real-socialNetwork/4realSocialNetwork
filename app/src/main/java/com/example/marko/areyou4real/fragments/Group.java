package com.example.marko.areyou4real.fragments;

import android.widget.ImageView;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private ArrayList<String> listOfUsersInGroup = new ArrayList<>();

    public Group() {
    }
    public Group(String groupName, ArrayList<String> listOfUsersInGroup, ImageView groupPicture, String groupId) {
        this.groupName = groupName;
        this.listOfUsersInGroup = listOfUsersInGroup;

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
    public void addUserId(String userId){
        listOfUsersInGroup.add(userId);
    }
}
