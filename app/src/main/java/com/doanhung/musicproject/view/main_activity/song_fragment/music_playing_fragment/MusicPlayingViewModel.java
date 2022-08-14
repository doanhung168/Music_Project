package com.doanhung.musicproject.view.main_activity.song_fragment.music_playing_fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;
import com.doanhung.musicproject.worker_manager.SleepTimerWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MusicPlayingViewModel extends ViewModel {

    private final Application mApplication;
    private final MusicRepository mMusicRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    private final MutableLiveData<List<PlayList>> _mPlaylists = new MutableLiveData<>();
    public final LiveData<List<PlayList>> mPlaylists = _mPlaylists;

    private List<Long> mPlaylistIdsOfSong;

    public final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    @Inject
    public MusicPlayingViewModel(
            Application application,
            MusicRepository musicRepository
    ) {
        this.mMusicRepository = musicRepository;
        this.mApplication = application;
    }

    public void loadPlaylists() {
        mIsLoading.setValue(true);
        mMusicRepository.loadPlaylistFromDevice(result -> {
            if (result instanceof Result.Success) {
                _mPlaylists.postValue(((Result.Success<List<PlayList>>) result).data);
            } else {
                Event.LOADING_PLAY_LIST_DATA_FAILURE_EVENT.setException(
                        ((Result.Error<List<PlayList>>) result).exception
                );
                _mEvent.postValue(Event.LOADING_PLAY_LIST_DATA_FAILURE_EVENT);
            }
            mIsLoading.postValue(false);
        });
    }


    public void addSongToPlaylist(long playlistId, long songId) {
        mIsLoading.setValue(true);
        mMusicRepository.addSongToPlaylist(playlistId, songId, result -> {
            if (result instanceof Result.Success) {
                _mEvent.postValue(Event.ADD_A_SONG_TO_PLAYLIST_SUCCESS);
            } else {
                Event.ADD_A_SONG_TO_PLAYLIST_FAILURE.setException(
                        ((Result.Error<Void>) result).exception
                );
                _mEvent.postValue(Event.ADD_A_SONG_TO_PLAYLIST_FAILURE);
            }
            mIsLoading.postValue(false);
        });
    }

    public void getPlayListIdsOfCurrentSong(DeviceSong song) {
        mIsLoading.setValue(true);
        mMusicRepository.getPlaylistIdsOfSong(song.getId(), result -> {
            if (result instanceof Result.Success) {
                mPlaylistIdsOfSong = ((Result.Success<List<Long>>) result).data;
                _mEvent.postValue(Event.NAVIGATE_MUSIC_PLAYING_PLAYLIST_FRAGMENT_2);
            } else {
                Event.GET_PLAYLIST_IDS_OF_A_SONG_FAILURE.setException(
                        ((Result.Error<List<Long>>) result).exception
                );
                _mEvent.postValue(Event.GET_PLAYLIST_IDS_OF_A_SONG_FAILURE);
            }
            mIsLoading.postValue(false);
        });
    }

    public void setSleepTimer(long time) {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SleepTimerWorker.class)
                .setInitialDelay(time, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(mApplication.getApplicationContext())
                .enqueueUniqueWork("sleepTimer", ExistingWorkPolicy.REPLACE,  workRequest);
    }

    public void navigationMusicPlayingPlaylistFragment() {
        _mEvent.setValue(Event.NAVIGATE_MUSIC_PLAYING_PLAYLIST_FRAGMENT);
    }

    public List<Long> getPlaylistIdsOfSong() {
        return mPlaylistIdsOfSong;
    }

    public boolean checkHasNoPlaylistData() {
        return _mPlaylists.getValue() == null;
    }

    public List<PlayList> getPlaylists() {
        return _mPlaylists.getValue();
    }

    public static class MusicPlayingViewModelFactory implements ViewModelProvider.Factory {
        private final Application mApplication;
        private final MusicRepository mMusicRepository;

        public MusicPlayingViewModelFactory(MusicRepository musicRepository, Application application) {
            this.mMusicRepository = musicRepository;
            this.mApplication = application;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MusicPlayingViewModel.class)) {
                return (T) new MusicPlayingViewModel(mApplication, mMusicRepository);
            }
            throw new IllegalArgumentException("Un construct viewModel");
        }
    }
}
