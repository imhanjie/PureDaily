package com.melodyxxx.puredaily.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;


public class TipTitleView extends RelativeLayout {

    private ImageView iv_icon;
    private TextView tv_title;

    private void initView(Context context) {
        View.inflate(context, R.layout.custom_tip_title, TipTitleView.this);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    public TipTitleView(Context context) {
        super(context);
    }

    public TipTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, 0);
        iv_icon.setImageResource(a.getResourceId(R.styleable.CustomView_pure_icon, 0));
        tv_title.setText(a.getString(R.styleable.CustomView_pure_title));
        a.recycle();
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setIcon(int resId) {
        iv_icon.setImageResource(resId);
    }

}
