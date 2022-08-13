package com.doanhung.musicproject.broadcast_receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.doanhung.musicproject.service.MusicService;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.util.Constraint;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChargeReceive extends BroadcastReceiver {

    private AlertDialog mChargeDialog;

    @Inject
    MusicServiceController musicServiceController;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
            if (musicServiceController != null
                    && musicServiceController.getMediaPlayer() != null
                    && musicServiceController.getMediaPlayer().isPlaying()) {

                mChargeDialog = new AlertDialog.Builder(context)
                        .setMessage("Phone is charging, stop music to protect battery")
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try {
                                Intent stopMusicIntent = new Intent(context.getApplicationContext(), MusicService.class);
                                stopMusicIntent.setAction(Constraint.STOP_MUSIC);
                                context.startService(stopMusicIntent);
                            } catch (Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .create();
                mChargeDialog.show();
            }

        }

        if (intent.getAction().equals(Intent.ACTION_POWER_DISCONNECTED)) {
            if (mChargeDialog != null) {
                mChargeDialog.dismiss();
            }
        }
    }
}
