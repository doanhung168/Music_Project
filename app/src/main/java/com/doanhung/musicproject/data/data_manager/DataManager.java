package com.doanhung.musicproject.data.data_manager;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.util.CommonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;


public class LoadDataManager {
    private final Context mContext;

    @Inject
    public LoadDataManager(@ApplicationContext Context context) {
        this.mContext = context;
    }

    public List<DeviceSong> loadAllSongFromDevice() {
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

                DeviceSong song = new DeviceSong(id, name, data, image, artist, album, duration, false);
                songs.add(song);
            }
            cursor.close();
        }
        return songs;
    }

}
