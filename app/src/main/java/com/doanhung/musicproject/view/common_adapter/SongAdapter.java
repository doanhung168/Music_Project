package com.doanhung.musicproject.view.common_adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.databinding.AdapterSongItemBinding;

public class SongAdapter extends ListAdapter<DeviceSong, SongAdapter.SongViewHolder> {

    private OnClickSongItemListener mOnClickSongItemListener;

    public SongAdapter(@NonNull DiffUtil.ItemCallback<DeviceSong> diffCallback) {
        super(diffCallback);
    }

    public void setOnClickSongItemListener(OnClickSongItemListener onClickSongItemListener) {
        mOnClickSongItemListener = onClickSongItemListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSongItemBinding binding =
                AdapterSongItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        DeviceSong song = getItem(position);
        if (song != null) {
            holder.mBinding.setSong(song);
            holder.mBinding.setOnClickSongItem(mOnClickSongItemListener);
            holder.mBinding.executePendingBindings();
        }
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {

        private final AdapterSongItemBinding mBinding;

        public SongViewHolder(@NonNull AdapterSongItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public interface OnClickSongItemListener {
        void onClickSongItem(DeviceSong deviceSong);
    }
}
