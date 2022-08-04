package com.doanhung.musicproject.data.model;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.Objects;

public class DeviceItem {
    private String mName;
    private Drawable mImage;

    public DeviceItem(String mName, Drawable mImage) {
        this.mName = mName;
        this.mImage = mImage;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Drawable getImage() {
        return mImage;
    }

    public void setImage(Drawable mImage) {
        this.mImage = mImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceItem deviceItem = (DeviceItem) o;
        return Objects.equals(mName, deviceItem.mName) && Objects.equals(mImage, deviceItem.mImage);
    }

    public final static DiffUtil.ItemCallback<DeviceItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<DeviceItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull DeviceItem oldDeviceItem, @NonNull DeviceItem newDeviceItem) {
            return newDeviceItem.equals(oldDeviceItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull DeviceItem oldDeviceItem, @NonNull DeviceItem newDeviceItem) {
            return newDeviceItem.equals(oldDeviceItem);
        }
    };
}

