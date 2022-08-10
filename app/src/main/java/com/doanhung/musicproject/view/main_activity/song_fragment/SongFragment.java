package com.doanhung.musicproject.view.main_activity.song_fragment;

import static com.doanhung.musicproject.util.Constraint.ALBUM_FRAGMENT_INDEX;
import static com.doanhung.musicproject.util.Constraint.ALL_SONG_FRAGMENT_INDEX;
import static com.doanhung.musicproject.util.Constraint.ARTIST_FRAGMENT_INDEX;
import static com.doanhung.musicproject.util.Constraint.GENRE_FRAGMENT_INDEX;
import static com.doanhung.musicproject.util.Constraint.PLAYLIST_FRAGMENT_INDEX;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.databinding.FragmentSongBinding;
import com.doanhung.musicproject.view.BaseFragment;
import com.doanhung.musicproject.view.main_activity.OnOpenNavigationViewListener;
import com.google.android.material.tabs.TabLayoutMediator;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SongFragment extends BaseFragment<FragmentSongBinding> {

    private OnOpenNavigationViewListener mOnOpenNavViewListener;
    private SongFragmentPagerAdapter mSongFragmentPagerAdapter;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_song;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSongFragmentPagerAdapter = new SongFragmentPagerAdapter(requireActivity());
        setupToolbar();
        setupViewPagerAndTabLayout();
    }


    private void setupToolbar() {
        mOnOpenNavViewListener = (OnOpenNavigationViewListener) requireActivity();
        mBinding.toolbar.setNavigationOnClickListener(v -> mOnOpenNavViewListener.openOrCloseNavView());
        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.searchFragment) {
                Toast.makeText(requireContext(), "Go to search fragment", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void setupViewPagerAndTabLayout() {
        mBinding.viewPagerSongs.setAdapter(mSongFragmentPagerAdapter);
        new TabLayoutMediator(mBinding.tabLayoutSongs, mBinding.viewPagerSongs, (tab, position) -> {
            switch (position) {
                case ALL_SONG_FRAGMENT_INDEX:
                    tab.setText(getString(R.string.all_song));
                    break;
                case PLAYLIST_FRAGMENT_INDEX:
                    tab.setText(getString(R.string.playlist));
                    break;
                case ALBUM_FRAGMENT_INDEX:
                    tab.setText(getString(R.string.albums));
                    break;
                case ARTIST_FRAGMENT_INDEX:
                    tab.setText(getString(R.string.artists));
                    break;
                case GENRE_FRAGMENT_INDEX:
                    tab.setText(getString(R.string.genres));
                    break;
                default:
                    break;
            }
        }).attach();
    }

}