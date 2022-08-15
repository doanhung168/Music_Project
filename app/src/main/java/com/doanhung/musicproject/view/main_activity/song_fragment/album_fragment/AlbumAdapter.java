package com.doanhung.musicproject.view.main_activity.song_fragment.album_fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.data_model.Album;
import com.doanhung.musicproject.databinding.AdapterAlbumItemBinding;

public class AlbumAdapter extends ListAdapter<Album, AlbumAdapter.AlbumViewHolder> {

    private OnClickAlbumItemListener mOnClickAlbumItemListener;

    public AlbumAdapter(@NonNull DiffUtil.ItemCallback<Album> diffCallback) {
        super(diffCallback);
    }

    public void setOnClickAlbumItemListener(OnClickAlbumItemListener onClickAlbumItemListener) {
        mOnClickAlbumItemListener = onClickAlbumItemListener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterAlbumItemBinding binding =
                AdapterAlbumItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlbumViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = getItem(position);
        if (album != null) {
            holder.mBinding.setOnClickAlbumItem(mOnClickAlbumItemListener);
            holder.mBinding.setAlbum(album);
            holder.mBinding.executePendingBindings();


        }
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {

        private final AdapterAlbumItemBinding mBinding;

        public AlbumViewHolder(@NonNull AdapterAlbumItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public interface OnClickAlbumItemListener {
        void onClickAlbumMenu(View view, Album album);
        void onClickAlbumItem(Album album);
    }

}
