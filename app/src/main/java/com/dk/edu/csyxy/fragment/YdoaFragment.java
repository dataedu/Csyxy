package com.dk.edu.csyxy.fragment;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.dk.edu.core.http.HttpUtil;
import com.dk.edu.core.http.request.HttpListener;
import com.dk.edu.core.ui.BaseFragment;
import com.dk.edu.core.util.CoreSharedPreferencesHelper;
import com.dk.edu.core.util.DeviceUtil;
import com.dk.edu.core.util.StringUtils;
import com.dk.edu.core.widget.ErrorLayout;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.adapter.YdOaAdapter;
import com.dk.edu.csyxy.entity.YdOaEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.os.Handler;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动OA
 * 作者：janabo on 2017/6/8 14:23
 */
public class YdoaFragment extends BaseFragment{

    private Context mContext;
    private GridView other_recycler_view;//其它管理
    private GridView oprition_recycler_view;//业务管理
    private LinearLayout oprition_layout;
    private LinearLayout other_layout;
    private YdOaAdapter bAdapter;
    private YdOaAdapter oAdapter;
    private CoreSharedPreferencesHelper helper;
    private String userIdDes = "";

    List<YdOaEntity> bData = new ArrayList<YdOaEntity>();
    List<YdOaEntity> oData = new ArrayList<YdOaEntity>();
    List<YdOaEntity> aData = new ArrayList<YdOaEntity>();

    private ErrorLayout errorLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.mp_ydoa;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        helper = new CoreSharedPreferencesHelper(getActivity());
        userIdDes = helper.getValue("userIdDes");
        Log.e("userIdDes++++++++", userIdDes+"");

        initView();
        if(DeviceUtil.checkNet()){
            getList();
        }else{
           errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
        }

    }

    private void initView(){
        mContext = getActivity();
        errorLayout = findView(R.id.error_layout);
        other_recycler_view = findView(R.id.other_recycler_view);
        oprition_recycler_view = findView(R.id.oprition_recycler_view);
        oprition_layout = findView(R.id.oprition_layout);
        other_layout = findView(R.id.other_layout);
        bAdapter = new YdOaAdapter(bData,mContext, userIdDes);
        oprition_recycler_view.setAdapter(bAdapter);
        oAdapter = new YdOaAdapter(oData,mContext,userIdDes);
        other_recycler_view.setAdapter(oAdapter);
    }

    public void getList() {
        errorLayout.setErrorType(ErrorLayout.LOADDATA);
        HttpUtil.getInstance().postJsonObjectRequest("apps/xyfg/getImageList", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") != 200) {
                        errorLayout.setErrorType(ErrorLayout.DATAFAIL);
                    }else{
                        errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        String json =  result.getJSONArray("data").toString();
                        List<YdOaEntity> list1 = new Gson().fromJson(json,new TypeToken<List<YdOaEntity>>(){}.getType());
                        if(list1 == null){
                            mHander.sendEmptyMessage(0);
                        }else if(list1.size() == 0){
                            mHander.sendEmptyMessage(-1);
                        }else {
                            aData.clear();
                            aData.addAll(list1);
                            mHander.sendEmptyMessage(1);
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

    Handler mHander = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case -1:
                    errorLayout.setErrorType(ErrorLayout.NODATA);
                    break;
                case 0:
                    errorLayout.setErrorType(ErrorLayout.DATAFAIL);
                    break;
                case 1:
                    bData.clear();
                    oData.clear();
                    for(YdOaEntity o : aData){
                        if(StringUtils.isNotEmpty(o.getDetailUrl()))
                            bData.add(o);
                        else
                            oData.add(o);
                    }
                    if(bData.size()>0)
                        oprition_layout.setVisibility(View.VISIBLE);
                    else
                        oprition_layout.setVisibility(View.GONE);
                    if(oData.size()>0)
                        other_layout.setVisibility(View.VISIBLE);
                    else
                        other_layout.setVisibility(View.GONE);

                    bAdapter.notifyDataSetChanged();
                    oAdapter.notifyDataSetChanged();
                    errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
                    break;
                default:
                    break;
            }

        };
    };
}
