package com.dk.edu.csyxy.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.dk.edu.core.entity.App;
import com.dk.edu.core.ui.BaseFragment;
import com.dk.edu.core.util.CoreSharedPreferencesHelper;
import com.dk.edu.core.util.DeviceUtil;
import com.dk.edu.core.view.RecycleViewDividerNoPadding;
import com.dk.edu.core.widget.ErrorLayout;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.adapter.CygnAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.dk.edu.csyxy.R.id.main_error;

/**
 * 常用功能
 * 作者：janabo on 2017/6/8 14:24
 */
public class CygnFragment extends BaseFragment{
    private Context mContext;
    List<App> mData = new ArrayList<>();
    RecyclerView app_recycler_view;
    private ErrorLayout mError;
    CygnAdapter uAdapter;//非自定义oa app
    RelativeLayout layout_bg;
    CoreSharedPreferencesHelper helper;

    @Override
    protected int getLayoutId() {
        return R.layout.mp_cygn;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mContext = getContext();
        helper = new CoreSharedPreferencesHelper(mContext);
        layout_bg = findView(R.id.layout_bg);
        app_recycler_view = (RecyclerView) root.findViewById(R.id.app_recycler_view);
        mError = (ErrorLayout) root.findViewById(main_error);

        RecycleViewDividerNoPadding vDivider = new RecycleViewDividerNoPadding(getActivity(), GridLayoutManager.VERTICAL, 1, Color.rgb(201, 201, 201));
        RecycleViewDividerNoPadding hDivider = new RecycleViewDividerNoPadding(getActivity(), GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201));
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        uAdapter = new CygnAdapter(mContext,mData,layout_bg);
        app_recycler_view.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        app_recycler_view.setAdapter(uAdapter);
        app_recycler_view.setItemAnimator(animator);
        app_recycler_view.addItemDecoration(vDivider);//添加分割线
        app_recycler_view.addItemDecoration(hDivider);//添加分割线
        if(DeviceUtil.checkNet()){
//            getDemoData();
            getData();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void getData(){
       List<App> apps = helper.getAllAppList();
        if(apps.size()>0){
            mData.clear();
            mData.addAll(apps);
            uAdapter.notifyDataSetChanged();
        }else{
            mData.clear();
            uAdapter.notifyDataSetChanged();
            mError.setErrorType(ErrorLayout.NODATA);
        }
    }

    public void getDemoData(){
//        mData.add(new OaItemEntity("课表查询","课表查询","app_kbcx"));
//        mData.add(new OaItemEntity("网上评价","网上评价","app_wspj"));
//        mData.add(new OaItemEntity("网上选课","网上选课","app_wsxk"));
//        mData.add(new OaItemEntity("资助","资助","app_zz"));
//        mData.add(new OaItemEntity("一卡通","一卡通","app_ykt"));
//        mData.add(new OaItemEntity("图书馆","图书馆","app_tsg"));
//        mData.add(new OaItemEntity("设备保修","设备保修","app_sbbx"));
//        mData.add(new OaItemEntity("网上缴费","网上缴费","app_wsjf"));
//        mData.add(new OaItemEntity("宿舍管理","宿舍管理","app_ssgl"));
//        mData.add(new OaItemEntity("离校办理","离校办理","app_lxbl"));
//        mData.add(new OaItemEntity("迎新办理","迎新办理","app_yxbl"));
//        uAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
