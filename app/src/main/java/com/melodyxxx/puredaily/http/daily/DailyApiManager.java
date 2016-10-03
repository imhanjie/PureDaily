package com.melodyxxx.puredaily.http.daily;

import android.text.TextUtils;

import com.melodyxxx.puredaily.App;
import com.melodyxxx.puredaily.entity.daily.AllComments;
import com.melodyxxx.puredaily.entity.daily.Latest;
import com.melodyxxx.puredaily.entity.daily.NewsDetails;
import com.melodyxxx.puredaily.entity.daily.Theme;
import com.melodyxxx.puredaily.entity.daily.ThemeDetails;
import com.melodyxxx.puredaily.entity.daily.Themes;
import com.melodyxxx.puredaily.utils.L;
import com.melodyxxx.puredaily.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/16.
 * Description:
 */
public class DailyApiManager {

    private static final int MAX_AGE_DEFAULT = 30;
    private static final int MAX_STALE_DEFAULT = 60 * 60 * 6; // 没网失效6小时

    private DailyApiService mDailyApiService;
    private static DailyApiManager sDailyApiManager;

    private DailyApiManager() {
        // 设置缓存路径
        File httpCacheDir = new File(App.INSTANCE.getExternalCacheDir(), "response-cache");
        // 设置缓存10M
        Cache cache = new Cache(httpCacheDir, 10 * 1024 * 1024);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
                .cache(cache)
                .addInterceptor(LOGGING_INTERCEPTOR)
                .addInterceptor(CACHE_INTERCEPTOR)
                .addNetworkInterceptor(CACHE_INTERCEPTOR);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(DailyApiService.BASE_URL)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mDailyApiService = retrofit.create(DailyApiService.class);

    }

    private static final Interceptor CACHE_INTERCEPTOR = new Interceptor() {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetworkUtil.isNetworkAvailable(App.INSTANCE)) {//没网强制从缓存读取(必须得写，不然断网状态下，退出应用，或者等待一分钟后，就获取不到缓存）
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            Response responseLatest;

            if (NetworkUtil.isNetworkAvailable(App.INSTANCE)) {
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    // 没有使用注解Header控制缓存，这里需要指定下
                    cacheControl = "Cache-Control: public, max-age=" + MAX_AGE_DEFAULT;
                }
                responseLatest = response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .removeHeader("ETag")
                        .header("Cache-Control", cacheControl)
                        .build();
            } else {
                responseLatest = response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .removeHeader("ETag")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + MAX_STALE_DEFAULT)
                        .build();
            }
            return responseLatest;
        }
    };

    private static final Interceptor LOGGING_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            L.e(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            L.e(String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    };

    // ------------------------------------- Public Methods -------------------------------------

    // 获取单例
    public static DailyApiManager getInstance() {
        if (sDailyApiManager == null) {
            synchronized (DailyApiManager.class) {
                if (sDailyApiManager == null) {
                    sDailyApiManager = new DailyApiManager();
                }
            }
        }
        return sDailyApiManager;
    }

    /**
     * 获取最新日报信息
     *
     * @return
     */
    public Observable<Latest> getLatest() {
        return mDailyApiService.getLatest()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取历史日报
     *
     * @param date
     * @return
     */
    public Observable<Latest> getHistory(String date) {
        return mDailyApiService.getHistory(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取日报的详细信息
     *
     * @param id
     * @return
     */
    public Observable<NewsDetails> getDetails(int id) {
        return mDailyApiService.getDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取评论
     *
     * @param id
     * @return
     */
    public Observable<AllComments> getComments(int id) {
        Observable<AllComments.Comments> longComments = mDailyApiService.getLongComments(id);
        Observable<AllComments.Comments> shortComments = mDailyApiService.getShortComments(id);
        return Observable.zip(longComments, shortComments, new Func2<AllComments.Comments, AllComments.Comments, AllComments>() {
            @Override
            public AllComments call(AllComments.Comments longComments, AllComments.Comments shortComments) {
                return new AllComments(longComments, shortComments);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取可订阅的主题列表
     *
     * @return
     */
    public Observable<List<Theme>> getThemes() {
        return mDailyApiService.getThemes()
                .map(new Func1<Themes, List<Theme>>() {
                    @Override
                    public List<Theme> call(Themes themes) {
                        return themes.others;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取指定id的主题的详细信息
     *
     * @param id
     * @return
     */
    public Observable<ThemeDetails> getThemeDetails(int id) {
        return mDailyApiService.getThemeDetails(id)
                .map(new Func1<ThemeDetails, ThemeDetails>() {
                    @Override
                    public ThemeDetails call(ThemeDetails themeDetails) {
                        // 过滤掉没有图片的story
                        for (int i = 0; i < themeDetails.stories.size(); i++) {
                            if (themeDetails.stories.get(i).images == null) {
                                themeDetails.stories.remove(i);
                                i--;
                            }
                        }
                        return themeDetails;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


}
