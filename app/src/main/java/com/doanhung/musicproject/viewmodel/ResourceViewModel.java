package com.doanhung.musicproject.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.DeviceItem;
import com.doanhung.musicproject.data.model.Song;
import com.doanhung.musicproject.data.repository.ResourceRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

import java.util.List;

public class ResourceViewModel extends ViewModel {

    private static final String TAG = "ResourceViewModel";

    private final ResourceRepository mResourceRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    private final MutableLiveData<List<DeviceItem>> _mNavViewItems = new MutableLiveData<>();
    public final LiveData<List<DeviceItem>> mNavViewItems = _mNavViewItems;

    private final MutableLiveData<List<DeviceItem>> _mSettingItems = new MutableLiveData<>();
    public final LiveData<List<DeviceItem>> mSettingItems = _mSettingItems;

    private final MutableLiveData<List<Song>> _mHotRecommendedItems = new MutableLiveData<>();
    public final LiveData<List<Song>> mHotRecommendedItems = _mHotRecommendedItems;


    public ResourceViewModel(ResourceRepository resourceRepository) {
        this.mResourceRepository = resourceRepository;
    }

    public void loadDataForNavView() {
        mResourceRepository.loadDataForNavView(result -> {
            if (result instanceof Result.Success) {
                _mNavViewItems.postValue(((Result.Success<List<DeviceItem>>) result).data);
            } else {
                _mEvent.setValue(Event.LOADING_NAV_VIEW_DATA_FAILURE_EVENT);
            }
        });
    }

    public void loadDataForSettingFragment() {
        mResourceRepository.loadDataForSettingFragment(result -> {
            if (result instanceof Result.Success) {
                _mSettingItems.postValue(((Result.Success<List<DeviceItem>>) result).data);
            } else {
                _mEvent.setValue(Event.LOADING_SETTING_FRAGMENT_DATA_FAILURE_EVENT);
            }
        });
    }

    public void loadDataForHotRecommendedPart() {
        mResourceRepository.loadDataForHotRecommendPart(result -> {
            if (result instanceof Result.Success) {
                _mHotRecommendedItems.postValue(((Result.Success<List<Song>>) result).data);
            } else {
                _mEvent.setValue(Event.LOADING_HOT_RECOMMENDED_PART_FAILURE_EVENT);
            }
        });
    }


    public static class ResourceViewModelFactory implements ViewModelProvider.Factory {
        private final ResourceRepository mResourceRepository;

        public ResourceViewModelFactory(ResourceRepository resourceRepository) {
            mResourceRepository = resourceRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ResourceViewModel(mResourceRepository);
        }
    }

}
