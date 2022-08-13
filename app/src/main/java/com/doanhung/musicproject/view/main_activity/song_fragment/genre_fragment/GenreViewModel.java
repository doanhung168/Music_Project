package com.doanhung.musicproject.view.main_activity.song_fragment.genre_fragment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.data_model.Genre;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.util.Result;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class GenreViewModel extends ViewModel {

    private final MusicRepository mMusicRepository;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final SingleLiveEvent<Event> mEvent = _mEvent;

    public final MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>(false);

    private final MutableLiveData<List<Genre>> _mGenres = new MutableLiveData<>();
    public final LiveData<List<Genre>> mGenres = _mGenres;

    @Inject
    public GenreViewModel(MusicRepository mMusicRepository) {
        this.mMusicRepository = mMusicRepository;
    }

    public void loadGenres() {
        mIsLoading.setValue(true);
        mMusicRepository.loadGenres(result -> {
            if (result instanceof Result.Success) {
                _mGenres.postValue(((Result.Success<List<Genre>>) result).data);
            } else {
                Event.LOADING_GENRE_FAILURE.setException(
                        ((Result.Error<List<Genre>>) result).exception
                );
                _mEvent.postValue(Event.LOADING_GENRE_FAILURE);
            }
            mIsLoading.postValue(false);
        });
    }

    public boolean checkHasNoGenresData() {
        return _mGenres.getValue() == null;
    }

    public static class GenreViewModelFactory implements ViewModelProvider.Factory {

        private final MusicRepository mMusicRepository;

        public GenreViewModelFactory(MusicRepository mMusicRepository) {
            this.mMusicRepository = mMusicRepository;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(GenreViewModel.class)) {
                return (T) new GenreViewModel(mMusicRepository);
            }
            throw new IllegalArgumentException("Un construct viewModel");
        }
    }
}
