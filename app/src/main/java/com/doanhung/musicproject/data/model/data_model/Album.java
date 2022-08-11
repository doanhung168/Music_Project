package com.doanhung.musicproject.data.model.data_model;

import android.graphics.drawable.Drawable;
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

    public Album(long id, String name, Uri image, String artist, String artistId, int numberOfSongs) {
        this.mId = id;
        this.mName = name;
        this.mImage = image;
        this.mArtist = artist;
        this.mNumberOfSongs = numberOfSongs;
        this.mArtistId = artistId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return mId == album.mId && mNumberOfSongs == album.mNumberOfSongs && Objects.equals(mName, album.mName) && Objects.equals(mImage, album.mImage) && Objects.equals(mArtistId, album.mArtistId);
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
