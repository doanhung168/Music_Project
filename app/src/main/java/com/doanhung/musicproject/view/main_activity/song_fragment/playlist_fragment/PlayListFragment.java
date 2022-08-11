package com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentPlayListBinding;
import com.doanhung.musicproject.view.BaseFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlayListFragment extends BaseFragment<FragmentPlayListBinding> implements
        MyPlaylistAdapter.OnClickPlaylist {

    private static final String TAG = "PlayListFragment";

    @Inject
    MusicRepository mMusicRepository;
    private PlayListViewModel mPlayListViewModel;

    @Inject
    MyPlaylistAdapter mMyPlaylistAdapter;

    @Inject
    HeaderPlayListAdapter mHeaderPlayListAdapter;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_play_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refactorView();

        initViewModel();
        setupRcvHeaderPlayList();
        setupRcvPlaylist();

        listenEvents();
    }

    private void refactorView() {
        mBinding.btnToAddingPlaylistFrag.setImageTintList(null);
    }

    private void initViewModel() {
        mPlayListViewModel = new ViewModelProvider(requireActivity(),
                new PlayListViewModel.PlayListViewModelFactory(mMusicRepository))
                .get(PlayListViewModel.class);

        mBinding.setViewModel(mPlayListViewModel);
    }

    private void setupRcvHeaderPlayList() {
        mBinding.rcvHeaderPlaylist.setLayoutManager(
                new GridLayoutManager(requireContext(), 2)
        );
        mBinding.rcvHeaderPlaylist.setAdapter(mHeaderPlayListAdapter);

        if (mPlayListViewModel.checkHasNoHeaderPlaylists()) {
            mPlayListViewModel.loadDataForHeaderPlaylistPart();
        }

        mPlayListViewModel.mHeaderPlaylist.observe(getViewLifecycleOwner(), playLists -> {
            if (playLists != null) mHeaderPlayListAdapter.submitList(playLists);
        });
    }


    @SuppressLint("NotifyDataSetChanged")
    private void setupRcvPlaylist() {
        mBinding.rcvPlaylist.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        mMyPlaylistAdapter.setOnClickPlaylist(this);
        mBinding.rcvPlaylist.setAdapter(mMyPlaylistAdapter);

        if (mPlayListViewModel.checkHasNoMyPlaylists()) {
            mPlayListViewModel.loadPlaylistFromDevice();
        }

        mPlayListViewModel.mPlaylist.observe(getViewLifecycleOwner(), playLists -> {
            if (playLists != null) {
                mMyPlaylistAdapter.submitList(playLists);
                mMyPlaylistAdapter.notifyDataSetChanged(); // temporary way for remove item
            }
        });

    }


    private void listenEvents() {
        mPlayListViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            switch (event) {
                case LOADING_HEADER_PLAYLIST_FAILURE_EVENT:
                case LOADING_MY_PLAYLIST_FAILURE_EVENT:
                case REMOVE_PLAYLIST_FAILURE:
                    Toast.makeText(requireContext(), event.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    break;
                case SHOW_PLAYLIST_DIALOG:
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                            .navigate(R.id.addingMyPlayListFragment);
                    break;
                case REMOVE_PLAYLIST_SUCCESS:
                    Toast.makeText(requireContext(), "Remove playlist successful", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    public boolean onDeletePlaylist(PlayList playList) {
        mPlayListViewModel.removePlaylistFromDevice(playList);
        mPlayListViewModel.reloadMyPlaylistData();
        return true;
    }
}