package com.dk.edu.csyxy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.entity.News;
import com.dk.edu.csyxy.ui.HeaderView;

import java.util.List;

/**
 * 作者：janabo on 2017/6/13 17:04
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>{
    Context mContext;
    List<News> mData;
    LayoutInflater mInflater;

    public NewsAdapter(Context context, List<News> news) {
        this.mContext = context;
        this.mData = news;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == 0){
            v = mInflater.inflate(R.layout.mp_news_item,parent,false);
        }
// else if(viewType == -1){
//            v = mInflater.inflate(R.layout.core_item_footer,parent,false);
//        }
        else{
            v = mInflater.inflate(R.layout.core_headview,parent,false);
            HeaderView headerView = new HeaderView();
            headerView.init(v,mContext);
        }

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if(getItemViewType(position) == 0){
            News news = mData.get(position);
            holder.title.setText(news.getName());
            holder.time.setText(news.getPublishTime());
            if (news.getImage() == null ){
       //         holder.image.setVisibility(View.GONE);
            } else {
                holder.image.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(news.getImage()).fitCenter().into(holder.image);
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
        private TextView title,time;
        public MyViewHolder(View v) {
            super(v);
            image = (ImageView) itemView.findViewById(R.id.news_img);// 取得实例
            title = (TextView) itemView.findViewById(R.id.news_title);// 取得实例
            time = (TextView) itemView.findViewById(R.id.news_time);// 取得实例
        }
    }
}
