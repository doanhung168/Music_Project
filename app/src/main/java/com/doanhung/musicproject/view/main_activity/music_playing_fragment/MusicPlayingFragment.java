package com.doanhung.musicproject.view.main_activity.music_playing_fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.databinding.FragmentMusicPlayingBinding;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.util.CommonUtil;
import com.doanhung.musicproject.view.BaseFragment;
import com.doanhung.musicproject.view.main_activity.MainViewModel;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class MusicPlayingFragment extends BaseFragment<FragmentMusicPlayingBinding> {

    private static final String TAG = "MusicPlayingFragment";

    @Inject
    MusicServiceController musicServiceController;
    private MainViewModel mMainViewModel;

    @Inject
    ScheduledExecutorService mScheduledExecutorService;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_music_playing;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAndAttackViewModel();

        //hide music player bar
        mMainViewModel.setShowMusicPlayerBar(false);

        setupToolbar();
        observeCurrentSong();
    }

    private void initAndAttackViewModel() {
        mMainViewModel = new ViewModelProvider(requireActivity(),
                new MainViewModel.MainViewModelFactory(requireActivity().getApplication(), musicServiceController))
                .get(MainViewModel.class);

        mBinding.setViewModel(mMainViewModel);
    }

    private void setupToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(v ->
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .popBackStack());
    }

    private void observeCurrentSong() {
        mMainViewModel.mCurrentSong.observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                mBinding.chkRepeat.setChecked(mMainViewModel.getIsRepeat());
                mBinding.chkShuffle.setChecked(mMainViewModel.getIsShuffle());
                handlerUIFollowCurrentSong(song);
                updateProgressOfSong();
            } else {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .popBackStack();
            }
        });
    }

    private void handlerUIFollowCurrentSong(DeviceSong song) {
        mBinding.musicCircleView.setDuration(song.getDuration());
        mBinding.musicCircleView.setOnMusicBarChangeListener(currentPosition -> {
            if (mMainViewModel.getMediaPlayer() != null) {
                mMainViewModel.getMediaPlayer().seekTo((int) currentPosition);
            }
        });
    }

    private void updateProgressOfSong() {
        mScheduledExecutorService.scheduleWithFixedDelay(() -> {
            if (mMainViewModel.getCurrentSong() != null) {
                int currentPosition = mMainViewModel.getMediaPlayer().getCurrentPosition();
                requireActivity().runOnUiThread(() -> {
                    if (mBinding != null) {
                        mBinding.musicCircleView.setCurrentPosition(currentPosition);
                        mBinding.tvCurrentPosition.setText(CommonUtil.getTime(currentPosition));
                    }
                });

            }
        }, 0, 999, TimeUnit.MILLISECONDS);
    }


    @Override
    public void onDestroyView() {
        mBinding.musicCircleView.removeOnMusicBarChangeListener();
        mMainViewModel.setShowMusicPlayerBar(true);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdown();
        }
    }
}