package com.doanhung.musicproject.util;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.doanhung.musicproject.R;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BindingAdapter {

    @androidx.databinding.BindingAdapter("songDuration")
    public static void setSongDuration(TextView textView, long duration) {
        String time = CommonUtil.getTime(duration);
        textView.setText(time);
    }

    @androidx.databinding.BindingAdapter(value = {"button", "checkedButton", "isChecked"})
    public static void setButton(MaterialCheckBox checkBox, Drawable button, Drawable checkedButton, boolean isChecked) {
        if (isChecked) {
            checkBox.setButtonDrawable(checkedButton);
        } else {
            checkBox.setButtonDrawable(button);
        }
    }

    @androidx.databinding.BindingAdapter(value = {"placeHolderImage", "uri"})
    public static void setImage(ImageView imageView, Drawable placeHolderImage, Uri uri) {
        try {
            imageView.setImageURI(uri);
        } catch (Exception e) {
            imageView.setImageDrawable(placeHolderImage);
        }
    }

    @androidx.databinding.BindingAdapter(value = {"dummyImage"})
    public static void setDummyImage(ImageView imageView, boolean isDummy) {
        List<Drawable> drawables = new ArrayList<>();
        drawables.add(ContextCompat.getDrawable(imageView.getContext(), R.drawable.image_header_playlist_sample_1));
        drawables.add(ContextCompat.getDrawable(imageView.getContext(), R.drawable.image_header_playlist_sample_2));
        drawables.add(ContextCompat.getDrawable(imageView.getContext(), R.drawable.image_header_playlist_sample_3));
        drawables.add(ContextCompat.getDrawable(imageView.getContext(), R.drawable.image_header_playlist_sample_4));
        drawables.add(ContextCompat.getDrawable(imageView.getContext(), R.drawable.image_hot_recommended_sample_1));
        drawables.add(ContextCompat.getDrawable(imageView.getContext(), R.drawable.image_hot_recommended_sample_2));
        drawables.add(ContextCompat.getDrawable(imageView.getContext(), R.drawable.image_playlist_sample_1));
        drawables.add(ContextCompat.getDrawable(imageView.getContext(), R.drawable.image_playlist_sample_2));

        Random random = new Random();
        if (isDummy) {
            imageView.setImageDrawable(drawables.get(random.nextInt(7)));
        }
    }


}
