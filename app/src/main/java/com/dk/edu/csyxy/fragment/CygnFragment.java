package com.dk.edu.csyxy.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.VolleyError;
import com.dk.edu.core.http.HttpUtil;
import com.dk.edu.core.http.request.HttpListener;
import com.dk.edu.core.ui.BaseFragment;
import com.dk.edu.core.util.DeviceUtil;
import com.dk.edu.core.view.RecycleViewDivider;
import com.dk.edu.core.widget.ErrorLayout;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.adapter.CygnAdapter;
import com.dk.edu.csyxy.entity.OaItemEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 常用功能
 * 作者：janabo on 2017/6/8 14:24
 */
public class CygnFragment extends BaseFragment{
    private Context mContext;
    List<OaItemEntity> mData = new ArrayList<>();
    RecyclerView app_recycler_view;
    private ErrorLayout mError;
    CygnAdapter uAdapter;//非自定义oa app

    @Override
    protected int getLayoutId() {
        return R.layout.mp_cygn;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mContext = getContext();
        app_recycler_view = (RecyclerView) root.findViewById(R.id.app_recycler_view);
        mError = (ErrorLayout) root.findViewById(R.id.main_error);

        RecycleViewDivider vDivider = new RecycleViewDivider(getActivity(), GridLayoutManager.VERTICAL, 1, Color.rgb(201, 201, 201));
        RecycleViewDivider hDivider = new RecycleViewDivider(getActivity(), GridLayoutManager.HORIZONTAL, 1, Color.rgb(201, 201, 201));
        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
        uAdapter = new CygnAdapter(mContext,mData);
        app_recycler_view.setLayoutManager(new GridLayoutManager(mContext, 4, GridLayoutManager.VERTICAL, false));
        app_recycler_view.setAdapter(uAdapter);
        app_recycler_view.setItemAnimator(animator);
        app_recycler_view.addItemDecoration(vDivider);//添加分割线
        app_recycler_view.addItemDecoration(hDivider);//添加分割线
        if(DeviceUtil.checkNet()){
            getDemoData();
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public void getData(){
        HttpUtil.getInstance().postJsonObjectRequest("apps/oa/listModule", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                if (result.optInt("code") == 200){//成功返回数据

                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }

    public void getDemoData(){
        mData.add(new OaItemEntity("课表查询","课表查询","app_kbcx"));
        mData.add(new OaItemEntity("网上评价","网上评价","app_wspj"));
        mData.add(new OaItemEntity("网上选课","网上选课","app_wsxk"));
        mData.add(new OaItemEntity("资助","资助","app_zz"));
        mData.add(new OaItemEntity("一卡通","一卡通","app_ykt"));
        mData.add(new OaItemEntity("图书馆","图书馆","app_tsg"));
        mData.add(new OaItemEntity("设备保修","设备保修","app_sbbx"));
        mData.add(new OaItemEntity("网上缴费","网上缴费","app_wsjf"));
        mData.add(new OaItemEntity("宿舍管理","宿舍管理","app_ssgl"));
        mData.add(new OaItemEntity("离校办理","离校办理","app_lxbl"));
        mData.add(new OaItemEntity("迎新办理","迎新办理","app_yxbl"));
        uAdapter.notifyDataSetChanged();
    }
}
