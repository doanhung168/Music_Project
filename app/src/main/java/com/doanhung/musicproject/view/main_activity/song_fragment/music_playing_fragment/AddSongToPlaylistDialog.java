package com.doanhung.musicproject.view.main_activity.song_fragment.music_playing_fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.doanhung.musicproject.data.model.data_model.PlayList;

import java.util.ArrayList;
import java.util.List;

public class AddSongToPlaylistDialog extends DialogFragment {

    private final OnAddSongToPlaylistListener mOnAddSongToPlaylistListener;
    private final String[] mPlaylistIds;
    private final String[] mPlaylistNames;

    private int choicePos;

    public AddSongToPlaylistDialog(OnAddSongToPlaylistListener listener, List<PlayList> playLists) {
        this.mOnAddSongToPlaylistListener = listener;

        mPlaylistIds = getPlaylistId(playLists);
        mPlaylistNames = getPlaylistNames(playLists);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Choose a playlist to add song into it")
                .setSingleChoiceItems(mPlaylistNames, choicePos, (dialog, which) -> {
                    choicePos = which;
                })
                .setNegativeButton("Cancel", (dialog, which) -> dismiss())
                .setPositiveButton("Add", (dialog, which) -> {
                    String choosePlaylistId = mPlaylistIds[choicePos];
                    mOnAddSongToPlaylistListener.OnAddSongToPlaylist(choosePlaylistId);
                });
        return builder.create();
    }

    public String[] getPlaylistId(List<PlayList> playLists) {
        List<String> playlistIds = new ArrayList<>();
        for (PlayList playList : playLists) {
            playlistIds.add(String.valueOf(playList.getId()));
        }
        String[] result = new String[playlistIds.size()];
        return playlistIds.toArray(result);
    }

    public String[] getPlaylistNames(List<PlayList> playLists) {
        List<String> playlistNames = new ArrayList<>();
        for (PlayList playList : playLists) {
            playlistNames.add(playList.getName());
        }
        String[] result = new String[playlistNames.size()];
        return playlistNames.toArray(result);
    }

    public interface OnAddSongToPlaylistListener {
        void OnAddSongToPlaylist(String playlistId);
    }
}
