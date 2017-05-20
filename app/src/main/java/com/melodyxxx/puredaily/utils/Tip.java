package com.melodyxxx.puredaily.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.StringRes;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.melodyxxx.puredaily.R;

/**
 * Created by hanjie on 2017/4/14.
 */

public class Tip {

    enum Type {
        Notice, SUCCESS
    }


    private Context mContext;

    private Tip(Context context) {
        this.mContext = context;
    }

    public static Tip with(Context context) {
        return new Tip(context);
    }

    public void onNotice(@StringRes int resId) {
        onNotice(mContext.getString(resId));
    }

    public void onNotice(String s) {
        show(s, Type.Notice);
    }

    public void onSuccess(@StringRes int resId) {
        onSuccess(mContext.getString(resId));
    }

    public void onSuccess(String s) {
        show(s, Type.SUCCESS);
    }

    public void show(String s, Type type) {
        TextView textView = new TextView(mContext);
        textView.setText(s);
        int iconResId = 0;
        switch (type) {
            case Notice:
                iconResId = R.drawable.ic_tip_attention;
                break;
            case SUCCESS:
                iconResId = R.drawable.ic_tip_success;
                break;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
        textView.setCompoundDrawablePadding((int) DimensionUtils.dp(8));
        textView.setTextColor(Color.WHITE);
        int paddingLeftAndRight = (int) DimensionUtils.dp(27);
        int paddingTopAndBottom = (int) DimensionUtils.dp(14);
        textView.setPadding(paddingLeftAndRight, paddingTopAndBottom, paddingLeftAndRight, paddingTopAndBottom);
        ViewCompat.setBackground(textView, mContext.getResources().getDrawable(R.drawable.bg_toast));
        Toast toast = new Toast(mContext);
        toast.setView(textView);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
