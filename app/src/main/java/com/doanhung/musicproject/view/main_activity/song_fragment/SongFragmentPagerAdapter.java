package com.doanhung.musicproject.view.main_activity.song_fragment;

import static com.doanhung.musicproject.util.Constraint.ALBUM_FRAGMENT_INDEX;
import static com.doanhung.musicproject.util.Constraint.ALL_SONG_FRAGMENT_INDEX;
import static com.doanhung.musicproject.util.Constraint.ARTIST_FRAGMENT_INDEX;
import static com.doanhung.musicproject.util.Constraint.GENRE_FRAGMENT_INDEX;
import static com.doanhung.musicproject.util.Constraint.NUMBER_OF_SONG_FRAGMENTS;
import static com.doanhung.musicproject.util.Constraint.PLAYLIST_FRAGMENT_INDEX;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.doanhung.musicproject.view.main_activity.song_fragment.album_fragment.AlbumFragment;
import com.doanhung.musicproject.view.main_activity.song_fragment.all_song_fragment.AllSongFragment;
import com.doanhung.musicproject.view.main_activity.song_fragment.artist_fragment.ArtistFragment;
import com.doanhung.musicproject.view.main_activity.song_fragment.genre_fragment.GenreFragment;
import com.doanhung.musicproject.view.main_activity.song_fragment.playlist_fragment.PlayListFragment;

import javax.inject.Inject;

public class SongFragmentPagerAdapter extends FragmentStateAdapter {

    public SongFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case ALL_SONG_FRAGMENT_INDEX:
                return new AllSongFragment();
            case PLAYLIST_FRAGMENT_INDEX:
                return new PlayListFragment();
            case ALBUM_FRAGMENT_INDEX:
                return new AlbumFragment();
            case ARTIST_FRAGMENT_INDEX:
                return new ArtistFragment();
            case GENRE_FRAGMENT_INDEX:
                return new GenreFragment();
            default:
                throw new IllegalStateException("error not except");
        }
    }

    @Override
    public int getItemCount() {
        return NUMBER_OF_SONG_FRAGMENTS;
    }
}
