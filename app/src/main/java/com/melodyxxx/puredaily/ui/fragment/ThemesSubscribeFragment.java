package com.melodyxxx.puredaily.ui.fragment;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.entity.bmob.BmobTheme;
import com.melodyxxx.puredaily.entity.daily.Theme;
import com.melodyxxx.puredaily.http.daily.DailyApiManager;
import com.melodyxxx.puredaily.ui.activity.HomeActivity;
import com.melodyxxx.puredaily.ui.adapter.BaseAdapter;
import com.melodyxxx.puredaily.ui.adapter.ThemesSubscribeAdapter;
import com.melodyxxx.puredaily.utils.Blur;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.utils.SnackBarUtils;
import com.melodyxxx.puredaily.utils.Tip;
import com.melodyxxx.puredaily.widget.LoadingDialog;
import com.melodyxxx.puredaily.widget.PureAlertDialog;
import com.melodyxxx.puredaily.widget.SubscribeTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscriber;
import rx.Subscription;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/22.
 * Description:
 */

public class ThemesSubscribeFragment extends SubscriptionFragment {

    @BindView(R.id.view_blur_one)
    View mBlurOneView;

    @BindView(R.id.view_blur_two)
    View mBlurTwoView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading)
    AVLoadingIndicatorView mLoadingView;

    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;

    private ThemesSubscribeAdapter mAdapter;

    private List<Theme> mThemes;

    private boolean mTurnBlurOne = false;

    private int mLastPosition = -1;
    private LoadingDialog mLoadingDialog = LoadingDialog.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (!PrefUtils.getBoolean(getContext(), PrefConstants.THEMES_SUBSCRIBE_TIPS_SHOWED, false)) {
            showTipsDialog();
        }
    }

    private void showTipsDialog() {
        PureAlertDialog dialog = new PureAlertDialog(getActivity(), R.string.tips, R.string.dialog_content_themes_subscribe);
        AlertDialog.Builder builder = dialog.getBuilder();
        builder.setPositiveButton(R.string.dialog_action_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrefUtils.putBoolean(getContext(), PrefConstants.THEMES_SUBSCRIBE_TIPS_SHOWED, true);
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        dialog.show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ((HomeActivity) getActivity()).setToolbarTitle(R.string.fragment_title_themes_subscribe);
        View view = inflater.inflate(R.layout.fragment_themes_subscribe, container, false);
        ButterKnife.bind(this, view);
        getThemesListData();
        mBlurOneView.post(new Runnable() {
            @Override
            public void run() {
                Blur blur = new Blur(getContext(), BitmapFactory.decodeResource(getResources(), R.drawable.img_theme_blur), mBlurOneView);
                blur.blur(32, 25);
            }
        });
        return view;
    }

    private void initRecyclerView() {
        mAdapter = new ThemesSubscribeAdapter(getContext(), mThemes);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(lm);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final RecyclerView.ViewHolder holder, final int position) {
                final SubscribeTextView subscribeTextView = ((ThemesSubscribeAdapter.MyViewHolder) holder).subscribeTextView;
                final boolean isSubscribed = subscribeTextView.isSubscribed();
                Theme clickTheme = mThemes.get(position);
                if (!isSubscribed) {
                    // 未订阅
                    mLoadingDialog.showWith(getFragmentManager(), "正在订阅");
                    BmobTheme bmobTheme = new BmobTheme();
                    bmobTheme.setName(getCurrentUserName());
                    bmobTheme.setId(clickTheme.getId());
                    bmobTheme.setColor(clickTheme.getColor());
                    bmobTheme.setThumbnail(clickTheme.getThumbnail());
                    bmobTheme.setDescription(clickTheme.getDescription());
                    bmobTheme.setThemeName(clickTheme.getName());
                    bmobTheme.setTime(System.currentTimeMillis());
                    bmobTheme.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            mLoadingDialog.dismiss();
                            if (e == null) {
                                Tip.with(getContext()).onSuccess("订阅成功");
                                successFromServer(holder, position);
                            } else {
                                Tip.with(getContext()).onNotice("服务器异常:" + e.getMessage());
                            }
                        }
                    });
                } else {
                    // 已订阅
                    mLoadingDialog.showWith(getFragmentManager(), "请稍后");
                    // 查询objectId
                    BmobQuery<BmobTheme> query = new BmobQuery<>();
                    query.addWhereEqualTo("name", getCurrentUserName());
                    query.addWhereEqualTo("id", clickTheme.getId());
                    query.setLimit(1);
                    query.findObjects(new FindListener<BmobTheme>() {
                        @Override
                        public void done(List<BmobTheme> object, BmobException e) {
                            if (e == null) {
                                if (object.size() == 0) {
                                    // 没有数据
                                    mLoadingDialog.dismiss();
                                    return;
                                }
                                // 查询到数据
                                BmobTheme bmobTheme = new BmobTheme();
                                bmobTheme.setObjectId(object.get(0).getObjectId());
                                bmobTheme.delete(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        mLoadingDialog.dismiss();
                                        if (e == null) {
                                            Tip.with(getContext()).onSuccess("取消订阅成功");
                                            successFromServer(holder, position);
                                        } else {
                                            Tip.with(getContext()).onNotice("取消订阅失败" + e.getMessage());
                                        }
                                    }
                                });
                            } else {
                                mLoadingDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
    }

    private void successFromServer(RecyclerView.ViewHolder holder, int position) {
        final SubscribeTextView subscribeTextView = ((ThemesSubscribeAdapter.MyViewHolder) holder).subscribeTextView;
        final boolean isSubscribed = subscribeTextView.isSubscribed();
        mAdapter.setSubscribe(position, !isSubscribed);
        subscribeTextView.setSubscribe(!isSubscribed);
        ((HomeActivity) getActivity()).updateSubscribedMenu();
        if (position == mLastPosition || isSubscribed) {
            return;
        }
        Blur blur = new Blur(getContext(), ((ThemesSubscribeAdapter.MyViewHolder) holder).themeImage, mTurnBlurOne ? mBlurOneView : mBlurTwoView);
        blur.blur(32, 25);
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentAnimatedValue = (float) animation.getAnimatedValue();
                if (mTurnBlurOne) {
                    mBlurOneView.setAlpha(currentAnimatedValue);
                    mBlurTwoView.setAlpha(1 - currentAnimatedValue);
                } else {
                    mBlurTwoView.setAlpha(currentAnimatedValue);
                    mBlurOneView.setAlpha(1 - currentAnimatedValue);
                }
                if (currentAnimatedValue == 1f) {
                    mTurnBlurOne = !mTurnBlurOne;
                }
            }
        });
        animator.setDuration(400);
        animator.start();
        mLastPosition = position;
    }

    /**
     * 获取可订阅的Themes列表
     */
    private void getThemesListData() {
        Subscription subscription = DailyApiManager.getInstance().getThemes()
                .subscribe(new Subscriber<List<Theme>>() {
                    @Override
                    public void onStart() {
                        startLoadingAnim();
                    }

                    @Override
                    public void onNext(List<Theme> themes) {
                        mThemes = themes;
                        if (mThemes != null && mThemes.size() != 0) {
                            initRecyclerView();
                        } else {
                            displayEmptyView();
                        }
                        stopLoadingAnim();
                    }

                    @Override
                    public void onCompleted() {
                        stopLoadingAnim();
                    }

                    @Override
                    public void onError(Throwable e) {
                        SnackBarUtils.makeShort(getContext(), mEmptyView, e.getMessage());
                        stopLoadingAnim();
                    }

                });
        mCompositeSubscription.add(subscription);
    }

    private void startLoadingAnim() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    private void stopLoadingAnim() {
        mLoadingView.setVisibility(View.GONE);
    }

    private void displayEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

}
