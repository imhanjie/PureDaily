package com.melodyxxx.puredaily.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/20.
 * Description:
 */

public class AutoViewPager extends ViewPager {

    private boolean mInvisible;

    public AutoViewPager(Context context) {
        super(context);
        init();
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private static final int TIME_SCROLL_DURATION = 1000;    // 350ms
    private static final int TIME_SWITCH_INTERVAL = 3000;   // 3s

    private int mCurrentPos = 0;
    private int mPageCount = 0;

    private Runnable mTask = new Runnable() {
        @Override
        public void run() {
            mCurrentPos = (++mCurrentPos) % mPageCount;
            setCurrentItem(mCurrentPos, true);
            postDelayed(this, TIME_SWITCH_INTERVAL + TIME_SCROLL_DURATION);
        }
    };

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mPageCount = adapter.getCount();
        super.setAdapter(adapter);
    }

    private void init() {
        MyScroller scroller = new MyScroller(getContext());
        scroller.setScrollDuration(TIME_SCROLL_DURATION);
        scroller.initViewPagerScroll(this);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                removeCallbacks(mTask);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                postDelayed(mTask, TIME_SWITCH_INTERVAL);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == View.INVISIBLE) {
            removeCallbacks(mTask);
            mInvisible = true;
        } else if (visibility == View.VISIBLE) {
            // 从不可见重新进入可见状态(第一次启动Activity除外,针对onAttachedToWindow()之后View的状态)
            if (mInvisible) {
                postDelayed(mTask, TIME_SWITCH_INTERVAL);
            }
            mInvisible = false;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
            mFirstLayout.setAccessible(true);
            mFirstLayout.setBoolean(this, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        postDelayed(mTask, TIME_SWITCH_INTERVAL);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(mTask);
    }

    private class MyScroller extends Scroller {

        public MyScroller(Context context) {
            super(context);
        }

        public MyScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public MyScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        private int mScrollDuration = 2000;

        /**
         * 设置速度速度
         *
         * @param duration
         */
        public void setScrollDuration(int duration) {
            this.mScrollDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mScrollDuration);
        }

        public void initViewPagerScroll(ViewPager viewPager) {
            try {
                Field mScroller = ViewPager.class.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                mScroller.set(viewPager, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
