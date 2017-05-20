package com.melodyxxx.puredaily.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.dao.CollectionManager;
import com.melodyxxx.puredaily.dao.ThemeManager;
import com.melodyxxx.puredaily.entity.app.AccountStatusChanged;
import com.melodyxxx.puredaily.entity.bmob.BmobCollection;
import com.melodyxxx.puredaily.entity.bmob.BmobTheme;
import com.melodyxxx.puredaily.entity.bmob.BmobUser;
import com.melodyxxx.puredaily.entity.daily.Collection;
import com.melodyxxx.puredaily.entity.daily.Theme;
import com.melodyxxx.puredaily.rx.RxBus;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.utils.Tip;
import com.melodyxxx.puredaily.widget.LoadingDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by hanjie on 2017/5/6.
 */

public class LoginActivity extends SubscriptionActivity {

    private LoadingDialog mLoadingDialog = LoadingDialog.create();

    private boolean isReg = false;

    @BindView(R.id.tv_switch)
    TextView mSwitchTv;

    @BindView(R.id.et_name)
    TextView mNameEt;

    @BindView(R.id.et_pwd)
    TextView mPwdEt;

    @BindView(R.id.et_pwd_confirm)
    TextView mPwdConfirmEt;

    @BindView(R.id.tv_login)
    TextView mLoginTv;

    @Override
    public int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    public int getStatusBarOptions() {
        return StatusBarOptions.LIGHT_STATUS_BAR;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarTitle("账户");
    }

    @OnClick(R.id.tv_switch)
    public void switchMode() {
        isReg = !isReg;
        if (isReg) {
            mPwdConfirmEt.setVisibility(View.VISIBLE);
            mLoginTv.setText("注册");
            mSwitchTv.setText("已有账号");
        } else {
            mPwdConfirmEt.setVisibility(View.GONE);
            mLoginTv.setText("登录");
            mSwitchTv.setText("注册账号");
        }
    }

    @OnClick(R.id.tv_login)
    public void loginOrReg() {
        if (isReg) {
            reg();
        } else {
            login();
        }
    }

    private void reg() {
        final String name = mNameEt.getText().toString().trim();
        final String pwd = mPwdEt.getText().toString().trim();
        String pwdConfirm = mPwdConfirmEt.getText().toString().trim();
        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(pwd)
                || TextUtils.isEmpty(pwdConfirm)) {
            Tip.with(this).onNotice("用户名和密码不能为空");
            return;
        }
        if (!pwd.equals(pwdConfirm)) {
            Tip.with(this).onNotice("两次输入的密码不匹配");
            return;
        }

        mLoadingDialog.showWith(getSupportFragmentManager(), "正在注册");

        // 先查询是否已经有过该用户名注册过
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("name", name);
        query.setLimit(1);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 0) {
                        // 没有数据
                        regToServer(name, pwd);
                        return;
                    }
                    // 查询到数据
                    Tip.with(LoginActivity.this).onNotice("该用户名已注册过");
                    mLoadingDialog.dismiss();
                } else {
                    Tip.with(LoginActivity.this).onNotice("服务器异常:" + e.getMessage());
                    mLoadingDialog.dismiss();
                }
            }
        });
    }

    private void regToServer(String name, String pwd) {
        BmobUser bmobUser = new BmobUser(name, pwd);
        bmobUser.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                mLoadingDialog.dismiss();
                if (e == null) {
                    Tip.with(LoginActivity.this).onSuccess("注册成功");
                    mSwitchTv.performClick();
                } else {
                    Tip.with(LoginActivity.this).onNotice("服务器异常:" + e.getMessage());
                }
            }
        });
    }

    private void login() {
        final String name = mNameEt.getText().toString().trim();
        final String pwd = mPwdEt.getText().toString().trim();
        if (TextUtils.isEmpty(name)
                || TextUtils.isEmpty(pwd)) {
            Tip.with(this).onNotice("用户名和密码不能为空");
            return;
        }
        mLoadingDialog.showWith(getSupportFragmentManager(), "正在登录");
        // login
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("name", name);
        query.addWhereEqualTo("pwd", pwd);
        query.setLimit(1);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if (e == null) {
                    if (object.size() == 0) {
                        // 没有数据
                        Tip.with(LoginActivity.this).onNotice("用户名或密码错误");
                        return;
                    }
                    // 查询到数据
                    loginSuccess(name, pwd);
                } else {
                    Tip.with(LoginActivity.this).onNotice("服务器异常:" + e.getMessage());
                }
            }
        });
    }

    private void loginSuccess(final String name, final String pwd) {
        Tip.with(LoginActivity.this).onSuccess("登录成功");
        PrefUtils.putString(LoginActivity.this, PrefConstants.USER_NAME, name);
        PrefUtils.putString(LoginActivity.this, PrefConstants.USER_PWD, pwd);
        CollectionManager.deleteAll();
        ThemeManager.deleteAll();
        mLoadingDialog.showWith(getSupportFragmentManager(), "正在同步");
        BmobQuery<BmobCollection> query = new BmobQuery<>();
        query.addWhereEqualTo("name", name);
        query.findObjects(new FindListener<BmobCollection>() {
            @Override
            public void done(List<BmobCollection> collections, BmobException e) {
                if (e == null) {
                    if (collections.size() == 0) {
                        return;
                    }
                    // 查询到数据mLoadingDialog
                    insertCollectionsToLocal(collections);
                    RxBus.getInstance().send(new AccountStatusChanged());
                    BmobQuery<BmobTheme> query = new BmobQuery<>();
                    query.addWhereEqualTo("name", name);
                    query.findObjects(new FindListener<BmobTheme>() {
                        @Override
                        public void done(List<BmobTheme> themes, BmobException e) {
                            mLoadingDialog.dismiss();
                            if (e == null) {
                                if (themes.size() == 0) {
                                    return;
                                }
                                insertThemesToLocal(themes);
                                RxBus.getInstance().send(new AccountStatusChanged());
                            } else {
                                Tip.with(LoginActivity.this).onNotice("服务器异常:" + e.getMessage());
                            }
                        }
                    });
                } else {
                    Tip.with(LoginActivity.this).onNotice("服务器异常:" + e.getMessage());
                }
            }
        });
        finish();
    }

    private void insertThemesToLocal(List<BmobTheme> themes) {
        for (BmobTheme theme : themes) {
            Theme t = new Theme(theme.getId(), theme.getColor(), theme.getThumbnail(), theme.getDescription(), theme.getThemeName(), theme.getTime());
            ThemeManager.insert(t);
        }
    }

    private void insertCollectionsToLocal(List<BmobCollection> collections) {
        for (BmobCollection collection : collections) {
            CollectionManager.insert(new Collection(collection.getId(), collection.getTitle(), collection.getImageUrl(), collection.getTime()));
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

}
