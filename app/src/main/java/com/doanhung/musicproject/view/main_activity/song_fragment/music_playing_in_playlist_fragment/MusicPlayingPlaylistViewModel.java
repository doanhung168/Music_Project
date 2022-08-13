package com.doanhung.musicproject.view.main_activity.song_fragment.music_playing_in_playlist_fragment;

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

public class MusicPlayingPlaylistViewModel extends ViewModel {
    private final MusicRepository mMusicRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    private final MutableLiveData<List<DeviceSong>> _mSongsOfPlaylist = new MutableLiveData<>();
    public final LiveData<List<DeviceSong>> mSongOfPlaylist = _mSongsOfPlaylist;

    public final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    public MusicPlayingPlaylistViewModel(MusicRepository mMusicRepository) {
        this.mMusicRepository = mMusicRepository;
    }

    public void loadSongsOfPlaylist(long playlistId) {
        mIsLoading.setValue(true);
        mMusicRepository.loadSongsOfPlaylist(playlistId, result -> {
            if (result instanceof Result.Success) {
                _mSongsOfPlaylist.postValue(((Result.Success<List<DeviceSong>>) result).data);
            } else {
                Event.LOADING_SONGS_OF_PLAYLIST_FAILURE.setException(
                        ((Result.Error<List<DeviceSong>>) result).exception
                );
                _mEvent.postValue(Event.LOADING_SONGS_OF_PLAYLIST_FAILURE);
            }
            mIsLoading.postValue(false);
        });
    }

    public void validateSongsFollowTrack(MusicSource musicSource, DeviceSong neededValidateSong) {
        if(getSongs() != null) {
            if (musicSource == MusicSource.SONGS_OF_PLAYLIST_SOURCE || musicSource == MusicSource.ALL_SONG_SOURCE) {
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
            _mSongsOfPlaylist.setValue(getSongs());
        }
    }

    public List<DeviceSong> getSongs() {
        return _mSongsOfPlaylist.getValue();
    }

    public static class MusicPlayingPlaylistViewModelFactory implements ViewModelProvider.Factory {
        private final MusicRepository mMusicRepository;

        public MusicPlayingPlaylistViewModelFactory(MusicRepository mMusicRepository) {
            this.mMusicRepository = mMusicRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MusicPlayingPlaylistViewModel.class)) {
                return (T) new MusicPlayingPlaylistViewModel(mMusicRepository);
            }
            throw new IllegalArgumentException("Un construct viewModel");
        }
    }
}
