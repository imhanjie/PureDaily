package com.melodyxxx.puredaily.dao;

import com.melodyxxx.puredaily.App;
import com.melodyxxx.puredaily.entity.daily.Theme;
import com.melodyxxx.puredaily.entity.ThemeDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/26.
 * Description:
 */

public class ThemeManager {

    private static ThemeDao sDao = App.INSTANCE.getDaoSession().getThemeDao();

    public static boolean isSubscribed(long themeId) {
        QueryBuilder qb = sDao.queryBuilder();
        qb.where(ThemeDao.Properties.Id.eq(themeId));
        return qb.unique() != null;
    }

    public static void deleteById(long themeId) {
        sDao.deleteByKey(themeId);
    }


    public static void insert(Theme theme) {
        sDao.insert(theme);
    }

    public static long count() {
        return sDao.count();
    }

    public static List<Theme> queryAll() {
        QueryBuilder qb = sDao.queryBuilder();
        qb.orderDesc(ThemeDao.Properties.Time);
        return qb.list();
    }

    public static void deleteAll() {
        sDao.deleteAll();
    }

}
