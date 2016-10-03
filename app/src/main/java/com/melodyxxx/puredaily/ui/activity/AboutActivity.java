package com.melodyxxx.puredaily.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.entity.app.LatestVersion;
import com.melodyxxx.puredaily.http.app.AppApiManager;
import com.melodyxxx.puredaily.utils.Blur;
import com.melodyxxx.puredaily.utils.CommonUtils;
import com.melodyxxx.puredaily.utils.SnackBarUtils;
import com.melodyxxx.puredaily.widget.PureAlertDialog;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscription;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/6/4.
 * Description:
 */
public class AboutActivity extends SubscriptionActivity implements AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @BindView(R.id.tv_version_info)
    TextView mVersionInfo;

    @BindView(R.id.iv_header_img)
    ImageView mHeaderImg;

    @BindView(R.id.iv_blur)
    ImageView mBlurImg;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

    @Override
    public int getContentView() {
        return R.layout.activity_about;
    }

    @Override
    public int getStatusBarOptions() {
        return StatusBarOptions.LAYOUT_FULLSCREEN_STATUS_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        mAppBarLayout.addOnOffsetChangedListener(this);
        mVersionInfo.setText(String.format(getString(R.string.activity_about_version_info), CommonUtils.getVersionName(this), CommonUtils.getVersionCode(this)));
        mHeaderImg.setImageResource(R.drawable.img_header_day);
        mBlurImg.post(new Runnable() {
            @Override
            public void run() {
                Blur blur = new Blur(AboutActivity.this, mHeaderImg, mBlurImg);
                blur.blur(32, 25);
            }
        });
    }

    @OnClick(R.id.tv_open_source_address)
    public void jumpToGithub() {
        CommonUtils.jumpTo(this, getString(R.string.open_source_address));
    }

    @OnClick(R.id.rl_weibo)
    public void jumpToWeibo() {
        CommonUtils.jumpTo(this, "http://weibo.com/95han/");
    }

    @OnClick(R.id.rl_email)
    public void copyEmailAddress() {
        CommonUtils.copy2Clipboard(this, "hanjie95@126.com");
        SnackBarUtils.makeShort(this, mHeaderImg, getString(R.string.copy_success)).show();
    }

    @OnClick(R.id.fab)
    public void checkLatestVersionInfo() {
        Subscription subscription = AppApiManager.getInstance().getLatestVersion()
                .subscribe(new Observer<LatestVersion>() {

                    @Override
                    public void onNext(LatestVersion latestVersion) {
                        onGetSuccess(latestVersion);
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        onGetFailed(e.getMessage());
                    }

                });
        mCompositeSubscription.add(subscription);
    }

    private void onGetSuccess(final LatestVersion latestVersion) {
        int latestVersionCode = latestVersion.version_code;
        if (latestVersionCode <= CommonUtils.getVersionCode(AboutActivity.this)) {
            SnackBarUtils.makeShort(AboutActivity.this, mHeaderImg, getString(R.string.tip_app_no_update)).show();
            return;
        }

        PureAlertDialog dialog = new PureAlertDialog(this, String.format(getString(R.string.dialog_title_app_update), latestVersion.version_name), latestVersion.changelog);
        AlertDialog.Builder builder = dialog.getBuilder();
        builder.setPositiveButton(R.string.dialog_action_update_now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommonUtils.jumpTo(AboutActivity.this, latestVersion.download_url);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.dialog_action_remind_next_time, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void onGetFailed(String errorMsg) {
        SnackBarUtils.makeShort(AboutActivity.this, mHeaderImg, errorMsg).show();
    }

    public static void startAboutActivity(Activity activity) {
        activity.startActivity(new Intent(activity, AboutActivity.class));
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float totalScrollRange = appBarLayout.getTotalScrollRange();
        mVersionInfo.setAlpha(Math.abs(verticalOffset + totalScrollRange) / totalScrollRange);
        mHeaderImg.setAlpha(Math.abs(verticalOffset + totalScrollRange) / totalScrollRange);
    }

}
