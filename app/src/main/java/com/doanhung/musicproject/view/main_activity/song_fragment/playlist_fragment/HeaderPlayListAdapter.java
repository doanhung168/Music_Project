package com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.databinding.AdapterHeaderPlaylistItemBinding;

public class HeaderPlayListAdapter extends ListAdapter<PlayList, HeaderPlayListAdapter.HeaderPlayListViewHolder> {

    public HeaderPlayListAdapter(@NonNull DiffUtil.ItemCallback<PlayList> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public HeaderPlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterHeaderPlaylistItemBinding binding = AdapterHeaderPlaylistItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new HeaderPlayListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderPlayListViewHolder holder, int position) {
        PlayList playList = getItem(position);
        if (playList != null) {
            holder.mBinding.setPlaylist(playList);
            holder.mBinding.executePendingBindings();
        }
    }

    public static class HeaderPlayListViewHolder extends RecyclerView.ViewHolder {
        private final AdapterHeaderPlaylistItemBinding mBinding;

        public HeaderPlayListViewHolder(@NonNull AdapterHeaderPlaylistItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
