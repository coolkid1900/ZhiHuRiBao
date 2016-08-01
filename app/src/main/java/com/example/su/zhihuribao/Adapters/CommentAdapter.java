package com.example.su.zhihuribao.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.su.zhihuribao.Model.CommentsItem;
import com.example.su.zhihuribao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 16/5/11.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    Context mContext;
    List<CommentsItem> mItemList=new ArrayList<>();


    public CommentAdapter(Context context, List<CommentsItem> list) {
        this.mContext=context;
        this.mItemList=list;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_comments,parent,false);
        CommentHolder commentHolder=new CommentHolder(view);
        return commentHolder;
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        CommentsItem commentsItem=mItemList.get(position);
        if (commentsItem.getAvatar()==null){
            holder.iv_avatar.setVisibility(View.INVISIBLE);
        }else {
            Glide.with(mContext).load(commentsItem.getAvatar()).centerCrop().into(holder.iv_avatar);
        }
        holder.tv_author.setText(commentsItem.getAuthor());
        holder.tv_comment.setText(commentsItem.getComment());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        ImageView iv_avatar;
        TextView tv_author;
        TextView tv_comment;

        public CommentHolder(View itemView) {
            super(itemView);
            iv_avatar = (ImageView) itemView.findViewById(R.id.comment_avatar);
            tv_author = (TextView) itemView.findViewById(R.id.comment_author);
            TextPaint textPaint=tv_author.getPaint();
            textPaint.setFakeBoldText(true);
            tv_comment = (TextView) itemView.findViewById(R.id.comment);
        }
    }
}
