package com.doanhung.musicproject.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.doanhung.musicproject.data.model.data_model.Song;

@Database(entities = {Song.class}, version = 1)
public abstract class MusicDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "Music.db";

    public abstract MusicDao musicDao();

    private static MusicDatabase instance;

    public static MusicDatabase getInstance(Context context) {
        synchronized (MusicDatabase.class) {
            if (instance == null) {
                instance = Room.databaseBuilder(context, MusicDatabase.class, DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return instance;
    }

}
