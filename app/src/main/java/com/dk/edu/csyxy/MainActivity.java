package com.dk.edu.csyxy;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dk.edu.core.dialog.AlertDialog;
import com.dk.edu.core.ui.MyActivity;
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

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mMainUi = (FrameLayout) findViewById(R.id.activity_main_ui);
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
    }

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
}
