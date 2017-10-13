package com.dk.mp.csyxy.ui.hy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.dk.mp.core.dialog.MsgDialog;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.core.widget.ListRadioDialog;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.http.Manager;
import com.dk.mp.csyxy.ui.hy.adapter.KsAdapter;
import com.dk.mp.csyxy.ui.hy.db.YellowPageDBHelper;
import com.dk.mp.core.entity.Ks;
import com.dk.mp.csyxy.ui.hy.db.YellowRealmHelper;

import org.json.JSONObject;


/**
 * @since 
 * @version 2013-4-2
 * @author wwang
 */
public class HyKsActivity extends MyActivity {
	private List<Ks> list = new ArrayList<>();
	private String bmid,bmname;//群组或部门或跟人分组的id
	private ListView listView;
	private KsAdapter adapter;
	private YellowRealmHelper yellowRealmHelper;

	private ErrorLayout errorLayout;

	@Override
	protected int getLayoutID() {
		return R.layout.ac_hy_ks;
	}

	@Override
	protected void initialize() {
		super.initialize();

		yellowRealmHelper = new YellowRealmHelper(mContext);

		bmid = getIntent().getStringExtra("id");
		bmname = getIntent().getStringExtra("name");

		setTitle(bmname);
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		initViews();
		if(DeviceUtil.checkNet()){
			getPeopleList();
		}else{
			getList();
		}
	}

	/**
	 * 初始化控�?
	 */
	private void initViews() {
		errorLayout = (ErrorLayout) findViewById(R.id.error_layout);
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				final ListRadioDialog l = new ListRadioDialog(mContext);

				if (list.get(position).getTels() != null){
					final String[] str = list.get(position).getTels().split("/");

					l.show(str,new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							l.cancel();
							DeviceUtil.call(mContext, str[position]);
						}
					});
				}
				/*final String[] str = getTel(adapter.getItem(position).getValues());
				if (str.length > 0) {
					final ListRadioDialog l = new ListRadioDialog(mContext);
					l.show(str, new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							l.cancel();
							DeviceUtil.call(mContext, list.get(position).getTels());
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
	 * 获取联系人列�?
	 */
	private void getList() {
//		list = new YellowPageDBHelper(mContext).getPersonListByDepart(bmid);
		list = yellowRealmHelper.queryPersonsByDepartmentid(bmid);
		if (list.size() > 0) {
			errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
			adapter = new KsAdapter(mContext, list);
			listView.setAdapter(adapter);
			MsgDialog.show(mContext, mContext.getString(R.string.net_no2));
		}else{
			errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (adapter == null) {
					adapter = new KsAdapter(mContext, list);
					listView.setAdapter(adapter);
				} else {
					adapter.setList(list);
					adapter.notifyDataSetChanged();
				}
				break;
			case 1:
				if (adapter == null) {
					adapter = new KsAdapter(mContext, list);
					listView.setAdapter(adapter);
				} else {
					adapter.setList(list);
					adapter.notifyDataSetChanged();
				}
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 初始化列表.
	 * @return List<App>
	 */
	public void getPeopleList() {
		if (DeviceUtil.checkNet()) {
			errorLayout.setErrorType(ErrorLayout.LOADDATA);
			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("id", bmid);
			HttpUtil.getInstance().postJsonObjectRequest("apps/yellowpage/getKsList?id="+bmid, map, new HttpListener<JSONObject>() {
				@Override
				public void onSuccess(JSONObject result) {
					try {
						if (result.getInt("code") != 200) {
							errorLayout.setErrorType(ErrorLayout.DATAFAIL);
						}else{
							errorLayout.setErrorType(ErrorLayout.HIDE_LAYOUT);
							String json =  result.getJSONArray("data").toString();
//							List<Ks> departments = new Gson().fromJson(json,new TypeToken<List<Ks>>(){}.getType());
							List<Ks> departments = Manager.getHy(result,bmid,bmname);
							list.addAll(departments);

							if (list.size()>0){
//								new YellowPageDBHelper(mContext).insertPeopleList(id, list);
								yellowRealmHelper.deletePersonsByDepartmentid(bmid);
								yellowRealmHelper.addPersons(departments);
								handler.sendEmptyMessage(1);
							}else {
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
	}

}
