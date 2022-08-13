package com.doanhung.musicproject.view.main_activity.setting_fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.app_system_model.DeviceItem;
import com.doanhung.musicproject.databinding.FragmentSettingBinding;
import com.doanhung.musicproject.di.AppModule;
import com.doanhung.musicproject.view.BaseFragment;
import com.doanhung.musicproject.view.common_adapter.DeviceItemAdapter;
import com.doanhung.musicproject.view.main_activity.OnOpenNavigationViewListener;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingFragment extends BaseFragment<FragmentSettingBinding>
        implements DeviceItemAdapter.OnClickDeviceItemListener {

    private OnOpenNavigationViewListener mOnOpenNavViewListener;

    @Inject
    @AppModule.DeviceItemAdapterWithoutDividerAnnotation
    DeviceItemAdapter mDeviceItemAdapter;

    private SettingViewModel mSettingViewModel;

    @Override
    public int getFragmentView() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModels();
        setupToolbar();
        setupRcvSettings();
        listenLoadingSettingDataEvens();
    }


    private void initViewModels() {
        mSettingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
    }

    private void setupToolbar() {
        mOnOpenNavViewListener = (OnOpenNavigationViewListener) requireActivity();
        mBinding.toolbar.setNavigationOnClickListener(v -> mOnOpenNavViewListener.openOrCloseNavView());
    }

    private void setupRcvSettings() {
        mDeviceItemAdapter.setOnClickItemListener(this);
        mBinding.rcvSettings.setAdapter(mDeviceItemAdapter);

        if (mSettingViewModel.checkHasNoSettingData()) {
            mSettingViewModel.loadSettingData();
        }
        mSettingViewModel.mSettingItems.observe(getViewLifecycleOwner(), items -> {
            if (items != null) mDeviceItemAdapter.submitList(items);
        });
    }

    private void listenLoadingSettingDataEvens() {
        mSettingViewModel.mEvent.observe(getViewLifecycleOwner(), event -> {
            switch (event) {
                case LOADING_SETTING_FRAGMENT_DATA_EVENT:
                    // may be show loading progress bar
                    break;
                case LOADING_SETTING_FRAGMENT_DATA_FAILURE_EVENT:
                    Toast.makeText(requireContext(), event.getException().getMessage(), Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        });
    }

    @Override
    public void onClickDeviceItem(DeviceItem deviceItem) {
        Toast.makeText(requireContext(), deviceItem.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}