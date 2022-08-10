package com.doanhung.musicproject.data.model.data_model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class PlayList {
    private long mId;
    private String mName;
    private Drawable mImage;
    private String mOwner;
    private int mNumberSongOfPlaylist;


    public PlayList() {
    }

    public PlayList(long id, String name, Drawable image) {
        this.mId = id;
        this.mName = name;
        this.mImage = image;
    }

    public PlayList(long id, String name, Drawable image, String owner, int numberSongOfPlaylist) {
        this.mId = id;
        this.mName = name;
        this.mImage = image;
        this.mOwner = owner;
        this.mNumberSongOfPlaylist = numberSongOfPlaylist;
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

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        this.mOwner = owner;
    }

    public int getNumberSongOfPlaylist() {
        return mNumberSongOfPlaylist;
    }

    public void setNumberSongOfPlaylist(int numberSongOfPlaylist) {
        this.mNumberSongOfPlaylist = numberSongOfPlaylist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayList playList = (PlayList) o;
        return Objects.equals(mName, playList.mName) && Objects.equals(mImage, playList.mImage) && Objects.equals(mOwner, playList.mOwner);
    }


    public final static DiffUtil.ItemCallback<PlayList> DIFF_CALLBACK = new DiffUtil.ItemCallback<PlayList>() {
        @Override
        public boolean areItemsTheSame(@NonNull PlayList oldItem, @NonNull PlayList newItem) {
            return newItem.mId == oldItem.mId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PlayList oldItem, @NonNull PlayList newItem) {
            return newItem.equals(oldItem);
        }
    };
}
