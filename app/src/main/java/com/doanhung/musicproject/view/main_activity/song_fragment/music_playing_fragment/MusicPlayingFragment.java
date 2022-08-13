package com.doanhung.musicproject.view.main_activity.song_fragment.music_playing_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentMusicPlayingBinding;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.util.CommonUtil;
import com.doanhung.musicproject.view.BaseFragment;
import com.doanhung.musicproject.view.main_activity.MainViewModel;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@SuppressLint("ParcelCreator")
@AndroidEntryPoint
public class MusicPlayingFragment extends BaseFragment<FragmentMusicPlayingBinding> implements
        AddSongToPlaylistDialog.OnAddSongToPlaylistListener {

    private static final String TAG = "MusicPlayingFragment";

    @Inject
    MusicServiceController mMusicServiceController;
    private MainViewModel mMainViewModel;

    @Inject
    MusicRepository mMusicRepository;
    private MusicPlayingViewModel mMusicPlayingViewModel;

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
        observePlaylist();
        listenEvents();
    }

    private void initAndAttackViewModel() {
        mMainViewModel = new ViewModelProvider(requireActivity(),
                new MainViewModel.MainViewModelFactory(requireActivity().getApplication(), mMusicServiceController))
                .get(MainViewModel.class);

        mBinding.setViewModel(mMainViewModel);

        mMusicPlayingViewModel = new ViewModelProvider(this,
                new MusicPlayingViewModel.MusicPlayingViewModelFactory(mMusicRepository))
                .get(MusicPlayingViewModel.class);
    }

    @SuppressLint("NonConstantResourceId")
    private void setupToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(v ->
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .popBackStack());

        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.add_to_playlist:
                    handleToClickAddSongToPlaylist();
                    break;
            }
            return true;
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
        });
    }

    private void observePlaylist() {
        mMusicPlayingViewModel.mPlaylists.observe(getViewLifecycleOwner(), playLists -> {
            if (playLists != null) {
                showAddPlaylistDialog(playLists);
            }
        });
    }

    private void listenEvents() {
        mMusicPlayingViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            switch (event) {
                case LOADING_PLAY_LIST_DATA_FAILURE_EVENT:
                case ADD_A_SONG_TO_PLAYLIST_FAILURE:
                    Toast.makeText(requireContext(), event.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case ADD_A_SONG_TO_PLAYLIST_SUCCESS:
                    Toast.makeText(requireContext(), "Add song to playlist successful", Toast.LENGTH_SHORT).show();
                    break;
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

    private void handleToClickAddSongToPlaylist() {
        if (mMusicPlayingViewModel.checkHasNoPlaylistData()) {
            mMusicPlayingViewModel.loadPlaylists();
        } else {
            showAddPlaylistDialog(mMusicPlayingViewModel.getPlaylists());
        }
    }

    public void showAddPlaylistDialog(List<PlayList> playLists) {
        if (playLists.size() > 0) {
            DialogFragment dialog =
                    new AddSongToPlaylistDialog(this, playLists);
            dialog.show(getParentFragmentManager(), TAG);
        } else {
            Toast.makeText(requireContext(), "You need having playlist in advance", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void OnAddSongToPlaylist(String playlistId) {
        mMusicPlayingViewModel.addSongToPlaylist(Long.parseLong(playlistId), mMainViewModel.getCurrentSong().getId());
    }

}