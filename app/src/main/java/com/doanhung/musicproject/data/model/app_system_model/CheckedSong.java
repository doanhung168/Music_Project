package com.doanhung.musicproject.data.model.app_system_model;

import com.doanhung.musicproject.data.model.data_model.Song;

public class CheckedSong extends Song {
    private boolean isChecked;

    public CheckedSong(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
