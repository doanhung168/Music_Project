package com.doanhung.musicproject.service;

import static com.doanhung.musicproject.util.Constraint.MUSIC_NOTIFICATION_CHANNEL_ID;
import static com.doanhung.musicproject.util.Constraint.MUSIC_SERVICE_NOTIFY_ID;
import static com.doanhung.musicproject.util.Constraint.PLAY_A_NEXT_SONG;
import static com.doanhung.musicproject.util.Constraint.PLAY_A_PREVIOUS_SONG;
import static com.doanhung.musicproject.util.Constraint.PLAY_EXTERNAL_SONG;
import static com.doanhung.musicproject.util.Constraint.PLAY_OR_PAUSE_CURRENT_SONG;
import static com.doanhung.musicproject.util.Constraint.RESTORE_UI_WITH_MUSIC_SERVICE;
import static com.doanhung.musicproject.util.Constraint.STOP_MUSIC;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.broadcast_receive.MusicBroadcastReceiver;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.util.CommonUtil;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MusicService extends Service {

    public static boolean isHasServiceRunning;

    private MediaSessionCompat mMediaSessionCompat;

    @Inject
    MusicServiceController mMusicServiceController;


    @Override
    public void onCreate() {
        super.onCreate();
        mMediaSessionCompat = new MediaSessionCompat(this, "My Music");
        isHasServiceRunning = true;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            handlerMusic(intent.getAction());
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isHasServiceRunning = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Deliver play music related actions
     *
     * @param action one of these action like play next song, play previous song, play and pause song
     */
    private void handlerMusic(String action) {
        switch (action) {
            case STOP_MUSIC: {
                mMusicServiceController.removeData();
                stopSelf();
                break;
            }
            case PLAY_A_PREVIOUS_SONG: {
                mMusicServiceController.playPreviousSong();
                showNotify(mMusicServiceController.getCurrentSong());
                break;
            }
            case PLAY_OR_PAUSE_CURRENT_SONG: {
                mMusicServiceController.playOrPauseCurrentSong();
                showNotify(mMusicServiceController.getCurrentSong());
                break;
            }
            case PLAY_A_NEXT_SONG: {
                mMusicServiceController.playNextSong();
                showNotify(mMusicServiceController.getCurrentSong());
                break;
            }
            case PLAY_EXTERNAL_SONG:
                mMusicServiceController.playExternalSong();
                showNotify(mMusicServiceController.getCurrentSong());
                break;
            case RESTORE_UI_WITH_MUSIC_SERVICE:
                mMusicServiceController.notifyMusicObserver();
                break;

        }
    }


    private void showNotify(@NonNull DeviceSong song) {

        Intent prevIntent = new Intent(this, MusicBroadcastReceiver.class).setAction(PLAY_A_PREVIOUS_SONG);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent playIntent = new Intent(this, MusicBroadcastReceiver.class).setAction(PLAY_OR_PAUSE_CURRENT_SONG);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent nextIntent = new Intent(this, MusicBroadcastReceiver.class).setAction(PLAY_A_NEXT_SONG);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_IMMUTABLE);

        Intent clearIntent = new Intent(this, MusicBroadcastReceiver.class).setAction(STOP_MUSIC);
        PendingIntent clearPendingIntent = PendingIntent.getBroadcast(this, 0, clearIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification =
                new NotificationCompat.Builder(this, MUSIC_NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(song.getName())
                        .setContentText(song.getArtist())
                        .setSmallIcon(R.drawable.ic_checked_song)
                        .setLargeIcon(CommonUtil.drawableToBitmap(song.getImage()))
                        .addAction(R.drawable.ic_previous_song_for_now_playing, "Previous", prevPendingIntent)
                        .addAction(
                                song.isPlaying() ? R.drawable.ic_pause_for_notify : R.drawable.ic_play_for_notify,
                                "PauseOrPlay",
                                playPendingIntent
                        )
                        .addAction(R.drawable.ic_next_song, "Next", nextPendingIntent)
                        .addAction(R.drawable.ic_clear__big_size, "Clear", clearPendingIntent)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                                .setShowActionsInCompactView(0, 1, 2)
                                .setMediaSession(mMediaSessionCompat.getSessionToken())
                        )
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setOnlyAlertOnce(true)
                        .build();

        startForeground(MUSIC_SERVICE_NOTIFY_ID, notification);
    }


}
