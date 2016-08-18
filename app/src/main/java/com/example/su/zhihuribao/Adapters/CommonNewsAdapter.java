package com.example.su.zhihuribao.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.su.zhihuribao.Model.CommonNewsItem;
import com.example.su.zhihuribao.Interfaces.OnItemClickListener;
import com.example.su.zhihuribao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 16/5/10.
 */
public class CommonNewsAdapter extends RecyclerView.Adapter<CommonNewsAdapter.HotNewsHolder> {
    Context mContext;
    List<CommonNewsItem> mItemList = new ArrayList<>();
    OnItemClickListener mOnItemClickListener;

    public CommonNewsAdapter(Context context, List<CommonNewsItem> list) {
        this.mContext = context;
        this.mItemList = list;
    }

    @Override
    public HotNewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_homepage, parent, false);
        HotNewsHolder hotNewsHolder = new HotNewsHolder(view, mOnItemClickListener);
        return hotNewsHolder;
    }

    @Override
    public void onBindViewHolder(HotNewsHolder holder, int position) {
        CommonNewsItem commonNewsItem = mItemList.get(position);
        if (commonNewsItem.getThumbnail() == null) {
            holder.mImageView.setVisibility(View.GONE);
        } else {
            Glide.with(mContext).load(commonNewsItem.getThumbnail()).centerCrop().into(holder.mImageView);
        }
        holder.mTextView.setText(commonNewsItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public class HotNewsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImageView;
        TextView mTextView;
        CardView mCardView;
        OnItemClickListener mOnItemClickListener;

        public HotNewsHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image_item_homepage);
            mTextView = (TextView) itemView.findViewById(R.id.text_item_homepage);
            mCardView = (CardView) itemView.findViewById(R.id.card_view_item);
            this.mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }
}



