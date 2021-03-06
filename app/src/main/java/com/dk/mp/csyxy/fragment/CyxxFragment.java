package com.dk.mp.csyxy.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.androidkun.xtablayout.XTabLayout;
import com.dk.mp.core.receiver.NetworkConnectChangedReceiver;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.BroadcastUtil;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.adapter.MyFragmentPagerAdapter;
import com.dk.mp.csyxy.ui.hy.HyFragment;
import com.dk.mp.csyxy.ui.xxxw.XxxwMainFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 长医信息
 * 作者：janabo on 2017/6/8 14:22
 */
public class CyxxFragment extends BaseFragment{
    XTabLayout mTabLayout;
    ViewPager mViewPager;
    private List<BaseFragment> fragments;//定义要装fragment的列表
    private List<String> titles;
    MyFragmentPagerAdapter myFragmentPagerAdapter;

    private LinearLayout warn_main;//是否有网络

    @Override
    protected int getLayoutId() {
        return R.layout.mp_cyxx;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mTabLayout = findView(R.id.tab_FindFragment_title);
        mViewPager = findView(R.id.vp_FindFragment_pager);
        warn_main = findView(R.id.warn_main);
        getData();

    }

    public void getData(){
        //将fragment装进列表中
        fragments = new ArrayList<>();
        fragments.add(NewsFragment.newInstance("zjcy"));
//        fragments.add(NewsFragment.newInstance("xw"));
        fragments.add(new XxxwMainFragment());
        fragments.add(NewsFragment.newInstance("gg"));
        fragments.add(HttpWebFragment.newInstance("http://www.csmu.edu.cn/mobilerecruit/default.aspx"));
        fragments.add(new HyFragment());
        fragments.add(NewsFragment.newInstance("rcyj"));
        fragments.add(NewsFragment.newInstance("ybxx"));
        fragments.add(HttpWebFragment.newInstance("http://www.csmu.edu.cn/Mobile/index.aspx"));
        fragments.add(HttpWebFragment.newInstance("http://ditu.amap.com/search?id=B02DB049OA&city=430112&geoobj=118.9078%7C32.077678%7C118.915654%7C32.082024&query_type=IDQ&query=%E9%95%BF%E6%B2%99%E5%8C%BB%E5%AD%A6%E9%99%A2&zoom=17 "));
        fragments.add(NewsFragment.newInstance("dzjs"));
        fragments.add(new XyfgFragment());

//        #############
//        fragments.add(new CenterPersonFragment());
//        fragments.add(new CenterPersonFragment());
//        fragments.add(new CenterPersonFragment());
        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        titles = new ArrayList<>();
        titles.add("走进长医");
        titles.add("新闻");
        titles.add("公告");
        titles.add("招生专栏");
        titles.add("常用电话");
        titles.add("人才引进");
        titles.add("邀标信息");
        titles.add("学校官网");
        titles.add("学校地图");
        titles.add("党政建设");
        titles.add("校园风光");

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        FragmentManager manager = getFragmentManager();
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(manager,fragments,titles);
        mViewPager.setAdapter(myFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        BroadcastUtil.registerReceiver(getContext(), mRefreshBroadcastReceiver, new String[]{"checknetwork_true","checknetwork_false"});
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(new NetworkConnectChangedReceiver(), filter);
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("NewApi") @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("checknetwork_true")) {
                warn_main.setVisibility(View.GONE);
            }
            if(action.equals("checknetwork_false")){
                warn_main.setVisibility(View.VISIBLE);
            }
        }
    };

}
