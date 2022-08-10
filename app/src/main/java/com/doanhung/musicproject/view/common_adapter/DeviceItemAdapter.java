package com.doanhung.musicproject.view.common_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.app_system_model.DeviceItem;
import com.doanhung.musicproject.databinding.AdapterDeviceItemBinding;


public class DeviceItemAdapter extends ListAdapter<DeviceItem, DeviceItemAdapter.ItemViewHolder> {
    private OnClickDeviceItemListener mOnClickDeviceItemListener;
    private final boolean mHasLastDivider;

    public DeviceItemAdapter(@NonNull DiffUtil.ItemCallback<DeviceItem> diffCallback, boolean hasLastDivider) {
        super(diffCallback);
        mHasLastDivider = hasLastDivider;
    }

    public void setOnClickItemListener(OnClickDeviceItemListener onClickDeviceItemListener) {
        mOnClickDeviceItemListener = onClickDeviceItemListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterDeviceItemBinding itemBinding =
                AdapterDeviceItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        DeviceItem deviceItem = getItem(position);
        if (deviceItem != null) {
            holder.mItemBinding.setDeviceItem(deviceItem);
            holder.mItemBinding.setOnClickDeviceItem(mOnClickDeviceItemListener);
            holder.mItemBinding.executePendingBindings();
        }

        if (!mHasLastDivider && position == getItemCount() - 1) {
            holder.mItemBinding.divider.setVisibility(View.INVISIBLE);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private final AdapterDeviceItemBinding mItemBinding;

        public ItemViewHolder(@NonNull AdapterDeviceItemBinding binding) {
            super(binding.getRoot());
            mItemBinding = binding;
        }
    }

    public interface OnClickDeviceItemListener {
        void onClickDeviceItem(DeviceItem deviceItem);
    }
}
