package com.doanhung.musicproject.view.main_activity;

import static com.doanhung.musicproject.util.event.Event.LOADING_NAV_VIEW_DATA_EVENT;
import static com.doanhung.musicproject.util.event.Event.LOADING_NAV_VIEW_DATA_FAILURE_EVENT;

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
public class ExtraMainViewModel extends ViewModel {
    private final AppSystemRepository mAppSystemRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    private final MutableLiveData<List<DeviceItem>> _mNavViewItems = new MutableLiveData<>();
    public final LiveData<List<DeviceItem>> mNavViewItems = _mNavViewItems;

    @Inject
    public ExtraMainViewModel(AppSystemRepository appSystemRepository) {
        this.mAppSystemRepository = appSystemRepository;
    }

    public void loadNavViewData() {
        _mEvent.setValue(LOADING_NAV_VIEW_DATA_EVENT);
        mAppSystemRepository.loadDataForNavView(result -> {
            if (result instanceof Result.Success) {
                _mNavViewItems.postValue(((Result.Success<List<DeviceItem>>) result).data);
            } else {
                LOADING_NAV_VIEW_DATA_FAILURE_EVENT.setException(
                        ((Result.Error<List<DeviceItem>>) result).exception);
                _mEvent.setValue(LOADING_NAV_VIEW_DATA_FAILURE_EVENT);
            }
        });
    }

    public boolean checkHasNoNavViewData() {
        return _mNavViewItems.getValue() == null;
    }


    public static class ExtraMainViewModelFactory implements ViewModelProvider.Factory {
        private final AppSystemRepository mAppSystemRepository;

        public ExtraMainViewModelFactory(AppSystemRepository appSystemRepository) {
            mAppSystemRepository = appSystemRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ExtraMainViewModel.class)) {
                return (T) new ExtraMainViewModel(mAppSystemRepository);
            }
            throw new IllegalArgumentException("Un construct viewModel");
        }
    }
}
