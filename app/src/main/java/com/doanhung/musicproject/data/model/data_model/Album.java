package com.doanhung.musicproject.data.model.data_model;

import android.net.Uri;

public class Album {
    private long mId;
    private String mName;
    private Uri mImage;
    private String mArtistId;
    private int mNumberOfSongs;

    public Album(long id, String name, Uri image, String artistId, int numberOfSongs) {
        this.mId = id;
        this.mName = name;
        this.mImage = image;
        this.mArtistId = artistId;
        this.mNumberOfSongs = numberOfSongs;
    }

    public Album() {
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

    public Uri getImage() {
        return mImage;
    }

    public void setImage(Uri image) {
        this.mImage = image;
    }

    public String getArtistId() {
        return mArtistId;
    }

    public void setArtistId(String artistId) {
        this.mArtistId = artistId;
    }

    public int getNumberOfSongs() {
        return mNumberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.mNumberOfSongs = numberOfSongs;
    }
}
