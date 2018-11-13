package com.example.marko.areyou4real.model;

import android.widget.ImageView;

import java.net.URI;
import java.net.URL;

public class Place {
    private String mPlaceName;
    private String mPlaceAdress;
    private ImageView mPlacePicture;
    private int mPlacePhoneNumber;
    private double mPlacePrice;
    private String mPlaceDescription;

    public Place(String mPlaceName, String mPlaceAdress, ImageView mPlacePicture,  int mPlacePhoneNumber, double mPlacePrice, String mPlaceDescription) {
        this.mPlaceName = mPlaceName;
        this.mPlaceAdress = mPlaceAdress;
        this.mPlacePicture = mPlacePicture;
        this.mPlacePhoneNumber = mPlacePhoneNumber;
        this.mPlacePrice = mPlacePrice;
        this.mPlaceDescription = mPlaceDescription;
    }

    public String getmPlaceName() {
        return mPlaceName;
    }

    public void setmPlaceName(String mPlaceName) {
        this.mPlaceName = mPlaceName;
    }

    public String getmPlaceAdress() {
        return mPlaceAdress;
    }

    public void setmPlaceAdress(String mPlaceAdress) {
        this.mPlaceAdress = mPlaceAdress;
    }

    public ImageView getmPlacePicture() {
        return mPlacePicture;
    }

    public void setmPlacePicture(ImageView mPlacePicture) {
        this.mPlacePicture = mPlacePicture;
    }


    public int getmPlacePhoneNumber() {
        return mPlacePhoneNumber;
    }

    public void setmPlacePhoneNumber(int mPlacePhoneNumber) {
        this.mPlacePhoneNumber = mPlacePhoneNumber;
    }

    public double getmPlacePrice() {
        return mPlacePrice;
    }

    public void setmPlacePrice(double mPlacePrice) {
        this.mPlacePrice = mPlacePrice;
    }

    public String getmPlaceDescription() {
        return mPlaceDescription;
    }

    public void setmPlaceDescription(String mPlaceDescription) {
        this.mPlaceDescription = mPlaceDescription;
    }
}
