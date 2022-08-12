package com.doanhung.musicproject.view.main_activity.song_fragment.artist_fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.doanhung.musicproject.data.model.data_model.Artist;
import com.doanhung.musicproject.databinding.AdapterArtistItemBinding;

public class ArtistAdapter extends ListAdapter<Artist, ArtistAdapter.ArtistViewHolder> {

    private OnClickArtistItemListener mOnClickArtistItemListener;

    public ArtistAdapter(@NonNull DiffUtil.ItemCallback<Artist> diffCallback) {
        super(diffCallback);
    }

    public void setOnClickArtistItemListener(OnClickArtistItemListener onClickArtistItemListener) {
        mOnClickArtistItemListener = onClickArtistItemListener;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterArtistItemBinding binding =
                AdapterArtistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ArtistViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = getItem(position);
        if (artist != null) {
            holder.mBinding.setArtist(artist);
            holder.mBinding.setOnClickItem(mOnClickArtistItemListener);
            holder.mBinding.executePendingBindings();
        }
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        private final AdapterArtistItemBinding mBinding;

        public ArtistViewHolder(@NonNull AdapterArtistItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public interface OnClickArtistItemListener {
        void onClickArtistMenu(View view, Artist artist);
        void onClickArtistItem(Artist artist);
    }
}
