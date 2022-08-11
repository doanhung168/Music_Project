package com.doanhung.musicproject.view.main_activity.song_fragment.add_playlist_fragment;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.app_system_model.CheckedSong;
import com.doanhung.musicproject.databinding.AdapterSelectedSongItemBinding;

public class SelectedSongAdapter extends ListAdapter<CheckedSong, SelectedSongAdapter.AddingViewHolder> {

    private OnClickSongListener mOnClickSongListener;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    public SelectedSongAdapter(@NonNull DiffUtil.ItemCallback<CheckedSong> diffCallback) {
        super(diffCallback);
    }

    public void setOnCheckedSongListener(OnClickSongListener onClickSongListener) {
        this.mOnClickSongListener = onClickSongListener;
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
        CheckedSong song = getItem(position);
        if (song != null) {
            holder.mBinding.checkbox.setChecked(song.isChecked());
            holder.mBinding.setSong(song);
            holder.mBinding.setOnClickSong(mOnClickSongListener);
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

    public interface OnClickSongListener {
        void onClickSong(CheckedSong song);
    }
}
