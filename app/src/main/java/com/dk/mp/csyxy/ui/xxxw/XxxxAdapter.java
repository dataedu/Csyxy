package com.dk.mp.csyxy.ui.xxxw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.entity.News;
import com.dk.mp.csyxy.ui.NewsDetailActivity;

import java.io.Serializable;
import java.util.List;


/**
 * Created by cobb on 2017/8/24.
 */

public class XxxxAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<News> list;

    public XxxxAdapter(Context context, List<News> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position+1)%4 == 1){
            return 0;
        }else {
            return 1;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_xxxw_two, parent, false);
            return new MyViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_xxxw_one, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final News news = list.get(position);
        final MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.title.setText(news.getName());
        viewHolder.time.setText(news.getPublishTime());

        if ((position+1)%4 == 1){
            if (StringUtils.isNotEmpty(news.getImage())){
                Glide.with(mContext).load(news.getImage()).fitCenter().into(viewHolder.image);
            }else {
                Glide.with(mContext).load(R.mipmap.zjcy_a).into(viewHolder.image);
            }

        }else {
            Glide.with(mContext).load(news.getImage()).fitCenter().into(viewHolder.image);
        }

        viewHolder.newsdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("news", (Serializable) news);

                if ((position+1)%4 == 1){
                    if (!StringUtils.isNotEmpty(news.getImage())){
                        intent.putExtra("mType","xw");
                    }
                }

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,v,
                                mContext.getString(R.string.transition__img));
                ActivityCompat.startActivity((Activity) mContext,intent,options.toBundle());
                ((Activity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.scale_down);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView title,time;
        private LinearLayout newsdetail;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.news_img);// 取得实例
            title = (TextView) itemView.findViewById(R.id.news_title);// 取得实例
            time = (TextView) itemView.findViewById(R.id.news_time);// 取得实例
            newsdetail = (LinearLayout) itemView.findViewById(R.id.newsdetail);
        }
    }
}
