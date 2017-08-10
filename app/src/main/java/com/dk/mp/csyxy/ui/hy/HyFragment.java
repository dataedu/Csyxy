package com.dk.mp.csyxy.ui.hy;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.BaseFragment;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.ui.hy.adapter.BmAdapter;
import com.dk.mp.csyxy.ui.hy.db.YellowRealmHelper;
import com.dk.mp.core.entity.Bm;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cobb on 2017/8/7.
 */

public class HyFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private ListView listView;
    private BmAdapter adapter;
    private List<Bm> list = new ArrayList<>();
    private LinearLayout search_linearlayout;
    private YellowRealmHelper realmHelper;

    private ErrorLayout errorLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_hy;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        realmHelper = new YellowRealmHelper(mContext);

        errorLayout = findView(R.id.error_layout);
        search_linearlayout = findView(R.id.search_linearlayout);
        listView = findView(R.id.listView);
        listView.setOnItemClickListener(this);

        search_linearlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HySearchActivity.class));
            }
        });

        if (DeviceUtil.checkNet()){
            getDepartList();
        }else{
            getList();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), HyKsActivity.class);
        intent.putExtra("id", list.get(position).getIdDepart());
        intent.putExtra("type", "depart");
        intent.putExtra("name", list.get(position).getNameDepart());
        startActivity(intent);
    }

    /**
     * 初始化列表.
     * @return List<App>
     */
    public  void getDepartList() {
        errorLayout.setErrorType(ErrorLayout.LOADDATA);
        Map<String, String> map = new HashMap<String, String>();
        HttpUtil.getInstance().postJsonObjectRequest("apps/yellowpage/getBmList", null, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (result.getInt("code") != 200) {
                        errorLayout.setErrorType(ErrorLayout.DATAFAIL);
                    }else{
                        errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
                        String json =  result.getJSONArray("data").toString();
                        List<Bm> departments = new Gson().fromJson(json,new TypeToken<List<Bm>>(){}.getType());
                        list.addAll(departments);
                        if (list.size()>0){
//                            new YellowPageDBHelper(getContext()).insertDepartList(list);
                            realmHelper.deleteDepartment();
                            realmHelper.addDepartment(departments);
                            handler.sendEmptyMessage(2);
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

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Logger.info("msg.what="+msg.what);
            switch (msg.what) {
                case 0:
                    errorLayout.setErrorType(ErrorLayout.LOADDATA);
                    break;
                case 1:
                    if (adapter == null) {
                        adapter = new BmAdapter(getContext(), list);
                        listView.setAdapter(adapter);
                    } else {
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    if (adapter == null) {
                        adapter = new BmAdapter(getContext(), list);
                        listView.setAdapter(adapter);
                    } else {
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                    }
                    errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
                    break;
            }
        };
    };

    /**
     * 初始化数据
     */
    private void getList() {
//        list = new YellowPageDBHelper(getContext()).getDepartMentList();
        list = realmHelper.queryAllDepartment();
        errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
        if (list.size() > 0) {
            adapter = new BmAdapter(getContext(), list);
            listView.setAdapter(adapter);
            MsgDialog.show(getContext(), getContext().getString(R.string.net_no2));
        }else{
//            errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
//            errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }
    }

}
