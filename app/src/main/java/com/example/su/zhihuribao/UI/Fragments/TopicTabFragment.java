package com.example.su.zhihuribao.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.su.zhihuribao.Adapters.TopicsTabAdapter;
import com.example.su.zhihuribao.Model.TopicsItem;
import com.example.su.zhihuribao.R;
import com.example.su.zhihuribao.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 16/5/7.
 */
public class TopicTabFragment extends Fragment{

    TabLayout mTabLayout;
    ViewPager mViewPager;
    RequestQueue  mRequestQueue;
    List<TopicsItem> mItemsList=new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_topics_tab,container,false);
        mTabLayout= (TabLayout) view.findViewById(R.id.tablayout_topic);
        mViewPager= (ViewPager) view.findViewById(R.id.viewpager_topic);
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.TOPICS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray=response.getJSONArray("others");
                    for (int i=0;i<jsonArray.length();i++){
                        String thumbnail=jsonArray.getJSONObject(i).getString("thumbnail");
                        String description=jsonArray.getJSONObject(i).getString("description");
                        String id=jsonArray.getJSONObject(i).getString("id");
                        String name=jsonArray.getJSONObject(i).getString("name");
                        TopicsItem topicsItem =new TopicsItem(id,name,thumbnail,description);
                        mItemsList.add(topicsItem);
                    }
                    TopicsTabAdapter topicsTabAdapter =new TopicsTabAdapter(getChildFragmentManager(),getActivity(),mItemsList);
                    mViewPager.setAdapter(topicsTabAdapter);
                    mTabLayout.setupWithViewPager(mViewPager);
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
        return view;
    }

}
