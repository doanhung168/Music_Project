package com.doanhung.musicproject.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.app_system_model.MusicSource;
import com.doanhung.musicproject.data.model.app_system_model.ServiceMusicData;
import com.doanhung.musicproject.data.model.data_model.Song;
import com.doanhung.musicproject.data.room.MusicDatabase;
import com.doanhung.musicproject.util.CommonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

public class MusicServiceController implements Subject {

    private final static int INIT_VALUE_SONG_INDEX = -1;
    private final static int FIRST_INDEX_OF_SONGS = 0;

    private final Context mContext;
    private MediaPlayer mMediaPlayer;

    private int mCurrentShuffleSongIndex = INIT_VALUE_SONG_INDEX;

    private int mCurrentSongIndex = INIT_VALUE_SONG_INDEX;
    private List<DeviceSong> mSongs;
    private DeviceSong mCurrentSong;
    private MusicSource mMusicSource;

    private List<DeviceSong> mShuffleSongs;

    private ServiceMusicData mReceivedData;
    private boolean mIsShuffle;
    private boolean mIsRepeat;

    private final List<MusicObserver> musicObservers = new ArrayList<>();

    public MusicServiceController(Context context) {
        this.mContext = context;
    }

    public void setData(ServiceMusicData serviceMusicData) {
        mReceivedData = serviceMusicData;
    }

    public void setShuffle(boolean shuffle) {
        mIsShuffle = shuffle;
        if(shuffle) {
            Collections.shuffle(mShuffleSongs);
            mCurrentShuffleSongIndex = CommonUtil.findItemIndex(getCurrentSong(), mShuffleSongs);
        }
        notifyFuncForMusicObserver();
    }

    public void setRepeat(boolean repeat) {
        mIsRepeat = repeat;
        notifyFuncForMusicObserver();
    }

    public void playExternalSong() {
        if (isDifferenceMusicSource(mReceivedData)) {
            mCurrentShuffleSongIndex = INIT_VALUE_SONG_INDEX;
            mCurrentSongIndex = INIT_VALUE_SONG_INDEX;
        }

        if (isSameSong(mReceivedData)) {
            DeviceSong sameSong = mReceivedData.getSong();
            playExternalSameSong(sameSong);
        } else {
            playExternalNewSong(mReceivedData);
        }
    }

    private void playExternalSameSong(@NonNull DeviceSong song) {
        if (song.isPlaying()) {
            resumeCurrentSong();
        } else {
            pauseCurrentSong();
        }
        notifyMusicObserver();
    }

    private void playExternalNewSong(@NonNull ServiceMusicData data) {
        MusicSource newMusicSource = data.getMusicSource();
        List<DeviceSong> newSongs = data.getSongs();
        DeviceSong newSong = data.getSong();

        startNewSong(newSong);
        updateData(newMusicSource, newSongs, newSong);
        notifyMusicObserver();
    }

    private boolean isSameSong(@NonNull ServiceMusicData receivedData) {
        return receivedData.getMusicSource() == getMusicSource()
                && receivedData.getSong().getId() == getCurrentSong().getId();
    }

    private boolean isDifferenceMusicSource(@NonNull ServiceMusicData receivedData) {
        return receivedData.getMusicSource() != getMusicSource();
    }

    public void playNextSong() {
        DeviceSong nextSong = getNextSong();
        startNewSong(nextSong);
        this.mCurrentSong = nextSong;
        mCurrentSongIndex = CommonUtil.findItemIndex(getCurrentSong(), mSongs);
        notifyMusicObserver();
    }

    public void playOrPauseCurrentSong() {
        if (mMediaPlayer.isPlaying()) {
            pauseCurrentSong();
        } else {
            resumeCurrentSong();
        }
    }

    public void playPreviousSong() {
        DeviceSong prevSong = getPreviousSong();
        startNewSong(prevSong);
        this.mCurrentSong = prevSong;
        mCurrentSongIndex = CommonUtil.findItemIndex(getCurrentSong(), mSongs);
        notifyMusicObserver();
    }

