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
import com.melodyxxx.puredaily.entity.daily.ThemeDetails;
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
public class ThemeDetailsAdapter extends BaseAdapter<ThemeDetailsAdapter.MyViewHolder> {

    private ThemeDetails mThemeDetails;
    private Context mContext;

    private LayoutInflater mInflater;

    public ThemeDetailsAdapter(Context context, ThemeDetails themeDetails) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mThemeDetails = themeDetails;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ThemeDetails.Story story = mThemeDetails.stories.get(position);
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
     * @param themeDetails
     */
    public void syncData(ThemeDetails themeDetails) {
        this.mThemeDetails = themeDetails;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mInflater.inflate(R.layout.item_latest, parent, false));
    }

    @Override
    public int getItemCount() {
        return mThemeDetails.stories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.latest_image);
            title = (TextView) itemView.findViewById(R.id.latest_title);
        }

    }

}
