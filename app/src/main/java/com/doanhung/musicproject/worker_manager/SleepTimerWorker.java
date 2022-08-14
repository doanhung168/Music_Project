package com.doanhung.musicproject.worker_manager;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.hilt.work.HiltWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.doanhung.musicproject.service.MusicService;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.util.Constraint;

import javax.inject.Inject;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedInject;

@HiltWorker
public class SleepTimerWorker extends Worker {

    private final MusicServiceController mMusicServiceController;

    @AssistedInject
    public SleepTimerWorker(
            @Assisted @NonNull Context context,
            @Assisted @NonNull WorkerParameters workerParams,
            MusicServiceController musicServiceController
    ) {
        super(context, workerParams);
        this.mMusicServiceController = musicServiceController;
    }


    @NonNull
    @Override
    public Result doWork() {
        try {
            if (mMusicServiceController != null
                    && mMusicServiceController.getMediaPlayer() != null
                    && mMusicServiceController.getMediaPlayer().isPlaying()) {
                Intent stopMusicIntent = new Intent(getApplicationContext(), MusicService.class);
                stopMusicIntent.setAction(Constraint.STOP_MUSIC);
                getApplicationContext().startService(stopMusicIntent);
            }
        } catch (Exception e) {
            return Result.failure();
        }
        return Result.success();
    }
}
