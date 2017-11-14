package com.dk.mp.csyxy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.ui.password.UpdatePwdActivity;

/**
 * 修改密码提示框
 * 作者：janabo on 2017/11/7 15:11
 */
public class PwdDialog {
    Context context;
    Dialog dialog;
    UpdatePwdActivity activity;
    ImageView mImg;//提示信息图标
    TextView mTxt;//提示信息文字
    TextView ok_text;//确定按钮里面的文字
    LinearLayout ok;//确定按钮
    private TextView mErrorMsg;
    boolean flag;
    public PwdDialog(final Context context, final UpdatePwdActivity activity, final boolean flag,String eMsg) {
        this.context = context;
        this.activity = activity;
        this.flag = flag;
        dialog = new Dialog(context, R.style.MyDialog);
        Window window = dialog.getWindow();
        window.setContentView(R.layout.mp_updatepwd_dialog);
        mImg = (ImageView) window.findViewById(R.id.img_pwdinfo);
        mTxt = (TextView) window.findViewById(R.id.txt_pwdinfo);
        ok_text = (TextView) window.findViewById(R.id.ok_text);
        ok = (LinearLayout) window.findViewById(R.id.ok);
        mErrorMsg = (TextView) window.findViewById(R.id.error_msg);

        if(!flag){
            mImg.setImageResource(R.mipmap.pwd_fail);
            mTxt.setText("密码修改失败！");
            mErrorMsg.setText(eMsg);
            ok_text.setText("再来一次");
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
                if(flag) {
                    activity.finish();
                }
            }
        });
    }

    public void show() {
        dialog.show();
    }

    public void hide() {
        if (dialog != null) {
            dialog.cancel();
        }
    }
}
