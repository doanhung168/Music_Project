package com.doanhung.musicproject.data.model.data_model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class Album {
    private long mId;
    private String mName;
    private Uri mImage;
    private String mArtist;
    private String mArtistId;
    private int mNumberOfSongs;
    private int mYear;

    public Album(long id, String name, Uri image, String artist, String artistId, int numberOfSongs) {
        this.mId = id;
        this.mName = name;
        this.mImage = image;
        this.mArtist = artist;
        this.mNumberOfSongs = numberOfSongs;
        this.mArtistId = artistId;
    }

    public Album(long mId, String mName, Uri mImage, String mArtist, String mArtistId, int mNumberOfSongs, int mYear) {
        this.mId = mId;
        this.mName = mName;
        this.mImage = mImage;
        this.mArtist = mArtist;
        this.mArtistId = mArtistId;
        this.mNumberOfSongs = mNumberOfSongs;
        this.mYear = mYear;
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

    public String getArtist() {
        return mArtist;
    }

    public void setArtistId(String artist) {
        this.mArtist = artist;
    }

    public int getNumberOfSongs() {
        return mNumberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.mNumberOfSongs = numberOfSongs;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        this.mYear = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return mNumberOfSongs == album.mNumberOfSongs && mYear == album.mYear && Objects.equals(mName, album.mName) && Objects.equals(mImage, album.mImage) && Objects.equals(mArtist, album.mArtist) && Objects.equals(mArtistId, album.mArtistId);
    }

    public static DiffUtil.ItemCallback<Album> DIFF_CALLBACK = new DiffUtil.ItemCallback<Album>() {
        @Override
        public boolean areItemsTheSame(@NonNull Album oldItem, @NonNull Album newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Album oldItem, @NonNull Album newItem) {
            return oldItem.equals(newItem);
        }
    };
}
