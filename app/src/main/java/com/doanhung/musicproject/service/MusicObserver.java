package com.doanhung.musicproject.service;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.app_system_model.MusicSource;

public interface MusicObserver {
    void updateData(MusicSource musicSource, DeviceSong song, boolean shuffle, boolean repeat);
}
