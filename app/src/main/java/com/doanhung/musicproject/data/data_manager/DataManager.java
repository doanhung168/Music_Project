package com.doanhung.musicproject.data.data_manager;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.app_system_model.CheckedSong;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.app_system_model.SongType;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.model.data_model.Song;
import com.doanhung.musicproject.util.CommonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;


public class DataManager {

    private static final String TAG = "DataManager";

    private final Context mContext;

    @Inject
    public DataManager(@ApplicationContext Context context) {
        this.mContext = context;
    }
    // SONGS
    public List<? extends Song> loadAllSongFromDevice(final SongType songType) {
        List<Song> songs = new ArrayList<>();

        String[] projection;

        if (CommonUtil.isAboveVersionQ()) {
            projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.VOLUME_NAME
            };
        } else {
            projection = new String[]{
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ALBUM,
            };
        }

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
        );

        if (cursor != null && cursor.getCount() > 0) {

            int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int nameIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumsIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);

            int volumeNameIndex = -1;
            if (CommonUtil.isAboveVersionQ()) {
                volumeNameIndex = cursor.getColumnIndex(MediaStore.Audio.Media.VOLUME_NAME);
            }

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idIndex);
                String name = cursor.getString(nameIndex);
                Uri data = Uri.parse(cursor.getString(dataIndex));
                String album = cursor.getString(albumsIndex);
                String artist = cursor.getString(artistIndex);
                long duration = cursor.getLong(durationIndex);
                Drawable image = null;

                if (CommonUtil.isAboveVersionQ()) {
                    if (volumeNameIndex != -1) {
                        String volumeName = cursor.getString(volumeNameIndex);
                        try {
                            image = CommonUtil.loadThumbnailForAboveVersionQ(mContext, volumeName, id);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    image = CommonUtil.loadThumbnail(mContext, data);
                }

                if (songType == SongType.DEVICE_SONG) {
                    DeviceSong song = new DeviceSong(id, name, data, image, artist, album, duration, false);
                    songs.add(song);
                }

                if (songType == SongType.CHECKED_SONG) {
                    CheckedSong song = new CheckedSong(id, name, data, image, artist, album, duration, false);
                    songs.add(song);
                }

            }
            cursor.close();
        }
        return songs;
    }

    // MY PLAYLISTS
    public void deletePlaylist(@NonNull final PlayList playlist) {
        mContext.getContentResolver()
                .delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                        MediaStore.Audio.Playlists._ID + "=?",
                        new String[]{String.valueOf(playlist.getId())}
                );
    }

    public void createPlaylist(String playlistName, List<Long> songIds) {
        boolean success = checkPlaylistName(playlistName);
        if (!success) {
            throw new IllegalArgumentException("playlist name is duplicate");
        }
        addPlaylistToDevice(playlistName, songIds);
    }

    public ArrayList<PlayList> getPlayList() {
        final String[] playListProjection = new String[]{
                MediaStore.Audio.Playlists._ID,
                MediaStore.Audio.Playlists.NAME,
        };

        ArrayList<PlayList> playLists = new ArrayList<>();
        Cursor playlistCursor = mContext.getContentResolver()
                .query(
                        MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                        playListProjection,
                        null,
                        null,
                        null
                );

        if (playlistCursor.getCount() > 0) {
            int idIndex = playlistCursor.getColumnIndex(MediaStore.Audio.Playlists._ID);
            int nameIndex = playlistCursor.getColumnIndex(MediaStore.Audio.Playlists.NAME);

            while (playlistCursor.moveToNext()) {
                PlayList playList = new PlayList();
                playList.setId(playlistCursor.getLong(idIndex));
                playList.setName(playlistCursor.getString(nameIndex));
                playList.setImage(ContextCompat.getDrawable(mContext, R.drawable.image_header_playlist_sample_1));
                playLists.add(playList);
            }
        }

        playlistCursor.close();

        return playLists;
    }

    public boolean checkPlaylistName(String playlistName) {
        List<PlayList> playLists = getPlayList();
        for (int i = 0; i < playLists.size(); i++) {
            if (playlistName.equals(playLists.get(i).getName())) {
                return false;
            }
        }
        return true;
    }

    public void addPlaylistToDevice(String playlistName, List<Long> songIds) {
        Uri playlistUri = makeNewPlaylist(playlistName);
        addSongsToPlaylist(playlistUri, songIds);
    }

    private Uri makeNewPlaylist(String playlistName) {
        Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

        ContentValues playlistValue = new ContentValues();
        playlistValue.put(MediaStore.Audio.Playlists.NAME, playlistName);
        playlistValue.put(MediaStore.Audio.Media.DATE_ADDED, System.currentTimeMillis());
        playlistValue.put(MediaStore.Audio.Media.DATE_MODIFIED, System.currentTimeMillis());

        return mContext.getContentResolver().insert(playlists, playlistValue);
    }

    private void addSongsToPlaylist(Uri playListUri, List<Long> songIds) {
        long playlistId = ContentUris.parseId(playListUri);

        Cursor getTrackCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
                null,
                null,
                null,
                MediaStore.Audio.Playlists.Members.TRACK + " ASC"
        );

        long count = 0;
        int trackIndex = getTrackCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.TRACK);
        if (getTrackCursor.moveToLast()) {
            count = getTrackCursor.getLong(trackIndex);
        }
        getTrackCursor.close();

        ContentValues[] songValues = new ContentValues[songIds.size()];
        for (int i = 0; i < songIds.size(); i++) {
            songValues[i] = new ContentValues();
            songValues[i].put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, count + 1);
            songValues[i].put(MediaStore.Audio.Playlists.Members.AUDIO_ID, songIds.get(i));
        }

        Uri insertPlaylistUri =
                MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);

        mContext.getContentResolver().bulkInsert(
                insertPlaylistUri,
                songValues
        );
        mContext.getContentResolver().notifyChange(Uri.parse("content://media"), null);
    }

    // ALBUMS



}
