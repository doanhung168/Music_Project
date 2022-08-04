package com.doanhung.musicproject.data.repository;

import com.doanhung.musicproject.data.DummyData;
import com.doanhung.musicproject.data.model.DeviceItem;
import com.doanhung.musicproject.data.model.Song;
import com.doanhung.musicproject.util.Result;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ResourceRepository {
    private final DummyData mDummyData;
    private final Executor mExecutor;

    private static final String TAG = "ResourceRepository";

    @Inject
    public ResourceRepository(DummyData dummyData, Executor executor) {
        mDummyData = dummyData;
        mExecutor = executor;
    }

    public void loadDataForNavView(RepositoryCallback<List<DeviceItem>> callback) {
        mExecutor.execute(() -> {
            try {
                List<DeviceItem> navViewData = mDummyData.makeDataForNavView();
                Result<List<DeviceItem>> result = new Result.Success<>(navViewData);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<DeviceItem>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);
            }
        });
    }

    public void loadDataForSettingFragment(RepositoryCallback<List<DeviceItem>> callback) {
        mExecutor.execute(() -> {
            try {
                List<DeviceItem> settingData = mDummyData.makeDataForSettingFragment();
                Result<List<DeviceItem>> result = new Result.Success<>(settingData);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<DeviceItem>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);
            }
        });
    }

    public void loadDataForHotRecommendPart(RepositoryCallback<List<Song>> callback) {
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




    public interface RepositoryCallback<T> {
        void onComplete(Result<T> result);
    }

}
