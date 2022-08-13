package com.doanhung.musicproject.view.main_activity.song_fragment.music_playing_in_playlist_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.app_system_model.MusicSource;
import com.doanhung.musicproject.data.model.app_system_model.ServiceMusicData;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentMusicPlayingPlaylistBinding;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.util.CommonUtil;
import com.doanhung.musicproject.view.BaseFragment;
import com.doanhung.musicproject.view.main_activity.MainViewModel;
import com.doanhung.musicproject.view.main_activity.song_fragment.all_song_fragment.AllSongAdapter;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MusicPlayingPlaylistFragment extends BaseFragment<FragmentMusicPlayingPlaylistBinding> implements AllSongAdapter.OnClickSongItemListener {

    private MainViewModel mMainViewModel;
    private MusicPlayingPlaylistViewModel mMusicPlayingPlaylistViewModel;

    @Inject
    AllSongAdapter mAllSongAdapter;

    @Inject
    ScheduledExecutorService mScheduledExecutorService;

    private long mPlaylistId = -1;
    private boolean needValidateRcvAfterComebackWithService;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_music_playing_playlist;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAndAttackViewModels();
        getData();
        hideMusicPlayerBar();

        setupToolbar();
        setupRcvSongs();
        observeCurrentSong();
    }

    private void initAndAttackViewModels() {
        mMainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mBinding.setMainViewModel(mMainViewModel);

        mMusicPlayingPlaylistViewModel = new ViewModelProvider(this).get(MusicPlayingPlaylistViewModel.class);
    }

    private void hideMusicPlayerBar() {
        mMainViewModel.setShowMusicPlayerBar(false);
    }

    private void getData() {
        mPlaylistId = MusicPlayingPlaylistFragmentArgs.fromBundle(getArguments()).getPlaylistId();
    }

    private void setupToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .popBackStack();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRcvSongs() {
        mAllSongAdapter.setOnClickSongItemListener(this);
        mBinding.rcvSongs.setAdapter(mAllSongAdapter);
        mMusicPlayingPlaylistViewModel.loadSongsOfPlaylist(mPlaylistId);
        mMusicPlayingPlaylistViewModel.mSongOfPlaylist.observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                mAllSongAdapter.submitList(songs);

                if (needValidateRcvAfterComebackWithService) {
                    needValidateRcvAfterComebackWithService = false;
                    validateRcvFollowTrackManual();
                }

                mAllSongAdapter.notifyDataSetChanged();
            }
        });
    }

    private void observeCurrentSong() {
        mMainViewModel.mCurrentSong.observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                handlerUIFollowCurrentSong(song);
                updateProgressOfSong();
            } else {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .popBackStack();
            }

            if (mMusicPlayingPlaylistViewModel.getSongs() == null) {
                needValidateRcvAfterComebackWithService = true;
            } else {
                validateRcvFollowTrackManual();
            }
        });
    }

    private void handlerUIFollowCurrentSong(DeviceSong song) {
        mBinding.musicPlayerCircle.setDuration(song.getDuration());
        mBinding.musicPlayerCircle.setOnMusicBarChangeListener(currentPosition -> {
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
                        mBinding.musicPlayerCircle.setCurrentPosition(currentPosition);
                        mBinding.tvCurrentPosition.setText(CommonUtil.getTime(currentPosition));
                    }
                });

            }
        }, 0, 999, TimeUnit.MILLISECONDS);
    }

    private void validateRcvFollowTrackManual() {
        mMusicPlayingPlaylistViewModel.validateSongsFollowTrack(
                mMainViewModel.getMusicSource(),
                mMainViewModel.getCurrentSong()
        );
    }

    @Override
    public void onClickSongItem(DeviceSong song) {
        song.setPlaying(true);
        mMainViewModel.playExternalSong(
                new ServiceMusicData(MusicSource.SONGS_OF_PLAYLIST_SOURCE, mMusicPlayingPlaylistViewModel.getSongs(), song));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mScheduledExecutorService != null) {
            mScheduledExecutorService.shutdown();
        }
    }
}