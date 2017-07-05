package com.dk.edu.csyxy;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dk.edu.core.dialog.AlertDialog;
import com.dk.edu.core.receiver.NetworkConnectChangedReceiver;
import com.dk.edu.core.ui.MyActivity;
import com.dk.edu.core.util.BroadcastUtil;
import com.dk.edu.core.util.DeviceUtil;
import com.dk.edu.csyxy.fragment.NavFragment;
import com.dk.edu.csyxy.fragment.NavigationButton;
import com.dk.edu.csyxy.ui.message.MessageActivity;

/**
 * 主页面
 */
public class MainActivity extends MyActivity implements NavFragment.OnNavigationReselectListener {

    FrameLayout mMainUi;
    private NavFragment mNavBar;
    private ImageView oa_search;

    private LinearLayout warn_main;//是否有网络

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mMainUi = (FrameLayout) findViewById(R.id.activity_main_ui);
        warn_main = (LinearLayout) findViewById(R.id.warn_main);
        FragmentManager manager = getSupportFragmentManager();
        mNavBar = (NavFragment)manager.findFragmentById(R.id.fag_nav);
        mNavBar.setup(this, manager, R.id.main_container, this);
        oa_search = (ImageView) findViewById(R.id.oa_search);
        oa_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
            }
        });

        BroadcastUtil.registerReceiver(this, mRefreshBroadcastReceiver, new String[]{"checknetwork_true","checknetwork_false"});

    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("NewApi") @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("checknetwork_true")) {
                warn_main.setVisibility(View.GONE);
                BroadcastUtil.sendBroadcast(context,"ref_headerview");
            }
            if(action.equals("checknetwork_false")){
                warn_main.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog alertDialog = new AlertDialog(mContext);
            alertDialog.exitApp();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onReselect(NavigationButton navigationButton) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(DeviceUtil.checkNet()){
            warn_main.setVisibility(View.GONE);
        }else{
            warn_main.setVisibility(View.VISIBLE);
        }
    }
}
