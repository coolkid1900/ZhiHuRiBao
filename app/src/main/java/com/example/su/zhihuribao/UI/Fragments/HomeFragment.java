package com.example.su.zhihuribao.UI.Fragments;

import android.content.Context;
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
import com.example.su.zhihuribao.Adapters.HomePageAdapter;
import com.example.su.zhihuribao.Model.ImagesNewsItem;
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
 * Created by su on 16/5/5.
 */
public class HomeFragment extends Fragment {

    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    HomePageAdapter mHomePageAdapter;
    RequestQueue mRequestQueue;
    LinearLayoutManager mLinearLayoutManager;
    List<ImagesNewsItem> mItemList = new ArrayList<>();

    private boolean move = false;

    private OnRecyclerViewCreated mOnRecyclerViewCreated;
    //记录顶部显示的项
    private int position = 0;
    //记录顶部项的偏移
    private int scroll = 0;


    @Override
    public void onAttach(Context context) {
        mOnRecyclerViewCreated = (OnRecyclerViewCreated) context;
        super.onAttach(context);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_homepage);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_homepage);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        SwipeRefreshLayout.OnRefreshListener onRefreshListener=new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadhomepage(null);
            }

        };
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//            }
//        });
        onRefreshListener.onRefresh();
        mSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        mSwipeRefreshLayout.setDistanceToTriggerSync(300);
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        return view;
    }

    private void loadhomepage(String data) {
        mItemList.clear();
        String url = null;
        if (data == null) {
            url = Api.LATEST;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (!mItemList.isEmpty()) {
                    mItemList.clear();
                }

                try {
                    if (!response.getString("date").isEmpty()) {
                        JSONArray array = response.getJSONArray("stories");
                        for (int i = 0; i < array.length(); i++) {
                            JSONArray images = array.getJSONObject(i).getJSONArray("images");
                            List<String> imageList = new ArrayList<String>();
                            for (int j = 0; j < images.length(); j++) {
                                String imgUrl = images.getString(j);
                                imageList.add(imgUrl);
                            }

                            ImagesNewsItem item = new ImagesNewsItem(
                                    array.getJSONObject(i).getString("title"),
                                    imageList,
                                    array.getJSONObject(i).getString("type"),
                                    array.getJSONObject(i).getString("id"));
                            mItemList.add(item);
                        }
                    }

                    if (mSwipeRefreshLayout.isRefreshing()) {
                        Snackbar.make(mSwipeRefreshLayout, R.string.refresh_finish, Snackbar.LENGTH_SHORT).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    mHomePageAdapter = new HomePageAdapter(getActivity(), mItemList);
                    mRecyclerView.setAdapter(mHomePageAdapter);
                    mHomePageAdapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(getActivity(), ContentActivity.class);
                            intent.putExtra("id", mItemList.get(position).getId());
                            intent.putExtra("image", mItemList.get(position).getFirstImage());
                            intent.putExtra("title", mItemList.get(position).getTitle());
                            startActivity(intent);
                        }
                    });

                    mRecyclerView.addOnScrollListener(new RecyclerViewListener());

                    position = getArguments().getInt("position");
                    scroll = getArguments().getInt("scroll");
                    move();

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

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    public interface OnRecyclerViewCreated{
        void recyclerViewCreated();
    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (move){
                move = false;
                int n = position - mLinearLayoutManager.findFirstVisibleItemPosition();
                if ( 0 <= n && n < mRecyclerView.getChildCount()){
                    int top = mRecyclerView.getChildAt(n).getTop() - scroll;
                    mRecyclerView.smoothScrollBy(0, top);
                }
                mOnRecyclerViewCreated.recyclerViewCreated();

            }
        }
    }

    private void move(){
        if (position<0 || position>=mRecyclerView.getAdapter().getItemCount() ) {
            return;
        }
        int firstItem = mLinearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = mLinearLayoutManager.findLastVisibleItemPosition();
        if (position <= firstItem ){
            mRecyclerView.scrollToPosition(position);
            move = true;
        }else if ( position <= lastItem ){
            int top = mRecyclerView.getChildAt(position - firstItem).getTop() - scroll;
            mRecyclerView.scrollBy(0, top);
            mOnRecyclerViewCreated.recyclerViewCreated();
        }else{
            mRecyclerView.scrollToPosition(position);
            move = true;
        }
    }
}
