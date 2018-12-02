package com.gg4real.marko.areyou4real.model;

public class Arena {
    private String arenaName;
    private String arenaPrice;
    private String arenaPhoneNumber;
    private String arenaDescription;
    private String imageResourceId;

    public Arena() {
    }

    public Arena(String arenaName, String arenaPrice, String arenaPhoneNumber, String arenaDescription, String imageResourceId) {
        this.arenaName = arenaName;
        this.arenaPrice = arenaPrice;
        this.arenaPhoneNumber = arenaPhoneNumber;
        this.arenaDescription = arenaDescription;
        this.imageResourceId = imageResourceId;

    }
    public String getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(String imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getArenaName() {
        return arenaName;
    }

    public void setArenaName(String arenaName) {
        this.arenaName = arenaName;
    }

    public String getArenaPrice() {
        return arenaPrice;
    }

    public void setArenaPrice(String arenaPrice) {
        this.arenaPrice = arenaPrice;
    }

    public String getArenaPhoneNumber() {
        return arenaPhoneNumber;
    }

    public void setArenaPhoneNumber(String arenaPhoneNumber) {
        this.arenaPhoneNumber = arenaPhoneNumber;
    }

    public String getArenaDescription() {
        return arenaDescription;
    }

    public void setArenaDescription(String arenaDescription) {
        this.arenaDescription = arenaDescription;
    }
}
