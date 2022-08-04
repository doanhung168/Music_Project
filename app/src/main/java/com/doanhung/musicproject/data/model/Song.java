package com.doanhung.musicproject.data.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class Song {
    private long mId;
    private String mName;
    private Drawable mImage;
    private String mArtist;
    private long mAlbumId;
    private long mDuration;

    public Song() {
    }

    public Song(long id, String name, Drawable image, String artist, long albumId, long duration) {
        this.mId = id;
        this.mName = name;
        this.mImage = image;
        this.mArtist = artist;
        this.mAlbumId = albumId;
        this.mDuration = duration;
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

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable image) {
        this.mImage = image;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        this.mArtist = artist;
    }

    public long getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(long albumId) {
        this.mAlbumId = albumId;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return mAlbumId == song.mAlbumId && mDuration == song.mDuration && Objects.equals(mName, song.mName) && Objects.equals(mImage, song.mImage) && Objects.equals(mArtist, song.mArtist);
    }

    public final static DiffUtil.ItemCallback<Song> DIFF_CALLBACK = new DiffUtil.ItemCallback<Song>() {
        @Override
        public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.mId == newItem.mId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.equals(newItem);
        }
    };
}
