package com.dk.mp.csyxy.ui.xyfg;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.ui.xyfg.entity.SceneryEntity;
import com.dk.mp.csyxy.ui.xyfg.view.ZoomImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SceneryDetailsActivity extends MyActivity implements OnClickListener{

	private ViewPager scenerydetails;
	private List<SceneryEntity> list = new ArrayList<SceneryEntity>();
	private RelativeLayout layout;

	private Gson gson = new Gson();

	@Override
	protected int getLayoutID() {
		return R.layout.app_scenery_details;
	}

	@Override
	protected void initView() {
		super.initView();

		setTitle(getIntent().getStringExtra("title"));

		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		initViews();
	}

	@SuppressWarnings("deprecation")
	private void initViews(){
		scenerydetails = (ViewPager) findViewById(R.id.scenerydetails);
		list = gson.fromJson(getIntent().getStringExtra("list"), new TypeToken<ArrayList<SceneryEntity>>() {}.getType());
		scenerydetails.setAdapter(new MyPagerAdapter());
		scenerydetails.setCurrentItem(getIntent().getIntExtra("index", 0));
		scenerydetails.setOnPageChangeListener (new OnPageChangeListener() {
            @Override
            public void onPageSelected(int select) {
            	setTitle((select+1)+"/"+list.size());
            }
           
            @Override
            public void onPageScrolled(int positon, float arg1, int arg2 ) {}
           
            @Override
            public void onPageScrollStateChanged(int state) {}
      });
		layout = (RelativeLayout) findViewById(R.id.titleid);
	}
	
	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View)object); 
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			View view = LayoutInflater.from(SceneryDetailsActivity.this).inflate(R.layout.app_scenery_detail,null);
			ZoomImageView sd = (ZoomImageView) view.findViewById(R.id.scenerydetailgridimage);
			Glide.with(getApplicationContext()).load(list.get(arg1).getImage()).placeholder(R.mipmap.image_defualt).into(sd);
			sd.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(layout.isShown()){
						layout.setVisibility(View.INVISIBLE);
					}else{
						layout.setVisibility(View.VISIBLE);
					}
					
				}
			});
	        ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(view);
			return view;
		}
		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override
		public Parcelable saveState() {
			return null;
		}
		@Override
		public void startUpdate(View arg0) {}
	}

	@Override
	public void onClick(View v) {
		if(layout.isShown()){
			layout.setVisibility(View.INVISIBLE);
		}else{
			layout.setVisibility(View.VISIBLE);
		}
	}
}
