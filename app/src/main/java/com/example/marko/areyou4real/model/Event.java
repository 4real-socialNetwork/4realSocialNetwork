package com.example.marko.areyou4real.model;

import java.util.ArrayList;

public class Event {
    private String name;
    private String activity;
    private String eventDescription;
    private int startHour;

    public int getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(int startMinute) {
        this.startMinute = startMinute;
    }

    private int startMinute;
    private double eventLat;
    private double eventLng;
    private int usersNeeded;
    private int usersEntered;
    private String eventId;
    private String idOfTheUserWhoCreatedIt;
    private ArrayList<String> listOfUsersParticipatingInEvent = new ArrayList<>();

    public String getEventAdress() {
        return eventAdress;
    }

    public void setEventAdress(String eventAdress) {
        this.eventAdress = eventAdress;
    }

    private String eventAdress;

    public ArrayList<String> getListOfUsersParticipatingInEvent() {
        return listOfUsersParticipatingInEvent;
    }

    public void setListOfUsersParticipatingInEvent(ArrayList<String> listOfUsersParticipatingInEvent) {
        this.listOfUsersParticipatingInEvent = listOfUsersParticipatingInEvent;
    }

    public String getIdOfTheUserWhoCreatedIt() {
        return idOfTheUserWhoCreatedIt;
    }

    public void setIdOfTheUserWhoCreatedIt(String idOfTheUserWhoCreatedIt) {
        this.idOfTheUserWhoCreatedIt = idOfTheUserWhoCreatedIt;
    }

    public Event() {

    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Event(String idOfTheUserWhoCreatedIt, String name, String activity, int startHour, int startMinute, double eventLat, double eventLng, int usersNeeded, String eventDescription, String eventAdress) {
        this.idOfTheUserWhoCreatedIt = idOfTheUserWhoCreatedIt;
        this.name = name;
        this.activity = activity;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.eventLat = eventLat;
        this.eventLng = eventLng;
        this.usersNeeded = usersNeeded;
        this.eventDescription = eventDescription;
        this.eventAdress = eventAdress;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public int getStartHour() {
        return startHour;
    }


    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public double getEventLat() {
        return eventLat;
    }

    public void setEventLat(double eventLat) {
        this.eventLat = eventLat;
    }

    public double getEventLng() {
        return eventLng;
    }

    public void setEventLng(double eventLng) {
        this.eventLng = eventLng;
    }

    public int getUsersNeeded() {
        return usersNeeded;
    }

    public void setUsersNeeded(int usersNeeded) {
        this.usersNeeded = usersNeeded;
    }

    public int getUsersEntered() {
        return usersEntered;
    }

    public void setUsersEntered(int usersEntered) {
        this.usersEntered = usersEntered;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void addUsersToArray(String userId) {
        listOfUsersParticipatingInEvent.add(userId);
        usersEntered += 1;

    }

    public void addCreatorUserToArray(String userId) {
        listOfUsersParticipatingInEvent.add(userId);
    }

    public void removeUserFromEvent(String userIdToBeRemoved) {
        listOfUsersParticipatingInEvent.remove(userIdToBeRemoved);
        usersEntered -= 1;
    }
}
