package com.melodyxxx.puredaily;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.melodyxxx.puredaily.entity.DaoMaster;
import com.melodyxxx.puredaily.entity.DaoSession;

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
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
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
