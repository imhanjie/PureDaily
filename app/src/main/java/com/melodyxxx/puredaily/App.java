package com.melodyxxx.puredaily;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.Stetho;
import com.melodyxxx.puredaily.entity.daily.DaoMaster;
import com.melodyxxx.puredaily.entity.daily.DaoSession;

import cn.bmob.v3.Bmob;

public class App extends Application {

    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    public static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        initDatabase();
        initBmob();
        Stetho.initializeWithDefaults(this);
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }

    private void initBmob() {
        Bmob.initialize(this, "3aaf1e4d34bea8d9d4f6b22f87ae3a30");
    }

    private void initDatabase() {
        mHelper = new DaoMaster.DevOpenHelper(this, "daily.mDb", null);
        mDb = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }


}
