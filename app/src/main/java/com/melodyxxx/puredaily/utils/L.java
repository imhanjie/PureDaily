package com.melodyxxx.puredaily.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hanjie on 16/09/12.
 */
public class L {

    private static boolean sEnabled = true;

    private static final String TAG = "bingo";

    private static String getStackTrace() {
        StringBuffer buffer = new StringBuffer();
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            buffer.append("\tat ");
            buffer.append(element.toString());
            buffer.append("\n");
        }
        return buffer.toString();
    }

    private static StackTraceElement getTargetStackTraceElement() {
        StackTraceElement targetElement = null;
        boolean shouldTrace = false;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement e : elements) {
            boolean isLogMethod = e.getClassName().equals(L.class.getName());
            if (shouldTrace && !isLogMethod) {
                targetElement = e;
                break;
            }
            shouldTrace = isLogMethod;
        }
        return targetElement;
    }

    private static String getTargetPath() {
        StackTraceElement element = getTargetStackTraceElement();
        return "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
    }

    // ----------------------Public Method----------------------

    private static long sTimeMillis;

    public static void startTimer() {
        e("Timer start.");
        sTimeMillis = System.currentTimeMillis();
    }

    public static void endTimer() {
        if (sTimeMillis == 0) {
            e("Timer error.");
            return;
        }
        e("Timer end. " + "Spend: " + (System.currentTimeMillis() - sTimeMillis) + " ms ");
        sTimeMillis = 0;
    }

    /**
     * 打印当前调用栈
     */
    public static void printStackTrace() {
        e(getStackTrace());
    }

    public static void e(String msg) {
        if (!sEnabled) {
            return;
        }
        Log.e(TAG, msg + "\t\t" + getTargetPath());
    }

    public static void w(String msg) {
        if (!sEnabled) {
            return;
        }
        Log.w(TAG, msg + "\t\t" + getTargetPath());
    }

    public static void i(String msg) {
        if (!sEnabled) {
            return;
        }
        Log.i(TAG, msg + "\t\t" + getTargetPath());
    }

    public static void d(String msg) {
        if (!sEnabled) {
            return;
        }
        Log.d(TAG, msg + "\t\t" + getTargetPath());
    }

    public static void v(String msg) {
        if (!sEnabled) {
            return;
        }
        Log.v(TAG, msg + "\t\t" + getTargetPath());
    }

    public static void printJson(String json) {
        if (!sEnabled) {
            return;
        }
        try {
            String jsonString = new JSONObject(json).toString(4);
            Log.e(TAG, "At: " + getTargetPath());
            int maxSize = 2000;
            int length = jsonString.length();
            int part = length / maxSize;
            if (length % maxSize != 0) {
                part += 1;
            }
            for (int i = 0; i < part; i++) {
                if (i == part - 1) {
                    Log.e(TAG, jsonString.substring(i * maxSize, length));
                } else {
                    Log.e(TAG, jsonString.substring(i * maxSize, (i + 1) * maxSize));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "At: " + getTargetPath() + "\n" + "Error json data! Please check:\n" + json);
        }
    }

}
