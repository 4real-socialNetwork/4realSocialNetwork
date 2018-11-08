package com.example.marko.areyou4real;

import android.widget.ImageView;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {

    String userId;
    String name;
    String surname;
    String email;
    ImageView profilePicure;
    String description;
    ArrayList<String> interests;
    int timeStartHour;
    int timeStartMinutes;
    int timeStopHour;
    int timeStopMinutes;
    int range;
    double userLat;
    double userLong;
    String userToken;

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public double getUserLong() {
        return userLong;
    }

    public void setUserLong(double userLong) {
        this.userLong = userLong;
    }

    public int getTimeStartHour() {
        return timeStartHour;
    }

    public void setTimeStartHour(int timeStartHour) {
        this.timeStartHour = timeStartHour;
    }

    public int getTimestartMinute() {
        return timeStartMinutes;
    }

    public void setTimestartMinute(int timestartMinute) {
        this.timeStartMinutes = timestartMinute;
    }

    public int getTimeStopHour() {
        return timeStopHour;
    }

    public void setTimeStopHour(int timeStopHour) {
        this.timeStopHour = timeStopHour;
    }


    public int getTimeStopMinute() {
        return timeStopMinutes;
    }

    public void setTimeStopMinute(int timeStopMinute) {
        this.timeStopMinutes = timeStopMinute;
    }

    public User() {
    }

    public int getUsersFinalGrade() {
        return usersFinalGrade;
    }

    public void setUsersFinalGrade(int usersFinalGrade) {
        this.usersFinalGrade = usersFinalGrade;
    }

    public ArrayList<String> getInterests() {
        return interests;
    }

    public void setInterests(ArrayList<String> interests) {
        this.interests = interests;
    }


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


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User(String userId,String userToken, String name, String surname, String email, String description,
                ArrayList<String> interests, int range, int timeStartHour,
                int timeStartMinutes, int timeStopHour, int timeStopMinutes) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.description = description;
        this.interests = interests;
        this.range = range;
        this.timeStartHour = timeStartHour;
        this.timeStartMinutes = timeStartMinutes;
        this.timeStopHour = timeStopHour;
        this.timeStopMinutes = timeStopMinutes;
        this.userToken = userToken;
    }

    public void gradeUser(int grade) {
        userGrades += grade;
        numberOfGrades += 1;
        usersFinalGrade = userGrades / numberOfGrades;
    }
}
