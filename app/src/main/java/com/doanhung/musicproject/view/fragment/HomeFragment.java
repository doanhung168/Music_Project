package com.doanhung.musicproject.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.repository.ResourceRepository;
import com.doanhung.musicproject.databinding.FragmentHomeBinding;
import com.doanhung.musicproject.view.OnOpenNavigationViewListener;
import com.doanhung.musicproject.view.adapter.HotRecommendedAdapter;
import com.doanhung.musicproject.viewmodel.ResourceViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {

    private OnOpenNavigationViewListener mOnOpenNavViewListener;


    @Inject
    HotRecommendedAdapter mHotRecommendedAdapter;

    @Inject
    ResourceRepository mResourceRepository;
    private ResourceViewModel mResourceViewModel;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModels();
        setupToolbar();
        setupRcvHotRecommendedPart();
        setupListeningEvents();
    }

    private void initViewModels() {
        mResourceViewModel = new ViewModelProvider(getViewModelStore(),
                new ResourceViewModel.ResourceViewModelFactory(mResourceRepository))
                .get(ResourceViewModel.class);

    }

    private void setupToolbar() {
        mOnOpenNavViewListener = (OnOpenNavigationViewListener) requireActivity();
        mBinding.toolbar.setNavigationOnClickListener(v -> {
            mOnOpenNavViewListener.openOrCloseNavView();
        });
    }


    private void setupRcvHotRecommendedPart() {
        mBinding.rcvHotRecommended.setLayoutManager(
                new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        mBinding.rcvHotRecommended.setAdapter(mHotRecommendedAdapter);
        mResourceViewModel.loadDataForHotRecommendedPart();
        mResourceViewModel.mHotRecommendedItems.observe(getViewLifecycleOwner(), songs -> {
            if (songs != null) mHotRecommendedAdapter.submitList(songs);
        });
    }

    @SuppressWarnings("SwitchStatementWithTooFewBranches")
    private void setupListeningEvents() {
        mResourceViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            switch (event) {
                case LOADING_HOT_RECOMMENDED_PART_FAILURE_EVENT:
                    Toast.makeText(requireContext(), "Load recommended data failure", Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        });
    }


}