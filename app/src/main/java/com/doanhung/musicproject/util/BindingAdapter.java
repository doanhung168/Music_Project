package com.doanhung.musicproject.util;

import android.widget.TextView;

public class BindingAdapter {

    @androidx.databinding.BindingAdapter("app:songDuration")
    public static void setSongDuration(TextView textView, long duration) {
        String time = CommonUtil.getTime(duration);
        textView.setText(time);
    }
}
