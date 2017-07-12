package com.dk.mp.csyxy.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dk.mp.core.ui.HttpWebActivity;
import com.dk.mp.core.util.CoreSharedPreferencesHelper;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.util.security.Signature;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.entity.YdOaEntity;

import java.util.List;

public class YdOaAdapter extends BaseAdapter{

	private List<YdOaEntity> mData;
    private Context mContext;
    LayoutInflater inflater;
    private CoreSharedPreferencesHelper helper;
    
//    private String userIdDes = "";

	public YdOaAdapter(List<YdOaEntity> mData, Context mContext2) {
		super();
		this.mData = mData;
		this.mContext = mContext2;
		inflater = LayoutInflater.from(mContext);
        helper = new CoreSharedPreferencesHelper(mContext);
//		this.userIdDes = userIdDes;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.ydoa_adapter, parent, false);
			holder.imageView = (ImageView) convertView.findViewById(R.id.item_image);
			holder.mTextView = (TextView) convertView.findViewById(R.id.item_text);
			holder.container = (FrameLayout) convertView.findViewById(R.id.container);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final YdOaEntity entity = mData.get(position);
		if(StringUtils.isNotEmpty(entity.getUrl())) {
            setAppIcon(holder, entity.getName());//自定义oa
        }else{
            setAppIcon(holder, entity.getIdentity());//非自定义
        }
        holder.mTextView.setText(entity.getLabel());
        
        holder.container.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                 Intent intent;
                 if(StringUtils.isNotEmpty(entity.getUrl())){
                     intent  = new Intent(mContext, HttpWebActivity.class);
                     intent.putExtra("title",entity.getLabel());
                     intent.putExtra("close_web",-1);

                     String uid ="";
                     if(helper.getLoginMsg() != null){
                         uid = helper.getLoginMsg().getUid();
                         uid = Signature.encrypt(uid+"|"+Signature.encrypt("dake_oa_app_key")+"|"+System.currentTimeMillis());
                     }
                     intent.putExtra("url",entity.getUrl()+"&token="+uid);
//                     intent.putExtra("url",entity.getUrl()+"&token="+userIdDes);
//                     Log.e("userIdDes", entity.getUrl()+"&token="+userIdDes);
                 }else{
                     intent = new Intent();
                     intent.putExtra("title",entity.getLabel());
                     intent.setAction("oa_"+entity.getIdentity());
                     intent.addCategory(Intent.CATEGORY_DEFAULT);
                 }
                 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                 mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	
	/**
	 * @version 2013-3-21
	 * @author wangw
	 */
	private static class ViewHolder {
        public ImageView imageView;
        public TextView mTextView;
        public FrameLayout container;
	}
	
	public void setAppIcon(ViewHolder holder,String appname){
        switch (appname){
            case "tzgg"://通知公告
                holder.imageView.setImageResource(R.drawable.app_tzgg_oa);
                break;
            case "gzzd"://规章制度
                holder.imageView.setImageResource(R.drawable.app_gzzd);
                break;
            case "zbap"://值班安排
                holder.imageView.setImageResource(R.drawable.app_zbap);
                break;
            case "grrc"://领导日程
                holder.imageView.setImageResource(R.drawable.app_ldrc);
                break;
            case "hygl"://会议管理
                holder.imageView.setImageResource(R.drawable.app_hygl);
                break;
            case "cy"://我的传阅
                holder.imageView.setImageResource(R.drawable.app_wdcy);
                break;
            case "gk"://公开
                holder.imageView.setImageResource(R.drawable.app_gk);
                break;
            case "wdsh"://我的审核
                holder.imageView.setImageResource(R.drawable.app_wdsh);
                break;
            case "wddb"://我的待办
                holder.imageView.setImageResource(R.drawable.app_wddb);
                break;
            case "dwsq"://我的申请
                holder.imageView.setImageResource(R.drawable.app_wdsq);
                break;
            case "wdcg"://我的草稿
                holder.imageView.setImageResource(R.drawable.app_wdcg);
                break;
            case "ywhy"://邀我参加的会议
                holder.imageView.setImageResource(R.drawable.app_ywhy);
                break;
            case "tzgggk":
            	holder.imageView.setImageResource(R.drawable.app_tzgg_zdy);
            	break;
            default:
                holder.imageView.setImageResource(R.drawable.app_hygl);
                break;
        }
    }
}
