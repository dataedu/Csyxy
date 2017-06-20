package com.dk.edu.csyxy.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.VolleyError;
import com.dk.edu.core.http.HttpUtil;
import com.dk.edu.core.http.request.HttpListener;
import com.dk.edu.core.ui.BaseFragment;
import com.dk.edu.core.util.BroadcastUtil;
import com.dk.edu.core.view.RecycleViewDivider;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.adapter.NewsAdapter;
import com.dk.edu.csyxy.entity.News;
import com.dk.edu.csyxy.entity.PageMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻模块
 * 作者：janabo on 2017/6/13 16:39
 */
public class NewsFragment extends BaseFragment{
    SwipeRefreshLayout mRefresh;
    RecyclerView mRecyclerView;
    NewsAdapter nAdapter;
    List<News> news = new ArrayList<>();
    boolean isLoading;
    int pageNo = 1;
    int totalPages = 1;
    private String mType;
    boolean isRefreshing = false;

    @Override
    protected int getLayoutId() {
        return R.layout.mp_news;
    }

    public static NewsFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("mType",type);
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onFirstUserVisible() {
        super.onFirstUserVisible();
        initMyData();
    }

    protected void initMyData() {
        mRefresh = findView(R.id.swipe_refresh);
        mRecyclerView = findView(R.id.rv_listview);
        news.add(new News(1));
        nAdapter = new NewsAdapter(getContext(),news);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线
        mRecyclerView.setAdapter(nAdapter);
        mType = getArguments().getString("mType");
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                BroadcastUtil.sendBroadcast(getContext(),"ref_headerview");
                news.clear();
                news.add(new News(1));
                getList();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == nAdapter.getItemCount()) {
                    Log.d("test", "loading executed");
                    isRefreshing = mRefresh.isRefreshing();
                    if (isRefreshing) {
//                        nAdapter.notifyItemRemoved(nAdapter.getItemCount());
                        return;
                    }
                    if(totalPages<=pageNo){
//                        nAdapter.notifyItemRemoved(nAdapter.getItemCount());
                        return;
                    }
                    if (!isLoading) {
                        isLoading = true;
                        pageNo++;
                        loadMore();
                        Log.d("test", "load more completed");
                        isLoading = false;
                    }
                }
            }
        });

        //recycleview刷新时clear后加载新的item会报错
//        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (isRefreshing) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

        mRefresh.setRefreshing(true);
        getList();
    }

    public void getList(){
        HttpUtil.getInstance().postJsonObjectRequest(mType + "pageNo=" + pageNo, null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if(result.getInt("code") == 200){
                        JSONObject jo = result.getJSONObject("data");
                        PageMsg<News> pageMsg = new Gson().fromJson(jo.getJSONObject("news").toString(),new TypeToken<PageMsg<News>>(){}.getType());
                        totalPages = pageMsg.getTotalPages();
                        if(pageMsg.getList() != null && pageMsg.getList().size()>0) {
                            news.addAll(pageMsg.getList());
                            nAdapter.notifyDataSetChanged();
                            //停止刷新
                            mRefresh.setRefreshing(false);
                            //RecyclerView滑动到第一个
                            mRecyclerView.scrollToPosition(0);
                        }else{
                            mRefresh.setRefreshing(false);
                        }
                    }else{
                        mRefresh.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onError(VolleyError error) {
                nAdapter.notifyDataSetChanged();
                mRefresh.setRefreshing(false);
            }
        });
    }

    public void loadMore(){
        HttpUtil.getInstance().gsonRequest(new TypeToken<PageMsg<News>>(){}, "http://ydoa.czlgj.com:9064/ydxy/apps/xxxw/list?pageNo="+pageNo, null, new HttpListener<PageMsg<News>>() {
            @Override
            public void onSuccess(PageMsg<News> result) {
                totalPages = result.getTotalPages();
                if(result.getList() != null && result.getList().size()>0) {
                    news.addAll(result.getList());
                    nAdapter.notifyDataSetChanged();
                    nAdapter.notifyItemRemoved(nAdapter.getItemCount());
                }else{
                    nAdapter.notifyItemRemoved(nAdapter.getItemCount());
                }
            }
            @Override
            public void onError(VolleyError error) {
                nAdapter.notifyItemRemoved(nAdapter.getItemCount());
            }
        });
    }

}
