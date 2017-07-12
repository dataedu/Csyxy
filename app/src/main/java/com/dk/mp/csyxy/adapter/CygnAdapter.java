package com.dk.mp.csyxy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dk.mp.core.entity.App;
import com.dk.mp.core.util.ImageUtil;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.csyxy.R;

import java.util.List;

/**
 * 作者：janabo on 2017/6/9 14:15
 */
public class CygnAdapter extends RecyclerView.Adapter<CygnAdapter.MyViewHolder>{
    private List<App> mData;
    private Context mContext;
    LayoutInflater inflater;
    private RelativeLayout layout_bg;

    public CygnAdapter(Context mContext, List<App> mData, RelativeLayout layout_bg){
        this.mContext = mContext;
        this.mData = mData;
        this.layout_bg = layout_bg;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.main_manager_item,null,false);
            return new MyViewHolder(v);
        }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
            App bean = mData.get(position);
            holder.imageView.setImageResource(ImageUtil.getResource(bean.getIcon()));
            holder.mTextView.setText(bean.getName());
        }

    @Override
    public int getItemCount() {
            return mData.size();
        }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout mContainer;
        public ImageView imageView;
        public TextView mTextView;

        public MyViewHolder(View v) {
            super(v);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            imageView = (ImageView) v.findViewById(R.id.item_image);
            mTextView = (TextView) v.findViewById(R.id.item_text);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SnackBarUtil.showShort(layout_bg,"建设中");
                }
            });
        }
    }


}