    private void updateData(MusicSource musicSource, List<DeviceSong> songs, DeviceSong song) {
        this.mMusicSource = musicSource;
        this.mSongs = songs;
        this.mCurrentSong = song;

        // for shuffle list
        mShuffleSongs = new ArrayList<>();
        mShuffleSongs.addAll(songs);

        if (mIsShuffle) {
            mCurrentShuffleSongIndex = CommonUtil.findItemIndex(getCurrentSong(), mShuffleSongs);
        } else {
            mCurrentSongIndex = CommonUtil.findItemIndex(getCurrentSong(), mSongs);
        }
    }

    public void removeData() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

        mMusicSource = null;
        mSongs = null;
        mCurrentSong = null;
        mCurrentSongIndex = INIT_VALUE_SONG_INDEX;
        mCurrentShuffleSongIndex = INIT_VALUE_SONG_INDEX;
        notifyMusicObserver();
    }


    private void pauseCurrentSong() {
        mMediaPlayer.pause();
        DeviceSong pausedSong = getCurrentSong();
        pausedSong.setPlaying(false);
        mCurrentSong = pausedSong;
        notifyMusicObserver();
    }

    private void resumeCurrentSong() {
        mMediaPlayer.start();
        DeviceSong playingSong = getCurrentSong();
        playingSong.setPlaying(true);
        mCurrentSong = playingSong;
        notifyMusicObserver();
    }


    private void startNewSong(@NonNull DeviceSong song) {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        song.setPlaying(true);
        mMediaPlayer = MediaPlayer.create(mContext.getApplicationContext(), song.getData());
        mMediaPlayer.setOnCompletionListener(mp -> {
            if (mIsRepeat) {
                playCurrentSongAgain();
            } else {
                playNextSong();
            }
        });

        Song currentSong = new Song(song.getId(), System.currentTimeMillis());
        MusicDatabase.getInstance(mContext).musicDao().insertSong(currentSong);
        mMediaPlayer.start();
    }

    private void playCurrentSongAgain() {
        startNewSong(getCurrentSong());
        notifyMusicObserver();
    }


    private int getLastIndexOfSongs() {
        if (mIsShuffle) {
            return mShuffleSongs.size() - 1;
        } else {
            return mSongs.size() - 1;
        }

    }

    public DeviceSong getCurrentSong() {
        return mCurrentSong;
    }

    public MusicSource getMusicSource() {
        return mMusicSource;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    private DeviceSong getNextSong() {
        if (mIsShuffle) {
            if (mCurrentShuffleSongIndex == getLastIndexOfSongs()) {
                return mShuffleSongs.get(FIRST_INDEX_OF_SONGS);
            } else {
                return mShuffleSongs.get(++mCurrentShuffleSongIndex);
            }
        } else {
            if (mCurrentSongIndex == getLastIndexOfSongs()) {
                return mSongs.get(FIRST_INDEX_OF_SONGS);
            } else {
                return mSongs.get(++mCurrentSongIndex);
            }
        }

    }

    private DeviceSong getPreviousSong() {
        if (mIsShuffle) {
            if (mCurrentShuffleSongIndex == FIRST_INDEX_OF_SONGS) {
                return mShuffleSongs.get(getLastIndexOfSongs());
            } else {
                return mShuffleSongs.get(--mCurrentShuffleSongIndex);
            }
        } else {
            if (mCurrentSongIndex == FIRST_INDEX_OF_SONGS) {
                return mSongs.get(getLastIndexOfSongs());
            } else {
                return mSongs.get(--mCurrentSongIndex);
            }
        }
    }

    @Override
    public void registerMusicObserver(MusicObserver musicObserver) {
        musicObservers.add(musicObserver);
    }

    @Override
    public void removeMusicObserver(MusicObserver musicObserver) {
        musicObservers.remove(musicObserver);
    }

    @Override
    public void notifyMusicObserver() {
        for (MusicObserver observer : musicObservers) {
            observer.updateData(mMusicSource, mCurrentSong);
        }
    }

    public void notifyFuncForMusicObserver() {
        for (MusicObserver observer : musicObservers) {
            observer.updateData(mIsShuffle, mIsRepeat);
        }
    }

}
