package com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.databinding.AdapterMyPlaylistItemBinding;

public class MyPlaylistAdapter extends ListAdapter<PlayList, MyPlaylistAdapter.MyPlaylistViewHolder> {

    private OnClickPlaylist mOnClickPlaylist;

    public MyPlaylistAdapter(@NonNull DiffUtil.ItemCallback<PlayList> diffCallback) {
        super(diffCallback);
    }

    public void setOnClickPlaylist(OnClickPlaylist onClickPlaylist) {
        mOnClickPlaylist = onClickPlaylist;
    }

    @NonNull
    @Override
    public MyPlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterMyPlaylistItemBinding binding =
                AdapterMyPlaylistItemBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );
        return new MyPlaylistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPlaylistViewHolder holder, int position) {
        PlayList playList = getItem(position);
        if (playList != null) {
            holder.mBinding.setOnClickItem(mOnClickPlaylist);
            holder.mBinding.setPlaylist(playList);
            holder.mBinding.executePendingBindings();
        }
    }

    public static class MyPlaylistViewHolder extends RecyclerView.ViewHolder {

        private final AdapterMyPlaylistItemBinding mBinding;

        public MyPlaylistViewHolder(@NonNull AdapterMyPlaylistItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public interface OnClickPlaylist {
        boolean onDeletePlaylist(PlayList playList);
    }
}
