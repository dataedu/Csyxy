package com.dk.edu.csyxy.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.edu.core.entity.SlideNews;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.entity.News;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：janabo on 2017/6/13 16:51
 */
public class HeaderView {

    private RollPagerView mLoopViewPager;
    private ImageView defultImg;
    private TextView defultTex;

    private TestLoopAdapter mLoopAdapter;
    private List<SlideNews> slideList = new ArrayList<>();
    private Context context;
    private View view;
    private String mType;

    public void init(final View view, final Context context, String Mtype,List<SlideNews> slideNewses){
        this.view = view;
        this.mType = Mtype;
        this.context = context;
        Log.e("---------------------",mType+"");
        mLoopViewPager = (RollPagerView) view.findViewById(R.id.loop_view_pager);
        defultImg = (ImageView) view.findViewById(R.id.loop_defult_img);

        if(slideNewses !=null && slideNewses.size()>0){
            slideList.addAll(slideNewses);

            mLoopViewPager.setVisibility(View.VISIBLE);
            defultImg.setVisibility(View.GONE);

            mLoopViewPager.setPlayDelay(3000);
            mLoopAdapter = new TestLoopAdapter(mLoopViewPager);
            mLoopViewPager.setAdapter(mLoopAdapter);
            mLoopViewPager.setHintView(new newColorPointHintView(context, Color.GRAY, Color.WHITE));
        }else{
//            if (mType.equals("zjcy")){
//                slideList.add(new SlideNews("1", "http://default", "走进长医", "res://com.dk.edu.csyxy/" + R.mipmap.zjcy, null));
//            }else if (mType.equals("xw")){
//                slideList.add(new SlideNews("1", "http://default", "新闻", "res://com.dk.edu.csyxy/" + R.mipmap.xw, null));
//            } else if (mType.equals("gg")){
//                slideList.add(new SlideNews("1", "http://default", "公告", "res://com.dk.edu.csyxy/" + R.mipmap.gg, null));
//            } else if (mType.equals("rcyj")){
//                slideList.add(new SlideNews("1", "http://default", "人才引进", "res://com.dk.edu.csyxy/" + R.mipmap.rcyj, null));
//            } else if (mType.equals("ybxx")){
//                slideList.add(new SlideNews("1", "http://default", "邀标信息", "res://com.dk.edu.csyxy/" + R.mipmap.ybxx, null));
//            }else if (mType.equals("dzjs")){
//                slideList.add(new SlideNews("1", "http://default", "党政建设", "res://com.dk.edu.csyxy/" + R.mipmap.dzjs, null));
//            }

            defultImg.setVisibility(View.VISIBLE);
            mLoopViewPager.setVisibility(View.GONE);
            defultTex = (TextView) view.findViewById(R.id.tip);
            defult();
        }

  //      getData();
        //注册网络状态监听
 //       BroadcastUtil.registerReceiver(context, mRefreshBroadcastReceiver, new String[]{"ref_headerview"});
    }

//    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals("ref_headerview")) {
//                getData();
//            }
//        }
//    };

