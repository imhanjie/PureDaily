package com.melodyxxx.puredaily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melodyxxx.puredaily.http.daily.DailyApiManager;
import com.melodyxxx.puredaily.utils.L;

import rx.subscriptions.CompositeSubscription;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/18.
 * Description:
 */
public abstract class SubscriptionFragment extends BaseFragment {

    protected DailyApiManager mDailyApiManager;

    /**
     * 使用CompositeSubscription持有Subscription
     */
    protected CompositeSubscription mCompositeSubscription;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDailyApiManager = DailyApiManager.getInstance();
        mCompositeSubscription = new CompositeSubscription();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeSubscription.unsubscribe();
    }


}
