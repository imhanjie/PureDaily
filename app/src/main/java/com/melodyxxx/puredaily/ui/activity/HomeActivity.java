package com.melodyxxx.puredaily.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.dao.CollectionManager;
import com.melodyxxx.puredaily.dao.ThemeManager;
import com.melodyxxx.puredaily.entity.app.LatestVersion;
import com.melodyxxx.puredaily.entity.daily.Theme;
import com.melodyxxx.puredaily.entity.daily.ThemeDetails;
import com.melodyxxx.puredaily.http.app.AppApiManager;
import com.melodyxxx.puredaily.http.daily.DailyApiManager;
import com.melodyxxx.puredaily.ui.fragment.CollectionsFragment;
import com.melodyxxx.puredaily.ui.fragment.LatestFragment;
import com.melodyxxx.puredaily.ui.fragment.ThemesSubscribeFragment;
import com.melodyxxx.puredaily.utils.CommonUtils;
import com.melodyxxx.puredaily.utils.L;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.widget.PureAlertDialog;

import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;

public class HomeActivity extends SubscriptionActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int SUBSCRIBED_MENU_GROUP_ID = 19950827;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private ImageView mNavHeaderImgView;
    private SwitchCompat mPicModeSwitch;
    private TextView mCollectionsCount;

    @Override
    public int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    public int getStatusBarOptions() {
        return StatusBarOptions.LAYOUT_FULLSCREEN_LIGHT_STATUS_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle(null);
        initDrawerLayout();
        initNavView();
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            switchFragment(new LatestFragment());
        }
        if (PrefUtils.getBoolean(this, PrefConstants.AUTO_CHECK_APP_UPDATE, PrefConstants.DEFAULT_CHECK_APP_UPDATE)) {
            checkLatestVersionInfo();
        }
    }

    private void checkLatestVersionInfo() {
        Subscription subscription = AppApiManager.getInstance().getLatestVersion()
                .subscribe(new Observer<LatestVersion>() {

                    @Override
                    public void onNext(final LatestVersion latestVersion) {
                        int latestVersionCode = latestVersion.version_code;
                        if (latestVersionCode <= CommonUtils.getVersionCode(HomeActivity.this)) {
//                            SnackBarUtils.makeShort(HomeActivity.this, mToolbar, getString(R.string.tip_app_no_update)).show();
                            return;
                        }
                        PureAlertDialog dialog = new PureAlertDialog(HomeActivity.this, String.format(getString(R.string.dialog_title_app_update), latestVersion.version_name), latestVersion.changelog);
                        AlertDialog.Builder builder = dialog.getBuilder();
                        builder.setPositiveButton(R.string.dialog_action_update_now, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CommonUtils.jumpTo(HomeActivity.this, latestVersion.download_url);
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

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                });

        mCompositeSubscription.add(subscription);

    }

    private void initNavView() {
        // 初始化抽屉Header ImageView
        mNavHeaderImgView = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.iv_header);
        mNavHeaderImgView.setImageResource(R.drawable.img_header_day);
        // 初始化抽屉无图模式SwitchCompat
        Menu menu = mNavigationView.getMenu();
        MenuItem picModeMenuItem = menu.findItem(R.id.nav_pic_mode);
        mPicModeSwitch = (SwitchCompat) MenuItemCompat.getActionView(picModeMenuItem).findViewById(R.id.view_switch);
        mPicModeSwitch.setChecked(PrefUtils.getBoolean(this, PrefConstants.MODE_NO_PIC, false));
        mPicModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrefUtils.putBoolean(HomeActivity.this, PrefConstants.MODE_NO_PIC, isChecked);
                mPicModeSwitch.setChecked(isChecked);
                switchFragment(new LatestFragment());
            }
        });
        // 初始化收藏夹数量TextView
        MenuItem collectionsMenuItem = menu.findItem(R.id.nav_collections);
        mCollectionsCount = (TextView) MenuItemCompat.getActionView(collectionsMenuItem).findViewById(R.id.text_view);
        //  ---------------------------- Test -----------------------------------
        updateSubscribedMenu();
    }

    public void updateSubscribedMenu() {
        Menu menu = mNavigationView.getMenu();
        menu.removeGroup(SUBSCRIBED_MENU_GROUP_ID);
        // 获取已订阅的主题
        List<Theme> themes = ThemeManager.queryAll();
        SubMenu subscribedMenu = menu.addSubMenu(SUBSCRIBED_MENU_GROUP_ID, Menu.NONE, 2, "已订阅");
        Theme theme;
        for (int i = 0; i < themes.size(); i++) {
            theme = themes.get(i);
            subscribedMenu.add(Menu.NONE, (int) theme.getId(), Menu.NONE, theme.getName()).setIcon(getIconResIdByThemeId((int) theme.getId()));
        }
    }

    public void updateCollectionsCount() {
        mCollectionsCount.setText(String.valueOf(CollectionManager.count()));
    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    public void switchFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateCollectionsCount();
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                SettingsActivity.start(this);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        final int itemId = item.getItemId();
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (itemId) {
                    case R.id.nav_latest_daily: {
                        LatestFragment latestFragment = new LatestFragment();
                        switchFragment(latestFragment);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.nav_collections: {
                        CollectionsFragment collectionsFragment = new CollectionsFragment();
                        switchFragment(collectionsFragment);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.nav_themes_subscribe: {
                        ThemesSubscribeFragment themesSubscribeFragment = new ThemesSubscribeFragment();
                        switchFragment(themesSubscribeFragment);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.nav_about: {
                        AboutActivity.startAboutActivity(HomeActivity.this);
                        break;
                    }
                    case R.id.nav_settings: {
                        SettingsActivity.start(HomeActivity.this);
                        break;
                    }
                    default:
                        ThemeActivity.start(HomeActivity.this, itemId);
                        break;
                }
            }
        }, 300);
        return false;
    }

    private int getIconResIdByThemeId(int id) {
        int resId;
        switch (id) {
            case 2:
                resId = R.drawable.ic_theme_game;
                break;
            case 3:
                resId = R.drawable.ic_theme_movie;
                break;
            case 4:
                resId = R.drawable.ic_theme_design;
                break;
            case 5:
                resId = R.drawable.ic_theme_company;
                break;
            case 6:
                resId = R.drawable.ic_theme_finance;
                break;
            case 7:
                resId = R.drawable.ic_theme_music;
                break;
            case 8:
                resId = R.drawable.ic_theme_sports;
                break;
            case 9:
                resId = R.drawable.ic_theme_anime;
                break;
            case 10:
                resId = R.drawable.ic_theme_net_safe;
                break;
            case 11:
                resId = R.drawable.ic_theme_boring;
                break;
            case 12:
                resId = R.drawable.ic_theme_recommend;
                break;
            case 13:
            default:
                resId = R.drawable.ic_theme_psychology;
                break;
        }
        return resId;
    }

}
