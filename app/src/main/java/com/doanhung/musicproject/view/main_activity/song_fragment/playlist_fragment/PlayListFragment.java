package com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.databinding.FragmentPlayListBinding;
import com.doanhung.musicproject.view.BaseFragment;


public class PlayListFragment extends BaseFragment<FragmentPlayListBinding> {

    @Override
    public int getFragmentView() {
        return R.layout.fragment_play_list;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}