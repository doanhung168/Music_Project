package com.doanhung.musicproject.view.main_activity.song_fragment.all_song_fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.data.model.data_model.Song;
import com.doanhung.musicproject.databinding.AdapterAllSongItemBinding;

public class AllSongAdapter extends ListAdapter<DeviceSong, AllSongAdapter.SongViewHolder> {

    private OnClickSongItemListener mOnClickSongItemListener;

    public AllSongAdapter(@NonNull DiffUtil.ItemCallback<DeviceSong> diffCallback) {
        super(diffCallback);
    }

    public void setOnClickSongItemListener(OnClickSongItemListener onClickSongItemListener) {
        this.mOnClickSongItemListener = onClickSongItemListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterAllSongItemBinding binding =
                AdapterAllSongItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()), parent, false
                );

        return new SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        DeviceSong song = getItem(position);
        if (song != null) {
            holder.mBinding.setSong(song);
            holder.mBinding.setOnClickItem(mOnClickSongItemListener);
            holder.mBinding.executePendingBindings();
        }

        if (position == getItemCount() - 1) {
            holder.mBinding.divider.setVisibility(View.INVISIBLE);
        }
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        private final AdapterAllSongItemBinding mBinding;

        public SongViewHolder(@NonNull AdapterAllSongItemBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }

    public interface OnClickSongItemListener {
        void onClickSongItem(DeviceSong song);
    }
}
