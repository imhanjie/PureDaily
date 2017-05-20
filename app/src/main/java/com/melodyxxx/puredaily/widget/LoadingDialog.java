package com.melodyxxx.puredaily.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hanjie on 2017/5/10.
 */

public class LoadingDialog extends DialogFragment {

    protected Context mContext;
    private View mRootView;
    private String mTip;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_tip)
    TextView mTipTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setStyle(R.style.MrWind_Dialog_CustomStyle, getTheme());
        setCancelable(false);
        View view = inflater.inflate(R.layout.dialog_loading, container);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    public static LoadingDialog create() {
        LoadingDialog dialog = new LoadingDialog();
        return dialog;
    }

    private void initView() {
        mTipTextView.setText(mTip);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgressBar.getIndeterminateDrawable().setColorFilter(0xFFFFFFFF, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void showWith(FragmentManager manager, String tip) {
        if (!isAdded()) {
            mTip = tip;
            show(manager, "t");
        } else {
            mTipTextView.setText(tip);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }
}
