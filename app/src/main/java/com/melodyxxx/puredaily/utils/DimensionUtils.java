package com.melodyxxx.puredaily.utils;

import android.util.TypedValue;

import com.melodyxxx.puredaily.App;

public class DimensionUtils {

    public static float dp(float dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, App.INSTANCE.getResources().getDisplayMetrics());
    }

    public static float sp(float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, App.INSTANCE.getResources().getDisplayMetrics());
    }

}
