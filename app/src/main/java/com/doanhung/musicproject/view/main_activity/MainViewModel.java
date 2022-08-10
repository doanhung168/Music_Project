package com.doanhung.musicproject.view.main_activity;

import static com.doanhung.musicproject.util.Constraint.PLAY_A_NEXT_SONG;
import static com.doanhung.musicproject.util.Constraint.PLAY_A_PREVIOUS_SONG;
import static com.doanhung.musicproject.util.Constraint.PLAY_EXTERNAL_SONG;
import static com.doanhung.musicproject.util.Constraint.PLAY_OR_PAUSE_CURRENT_SONG;
import static com.doanhung.musicproject.util.Constraint.RESTORE_UI_WITH_MUSIC_SERVICE;
import static com.doanhung.musicproject.util.Constraint.STOP_MUSIC;

import android.app.Application;
import android.content.Intent;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.app_system_model.MusicSource;
import com.doanhung.musicproject.data.model.app_system_model.ServiceMusicData;
import com.doanhung.musicproject.service.MusicObserver;
import com.doanhung.musicproject.service.MusicService;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.util.event.SingleLiveEvent;

public class MainViewModel extends ViewModel implements MusicObserver {

    private Application mApplication;
    private Intent mServiceIntent;
    private final MusicServiceController mMusicServiceController;

    private final SingleLiveEvent<Event> _mEvent = new SingleLiveEvent<>();
    public final LiveData<Event> mEvent = _mEvent;

    private final MutableLiveData<DeviceSong> _mCurrentSong = new MutableLiveData<>();
    public final LiveData<DeviceSong> mCurrentSong = _mCurrentSong;

    public MutableLiveData<Boolean> mShowMusicPlayerBar = new MutableLiveData<>(true);

    private final MutableLiveData<Boolean> _mIsShuffle = new MutableLiveData<>(false);
    public final LiveData<Boolean> mIsShuffle = _mIsShuffle;

    private final MutableLiveData<Boolean> _mIsRepeat = new MutableLiveData<>(false);
    public final LiveData<Boolean> mIsRepeat = _mIsRepeat;

    private MusicSource mMusicSource;

    public boolean getIsRepeat() {
        return Boolean.TRUE.equals(_mIsRepeat.getValue());
    }

    public boolean getIsShuffle() {
        return Boolean.TRUE.equals(_mIsShuffle.getValue());
    }

    public MainViewModel(Application application, MusicServiceController musicServiceController) {
        this.mApplication = application;
        this.mMusicServiceController = musicServiceController;
        this.mMusicServiceController.registerMusicObserver(this);
        mServiceIntent = new Intent(application.getApplicationContext(), MusicService.class);
    }

    public void setShowMusicPlayerBar(boolean isShow) {
        mShowMusicPlayerBar.setValue(isShow);
    }

    public synchronized MediaPlayer getMediaPlayer() {
        return mMusicServiceController.getMediaPlayer();
    }

    public DeviceSong getCurrentSong() {
        return _mCurrentSong.getValue();
    }

    public MusicSource getMusicSource() {
        return mMusicSource;
    }

    public void playExternalSong(ServiceMusicData serviceMusicData) {
        mMusicServiceController.setData(serviceMusicData);
        mServiceIntent.setAction(PLAY_EXTERNAL_SONG);
        mApplication.startService(mServiceIntent);
    }

    public void playNextSong() {
        mServiceIntent.setAction(PLAY_A_NEXT_SONG);
        startMusicService();
    }

    public void playPreviousSong() {
        mServiceIntent.setAction(PLAY_A_PREVIOUS_SONG);
        startMusicService();
    }

    public void playOrPlayCurrentSong() {
        mServiceIntent.setAction(PLAY_OR_PAUSE_CURRENT_SONG);
        startMusicService();
    }

    public void restoreUIWithService() {
        mServiceIntent.setAction(RESTORE_UI_WITH_MUSIC_SERVICE);
        startMusicService();
    }

    public void shuffle(boolean shuffle) {
        mMusicServiceController.setShuffle(shuffle);
    }

    public void repeat(boolean repeat) {
        mMusicServiceController.setRepeat(repeat);
    }

    public void stopMusic() {
        mServiceIntent.setAction(STOP_MUSIC);
        startMusicService();
    }

    private void startMusicService() {
        mApplication.startService(mServiceIntent);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mApplication = null;
        mServiceIntent = null;
        mMusicServiceController.removeMusicObserver(this);
    }

    @Override
    public void updateData(MusicSource musicSource, DeviceSong song) {
        mMusicSource = musicSource;
        _mCurrentSong.setValue(song);
    }

    @Override
    public void updateData(boolean shuffle, boolean repeat) {
        _mIsShuffle.setValue(shuffle);
        _mIsRepeat.setValue(repeat);
    }

    public static class MainViewModelFactory implements ViewModelProvider.Factory {
        private final Application application;
        private final MusicServiceController musicServiceController;

        public MainViewModelFactory(Application application, MusicServiceController musicServiceController) {
            this.application = application;
            this.musicServiceController = musicServiceController;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(MainViewModel.class)) {
                return (T) new MainViewModel(application, musicServiceController);
            }
            throw new IllegalArgumentException("Un construct viewModel");

        }
    }
}
