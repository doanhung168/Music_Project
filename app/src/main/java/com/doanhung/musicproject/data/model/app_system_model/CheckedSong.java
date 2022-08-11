package com.doanhung.musicproject.data.model.app_system_model;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.doanhung.musicproject.data.model.data_model.Song;

public class CheckedSong extends Song {
    private boolean isChecked;


    public CheckedSong(long id, String name, Uri data, Drawable image, String artist, String album, long duration, boolean isChecked) {
        super(id, name, data, image, artist, album, duration);
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CheckedSong that = (CheckedSong) o;
        return isChecked() == that.isChecked();
    }

    public final static DiffUtil.ItemCallback<CheckedSong> DIFF_CALLBACK = new DiffUtil.ItemCallback<CheckedSong>() {
        @Override
        public boolean areItemsTheSame(@NonNull CheckedSong oldItem, @NonNull CheckedSong newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CheckedSong oldItem, @NonNull CheckedSong newItem) {
            return oldItem.equals(newItem);
        }
    };
}
