package com.doanhung.musicproject.service;

public interface Subject {
    void registerMusicObserver(MusicObserver musicObserver);

    void removeMusicObserver(MusicObserver musicObserver);

    void notifyMusicObserver();
}
