package com.doanhung.musicproject.util.event;

public enum Event {

    LOADING_NAV_VIEW_DATA_FAILURE_EVENT,

    LOADING_SETTING_FRAGMENT_DATA_EVENT,
    LOADING_SETTING_FRAGMENT_DATA_FAILURE_EVENT,

    LOADING_HOT_RECOMMENDED_DATA_FAILURE_EVENT,

    LOADING_ALL_SONG_FROM_DEVICE_EVENT,
    LOADING_ALL_SONG_FROM_DEVICE_FAILURE_EVENT,

    LOADING_PLAY_LIST_DATA_FAILURE_EVENT,
    LOADING_NAV_VIEW_DATA_EVENT,
    LOADING_HOT_RECOMMENDED_DATA_EVENT,

    NAVIGATE_MUSIC_PLAYING_FRAGMENT, LOADING_HEADER_PLAYLIST, LOADING_HEADER_PLAYLIST_FAILURE_EVENT;


    private Exception exception;

    Event() {
    }

    Event(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
