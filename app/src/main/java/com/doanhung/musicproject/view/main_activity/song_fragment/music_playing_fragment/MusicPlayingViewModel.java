package com.doanhung.musicproject.view.main_activity.song_fragment.music_playing_fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

import java.util.List;

public class MusicPlayingViewModel extends ViewModel {
    private final MusicRepository mMusicRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    private final MutableLiveData<List<PlayList>> _mPlaylists = new MutableLiveData<>();
    public final LiveData<List<PlayList>> mPlaylists = _mPlaylists;

    public final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    public MusicPlayingViewModel(MusicRepository musicRepository) {
        this.mMusicRepository = musicRepository;
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

    public boolean checkHasNoPlaylistData() {
        return _mPlaylists.getValue() == null;
    }

    public List<PlayList> getPlaylists() {
        return _mPlaylists.getValue();
    }

    public static class MusicPlayingViewModelFactory implements ViewModelProvider.Factory {
        private final MusicRepository mMusicRepository;

        public MusicPlayingViewModelFactory(MusicRepository musicRepository) {
            this.mMusicRepository = musicRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MusicPlayingViewModel.class)) {
                return (T) new MusicPlayingViewModel(mMusicRepository);
            }
            throw new IllegalArgumentException("Un construct viewModel");
        }
    }
}