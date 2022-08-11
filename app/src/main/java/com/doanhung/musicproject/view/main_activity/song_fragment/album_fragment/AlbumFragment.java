package com.doanhung.musicproject.view.main_activity.song_fragment.album_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.data_model.Album;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentAlbumBinding;
import com.doanhung.musicproject.view.BaseFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AlbumFragment extends BaseFragment<FragmentAlbumBinding> implements
        AlbumAdapter.OnClickAlbumItemListener, PopupMenu.OnMenuItemClickListener {

    @Inject
    MusicRepository mMusicRepository;
    private AlbumViewModel mAlbumViewModel;

    @Inject
    AlbumAdapter mAlbumAdapter;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_album;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModels();
        setupRcvAlbums();

    }

    private void initViewModels() {
        mAlbumViewModel = new ViewModelProvider(this,
                new AlbumViewModel.AlbumViewModelFactory(mMusicRepository))
                .get(AlbumViewModel.class);
    }

    private void setupRcvAlbums() {
        mBinding.rcvAlbums.setLayoutManager(
                new GridLayoutManager(requireContext(), 2)
        );

        mAlbumAdapter.setOnClickAlbumItemListener(this);
        mBinding.rcvAlbums.setAdapter(mAlbumAdapter);

        mAlbumViewModel.loadAlbumFromDevice();
        mAlbumViewModel.mAlbums.observe(getViewLifecycleOwner(), albums -> {
            if (albums != null) mAlbumAdapter.submitList(albums);
        });
    }

    @Override
    public void onClickAlbumMenu(View view, Album album) {
        showPopupMenu(view);
    }

    private void showPopupMenu(View view) {
        Context wrapper = new ContextThemeWrapper(getContext(), R.style.PopupMenu);
        final PopupMenu popup = new PopupMenu(wrapper, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_album_fragment);
        popup.show();
    }

    @SuppressLint("NonConstantResourceId")
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