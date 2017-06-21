package com.dk.edu.csyxy.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.edu.core.entity.SlideNews;
import com.dk.edu.core.http.HttpUtil;
import com.dk.edu.core.http.request.HttpListener;
import com.dk.edu.core.util.BroadcastUtil;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.entity.News;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2017/6/13 16:51
 */
public class HeaderView {
    private RollPagerView mLoopViewPager;
    private TestLoopAdapter mLoopAdapter;
    private List<SlideNews> slideList = new ArrayList<>();
    private Context context;
    private View view;
    private Gson gson = new Gson();

    public void init(final View view, final Context context){
        this.view = view;
        slideList.add(new SlideNews("1", "http://default", "移动校园,老师学生的好帮手", "res://com.dk.edu.csyxy/" + R.mipmap.banner_def, null));
        mLoopViewPager = (RollPagerView) view.findViewById(R.id.loop_view_pager);
        mLoopViewPager.setPlayDelay(3000);
        mLoopAdapter = new TestLoopAdapter(mLoopViewPager);
        mLoopViewPager.setAdapter(mLoopAdapter);
        mLoopViewPager.setHintView(new newColorPointHintView(context, Color.GRAY, Color.WHITE));
        this.context = context;
        getData();
        //注册网络状态监听
        BroadcastUtil.registerReceiver(context, mRefreshBroadcastReceiver, new String[]{"ref_headerview"});
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("ref_headerview")) {
                getData();
            }
        }
    };

    private class TestLoopAdapter extends LoopPagerAdapter {
        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }
        @Override
        public View getView(ViewGroup container, final int position) {
            SimpleDraweeView view = new SimpleDraweeView(container.getContext());
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setImageURI(Uri.parse(slideList.get(position).getImage()));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,NewsDetailActivity.class);
                    News news = new News(1);
                    news.setImage(slideList.get(position).getImage());
                    news.setUrl(slideList.get(position).getUrl());
                    intent.putExtra("news", (Serializable)news);
                    ViewCompat.setTransitionName(v, "detail_element");
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,v,
                                    context.getString(R.string.transition__img));
                    ActivityCompat.startActivity((Activity) context,intent,options.toBundle());
                    ((Activity) context).overridePendingTransition(R.anim.slide_up, R.anim.scale_down);
                }
            });

            GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(context.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(300)
                    .setPlaceholderImage(R.color.bg)
                    .setFailureImage(R.mipmap.banner_def)
                    .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build();
            view.setHierarchy(hierarchy);
            return view;
        }
        @Override
        public int getRealCount() {
            return slideList.size();
        }
    }

    private class newColorPointHintView extends ColorPointHintView {
        public newColorPointHintView(Context context, int focusColor, int normalColor) {
            super(context, focusColor, normalColor);
        }

        @Override
        public void setCurrent(int current) {
            super.setCurrent(current);
            TextView textView = (TextView) view.findViewById(R.id.tip);
            textView.setText(slideList.get(current).getName());
        }
    }


    public void getData(){
        HttpUtil.getInstance().postJsonObjectRequest("http://ydoa.czlgj.com:9064/ydxy/apps/xxxw/slide", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optInt("code") == 200){//成功返回数据
                    try {
                        List<SlideNews> list = gson.fromJson(result.getJSONArray("data").toString(),new TypeToken<List<SlideNews>>(){}.getType());
                        if (list.size() != 0) {
                            slideList.clear();
                        }
                        if(list.size()>4) {
                            slideList.addAll(list.subList(0, 4));
                        }
                        mLoopAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
            }
        });
    }
}
