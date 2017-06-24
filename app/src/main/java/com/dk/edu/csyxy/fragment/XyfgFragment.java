package com.dk.edu.csyxy.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.android.volley.VolleyError;
import com.dk.edu.core.entity.Department;
import com.dk.edu.core.http.HttpUtil;
import com.dk.edu.core.http.request.HttpListener;
import com.dk.edu.core.ui.BaseFragment;
import com.dk.edu.core.util.DeviceUtil;
import com.dk.edu.core.widget.ErrorLayout;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.ui.xyfg.entity.SceneryEntity;
import com.dk.edu.csyxy.ui.xyfg.view.SceneryItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cobb on 2017/6/24.
 */

public class XyfgFragment extends BaseFragment {

    private List<SceneryEntity> list;
    private ScrollView scroll;

    private ErrorLayout errorLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.app_scenery;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        scroll = findView(R.id.sceneryScrollView);
        errorLayout = findView(R.id.error_layout);

        if(DeviceUtil.checkNet()){
            initDatas();
        }else{
           errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    private void initDatas(){
        errorLayout.setErrorType(ErrorLayout.LOADDATA);

        HttpUtil.getInstance().postJsonObjectRequest("apps/xyfg/getTypeList", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") != 200) {
                        errorLayout.setErrorType(ErrorLayout.DATAFAIL);
                    }else{
                        errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        String json =  result.getJSONArray("data").toString();
                        list = new Gson().fromJson(json,new TypeToken<List<SceneryEntity>>(){}.getType());
                        if (list.size() > 0){
                            LinearLayout layout = SceneryItem.getViews(getActivity(), list);
                            scroll.addView(layout);
                        }else {
                            errorLayout.setErrorType(ErrorLayout.NODATA);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    errorLayout.setErrorType(ErrorLayout.DATAFAIL);
                }
            }
            @Override
            public void onError(VolleyError error) {
                errorLayout.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }



}
