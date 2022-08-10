package com.doanhung.musicproject.broadcast_receive;

import static com.doanhung.musicproject.util.Constraint.STOP_MUSIC;
import static com.doanhung.musicproject.util.Constraint.PLAY_A_NEXT_SONG;
import static com.doanhung.musicproject.util.Constraint.PLAY_OR_PAUSE_CURRENT_SONG;
import static com.doanhung.musicproject.util.Constraint.PLAY_A_PREVIOUS_SONG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.doanhung.musicproject.service.MusicService;

public class MusicBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MusicService.class);
        switch (intent.getAction()) {
            case PLAY_A_PREVIOUS_SONG: {
                serviceIntent.setAction(PLAY_A_PREVIOUS_SONG);
                break;
            }
            case PLAY_OR_PAUSE_CURRENT_SONG: {
                serviceIntent.setAction(PLAY_OR_PAUSE_CURRENT_SONG);
                break;
            }
            case PLAY_A_NEXT_SONG: {
                serviceIntent.setAction(PLAY_A_NEXT_SONG);
                break;
            }
            case STOP_MUSIC: {
                serviceIntent.setAction(STOP_MUSIC);
                break;
            }
        }
        context.startService(serviceIntent);
    }
}
