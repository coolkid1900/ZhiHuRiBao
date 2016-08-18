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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.example.su.zhihuribao.Adapters.TopicsListAdapter;
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
 * Created by su on 16/5/7.
 */
public class TopicViewPagerFragment extends Fragment{

    public static final String ARG_PAGE_TOPICS = "PAGE_TOPICS";

    ImageView mImageView;
    TextView mTextView;
    RecyclerViewHeader mRecyclerViewHeader;
    RecyclerView mRecyclerView;
    RequestQueue mRequestQueue;
    List<String> idlist=new ArrayList<>();
    List<ImagesNewsItem> mItemList=new ArrayList<>();
    TopicsListAdapter mTopicsListAdapter;

    private int mPage;

    public static TopicViewPagerFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_TOPICS, page);
        TopicViewPagerFragment topicViewPagerFragment = new TopicViewPagerFragment();
        topicViewPagerFragment.setArguments(args);
        return topicViewPagerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE_TOPICS);
        mRequestQueue= Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_topics_content,container,false);
        mImageView= (ImageView) view.findViewById(R.id.image_viewpager);
        mTextView= (TextView) view.findViewById(R.id.text_viewpager);
        mRecyclerViewHeader= (RecyclerViewHeader) view.findViewById(R.id.header);
        mRecyclerView= (RecyclerView) view.findViewById(R.id.recyclerview_viewpager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewHeader.attachTo(mRecyclerView);
        JsonObjectRequest topicsRequest=new JsonObjectRequest(Request.Method.GET, Api.TOPICS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final JSONArray jsonArray=response.getJSONArray("others");
                    for (int i=0;i<jsonArray.length();i++){
                        String id=jsonArray.getJSONObject(i).getString("id");
                        idlist.add(id);
                    }
                    JsonObjectRequest topicRequest=new JsonObjectRequest(Request.Method.GET, Api.TOPIC + idlist.get(mPage), null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Glide.with(getActivity()).load(response.getString("image")).centerCrop().into(mImageView);
                                mTextView.setText(response.getString("description"));
                                JSONArray stories=response.getJSONArray("stories");
                                for (int i=0;i<stories.length();i++){
                                    List<String> imageList=new ArrayList<>();
                                    if (stories.getJSONObject(i).isNull("images")){
                                        imageList=null;
                                    }else{
                                        JSONArray images=stories.getJSONObject(i).getJSONArray("images");
                                        for (int j = 0; j < images.length(); j++){
                                            String imgUrl = images.getString(j);
                                            imageList.add(imgUrl);
                                        }
                                    }
                                    ImagesNewsItem imagesNewsItem =new ImagesNewsItem(stories.getJSONObject(i).getString("title"),
                                            imageList,
                                            stories.getJSONObject(i).getString("type"),
                                            stories.getJSONObject(i).getString("id"));
                                    mItemList.add(imagesNewsItem);
                                }
                                mTopicsListAdapter=new TopicsListAdapter(getActivity(),mItemList);
                                mRecyclerView.setAdapter(mTopicsListAdapter);
                                mTopicsListAdapter.setOnItemClickListener(new OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Intent intent = new Intent(getActivity(), ContentActivity.class);
                                        intent.putExtra("id",mItemList.get(position).getId());
                                        intent.putExtra("title",mItemList.get(position).getTitle());
                                        intent.putExtra("image",mItemList.get(position).getFirstImage());
                                        startActivity(intent);
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    mRequestQueue.add(topicRequest);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mRequestQueue.add(topicsRequest);
        return view;

    }


}
