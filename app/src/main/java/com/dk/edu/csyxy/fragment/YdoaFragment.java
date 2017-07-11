package com.dk.edu.csyxy.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.edu.core.dialog.MsgDialog;
import com.dk.edu.core.http.HttpUtil;
import com.dk.edu.core.http.request.HttpListener;
import com.dk.edu.core.ui.BaseFragment;
import com.dk.edu.core.util.CoreSharedPreferencesHelper;
import com.dk.edu.core.util.DeviceUtil;
import com.dk.edu.core.view.MyGridView;
import com.dk.edu.core.widget.ErrorLayout;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.adapter.YdOaAdapter;
import com.dk.edu.csyxy.adapter.YdoaScheduleAdapter;
import com.dk.edu.csyxy.entity.YdOaEntity;
import com.dk.edu.csyxy.entity.YdoaSchedule;
import com.dk.edu.csyxy.entity.YdoaUserinfo;
import com.dk.edu.csyxy.ui.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动OA
 * 作者：janabo on 2017/6/8 14:23
 */
public class YdoaFragment extends BaseFragment{

    private Context mContext;
    private ErrorLayout error;

    private MyGridView oprition_recycler_view;//业务管理
    private LinearLayout oprition_layout;
    private YdOaAdapter oAdapter;
    List<YdOaEntity> oData = new ArrayList<YdOaEntity>();

    private CoreSharedPreferencesHelper helper;
    private String userIdDes = "";

    private TextView userinfo_lastname, userinfo_name, userinfo_department;
    private LinearLayout userInfoIv;

    private LinearLayout recentUse_layout, bezhuIv;
    private TextView beizhuTv;

