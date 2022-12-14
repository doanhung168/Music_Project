package com.doanhung.musicproject.di;

import static com.doanhung.musicproject.util.Constraint.DATABASE_NAME;

import android.content.Context;

import androidx.room.Room;

import com.doanhung.musicproject.data.model.app_system_model.CheckedSong;
import com.doanhung.musicproject.data.model.app_system_model.DeviceItem;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.data_model.Album;
import com.doanhung.musicproject.data.model.data_model.Artist;
import com.doanhung.musicproject.data.model.data_model.Genre;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.model.data_model.Song;
import com.doanhung.musicproject.data.room.MusicDao;
import com.doanhung.musicproject.data.room.MusicDatabase;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.view.common_adapter.DeviceItemAdapter;
import com.doanhung.musicproject.view.common_adapter.SongAdapter;
import com.doanhung.musicproject.view.main_activity.home_fragment.adapter.HotRecommendedAdapter;
import com.doanhung.musicproject.view.main_activity.home_fragment.adapter.PlayListAdapter;
import com.doanhung.musicproject.view.main_activity.home_fragment.adapter.RecentlySongAdapter;
import com.doanhung.musicproject.view.main_activity.song_fragment.album_fragment.AlbumAdapter;
import com.doanhung.musicproject.view.main_activity.song_fragment.all_song_fragment.AllSongAdapter;
import com.doanhung.musicproject.view.main_activity.song_fragment.artist_detail_fragment.TopAlbumAdapter;
import com.doanhung.musicproject.view.main_activity.song_fragment.artist_fragment.ArtistAdapter;
import com.doanhung.musicproject.view.main_activity.song_fragment.genre_fragment.GenreAdapter;
import com.doanhung.musicproject.view.main_activity.song_fragment.playlist_adding_fragment.SelectedSongAdapter;
import com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment.HeaderPlayListAdapter;
import com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment.MyPlaylistAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DeviceItemAdapterWithoutDividerAnnotation {
    }

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DeviceItemAdapterAnnotation {
    }

    @Singleton
    @Provides
    public static Executor provideExecutor() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Provides
    public static ScheduledExecutorService provideScheduleExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }


    @DeviceItemAdapterAnnotation
    @Provides
    public static DeviceItemAdapter provideDeviceItemAdapter() {
        return new DeviceItemAdapter(DeviceItem.DIFF_CALLBACK, true);
    }

    @DeviceItemAdapterWithoutDividerAnnotation
    @Provides
    public static DeviceItemAdapter provideDeviceItemAdapterWithoutLastDivider() {
        return new DeviceItemAdapter(DeviceItem.DIFF_CALLBACK, false);
    }


    @Provides
    public static HotRecommendedAdapter provideHotRecommendedAdapter() {
        return new HotRecommendedAdapter(Song.DIFF_CALLBACK);
    }

    @Provides
    public static PlayListAdapter providePlayListAdapter() {
        return new PlayListAdapter(PlayList.DIFF_CALLBACK);
    }


    @Provides
    public static AllSongAdapter provideSongAdapter() {
        return new AllSongAdapter(DeviceSong.DIFF_CALLBACK);
    }

    @Provides
    public static HeaderPlayListAdapter provideHeaderPlayListAdapter() {
        return new HeaderPlayListAdapter(PlayList.DIFF_CALLBACK);
    }


    @Singleton
    @Provides
    public static MusicServiceController provideMusicController(@ApplicationContext Context context) {
        return new MusicServiceController(context);
    }

    @Provides
    public static SelectedSongAdapter provideSelectedSongAdapter() {
        return new SelectedSongAdapter(CheckedSong.DIFF_CALLBACK);
    }

    @Provides
    public static MyPlaylistAdapter provideMyPlaylistAdapter() {
        return new MyPlaylistAdapter(PlayList.DIFF_CALLBACK);
    }

    @Provides
    public static AlbumAdapter provideAlbumAdapter() {
        return new AlbumAdapter(Album.DIFF_CALLBACK);
    }

    @Provides
    public static ArtistAdapter provideArtistAdapter() {
        return new ArtistAdapter(Artist.DIFF_CALLBACK);
    }

    @Provides
    public static TopAlbumAdapter provideTopAdapter() {
        return new TopAlbumAdapter(Album.DIFF_CALLBACK);
    }

    @Provides
    public static SongAdapter providerSongAdapter() {
        return new SongAdapter(DeviceSong.DIFF_CALLBACK);
    }

    @Provides
    public static GenreAdapter providerGenreAdapter() {
        return new GenreAdapter(Genre.DIFF_CALLBACK);
    }

    @Provides
    public static RecentlySongAdapter provideRecentlySongAdapter() {
        return new RecentlySongAdapter(DeviceSong.DIFF_CALLBACK);
    }


}
