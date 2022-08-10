package com.doanhung.musicproject;

import static com.doanhung.musicproject.util.Constraint.MUSIC_NOTIFICATION_CHANNEL_ID;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MusicApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createMusicNotificationChanel();
    }

    private void createMusicNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(MUSIC_NOTIFICATION_CHANNEL_ID,
                    "Music Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setSound(null, null);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
