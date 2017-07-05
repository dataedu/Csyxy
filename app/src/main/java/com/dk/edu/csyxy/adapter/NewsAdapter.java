package com.dk.edu.csyxy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dk.edu.core.entity.SlideNews;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.entity.News;
import com.dk.edu.csyxy.ui.HeaderView;
import com.dk.edu.csyxy.ui.NewsDetailActivity;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：janabo on 2017/6/13 17:04
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{
    Context mContext;
    List<News> mData;
    List<SlideNews> slideNewses;
    LayoutInflater mInflater;
    HeaderView headerView;

    private String mType;

    public NewsAdapter(Context context, List<News> news, String mType,List<SlideNews> slideNewses) {
        this.mContext = context;
        this.mData = news;
        this.mType = mType;
        this.slideNewses = slideNewses;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == 0){
            v = mInflater.inflate(R.layout.mp_news_item,parent,false);
        } else if(viewType ==2){
            v = mInflater.inflate(R.layout.news_nodata,parent,false);
        } else if(viewType ==3){
            v = mInflater.inflate(R.layout.news_errorserver,parent,false);
        } else if(viewType ==4){
            v = mInflater.inflate(R.layout.news_nonet,parent,false);
        } else{
            v = mInflater.inflate(R.layout.core_headview,parent,false);
            headerView = new HeaderView();
            headerView.init(v,mContext,mType,slideNewses);

        }

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(getItemViewType(position) == 0){
            final News news = mData.get(position);
            if (mType.equals("gg")){
                holder.title.setText(news.getName());
                holder.image.setVisibility(View.GONE);
                holder.time2.setVisibility(View.VISIBLE);
                holder.time.setVisibility(View.GONE);
                holder.time2.setText(news.getPublishTime());
            }else {
                holder.title.setText(news.getName());
                holder.time.setText(news.getPublishTime());
                if (news.getImage() == null ){
                    //         holder.image.setVisibility(View.GONE);
                } else {
                    holder.image.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(news.getImage()).fitCenter().into(holder.image);
                }
            }

            holder.newsdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewCompat.setTransitionName(v, "detail_element");

                    Intent intent = new Intent(mContext, NewsDetailActivity.class);
                    intent.putExtra("news", (Serializable) news);
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext,v,
                                    mContext.getString(R.string.transition__img));
                    ActivityCompat.startActivity((Activity) mContext,intent,options.toBundle());
                    ((Activity) mContext).overridePendingTransition(R.anim.slide_up, R.anim.scale_down);
                }
            });
        }else if(getItemViewType(position) == 1){
            if(headerView != null && slideNewses != null && slideNewses.size()>0){
                headerView.updateData(slideNewses);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (position + 1 == getItemCount()) {
//            return -1;
//        } else {
            return mData.get(position).getmType();
//        }
    }

    @Override
    public int getItemCount() {
//        return mData.size()==0?0:mData.size()+1;
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title,time, time2;

        private LinearLayout newsdetail;

        public MyViewHolder(View v) {
            super(v);
            image = (ImageView) itemView.findViewById(R.id.news_img);// 取得实例
            title = (TextView) itemView.findViewById(R.id.news_title);// 取得实例
            time = (TextView) itemView.findViewById(R.id.news_time);// 取得实例
            time2 = (TextView) itemView.findViewById(R.id.news_time2);

            newsdetail = (LinearLayout) itemView.findViewById(R.id.newsdetail);
        }
    }
}
