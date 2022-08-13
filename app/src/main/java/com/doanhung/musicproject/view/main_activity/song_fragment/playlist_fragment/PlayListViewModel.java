package com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment;

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

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PlayListViewModel extends ViewModel {

    private final MusicRepository mMusicRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    private final MutableLiveData<List<PlayList>> _mHeaderPlaylist = new MutableLiveData<>();
    public final LiveData<List<PlayList>> mHeaderPlaylist = _mHeaderPlaylist;

    private final MutableLiveData<List<PlayList>> _mPlaylist = new MutableLiveData<>();
    public final LiveData<List<PlayList>> mPlaylist = _mPlaylist;

    @Inject
    public PlayListViewModel(MusicRepository musicRepository) {
        this.mMusicRepository = musicRepository;
    }

    public void loadDataForHeaderPlaylistPart() {
        mMusicRepository.loadDataForHeaderPlaylistPart(result -> {
            if (result instanceof Result.Success) {
                _mHeaderPlaylist.postValue(((Result.Success<List<PlayList>>) result).data);
            } else {
                Event.LOADING_HEADER_PLAYLIST_FAILURE_EVENT.setException(
                        ((Result.Error<List<PlayList>>) result).exception);
                _mEvent.setValue(Event.LOADING_HEADER_PLAYLIST_FAILURE_EVENT);
            }
        });
    }

    public void loadPlaylistFromDevice() {
        mMusicRepository.loadPlaylistFromDevice(result -> {
            if (result instanceof Result.Success) {
                _mPlaylist.postValue(((Result.Success<List<PlayList>>) result).data);
            } else {
                Event.LOADING_MY_PLAYLIST_FAILURE_EVENT.setException(
                        ((Result.Error<List<PlayList>>) result).exception);
                _mEvent.setValue(Event.LOADING_MY_PLAYLIST_FAILURE_EVENT);
            }
        });
    }

    public void removePlaylistFromDevice(PlayList playList) {
        mMusicRepository.removePlaylistFromDevice(playList, result -> {
            if (result instanceof Result.Success) {
                _mEvent.postValue(Event.REMOVE_PLAYLIST_SUCCESS);
            } else {
                Event.REMOVE_PLAYLIST_FAILURE.setException(
                        ((Result.Error<Void>) result).exception);
                _mEvent.postValue(Event.REMOVE_PLAYLIST_FAILURE);
            }
        });
    }

    public void reloadMyPlaylistData() {
        loadPlaylistFromDevice();
    }

    public void showAddingPlayListDialog() {
        _mEvent.setValue(Event.SHOW_PLAYLIST_DIALOG);
    }

    public boolean checkHasNoHeaderPlaylists() {
        return _mHeaderPlaylist.getValue() == null;
    }

    public boolean checkHasNoMyPlaylists() {
        return _mPlaylist.getValue() == null;
    }

    public static class PlayListViewModelFactory implements ViewModelProvider.Factory {
        private final MusicRepository mMusicRepository;

        public PlayListViewModelFactory(MusicRepository musicRepository) {
            mMusicRepository = musicRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PlayListViewModel.class)) {
                return (T) new PlayListViewModel(mMusicRepository);
            }
            throw new IllegalArgumentException("Un construct model");
        }
    }
}
