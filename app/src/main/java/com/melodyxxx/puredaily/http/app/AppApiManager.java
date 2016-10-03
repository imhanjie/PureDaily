package com.melodyxxx.puredaily.http.app;

import com.melodyxxx.puredaily.entity.app.LatestVersion;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/16.
 * Description:
 */
public class AppApiManager {

    private AppApiService mAppApiService;
    private static AppApiManager sAppApiManager;

    private AppApiManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mAppApiService = retrofit.create(AppApiService.class);

    }


    // ------------------------------------- Public Methods -------------------------------------

    // 获取单例
    public static AppApiManager getInstance() {
        if (sAppApiManager == null) {
            synchronized (AppApiManager.class) {
                if (sAppApiManager == null) {
                    sAppApiManager = new AppApiManager();
                }
            }
        }
        return sAppApiManager;
    }

    /**
     * 获取App的最新版本信息
     *
     * @return
     */
    public Observable<LatestVersion> getLatestVersion() {
        return mAppApiService.getLatestVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
