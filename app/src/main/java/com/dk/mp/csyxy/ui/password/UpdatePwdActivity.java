package com.dk.mp.csyxy.ui.password;

import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.dk.mp.core.entity.LoginMsg;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.Base64Utils;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.dialog.PwdDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码
 * 作者：janabo on 2017/11/3 17:06
 */
public class UpdatePwdActivity extends MyActivity implements View.OnClickListener{
    private LinearLayout mRootview;
    private TextView mUserid;
    private EditText mOldPwd,mNewPwd,mNewRepwd;//密码输入框
    private ImageView mOClear,mNClear,mNRClear;//一键清楚按钮
    private TextView mErrorPwd,mErrorRepwd;//错误提示
    private LinearLayout ok,new_pwd_lin,new_repwd_lin,old_pwd_lin;
    private TextView ok_text;
    private Button mBack;
    private String mPwd="";
    LoginMsg loginMsg;

    @Override
    protected int getLayoutID() {
        return R.layout.mp_updatepwd;
    }

    @Override
    protected void initView() {
        super.initView();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.pwd_titlebar));
        }
        mUserid = (TextView) findViewById(R.id.user_id);
        loginMsg = getSharedPreferences().getLoginMsg();
        if(loginMsg!= null){
            mUserid.setText(loginMsg.getUid());
            mPwd = loginMsg.getPsw();
        }
        mRootview = (LinearLayout) findViewById(R.id.mrootview);
        mBack = (Button) findViewById(R.id.back);
        mErrorPwd = (TextView) findViewById(R.id.error_pwd);
        mErrorRepwd = (TextView) findViewById(R.id.error_repwd);
        mOldPwd = (EditText) findViewById(R.id.old_pwd);
        mNewPwd = (EditText) findViewById(R.id.new_pwd);
        mNewRepwd = (EditText) findViewById(R.id.new_repwd);
        mOClear = (ImageView) findViewById(R.id.old_pwd_clear);
        mNClear = (ImageView) findViewById(R.id.new_pwd_clear);
        mNRClear = (ImageView) findViewById(R.id.new_repwd_clear);
        old_pwd_lin = (LinearLayout) findViewById(R.id.old_pwd_lin);
        new_pwd_lin = (LinearLayout) findViewById(R.id.new_pwd_lin);
        new_repwd_lin = (LinearLayout) findViewById(R.id.new_repwd_lin);
        ok = (LinearLayout) findViewById(R.id.ok);
        ok_text = (TextView) findViewById(R.id.ok_text);

        mOldPwd.addTextChangedListener(oTextWatcher);
        mNewPwd.addTextChangedListener(oTextWatcher);
        mNewRepwd.addTextChangedListener(oTextWatcher);
        mOClear.setOnClickListener(this);
        mNClear.setOnClickListener(this);
        mNRClear.setOnClickListener(this);
        mBack.setOnClickListener(this);
        ok.setOnClickListener(this);
        ok.setEnabled(false);
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    TextWatcher oTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            dealUi();
            if(mNewPwd.getText().toString().equals(mNewRepwd.getText().toString())){
                mErrorRepwd.setText("");
                new_pwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
                new_repwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.old_pwd_clear:
                mOldPwd.setText("");
                mErrorPwd.setText("");
                old_pwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
                dealUi();
                break;
            case R.id.new_pwd_clear:
                mNewPwd.setText("");
                mErrorRepwd.setText("");
                new_pwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
                new_repwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
                dealUi();
                break;
            case R.id.new_repwd_clear:
                mNewRepwd.setText("");
                mErrorRepwd.setText("");
                new_pwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
                new_repwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
                dealUi();
                break;
            case R.id.ok:
                if(!mPwd.equals(Base64Utils.getBase64(mOldPwd.getText().toString().trim()))){
                    mErrorPwd.setText("密码不正确");
                    old_pwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd_error);
                }else {
                    mErrorPwd.setText("");
                    old_pwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
                    if(!mNewPwd.getText().toString().equals(mNewRepwd.getText().toString())){
                        mErrorRepwd.setText("两次密码不一致");
                        new_pwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd_error);
                        new_repwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd_error);
                    }else{
                        mErrorRepwd.setText("");
                        new_pwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
                        new_repwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
                        submit();
                    }
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void submit(){
        ok.setEnabled(false);
        Map<String,Object> map = new HashMap<>();
        map.put("userId",mUserid.getText().toString());
        map.put("password", Base64Utils.getBase64(mOldPwd.getText().toString().trim()));
        map.put("newPassword",Base64Utils.getBase64(mNewPwd.getText().toString().trim()));
        HttpUtil.getInstance().postJsonObjectRequest("updateAccount", map, new HttpListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result)  {
                ok.setEnabled(true);
                try {
                    if (result.getInt("code") != 200) {
                        mErrorPwd.setText(result.getString("msg"));
                        old_pwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd_error);
                    }else{
                        mErrorPwd.setText("");
                        old_pwd_lin.setBackgroundResource(R.drawable.border_radius_uppwd);
                        if(loginMsg!= null){//更新密码
                            loginMsg.setPsw(Base64Utils.getBase64(mNewPwd.getText().toString().trim()));
                        }
                        PwdDialog dialog = new PwdDialog(mContext,UpdatePwdActivity.this,true,"");//true成功 false失败
                        dialog.show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    PwdDialog dialog = new PwdDialog(mContext,UpdatePwdActivity.this,false,"密码修改失败");//true成功 false失败
                    dialog.show();
                }
            }

            @Override
            public void onError(VolleyError error) {
                ok.setEnabled(true);
                PwdDialog dialog = new PwdDialog(mContext,UpdatePwdActivity.this,false,"密码修改失败");//true成功 false失败
                dialog.show();
            }
        });
    }

    private void dealUi(){
        boolean flag = true;
        if(mOldPwd.getText().toString().length()>0){
            mOClear.setVisibility(View.VISIBLE);
        }else{
            mOClear.setVisibility(View.GONE);
            flag = false;
        }
        if(mNewPwd.getText().toString().length()>0){
            mNClear.setVisibility(View.VISIBLE);
        }else{
            mNClear.setVisibility(View.GONE);
            flag = false;
        }
        if(mNewRepwd.getText().toString().length()>0){
            mNRClear.setVisibility(View.VISIBLE);
        }else{
            mNRClear.setVisibility(View.GONE);
            flag = false;
        }

        if(flag){
            ok.setEnabled(true);
            ok_text.setTextColor(Color.rgb(255,255,255));
            ok.setBackgroundResource(R.drawable.border_radius_uppwd2);
        }else{
            ok.setEnabled(false);
            ok_text.setTextColor(Color.rgb(166,193,248));
            ok.setBackgroundResource(R.drawable.border_radius_uppwd2_noentry);
        }
    }
}
