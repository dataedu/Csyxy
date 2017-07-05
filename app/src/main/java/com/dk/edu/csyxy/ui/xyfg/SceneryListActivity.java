package com.dk.edu.csyxy.ui.xyfg;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.android.volley.VolleyError;
import com.dk.edu.core.http.HttpUtil;
import com.dk.edu.core.http.request.HttpListener;
import com.dk.edu.core.ui.MyActivity;
import com.dk.edu.core.util.DeviceUtil;
import com.dk.edu.core.widget.ErrorLayout;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.ui.xyfg.adapter.SceneryGridAdapter;
import com.dk.edu.csyxy.ui.xyfg.entity.SceneryEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneryListActivity extends MyActivity implements OnItemClickListener{
	
	private List<SceneryEntity> list = new ArrayList<SceneryEntity>();
	private GridView scenerygridview;
	private SceneryGridAdapter adapter;

	private Gson gson = new Gson();

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.app_scenery_listview;
	}

	@Override
	protected void initView() {
		super.initView();

		setTitle(getIntent().getStringExtra("title"));

		initViews();

		if(DeviceUtil.checkNet()) {
			initDatas();
		}else{
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}

	private void initViews(){
		scenerygridview = (GridView) findViewById(R.id.scenerygridview);
		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);
		adapter = new SceneryGridAdapter(this, list);
		scenerygridview.setAdapter(adapter);
		scenerygridview.setOnItemClickListener(this);

		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	private void initDatas(){
		errorLayout.setErrorType(ErrorLayout.LOADDATA);
		Map<String, Object> map = new HashMap<String, Object>();
		String idType = getIntent().getStringExtra("type");
		HttpUtil.getInstance().postJsonObjectRequest("apps/xyfg/getImageList?idType="+idType, map, new HttpListener<JSONObject>() {
			@Override
			public void onSuccess(JSONObject result)  {
				try {
					if (result.getInt("code") != 200) {
						errorLayout.setErrorType(ErrorLayout.DATAFAIL);
					}else{
						errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
						list.clear();
						String json =  result.getJSONArray("data").toString();
						List<SceneryEntity> list1 = new Gson().fromJson(json,new TypeToken<List<SceneryEntity>>(){}.getType());
						list.addAll(list1);
						if (list.size() > 0){
							adapter.notifyDataSetChanged();
						}else {
							scenerygridview.setVisibility(View.GONE);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this,SceneryDetailsActivity.class);
		intent.putExtra("list", gson.toJson(list));
		intent.putExtra("title", (position+1)+"/"+list.size());
		intent.putExtra("index", position);
		startActivity(intent);
	}
}
