package com.doanhung.musicproject.view.main_activity.song_fragment.album_fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.data_model.Album;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

import java.util.List;

public class AlbumViewModel extends ViewModel {

    private final MusicRepository mMusicRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final LiveData<Event> mEvent = _mEvent;

    private final MutableLiveData<List<Album>> _mAlbums = new MutableLiveData<>();
    public final LiveData<List<Album>> mAlbums = _mAlbums;

    public final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    public AlbumViewModel(MusicRepository mMusicRepository) {
        this.mMusicRepository = mMusicRepository;
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
