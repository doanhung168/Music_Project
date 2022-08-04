package com.doanhung.musicproject.data;

import android.content.Context;

import androidx.core.content.res.ResourcesCompat;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.DeviceItem;
import com.doanhung.musicproject.data.model.Song;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class DummyData {

    private final Context mContext;

    @Inject
    public DummyData(@ApplicationContext Context mContext) {
        this.mContext = mContext;
    }

    public List<DeviceItem> makeDataForNavView() {
        List<DeviceItem> navViewDeviceItems = new ArrayList<>();

        DeviceItem themeDeviceItem = new DeviceItem(
                mContext.getString(R.string.themes),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_paint, null)
        );

        DeviceItem ringtoneDeviceItem = new DeviceItem(
                mContext.getString(R.string.ringtone_cutter),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_scissors, null)
        );

        DeviceItem sleepTimerDeviceItem = new DeviceItem(
                mContext.getString(R.string.sleep_timer),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_stopwatch, null)
        );

        DeviceItem equliserDeviceItem = new DeviceItem(
                mContext.getString(R.string.equliser),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_sound_bars, null)
        );

        DeviceItem driveModeDeviceItem = new DeviceItem(
                mContext.getString(R.string.drive_mode),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_car, null)
        );

        DeviceItem hiddenFoldersDeviceItem = new DeviceItem(
                mContext.getString(R.string.hidden_folders),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_folder, null)
        );

        DeviceItem scanMediaDeviceItem = new DeviceItem(
                mContext.getString(R.string.scan_media),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_scanner, null)
        );

        navViewDeviceItems.add(themeDeviceItem);
        navViewDeviceItems.add(ringtoneDeviceItem);
        navViewDeviceItems.add(sleepTimerDeviceItem);
        navViewDeviceItems.add(equliserDeviceItem);
        navViewDeviceItems.add(driveModeDeviceItem);
        navViewDeviceItems.add(hiddenFoldersDeviceItem);
        navViewDeviceItems.add(scanMediaDeviceItem);

        return navViewDeviceItems;
    }

    public List<DeviceItem> makeDataForSettingFragment() {
        List<DeviceItem> settingDeviceItems = new ArrayList<>();

        DeviceItem youtubeDeviceItem = new DeviceItem(
                mContext.getString(R.string.display),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_youtube, null)
        );

        DeviceItem audioDeviceItem = new DeviceItem(
                mContext.getString(R.string.audio),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_volume, null)
        );

        DeviceItem headsetDeviceItem = new DeviceItem(
                mContext.getString(R.string.headset),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_headphones, null)
        );

        DeviceItem lockScreenDeviceItem = new DeviceItem(
                mContext.getString(R.string.lockscreen),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_padlock, null)
        );

        DeviceItem advancedDeviceItem = new DeviceItem(
                mContext.getString(R.string.advanced),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_menu, null)
        );

        DeviceItem otherDeviceItem = new DeviceItem(
                mContext.getString(R.string.other),
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_settings, null)
        );

        settingDeviceItems.add(youtubeDeviceItem);
        settingDeviceItems.add(audioDeviceItem);
        settingDeviceItems.add(headsetDeviceItem);
        settingDeviceItems.add(lockScreenDeviceItem);
        settingDeviceItems.add(advancedDeviceItem);
        settingDeviceItems.add(otherDeviceItem);

        return settingDeviceItems;
    }

    public List<Song> makeDataForHotRecommended() {
        List<Song> songList = new ArrayList<>();

        Song song1 = new Song(1, "Sound of Sky",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_sample_1, null),
                "Dilon Bruce", 0, 0);

        Song song2 = new Song(2, "Girl on Fire",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_sample_1, null),
                "Alecia Keys", 0, 0);

        Song song3 = new Song(3, "Sound of Sky",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_sample_1, null),
                "Dilon Bruce", 0, 0);

        Song song4 = new Song(4, "Sound of Sky",
                ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.image_sample_1, null),
                "Dilon Bruce", 0, 0);

        songList.add(song1);
        songList.add(song2);
        songList.add(song3);
        songList.add(song4);

        return songList;
    }
}
