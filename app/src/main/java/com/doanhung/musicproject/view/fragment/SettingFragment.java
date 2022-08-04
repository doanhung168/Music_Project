package com.doanhung.musicproject.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.DeviceItem;
import com.doanhung.musicproject.data.repository.ResourceRepository;
import com.doanhung.musicproject.databinding.FragmentSettingBinding;
import com.doanhung.musicproject.view.OnOpenNavigationViewListener;
import com.doanhung.musicproject.view.adapter.DeviceItemAdapter;
import com.doanhung.musicproject.viewmodel.ResourceViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingFragment extends BaseFragment<FragmentSettingBinding>
        implements DeviceItemAdapter.OnClickDeviceItemListener {

    @Inject
    DeviceItemAdapter mDeviceItemAdapter;

    @Inject
    ResourceRepository mResourceRepository;
    private ResourceViewModel resourceViewModel;

    private OnOpenNavigationViewListener mOnOpenNavViewListener;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        resourceViewModel =
                new ViewModelProvider(this,
                        new ResourceViewModel.ResourceViewModelFactory(mResourceRepository))
                        .get(ResourceViewModel.class);
        setupToolbar();
        setupRcvSettings();
    }

    private void setupToolbar() {
        mOnOpenNavViewListener = (OnOpenNavigationViewListener) requireActivity();
        mBinding.toolbar.setNavigationOnClickListener(v -> mOnOpenNavViewListener.openOrCloseNavView());
    }

    private void setupRcvSettings() {
        mDeviceItemAdapter.setOnClickItemListener(this);
        mBinding.rcvSettings.setAdapter(mDeviceItemAdapter);
        resourceViewModel.loadDataForSettingFragment();
        resourceViewModel.mSettingItems.observe(getViewLifecycleOwner(), items -> {
            if (items != null) mDeviceItemAdapter.submitList(items);
        });
    }


    @Override
    public void onClickDeviceItem(DeviceItem deviceItem) {
        Toast.makeText(requireContext(), deviceItem.getName(), Toast.LENGTH_SHORT).show();
    }
}