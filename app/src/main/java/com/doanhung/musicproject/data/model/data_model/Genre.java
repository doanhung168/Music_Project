package com.doanhung.musicproject.data.model.data_model;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class Genre {
    private long mId;
    private String mName;
    private int mNumberOfSongs;

    public Genre(long mId, String mName) {
        this.mId = mId;
        this.mName = mName;
    }

    public Genre(long id, String name, int numberOfSongs) {
        this.mId = id;
        this.mName = name;
        this.mNumberOfSongs = numberOfSongs;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return mNumberOfSongs == genre.mNumberOfSongs && Objects.equals(mName, genre.mName);
    }

    public static final DiffUtil.ItemCallback<Genre> DIFF_CALLBACK = new DiffUtil.ItemCallback<Genre>() {
        @Override
        public boolean areItemsTheSame(@NonNull Genre oldItem, @NonNull Genre newItem) {
            return oldItem.mId == newItem.mId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Genre oldItem, @NonNull Genre newItem) {
            return oldItem.equals(newItem);
        }
    };
}
