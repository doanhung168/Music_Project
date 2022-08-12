package com.doanhung.musicproject.view.main_activity.song_fragment.playlist_adding_fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.app_system_model.CheckedSong;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentAddingMyPlaylistBinding;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.util.CommonUtil;
import com.doanhung.musicproject.view.BaseFragment;
import com.doanhung.musicproject.view.main_activity.MainViewModel;
import com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment.PlayListViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddingMyPlayListFragment extends BaseFragment<FragmentAddingMyPlaylistBinding> implements
        SelectedSongAdapter.OnClickSongListener {

    @Inject
    MusicRepository mMusicRepository;
    private AddingMyPlaylistViewModel mAddingMyPlaylistViewModel;
    private PlayListViewModel mPlaylistViewModel; // It is not good way

    @Inject
    MusicServiceController mMusicServiceController;
    private MainViewModel mMainViewModel;


    @Inject
    SelectedSongAdapter mSelectedSongAdapter;

    private List<Long> mSongIdList;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_adding_my_playlist;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModels();
        mMainViewModel.setShowMusicPlayerBar(false);

        setupToolbar();
        setupRcvSong();
        listenEvents();
        setupClickBtnAddPlaylist();
    }

    private void initViewModels() {
        mAddingMyPlaylistViewModel = new ViewModelProvider(this,
                new AddingMyPlaylistViewModel.AddingMyPlaylistViewModelFactory(mMusicRepository))
                .get(AddingMyPlaylistViewModel.class);

        mMainViewModel = new ViewModelProvider(requireActivity(),
                new MainViewModel.MainViewModelFactory(requireActivity().getApplication(), mMusicServiceController))
                .get(MainViewModel.class);

        mPlaylistViewModel = new ViewModelProvider(requireActivity(),
                new PlayListViewModel.PlayListViewModelFactory(mMusicRepository))
                .get(PlayListViewModel.class);

        mBinding.setViewModel(mAddingMyPlaylistViewModel);
    }

    private void setupToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .popBackStack();
        });
    }


    private void setupRcvSong() {
        mSelectedSongAdapter.setOnCheckedSongListener(this);
        mBinding.rcvSongs.setAdapter(mSelectedSongAdapter);

        mAddingMyPlaylistViewModel.loadAllSong();
        mAddingMyPlaylistViewModel.mSongs.observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                mSelectedSongAdapter.submitList(songs);
            }
        });
    }

    private void listenEvents() {
        mAddingMyPlaylistViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            switch (event) {
                case LOADING_ALL_SONG_FROM_DEVICE_FAILURE_EVENT:
                case ADD_PLAYLIST_FAILURE_EVENT:
                    Toast.makeText(requireContext(), event.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case ADD_PLAYLIST_SUCCESS_EVENT:
                    Toast.makeText(requireContext(), "Add Playlist Successful", Toast.LENGTH_SHORT).show();
                    mPlaylistViewModel.reloadMyPlaylistData();
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                            .popBackStack();
                    break;
            }
        });

    }


    private void setupClickBtnAddPlaylist() {
        mBinding.btnAddPlaylist.setOnClickListener(v -> {
            String playlistName = mBinding.edtPlaylistName.getText().toString().trim();
            if (TextUtils.isEmpty(playlistName)) {
                Toast.makeText(requireContext(), "Invalid playlist name", Toast.LENGTH_SHORT).show();
                return;
            }
            mSongIdList = getSongIdList(mSelectedSongAdapter.getCurrentList());

            mAddingMyPlaylistViewModel.addPlayListToDevice(playlistName, mSongIdList);
        });

    }

    @Override
    public void onClickSong(CheckedSong song) {
        song.setChecked(!song.isChecked());
        mSelectedSongAdapter.notifyItemChanged(
                CommonUtil.findItemIndex(song, mSelectedSongAdapter.getCurrentList())
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMainViewModel.setShowMusicPlayerBar(true);
    }

    public List<Long> getSongIdList(List<CheckedSong> songList) {
        List<Long> songIdList = new ArrayList<>();
        for (CheckedSong checkedSong : songList) {
            if (checkedSong.isChecked()) {
                songIdList.add(checkedSong.getId());
            }
        }
        return songIdList;
    }

}
