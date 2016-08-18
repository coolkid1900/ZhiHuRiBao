package com.example.su.zhihuribao.UI.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.example.su.zhihuribao.Adapters.CommentAdapter;
import com.example.su.zhihuribao.Model.CommentsItem;
import com.example.su.zhihuribao.R;
import com.example.su.zhihuribao.UI.Custom.DividerItemDecoration;
import com.example.su.zhihuribao.Utils.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 16/5/10.
 */
public class CommentsFragment extends Fragment {

    public static final String ARG_PAGE_COMMENTS = "PAGE_COMMENTS";
    public static final String SHORT_COMMENT="/short-comments";
    public static final String LONG_COMMENT="/long-comments";
    private int mPage;
    private String contentid;

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    RequestQueue mRequestQueue;
    List<CommentsItem> mItemList=new ArrayList<>();

    public static CommentsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_COMMENTS, page);
        CommentsFragment commentsFragment=new CommentsFragment();
        commentsFragment.setArguments(args);
        return commentsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
        mPage = getArguments().getInt(ARG_PAGE_COMMENTS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_comments,container,false);
        mSwipeRefreshLayout= (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_comments);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recyclerview_comments);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL_LIST));
        Intent intent=getActivity().getIntent();
        contentid=intent.getStringExtra("id");
        SwipeRefreshLayout.OnRefreshListener onRefreshListener=new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadcomments();
            }
        };
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        onRefreshListener.onRefresh();
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        mSwipeRefreshLayout.setDistanceToTriggerSync(300);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        return view;
    }


    private void loadcomments(){
        mItemList.clear();
        if (mPage==0){
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.COMMENTS + contentid + SHORT_COMMENT, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(!response.isNull("comments")){
                        try {
                            JSONArray jsonArray=response.getJSONArray("comments");
                            for (int i=0;i<jsonArray.length();i++){
                                CommentsItem commentsItem=new CommentsItem(jsonArray.getJSONObject(i).getString("avatar"),
                                        jsonArray.getJSONObject(i).getString("author"),
                                        jsonArray.getJSONObject(i).getString("content"));
                                mItemList.add(commentsItem);
                            }

                            if (mSwipeRefreshLayout.isRefreshing()) {
                                Snackbar.make(mSwipeRefreshLayout, R.string.refresh_finish, Snackbar.LENGTH_SHORT).show();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                            CommentAdapter commentAdapter=new CommentAdapter(getActivity(),mItemList);
                            mRecyclerView.setAdapter(commentAdapter);

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
        }else if (mPage==1){
            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, Api.COMMENTS + contentid + LONG_COMMENT, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if(!response.isNull("comments")){
                        try {
                            JSONArray jsonArray=response.getJSONArray("comments");
                            for (int i=0;i<jsonArray.length();i++){
                                CommentsItem commentsItem=new CommentsItem(jsonArray.getJSONObject(i).getString("avatar"),
                                        jsonArray.getJSONObject(i).getString("author"),
                                        jsonArray.getJSONObject(i).getString("content"));
                                mItemList.add(commentsItem);
                            }

                            if (mSwipeRefreshLayout.isRefreshing()) {
                                Snackbar.make(mSwipeRefreshLayout, R.string.refresh_finish, Snackbar.LENGTH_SHORT).show();
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                            CommentAdapter commentAdapter=new CommentAdapter(getActivity(),mItemList);
                            mRecyclerView.setAdapter(commentAdapter);

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
        }


    }
}
