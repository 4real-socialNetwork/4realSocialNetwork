package com.example.marko.areyou4real.model;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private ArrayList<String> listOfUsersInGroup = new ArrayList<>();
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Group(String groupName, ArrayList<String> listOfUsersInGroup) {

        this.groupName = groupName;
        this.listOfUsersInGroup = listOfUsersInGroup;
    }

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
    public void addUserId(String userId){
        listOfUsersInGroup.add(userId);
    }



}
