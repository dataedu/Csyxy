package com.dk.mp.csyxy.ui.xxxw;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.SlideNews;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.view.RecycleViewDivider;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.adapter.NewsAdapter;
import com.dk.mp.csyxy.entity.News;
import com.dk.mp.csyxy.entity.PageMsg;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cobb on 2017/8/24.
 */

public class XxxwMainFragment extends BaseFragment{

    private ErrorLayout errorLayout;

    SwipeRefreshLayout mRefresh;
    RecyclerView mRecyclerView;
    XxxxAdapter nAdapter;
    List<News> news = new ArrayList<>();

    boolean isLoading;
    int pageNo = 1;
    int totalPages = 1;
    boolean isRefreshing = false;
    Gson gson = new Gson();

    private boolean nodata = false;

    @Override
    protected int getLayoutId() {
        return R.layout.mp_news;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        errorLayout = findView(R.id.error_layout);
        mRefresh = findView(R.id.swipe_refresh);
        mRecyclerView = findView(R.id.rv_listview);

        nAdapter = new XxxxAdapter(getContext(),news);
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(), GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201)));//添加分割线
        mRecyclerView.setAdapter(nAdapter);

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DeviceUtil.checkNet()){
                    pageNo = 1;
                    news.clear();
                    getList();
                }else {
                    mRefresh.setRefreshing(false);
                    return;
                }
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
                    if (isRefreshing == false){
                        isRefreshing = mRefresh.isRefreshing();
                        if (isRefreshing) {
                            return;
                        }
                        if(totalPages<=pageNo){
                            return;
                        }
                        if (!isLoading && DeviceUtil.checkNet()) {
                            isLoading = true;
                            pageNo++;
                            getList();
                            isLoading = false;
                        }
                    }

                }
            }
        });

        mRefresh.setRefreshing(true);
        getList();

//        mRecyclerView clean后刷新bug
        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                                             @Override
                                             public boolean onTouch(View v, MotionEvent event) {
                                                 if (isRefreshing) {
                                                     return true;
                                                 } else {
                                                     return false;
                                                 }
                                             }
                                         }
        );

        BroadcastUtil.registerReceiver(getContext(), mRefreshBroadcastReceiver, new String[]{"checknetwork_true","checknetwork_false"});
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("NewApi") @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("checknetwork_false")){
                if (nodata){
                    news.clear();
                }
            }
        }
    };

    public void getList(){
        nodata = false;
        isRefreshing = true;
        errorLayout.setErrorType(ErrorLayout.LOADDATA);

        if (DeviceUtil.checkNet()){
            Map<String, Object> map = new HashMap<>();
            HttpUtil.getInstance().postJsonObjectRequest("apps/tabs/news?type=xw&pageNo="+pageNo, map, new HttpListener<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        if(result.getInt("code") == 200){
                            JSONObject jo = result.getJSONObject("data");

                            //列表新闻
                            PageMsg<News> pageMsg = gson.fromJson(jo.getJSONObject("news").toString(),new TypeToken<PageMsg<News>>(){}.getType());
                            totalPages = pageMsg.getTotalPages();

                            if(pageMsg.getList() != null && pageMsg.getList().size()>0) {

                                errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
                                news.addAll(pageMsg.getList());
                                nAdapter.notifyDataSetChanged();
                                //停止刷新
                                mRefresh.setRefreshing(false);
                                isRefreshing = false;
                            }else{
                                errorLayout.setErrorType(ErrorLayout.NODATA);
                                mRefresh.setRefreshing(false);
                                isRefreshing = false;
                                nAdapter.notifyDataSetChanged();
                                nodata = true;
                            }
                        }else{
                            errorLayout.setErrorType(ErrorLayout.DATAFAIL);
                            mRefresh.setRefreshing(false);
                            isRefreshing = false;
                            nAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        errorLayout.setErrorType(ErrorLayout.DATAFAIL);
                        mRefresh.setRefreshing(false);
                        isRefreshing = false;
                        nAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    errorLayout.setErrorType(ErrorLayout.DATAFAIL);
                    nAdapter.notifyDataSetChanged();
                    mRefresh.setRefreshing(false);
                }
            });
        }else {
            errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
            nAdapter.notifyDataSetChanged();
            mRefresh.setRefreshing(false);
        }
    }
}

