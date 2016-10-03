package com.melodyxxx.puredaily.ui.activity;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.entity.daily.ThemeDetails;
import com.melodyxxx.puredaily.http.daily.DailyApiManager;
import com.melodyxxx.puredaily.ui.adapter.BaseAdapter;
import com.melodyxxx.puredaily.ui.adapter.ThemeDetailsAdapter;
import com.melodyxxx.puredaily.utils.Blur;
import com.melodyxxx.puredaily.utils.DividerItemDecoration;
import com.melodyxxx.puredaily.utils.SnackBarUtils;

import butterknife.BindView;
import rx.Subscriber;
import rx.Subscription;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/10/01.
 * Description:
 */

public class ThemeActivity extends SubscriptionActivity {

    @BindView(R.id.iv_theme)
    ImageView mThemeImage;

    @BindView(R.id.iv_theme_blur)
    ImageView mBlurImage;

    @BindView(R.id.tv_description)
    TextView mDescriptionText;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading)
    ProgressBar mLoadingView;

    private ThemeDetails mThemeDetails;

    private ThemeDetailsAdapter mAdapter;

    @Override
    public int getContentView() {
        return R.layout.activity_theme;
    }

    @Override
    public int getStatusBarOptions() {
        return StatusBarOptions.LAYOUT_FULLSCREEN_STATUS_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getIntent().getIntExtra("id", 0);
        Subscription subscription = DailyApiManager.getInstance().getThemeDetails(id)
                .subscribe(new Subscriber<ThemeDetails>() {
                    @Override
                    public void onNext(ThemeDetails themeDetails) {
                        onGetSuccess(themeDetails);
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

    public void onGetSuccess(ThemeDetails themeDetails) {
        this.mThemeDetails = themeDetails;
        setToolbarTitle(mThemeDetails.name);
        mDescriptionText.setText("@ " + mThemeDetails.description);
        Glide.with(ThemeActivity.this)
                .load(mThemeDetails.background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(new GlideDrawableImageViewTarget(mThemeImage) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        readyBlur();
                    }
                });
        if (mAdapter == null) {
            initRecyclerView();
        } else {
            mAdapter.syncData(mThemeDetails);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initRecyclerView() {
        mAdapter = new ThemeDetailsAdapter(this, mThemeDetails);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                DailyDetailsActivity.start(ThemeActivity.this, mThemeDetails.stories.get(position).id, ((ThemeDetailsAdapter.MyViewHolder) holder).image);
            }
        });
    }

    private void readyBlur() {
        mBlurImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                Blur blur = new Blur(ThemeActivity.this, mThemeImage, mBlurImage);
                blur.blur(6, 25);
                ValueAnimator animator = ValueAnimator.ofFloat(1.0f, 0.0f);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mThemeImage.setAlpha((Float) animation.getAnimatedValue());
                    }
                });
                animator.setDuration(1000);
                animator.start();
            }
        }, 1000);
    }

    public void onGetFailed(String errorMsg) {
        SnackBarUtils.makeShort(this, mLoadingView, errorMsg).show();
    }

    public void onGetDone() {
        stopLoadingAnim();
    }

    private void startLoadingAnim() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnim() {
        mLoadingView.setVisibility(View.GONE);
    }

    public static void start(Activity activity, int id) {
        Intent intent = new Intent(activity, ThemeActivity.class);
        intent.putExtra("id", id);
        activity.startActivity(intent);
    }

}
