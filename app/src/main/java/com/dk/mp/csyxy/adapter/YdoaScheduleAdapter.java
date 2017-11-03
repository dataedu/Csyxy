package com.dk.mp.csyxy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.entity.YdoaSchedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by cobb on 2017/6/27.
 */

public class YdoaScheduleAdapter extends RecyclerView.Adapter<YdoaScheduleAdapter.MyViewHolder>{

    private Context mContext;
    private List<YdoaSchedule.ScheduleItemsBean> mData;
    private LayoutInflater inflater;

    public YdoaScheduleAdapter(Context mContext, List<YdoaSchedule.ScheduleItemsBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
        inflater = LayoutInflater.from(mContext);
    }

    public List<YdoaSchedule.ScheduleItemsBean> getmData() {
        return mData;
    }

    public void setmData(List<YdoaSchedule.ScheduleItemsBean> mData) {
        this.mData = mData;
    }

    public void notify(List<YdoaSchedule.ScheduleItemsBean> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    @Override
    public YdoaScheduleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ydoa_ad_schedule, parent, false);
        return new YdoaScheduleAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(YdoaScheduleAdapter.MyViewHolder holder, int position) {
        YdoaSchedule.ScheduleItemsBean bean = mData.get(position);

        if (bean.getDate() != null){
            String[] date = bean.getDate().split("-");
            if (date.length > 2){
                String mo = date[1];
                String da = date[2];
                if (mo.startsWith("0")){
                    mo = mo.replace("0", "");
                }
                if (da.startsWith("0")){
                    da = da.replace("0", "");
                }
                holder.ydoa_date.setText(mo + "月" + da + "日");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    holder.ydoa_week.setText(getWeekOfDate(sdf.parse(bean.getDate())));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        if (position == mData.size()-1){
            holder.line.setVisibility(View.GONE);
        }

        holder.ydoa_host.setText(bean.getHost());
        holder.ydoa_organization.setText(bean.getOrganization());
        holder.ydoa_location.setText(bean.getLocation());
        holder.ydoa_content.setText(bean.getContent());
        holder.ydoa_sj.setText(bean.getTime());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ydoa_week, ydoa_date, ydoa_host, ydoa_organization, ydoa_location, ydoa_content,ydoa_sj;
        View line;

        public MyViewHolder(View itemView) {
            super(itemView);

            ydoa_week = (TextView) itemView.findViewById(R.id.ydoa_week);
            ydoa_date = (TextView) itemView.findViewById(R.id.ydoa_date);
            ydoa_host = (TextView) itemView.findViewById(R.id.ydoa_host);
            ydoa_organization = (TextView) itemView.findViewById(R.id.ydoa_organization);
            ydoa_location = (TextView) itemView.findViewById(R.id.ydoa_location);
            ydoa_content = (TextView) itemView.findViewById(R.id.ydoa_content);
            ydoa_sj = (TextView) itemView.findViewById(R.id.ydoa_sj);

            line = itemView.findViewById(R.id.line);

        }
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }
}
