package com.melodyxxx.puredaily.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.utils.ActivityCollector;
import com.melodyxxx.puredaily.utils.ColorUtils;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar mToolbar;

    public abstract int getContentView();

    public abstract int getStatusBarOptions();

    public static class StatusBarOptions {
        static final int NONE = 0;
        static final int TRANSLUCENT_STATUS_BAR = 1;
        static final int LAYOUT_FULLSCREEN_LIGHT_STATUS_BAR = 2;
        static final int LAYOUT_FULLSCREEN_STATUS_BAR = 3;
        static final int LIGHT_STATUS_BAR = 4;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        setStatusBarOptions(getStatusBarOptions());
        ButterKnife.bind(this);
        initToolBar();
        // NavigationBar跟随主题色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ColorUtils.getColorAccent(this));
        }
        ActivityCollector.addActivity(this);
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setToolbarTitle(String toolbarTitle) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(toolbarTitle);
        }
    }

    public void setToolbarTitle(int resId) {
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setTitle(getString(resId));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    private void setStatusBarOptions(int options) {
        switch (options) {
            case StatusBarOptions.TRANSLUCENT_STATUS_BAR:
                translucentStatusBar();
                break;
            case StatusBarOptions.LAYOUT_FULLSCREEN_STATUS_BAR:
                layoutFullscreen(false);
                break;
            case StatusBarOptions.LAYOUT_FULLSCREEN_LIGHT_STATUS_BAR:
                layoutFullscreen(true);
                break;
            case StatusBarOptions.LIGHT_STATUS_BAR:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                break;
            case StatusBarOptions.NONE:
            default:
                break;
        }
    }

    private void layoutFullscreen(boolean lightStatusBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            if (!lightStatusBar) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            } else {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    private void translucentStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
