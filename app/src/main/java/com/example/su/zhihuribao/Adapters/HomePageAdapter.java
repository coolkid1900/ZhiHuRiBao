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
import com.example.su.zhihuribao.Model.ImagesNewsItem;
import com.example.su.zhihuribao.Interfaces.OnItemClickListener;
import com.example.su.zhihuribao.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 16/5/6.
 */
public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.HomePageHolder> {

    List<ImagesNewsItem> mItemList=new ArrayList<>();
    OnItemClickListener mOnItemClickListener;
    Context mContext;

    public HomePageAdapter(Context context,List<ImagesNewsItem> list) {
        this.mContext=context;
        this.mItemList=list;
    }

    @Override
    public HomePageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homepage,parent,false);
        HomePageHolder topicListHolder=new HomePageHolder(view,mOnItemClickListener);
        return topicListHolder;
    }

    @Override
    public void onBindViewHolder(HomePageHolder holder, int position) {
        ImagesNewsItem item=mItemList.get(position);
        if (item.getFirstImage()==null){
            holder.mImageView.setVisibility(View.GONE);
        }else {
            Glide.with(mContext).load(item.getFirstImage()).centerCrop().into(holder.mImageView);
        }
        holder.mTextView.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }


    public class HomePageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageView;
        TextView mTextView;
        CardView mCardView;
        OnItemClickListener mOnItemClickListener;

        public HomePageHolder(View itemView, OnItemClickListener onItemClickListener) {
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
