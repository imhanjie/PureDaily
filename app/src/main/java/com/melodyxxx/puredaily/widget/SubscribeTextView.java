package com.melodyxxx.puredaily.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.utils.L;

/**
 * adb
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/22.
 * Description:
 */

public class SubscribeTextView extends TextView {

    private Context mContext;
    private Paint mPaint;
    private RectF mRectF;
    private String mSubscribedText;
    private String mUnSubscribeText;
    private boolean mSubscribed;
    private int mSubscribedColor;
    private int mUnSubscribeColor;

    public SubscribeTextView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public SubscribeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubscribeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
        // get custom attributes
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, 0);
        mSubscribedText = (String) a.getText(R.styleable.CustomView_pure_on);
        mUnSubscribeText = (String) a.getText(R.styleable.CustomView_pure_off);
        setText(mSubscribed ? mSubscribedText : mUnSubscribeText);
        // recycle
        a.recycle();
    }

    private void init() {
        // 默认未订阅
        mSubscribed = false;
        mSubscribedColor = getResources().getColor(R.color.subscribed_color);
        mUnSubscribeColor = getResources().getColor(R.color.un_subscribe_color);
        mPaint = new Paint();
        mPaint.setColor(mSubscribed ? mSubscribedColor : mUnSubscribeColor);
        setTextColor(mSubscribed ? mSubscribedColor : mUnSubscribeColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(dp2px(1));
        setPadding(dp2px(7), dp2px(4), dp2px(7), dp2px(4));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mRectF = new RectF(dp2px(2), dp2px(2), right - left - dp2px(2), bottom - top - dp2px(2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(mRectF, dp2px(3), dp2px(3), mPaint);
    }

    public boolean isSubscribed() {
        return mSubscribed;
    }

    public void setSubscribe(boolean subscribed) {
        int startColor = subscribed ? mUnSubscribeColor : mSubscribedColor;
        int endColor = subscribed ? mSubscribedColor : mUnSubscribeColor;
        mSubscribed = subscribed;
        setText(mSubscribed ? mSubscribedText : mUnSubscribeText);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ObjectAnimator animator = ObjectAnimator.ofArgb(this, "Color", startColor, endColor);
            animator.setDuration(400);
            animator.start();
        } else {
            mPaint.setColor(mSubscribed ? mSubscribedColor : mUnSubscribeColor);
            setTextColor(mSubscribed ? mSubscribedColor : mUnSubscribeColor);
        }
    }

    // ObjectAnimator Wrapper
    private void setColor(int color) {
        mPaint.setColor(color);
        setTextColor(color);
    }

    public int dp2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
