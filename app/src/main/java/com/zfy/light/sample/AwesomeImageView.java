package com.zfy.light.sample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * CreateAt : 2018/9/18
 * Describe : 跳转的封装，为了更好的页面解耦
 *
 * @author chendong
 */

public class AwesomeImageView extends android.support.v7.widget.AppCompatImageView {

    public AwesomeImageView(Context context) {
        this(context, null);
    }

    public AwesomeImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AwesomeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AwesomeImageView, defStyleAttr, 0);
        // 圆角控制
        mRoundCtrl = new RoundCtrl(this);
        parseRoundProperty(a);

        a.recycle();
    }

    private boolean mAttachToWindow;
    private int     mWidth;
    private int     mHeight;

    public RoundCtrl getRoundCtrl() {
        return mRoundCtrl;
    }

    private RoundCtrl mRoundCtrl;

    private String mUrl;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRoundCtrl.onMeasure(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRoundCtrl.onDraw(canvas);
        super.onDraw(canvas);
    }



    // 解析设置圆角
    private void parseRoundProperty(TypedArray a) {
        float roundAll = a.getDimensionPixelSize(R.styleable.AwesomeImageView_round_all, -1);

        boolean roundCircle = a.getBoolean(R.styleable.AwesomeImageView_round_circle, false);

        float roundLeftSide = a.getDimensionPixelSize(R.styleable.AwesomeImageView_round_left, -1);
        float roundRightSide = a.getDimensionPixelSize(R.styleable.AwesomeImageView_round_right, -1);
        float roundTopSide = a.getDimensionPixelSize(R.styleable.AwesomeImageView_round_top, -1);
        float roundBotSide = a.getDimensionPixelSize(R.styleable.AwesomeImageView_round_bot, -1);

        float roundTopLeft = a.getDimensionPixelSize(R.styleable.AwesomeImageView_round_top_left, -1);
        float roundTopRight = a.getDimensionPixelSize(R.styleable.AwesomeImageView_round_top_right, -1);
        float roundBotLeft = a.getDimensionPixelSize(R.styleable.AwesomeImageView_round_bot_left, -1);
        float roundBotRight = a.getDimensionPixelSize(R.styleable.AwesomeImageView_round_bot_right, -1);

        if (roundCircle) {
            mRoundCtrl.setRoundCircle();
        } else {
            if (roundAll != -1) {
                roundTopLeft = roundAll;
                roundTopRight = roundAll;
                roundBotLeft = roundAll;
                roundBotRight = roundAll;
            } else if (roundTopSide != -1) {
                roundTopLeft = roundTopSide;
                roundTopRight = roundTopSide;
            } else if (roundBotSide != -1) {
                roundBotRight = roundLeftSide;
                roundBotLeft = roundBotSide;
            } else if (roundRightSide != -1) {
                roundTopRight = roundRightSide;
                roundBotRight = roundRightSide;
            } else if (roundLeftSide != -1) {
                roundTopLeft = roundLeftSide;
                roundBotLeft = roundLeftSide;
            }
            mRoundCtrl.setRound(roundTopLeft, roundTopRight, roundBotRight, roundBotLeft);
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        super.setLayoutParams(params);
        mRoundCtrl.mPath = null;
    }
    // 圆角控制
    public static class RoundCtrl {

        private boolean              mRoundCircle;
        private Path                 mPath;
        private float[]              mRadiusArr;
        private PaintFlagsDrawFilter mDrawFilter;
        private ImageView            iv;

        private int width;
        private int height;

        private RoundCtrl(ImageView iv) {
            this.iv = iv;
        }

        private void onDraw(Canvas canvas) {
            initRoundProperty();
            if (mDrawFilter != null) {
                canvas.setDrawFilter(mDrawFilter);
            }
            if (mPath != null) {
                canvas.clipPath(mPath);
            }
        }

        public void setRoundCircle() {
            mRoundCircle = true;
            initRoundProperty();
            iv.postInvalidate();
        }


        public void reset() {
            width = 0;
            height = 0;
            onMeasure(iv.getMeasuredWidth(), iv.getMaxHeight());
        }


        /**
         * 设置圆角大小
         *
         * @param radius 圆角大小
         */
        public void setRound(float radius) {
            mRadiusArr = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
            initRoundProperty();
            iv.postInvalidate();
        }

        /**
         * 设置圆角大小
         *
         * @param radiusTopLeft     t l
         * @param radiusTopRight    t r
         * @param radiusBottomRight b r
         * @param radiusBottomLeft  b l
         */
        public void setRound(float radiusTopLeft, float radiusTopRight, float radiusBottomRight, float radiusBottomLeft) {
            mRadiusArr = new float[]{radiusTopLeft, radiusTopLeft, radiusTopRight, radiusTopRight, radiusBottomRight, radiusBottomRight, radiusBottomLeft, radiusBottomLeft};
            initRoundProperty();
            this.iv.postInvalidate();
        }

        protected void onMeasure(int width, int height) {
            this.width = width;
            this.height = height;
            initRoundProperty();
        }

        // 圆角图片
        private void initRoundProperty() {
            if (width == 0) {
                width = iv.getMeasuredWidth();
            }
            if (height == 0) {
                height = iv.getMeasuredHeight();
            }
            if (width == 0 || height == 0) {
                return;
            }
            if (mRoundCircle) {
                float max = Math.max(width, height) / 2f;
                if (mRadiusArr == null) {
                    mRadiusArr = new float[]{max, max, max, max, max, max, max, max};
                }
            }
            if (mRadiusArr == null) {
                return;
            }
            if (mDrawFilter == null) {
                mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
            }
            if (mPath == null) {
                mPath = new Path();
                RectF clipRectF = new RectF(0, 0, width, height);
                mPath.addRoundRect(clipRectF, mRadiusArr, Path.Direction.CW);
            }
        }
    }

}
