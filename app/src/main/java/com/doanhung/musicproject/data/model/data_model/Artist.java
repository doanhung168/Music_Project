package com.doanhung.musicproject.data.model.data_model;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class Artist {
    private long mId;
    private String mName;
    private Uri mImage;
    private int mNumberOfAlbums;
    private int mNumberOnSongs;

    public Artist(long id, String name, Uri image, int numberOfAlbums, int numberOfSongs) {
        this.mId = id;
        this.mName = name;
        this.mImage = image;
        this.mNumberOfAlbums = numberOfAlbums;
        this.mNumberOnSongs = numberOfSongs;
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

    public Uri getImage() {
        return mImage;
    }

    public void setImage(Uri image) {
        this.mImage = image;
    }

    public int getNumberOnSongs() {
        return mNumberOnSongs;
    }

    public void setNumberOnSongs(int numberOnSongs) {
        this.mNumberOnSongs = numberOnSongs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return mNumberOfAlbums == artist.mNumberOfAlbums && mNumberOnSongs == artist.mNumberOnSongs && Objects.equals(mName, artist.mName);
    }


    public static final DiffUtil.ItemCallback<Artist> DIFF_CALLBACK = new DiffUtil.ItemCallback<Artist>() {
        @Override
        public boolean areItemsTheSame(@NonNull Artist oldItem, @NonNull Artist newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Artist oldItem, @NonNull Artist newItem) {
            return oldItem.equals(newItem);
        }
    };
}
