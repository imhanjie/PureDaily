package com.melodyxxx.puredaily.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.utils.ColorUtils;
import com.melodyxxx.puredaily.utils.CommonUtils;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/7/2.
 * Description:
 */
public class PureAlertDialog {

    private AlertDialog.Builder mBuilder;
    private Activity mActivity;

    public PureAlertDialog(Activity activity, @StringRes int titleResId, @StringRes int contentResId) {
        this(activity, activity.getString(titleResId), activity.getString(contentResId));
    }

    public PureAlertDialog(Activity activity, String title, String content) {
        mActivity = activity;
        mBuilder = new AlertDialog.Builder(activity, R.style.AlertDialogStyle);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_pure_alert, null);
        TextView titleTextView = (TextView) view.findViewById(R.id.tv_title);
        TextView contentTextView = (TextView) view.findViewById(R.id.tv_content);
        titleTextView.setText(title);
        contentTextView.setText(content);
        mBuilder.setView(view);
    }

    public AlertDialog.Builder getBuilder() {
        return mBuilder;
    }

    public void show() {
        AlertDialog alertDialog = mBuilder.show();
        alertDialog.getWindow().setLayout((int) (CommonUtils.getScreenSize(mActivity)[0] * 0.93), ViewGroup.LayoutParams.WRAP_CONTENT);
        int themeColor = ColorUtils.getColorAccent(mActivity);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(themeColor);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(themeColor);
        alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(themeColor);
    }
}
