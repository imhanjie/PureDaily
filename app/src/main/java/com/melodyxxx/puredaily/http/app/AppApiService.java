package com.melodyxxx.puredaily.http.app;

import com.melodyxxx.puredaily.entity.app.LatestVersion;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/14.
 * Description:
 */
public interface AppApiService {

    String BASE_URL = "https://coding.net/u/bluehan/p/api/git/raw/master/puredaily/";

    @Headers("Cache-Control: public, max-age=0")
    @GET("latest_version_info.json")
    Observable<LatestVersion> getLatestVersion();

}
