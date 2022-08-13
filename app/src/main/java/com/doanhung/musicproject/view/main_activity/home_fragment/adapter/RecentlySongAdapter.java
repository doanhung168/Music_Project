package com.doanhung.musicproject.view.main_activity.home_fragment.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.databinding.AdapterRecentlySongItemBinding;

public class RecentlySongAdapter extends ListAdapter<DeviceSong, RecentlySongAdapter.RecentlySongViewHolder> {

    private OnClickRecentlySongListener mOnClickRecentlySongListener;

    public RecentlySongAdapter(@NonNull DiffUtil.ItemCallback<DeviceSong> diffCallback) {
        super(diffCallback);
    }

    public void setOnClickRecentlySongListener(OnClickRecentlySongListener listener) {
        mOnClickRecentlySongListener = listener;
    }

    @NonNull
    @Override
    public RecentlySongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterRecentlySongItemBinding binding =
                AdapterRecentlySongItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RecentlySongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentlySongViewHolder holder, int position) {
        DeviceSong song = getItem(position);
        if (song != null) {
            holder.mBinding.setSong(song);
            holder.mBinding.setOnClickRecentlySong(mOnClickRecentlySongListener);
            holder.mBinding.executePendingBindings();
        }
    }

    public static class RecentlySongViewHolder extends RecyclerView.ViewHolder {

        private final AdapterRecentlySongItemBinding mBinding;

        public RecentlySongViewHolder(@NonNull AdapterRecentlySongItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public interface OnClickRecentlySongListener {
        void onClickRecentlySong(DeviceSong song);
    }
}
