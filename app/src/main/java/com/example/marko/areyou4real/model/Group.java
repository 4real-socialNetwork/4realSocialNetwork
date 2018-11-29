package com.example.marko.areyou4real.model;

import java.util.ArrayList;

public class Group {
    private String groupName;
    private ArrayList<String> listOfUsersInGroup = new ArrayList<>();
    private String groupId;
    private String groupAdmin;
    private boolean firstGroup;
    private boolean isSelected;
    private String groupPictureUrl;

    public String getGroupPictureUrl() {
        return groupPictureUrl;
    }

    public void setGroupPictureUrl(String groupPictureUrl) {
        this.groupPictureUrl = groupPictureUrl;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isFirstGroup() {
        return firstGroup;
    }

    public void setFirstGroup(boolean firstGroup) {
        this.firstGroup = firstGroup;
    }

    public Group(String groupName, ArrayList<String> listOfUsersInGroup, String groupId, String groupAdmin, boolean firstGroup,String groupPictureUrl) {
        this.groupName = groupName;
        this.listOfUsersInGroup = listOfUsersInGroup;
        this.groupId = groupId;
        this.groupAdmin = groupAdmin;
        this.firstGroup = firstGroup;
        this.groupPictureUrl = groupPictureUrl;

    }

    public String getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(String groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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
