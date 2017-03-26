package com.melodyxxx.puredaily.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.entity.daily.Latest;
import com.melodyxxx.puredaily.utils.L;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.widget.AutoViewPager;
import com.melodyxxx.puredaily.widget.IndicatorView;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/5/31.
 * Description: 最新消息适配器
 */
public class LatestAdapter extends BaseAdapter<LatestAdapter.MyViewHolder> {

    private static final int TYPE_TOP_LATEST = 0;
    private static final int TYPE_NORMAL_LATEST = 1;

    private static final int POSITION_HEADER_TOP_LATEST = 0;

    private Latest mLatest;
    private Context mContext;

    private LayoutInflater mInflater;

    private TopLatestPageAdapter mTopLatestAdapter;
    private AutoViewPager mTopLatestViewPager;
    private IndicatorView mIndicatorView;


    public LatestAdapter(Context context, Latest latest) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mLatest = latest;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (!isTopLatestHeaderPosition(position)) {
            bindNormalLatestItem(holder, position);
        }
    }

    private void bindTopLatestItem() {
        mTopLatestAdapter = new TopLatestPageAdapter(mContext, mLatest.top_stories);
        mTopLatestViewPager.setAdapter(mTopLatestAdapter);
        mIndicatorView.bind(mTopLatestViewPager, false);
        mIndicatorView.setCount(mLatest.top_stories.size());
    }

    private void bindNormalLatestItem(MyViewHolder holder, int position) {
        Latest.Story story = mLatest.stories.get(position);
        String imageUrl = story.images[0];
        String title = story.title;
        if (!PrefUtils.getBoolean(mContext, PrefConstants.MODE_NO_PIC, false)) {
            if (!TextUtils.isEmpty(imageUrl)) {
                Glide.with(mContext)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .into(holder.image);
            } else {
                holder.image.setImageDrawable(null);
            }
            holder.image.setVisibility(View.VISIBLE);
        } else {
            holder.image.setVisibility(View.GONE);
        }
        holder.title.setText(title);
    }

    /**
     * 同步数据源
     *
     * @param latest
     */
    public void syncData(Latest latest) {
        this.mLatest = latest;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP_LATEST) {
            if (mTopLatestHeaderView == null) {
                mTopLatestHeaderView = mInflater.inflate(R.layout.item_header_top_latest, parent, false);
                mTopLatestViewPager = (AutoViewPager) mTopLatestHeaderView.findViewById(R.id.view_pager);
                mIndicatorView = (IndicatorView) mTopLatestHeaderView.findViewById(R.id.indicator_view);
                bindTopLatestItem();
            }
            return new MyViewHolder(mTopLatestHeaderView);
        } else {
            return new MyViewHolder(mInflater.inflate(R.layout.item_latest, parent, false));
        }
    }

    @Override
    public int getItemCount() {
        return mLatest.stories.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isTopLatestHeaderPosition(position)) {
            return TYPE_TOP_LATEST;
        } else {
            return TYPE_NORMAL_LATEST;
        }
    }

    public boolean isTopLatestHeaderPosition(int position) {
        return mLatest.top_stories != null && position == POSITION_HEADER_TOP_LATEST;
    }

    private View mTopLatestHeaderView;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        // Header list views

        // Normal list views
        public ImageView image;
        public TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            if (itemView == mTopLatestHeaderView) {
                return;
            }
            // List view
            image = (ImageView) itemView.findViewById(R.id.latest_image);
            title = (TextView) itemView.findViewById(R.id.latest_title);
        }

    }

    private void setAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400);
        view.startAnimation(anim);
    }

}
