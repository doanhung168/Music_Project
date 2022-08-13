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
import com.doanhung.musicproject.data.model.data_model.Album;
import com.doanhung.musicproject.data.model.data_model.Artist;
import com.doanhung.musicproject.data.model.data_model.Genre;
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

                Drawable image = ContextCompat.getDrawable(mContext, R.drawable.image_playlist_sample_2);

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

    public void addSongToPlaylist(long playlistId, long songId) {

        List<DeviceSong> songs = loadSongsOfPlaylist(playlistId);
        boolean isDuplicateSong = checkDuplicateSongOfPlaylist(songId, songs);
        if (isDuplicateSong) {
            throw new IllegalArgumentException("Song have already exited in playlist");
        }

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

        ContentValues songValues = new ContentValues();
        songValues.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, count + 1);
        songValues.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, songId);

        Uri insertPlaylistUri =
                MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId);
        mContext.getContentResolver().insert(insertPlaylistUri, songValues);
        mContext.getContentResolver().notifyChange(Uri.parse("content://media"), null);
    }

    public List<DeviceSong> loadSongsOfPlaylist(long playlistId) {
        List<DeviceSong> songs = new ArrayList<>();

        String[] playListSongProjection = new String[]{
                MediaStore.Audio.Playlists.Members.AUDIO_ID,
                MediaStore.Audio.Playlists.Members.TITLE,
                MediaStore.Audio.Playlists.Members.ARTIST,
                MediaStore.Audio.Playlists.Members.ALBUM,
                MediaStore.Audio.Playlists.Members.DATA,
                MediaStore.Audio.Playlists.Members.ALBUM_ID,
                MediaStore.Audio.Playlists.Members.ARTIST_ID,
                MediaStore.Audio.Playlists.Members.DURATION
        };

        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
                playListSongProjection,
                MediaStore.Audio.Media.IS_MUSIC + " != 0",
                null,
                null
        );

        if (cursor.getCount() > 0) {
            int idIndex = cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID);
            int nameIndex = cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.TITLE);
            int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST);
            int albumIndex = cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM);
            int durationIndex = cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.DURATION);
            int dataIndex = cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.DATA);
            int albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM_ID);
            int artistIdIndex = cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST_ID);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idIndex);
                String name = cursor.getString(nameIndex);
                String artist = cursor.getString(artistIndex);
                long artistId = cursor.getLong(artistIdIndex);
                long albumId = cursor.getLong(albumIdIndex);
                Uri data = Uri.parse(cursor.getString(dataIndex));
                String album = cursor.getString(albumIndex);
                long duration = cursor.getLong(durationIndex);

                Drawable image = ContextCompat.getDrawable(mContext, R.drawable.image_playlist_sample_2);

                if (CommonUtil.isAboveVersionQ()) {
                    try {
                        image = CommonUtil.loadThumbnailForAboveVersionQ_v2(mContext, id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    image = CommonUtil.loadThumbnail(mContext, data);
                }


                DeviceSong song = new DeviceSong(id, name, data, image, artist, album, duration, false);
                song.setArtistId(artistId);
                song.setAlbumId(albumId);
                songs.add(song);
            }
        }

        cursor.close();
        return songs;
    }

    private boolean checkDuplicateSongOfPlaylist(long songId, List<DeviceSong> songs) {
        for (Song song : songs) {
            if (song.getId() == songId) {
                return true;
            }
        }
        return false;
    }

    public List<Long> getPlaylistIdsOfSong(long songId) {
        List<Long> playlistIds = new ArrayList<>();

        String[] playListProjection = new String[]{
                MediaStore.Audio.Playlists._ID
        };

        String[] playListSongProjection = new String[]{
                MediaStore.Audio.Playlists.Members.AUDIO_ID,
        };


        Cursor playlistCursor = mContext.getContentResolver()
                .query(
                        MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                        playListProjection,
                        null,
                        null,
                        null
                );

        if (playlistCursor.getCount() > 0) {
            int playlistIdIndex = playlistCursor.getColumnIndex(MediaStore.Audio.Playlists._ID);

            while (playlistCursor.moveToNext()) {

                long playlistId = playlistCursor.getLong(playlistIdIndex);

                Cursor songOfPlaylistCursor = mContext.getContentResolver().query(
                        MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
                        playListSongProjection,
                        MediaStore.Audio.Media.IS_MUSIC + " != 0",
                        null,
                        null
                );

                if (songOfPlaylistCursor.getCount() > 0) {
                    int songIdIndex = songOfPlaylistCursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID);

                    while (songOfPlaylistCursor.moveToNext()) {
                        long _songId = songOfPlaylistCursor.getLong(songIdIndex);

                        if (_songId == songId) {
                            playlistIds.add(playlistId);
                            break;
                        }
                    }
                    songOfPlaylistCursor.close();
                }
            }
            playlistCursor.close();
        }
        return playlistIds;
    }


    // ALBUMS
    public List<Album> loadAlbumFromDevice() {
        List<Album> albumList = new ArrayList<>();

        String[] albumProjection = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
        };

        Cursor loadAlbumCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                albumProjection,
                null,
                null,
                null
        );

        if (loadAlbumCursor.getCount() > 0) {
            int idIndex = loadAlbumCursor.getColumnIndex(MediaStore.Audio.Albums._ID);
            int nameIndex = loadAlbumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM);
            int imageIndex = loadAlbumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            int artistIndex = loadAlbumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST);
            int artistIdIndex = loadAlbumCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID);
            int numberOfSongIndex = loadAlbumCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS);

            while (loadAlbumCursor.moveToNext()) {
                long id = loadAlbumCursor.getLong(idIndex);
                String albumName = loadAlbumCursor.getString(nameIndex);
                Uri albumImage = Uri.parse(loadAlbumCursor.getString(imageIndex));
                String artist = loadAlbumCursor.getString(artistIndex);
                String artistId = loadAlbumCursor.getString(artistIdIndex);
                int numberOfSongs = loadAlbumCursor.getInt(numberOfSongIndex);

                Album album = new Album(id, albumName, albumImage, artist, artistId, numberOfSongs);
                albumList.add(album);
            }
        }

        loadAlbumCursor.close();

        return albumList;
    }

    // ARTIST

    public List<Artist> loadArtists() {
        List<Artist> artists = new ArrayList<>();

        String[] artistProjection = new String[]{
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
        };
        Cursor artistCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                artistProjection,
                null,
                null,
                null);

        if (artistCursor.getCount() > 0) {
            int idIndex = artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID);
            int nameIndex = artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST);
            int numberOfSongIndex = artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            int numberOfAlbumIndex = artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);

            while (artistCursor.moveToNext()) {
                long id = artistCursor.getLong(idIndex);
                String name = artistCursor.getString(nameIndex);
                int numberOfSong = artistCursor.getInt(numberOfSongIndex);
                int numberOfAlbum = artistCursor.getInt(numberOfAlbumIndex);

                Artist artist = new Artist(id, name, null, numberOfAlbum, numberOfSong);
                artists.add(artist);
            }
        }

        artistCursor.close();
        return artists;
    }

    // GENRE

    @SuppressWarnings("unchecked")
    public List<Genre> loadGenres() {
        List<Genre> genres = new ArrayList<>();

        String[] genreProjection = new String[]{
                MediaStore.Audio.Genres._ID,
                MediaStore.Audio.Genres.NAME,
        };

        List<DeviceSong> songs = (List<DeviceSong>) loadAllSongFromDevice(SongType.DEVICE_SONG);

        Cursor genreCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                genreProjection,
                null,
                null,
                null);


        if (genreCursor.getCount() > 0) {
            int idIndex = genreCursor.getColumnIndex(MediaStore.Audio.Genres._ID);
            int nameIndex = genreCursor.getColumnIndex(MediaStore.Audio.Genres.NAME);

            while (genreCursor.moveToNext()) {
                long genreId = genreCursor.getLong(idIndex);
                String genreName = genreCursor.getString(nameIndex);
                Genre genre = new Genre(genreId, genreName);

                int songOfGenre = 0;

                Cursor genreMemberCursor = mContext.getContentResolver().query(
                        MediaStore.Audio.Genres.Members.getContentUri("external", genreId),
                        new String[]{MediaStore.Audio.Media._ID},
                        MediaStore.Audio.Media.IS_MUSIC + " != 0 ",
                        null,
                        null
                );

                if (genreMemberCursor.getCount() > 0) {
                    int songIdIndex = genreMemberCursor.getColumnIndex(MediaStore.Audio.Media._ID);

                    while (genreMemberCursor.moveToNext()) {
                        long songId = genreMemberCursor.getLong(songIdIndex);

                        for (Song s : songs) {
                            if (s.getId() == songId) {
                                ++songOfGenre;
                            }
                        }
                    }

                    genreMemberCursor.close();
                }

                genre.setNumberOfSongs(songOfGenre);

                genres.add(genre);
            }
            genreCursor.close();
        }

        genreCursor.close();


        return genres;
    }

    // SONGS OF ALBUM

    public List<DeviceSong> loadSongsOfAlbum(String albumId) {
        List<DeviceSong> songs = new ArrayList<>();

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

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0" + " AND "
                + MediaStore.Audio.Media.ALBUM_ID + " = " + albumId;

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

                DeviceSong song = new DeviceSong(id, name, data, image, artist, album, duration, false);
                songs.add(song);

            }
            cursor.close();
        }
        return songs;
    }


}
