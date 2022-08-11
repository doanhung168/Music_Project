package com.doanhung.musicproject.util;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.checkbox.MaterialCheckBox;

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

    @androidx.databinding.BindingAdapter(value = {"placeHolderImage", "image", "isHasImage"})
    public static void setImage(ImageView imageView, Drawable placeHolderImage, Drawable image, boolean isHasImage) {
        if (image != null) {
            imageView.setImageDrawable(image);
        } else {
            imageView.setImageDrawable(placeHolderImage);
        }
    }


}
