package com.example.marko.areyou4real;

public class Arena {
    private String arenaName;
    private String arenaPrice;
    private String arenaPhoneNumber;
    private String arenaDescription;

    public Arena() {
    }

    public Arena(String arenaName, String arenaPrice, String arenaPhoneNumber, String arenaDescription) {
        this.arenaName = arenaName;
        this.arenaPrice = arenaPrice;
        this.arenaPhoneNumber = arenaPhoneNumber;
        this.arenaDescription = arenaDescription;
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
