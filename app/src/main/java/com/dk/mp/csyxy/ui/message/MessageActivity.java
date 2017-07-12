package com.dk.mp.csyxy.ui.message;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.csyxy.R;

;

/**
 * @since 
 * @version 2013-2-18
 * @author wwang
 */
public class MessageActivity extends MyActivity {
	public Context mContext;
	private ErrorLayout error_layout;
	private Button back;

	@Override
	protected int getLayoutID() {
		return R.layout.mp_message_main;
	}

	@Override
	protected void initialize() {
		super.initialize();
		setTitle("消息");
		initViews();
	}

	private void initViews() {
		back = (Button) findViewById(R.id.back);
		mContext = MessageActivity.this;
		error_layout = (ErrorLayout) findViewById(R.id.error_layout);
		if(DeviceUtil.checkNet()){
			error_layout.setErrorType(ErrorLayout.NODATA);
		}else{
			error_layout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				back();
			}
		});
	}
}
