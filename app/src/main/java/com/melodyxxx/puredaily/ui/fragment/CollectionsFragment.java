package com.melodyxxx.puredaily.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.entity.app.AccountStatusChanged;
import com.melodyxxx.puredaily.entity.bmob.BmobCollection;
import com.melodyxxx.puredaily.rx.RxBus;
import com.melodyxxx.puredaily.ui.activity.LoginActivity;
import com.melodyxxx.puredaily.ui.adapter.BaseAdapter;
import com.melodyxxx.puredaily.ui.adapter.CollectionsAdapter;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.dao.CollectionManager;
import com.melodyxxx.puredaily.entity.daily.Collection;
import com.melodyxxx.puredaily.ui.activity.DailyDetailsActivity;
import com.melodyxxx.puredaily.ui.activity.HomeActivity;
import com.melodyxxx.puredaily.utils.ColorUtils;
import com.melodyxxx.puredaily.utils.DividerItemDecoration;
import com.melodyxxx.puredaily.utils.MenuTintUtils;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.utils.SnackBarUtils;
import com.melodyxxx.puredaily.utils.Tip;
import com.melodyxxx.puredaily.widget.LoadingDialog;
import com.melodyxxx.puredaily.widget.PureAlertDialog;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/6/6.
 * Description: 收藏Fragment
 */
public class CollectionsFragment extends SubscriptionFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.loading)
    AVLoadingIndicatorView mLoadingView;

    @BindView(R.id.empty_view)
    LinearLayout mEmptyView;

    private CollectionsAdapter mAdapter;

    private List<Collection> mCollections = new ArrayList<>();
    private LoadingDialog mLoadingDialog = LoadingDialog.create();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (!PrefUtils.getBoolean(getContext(), PrefConstants.COLLECTIONS_TIPS_SHOWED, false)) {
            showTipsDialog();
        }
    }

    private void showTipsDialog() {
        PureAlertDialog dialog = new PureAlertDialog(getActivity(), R.string.tips, R.string.dialog_content_collections_manage);
        AlertDialog.Builder builder = dialog.getBuilder();
        builder.setPositiveButton(R.string.dialog_action_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrefUtils.putBoolean(getContext(), PrefConstants.COLLECTIONS_TIPS_SHOWED, true);
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
        ((HomeActivity) getActivity()).setToolbarTitle(R.string.fragment_title_collections);
        View view = inflater.inflate(R.layout.fragment_collections, container, false);
        ButterKnife.bind(this, view);
        registerExitEvent();
        return view;
    }

    private void registerExitEvent() {
        RxBus.getInstance().toObservable(AccountStatusChanged.class)
                .subscribe(new Action1<AccountStatusChanged>() {
                    @Override
                    public void call(AccountStatusChanged event) {
                        getDataFromDatabase();
                    }
                });
    }

    private void initRecyclerView() {
        mAdapter = new CollectionsAdapter(getContext(), mCollections);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                DailyDetailsActivity.start(getActivity(), (int) mCollections.get(position).getId(), ((CollectionsAdapter.MyViewHolder) holder).image);
            }
        });
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int deletePosition = viewHolder.getLayoutPosition();
//                CollectionManager.deleteById(mCollections.get(deletePosition).getId());
//                mAdapter.delete(deletePosition);
//                ((HomeActivity) getActivity()).updateCollectionsCount();
//                if (CollectionManager.count() == 0) {
//                    displayEmptyView();
//                }
//            }
//        });
//        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 从数据库获取已收藏的文章
     */
    private void getDataFromDatabase() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<Collection>>() {
            @Override
            public void call(Subscriber<? super List<Collection>> subscriber) {
                subscriber.onNext(CollectionManager.queryAll());
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Collection>>() {

                    @Override
                    public void onStart() {
                        startLoadingAnim();
                    }

                    @Override
                    public void onNext(List<Collection> collections) {
                        mCollections = collections;
                        if (mCollections.size() == 0) {
                            displayEmptyView();
                        } else if (mAdapter == null) {
                            initRecyclerView();
                        } else {
                            mAdapter.syncData(mCollections);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCompleted() {
                        stopLoadingAnim();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopLoadingAnim();
                    }
                });

        mCompositeSubscription.add(subscription);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_all_collections: {
                if (mCollections.size() != 0) {
                    clearAllCollections();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearAllCollections() {
        PureAlertDialog dialog = new PureAlertDialog(getActivity(), R.string.tips, R.string.tip_confirm_clear_all_collections);
        AlertDialog.Builder builder = dialog.getBuilder();
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                queryByUserName();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void queryByUserName() {
        mLoadingDialog.showWith(getFragmentManager(), "正在删除");
        BmobQuery<BmobCollection> query = new BmobQuery<>();
        query.addWhereEqualTo("name", getCurrentUserName());
        query.findObjects(new FindListener<BmobCollection>() {
            @Override
            public void done(List<BmobCollection> collections, BmobException e) {
                if (e == null) {
                    if (collections.size() == 0) {
                        return;
                    }
                    batchDelete(collections);
                } else {
                    Tip.with(getContext()).onNotice("服务器异常:" + e.getMessage());
                    mLoadingDialog.dismiss();
                }
            }
        });
    }

    private void batchDelete(List<BmobCollection> collections) {
        ArrayList<BmobObject> bmobObjects = new ArrayList<>();
        for (BmobCollection collection : collections) {
            BmobCollection c = new BmobCollection();
            c.setObjectId(collection.getObjectId());
            bmobObjects.add(c);
        }
        new BmobBatch().deleteBatch(bmobObjects).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> o, BmobException e) {
                mLoadingDialog.dismiss();
                if (e == null) {
                    deleteFromLocal();
                } else {
                    Tip.with(getContext()).onNotice("服务器异常:" + e.getMessage());
                }
            }
        });
    }

    private void deleteFromLocal() {
        CollectionManager.deleteAll();
        getDataFromDatabase();
        SnackBarUtils.makeShort(getContext(), mEmptyView, getString(R.string.tip_clear_all_collections)).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_collections_fragment, menu);
        MenuTintUtils.tintAllIcons(menu, ColorUtils.getColorAccent(getContext()));
        super.onCreateOptionsMenu(menu, inflater);
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

    @Override
    public void onStart() {
        super.onStart();
        getDataFromDatabase();
    }

}