    private void defult(){
        String text = "";
        int Imgid = 0;

        if (mType.equals("zjcy")){
            text = "走进长医";
            Imgid = R.mipmap.zjcy;
        }else if (mType.equals("xw")){
            text = "新闻";
            Imgid = R.mipmap.xw;
        } else if (mType.equals("gg")){
            text = "公告";
            Imgid = R.mipmap.gg;
        } else if (mType.equals("rcyj")){
            text = "人才引进";
            Imgid = R.mipmap.rcyj;
        } else if (mType.equals("ybxx")){
            text = "邀标信息";
            Imgid = R.mipmap.ybxx;
        }else if (mType.equals("dzjs")){
            text = "党政建设";
            Imgid = R.mipmap.dzjs;
        }

        defultImg.setImageResource(Imgid);
        defultTex.setText(text);
        defultTex.getBackground().setAlpha(110);

        final String finalText = text;
        defultImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NewsDetailActivity.class);
                News news = new News(1);
                intent.putExtra("mType",mType);
                news.setImage(String.valueOf(defultImg));
                news.setUrl(null);
                news.setName(finalText);
                intent.putExtra("news", (Serializable)news);
                ViewCompat.setTransitionName(v, "detail_element");
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,v,
                                context.getString(R.string.transition__img));
                ActivityCompat.startActivity((Activity) context,intent,options.toBundle());
                ((Activity) context).overridePendingTransition(R.anim.slide_up, R.anim.scale_down);
            }
        });
    }

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
                    if (slideList.get(position).getId().equals("1")){
                        intent.putExtra("mType",mType);
                    }
                    news.setImage(slideList.get(position).getImage());
                    news.setUrl(slideList.get(position).getUrl());
                    news.setName(slideList.get(position).getName());
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
            textView.getBackground().setAlpha(110);
        }
    }


//    public void getData(){
//        Map<String, Object> map = new HashMap<>();
//        map.put("type",mType);
//        map.put("pageNo",1);
//
//        Log.e("===============",mType);
//
//        HttpUtil.getInstance().postJsonObjectRequest("apps/tabs/news", map, new HttpListener<JSONObject>() {
//            @Override
//            public void onSuccess(JSONObject result) {
//                try {
//                    if (result.getInt("code") == 200){
//                        JSONObject jo = result.getJSONObject("data");
//                        List<SlideNews> list = gson.fromJson(jo.getJSONArray("slide").toString(),new TypeToken<List<SlideNews>>(){}.getType());
//                        if (list.size() != 0) {
//                            slideList.clear();
//                        }
//                        if(list.size()>4) {
//                            slideList.addAll(list.subList(0, 4));
//                        }
//                        mLoopAdapter.notifyDataSetChanged();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(VolleyError error) {
//                error.printStackTrace();
//            }
//        });
//
//    }

    /**
     * 更新数据
     * @param slideNewses
     */
    public void updateData(List<SlideNews> slideNewses){
        slideList.clear();
//        if(slideNewses!= null && slideNewses.size()>0){
            slideList.addAll(slideNewses);
//        }else{
//            if (mType.equals("zjcy")){
//                slideList.add(new SlideNews("1", "http://default", "走进长医", "res://com.dk.edu.csyxy/" + R.mipmap.zjcy, null));
//            }else if (mType.equals("xw")){
//                slideList.add(new SlideNews("1", "http://default", "新闻", "res://com.dk.edu.csyxy/" + R.mipmap.xw, null));
//            } else if (mType.equals("gg")){
//                slideList.add(new SlideNews("1", "http://default", "公告", "res://com.dk.edu.csyxy/" + R.mipmap.gg, null));
//            } else if (mType.equals("rcyj")){
//                slideList.add(new SlideNews("1", "http://default", "人才引进", "res://com.dk.edu.csyxy/" + R.mipmap.rcyj, null));
//            } else if (mType.equals("ybxx")){
//                slideList.add(new SlideNews("1", "http://default", "邀标信息", "res://com.dk.edu.csyxy/" + R.mipmap.ybxx, null));
//            }else if (mType.equals("dzjs")){
//                slideList.add(new SlideNews("1", "http://default", "党政建设", "res://com.dk.edu.csyxy/" + R.mipmap.dzjs, null));
//            }

//        mLoopAdapter.notifyDataSetChanged();
            mLoopViewPager.setVisibility(View.VISIBLE);
            defultImg.setVisibility(View.GONE);

            mLoopViewPager.setPlayDelay(3000);
            mLoopAdapter = new TestLoopAdapter(mLoopViewPager);
            mLoopViewPager.setAdapter(mLoopAdapter);
            mLoopViewPager.setHintView(new newColorPointHintView(context, Color.GRAY, Color.WHITE));
//        }
    }

}
