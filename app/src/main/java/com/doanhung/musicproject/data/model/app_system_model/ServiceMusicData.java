package com.doanhung.musicproject.data.model.app_system_model;

import java.util.List;

public class ServiceMusicData {
    private MusicSource mMusicSource;
    private List<DeviceSong> mSongs;
    private DeviceSong mSong;

    public ServiceMusicData(MusicSource musicSource, List<DeviceSong> songs, DeviceSong song) {
        this.mMusicSource = musicSource;
        this.mSongs = songs;
        this.mSong = song;
    }

    public MusicSource getMusicSource() {
        return mMusicSource;
    }

    public void setMusicSource(MusicSource musicSource) {
        this.mMusicSource = musicSource;
    }

    public List<DeviceSong> getSongs() {
        return mSongs;
    }

    public void setSongs(List<DeviceSong> songs) {
        this.mSongs = songs;
    }

    public DeviceSong getSong() {
        return mSong;
    }

    public void setCurrentSong(DeviceSong song) {
        this.mSong = song;
    }
}
