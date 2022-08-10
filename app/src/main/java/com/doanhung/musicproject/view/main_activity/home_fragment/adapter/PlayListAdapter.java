package com.doanhung.musicproject.view.main_activity.home_fragment.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.data_model.PlayList;
import com.doanhung.musicproject.databinding.AdapterPlaylistItemBinding;

public class PlayListAdapter extends ListAdapter<PlayList, PlayListAdapter.PlayListViewHolder> {

    private OnClickPlaylistItemListener mOnClickPlaylistItemListener;

    public PlayListAdapter(@NonNull DiffUtil.ItemCallback<PlayList> diffCallback) {
        super(diffCallback);
    }

    public void setOnClickPlaylistItemListener(OnClickPlaylistItemListener onClickPlaylistItemListener) {
        mOnClickPlaylistItemListener = onClickPlaylistItemListener;
    }

    @NonNull
    @Override
    public PlayListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterPlaylistItemBinding binding = AdapterPlaylistItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new PlayListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListViewHolder holder, int position) {
        PlayList playList = getItem(position);
        if (playList != null) {
            holder.mBinding.setPlaylist(playList);
            holder.mBinding.setOnClickItem(mOnClickPlaylistItemListener);
            holder.mBinding.executePendingBindings();
        }
    }

    public static class PlayListViewHolder extends RecyclerView.ViewHolder {
        private final AdapterPlaylistItemBinding mBinding;

        public PlayListViewHolder(@NonNull AdapterPlaylistItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public interface OnClickPlaylistItemListener {
        void onClickPlaylistItem(PlayList playList);
    }
}
