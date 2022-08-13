package com.doanhung.musicproject.view.main_activity.song_fragment.all_song_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.app_system_model.MusicSource;
import com.doanhung.musicproject.data.model.app_system_model.ServiceMusicData;
import com.doanhung.musicproject.databinding.FragmentAllSongBinding;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.view.BaseFragment;
import com.doanhung.musicproject.view.main_activity.MainViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class AllSongFragment extends BaseFragment<FragmentAllSongBinding>
        implements AllSongAdapter.OnClickSongItemListener {

    private AllSongViewModel mAllSongViewModel;
    private MainViewModel mMainViewModel;

    @Inject
    AllSongAdapter mAllSongAdapter;

    private boolean needValidateRcvAfterRefresh;
    private boolean needValidateRcvAfterComebackWithService;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_all_song;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAndAttackViewModel();
        setupRcvAllSong();
        setupSwipeRefreshLayout();
        listenLoadingAllSongEvent();
        validateRcvFollowTrack();
    }

    private void initAndAttackViewModel() {
        mAllSongViewModel = new ViewModelProvider(requireActivity()).get(AllSongViewModel.class);
        mBinding.setViewModel(mAllSongViewModel);

        mMainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupRcvAllSong() {
        mAllSongAdapter.setOnClickSongItemListener(this);
        mBinding.rcvAllSongs.setAdapter(mAllSongAdapter);

        if (mAllSongViewModel.checkHasNoAllSongData()) {
            mAllSongViewModel.loadAllSong();
        }

        mAllSongViewModel.mSongs.observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                mAllSongAdapter.submitList(songs);
            }
            mBinding.swipeLayout.setRefreshing(false);

            if (needValidateRcvAfterRefresh) {
                validateRcvFollowTrackManual();
                needValidateRcvAfterRefresh = false;
            }

            if (needValidateRcvAfterComebackWithService) {
                validateRcvFollowTrackManual();
                needValidateRcvAfterComebackWithService = false;
            }
            mAllSongAdapter.notifyDataSetChanged();
        });
    }

    private void setupSwipeRefreshLayout() {
        mBinding.swipeLayout.setOnRefreshListener(() -> {
            needValidateRcvAfterRefresh = true;
            mAllSongViewModel.loadAllSong();
        });
    }

    private void listenLoadingAllSongEvent() {
        mAllSongViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            if (event == Event.LOADING_ALL_SONG_FROM_DEVICE_FAILURE_EVENT) {
                Toast.makeText(requireContext(), event.getException().getMessage(), Toast.LENGTH_SHORT)
                        .show();
                mBinding.swipeLayout.setRefreshing(false);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void validateRcvFollowTrack() {
        mMainViewModel.mCurrentSong.observe(getViewLifecycleOwner(), newSong -> {
            if (mAllSongViewModel.getSongs() == null) {
                needValidateRcvAfterComebackWithService = true;
            } else {
                mAllSongViewModel.validateSongsFollowTrack(
                        mMainViewModel.getMusicSource(),
                        mMainViewModel.getCurrentSong()
                );
            }
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    private void validateRcvFollowTrackManual() {
        mAllSongViewModel.validateSongsFollowTrack(
                mMainViewModel.getMusicSource(),
                mMainViewModel.getCurrentSong()
        );
    }


    @Override
    public void onClickSongItem(DeviceSong song) {
        song.setPlaying(true);
        mMainViewModel.playExternalSong(
                new ServiceMusicData(MusicSource.ALL_SONG_SOURCE, mAllSongViewModel.getSongs(), song));

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.musicPlayingFragment);
    }
}