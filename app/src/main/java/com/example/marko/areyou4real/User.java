package com.example.marko.areyou4real;

import android.widget.ImageView;


public class User {

    String userId;
    String name;
    String surname;
    String email;
    ImageView profilePicure;
    String description;
    String interest;

    public int getUsersFinalGrade() {
        return usersFinalGrade;
    }

    public void setUsersFinalGrade(int usersFinalGrade) {
        this.usersFinalGrade = usersFinalGrade;
    }

    public String getInterest() {

        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    int range;
    int fromTime;
    int toTime;
    //we will use this in the case of leaving reviews from 1 to 5, and then we will calculate the users review grade.
    private int userGrades;
    private int numberOfGrades;
    private int usersFinalGrade;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ImageView getProfilePicure() {
        return profilePicure;
    }

    public void setProfilePicure(ImageView profilePicure) {
        this.profilePicure = profilePicure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getFromTime() {
        return fromTime;
    }

    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }

    public int getToTime() {
        return toTime;
    }

    public void setToTime(int toTime) {
        this.toTime = toTime;
    }






    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User(String name, String surname, String email, String description, int range, int fromTime, int toTime) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.description = description;
        this.range = range;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public User() {
    }

    ;

    public void gradeUser(int grade) {
        userGrades += grade;
        numberOfGrades += 1;
        usersFinalGrade = userGrades/numberOfGrades;
    }
}
