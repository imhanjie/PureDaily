package com.melodyxxx.puredaily.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;

/**
 * 自定义组合控件，两个TextView以及一个CheckBox
 */
public class CheckBoxItemView extends RelativeLayout {

    /**
     * 选项的标题
     */
    private TextView tv_title;
    /**
     * 选项的描述信息
     */
    private TextView tv_desc;
    /**
     * 选项的勾选框
     */
    private AppCompatCheckBox cb_check;

    /**
     * 从自定义组合控件的属性文件attrs.xml中获取的标题
     */
    private String mTitle;
    /**
     * 从自定义组合控件的属性文件attrs.xml中获取到的非勾选状态的描述信息
     */
    private String mCheckOff;
    /**
     * 从自定义组合控件的属性文件attrs.xml中获取到的勾选状态的描述信息
     */
    private String mCheckOn;
    /**
     * 从自定义组合控件的属性文件attrs.xml中获取到的底部line的可见性
     */
    private boolean mButtonLine;
    /**
     * 底部line
     */
    private View mBottomLineView;

    /**
     * 初始化视图(加载布局，实例化各控件)
     *
     * @param context 用于来加载布局文件使用的context
     */
    private void initView(Context context) {
        // 加载布局文件
        View.inflate(context, R.layout.item_checkbox, CheckBoxItemView.this);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        cb_check = (AppCompatCheckBox) findViewById(R.id.cb_check);
        tv_title = (TextView) findViewById(R.id.tv_title);
        mBottomLineView = findViewById(R.id.view_bottomLine);
    }

    /**
     * 在代码实例化的时候使用
     *
     * @param context
     */
    public CheckBoxItemView(Context context) {
        super(context);
    }

    /**
     * 在布局文件实例化的使用
     *
     * @param context
     * @param attrs
     */
    public CheckBoxItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 要设置样式的时候使用
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CheckBoxItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, 0);
        mCheckOn = (String) a.getText(R.styleable.CustomView_pure_on);
        mCheckOff = (String) a.getText(R.styleable.CustomView_pure_off);
        mTitle = (String) a.getText(R.styleable.CustomView_pure_title);
        mButtonLine = a.getBoolean(R.styleable.CustomView_pure_bottomLine, true);
        if (mButtonLine) {
            mBottomLineView.setVisibility(VISIBLE);
        } else {
            mBottomLineView.setVisibility(INVISIBLE);
        }
        tv_title.setText(mTitle);
        tv_desc.setText(mCheckOff);
        a.recycle();
    }

    /**
     * 返回CheckBox的状态
     *
     * @return
     */
    public boolean isChecked() {
        return cb_check.isChecked();
    }

    /**
     * 设置CheckBox的状态
     *
     * @param isChecked
     */
    public void setChecked(boolean isChecked) {
        if (isChecked) {
            setDesc(mCheckOn);
        } else {
            setDesc(mCheckOff);
        }
        cb_check.setChecked(isChecked);
    }

    /**
     * 设置选项的描述信息文本
     *
     * @param desc 描述信息
     */
    public void setDesc(String desc) {
        tv_desc.setText(desc);
    }

}
