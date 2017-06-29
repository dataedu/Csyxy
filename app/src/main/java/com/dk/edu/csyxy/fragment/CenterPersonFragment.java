package com.dk.edu.csyxy.fragment;


import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dk.edu.core.dialog.AlertDialog;
import com.dk.edu.core.entity.User;
import com.dk.edu.core.ui.BaseFragment;
import com.dk.edu.core.ui.LoginActivity;
import com.dk.edu.core.util.BroadcastUtil;
import com.dk.edu.core.util.CoreSharedPreferencesHelper;
import com.dk.edu.core.util.FileUtil;
import com.dk.edu.core.util.SnackBarUtil;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.ui.AboutActivity;
import com.dk.edu.csyxy.ui.UserInfoActivity;

/**
 * 作者：janabo on 2017/6/7 16:34
 */
public  class CenterPersonFragment extends BaseFragment implements View.OnClickListener{
    private TextView name,xm,bmhyx;
    private LinearLayout login,feedback,version,cleanCache,about;
    private CoreSharedPreferencesHelper helper;
    private LinearLayout xsxx;
    SwitchCompat checkbox_settting;
    private ScrollView setting_scro;
    private ImageView pesonphoto;
    private TextView font_txt;

    @Override
    protected int getLayoutId() {
        return R.layout.mp_setting;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        helper = getSharedPreferences();
        login = (LinearLayout) root.findViewById(R.id.login);
        feedback = (LinearLayout) root.findViewById(R.id.feedback);
        version = (LinearLayout) root.findViewById(R.id.version);
        cleanCache = (LinearLayout) root.findViewById(R.id.cleanCache);
        about = (LinearLayout) root.findViewById(R.id.about);

        setting_scro = (ScrollView) root.findViewById(R.id.setting_scro);
        name = (TextView) root.findViewById(R.id.name);
        xsxx = (LinearLayout) root.findViewById(R.id.xsxx);
        xm = (TextView) root.findViewById(R.id.xm);
        bmhyx = (TextView) root.findViewById(R.id.bmhyx);
        pesonphoto = (ImageView) root.findViewById(R.id.photo);
        checkbox_settting = (SwitchCompat) root.findViewById(R.id.checkbox_settting);
        checkbox_settting.setChecked(helper.getBoolean("push_check"));
        checkbox_settting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SnackBarUtil.showShort(setting_scro,
                        isChecked?"开启推送设置，您将及时收到学校公布的各类重要消息！":"关闭推送设置，您将不会收到学校公布的各类重要消息！");
                helper.setBoolean("push_check",isChecked);
            }
        });

        BroadcastUtil.registerReceiver(getContext(), mRefreshBroadcastReceiver, new String[]{"login","user"});
        setUser();

        login.setOnClickListener(this);
        feedback.setOnClickListener(this);
        version.setOnClickListener(this);
        cleanCache.setOnClickListener(this);
        about.setOnClickListener(this);
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("user")) {
                setUser();
            }else if(action.equals("login")){
                Intent in = new Intent(context,LoginActivity.class);
                startActivity(in);
            }
        }
    };

    /**
     * 是否开启推送
     */
    public void topush(){
        if(checkbox_settting.isChecked()){
            SnackBarUtil.showShort(setting_scro,"关闭推送设置，您将不会收到学校公布的各类重要消息！");
            checkbox_settting.setChecked(false);
            helper.setBoolean("push_check",false);
        }else{
            SnackBarUtil.showShort(setting_scro,"开启推送设置，您将及时收到学校公布的各类重要消息！");
            checkbox_settting.setChecked(true);
            helper.setBoolean("push_check",true);
        }
    }

    /**
     * 清除缓存
     */
    public  void onClickCleanCache() {
        AlertDialog alert = new AlertDialog(getContext());
        alert.show("提示", "确定要清除缓存吗?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileUtil.delete(7);
                        SnackBarUtil.showShort(setting_scro,"清除缓存成功!");
                    }
                }).start();
            }
        });
    }

    /**
     * 关于
     */
    public void toabout(){
        Intent intent = new Intent(getContext(),AboutActivity.class);
        startActivity(intent);
    }

    /**
     * 登录 或者跳转到个人信息
     * @param
     */
    public void tologin(){
        User user = helper.getUser();
        if (user != null) {
            Intent intent = new Intent(getContext(), UserInfoActivity.class);
            ActivityOptions options = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), pesonphoto, "personphoto");
                ActivityCompat.startActivity(getActivity(),intent, options.toBundle());
            }else{
                startActivity(intent);
            }

        } else {
            Intent in = new Intent(getContext(),LoginActivity.class);
            startActivity(in);
        }
    }

    private void setUser() {
        User user = helper.getUser();
        if (user != null) {
            name.setVisibility(View.GONE);
            xsxx.setVisibility(View.VISIBLE);
            xm.setText(user.getUserName());
            helper.setValue("userIdDes",user.getUserIdDes());
            String departname = "null".equals(user.getDepartName())||user.getDepartName() == null ? "":user.getDepartName();
            if("1".equals(user.getRoles())){
                bmhyx.setText("部门："+departname);
            }else{
                bmhyx.setText("院系："+departname);
            }
        } else {
            name.setText("点击登录");
            name.setVisibility(View.VISIBLE);
            xsxx.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login:
                tologin();
                break;
            case R.id.feedback:
                topush();
                break;
            case R.id.version:
                break;
            case R.id.cleanCache:
                onClickCleanCache();
                break;
            case R.id.about:
                toabout();
                break;
        }
    }

    public CoreSharedPreferencesHelper getSharedPreferences() {
        if (helper == null){
            helper = new CoreSharedPreferencesHelper(getContext());
        }
        return helper;
    }
}
