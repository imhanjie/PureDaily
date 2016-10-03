package com.melodyxxx.puredaily.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;

import com.melodyxxx.puredaily.R;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/20.
 * Description:
 */

public class ColorUtils {

    public static int getColorPrimary(Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        return value.data;
    }

    public static int getColorPrimaryDark(Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimaryDark, value, true);
        return value.data;
    }

    public static int getColorAccent(Context context) {
        final TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        return value.data;
    }

    /**
     * 获取不透明的颜色值
     *
     * @param originColor 原始颜色
     * @return 不透明的颜色值
     */
    public static int getOpaqueColor(int originColor) {
        return Color.argb(255, Color.red(originColor), Color.green(originColor), Color.blue(originColor));
    }

    /**
     * 获取不透明的颜色值
     *
     * @param color 原始颜色
     * @param alpha 指定透明度
     * @return 不透明的颜色值
     */
    public static int getSpecAlphaColor(int color, int alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }


}
