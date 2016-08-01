package com.example.su.zhihuribao.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.su.zhihuribao.Model.TopicsItem;
import com.example.su.zhihuribao.UI.Fragments.TopicViewPagerFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 16/5/7.
 */
public class TopicsTabAdapter extends FragmentPagerAdapter{

    Context mContext;
    List<TopicsItem> mItemsList=new ArrayList<>();

    public TopicsTabAdapter(FragmentManager fm, Context context, List<TopicsItem> list) {
        super(fm);
        this.mContext=context;
        this.mItemsList=list;
    }

    @Override
    public Fragment getItem(int position) {
        return TopicViewPagerFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mItemsList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItemsList.get(position).getName();
    }
}
