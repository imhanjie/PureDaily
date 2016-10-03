package com.melodyxxx.puredaily.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.entity.daily.Latest;
import com.melodyxxx.puredaily.ui.activity.DailyDetailsActivity;
import com.melodyxxx.puredaily.ui.activity.HomeActivity;
import com.melodyxxx.puredaily.utils.Blur;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/09/19.
 * Description:
 */
public class TopLatestPageAdapter extends PagerAdapter {

    private List<Latest.TopStory> mTopStories;
    private List<View> mViews;
    private Context mContext;

    public TopLatestPageAdapter(Context context, List<Latest.TopStory> topStories) {
        this.mTopStories = topStories;
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mViews = new ArrayList<>();
        for (int i = 0; i < topStories.size(); i++) {
            mViews.add(inflater.inflate(R.layout.item_top_latest, null));
        }
    }

    @Override
    public int getCount() {
        return mTopStories.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mViews.get(position);
        final TextView titleText = (TextView) view.findViewById(R.id.tv_title);
        titleText.setText(mTopStories.get(position).title);
        final ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);
        final FrameLayout coverView = (FrameLayout) view.findViewById(R.id.cover_view);
        Glide.with(mContext)
                .load(mTopStories.get(position).image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        Blur blur = new Blur(mContext, imageView, coverView);
                        blur.blur(2f, 25f);
                    }
                });
        // 设置监听
        view.findViewById(R.id.root_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DailyDetailsActivity.start((HomeActivity) mContext, mTopStories.get(position).id, imageView);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViews.get(position));
    }

    public void syncData(List<Latest.TopStory> topStories) {
        this.mTopStories = topStories;
    }

}
