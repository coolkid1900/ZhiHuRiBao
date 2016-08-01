package com.example.su.zhihuribao.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.su.zhihuribao.UI.Fragments.CommentsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 16/5/10.
 */
public class CommentsViewPagerAdapter extends FragmentPagerAdapter{
    Context mContext;
    List<String> mList=new ArrayList<>();

    public CommentsViewPagerAdapter(FragmentManager fm, Context context, List<String> list) {
        super(fm);
        this.mContext=context;
        this.mList=list;
    }

    @Override
    public Fragment getItem(int position) {
        return CommentsFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position);
    }
}
