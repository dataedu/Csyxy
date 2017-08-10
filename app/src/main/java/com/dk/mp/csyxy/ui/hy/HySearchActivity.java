package com.dk.mp.csyxy.ui.hy;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.SnackBarUtil;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.core.widget.ListRadioDialog;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.http.Manager;
import com.dk.mp.csyxy.ui.hy.adapter.KsAdapter;
import com.dk.mp.core.entity.Ks;
import com.dk.mp.csyxy.ui.hy.db.YellowRealmHelper;
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

public class HySearchActivity extends MyActivity {

    private KsAdapter mAdapter;
    private ListView mListView;
    private EditText searchKeywords;
    private List<Ks> list = new ArrayList<>();
    private TextView cancle_search;

    private ErrorLayout mError;
    private LinearLayout layout_search;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_hy_search;
    }

    @Override
    protected void initialize() {
        super.initialize();
        findView();
//        setTitle("搜索");

        if (DeviceUtil.checkNet()) {
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }

    }

    private void findView() {
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        layout_search = (LinearLayout) findViewById(R.id.layout_search);

        mListView = (ListView) findViewById(R.id.listView);
        cancle_search = (TextView) findViewById(R.id.cancle_search);
        searchKeywords = (EditText) findViewById(R.id.search_Keywords);
        searchKeywords.setHint("科室、电话");
        searchKeywords.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    final String keywords = searchKeywords.getText().toString();
                    Logger.info(keywords);
                    if (StringUtils.isNotEmpty(keywords)) {
                        if(DeviceUtil.checkNet()){//判断是否有网络
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            query();
                            list.clear();
                        }else{
                            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
                        }
                    } else {
                        SnackBarUtil.showShort(layout_search,"请输入关键字");
                    }
                }else if(actionId == 3 && event == null){
                    final String keywords = searchKeywords.getText().toString();
                    if (!StringUtils.isNotEmpty(keywords)) {
                        hideSoftInput();
                        SnackBarUtil.showShort(layout_search,"请输入关键字");
                    }
                }
                return false;
            }
        });
        cancle_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                final ListRadioDialog l = new ListRadioDialog(mContext);
                if (list.get(position).getTels() != null){
                    final String[] str = list.get(position).getTels().split("/");

                    l.show(str,new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            l.cancel();
                            DeviceUtil.call(mContext, str[position]);
                        }
                    });
                }

                /*final String[] str = getTel(list.get(position).getValues());
                if (str.length > 0) {
                    final ListRadioDialog l = new ListRadioDialog(HySearchActivity.this);
                    l.show(str, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            l.cancel();
                            DeviceUtil.call(HySearchActivity.this, str[position]);
                        }
                    });
                }*/

            }
        });
    }

    private String[] getTel(List<String> tels) {
        String[] temp = new String[tels.size()];
        for (int i = 0; i < tels.size(); i++) {
            temp[i] = tels.get(i);
        }
        return temp;
    }

    /**
     * 初始化列表.
     * @return List<App>
     */
    public void query() {
        mError.setErrorType(ErrorLayout.LOADDATA);
        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("key", searchKeywords.getText().toString());
        HttpUtil.getInstance().postJsonObjectRequest("apps/yellowpage/query?key="+searchKeywords.getText().toString(), map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    if (result.getInt("code") != 200) {
                        mError.setErrorType(ErrorLayout.DATAFAIL);
                    } else {
                        String json = result.getJSONArray("data").toString();
//                        List<Ks> departments = new Gson().fromJson(json, new TypeToken<List<Ks>>() {}.getType());
                          List<Ks> departments = Manager.getHySerach(result);
                        list.addAll(departments);
                        if (departments.size() > 0) {
                            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
                            mAdapter = new KsAdapter(HySearchActivity.this, list);
                            mListView.setAdapter(mAdapter);
                        } else {
                            mError.setErrorType(ErrorLayout.SEARCHNODATA);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mError.setErrorType(ErrorLayout.DATAFAIL);
                }
            }
            @Override
            public void onError(VolleyError error) {
                mError.setErrorType(ErrorLayout.DATAFAIL);
            }
        });
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

}
