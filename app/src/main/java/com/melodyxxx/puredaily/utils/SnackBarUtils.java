package com.melodyxxx.puredaily.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class SnackBarUtils {

    private int mBackgroundColor = 0xffe91e63;

    private Snackbar mSnackBar;

    private SnackBarUtils(Context context, Snackbar snackbar) {
        mSnackBar = snackbar;
        mBackgroundColor = ColorUtils.getColorAccent(context);
    }

    public static SnackBarUtils makeShort(Context context, View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        return new SnackBarUtils(context, snackbar);
    }

    public static SnackBarUtils makeLong(Context context, View view, String text) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
        return new SnackBarUtils(context, snackbar);
    }

    public void show() {
        setSnackBarBackgroundColor(mBackgroundColor);
        mSnackBar.show();
    }

    private void setSnackBarBackgroundColor(int color) {
        View view = mSnackBar.getView();
        view.setBackgroundColor(color);
    }


    public static void customSnackBar(Snackbar snackbar, int backgroundColor, int textColor, int actionTextColor) {
        View view = snackbar.getView();
        view.setBackgroundColor(backgroundColor);
        ((TextView) view.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(textColor);
        if (actionTextColor != 0) {
            ((TextView) view.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(actionTextColor);
        }
    }

}
