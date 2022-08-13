package com.doanhung.musicproject.view.main_activity.song_fragment.artist_detail_fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentArtistDetailBinding;
import com.doanhung.musicproject.util.CommonUtil;
import com.doanhung.musicproject.view.BaseFragment;
import com.doanhung.musicproject.view.common_adapter.SongAdapter;
import com.doanhung.musicproject.view.main_activity.song_fragment.artist_fragment.ArtistViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ArtistDetailFragment extends BaseFragment<FragmentArtistDetailBinding> {

    private ArtistViewModel mArtistViewModel;

    @Inject
    TopAlbumAdapter mTopAlbumAdapter;

    @Inject
    SongAdapter mSongAdapter;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_artist_detail;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModels();
        setupToolbar();
        setupBanned();
        setupRcvTopAlbums();
        setupRcvTopSongs();
    }

    private void initViewModels() {
        mArtistViewModel = new ViewModelProvider(requireActivity()).get(ArtistViewModel.class);
    }

    private void setupToolbar() {
        mBinding.toolbar.setNavigationOnClickListener(v -> {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .popBackStack();
        });
    }

    private void setupBanned() {
        mBinding.imvArtistDetailBanned.setImageDrawable(
                CommonUtil.blurDrawable(requireContext(),
                        ContextCompat.getDrawable(requireContext(), R.drawable.image_header_playlist_sample_1))
        );
        mBinding.btnFollow.setBackgroundTintList(null);
        mArtistViewModel.mCurrentArtist.observe(getViewLifecycleOwner(), artist -> {
            mBinding.tvArtistName.setText(artist.getName());
        });
    }

    private void setupRcvTopAlbums() {
        mBinding.rcvTopAlbums.setAdapter(mTopAlbumAdapter);
        mArtistViewModel.loadTopAlbums();
        mArtistViewModel.mTopAlbumsOfArtist.observe(getViewLifecycleOwner(), albums -> {
            if (albums != null) {
                mTopAlbumAdapter.submitList(albums);
            }
        });
    }

    private void setupRcvTopSongs() {
        mBinding.rcvTopSongs.setAdapter(mSongAdapter);
        mArtistViewModel.loadTopSongs();
        mArtistViewModel.mTopSongOfArtist.observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) {
                mSongAdapter.submitList(songs);
            }
        });
    }
}