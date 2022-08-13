package com.doanhung.musicproject.view.main_activity.setting_fragment;

import static com.doanhung.musicproject.util.event.Event.LOADING_SETTING_FRAGMENT_DATA_EVENT;
import static com.doanhung.musicproject.util.event.Event.LOADING_SETTING_FRAGMENT_DATA_FAILURE_EVENT;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.app_system_model.DeviceItem;
import com.doanhung.musicproject.data.repository.AppSystemRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SettingViewModel extends ViewModel {
    private final AppSystemRepository mAppSystemRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    private final MutableLiveData<List<DeviceItem>> _mSettingItems = new MutableLiveData<>();
    public final LiveData<List<DeviceItem>> mSettingItems = _mSettingItems;

    @Inject
    public SettingViewModel(AppSystemRepository appSystemRepository) {
        this.mAppSystemRepository = appSystemRepository;
    }

    public void loadSettingData() {
        _mEvent.setValue(LOADING_SETTING_FRAGMENT_DATA_EVENT);
        mAppSystemRepository.loadDataForSettingFragment(result -> {
            if (result instanceof Result.Success) {
                _mSettingItems.postValue(((Result.Success<List<DeviceItem>>) result).data);
            } else {
                LOADING_SETTING_FRAGMENT_DATA_FAILURE_EVENT
                        .setException(((Result.Error<List<DeviceItem>>) result).exception);
                _mEvent.postValue(LOADING_SETTING_FRAGMENT_DATA_FAILURE_EVENT);
            }
        });
    }

    public boolean checkHasNoSettingData() {
        return _mSettingItems.getValue() == null;
    }

    public static class SettingViewModelFactory implements ViewModelProvider.Factory {
        private final AppSystemRepository mAppSystemRepository;

        public SettingViewModelFactory(AppSystemRepository appSystemRepository) {
            mAppSystemRepository = appSystemRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(SettingViewModel.class)) {
                return (T) new SettingViewModel(mAppSystemRepository);
            }
            throw new IllegalArgumentException("Un construct viewModel");
        }
    }

}
