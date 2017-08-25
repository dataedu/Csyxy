package com.dk.mp.csyxy.ui;

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

import com.bumptech.glide.Glide;
import com.dk.mp.core.entity.SlideNews;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.entity.News;
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

    public void init(final View view, final Context context, String Mtype,List<SlideNews> slideNewses) {
        this.view = view;
        this.mType = Mtype;
        this.context = context;
        Log.e("---------------------", mType + "");
        mLoopViewPager = (RollPagerView) view.findViewById(R.id.loop_view_pager);
        defultImg = (ImageView) view.findViewById(R.id.loop_defult_img);

        if (slideNewses != null && slideNewses.size() > 0) {
            slideList.addAll(slideNewses);

            mLoopViewPager.setVisibility(View.VISIBLE);
            defultImg.setVisibility(View.GONE);

            mLoopViewPager.setPlayDelay(3000);
            mLoopAdapter = new TestLoopAdapter(mLoopViewPager);
            mLoopViewPager.setAdapter(mLoopAdapter);
            mLoopViewPager.setHintView(new newColorPointHintView(context, Color.GRAY, Color.WHITE));
        } else {
            if (mType.equals("zjcy")) {
                slideList.add(new SlideNews("a", "http://default", "走进长医", "res://com.dk.edu.csyxy/" + R.mipmap.zjcy_a, null));
                slideList.add(new SlideNews("b", "http://default", "走进长医", "res://com.dk.edu.csyxy/" + R.mipmap.zjcy_b, null));
                slideList.add(new SlideNews("c", "http://default", "走进长医", "res://com.dk.edu.csyxy/" + R.mipmap.zjcy_c, null));
                slideList.add(new SlideNews("d", "http://default", "走进长医", "res://com.dk.edu.csyxy/" + R.mipmap.zjcy_d, null));

                mLoopViewPager.setVisibility(View.VISIBLE);
                defultImg.setVisibility(View.GONE);

                mLoopViewPager.setPlayDelay(3000);
                mLoopAdapter = new TestLoopAdapter(mLoopViewPager);
                mLoopViewPager.setAdapter(mLoopAdapter);
                mLoopViewPager.setHintView(new newColorPointHintView(context, Color.GRAY, Color.WHITE));

            } else {
                defultImg.setVisibility(View.VISIBLE);
                mLoopViewPager.setVisibility(View.GONE);
                defultTex = (TextView) view.findViewById(R.id.tip);
                defult();
            }

        }
    }

    private void defult(){
        String text = "";
        int Imgid = 0;

     /*   if (mType.equals("zjcy")){
            text = "走进长医";
            Imgid = R.mipmap.zjcy;
        }else */
          /*  if (mType.equals("xw")){
            text = "新闻";
            Imgid = R.mipmap.xw;
        } else*/
            if (mType.equals("gg")){
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

//        defultImg.setImageResource(Imgid);
        Glide.with(context).load(Imgid).into(defultImg);
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
                    news.setId(slideList.get(position).getId());
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

    /**
     * 更新数据
     * @param slideNewses
     */
    public void updateData(List<SlideNews> slideNewses){
        slideList.clear();
            slideList.addAll(slideNewses);
            mLoopViewPager.setVisibility(View.VISIBLE);
            defultImg.setVisibility(View.GONE);

            mLoopViewPager.setPlayDelay(3000);
            mLoopAdapter = new TestLoopAdapter(mLoopViewPager);
            mLoopViewPager.setAdapter(mLoopAdapter);
            mLoopViewPager.setHintView(new newColorPointHintView(context, Color.GRAY, Color.WHITE));
    }

}
