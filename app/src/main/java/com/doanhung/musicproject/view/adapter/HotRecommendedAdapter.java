package com.doanhung.musicproject.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.Song;
import com.doanhung.musicproject.databinding.AdapterHotRecommendedBinding;

public class HotRecommendedAdapter extends ListAdapter<Song, HotRecommendedAdapter.HotRecommendedViewHolder> {

    public HotRecommendedAdapter(@NonNull DiffUtil.ItemCallback<Song> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public HotRecommendedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterHotRecommendedBinding binding =
                AdapterHotRecommendedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HotRecommendedViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HotRecommendedViewHolder holder, int position) {
        Song song = getItem(position);
        if (song != null) {
            holder.mBinding.setSong(song);
            holder.mBinding.executePendingBindings();
        }
    }

    public static class HotRecommendedViewHolder extends RecyclerView.ViewHolder {

        private final AdapterHotRecommendedBinding mBinding;

        public HotRecommendedViewHolder(@NonNull AdapterHotRecommendedBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }


}
