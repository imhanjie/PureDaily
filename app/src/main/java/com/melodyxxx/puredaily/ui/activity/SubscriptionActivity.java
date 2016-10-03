package com.melodyxxx.puredaily.ui.activity;

import android.os.Bundle;

import com.melodyxxx.puredaily.http.daily.DailyApiManager;

import rx.subscriptions.CompositeSubscription;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/18.
 * Description:
 */
public abstract class SubscriptionActivity extends BaseActivity {

    protected DailyApiManager mDailyApiManager;

    /**
     * 使用CompositeSubscription持有Subscription
     */
    protected CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDailyApiManager = DailyApiManager.getInstance();
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }
}
