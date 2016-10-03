package com.melodyxxx.puredaily.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 支持ItemClick的RecyclerView适配器
 * <p>
 * Created by hanjie on 2016/5/31.
 */
public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected BaseAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder, holder.getLayoutPosition());
                }
            });
        }
    }

}
