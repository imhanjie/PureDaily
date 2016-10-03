package com.melodyxxx.puredaily.utils;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by hanjie on 2016/5/8.
 */
public class ActivityCollector {

    private static ArrayList<Activity> sActivities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        sActivities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        sActivities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : sActivities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
