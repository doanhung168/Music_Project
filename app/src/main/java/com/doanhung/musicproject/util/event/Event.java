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

    NAVIGATE_MUSIC_PLAYING_FRAGMENT,
    LOADING_HEADER_PLAYLIST,
    LOADING_HEADER_PLAYLIST_FAILURE_EVENT,
    SHOW_PLAYLIST_DIALOG, ADD_PLAYLIST_FAILURE_EVENT, ADD_PLAYLIST_SUCCESS_EVENT, LOADING_MY_PLAYLIST_FAILURE_EVENT, REMOVE_PLAYLIST_SUCCESS, REMOVE_PLAYLIST_FAILURE, LOADING_ALL_ALBUM_FAILURE, LOADING_ARTISTS_FAILURE, LOADING_TOP_SONG_OF_ARTIST, LOADING_TOP_ALBUM_OF_ARTIST, LOADING_GENRE_FAILURE, LOADING_SONGS_OF_ALBUM_FAILURE;


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
