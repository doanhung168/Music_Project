package com.doanhung.musicproject.data.model.data_model;

import android.graphics.Bitmap;

public class Genre {
    private long mId;
    private String mName;
    private int mNumberOfSongs;
    private Bitmap mImage;

    public Genre() {
    }

    public Genre(long id, String name, int numberOfSongs, Bitmap image) {
        this.mId = id;
        this.mName = name;
        this.mNumberOfSongs = numberOfSongs;
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

    public int getNumberOfSongs() {
        return mNumberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.mNumberOfSongs = numberOfSongs;
    }

    public Bitmap getImage() {
        return mImage;
    }

    public void setImage(Bitmap image) {
        this.mImage = image;
    }
}
