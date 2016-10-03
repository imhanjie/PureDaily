package com.melodyxxx.puredaily.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.ui.adapter.CommentAdapter;
import com.melodyxxx.puredaily.entity.daily.AllComments;
import com.melodyxxx.puredaily.utils.DividerItemDecoration;
import com.melodyxxx.puredaily.utils.SnackBarUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import rx.Observer;
import rx.Subscription;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/6/2.
 * Description: 评论页
 */
public class CommentActivity extends SubscriptionActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading)
    AVLoadingIndicatorView mLoadingView;

    @BindView(R.id.no_data_area)
    LinearLayout mNoDataArea;

    CommentAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_comment;
    }

    @Override
    public int getStatusBarOptions() {
        return StatusBarOptions.LIGHT_STATUS_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(R.string.activity_title_comment);
        startLoadingAnim();
        getCommentData(getIntent().getIntExtra("id", 0));
    }

    private void getCommentData(final int id) {
        Subscription subscription = mDailyApiManager.getComments(id)
                .subscribe(new Observer<AllComments>() {

                    @Override
                    public void onNext(AllComments comments) {
                        onGetSuccess(comments);
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

                });
        mCompositeSubscription.add(subscription);
    }


    private void onGetSuccess(AllComments allComments) {
        if (allComments.longComments.comments.size() == 0 && allComments.shortComments.comments.size() == 0) {
            mNoDataArea.setVisibility(View.VISIBLE);
            return;
        }
        initRecyclerView(allComments);
    }

    private void onGetFailed(String errorMsg) {
        SnackBarUtils.makeShort(this, mLoadingView, errorMsg).show();
    }

    private void onGetDone() {
        stopLoadingAnim();
    }

    private void initRecyclerView(AllComments allComments) {
        mAdapter = new CommentAdapter(this, allComments);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    public static void startCommentActivity(Activity activity, int id) {
        Intent intent = new Intent(activity, CommentActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

    private void startLoadingAnim() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnim() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
