package com.doanhung.musicproject.data.model.data_model;

import android.graphics.drawable.Drawable;

public class Artist {
    private long mId;
    private String mName;
    private int mNumberOfAlbums;
    private Drawable mImage;

    public Artist() {
    }

    public Artist(long id, String name, int numberOfAlbums, Drawable image) {
        this.mId = id;
        this.mName = name;
        this.mNumberOfAlbums = numberOfAlbums;
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

    public int getNumberOfAlbums() {
        return mNumberOfAlbums;
    }

    public void setNumberOfAlbums(int numberOfAlbums) {
        this.mNumberOfAlbums = numberOfAlbums;
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable image) {
        this.mImage = image;
    }
}
