package com.doanhung.musicproject.view.main_activity.song_fragment.all_song_fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.app_system_model.MusicSource;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AllSongViewModel extends ViewModel {

    private final MusicRepository mMusicRepository;

    private final MutableLiveData<List<DeviceSong>> _mSongs = new MutableLiveData<>();
    public final LiveData<List<DeviceSong>> mSongs = _mSongs;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    public final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    @Inject
    public AllSongViewModel(MusicRepository mMusicRepository) {
        this.mMusicRepository = mMusicRepository;
    }

    public List<DeviceSong> getSongs() {
        return mSongs.getValue();
    }

    public void loadAllSong() {
        mIsLoading.setValue(true);
        mMusicRepository.loadAllSong(result -> {
            if (result instanceof Result.Success) {
                _mSongs.postValue(((Result.Success<List<DeviceSong>>) result).data);
            } else {
                Event.LOADING_ALL_SONG_FROM_DEVICE_FAILURE_EVENT.setException(
                        ((Result.Error<List<DeviceSong>>) result).exception);
                _mEvent.postValue(Event.LOADING_ALL_SONG_FROM_DEVICE_FAILURE_EVENT);
            }
            mIsLoading.postValue(false);
        });
    }

    public boolean checkHasNoAllSongData() {
        return _mSongs.getValue() == null;
    }

    public void validateSongsFollowTrack(MusicSource musicSource, DeviceSong neededValidateSong) {
        if (musicSource == MusicSource.ALL_SONG_SOURCE) {
            for (DeviceSong song : getSongs()) {
                if (song.getId() == neededValidateSong.getId()) {
                    song.setPlaying(neededValidateSong.isPlaying());
                } else {
                    song.setPlaying(false);
                }
            }
        } else {
            for (DeviceSong song : getSongs()) {
                song.setPlaying(false);
            }
        }
        _mSongs.setValue(getSongs());
    }

    public static class AllSongViewModelFactory implements ViewModelProvider.Factory {
        private final MusicRepository mMusicRepository;

        public AllSongViewModelFactory(MusicRepository musicRepository) {
            mMusicRepository = musicRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AllSongViewModel.class)) {
                return (T) new AllSongViewModel(mMusicRepository);
            }
            throw new IllegalArgumentException("Un construct viewModel");
        }
    }

}
