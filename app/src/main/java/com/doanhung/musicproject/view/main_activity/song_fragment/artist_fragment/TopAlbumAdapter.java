package com.doanhung.musicproject.view.main_activity.song_fragment.artist_fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.data_model.Album;
import com.doanhung.musicproject.databinding.AdapterTopAlbumItemBinding;

public class TopAlbumAdapter extends ListAdapter<Album, TopAlbumAdapter.TopAlbumViewHolder> {


    public TopAlbumAdapter(@NonNull DiffUtil.ItemCallback<Album> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public TopAlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterTopAlbumItemBinding binding =
                AdapterTopAlbumItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TopAlbumViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TopAlbumViewHolder holder, int position) {
        Album album = getItem(position);
        if (album != null) {
            holder.mBinding.setAlbum(album);
            holder.mBinding.executePendingBindings();
        }
    }

    public static class TopAlbumViewHolder extends RecyclerView.ViewHolder {

        private final AdapterTopAlbumItemBinding mBinding;

        public TopAlbumViewHolder(@NonNull AdapterTopAlbumItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}