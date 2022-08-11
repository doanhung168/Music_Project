package com.doanhung.musicproject.view.main_activity.song_fragment.add_playlist_fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.app_system_model.CheckedSong;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

import java.util.List;

public class AddingMyPlaylistViewModel extends ViewModel {

    private final MusicRepository mMusicRepository;

    private final MutableLiveData<List<CheckedSong>> _mSongs = new MutableLiveData<>();
    public final LiveData<List<CheckedSong>> mSongs = _mSongs;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final LiveData<Event> mEvent = _mEvent;

    public final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    public AddingMyPlaylistViewModel(MusicRepository musicRepository) {
        this.mMusicRepository = musicRepository;
    }

    public void setSongs(List<CheckedSong> songs) {
        _mSongs.setValue(songs);
    }

    public void loadAllSong() {
        mIsLoading.setValue(true);
        mMusicRepository.loadAllCheckedSong(result -> {
            if (result instanceof Result.Success) {
                _mSongs.postValue(((Result.Success<List<CheckedSong>>) result).data);
            } else {
                Event.LOADING_ALL_SONG_FROM_DEVICE_FAILURE_EVENT.setException(
                        ((Result.Error<List<CheckedSong>>) result).exception);
                _mEvent.postValue(Event.LOADING_ALL_SONG_FROM_DEVICE_FAILURE_EVENT);
            }
            mIsLoading.postValue(false);
        });
    }

    public void addPlayListToDevice(String playlistName, List<Long> songIdList) {
        mIsLoading.setValue(true);
        mMusicRepository.createPlaylist(playlistName, songIdList, result -> {
            if (result instanceof Result.Success) {
                _mEvent.postValue(Event.ADD_PLAYLIST_SUCCESS_EVENT);
            } else {
                Event.ADD_PLAYLIST_FAILURE_EVENT.setException(
                        ((Result.Error<Void>) result).exception
                );
                _mEvent.postValue(Event.ADD_PLAYLIST_FAILURE_EVENT);
            }
        });
    }

    public static class AddingMyPlaylistViewModelFactory implements ViewModelProvider.Factory {
        private final MusicRepository mMusicRepository;

        public AddingMyPlaylistViewModelFactory(MusicRepository musicRepository) {
            mMusicRepository = musicRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AddingMyPlaylistViewModel.class)) {
                return (T) new AddingMyPlaylistViewModel(mMusicRepository);
            }
            throw new IllegalArgumentException("Un construct viewModel");
        }
    }
}
