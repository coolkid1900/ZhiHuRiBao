package com.example.su.zhihuribao.UI.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.su.zhihuribao.Adapters.CommonNewsAdapter;
import com.example.su.zhihuribao.DB.StarDBHelper;
import com.example.su.zhihuribao.Interfaces.OnItemClickListener;
import com.example.su.zhihuribao.Model.CommonNewsItem;
import com.example.su.zhihuribao.R;
import com.example.su.zhihuribao.UI.Custom.ChangeTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 16/5/13.
 */
public class StarActivity extends AppCompatActivity{
    Toolbar mToolbar;
    RecyclerView mRecyclerView;

    private StarDBHelper mDBHelper;
    private SQLiteDatabase mDatabase;
    private String content_id;
    private String image;
    private String shareUrl;
    private String title;
    private List<CommonNewsItem> mItemList=new ArrayList<>();
    private CommonNewsAdapter mCommonNewsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ChangeTheme.setTheme(StarActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star);
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView= (RecyclerView) findViewById(R.id.recyclerview_star);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDBHelper=new StarDBHelper(this,"Star.db",null,1);
        mDatabase=mDBHelper.getWritableDatabase();
        Cursor cursor=mDatabase.query("STAR",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                content_id=cursor.getString(cursor.getColumnIndex("id"));
                image=cursor.getString(cursor.getColumnIndex("image"));
                shareUrl=cursor.getString(cursor.getColumnIndex("url"));
                title=cursor.getString(cursor.getColumnIndex("title"));
                CommonNewsItem commonNewsItem=new CommonNewsItem(content_id,shareUrl,image,title);
                mItemList.add(commonNewsItem);
            }while (cursor.moveToNext());
        }
        mCommonNewsAdapter =new CommonNewsAdapter(StarActivity.this,mItemList);
        mRecyclerView.setAdapter(mCommonNewsAdapter);
        mCommonNewsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(StarActivity.this,ContentActivity.class);
                intent.putExtra("id",mItemList.get(position).getId());
                intent.putExtra("title",mItemList.get(position).getTitle());
                intent.putExtra("image",mItemList.get(position).getThumbnail());
                startActivity(intent);
            }
        });
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
