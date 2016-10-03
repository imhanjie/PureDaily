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
import com.melodyxxx.puredaily.R;
import com.melodyxxx.puredaily.constant.PrefConstants;
import com.melodyxxx.puredaily.entity.daily.AllComments;
import com.melodyxxx.puredaily.utils.PrefUtils;
import com.melodyxxx.puredaily.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论适配器
 * <p/>
 * Created by hanjie on 2016/6/2.
 */
public class CommentAdapter extends BaseAdapter<CommentAdapter.MyViewHolder> {

    private static final int TYPE_COMMENT = 0;
    private static final int TYPE_LONG_COMMENT_HEADER = 1;
    private static final int TYPE_SHORT_COMMENT_HEADER = 2;

    // 长评在评论集合中的起始位置
    private int mLongCommentStartPos = -1;

    // 短评在评论集合中的起始位置
    private int mShortCommentStatPos = -1;

    private List<AllComments.Comments.Comment> mComments;
    private Context mContext;

    private View mCommentHeaderView;

    public CommentAdapter(Context context, AllComments allComments) {
        this.mContext = context;
        mComments = new ArrayList<>();
        List<AllComments.Comments.Comment> longComments = allComments.longComments.comments;
        List<AllComments.Comments.Comment> shortComments = allComments.shortComments.comments;
        mComments.addAll(longComments);
        mComments.addAll(shortComments);
        if (longComments.size() != 0) {
            this.mLongCommentStartPos = 0;
            if (shortComments.size() != 0) {
                this.mShortCommentStatPos = longComments.size();
            }
        } else if (shortComments.size() != 0) {
            this.mShortCommentStatPos = 0;
        }
        // 根据是否有长、短评论设置不同的长度
        if (hasLongComment()) {
            // 添加一个null作为Comment Header的占位，以便后面取Comment时position的统一
            this.mComments.add(0, null);
            // 短评起始位置需要偏移
            mShortCommentStatPos++;
        }
        if (hasShortComment()) {
            this.mComments.add(mShortCommentStatPos, null);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (position == mLongCommentStartPos) {
            holder.commentType.setText("长评");
        } else if (position == mShortCommentStatPos) {
            holder.commentType.setText("短评");
        } else {
            AllComments.Comments.Comment comment = mComments.get(position);
            if (!PrefUtils.getBoolean(mContext, PrefConstants.MODE_NO_PIC, false)) {
                Glide.with(mContext)
                        .load(comment.avatar)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .into(holder.avatar);
            } else {
                holder.avatar.setImageResource(R.drawable.ic_default_avatar);
            }
            holder.author.setText(comment.author);
            holder.time.setText(TimeUtils.formatTimeLine(Long.parseLong(comment.time + "000")));
            holder.content.setText(comment.content);
            holder.likeCount.setText(String.valueOf(comment.likes));
        }
        // 开启动画
        setAnimation(holder.itemView);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mLongCommentStartPos) {
            return TYPE_LONG_COMMENT_HEADER;
        }
        if (position == mShortCommentStatPos) {
            return TYPE_SHORT_COMMENT_HEADER;
        }
        return TYPE_COMMENT;

    }

    private boolean hasLongComment() {
        return mLongCommentStartPos >= 0;
    }

    private boolean hasShortComment() {
        return mShortCommentStatPos >= 0;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (viewType == TYPE_LONG_COMMENT_HEADER || viewType == TYPE_SHORT_COMMENT_HEADER) {
            mCommentHeaderView = inflater.inflate(R.layout.item_comment_header, parent, false);
            return new MyViewHolder(mCommentHeaderView);
        } else {
            return new MyViewHolder(inflater.inflate(R.layout.item_comment, parent, false));
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView commentType;

        ImageView avatar;
        TextView author;
        TextView time;
        TextView content;
        TextView likeCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            if (itemView == mCommentHeaderView) {
                commentType = (TextView) itemView.findViewById(R.id.tv_comment_type);
                return;
            }
            avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            content = (TextView) itemView.findViewById(R.id.tv_content);
            likeCount = (TextView) itemView.findViewById(R.id.tv_like_count);
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
