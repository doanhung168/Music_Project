package com.doanhung.musicproject.view.main_activity.home_fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.data.model.data_model.Song;
import com.doanhung.musicproject.data.repository.MusicRepository;
import com.doanhung.musicproject.databinding.FragmentHomeBinding;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.view.BaseFragment;
import com.doanhung.musicproject.view.main_activity.MainViewModel;
import com.doanhung.musicproject.view.main_activity.OnOpenNavigationViewListener;
import com.doanhung.musicproject.view.main_activity.home_fragment.adapter.HotRecommendedAdapter;
import com.doanhung.musicproject.view.main_activity.home_fragment.adapter.PlayListAdapter;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements
        PlayListAdapter.OnClickPlaylistItemListener,
        HotRecommendedAdapter.OnClickHotRecommendItemListener {

    private OnOpenNavigationViewListener mOnOpenNavViewListener;

    @Inject
    HotRecommendedAdapter mHotRecommendedAdapter;

    @Inject
    PlayListAdapter mPlayListAdapter;

    @Inject
    MusicRepository mMusicRepository;
    private HomeViewModel mHomeViewModel;

    @Inject
    MusicServiceController mMusicServiceController;
    private MainViewModel mMainViewModel;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModels();
        setupToolbar();
        setupSwipeFreshLayout();
        setupRcvHotRecommendedPart();
        setupRcvPlayListPart();

        setupListeningEvents();
        mMainViewModel.setShowMusicPlayerBar(true);
    }

    private void initViewModels() {
        mHomeViewModel = new ViewModelProvider(this,
                new HomeViewModel.HomeViewModelFactory(mMusicRepository))
                .get(HomeViewModel.class);

        mMainViewModel =
                new ViewModelProvider(requireActivity(),
                        new MainViewModel.MainViewModelFactory(requireActivity().getApplication(), mMusicServiceController))
                        .get(MainViewModel.class);
    }

    private void setupToolbar() {
        mOnOpenNavViewListener = (OnOpenNavigationViewListener) requireActivity();
        mBinding.toolbar.setNavigationOnClickListener(v -> {
            mOnOpenNavViewListener.openOrCloseNavView();
        });

        mBinding.edtSearch.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mBinding.tvHintSearch.setVisibility(View.INVISIBLE);
            } else {
                mBinding.tvHintSearch.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupSwipeFreshLayout() {
        mBinding.swipeLayout.setOnRefreshListener(() -> {
            mHomeViewModel.loadDataForHotRecommendedPart();
            mHomeViewModel.loadDataForPlaylistPart();
        });
    }


    private void setupRcvHotRecommendedPart() {
        mBinding.rcvHotRecommended.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        mHotRecommendedAdapter.setOnClickHotRecommendItemListener(this);
        mBinding.rcvHotRecommended.setAdapter(mHotRecommendedAdapter);

        if (mHomeViewModel.checkHasNoRecommendedData()) {
            mHomeViewModel.loadDataForHotRecommendedPart();
        }

        mHomeViewModel.mHotRecommendedItems.observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) mHotRecommendedAdapter.submitList(songs);
        });
    }

    private void setupRcvPlayListPart() {
        mBinding.rcvPlaylist.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );

        mPlayListAdapter.setOnClickPlaylistItemListener(this);
        mBinding.rcvPlaylist.setAdapter(mPlayListAdapter);

        if (mHomeViewModel.checkHasNoPlayListData()) {
            mHomeViewModel.loadDataForPlaylistPart();
        }

        mHomeViewModel.mPlayListItems.observe(getViewLifecycleOwner(), playLists -> {
            if (playLists != null) mPlayListAdapter.submitList(playLists);
            mBinding.swipeLayout.setRefreshing(false);
        });
    }

    private void setupListeningEvents() {
        mHomeViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            switch (event) {
                case LOADING_HOT_RECOMMENDED_DATA_FAILURE_EVENT:
                case LOADING_PLAY_LIST_DATA_FAILURE_EVENT:
                    Toast.makeText(requireContext(), event.getException().getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    mBinding.swipeLayout.setRefreshing(false);
                    break;
            }
        });
    }


    @Override
    public void onClickPlaylistItem(PlayList playList) {
        Toast.makeText(requireContext(), playList.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickHotRecommendItem(Song song) {
        Toast.makeText(requireContext(), song.getName(), Toast.LENGTH_SHORT).show();
    }
}