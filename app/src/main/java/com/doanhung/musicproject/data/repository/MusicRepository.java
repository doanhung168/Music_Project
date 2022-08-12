package com.doanhung.musicproject.data.repository;

import com.doanhung.musicproject.data.data_manager.DataManager;
import com.doanhung.musicproject.data.data_manager.DummyData;
import com.doanhung.musicproject.data.model.app_system_model.CheckedSong;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.app_system_model.SongType;
import com.doanhung.musicproject.data.model.data_model.Album;
import com.doanhung.musicproject.data.model.data_model.Artist;
import com.doanhung.musicproject.data.model.data_model.Genre;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.model.data_model.Song;
import com.doanhung.musicproject.util.Result;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

public class MusicRepository {

    private static final String TAG = "MusicRepository";

    private final Executor mExecutor;
    private final DataManager mDataManager;
    private final DummyData mDummyData;

    @Inject
    public MusicRepository(Executor executor, DataManager dataManager, DummyData dummyData) {
        this.mExecutor = executor;
        this.mDataManager = dataManager;
        this.mDummyData = dummyData;
    }


    public void loadDataForHotRecommendPart(MusicRepositoryCallback<List<Song>> callback) {
        mExecutor.execute(() -> {
            try {
                List<Song> hotRecommendedSongs = mDummyData.makeDataForHotRecommended();
                Result<List<Song>> result = new Result.Success<>(hotRecommendedSongs);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<Song>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);
            }
        });
    }

    public void loadDataForPlaylistPart(MusicRepositoryCallback<List<PlayList>> callback) {
        mExecutor.execute(() -> {
            try {
                List<PlayList> playLists = mDummyData.makeDataForPlayListPart();
                Result<List<PlayList>> result = new Result.Success<>(playLists);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<PlayList>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);

            }
        });
    }

    public void loadAllSong(MusicRepositoryCallback<List<DeviceSong>> callback) {
        mExecutor.execute(() -> {
            try {
                @SuppressWarnings("unchecked")
                List<DeviceSong> songs = (List<DeviceSong>) mDataManager.loadAllSongFromDevice(SongType.DEVICE_SONG);
                Result<List<DeviceSong>> result = new Result.Success<>(songs);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<DeviceSong>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);
            }
        });
    }

    public void loadAllCheckedSong(MusicRepositoryCallback<List<CheckedSong>> callback) {
        mExecutor.execute(() -> {
            try {
                @SuppressWarnings("unchecked")
                List<CheckedSong> songs = (List<CheckedSong>) mDataManager.loadAllSongFromDevice(SongType.CHECKED_SONG);
                Result<List<CheckedSong>> result = new Result.Success<>(songs);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<CheckedSong>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);
            }
        });
    }

    public void loadDataForHeaderPlaylistPart(MusicRepositoryCallback<List<PlayList>> callback) {
        mExecutor.execute(() -> {
            try {
                List<PlayList> playLists = mDummyData.makeDataForHeaderPlayListPart();
                Result<List<PlayList>> result = new Result.Success<>(playLists);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<List<PlayList>> errorResult = new Result.Error<>(e);
                callback.onComplete(errorResult);

            }
        });
    }

    public void createPlaylist(String playlistName, List<Long> songIdList, MusicRepositoryCallback<Void> musicRepositoryCallback) {
        mExecutor.execute(() -> {
            try {
                mDataManager.createPlaylist(playlistName, songIdList);
                Result<Void> result = new Result.Success<>(null);
                musicRepositoryCallback.onComplete(result);

            } catch (Exception e) {
                Result<Void> errorResult = new Result.Error<>(e);
                musicRepositoryCallback.onComplete(errorResult);
            }
        });
    }

    public void loadPlaylistFromDevice(MusicRepositoryCallback<List<PlayList>> musicRepositoryCallback) {
        mExecutor.execute(() -> {
            try {
                List<PlayList> playLists = mDataManager.getPlayList();
                Result<List<PlayList>> result = new Result.Success<>(playLists);
                musicRepositoryCallback.onComplete(result);
            } catch (Exception e) {
                Result<List<PlayList>> errorResult = new Result.Error<>(e);
                musicRepositoryCallback.onComplete(errorResult);
            }
        });
    }

    public void removePlaylistFromDevice(PlayList playList, MusicRepositoryCallback<Void> musicRepositoryCallback) {
        mExecutor.execute(() -> {
            try {
                mDataManager.deletePlaylist(playList);
                Result<Void> result = new Result.Success<>(null);
                musicRepositoryCallback.onComplete(result);
            } catch (Exception e) {
                Result<Void> errorResult = new Result.Error<>(e);
                musicRepositoryCallback.onComplete(errorResult);
            }
        });
    }

    public void loadAllAlbum(MusicRepositoryCallback<List<Album>> musicRepositoryCallback) {
        mExecutor.execute(() -> {
            try {
                List<Album> albumList = mDataManager.loadAlbumFromDevice();
                Result<List<Album>> result = new Result.Success<>(albumList);
                musicRepositoryCallback.onComplete(result);
            } catch (Exception e) {
                Result<List<Album>> errorResult = new Result.Error<>(e);
                musicRepositoryCallback.onComplete(errorResult);
            }
        });
    }

    public void loadArtists(MusicRepositoryCallback<List<Artist>> musicRepositoryCallback) {
        mExecutor.execute(() -> {
            try {
                List<Artist> artists = mDataManager.loadArtists();
                Result<List<Artist>> result = new Result.Success<>(artists);
                musicRepositoryCallback.onComplete(result);
            } catch (Exception e) {
                Result<List<Artist>> errorResult = new Result.Error<>(e);
                musicRepositoryCallback.onComplete(errorResult);
            }
        });
    }

    public void loadTopAlbums(String artist, MusicRepositoryCallback<List<Album>> musicRepositoryCallback) {
        mExecutor.execute(() -> {
            try {
                List<Album> albums = mDummyData.makeDataForTopAlbums(artist);
                Result<List<Album>> result = new Result.Success<>(albums);
                musicRepositoryCallback.onComplete(result);
            } catch (Exception e) {
                Result<List<Album>> errorResult = new Result.Error<>(e);
                musicRepositoryCallback.onComplete(errorResult);
            }
        });
    }

    public void loadTopSongs(String artist, MusicRepositoryCallback<List<DeviceSong>> musicRepositoryCallback) {
        mExecutor.execute(() -> {
            try {
                List<DeviceSong> songs = mDummyData.makeDataForTopSongs(artist);
                Result<List<DeviceSong>> result = new Result.Success<>(songs);
                musicRepositoryCallback.onComplete(result);
            } catch (Exception e) {
                Result<List<DeviceSong>> errorResult = new Result.Error<>(e);
                musicRepositoryCallback.onComplete(errorResult);
            }
        });
    }

    public void loadGenres(MusicRepositoryCallback<List<Genre>> musicRepositoryCallback) {
        mExecutor.execute(() -> {
            try {
                List<Genre> genres = mDataManager.loadGenres();
                Result<List<Genre>> result = new Result.Success<>(genres);
                musicRepositoryCallback.onComplete(result);
            } catch (Exception e) {
                Result<List<Genre>> errorResult = new Result.Error<>(e);
                musicRepositoryCallback.onComplete(errorResult);
            }
        });
    }


    public interface MusicRepositoryCallback<T> {
        void onComplete(Result<T> result);
    }

}
