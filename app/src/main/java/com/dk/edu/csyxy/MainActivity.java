package com.dk.edu.csyxy;

import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.dk.edu.core.dialog.AlertDialog;
import com.dk.edu.core.ui.MyActivity;
import com.dk.edu.csyxy.fragment.NavFragment;
import com.dk.edu.csyxy.fragment.NavigationButton;

/**
 * 主页面
 */
public class MainActivity extends MyActivity implements NavFragment.OnNavigationReselectListener {

    FrameLayout mMainUi;
    private NavFragment mNavBar;

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
