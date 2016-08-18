package com.example.su.zhihuribao.UI.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.su.zhihuribao.R;
import com.example.su.zhihuribao.Utils.Api;
import com.example.su.zhihuribao.Utils.NetworkState;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by su on 16/5/5.
 */
public class WelcomeActivity extends AppCompatActivity{
    ImageView image_welcome;
    TextView text_welcome;
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        image_welcome= (ImageView) findViewById(R.id.image_welcome);
        text_welcome= (TextView) findViewById(R.id.text_welcome);
        mRequestQueue= Volley.newRequestQueue(getApplicationContext());
        if (NetworkState.networkConneted(getApplicationContext())){
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.START_IMAGE, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(final JSONObject response) {
                    try{
                        if (response.getString("img").isEmpty()||response.isNull("img")){
                            image_welcome.setImageResource(R.drawable.welcome);
                            text_welcome.setText(R.string.text_splash);
                        }else{
                            Glide.with(WelcomeActivity.this).load(response.getString("img")).into(image_welcome);
                            text_welcome.setText(response.getString("text"));
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            mRequestQueue.add(jsonObjectRequest);
        }else {
            Glide.with(WelcomeActivity.this).load(R.drawable.welcome).centerCrop().into(image_welcome);
            text_welcome.setText(R.string.text_splash);
        }


        final Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
            }
        };
        timer.schedule(timerTask, 1000 * 2);
    }

    private void downloadimgage(final JSONObject response){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL(response.getString("img"));
                    InputStream inputStream=url.openStream();
                    OutputStream outputStream=openFileOutput("welcom.jpg",MODE_PRIVATE);
                    byte[] buff=new byte[1024];
                    int hasRead=0;
                    while((hasRead=inputStream.read(buff))>0){
                        outputStream.write(buff,0,hasRead);
                    }
                    inputStream.close();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
