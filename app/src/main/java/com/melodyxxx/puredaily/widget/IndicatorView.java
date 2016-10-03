package com.melodyxxx.puredaily.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/7/16.
 * Description:
 */
public class IndicatorView extends View {

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 圆的个数(不包括动态移动的圆)
     */
    private int mCount = 1;
    /**
     * 保存上一次圆的个数(requestLayout的依据)
     */
    private int mPreCount = mCount;
    /**
     * 圆的半径
     */
    private int mRadius;
    /**
     * 圆之间的间距
     */
    private int mSpacing;
    /**
     * 四周的空白距离
     */
    private int mPadding;
    /**
     * 对应ViewPager onPageScrolled的position
     */
    private int mPosition = 0;
    /**
     * 对应ViewPager onPageScrolled的offset
     */
    private float mPositionOffset = 0;

    private float mCurrentAlphaValue = 1;

    private boolean mEnableAnimation = true;

    protected Context mContext;

    public void setCount(int count) {
        mCount = count;
        if (mPreCount != mCount) {
            requestLayout();
            mPreCount = mCount;
        }
        mCurrentAlphaValue = 1f;
        if (mEnableAnimation) {
            delayPlayLaunchAnimation(0);
        }
    }

    public IndicatorView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    ViewPager.OnPageChangeListener mListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mPosition = position;
            mPositionOffset = positionOffset;
            invalidate();
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mEnableAnimation) {
                playAnimation(state);
            }
        }
    };

    /**
     * 绑定ViewPager
     *
     * @param viewPager
     */
    public void bind(@NonNull ViewPager viewPager, boolean enableAnimation) {
        mEnableAnimation = enableAnimation;
        viewPager.addOnPageChangeListener(mListener);
    }

    /**
     * 解绑ViewPager
     *
     * @param viewPager
     */
    public void unBind(@NonNull ViewPager viewPager) {
        viewPager.removeOnPageChangeListener(mListener);
    }

    ValueAnimator mAlphaUpAnimator;
    ValueAnimator mAlphaDownAnimator;

    private void playAnimation(int state) {
        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
            if (mAlphaDownAnimator != null && mAlphaDownAnimator.isStarted()) {
                mAlphaDownAnimator.cancel();
            }
            mAlphaUpAnimator = ValueAnimator.ofFloat(mCurrentAlphaValue, 1f);
            mAlphaUpAnimator.setDuration((long) (500 * (1f - mCurrentAlphaValue)));
            mAlphaUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentAlphaValue = (float) animation.getAnimatedValue();
                    setAlpha(mCurrentAlphaValue);
                }
            });
            mAlphaUpAnimator.start();
        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
            if (mAlphaUpAnimator != null && mAlphaUpAnimator.isStarted()) {
                mAlphaUpAnimator.cancel();
            }
            mAlphaDownAnimator = ValueAnimator.ofFloat(mCurrentAlphaValue, mCurrentAlphaValue, 0f);
            mAlphaDownAnimator.setDuration((long) (2000 * mCurrentAlphaValue));
            mAlphaDownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentAlphaValue = (float) animation.getAnimatedValue();
                    setAlpha(mCurrentAlphaValue);
                }
            });
            mAlphaDownAnimator.start();
        }
    }

    private void init() {
        mRadius = dp2px(2);
        mSpacing = 4 * mRadius;
        mPadding = 2 * mRadius;

        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(100);
        mCirclePaint.setColor(Color.WHITE);
    }

    private void delayPlayLaunchAnimation(long time) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                playAnimation(ViewPager.SCROLL_STATE_IDLE);
            }
        }, time);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = mSpacing * (mCount - 1) + 2 * mRadius * mCount + 2 * mPadding;
        int measureHeight = 2 * mRadius + mPadding;
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 两个圆的圆形的间距距离
        int centerSpacing = mSpacing + 2 * mRadius;
        for (int i = 0; i < mCount; i++) {
            canvas.drawCircle(mPadding + mRadius + i * centerSpacing, getHeight() / 2, mRadius, mPaint);
        }
        canvas.drawCircle(mPadding + mRadius + mPosition * centerSpacing + mPositionOffset * centerSpacing, getHeight() / 2, mRadius, mCirclePaint);
    }

    public int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
