package com.doanhung.musicproject.view.main_activity.song_fragment.genre_fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentGenreBinding;
import com.doanhung.musicproject.util.event.Event;
import com.doanhung.musicproject.view.BaseFragment;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class GenreFragment extends BaseFragment<FragmentGenreBinding> {

    private GenreViewModel mGenreViewModel;

    @Inject
    GenreAdapter mGenreAdapter;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_genre;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAndAttackViewModels();
        setupRcvGenres();
        listenEvens();
    }

    private void initAndAttackViewModels() {
        mGenreViewModel = new ViewModelProvider(this).get(GenreViewModel.class);
        mBinding.setViewModel(mGenreViewModel);
    }

    private void setupRcvGenres() {
        mBinding.rcvGenres.setAdapter(mGenreAdapter);
        mGenreViewModel.loadGenres();
        mGenreViewModel.mGenres.observe(getViewLifecycleOwner(), genres -> {
            if (genres != null) {
                mGenreAdapter.submitList(genres);
            }
        });
    }

    private void listenEvens() {
        mGenreViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            if (event == Event.LOADING_GENRE_FAILURE) {
                Toast.makeText(requireContext(), event.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}