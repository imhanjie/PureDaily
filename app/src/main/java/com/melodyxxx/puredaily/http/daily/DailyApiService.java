package com.melodyxxx.puredaily.http.daily;

import com.melodyxxx.puredaily.entity.daily.AllComments;
import com.melodyxxx.puredaily.entity.daily.Latest;
import com.melodyxxx.puredaily.entity.daily.NewsDetails;
import com.melodyxxx.puredaily.entity.daily.ThemeDetails;
import com.melodyxxx.puredaily.entity.daily.Themes;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/14.
 * Description:
 */
public interface DailyApiService {

    String BASE_URL = "http://news-at.zhihu.com/api/4/";

    @Headers("Cache-Control: public, max-age=60")
    @GET("news/latest")
    Observable<Latest> getLatest();

    @Headers("Cache-Control: public, max-age=60")
    @GET("news/before/{date}")
    Observable<Latest> getHistory(@Path("date") String date);

    @Headers("Cache-Control: public, max-age=1200")
    @GET("news/{id}")
    Observable<NewsDetails> getDetails(@Path("id") int id);

    @Headers("Cache-Control: public, max-age=0")
    @GET("story/{id}/short-comments")
    Observable<AllComments.Comments> getShortComments(@Path("id") int id);

    @Headers("Cache-Control: public, max-age=0")
    @GET("story/{id}/long-comments")
    Observable<AllComments.Comments> getLongComments(@Path("id") int id);

    @Headers("Cache-Control: public, max-age=0")
    @GET("themes")
    Observable<Themes> getThemes();

    @Headers("Cache-Control: public, max-age=60")
    @GET("theme/{id}")
    Observable<ThemeDetails> getThemeDetails(@Path("id") int id);

}
