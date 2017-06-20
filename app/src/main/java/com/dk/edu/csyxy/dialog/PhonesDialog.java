package com.dk.edu.csyxy.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dk.edu.core.entity.Jbxx;
import com.dk.edu.core.entity.LoginMsg;
import com.dk.edu.core.entity.XbPersons;
import com.dk.edu.core.util.BroadcastUtil;
import com.dk.edu.core.util.CoreSharedPreferencesHelper;
import com.dk.edu.core.util.DeviceUtil;
import com.dk.edu.core.util.StringUtils;
import com.dk.edu.core.view.RecycleViewDivider;
import com.dk.edu.csyxy.R;
import com.dk.edu.csyxy.db.CursorDBHelper;
import com.dk.edu.csyxy.db.RealmHelper;

import static com.dk.edu.core.http.HttpUtil.mContext;


/**
 * 选择电话号码弹出框
 * 作者：janabo on 2016/12/23 17:17
 */
public class PhonesDialog {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS};
    Activity activity;
    Context context;
    Dialog dialog;
    RecyclerView mRecyclerView;
    TextView name,department;
    Button cancel;
    RadioAdapter radioAdapter;
    ImageView mXb,mExpro;
    LinearLayout expro_lin,xb_lin;
    RealmHelper realmHelper;
    Jbxx jbxx;
    final String finalUid;
    public CoreSharedPreferencesHelper preference;

    public PhonesDialog(final Context context, final Activity activity){
        this.context = context;
        this.activity = activity;
        dialog = new Dialog(context, R.style.MyDialog);
        Window window = dialog.getWindow();
        window.setContentView(R.layout.app_phone_dialog);
        mRecyclerView = (RecyclerView) window.findViewById(R.id.mrecycler_view);
        cancel = (Button) window.findViewById(R.id.cancel_btn);
        name = (TextView) window.findViewById(R.id.name);
        department = (TextView) window.findViewById(R.id.department);
        mXb = (ImageView) window.findViewById(R.id.xb_img);
        mExpro = (ImageView) window.findViewById(R.id.expro_img);
        expro_lin = (LinearLayout) window.findViewById(R.id.expro_lin);
        xb_lin = (LinearLayout) window.findViewById(R.id.xb_lin);
        realmHelper = new RealmHelper(context);
        preference = new CoreSharedPreferencesHelper(context);
        LoginMsg loginMsg = preference.getLoginMsg();
        String uid = "guest";
        if(loginMsg != null){
            uid = loginMsg.getUid();
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cancel();
            }
        });
        finalUid = uid;
        xb_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(realmHelper.queryPersonsByKey(jbxx.getPrikey()+finalUid) !=null) {
                    mXb.setBackgroundResource(R.mipmap.icon_unsc);
                    realmHelper.deleteXbById(jbxx.getPrikey()+finalUid);
                } else {
                    mXb.setBackgroundResource(R.mipmap.icon_sc);
                    XbPersons persons = new XbPersons();
                    persons.setId(jbxx.getId());
                    persons.setDepartmentname(jbxx.getDepartmentname());
                    persons.setDepartmentid(jbxx.getDepartmentid());
                    persons.setName(jbxx.getName());
                    persons.setPrikey(jbxx.getPrikey()+finalUid);
                    persons.setPhones(jbxx.getPhones());
                    persons.setLoginname(finalUid);
                    realmHelper.addXb(persons);
                }
                BroadcastUtil.sendBroadcast(context, "txl_persons");
            }
        });
        expro_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(activity);
            }
        });
    }

    /**
     * 请求读写权限
     * @param activity
     */
    public void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_CONTACTS);
        int rePermission = ActivityCompat.checkSelfPermission(activity,Manifest.permission.READ_CONTACTS);

        if (permission != PackageManager.PERMISSION_GRANTED || rePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }else{
            expPhones();
        }
    }

    public void show(Jbxx j) {
        jbxx = j;
        name.setText(j.getName());
        department.setText(j.getDepartmentname());
        mRecyclerView.setHasFixedSize ( true );
        mRecyclerView.setLayoutManager ( new LinearLayoutManager( mContext ) );
        if(realmHelper.queryPersonsByKey(jbxx.getPrikey()+finalUid) !=null)
            mXb.setBackgroundResource(R.mipmap.icon_sc);
        else
            mXb.setBackgroundResource(R.mipmap.icon_unsc);
        String[] items=null;
        if(StringUtils.isNotEmpty(j.getPhones())){
            items = j.getPhones().split("/");
        }
        if(items != null) {
            radioAdapter = new RadioAdapter(context, items);
            mRecyclerView.setAdapter(radioAdapter);
            mRecyclerView.addItemDecoration(new RecycleViewDivider(mContext, LinearLayoutManager.HORIZONTAL, DeviceUtil.dip2px(mContext, 0.8f), Color.rgb(229, 229, 229)));
        }
        dialog.show();
    }

    public void cancel() {
        if (dialog != null) {
            dialog.cancel();
        }
    }

    /**
     * 导出电话簿
     */
    private void expPhones(){
        String phones = jbxx.getPhones();
        String phone = "";
        if(StringUtils.isNotEmpty(phones)){
            String[] items =  phones.split("/");
            for(int i=0;i<items.length;i++){
                if(i == 0) {
                    phone = items[i];
                }
                if(items[i].length() > phone.length() && phone.length() != 11){
                    phone = items[i];
                }else if(items[i].length() == 11){
                    phone = items[i];
                }
            }

            if (!CursorDBHelper.checkNumber(context, phone)) {
                CursorDBHelper.insertPerson(context, jbxx.getName(), items);
                mExpro.setImageResource(R.mipmap.icon_expr_success);
                Toast.makeText(context,"导入联系人成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context,"该联系人已经存在，无需重复导入",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context,"该联系人无电话号码",Toast.LENGTH_SHORT).show();
        }
    }

    class RadioAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private Context context;
        private String[] list;
        LayoutInflater inflater;

        public RadioAdapter(Context context, String[] list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.app_phone_dialog_item,parent,false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            String phone = list[position];
            ((MyViewHolder)holder).phone.setText(phone);
            if(phone.length() != 11){
                ((MyViewHolder)holder).sendmsg.setVisibility(View.GONE);
            }else{
                ((MyViewHolder)holder).sendmsg.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return list.length;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView phone;
            private ImageView sendmsg;
            private ImageView callphone;


            public MyViewHolder(View itemView) {
                super(itemView);
                phone = (TextView) itemView.findViewById(R.id.phone);
                sendmsg = (ImageView) itemView.findViewById(R.id.sendmsg);
                callphone = (ImageView) itemView.findViewById(R.id.callphone);

                sendmsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phone = list[getLayoutPosition()];
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });

                callphone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String phone = list[getLayoutPosition()];
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                });
            }
        }
    }


}
