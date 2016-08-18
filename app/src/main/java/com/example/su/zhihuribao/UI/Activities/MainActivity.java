package com.example.su.zhihuribao.UI.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.su.zhihuribao.R;
import com.example.su.zhihuribao.UI.Fragments.HomeFragment;
import com.example.su.zhihuribao.UI.Fragments.HotnewsFragment;
import com.example.su.zhihuribao.UI.Fragments.TopicTabFragment;
import com.example.su.zhihuribao.UI.Custom.ChangeTheme;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,HomeFragment.OnRecyclerViewCreated{
    private final long ANIMTION_TIME = 500;

    ViewGroup viewGroup;
    ImageView cacheimage;
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    HomeFragment homeFragment;

    int position;
    int scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ChangeTheme.setTheme(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addFragment(0,0);
    }

    private void initView(){
        viewGroup= (ViewGroup) findViewById(R.id.cache_view);
        cacheimage= (ImageView) findViewById(R.id.image_cache);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        homeFragment = new HomeFragment();
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.homepage) {
//            changeView(new HomeFragment());
            addFragment(0,0);
        } else if (id == R.id.topics) {
            changeView(new TopicTabFragment());
        } else if (id == R.id.hotnews) {
            changeView(new HotnewsFragment());
        } else if (id == R.id.theme) {
            changeTheme();
        } else if (id == R.id.setting) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        } else if (id == R.id.star) {
            Intent intent = new Intent(MainActivity.this, StarActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeView(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void changeTheme(){
        setDrawableCahe();
        setTheme();
        getState();
    }

    /**
     * 获取布局的DrawableCache给ImageView覆盖Fragment
     */
    private void setDrawableCahe() {
        //设置false清除缓存
        viewGroup.setDrawingCacheEnabled(false);
        //设置true之后可以获取Bitmap
        viewGroup.setDrawingCacheEnabled(true);
        cacheimage.setImageBitmap(viewGroup.getDrawingCache());
        cacheimage.setAlpha(1f);
        cacheimage.setVisibility(View.VISIBLE);
    }

    /**
     * 设置主题
     */
    private void setTheme(){
        if (ChangeTheme.getThemeValue(MainActivity.this) == 0){
            ChangeTheme.setThemeValue(MainActivity.this,1);
            setTheme(R.style.NightTheme);
            toolbar.setBackgroundColor(getResources().getColor(R.color.night_toolbar));
            navigationView.setBackgroundColor(getResources().getColor(R.color.night_background));
        } else {
            ChangeTheme.setThemeValue(MainActivity.this,0);
            setTheme(R.style.DayTheme);
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            navigationView.setBackgroundColor(getResources().getColor(R.color.day_background));
        }
    }

    /**
     * 获取当前fragment状态，在Demo中简单演示了RecyclerView的位置恢复
     */
    public void getState() {
        RecyclerView recyclerView = homeFragment.getRecyclerView();
        recyclerView.stopScroll();
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            position = layoutManager.findFirstVisibleItemPosition();
            scroll = recyclerView.getChildAt(0).getTop();

            addFragment(position,scroll);
        }
    }

    /**
     * 添加Fragment,如果已存在Fragment就先移除在添加
     * @param position
     * @param scroll
     */
    private void addFragment(int position,int scroll) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (homeFragment != null){
            fragmentTransaction.remove(homeFragment);
        }
        homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position",position);
        bundle.putInt("scroll",scroll);
        homeFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.fragment_container, homeFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void recyclerViewCreated() {
        startAnimation(cacheimage);
    }

    /**
     * ImageView的动画
     * @param view
     */
    private void startAnimation(final View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(1f).setDuration(ANIMTION_TIME);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float n = (float) animation.getAnimatedValue();
                view.setAlpha(1f - n);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                cacheimage.setVisibility(View.INVISIBLE);
            }
        });
        animator.start();
    }
}
