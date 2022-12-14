package com.doanhung.musicproject.data.model.data_model;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "song")
public class Song {
    @PrimaryKey
    private long mId;

    @ColumnInfo(name = "time")
    private long mPlayingTime;

    @Ignore
    private String mName;
    @Ignore
    private Uri mData;
    @Ignore
    private Drawable mImage;
    @Ignore
    private String mArtist;
    @Ignore
    private String mAlbum;
    @Ignore
    private long mDuration;
    @Ignore
    private long mGenreId;
    @Ignore
    private long mArtistId;
    @Ignore
    private long mAlbumId;

    public Song() {
    }

    @Ignore
    public Song(long mId, long mPlayingTime) {
        this.mId = mId;
        this.mPlayingTime = mPlayingTime;
    }

    @Ignore
    public Song(long id, String name, Uri data, Drawable image, String artist, String album, long duration) {
        this.mId = id;
        this.mName = name;
        this.mData = data;
        this.mImage = image;
        this.mArtist = artist;
        this.mAlbum = album;
        this.mDuration = duration;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public Uri getData() {
        return mData;
    }

    public void setData(Uri mData) {
        this.mData = mData;
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable image) {
        this.mImage = image;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String artist) {
        this.mArtist = artist;
    }

    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String album) {
        this.mAlbum = album;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public long getGenreId() {
        return mGenreId;
    }

    public void setGenreId(long genreId) {
        this.mGenreId = genreId;
    }

    public long getArtistId() {
        return mArtistId;
    }

    public void setArtistId(long artistId) {
        this.mArtistId = artistId;
    }

    public long getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(long albumId) {
        this.mAlbumId = albumId;
    }

    public long getPlayingTime() {
        return mPlayingTime;
    }

    public void setPlayingTime(long playingTime) {
        this.mPlayingTime = playingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(mAlbum, song.mAlbum) && mDuration == song.mDuration && Objects.equals(mName, song.mName) && Objects.equals(mImage, song.mImage) && Objects.equals(mArtist, song.mArtist);
    }

    public final static DiffUtil.ItemCallback<Song> DIFF_CALLBACK = new DiffUtil.ItemCallback<Song>() {
        @Override
        public boolean areItemsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.mId == newItem.mId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Song oldItem, @NonNull Song newItem) {
            return oldItem.equals(newItem);
        }
    };
}
