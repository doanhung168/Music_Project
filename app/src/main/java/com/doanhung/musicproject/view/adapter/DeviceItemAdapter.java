package com.doanhung.musicproject.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.DeviceItem;
import com.doanhung.musicproject.databinding.AdapterDeviceItemBinding;


public class DeviceItemAdapter extends ListAdapter<DeviceItem, DeviceItemAdapter.ItemViewHolder> {
    private OnClickDeviceItemListener mOnClickDeviceItemListener;

    public DeviceItemAdapter(@NonNull DiffUtil.ItemCallback<DeviceItem> diffCallback) {
        super(diffCallback);
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
