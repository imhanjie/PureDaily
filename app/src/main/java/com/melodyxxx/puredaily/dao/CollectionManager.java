package com.melodyxxx.puredaily.dao;

import com.melodyxxx.puredaily.App;
import com.melodyxxx.puredaily.entity.daily.Collection;
import com.melodyxxx.puredaily.entity.daily.CollectionDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/26.
 * Description:
 */

public class CollectionManager {

    private static CollectionDao sDao = App.INSTANCE.getDaoSession().getCollectionDao();

    /**
     * 判断是否已经收藏
     */
    public static boolean isCollected(long collectionId) {
        QueryBuilder qb = sDao.queryBuilder();
        qb.where(CollectionDao.Properties.Id.eq(collectionId));
        return qb.unique() != null;
    }

    /**
     * 根据 id 删除收藏的日报
     */
    public static void deleteById(long collectionId) {
        sDao.deleteByKey(collectionId);
    }

    /**
     * 添加一条数据
     */
    public static void insert(Collection collection) {
        sDao.insert(collection);
    }

    /**
     * 收藏的日报数量
     */
    public static long count() {
        return sDao.count();
    }

    /**
     * 查询所有已收藏的日报
     */
    public static List<Collection> queryAll() {
        QueryBuilder qb = sDao.queryBuilder();
        qb.orderDesc(CollectionDao.Properties.Time);
        return qb.list();
    }

    /**
     * 删除所有已收藏的日报
     */
    public static void deleteAll() {
        sDao.deleteAll();
    }

}
