package com.doanhung.musicproject.data.repository;

import com.doanhung.musicproject.data.data_manager.AppSystemData;
import com.doanhung.musicproject.data.model.app_system_model.DeviceItem;
import com.doanhung.musicproject.util.Result;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppSystemRepository {
    private final AppSystemData mAppSystemData;
    private final Executor mExecutor;

    @Inject
    public AppSystemRepository(AppSystemData appSystemData, Executor executor) {
        mAppSystemData = appSystemData;
        mExecutor = executor;
    }

    public void loadDataForNavView(AppSystemRepositoryCallback<List<DeviceItem>> callback) {
        mExecutor.execute(() -> {
            try {
                List<DeviceItem> navViewData = mAppSystemData.makeDataForNavView();
                Result<List<DeviceItem>> result = new Result.Success<>(navViewData);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<DeviceItem>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);
            }
        });
    }

    public void loadDataForSettingFragment(AppSystemRepositoryCallback<List<DeviceItem>> callback) {
        mExecutor.execute(() -> {
            try {
                List<DeviceItem> settingData = mAppSystemData.makeDataForSettingFragment();
                Result<List<DeviceItem>> result = new Result.Success<>(settingData);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<DeviceItem>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);
            }
        });
    }

    public interface AppSystemRepositoryCallback<T> {
        void onComplete(Result<T> result);
    }

}
