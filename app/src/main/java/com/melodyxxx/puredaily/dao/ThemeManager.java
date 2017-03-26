package com.melodyxxx.puredaily.dao;

import com.melodyxxx.puredaily.App;
import com.melodyxxx.puredaily.entity.daily.Theme;
import com.melodyxxx.puredaily.entity.daily.ThemeDao;

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

    /**
     * 判断是否已经订阅
     */
    public static boolean isSubscribed(long themeId) {
        QueryBuilder qb = sDao.queryBuilder();
        qb.where(ThemeDao.Properties.Id.eq(themeId));
        return qb.unique() != null;
    }

    /**
     * 根据 id 删除订阅的的主题
     */
    public static void deleteById(long themeId) {
        sDao.deleteByKey(themeId);
    }

    /**
     * 添加一条数据
     */
    public static void insert(Theme theme) {
        sDao.insert(theme);
    }

    /**
     * 订阅的主题数量
     */
    public static long count() {
        return sDao.count();
    }

    /**
     * 查询所有已订阅的主题
     */
    public static List<Theme> queryAll() {
        QueryBuilder qb = sDao.queryBuilder();
        qb.orderDesc(ThemeDao.Properties.Time);
        return qb.list();
    }

    /**
     * 删除所有已订阅的主题
     */
    public static void deleteAll() {
        sDao.deleteAll();
    }

}
