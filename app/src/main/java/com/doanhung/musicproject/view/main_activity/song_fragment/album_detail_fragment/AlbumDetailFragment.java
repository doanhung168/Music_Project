package com.doanhung.musicproject.view.main_activity.song_fragment.album_detail_fragment;

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
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentAlbumDetailBinding;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.view.BaseFragment;
import com.doanhung.musicproject.view.common_adapter.SongAdapter;
import com.doanhung.musicproject.view.main_activity.MainViewModel;
import com.doanhung.musicproject.view.main_activity.song_fragment.album_fragment.AlbumViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AlbumDetailFragment extends BaseFragment<FragmentAlbumDetailBinding> implements
        SongAdapter.OnClickSongItemListener {

    @Inject
    SongAdapter mSongAdapter;

    @Inject
    MusicRepository mMusicRepository;
    private AlbumViewModel mAlbumViewModel;

    @Inject
    MusicServiceController mMusicServiceController;
    private MainViewModel mMainViewModel;
    private boolean needValidateRcvAfterComebackWithService;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_album_detail;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAndAttackViewModels();
        setupToolbar();
        setupRcvSongs();
        listenEvents();

        validateRcvFollowTrack();
    }

    private void initAndAttackViewModels() {
        mAlbumViewModel = new ViewModelProvider(requireActivity(),
                new AlbumViewModel.AlbumViewModelFactory(mMusicRepository))
                .get(AlbumViewModel.class);
        mBinding.setViewModel(mAlbumViewModel);

        mMainViewModel = new ViewModelProvider(requireActivity(),
                new MainViewModel.MainViewModelFactory
                        (requireActivity().getApplication(), mMusicServiceController))
                .get(MainViewModel.class);
    }

    private void setupToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .popBackStack();
        });
    }

    private void setupRcvSongs() {
        mSongAdapter.setOnClickSongItemListener(this);
        mBinding.rcvSongs.setAdapter(mSongAdapter);

        mAlbumViewModel.loadSongsOfAlbum();
        mAlbumViewModel.mSongsOfAlbum.observe(getViewLifecycleOwner(), deviceSongs -> {
            if (deviceSongs != null) {
                mSongAdapter.submitList(deviceSongs);
            }

            if (needValidateRcvAfterComebackWithService) {
                validateRcvFollowTrackManual();
                needValidateRcvAfterComebackWithService = false;
            }
        });

    }

    private void listenEvents() {
        mAlbumViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            if (event == Event.LOADING_SONGS_OF_ALBUM_FAILURE) {
                Toast.makeText(requireContext(), event.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateRcvFollowTrack() {
        mMainViewModel.mCurrentSong.observe(getViewLifecycleOwner(), newSong -> {
            needValidateRcvAfterComebackWithService = true;
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    private void validateRcvFollowTrackManual() {
        mAlbumViewModel.validateSongsFollowTrack(
                mMainViewModel.getMusicSource(),
                mMainViewModel.getCurrentSong()
        );
        mSongAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickSongItem(DeviceSong song) {
        song.setPlaying(true);
        mMainViewModel.playExternalSong(
                new ServiceMusicData(MusicSource.SONGS_OF_ALBUM_SOURCE, mAlbumViewModel.getSongs(), song));

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.musicPlayingFragment);
    }
}