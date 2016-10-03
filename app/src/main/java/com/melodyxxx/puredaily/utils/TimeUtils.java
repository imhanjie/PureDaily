package com.melodyxxx.puredaily.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/29.
 * Description:
 */

public class TimeUtils {

    public static String formatTimeLine(long timeMillis) {
        Calendar current = Calendar.getInstance();
        Calendar commentTime = Calendar.getInstance();
        commentTime.setTimeInMillis(timeMillis);
        if (current.get(Calendar.YEAR) == commentTime.get(Calendar.YEAR)) {
            if (current.get(Calendar.DAY_OF_YEAR) == commentTime.get(Calendar.DAY_OF_YEAR)) {
                // 同一天
                return "今天 " + new SimpleDateFormat("HH:mm").format(new Date(timeMillis));
            } else {
                // 同一年
                return new SimpleDateFormat("MM-dd HH:mm").format(new Date(timeMillis));
            }
        } else {
            // 不同一天
            return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(timeMillis));
        }
    }
}
