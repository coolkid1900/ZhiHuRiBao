package com.example.su.zhihuribao.UI.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.su.zhihuribao.Adapters.CommentsViewPagerAdapter;
import com.example.su.zhihuribao.R;
import com.example.su.zhihuribao.UI.Custom.ChangeTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 16/5/10.
 */
public class CommentsActivity extends AppCompatActivity{

    TabLayout mTabLayout;
    ViewPager mViewPager;
    Toolbar mToolbar;
    List<String> mList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ChangeTheme.setTheme(CommentsActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        mTabLayout= (TabLayout)findViewById(R.id.tablayout_comments);
        mViewPager= (ViewPager)findViewById(R.id.viewpager_comments);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mList.add("短评论");
        mList.add("长评论");
        CommentsViewPagerAdapter newsViewPagerAdapter =new CommentsViewPagerAdapter(getSupportFragmentManager(),this,mList);
        mViewPager.setAdapter(newsViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
