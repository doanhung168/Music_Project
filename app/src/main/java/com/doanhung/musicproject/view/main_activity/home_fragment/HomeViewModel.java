package com.doanhung.musicproject.view.main_activity.home_fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.model.data_model.Song;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeViewModel extends ViewModel {

    private final MusicRepository mMusicRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    public final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();

    private final MutableLiveData<List<Song>> _mHotRecommendedItems = new MutableLiveData<>();
    public final LiveData<List<Song>> mHotRecommendedItems = _mHotRecommendedItems;

    private final MutableLiveData<List<PlayList>> _mPlayListItems = new MutableLiveData<>();
    public final LiveData<List<PlayList>> mPlayListItems = _mPlayListItems;

    private final MutableLiveData<List<DeviceSong>> _mRecentlySongs = new MutableLiveData<>();
    public final LiveData<List<DeviceSong>> mRecentlySong = _mRecentlySongs;

    @Inject
    public HomeViewModel(MusicRepository musicRepository) {
        this.mMusicRepository = musicRepository;
    }

    public void loadDataForHotRecommendedPart() {
        _mEvent.setValue(Event.LOADING_HOT_RECOMMENDED_DATA_EVENT);
        mMusicRepository.loadDataForHotRecommendPart(result -> {
            if (result instanceof Result.Success) {
                _mHotRecommendedItems.postValue(((Result.Success<List<Song>>) result).data);
            } else {
                Event.LOADING_PLAY_LIST_DATA_FAILURE_EVENT.setException(
                        ((Result.Error<List<Song>>) result).exception);
                _mEvent.setValue(Event.LOADING_HOT_RECOMMENDED_DATA_FAILURE_EVENT);
            }
        });
    }

    public void loadDataForPlaylistPart() {
        _mEvent.setValue(Event.LOADING_HOT_RECOMMENDED_DATA_EVENT);
        mMusicRepository.loadDataForPlaylistPart(result -> {
            if (result instanceof Result.Success) {
                _mPlayListItems.postValue(((Result.Success<List<PlayList>>) result).data);
            } else {
                Event.LOADING_PLAY_LIST_DATA_FAILURE_EVENT.setException(
                        ((Result.Error<List<PlayList>>) result).exception);
                _mEvent.setValue(Event.LOADING_PLAY_LIST_DATA_FAILURE_EVENT);
            }
        });
    }

    public void loadSongOfRecentSongs(List<Song> songs) {
        mIsLoading.setValue(true);
        mMusicRepository.loadSongsOfRecentSongs(songs, result -> {
            if (result instanceof Result.Success) {
                _mRecentlySongs.postValue(((Result.Success<List<DeviceSong>>) result).data);
            } else {
                Event.LOADING_RECENTLY_SONGS_FAILURE.setException(
                        ((Result.Error<List<DeviceSong>>) result).exception
                );
                _mEvent.postValue(Event.LOADING_RECENTLY_SONGS_FAILURE);
            }
            mIsLoading.postValue(false);
        });
    }

    public boolean checkHasNoRecommendedData() {
        return _mHotRecommendedItems.getValue() == null;
    }

    public boolean checkHasNoPlayListData() {
        return _mPlayListItems.getValue() == null;
    }

    public List<DeviceSong> getRecentlySong() {
        return _mRecentlySongs.getValue();
    }

    public static class HomeViewModelFactory implements ViewModelProvider.Factory {
        private final MusicRepository mMusicRepository;

        public HomeViewModelFactory(MusicRepository musicRepository) {
            mMusicRepository = musicRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(HomeViewModel.class)) {
                return (T) new HomeViewModel(mMusicRepository);
            }
            throw new IllegalArgumentException("Un construct model");
        }
    }


}
