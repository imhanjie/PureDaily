package com.melodyxxx.puredaily.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.widget.CheckBoxItemView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/6/4.
 * Description: 设置界面
 */
public class SettingsActivity extends BaseActivity {

    @BindView(R.id.item_auto_check_app_update)
    CheckBoxItemView mAutoCheckAppUpdate;

    @Override
    public int getContentView() {
        return R.layout.activity_settings;
    }

    @Override
    public int getStatusBarOptions() {
        return StatusBarOptions.LIGHT_STATUS_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(R.string.activity_title_settings);
        init();
    }

    private void init() {
        mAutoCheckAppUpdate.setChecked(PrefUtils.getBoolean(this, PrefConstants.AUTO_CHECK_APP_UPDATE, PrefConstants.DEFAULT_CHECK_APP_UPDATE));
    }

    @OnClick(R.id.item_auto_check_app_update)
    public void OnAutoCheckAppUpdateItemClick() {
        boolean isChecked = mAutoCheckAppUpdate.isChecked();
        PrefUtils.putBoolean(this, PrefConstants.AUTO_CHECK_APP_UPDATE, !isChecked);
        mAutoCheckAppUpdate.setChecked(!isChecked);
    }

    public static void start(Activity activity) {
        activity.startActivity(new Intent(activity, SettingsActivity.class));
    }
}
