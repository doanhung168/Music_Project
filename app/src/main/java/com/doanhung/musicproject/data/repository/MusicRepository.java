package com.doanhung.musicproject.data.repository;

import android.util.Log;

import com.doanhung.musicproject.data.data_manager.DummyData;
import com.doanhung.musicproject.data.data_manager.LoadDataManager;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.model.data_model.Song;
import com.doanhung.musicproject.util.Result;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

public class MusicRepository {

    private final Executor mExecutor;
    private final LoadDataManager mLoadDataManager;
    private final DummyData mDummyData;

    @Inject
    public MusicRepository(Executor executor, LoadDataManager loadDataManager, DummyData dummyData) {
        this.mExecutor = executor;
        this.mLoadDataManager = loadDataManager;
        this.mDummyData = dummyData;
    }


    public void loadDataForHotRecommendPart(MusicRepositoryCallback<List<Song>> callback) {
        mExecutor.execute(() -> {
            try {
                List<Song> hotRecommendedSongs = mDummyData.makeDataForHotRecommended();
                Result<List<Song>> result = new Result.Success<>(hotRecommendedSongs);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<Song>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);
            }
        });
    }

    public void loadDataForPlaylistPart(MusicRepositoryCallback<List<PlayList>> callback) {
        mExecutor.execute(() -> {
            try {
                List<PlayList> playLists = mDummyData.makeDataForPlayListPart();
                Result<List<PlayList>> result = new Result.Success<>(playLists);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<PlayList>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);

            }
        });
    }

    public void loadAllSong(MusicRepositoryCallback<List<DeviceSong>> callback) {
        mExecutor.execute(() -> {
            try {
                List<DeviceSong> songs = mLoadDataManager.loadAllSongFromDevice();
                Result<List<DeviceSong>> result = new Result.Success<>(songs);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<DeviceSong>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);
            }
        });
    }

    public void loadDataForHeaderPlaylistPart(MusicRepositoryCallback<List<PlayList>> callback) {
        mExecutor.execute(() -> {
            try {
                List<PlayList> playLists = mDummyData.makeDataForHeaderPlayListPart();
                Result<List<PlayList>> result = new Result.Success<>(playLists);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<PlayList>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);

            }
        });
    }


    public interface MusicRepositoryCallback<T> {
        void onComplete(Result<T> result);
    }

}
