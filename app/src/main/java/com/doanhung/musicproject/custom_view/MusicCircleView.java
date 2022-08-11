package com.doanhung.musicproject.custom_view;

import static java.lang.Math.PI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.util.CommonUtil;

/**
 * @Author Nguyen Doan Hung
 * @Name MusicCircleView
 * @Date 12:00PM -- 9/8/2022
 */
public class MusicCircleView extends View {

    private OnMusicBarChangeListener mOnMusicBarChangeListener;

    public static final int DEFAULT_PROGRESS_WIDTH = 8;
    private static final float CIRCLE_GAP = 30;

    // song image background
    private Drawable mSongImageBackground;
    private Paint mSongImagePaint;
    private RectF mSongImageRect;

    private Paint mBackgroundPaint;

    private Paint mProgressPaint;
    private RectF mProgressRect;
    private int mProgressWidth;

    private float mCx, mCy;
    private float mRadius;

    private Paint mThumbPaint;
    private float mCurrentRadian;
    private boolean mIsThumb;

    private long mSongDuration;
    private long mCurrentPosition;

    private boolean mIsInit;

    public MusicCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initValueFromXml(context, attrs);
        initPaint();
    }

    private void initValueFromXml(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MusicCircleView,
                0,
                0
        );

        try {
            mSongImageBackground = typedArray.getDrawable(R.styleable.MusicCircleView_musicAvatar);
            mProgressWidth = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    typedArray.getDimension(R.styleable.MusicCircleView_progressWidth, DEFAULT_PROGRESS_WIDTH),
                    getContext().getResources().getDisplayMetrics()
            );
        } finally {
            typedArray.recycle();
        }
    }

    private void initPaint() {
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(mProgressWidth);
        mBackgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.song_avatar_stroke_color));

        mSongImagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width, height;
        width = height = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(width, height);

        if (!mIsInit) {
            mCx = mCy = width / 2f;
            mRadius = (width / 2f) - CIRCLE_GAP;

            mProgressRect = new RectF(
                    mCx - mRadius,
                    mCy - mRadius,
                    mCx + mRadius,
                    mCy + mRadius
            );

            mSongImageRect = new RectF(
                    mCx - mRadius,
                    mCy - mRadius,
                    mCx + mRadius,
                    mCy + mRadius
            );

            mIsInit = true;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Log.i(TAG, "draw: ");
    }

    private static final String TAG = "MusicCircleView";

    @Override
    protected void onDraw(Canvas canvas) {

        Log.i(TAG, "onDraw: ");

        if (mSongImageBackground == null) {
            mSongImageBackground = ContextCompat.getDrawable(getContext(), R.drawable.image_playlist_sample_1);
        }
        canvas.drawBitmap(
                CommonUtil.getCircularBitmap(CommonUtil.drawableToBitmap(mSongImageBackground))
                , null,
                mSongImageRect,
                mSongImagePaint
        );

        canvas.drawCircle(mCx, mCy, mRadius, mBackgroundPaint);

        canvas.save();
        canvas.rotate(-90, mCx, mCy);

        // I'm unexpected but make by this way , it working
        // I don't know about why that. May be is rotate
        initProgressPaint();
        canvas.drawArc(mProgressRect, 0, (float) Math.toDegrees(mCurrentRadian), false, mProgressPaint);
        canvas.restore();

        canvas.save();
        canvas.rotate((float) Math.toDegrees(mCurrentRadian), mCx, mCy);
        initThumbPaint();
        canvas.drawCircle(mCx, mCy - mRadius, mProgressWidth, mThumbPaint);
        canvas.restore();

        super.onDraw(canvas);
    }

    private void initThumbPaint() {
        if (mThumbPaint == null) {
            mThumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            int[] colors = {
                    ContextCompat.getColor(getContext(), R.color.gradient_start),
                    ContextCompat.getColor(getContext(), R.color.gradient_end)
            };
            LinearGradient gradient = new LinearGradient(0, 0, mRadius, mRadius, colors, null, Shader.TileMode.MIRROR);
            mThumbPaint.setShader(gradient);
        }
    }

    private void initProgressPaint() {
        if (mProgressPaint == null) {
            mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            int[] colors = {
                    ContextCompat.getColor(getContext(), R.color.gradient_start),
                    ContextCompat.getColor(getContext(), R.color.gradient_center),
                    ContextCompat.getColor(getContext(), R.color.gradient_end)
            };

            float[] position = {0f, 0.6f, 1f};
            Shader gradient = new SweepGradient(mCx, mCy, colors, position);
            mProgressPaint.setStyle(Paint.Style.STROKE);
            mProgressPaint.setStrokeWidth(mProgressWidth);
            mProgressPaint.setShader(gradient);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isThumb(event.getX(), event.getY())) {
                    mIsThumb = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsThumb) {
                    mCurrentRadian = getRadian(event.getX(), event.getY());
                    invalidate();
                    mCurrentPosition = (long) ((mSongDuration * mCurrentRadian) / (2 * PI));
                    mOnMusicBarChangeListener.onProgressChanged(mCurrentPosition);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsThumb) {
                    mIsThumb = false;
                    mCurrentPosition = (long) ((mSongDuration * mCurrentRadian) / (2 * PI));
                    mOnMusicBarChangeListener.onProgressChanged(mCurrentPosition);
                }
                break;
        }
        return true;
    }


    private boolean isThumb(float x, float y) {
        float r = mRadius;
        float x1 = (float) (mCx + r * Math.sin(mCurrentRadian));
        float y1 = (float) (mCy - r * Math.cos(mCurrentRadian));
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1)) < mProgressWidth;
    }

    private float getRadian(float x, float y) {
        float alpha = (float) Math.atan((x - mCx) / (mCy - y));
        // Quadrant
        if (x > mCx && y > mCy) {
            // 2
            alpha = (float) (alpha + PI);
        } else if (x < mCx && y > mCy) {
            // 3
            alpha = (float) (alpha + PI);
        } else if (x < mCx && y < mCy) {
            // 4
            alpha = (float) (alpha + (2 * PI));
        }
        return alpha;
    }

    public void setMusicAvatar(Drawable drawable) {
        mSongImageBackground = drawable;
        invalidate();
        requestLayout();
    }

    public void setProgressWidth(int dpDimens) {
        mProgressWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpDimens,
                getContext().getResources().getDisplayMetrics());
        invalidate();
        requestLayout();
    }

    public void setCurrentPosition(long position) {
        mCurrentPosition = position;
        mCurrentRadian = (float) ((mCurrentPosition * (2 * PI)) / mSongDuration);
        invalidate();
    }

    public void setDuration(long duration) {
        mSongDuration = duration;
    }

    public void setOnMusicBarChangeListener(OnMusicBarChangeListener onMusicBarChangeListener) {
        mOnMusicBarChangeListener = onMusicBarChangeListener;
    }

    public void removeOnMusicBarChangeListener() {
        mOnMusicBarChangeListener = null;
    }

    public interface OnMusicBarChangeListener {
        void onProgressChanged(long currentPosition);
    }


}
