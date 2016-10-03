package com.melodyxxx.puredaily.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.ui.adapter.BaseAdapter;
import com.melodyxxx.puredaily.ui.adapter.LatestAdapter;
import com.melodyxxx.puredaily.entity.daily.Latest;
import com.melodyxxx.puredaily.ui.activity.DailyDetailsActivity;
import com.melodyxxx.puredaily.ui.activity.HomeActivity;
import com.melodyxxx.puredaily.utils.ColorUtils;
import com.melodyxxx.puredaily.utils.CommonUtils;
import com.melodyxxx.puredaily.utils.DividerItemDecoration;
import com.melodyxxx.puredaily.utils.MenuTintUtils;
import com.melodyxxx.puredaily.utils.SnackBarUtils;
import com.melodyxxx.puredaily.widget.VerticalSwipeRefreshLayout;
import com.wang.avi.AVLoadingIndicatorView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/5/31.
 * Description: 最新日报
 */
public class LatestFragment extends SubscriptionFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading)
    AVLoadingIndicatorView mLoadingView;

    @BindView(R.id.swipe)
    VerticalSwipeRefreshLayout mSwipe;

    private LatestAdapter mRecyclerViewAdapter;

    private Latest mLatest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((HomeActivity) getActivity()).setToolbarTitle(R.string.fragment_title_latest);
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        ButterKnife.bind(this, view);
        initSwipe();
        startLoadingAnim();
        getLatestData(null);
        return view;
    }

    private void initSwipe() {
        mSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLatestData(null);
            }
        });
        mSwipe.setColorSchemeColors(ColorUtils.getColorAccent(getContext()));
    }

    /**
     * 获取消息
     *
     * @param date 指定获取的该日期的日报(date为null代表默认获取今日最新消息)
     */
    private void getLatestData(String date) {
        Subscription subscription;
        if (TextUtils.isEmpty(date)) {
            // 获取最新日报信息
            subscription = mDailyApiManager.getLatest()
                    .subscribe(getSubscriber());
        } else {
            // 获取指定历史指定日期日报信息
            subscription = mDailyApiManager.getHistory(date)
                    .subscribe(getSubscriber());
        }
        mCompositeSubscription.add(subscription);
    }

    private Subscriber<Latest> getSubscriber() {
        return new Subscriber<Latest>() {

            @Override
            public void onNext(Latest latest) {
                onGetSuccess(latest, latest.date);
            }

            @Override
            public void onCompleted() {
                onGetDone();
            }

            @Override
            public void onError(Throwable e) {
                onGetFailed(e.getMessage());
                onGetDone();
            }

        };
    }

    private void onGetSuccess(Latest latest, String resultDate) {
        HomeActivity homeActivity = ((HomeActivity) getActivity());
        if (homeActivity != null) {
            homeActivity.setToolbarTitle(CommonUtils.formatResultDate(resultDate));
        }
        this.mLatest = latest;
        if (mRecyclerViewAdapter == null) {
            initRecyclerView();
        } else {
            mRecyclerViewAdapter.syncData(mLatest);
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    private void onGetFailed(String errorMsg) {
        SnackBarUtils.makeShort(getContext(), mLoadingView, errorMsg).show();
    }

    private void onGetDone() {
        if (mSwipe.isRefreshing()) {
            mSwipe.setRefreshing(false);
        }
        stopLoadingAnim();
    }

    private void initRecyclerView() {
        mRecyclerViewAdapter = new LatestAdapter(getContext(), mLatest);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                DailyDetailsActivity.start(getActivity(), mLatest.stories.get(position).id, ((LatestAdapter.MyViewHolder) holder).image);
            }
        });
    }

    private void startLoadingAnim() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnim() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_latest_fragment, menu);

        // Tine menu icon
        int colorAccent = ColorUtils.getColorAccent(getContext());
        MenuTintUtils.tintAllIcons(menu, colorAccent);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history: {
                displayCalendarChooser();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayCalendarChooser() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        getLatestData(CommonUtils.formatDateForHistory(year, monthOfYear, dayOfMonth + 1));
                    }
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        Calendar min = Calendar.getInstance();
        min.set(2013, 4, 19);
        dpd.setMinDate(min);
        Calendar max = Calendar.getInstance();
        dpd.setMaxDate(max);
        dpd.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

}
