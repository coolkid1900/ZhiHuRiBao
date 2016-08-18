package com.example.su.zhihuribao.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.su.zhihuribao.Adapters.CommonNewsAdapter;
import com.example.su.zhihuribao.Model.CommonNewsItem;
import com.example.su.zhihuribao.Interfaces.OnItemClickListener;
import com.example.su.zhihuribao.R;
import com.example.su.zhihuribao.UI.Activities.ContentActivity;
import com.example.su.zhihuribao.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 16/5/10.
 */
public class HotnewsFragment extends Fragment{
    RequestQueue mRequestQueue;
    RecyclerView mRecyclerView;
    List<CommonNewsItem> mItemList=new ArrayList<>();
    CommonNewsAdapter mCommonNewsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_hotnews,container,false);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recyclerview_hotnews);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.HOT, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (!response.isNull("recent")){
                    try {
                        JSONArray jsonArray=response.getJSONArray("recent");
                        for (int i=0;i<jsonArray.length();i++){
                            CommonNewsItem commonNewsItem =new CommonNewsItem(jsonArray.getJSONObject(i).getString("news_id"),
                                    jsonArray.getJSONObject(i).getString("url"),
                                    jsonArray.getJSONObject(i).getString("thumbnail"),
                                    jsonArray.getJSONObject(i).getString("title"));
                            mItemList.add(commonNewsItem);
                        }
                        mCommonNewsAdapter =new CommonNewsAdapter(getActivity(),mItemList);
                        mRecyclerView.setAdapter(mCommonNewsAdapter);
                        mCommonNewsAdapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(),ContentActivity.class);
                                intent.putExtra("id",mItemList.get(position).getId());
                                intent.putExtra("title",mItemList.get(position).getTitle());
                                intent.putExtra("image",mItemList.get(position).getThumbnail());
                                startActivity(intent);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
