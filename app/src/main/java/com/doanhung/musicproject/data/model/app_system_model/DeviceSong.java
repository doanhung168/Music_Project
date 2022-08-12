package com.doanhung.musicproject.data.model.app_system_model;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.doanhung.musicproject.data.model.data_model.Song;

public class DeviceSong extends Song {
    private boolean mIsPlaying;

    public DeviceSong(long id, String name, Uri data, Drawable image, String artist, String album, long duration, boolean isPlaying) {
        super(id, name, data, image, artist, album, duration);
        mIsPlaying = isPlaying;
    }

    public DeviceSong(long id, String name, long duration, String artist) {
        super();
        this.setId(id);
        this.setName(name);
        this.setDuration(duration);
        this.setArtist(artist);
    }


    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void setPlaying(boolean playing) {
        mIsPlaying = playing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeviceSong that = (DeviceSong) o;
        return mIsPlaying == that.mIsPlaying;
    }

    public static final DiffUtil.ItemCallback<DeviceSong> DIFF_CALLBACK = new DiffUtil.ItemCallback<DeviceSong>() {
        @Override
        public boolean areItemsTheSame(@NonNull DeviceSong oldItem, @NonNull DeviceSong newItem) {
            return newItem.getId() == oldItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull DeviceSong oldItem, @NonNull DeviceSong newItem) {
            return newItem.equals(oldItem);
        }
    };
}
