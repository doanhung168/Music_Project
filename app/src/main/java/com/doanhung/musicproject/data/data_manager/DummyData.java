package com.doanhung.musicproject.data.data_manager;

import android.content.Context;

import androidx.core.content.res.ResourcesCompat;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.model.data_model.Song;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

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
                "Piano Guys"
        );

        PlayList playList2 = new PlayList(
                2,
                "Summer Playlist",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_playlist_sample_2, null),
                "Dilon Bruce"
        );

        PlayList playList3 = new PlayList(
                3,
                "Pop Music",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_playlist_sample_1, null),
                "Michael Jackson"
        );

        PlayList playList4 = new PlayList(
                4,
                "Classic Playlist",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_playlist_sample_2, null),
                "Piano Guys"
        );

        playLists.add(playList1);
        playLists.add(playList2);
        playLists.add(playList3);
        playLists.add(playList4);

        return playLists;
    }


}
