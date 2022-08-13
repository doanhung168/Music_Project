package com.doanhung.musicproject.view.main_activity.song_fragment.album_fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.app_system_model.MusicSource;
import com.doanhung.musicproject.data.model.data_model.Album;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AlbumViewModel extends ViewModel {

    private final MusicRepository mMusicRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    private final MutableLiveData<List<Album>> _mAlbums = new MutableLiveData<>();
    public final LiveData<List<Album>> mAlbums = _mAlbums;

    private final MutableLiveData<Album> _mCurrentAlbum = new MutableLiveData<>();
    public final LiveData<Album> mCurrentAlbum = _mCurrentAlbum;

    private final MutableLiveData<List<DeviceSong>> _mSongsOfAlbum = new MutableLiveData<>();
    public final LiveData<List<DeviceSong>> mSongsOfAlbum = _mSongsOfAlbum;

    public final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    @Inject
    public AlbumViewModel(MusicRepository mMusicRepository) {
        this.mMusicRepository = mMusicRepository;
    }

    public void setCurrentAlbum(Album album) {
        _mCurrentAlbum.setValue(album);
    }

    public void loadAlbumFromDevice() {
        mMusicRepository.loadAllAlbum(result -> {
            if (result instanceof Result.Success) {
                _mAlbums.postValue(((Result.Success<List<Album>>) result).data);
            } else {
                Event.LOADING_ALL_ALBUM_FAILURE.setException(
                        ((Result.Error<List<Album>>) result).exception
                );
                _mEvent.postValue(Event.LOADING_ALL_ALBUM_FAILURE);
            }
        });
    }

    public void loadSongsOfAlbum() {
        mIsLoading.setValue(true);
        mMusicRepository.loadSongSOfAlbum(String.valueOf(_mCurrentAlbum.getValue().getId()), result -> {
            if (result instanceof Result.Success) {
                _mSongsOfAlbum.postValue(((Result.Success<List<DeviceSong>>) result).data);
            } else {
                Event.LOADING_SONGS_OF_ALBUM_FAILURE.setException(
                        ((Result.Error<List<DeviceSong>>) result).exception
                );
                _mEvent.postValue(Event.LOADING_SONGS_OF_ALBUM_FAILURE);
            }
            mIsLoading.postValue(false);
        });
    }

    public void validateSongsFollowTrack(MusicSource musicSource, DeviceSong neededValidateSong) {
        if (musicSource == null && neededValidateSong == null) {
            for (DeviceSong song : getSongs()) {
                song.setPlaying(false);
            }
            _mSongsOfAlbum.setValue(getSongs());
            return;
        }

        if (musicSource == MusicSource.SONGS_OF_ALBUM_SOURCE) {
            for (DeviceSong song : getSongs()) {
                if (song.getId() == neededValidateSong.getId()) {
                    song.setPlaying(neededValidateSong.isPlaying());
                } else {
                    song.setPlaying(false);
                }
            }
            _mSongsOfAlbum.setValue(getSongs());
        }
    }

    public boolean checkHasNoSongOfAlbumData() {
        return _mSongsOfAlbum.getValue() == null;
    }

    public boolean checkHasNoAlbumData() {
        return _mAlbums.getValue() == null;
    }

    public List<DeviceSong> getSongs() {
        return _mSongsOfAlbum.getValue();
    }


    public static class AlbumViewModelFactory implements ViewModelProvider.Factory {

        private final MusicRepository mMusicRepository;

        public AlbumViewModelFactory(MusicRepository mMusicRepository) {
            this.mMusicRepository = mMusicRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AlbumViewModel.class)) {
                return (T) new AlbumViewModel(mMusicRepository);
            }
            throw new IllegalArgumentException("Un struct viewModel");
        }
    }
}
