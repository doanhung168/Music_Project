package com.doanhung.musicproject.view.main_activity.song_fragment.add_playlist_fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.app_system_model.DeviceSong;
import com.doanhung.musicproject.databinding.AdapterSelectedSongItemBinding;

public class AddingPlayListAdapter extends ListAdapter<DeviceSong, AddingPlayListAdapter.AddingViewHolder> {

    protected AddingPlayListAdapter(@NonNull DiffUtil.ItemCallback<DeviceSong> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public AddingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSelectedSongItemBinding binding =
                AdapterSelectedSongItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );
        return new AddingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddingViewHolder holder, int position) {
        DeviceSong song = getItem(position);
        if (song != null) {
            holder.mBinding.setSong(song);
            holder.mBinding.executePendingBindings();
        }
    }

    public static class AddingViewHolder extends RecyclerView.ViewHolder {
        private final AdapterSelectedSongItemBinding mBinding;

        public AddingViewHolder(@NonNull AdapterSelectedSongItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
