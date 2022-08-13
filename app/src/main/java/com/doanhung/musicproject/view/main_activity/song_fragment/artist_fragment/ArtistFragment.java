package com.doanhung.musicproject.view.main_activity.song_fragment.artist_fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.data_model.Artist;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentArtistBinding;
import com.doanhung.musicproject.view.BaseFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ArtistFragment extends BaseFragment<FragmentArtistBinding> implements ArtistAdapter.OnClickArtistItemListener, PopupMenu.OnMenuItemClickListener {

    private ArtistViewModel mArtistViewModel;

    @Inject
    ArtistAdapter mArtistAdapter;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_artist;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAndAttackViewModel();
        setupRcvArtists();

    }

    private void initAndAttackViewModel() {
        mArtistViewModel = new ViewModelProvider(requireActivity()).get(ArtistViewModel.class);
        mBinding.setViewModel(mArtistViewModel);
    }

    private void setupRcvArtists() {
        mArtistAdapter.setOnClickArtistItemListener(this);
        mBinding.rcvArtist.setAdapter(mArtistAdapter);

        if (mArtistViewModel.checkHasNoArtists()) {
            mArtistViewModel.loadArtist();
        }

        mArtistViewModel.mArtists.observe(getViewLifecycleOwner(), artists -> {
            if (artists != null) {
                mArtistAdapter.submitList(artists);
            }
        });
    }


    @Override
    public void onClickArtistMenu(View view, Artist artist) {
        showPopupMenu(view);
    }

    @Override
    public void onClickArtistItem(Artist artist) {
        mArtistViewModel.setCurrentArtist(artist);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.artistDetailFragment);
    }

    private void showPopupMenu(View view) {
        Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        popupMenu.inflate(R.menu.menu_album_fragment);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.play_menu_item:
                break;
            case R.id.play_next_menu_item:
                break;
            case R.id.add_to_playing_queue_menu_item:
                break;
            case R.id.add_to_playlist_menu_item:
                break;
            case R.id.rename_menu_item:
                break;
            case R.id.tag_editor_menu_item:
                break;
            case R.id.go_to_artist_menu_item:
                break;
            case R.id.delete_from_device_menu_item:
                break;
            case R.id.share_menu_item:
                break;
        }
        return true;
    }
}