    //主题内容
    private LinearLayout themeIv;
    private TextView themeTv;
    private RecyclerView mRecyclerView;
    private YdoaScheduleAdapter mAdapter;
    List<YdoaSchedule.ScheduleItemsBean> mData = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.mp_ydoa;
    }

    @Override
    public void onResume() {
        super.onResume();

        helper = new CoreSharedPreferencesHelper(getActivity());
        userIdDes = helper.getValue("userIdDes");
        Log.e("userIdDes++++++++", userIdDes+"");

        initView();

        if (helper.getLoginMsg() == null){
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.putExtra("ydoa","ydoa");
            startActivity(intent);
        }else {
            if(DeviceUtil.checkNet()){
                error.setErrorType(ErrorLayout.LOADDATA);
                getSchedule();
                getList();
                getUserInfo();
            }else{
                setUserindo("NOnetwork");
//                error.setErrorType(ErrorLayout.HIDE_LAYOUT);
//                MsgDialog.show(mContext, getString(R.string.net_no2));
            }
        }
    }

    private void initView(){
        mContext = getActivity();
        error = findView(R.id.error_layout);

        userinfo_lastname = findView(R.id.userinfo_lastname);
        userinfo_name = findView(R.id.userinfo_name);
        userinfo_department = findView(R.id.userinfo_department);
        userInfoIv = findView(R.id.userInfoIv);
        userInfoIv.setVisibility(View.VISIBLE);

        oprition_recycler_view = findView(R.id.oprition_recycler_view);
        oprition_layout = findView(R.id.oprition_layout);
        oAdapter = new YdOaAdapter(oData,mContext);
        oprition_recycler_view.setAdapter(oAdapter);

        recentUse_layout = findView(R.id.recentUse_layout);
        bezhuIv = findView(R.id.bezhuIv);
        beizhuTv = findView(R.id.beizhuTv);

        themeIv = findView(R.id.themeIv);
        themeTv = findView(R.id.themeTv);
        mRecyclerView = findView(R.id.recycler_view);
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        mAdapter = new YdoaScheduleAdapter(mContext,mData);
        mRecyclerView.setAdapter ( mAdapter );
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void getUserInfo() {
        HttpUtil.getInstance().postJsonObjectRequest("apps/oa/getUserInfo", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
                    if (result.getInt("code") != 200) {
//                        MsgDialog.show(mContext, getString(R.string.data_fail));
                        setUserindo("NOserver");
                    }else{
                        YdoaUserinfo ydoaUserinfo = new Gson().fromJson(result.getJSONObject("data").toString(),YdoaUserinfo.class);
                        helper.setValue("ydoaUserinfo",result.getJSONObject("data").toString());
                        error.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        if (ydoaUserinfo != null){
                            String name = ydoaUserinfo.getUserName();
                            if (name != null){
                                userinfo_lastname.setText(name.subSequence(0,1));
                            }
                            userinfo_name.setText(ydoaUserinfo.getUserName());
                            userinfo_department.setText(ydoaUserinfo.getRole());

                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
//                    error.setErrorType(ErrorLayout.HIDE_LAYOUT);
//                    MsgDialog.show(mContext, getString(R.string.data_fail));
                    setUserindo("NOserver");
                }
            }
            @Override
            public void onError(VolleyError error3) {
//                MsgDialog.show(mContext, getString(R.string.data_fail));
//                error.setErrorType(ErrorLayout.HIDE_LAYOUT);
                setUserindo("NOserver");
            }
        });

    }

    private void setUserindo(String erro){
        error.setErrorType(ErrorLayout.HIDE_LAYOUT);
        if (helper.getValue("ydoaUserinfo") != null){
            YdoaUserinfo ydoaUserinfo = new Gson().fromJson(helper.getValue("ydoaUserinfo"),YdoaUserinfo.class);

            String name = ydoaUserinfo.getUserName();
            if (name != null){
                userinfo_lastname.setText(name.subSequence(0,1));
            }
            userinfo_name.setText(ydoaUserinfo.getUserName());
            userinfo_department.setText(ydoaUserinfo.getRole());

            if (erro.equals("NOserver")){
               MsgDialog.show(mContext,getString(R.string.data_fail));
            }else if (erro.equals("NOnetwork")){
                MsgDialog.show(mContext,getString(R.string.net_no2));
            }else {
                MsgDialog.show(mContext,getString(R.string.nodata));
            }

        }else {
            userInfoIv.setVisibility(View.GONE);
            oprition_layout.setVisibility(View.GONE);
            recentUse_layout.setVisibility(View.GONE);

            if (erro.equals("NOserver")){
                error.setErrorType(ErrorLayout.NODATA);
            }else if (erro.equals("NOnetwork")){
                error.setErrorType(ErrorLayout.NETWORK_ERROR);
            }else {
                error.setErrorType(ErrorLayout.NODATA);
            }
        }
    }

    private void getSchedule() {
        HttpUtil.getInstance().postJsonObjectRequest("apps/oa/leaderSchedule", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
//                    if (result.getInt("code") != 200) {
////                        MsgDialog.show(mContext, getString(R.string.data_fail));
//                    }else{
                    if (result.getInt("code") == 200){
                        String json =  result.getJSONObject("data").toString();
                        if (json != null){
                            recentUse_layout.setVisibility(View.VISIBLE);
                            YdoaSchedule yd = new Gson().fromJson(json,YdoaSchedule.class);

                            if (yd.getComment() != null && !yd.getComment().equals("")){
                                bezhuIv.setVisibility(View.VISIBLE);
                                beizhuTv.setText(yd.getComment());
                            }else {
                                bezhuIv.setVisibility(View.GONE);
                            }

                            if (yd.getScheduleItems() != null){
                                mData.clear();
                                mData.addAll(yd.getScheduleItems());
                                if (mData.size() > 0){
                                    themeIv.setVisibility(View.VISIBLE);
                                    themeTv.setText(yd.getTitle());

                                    mAdapter.notify(mData);
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
//                    MsgDialog.show(mContext, getString(R.string.data_fail));
                }
            }
            @Override
            public void onError(VolleyError error) {
//                MsgDialog.show(mContext, getString(R.string.data_fail));
            }
        });

    }

    public void getList() {
        HttpUtil.getInstance().postJsonObjectRequest("apps/oa/listModule", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                try {
//                    if (result.getInt("code") != 200) {
//                        MsgDialog.show(mContext, getString(R.string.data_fail));
//                    }else{
                    if (result.getInt("code") == 200) {
                        String json =  result.getJSONArray("data").toString();
                        List<YdOaEntity> list1 = new Gson().fromJson(json,new TypeToken<List<YdOaEntity>>(){}.getType());
                       if (list1.size()>0){
                           oData.clear();
                           oprition_layout.setVisibility(View.VISIBLE);
                           oData.addAll(list1);
                           oAdapter.notifyDataSetChanged();
                       }
                    }
                }catch (Exception e){
                    e.printStackTrace();
//                    MsgDialog.show(mContext, getString(R.string.data_fail));
                }
            }
            @Override
            public void onError(VolleyError error) {
//                MsgDialog.show(mContext, getString(R.string.data_fail));
            }
        });
    }

}
