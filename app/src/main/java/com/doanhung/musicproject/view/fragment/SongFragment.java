package com.doanhung.musicproject.view.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.databinding.FragmentSongBinding;
import com.doanhung.musicproject.view.OnOpenNavigationViewListener;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SongFragment extends BaseFragment<FragmentSongBinding> {

    private OnOpenNavigationViewListener mOnOpenNavViewListener;


    @Override
    public int getFragmentView() {
        return R.layout.fragment_song;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();


    }

    private void setupToolbar() {
        mOnOpenNavViewListener = (OnOpenNavigationViewListener) requireActivity();
        mBinding.toolbar.setNavigationOnClickListener(v -> mOnOpenNavViewListener.openOrCloseNavView());
    }
}