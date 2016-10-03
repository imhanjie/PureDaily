package com.melodyxxx.puredaily.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.dao.ThemeManager;
import com.melodyxxx.puredaily.entity.daily.Theme;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.widget.SubscribeTextView;

import java.util.List;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  2016/09/22.
 * Description: Themes列表适配器
 */
public class ThemesSubscribeAdapter extends BaseAdapter<ThemesSubscribeAdapter.MyViewHolder> {

    private List<Theme> mThemes;
    // 保存各Theme的订阅状态
    private boolean[] mSubscribedStatuses;
    private Context mContext;
    private LayoutInflater mInflater;

    public ThemesSubscribeAdapter(Context context, List<Theme> themes) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mThemes = themes;
        initSubscribedStatuses();
    }

    /**
     * 初始化各Theme的订阅状态
     */
    private void initSubscribedStatuses() {
        mSubscribedStatuses = new boolean[mThemes.size()];
        for (int i = 0; i < mThemes.size(); i++) {
            mSubscribedStatuses[i] = ThemeManager.isSubscribed(mThemes.get(i).id);
        }
    }

    public void setSubscribe(int position, boolean subscribe) {
        mSubscribedStatuses[position] = subscribe;
        if (subscribe) {
            Theme theme = mThemes.get(position);
            theme.time = System.currentTimeMillis();
            ThemeManager.insert(theme);
        } else {
            ThemeManager.deleteById(mThemes.get(position).id);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        Theme theme = mThemes.get(position);
        String themeImageUrl = theme.thumbnail;
        String themeName = theme.name;
        String themeDesc = theme.description;
        if (!PrefUtils.getBoolean(mContext, PrefConstants.MODE_NO_PIC, false)) {
            Glide.with(mContext)
                    .load(themeImageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .crossFade()
                    .into(new GlideDrawableImageViewTarget(holder.themeImage) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                        }
                    });
            holder.themeImage.setVisibility(View.VISIBLE);
        } else {
            holder.themeImage.setVisibility(View.GONE);
        }
        holder.themeName.setText(themeName);
        holder.themeDescription.setText(themeDesc);
        holder.subscribeTextView.setSubscribe(mSubscribedStatuses[position]);
        // 开启动画
        setAnimation(holder.itemView);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_themes_subscribe, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mThemes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView themeImage;
        TextView themeName;
        TextView themeDescription;
        public SubscribeTextView subscribeTextView;

        MyViewHolder(View itemView) {
            super(itemView);
            themeImage = (ImageView) itemView.findViewById(R.id.theme_image);
            themeName = (TextView) itemView.findViewById(R.id.theme_name);
            themeDescription = (TextView) itemView.findViewById(R.id.theme_description);
            subscribeTextView = (SubscribeTextView) itemView.findViewById(R.id.subscribe_text_view);
        }

    }

    private void setAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400);
        view.startAnimation(anim);
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        holder.itemView.clearAnimation();
    }
}
