package com.example.marko.areyou4real;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.pchmn.materialchips.model.ChipInterface;

public class UserChip implements ChipInterface {

    private String id;
    private String name;
    private String info;

    public UserChip(String id, String name, String info) {
        this.id = id;
        this.name = name;
        this.info = info;
    }

    @Override
    public Object getId() {
        return id;
    }

    @Override
    public Uri getAvatarUri() {
        return null;
    }

    @Override
    public Drawable getAvatarDrawable() {
        return null;
    }

    @Override
    public String getLabel() {
        return name;
    }

    @Override
    public String getInfo() {
        return info;
    }
}
