package com.doanhung.musicproject.data.model;

import android.graphics.Bitmap;

public class PlayList {
    private long mId;
    private String mName;
    private Bitmap mImage;

    public PlayList() {
    }

    public PlayList(long id, String name, Bitmap image) {
        this.mId = id;
        this.mName = name;
        this.mImage = image;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        this.mImage = image;
    }
}
