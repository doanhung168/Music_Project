package com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.databinding.FragmentAddingMyPlaylistBinding;
import com.doanhung.musicproject.view.BaseFragment;

public class AddingMyPlayListFragment extends BaseFragment<FragmentAddingMyPlaylistBinding> {

    @Override
    public int getFragmentView() {
        return R.layout.fragment_adding_my_playlist;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModels();
    }

    private void initViewModels() {

    }
}
