package com.doanhung.musicproject.data.data_manager;

import android.content.Context;

import androidx.core.content.res.ResourcesCompat;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.data_model.Album;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.model.data_model.Song;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class DummyData {

    private final Context mContext;

    @Inject
    public DummyData(@ApplicationContext Context mContext) {
        this.mContext = mContext;
    }

    public List<Song> makeDataForHotRecommended() {
        List<Song> songList = new ArrayList<>();

        Song song1 = new Song(1, "Sound of Sky", null,
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_hot_recommended_sample_1, null),
                "Dilon Bruce", "Summer", 0);

        Song song2 = new Song(2, "Girl on Fire", null,
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_hot_recommended_sample_2, null),
                "Alecia Keys", "Summer", 0);

        Song song3 = new Song(3, "Sound of Sky", null,
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_hot_recommended_sample_1, null),
                "Dilon Bruce", "Summer", 0);

        Song song4 = new Song(4, "Sound of Sky", null,
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_hot_recommended_sample_2, null),
                "Dilon Bruce", "Summer", 0);

        songList.add(song1);
        songList.add(song2);
        songList.add(song3);
        songList.add(song4);

        return songList;
    }

    public List<PlayList> makeDataForPlayListPart() {
        List<PlayList> playLists = new ArrayList<>();

        PlayList playList1 = new PlayList(
                1,
                "Classic Playlist",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_playlist_sample_1, null),
                "Piano Guys",
                200
        );

        PlayList playList2 = new PlayList(
                2,
                "Summer Playlist",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_playlist_sample_2, null),
                "Dilon Bruce",
                199
        );

        PlayList playList3 = new PlayList(
                3,
                "Pop Music",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_playlist_sample_1, null),
                "Michael Jackson",
                299
        );

        PlayList playList4 = new PlayList(
                4,
                "Classic Playlist",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_playlist_sample_2, null),
                "Piano Guys",
                10
        );

        playLists.add(playList1);
        playLists.add(playList2);
        playLists.add(playList3);
        playLists.add(playList4);

        return playLists;
    }

    public List<PlayList> makeDataForHeaderPlayListPart() {
        List<PlayList> playLists = new ArrayList<>();

        PlayList playList1 = new PlayList(
                1,
                "Classic Playlist",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_header_playlist_sample_1, null),
                "Piano Guys",
                200
        );

        PlayList playList2 = new PlayList(
                2,
                "Summer Playlist",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_header_playlist_sample_2, null),
                "Dilon Bruce",
                199
        );

        PlayList playList3 = new PlayList(
                3,
                "Pop Music",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_header_playlist_sample_3, null),
                "Michael Jackson",
                299
        );

        PlayList playList4 = new PlayList(
                4,
                "Classic Playlist",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_header_playlist_sample_4, null),
                "Piano Guys",
                10
        );

        playLists.add(playList1);
        playLists.add(playList2);
        playLists.add(playList3);
        playLists.add(playList4);

        return playLists;
    }

    public List<Album> makeDataForTopAlbums(String artist) {
        List<Album> albums = new ArrayList<>();

        Album album1 = new Album(1, "Fire Dragon", null, artist, "1", 3, 2019);
        Album album2 = new Album(2, "Fire Dragon", null, artist, "1", 3, 2022);
        Album album3 = new Album(3, "Fire Dragon", null, artist, "1", 3, 2021);
        Album album4 = new Album(4, "Fire Dragon", null, artist, "1", 3, 2020);
        Album album5 = new Album(5, "Fire Dragon", null, artist, "1", 3, 1998);

        albums.add(album1);
        albums.add(album2);
        albums.add(album3);
        albums.add(album4);
        albums.add(album5);
        return albums;
    }

    public List<DeviceSong> makeDataForTopSongs(String artist) {
        List<DeviceSong> songs = new ArrayList<>();

        DeviceSong deviceSong1 = new DeviceSong(1, "Song name", 1000 * 60 * 3, artist);
        DeviceSong deviceSong2 = new DeviceSong(2, "Song name", 1000 * 60 * 3, artist);
        DeviceSong deviceSong3 = new DeviceSong(3, "Song name", 1000 * 60 * 3, artist);
        DeviceSong deviceSong4 = new DeviceSong(4, "Song name", 1000 * 60 * 3, artist);
        DeviceSong deviceSong5 = new DeviceSong(5, "Song name", 1000 * 60 * 3, artist);
        DeviceSong deviceSong6 = new DeviceSong(6, "Song name", 1000 * 60 * 3, artist);
        DeviceSong deviceSong7 = new DeviceSong(7, "Song name", 1000 * 60 * 3, artist);
        DeviceSong deviceSong8 = new DeviceSong(8, "Song name", 1000 * 60 * 3, artist);

        songs.add(deviceSong1);
        songs.add(deviceSong2);
        songs.add(deviceSong3);
        songs.add(deviceSong4);
        songs.add(deviceSong5);
        songs.add(deviceSong6);
        songs.add(deviceSong7);
        songs.add(deviceSong8);

        return songs;
    }


}
