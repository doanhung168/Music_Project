package com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentPlayListBinding;
import com.doanhung.musicproject.view.BaseFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlayListFragment extends BaseFragment<FragmentPlayListBinding> {

    @Inject
    MusicRepository mMusicRepository;
    private PlayListViewModel mPlayListViewModel;

    @Inject
    HeaderPlayListAdapter mHeaderPlayListAdapter;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_play_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        setupRcvHeaderPlayList();
        listenEvents();
    }

    private void initViewModel() {
        mPlayListViewModel = new ViewModelProvider(requireActivity(),
                new PlayListViewModel.PlayListViewModelFactory(mMusicRepository))
                .get(PlayListViewModel.class);
    }

    private void setupRcvHeaderPlayList() {
        mBinding.rcvHeaderPlaylist.setLayoutManager(
                new GridLayoutManager(requireContext(), 2)
        );
        mBinding.rcvHeaderPlaylist.setAdapter(mHeaderPlayListAdapter);

        if (mPlayListViewModel.checkHasNoHeaderPlaylists()) {
            mPlayListViewModel.loadDataForPlaylistPart();
        }

        mPlayListViewModel.mHeaderPlaylist.observe(getViewLifecycleOwner(), playLists -> {
            if (playLists != null) mHeaderPlayListAdapter.submitList(playLists);
        });
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private void listenEvents() {
        mPlayListViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            switch (event) {
                case LOADING_HEADER_PLAYLIST_FAILURE_EVENT:
                    Toast.makeText(requireContext(), event.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }


}