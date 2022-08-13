package com.doanhung.musicproject.data.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.doanhung.musicproject.data.model.data_model.Song;

import java.util.List;

@Dao
public interface MusicDao {

    @Query("SELECT * FROM song")
    LiveData<List<Song>> getCurrentSongs();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSong(Song song);

    @Query("DELETE FROM song WHERE song.mId = :songId")
    void deleteSong(String songId);

}
