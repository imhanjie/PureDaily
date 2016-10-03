package com.melodyxxx.puredaily.utils;

import android.content.Context;

import com.melodyxxx.puredaily.constant.PrefConstants;

public class PrefUtils {

    public static void putString(Context context, String key, String value) {
        context.getSharedPreferences(PrefConstants.CONFIG_SP_FILE_NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key, String defValue) {
        return context.getSharedPreferences(PrefConstants.CONFIG_SP_FILE_NAME, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences(PrefConstants.CONFIG_SP_FILE_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return context.getSharedPreferences(PrefConstants.CONFIG_SP_FILE_NAME, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        context.getSharedPreferences(PrefConstants.CONFIG_SP_FILE_NAME, Context.MODE_PRIVATE).edit().putInt(key, value).apply();
    }

    public static int getInt(Context context, String key, int defValue) {
        return context.getSharedPreferences(PrefConstants.CONFIG_SP_FILE_NAME, Context.MODE_PRIVATE).getInt(key, defValue);
    }

}
