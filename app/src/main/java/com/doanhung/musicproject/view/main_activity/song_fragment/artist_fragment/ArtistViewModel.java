package com.doanhung.musicproject.view.main_activity.song_fragment.artist_fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.data_model.Album;
import com.doanhung.musicproject.data.model.data_model.Artist;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ArtistViewModel extends ViewModel {

    private final MusicRepository mMusicRepository;

    private final MutableLiveData<List<Artist>> _mArtists = new MutableLiveData<>();
    public final LiveData<List<Artist>> mArtists = _mArtists;

    private final MutableLiveData<Artist> _mCurrentArtists = new MutableLiveData<>();
    public final LiveData<Artist> mCurrentArtist = _mCurrentArtists;

    private final MutableLiveData<List<Album>> _mTopAlbumsOfArtist = new MutableLiveData<>();
    public final LiveData<List<Album>> mTopAlbumsOfArtist = _mTopAlbumsOfArtist;

    private final MutableLiveData<List<DeviceSong>> _mTopSongsOfArtist = new MutableLiveData<>();
    public final LiveData<List<DeviceSong>> mTopSongOfArtist = _mTopSongsOfArtist;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    public final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    @Inject
    public ArtistViewModel(MusicRepository mMusicRepository) {
        this.mMusicRepository = mMusicRepository;
    }

    public void setCurrentArtist(Artist artist) {
        _mCurrentArtists.setValue(artist);
    }

    public void loadArtist() {
        mMusicRepository.loadArtists(result -> {
            if (result instanceof Result.Success) {
                _mArtists.postValue(((Result.Success<List<Artist>>) result).data);
            } else {
                Event.LOADING_ARTISTS_FAILURE.setException(((Result.Error<List<Artist>>) result).exception);
                _mEvent.postValue(Event.LOADING_ARTISTS_FAILURE);
            }
        });
    }

    public void loadTopAlbums() {
        mMusicRepository.loadTopAlbums(Objects.requireNonNull(_mCurrentArtists.getValue()).getName(),
                result -> {
                    if (result instanceof Result.Success) {
                        _mTopAlbumsOfArtist.postValue(((Result.Success<List<Album>>) result).data);
                    } else {
                        Event.LOADING_TOP_ALBUM_OF_ARTIST.setException(
                                ((Result.Error<List<Album>>) result).exception
                        );
                        _mEvent.postValue(Event.LOADING_TOP_ALBUM_OF_ARTIST);
                    }
                });
    }

    public void loadTopSongs() {
        mMusicRepository.loadTopSongs(Objects.requireNonNull(_mCurrentArtists.getValue()).getName(),
                result -> {
                    if (result instanceof Result.Success) {
                        _mTopSongsOfArtist.postValue(((Result.Success<List<DeviceSong>>) result).data);
                    } else {
                        Event.LOADING_TOP_SONG_OF_ARTIST.setException(
                                ((Result.Error<List<DeviceSong>>) result).exception
                        );
                        _mEvent.postValue(Event.LOADING_TOP_SONG_OF_ARTIST);
                    }
                });
    }


    public boolean checkHasNoArtists() {
        return _mArtists.getValue() == null;
    }

    public static class ArtistViewModelFactory implements ViewModelProvider.Factory {
        private final MusicRepository mMusicRepository;

        public ArtistViewModelFactory(MusicRepository musicRepository) {
            mMusicRepository = musicRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ArtistViewModel.class)) {
                return (T) new ArtistViewModel(mMusicRepository);
            }
            throw new IllegalArgumentException("Un construct viewModel");
        }
    }
}
