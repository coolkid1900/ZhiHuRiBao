package com.example.su.zhihuribao.UI.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.su.zhihuribao.DB.StarDBHelper;
import com.example.su.zhihuribao.R;
import com.example.su.zhihuribao.Utils.Api;
import com.example.su.zhihuribao.UI.Custom.ChangeTheme;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by su on 16/5/6.
 */
public class ContentActivity extends AppCompatActivity implements View.OnClickListener{
    RequestQueue mRequestQueue;
    ImageView mImageView;
    TextView mTextView;
    WebView mWebView;
    FloatingActionButton star_fab;
    Toolbar mToolbar;

    private String title;
    private String content_id;
    private String image;
    private String shareUrl;

    private StarDBHelper mDBHelper;
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ChangeTheme.setTheme(ContentActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initview();
        setWebView();
        mDBHelper=new StarDBHelper(this,"Star.db",null,1);
        mDatabase=mDBHelper.getWritableDatabase();
        loadcontent();
        checkStar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            onBackPressed();
        }else if (id==R.id.comment){
            Intent intent=new Intent(ContentActivity.this,CommentsActivity.class);
            intent.putExtra("id",content_id);
            startActivity(intent);
        }else if (id==R.id.share){
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            String shareText = title + shareUrl + getString(R.string.share_via);
            shareIntent.putExtra(Intent.EXTRA_TEXT,shareText);
            startActivity(Intent.createChooser(shareIntent,getString(R.string.share_to)));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content,menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        if (!isExist(content_id)){
            star_fab.setImageResource(R.drawable.ic_star_white_48dp);
            ContentValues values=new ContentValues();
            values.put("id",content_id);
            values.put("url",shareUrl);
            values.put("image",image);
            values.put("title",title);
            mDatabase.insert("STAR",null,values);
            values.clear();
        }else{
            star_fab.setImageResource(R.drawable.ic_star_border_white_48dp);
            mDatabase.delete("STAR","id=?",new String[]{content_id});
        }

    }

    private void initview(){
        mImageView= (ImageView) findViewById(R.id.image_content);
        mTextView= (TextView) findViewById(R.id.text_copyright);
        mWebView= (WebView) findViewById(R.id.webview_content);
        mToolbar= (Toolbar) findViewById(R.id.toolbar);
        star_fab= (FloatingActionButton) findViewById(R.id.star_fab);
        star_fab.setOnClickListener(this);
        Intent intent=getIntent();
        content_id=intent.getStringExtra("id");
        image=intent.getStringExtra("image");
        title=intent.getStringExtra("title");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    private void setWebView(){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
    }

    private void loadcontent(){
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.NEWS+content_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.isNull("body")){
                        Glide.with(ContentActivity.this).load(image).centerCrop().into(mImageView);
                        mWebView.loadUrl(response.getString("share_url"));
                    }else {
                        if (!response.isNull("image")){
                            Glide.with(ContentActivity.this).load(response.getString("image")).centerCrop().into(mImageView);
                            mTextView.setText(response.getString("image_source"));
                            shareUrl=response.getString("share_url");
                        }else {
                            Glide.with(ContentActivity.this).load(image).centerCrop().into(mImageView);
                        }
                    }

                    String css = null;
                    if (response.getJSONArray("css").length() != 0){
                        for (int i = 0;i < response.getJSONArray("css").length();i++){
                            css = "<link type=\"text/css\" href=\"" +
                                    response.getJSONArray("css").getString(i) +
                                    "\" " +
                                    "rel=\"stylesheet\" />\n";
                        }
                    }

                    String content = response.getString("body").replace("<div class=\"img-place-holder\">", "");
                    content = content.replace("<div class=\"headline\">", "");
                    String html = "<!DOCTYPE html>\n"
                            + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                            + "<head>\n"
                            + "\t<meta charset=\"utf-8\" />\n</head>\n"
                            + css
                            + content
                            + "\n<body>";
                    mWebView.loadDataWithBaseURL("x-data://base",html,"text/html","utf-8",null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }

    private boolean isExist(String id){
        Cursor cursor=mDatabase.query("STAR",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                String contentid=cursor.getString(cursor.getColumnIndex("id"));
                if (contentid.equals(id))
                    return true;
            }while (cursor.moveToNext());
        }
        return false;
    }

    private void checkStar(){
        if (isExist(content_id)){
            star_fab.setImageResource(R.drawable.ic_star_white_48dp);
        }
    }
}
