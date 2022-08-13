package com.doanhung.musicproject.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Size;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.data_model.Song;

import java.io.IOException;
import java.util.List;

public class CommonUtil {



    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Drawable loadThumbnailForAboveVersionQ(Context context, String volumeName, long idItem) throws IOException {
        Bitmap bitmap = context.getContentResolver().loadThumbnail(
                MediaStore.Audio.Media.getContentUri(volumeName, idItem),
                new Size(640, 480),
                null
        );
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public static Drawable loadThumbnailForAboveVersionQ_v2(Context context, long idItem) throws IOException {
        Bitmap bitmap = context.getContentResolver().loadThumbnail(
                MediaStore.Audio.Media.getContentUri("external", idItem),
                new Size(640, 480),
                null
        );
        return new BitmapDrawable(context.getResources(), bitmap);
    }



    public static Drawable loadThumbnail(Context context, Uri fileUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(fileUri.toString());
        byte[] data = retriever.getEmbeddedPicture();
        retriever.release();
        if (data == null) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static boolean isAboveVersionQ() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.Q;
    }

    public static <I extends Song, L extends List<I>> int findItemIndex(I item, L list) {
        if (item == null || list == null) {
            throw new NullPointerException("Item or Item List is null");
        }
        int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            if (item.getId() == list.get(i).getId()) {
                return i;
            }
        }
        return -1;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap output;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        float r = 0;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2f;
        } else {
            r = bitmap.getWidth() / 2f;
        }

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @SuppressLint("DefaultLocale")
    public static String getTime(long duration) {
        int mm = (int) (duration / (1000 * 60));
        int ss = (int) ((duration % (1000 * 60)) / 1000);
        return String.format("%d:%02d", mm, ss);
    }

    public static Drawable blurDrawable(Context context, Drawable drawable) {
        try {
            Bitmap image = CommonUtil.drawableToBitmap(drawable);

            float BITMAP_SCALE = 0.2f;
            float BLUR_RADIUS = 0.5f;

            int width = Math.round(image.getWidth() * BITMAP_SCALE);
            int height = Math.round(image.getHeight() * BITMAP_SCALE);

            Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
            Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

            RenderScript rs = RenderScript.create(context);
            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
            theIntrinsic.setRadius(BLUR_RADIUS);
            theIntrinsic.setInput(tmpIn);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);

            return new BitmapDrawable(context.getResources(), outputBitmap);
        } catch (Exception e) {
            return ContextCompat.getDrawable(context, R.drawable.image_header_playlist_sample_1);
        }
    }
}
