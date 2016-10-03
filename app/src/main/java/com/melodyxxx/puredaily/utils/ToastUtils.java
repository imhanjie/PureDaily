package com.melodyxxx.puredaily.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.melodyxxx.puredaily.App;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/16.
 * Description:
 */
public class ToastUtils {

    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static Object sLock = new Object();
    private static Toast sToast;

    public static void makeShort(String msg) {
        showMessage(msg, Toast.LENGTH_SHORT);
    }

    public static void makeLong(String msg) {
        showMessage(msg, Toast.LENGTH_LONG);
    }

    private static void showMessage(final String msg, final int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(App.INSTANCE, msg, duration);
        } else {
            sToast.setText(msg);
            sToast.setDuration(duration);
        }
        sToast.show();
    }

}
