package com.dk.edu.csyxy.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.edu.core.entity.JsonData;
import com.dk.edu.core.entity.User;
import com.dk.edu.core.http.HttpUtil;
import com.dk.edu.core.http.request.HttpListener;
import com.dk.edu.core.ui.MyActivity;
import com.dk.edu.core.util.BroadcastUtil;
import com.dk.edu.core.util.CoreSharedPreferencesHelper;
import com.dk.edu.core.util.StringUtils;
import com.dk.edu.csyxy.R;

/**
 * 作者：janabo on 2017/6/8 17:46
 */
public class UserInfoActivity extends MyActivity {
    private ImageView photo;
    private TextView username, depart, xh,xhorgh,yxorbm;
    private Context context = UserInfoActivity.this;
    private CoreSharedPreferencesHelper h;
    private Button loginout;

    @Override
    protected int getLayoutID() {
        return R.layout.mp_userinfo;
    }

    @Override
    protected void initialize() {
        super.initialize();
        h = new CoreSharedPreferencesHelper(this);
        setTitle("个人资料");
        findView();
        dd();
    }

    /**
     * 初始化控件.
     */
    private void findView() {

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        photo = (ImageView) findViewById(R.id.photo);
        username = (TextView) findViewById(R.id.username);
        depart = (TextView) findViewById(R.id.depart);
        xh = (TextView) findViewById(R.id.xh);
        xhorgh = (TextView) findViewById(R.id.xhorgh);
        yxorbm = (TextView) findViewById(R.id.yxorbm);
        loginout = (Button) findViewById(R.id.loginout);
        loginout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginOut();
            }
        });
    }

    /**
     * 填充个人信息到界面.
     */
    private void dd() {
        User user= h.getUser();
        if (user != null) {
            username.setText(StringUtils.checkEmpty(user.getUserName()));
            depart.setText(StringUtils.checkEmpty(user.getDepartName()));
            xh.setText(StringUtils.checkEmpty(user.getUserId()));
            if("teacher".equals(user.getRoles())){
                xhorgh.setText("工号");
                yxorbm.setText("部门");
            }else{
                xhorgh.setText("学号");
                yxorbm.setText("院系");
            }

            if(StringUtils.isNotEmpty(user.getPhoto())){
                photo.setImageURI(Uri.parse(user.getPhoto()));
            }
        }
    }

    /**
     * 处理登录.
     */
    private void loginOut() {
        HttpUtil.getInstance().getGsonRequestJson(JsonData.class, "logout", new HttpListener<JsonData>() {
            @Override
            public void onSuccess(JsonData result) {
            }
            @Override
            public void onError(VolleyError error) {
            }
        });
        h.cleanUser();
        h.setValue("nick", null);
        BroadcastUtil.sendBroadcast(context, "user");

        Intent intent = new Intent();
        intent.putExtra("from", "lock");
        intent.setClass(UserInfoActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
