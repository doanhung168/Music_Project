package com.doanhung.musicproject.data.data_manager;

import android.content.Context;

import androidx.core.content.res.ResourcesCompat;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.app_system_model.DeviceItem;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class AppSystemData {

    private final Context mContext;

    @Inject
    public AppSystemData(@ApplicationContext Context mContext) {
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
}
