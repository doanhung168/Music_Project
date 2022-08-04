package com.doanhung.musicproject.di;

import androidx.annotation.NonNull;

import com.doanhung.musicproject.data.model.DeviceItem;
import com.doanhung.musicproject.data.model.Song;
import com.doanhung.musicproject.view.adapter.DeviceItemAdapter;
import com.doanhung.musicproject.view.adapter.HotRecommendedAdapter;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Singleton
    @Provides
    public static Executor provideExecutor() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @NonNull
    @Contract(" -> new")
    @Provides
    public static DeviceItemAdapter provideDeviceItemAdapter() {
        return new DeviceItemAdapter(DeviceItem.DIFF_CALLBACK);
    }

    @NonNull
    @Contract(" -> new")
    @Provides
    public static HotRecommendedAdapter provideHotRecommendedAdapter() {
        return new HotRecommendedAdapter(Song.DIFF_CALLBACK);
    }

}
