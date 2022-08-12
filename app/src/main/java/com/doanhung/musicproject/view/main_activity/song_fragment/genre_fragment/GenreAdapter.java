package com.doanhung.musicproject.view.main_activity.song_fragment.genre_fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.data_model.Genre;
import com.doanhung.musicproject.databinding.AdapterGenreItemBinding;

public class GenreAdapter extends ListAdapter<Genre, GenreAdapter.GenreViewHolder> {

    public GenreAdapter(@NonNull DiffUtil.ItemCallback<Genre> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterGenreItemBinding binding =
                AdapterGenreItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GenreViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        Genre genre = getItem(position);
        if (genre != null) {
            holder.mBinding.setGenre(genre);
            holder.mBinding.executePendingBindings();
        }
    }

    public static class GenreViewHolder extends RecyclerView.ViewHolder {
        private final AdapterGenreItemBinding mBinding;

        public GenreViewHolder(@NonNull AdapterGenreItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
