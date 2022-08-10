package com.doanhung.musicproject.di;

import android.content.Context;

import androidx.annotation.NonNull;

import com.doanhung.musicproject.data.model.app_system_model.DeviceItem;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.model.data_model.Song;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.view.common_adapter.DeviceItemAdapter;
import com.doanhung.musicproject.view.main_activity.home_fragment.adapter.HotRecommendedAdapter;
import com.doanhung.musicproject.view.main_activity.home_fragment.adapter.PlayListAdapter;
import com.doanhung.musicproject.view.main_activity.song_fragment.all_song_fragment.AllSongAdapter;

import org.jetbrains.annotations.Contract;

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
import dagger.hilt.android.scopes.ActivityScoped;
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


    @Singleton
    @Provides
    public static MusicServiceController provideMusicController(@ApplicationContext Context context) {
        return new MusicServiceController(context);
    }


}
