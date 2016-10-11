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

    public static boolean isCollected(long collectionId) {
        QueryBuilder qb = sDao.queryBuilder();
        qb.where(CollectionDao.Properties.Id.eq(collectionId));
        return qb.unique() != null;
    }

    public static void deleteById(long collectionId) {
        sDao.deleteByKey(collectionId);
    }


    public static void insert(Collection collection) {
        sDao.insert(collection);
    }

    public static long count() {
        return sDao.count();
    }

    public static List<Collection> queryAll() {
        QueryBuilder qb = sDao.queryBuilder();
        qb.orderDesc(CollectionDao.Properties.Time);
        return qb.list();
    }

    public static void deleteAll() {
        sDao.deleteAll();
    }

}
