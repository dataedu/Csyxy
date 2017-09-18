package com.dk.mp.csyxy.ui.xyfg;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.http.request.HttpListener;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.ui.xyfg.entity.SceneryEntity;
import com.dk.mp.csyxy.ui.xyfg.view.org.ImageLoaderCompat;
import com.dk.mp.csyxy.ui.xyfg.view.org.WaterfallSmartView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cobb on 2017/9/15.
 */

public class SceneryListActivity2 extends MyActivity implements AdapterView.OnItemClickListener {

    private PhotoAdapter mAdapter;
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private WaterfallSmartView mWaterfall;

    private List<SceneryEntity> list = new ArrayList<SceneryEntity>();
    private ErrorLayout errorLayout;

    @Override
    protected int getLayoutID() {
        return R.layout.app_scenery_listview2;
    }

    @Override
    protected void initialize() {
        super.initialize();

        setTitle(getIntent().getStringExtra("title"));

        errorLayout = (ErrorLayout) findViewById(R.id.error_layout);

        mAdapter = new PhotoAdapter(this);
        mWaterfall = (WaterfallSmartView) findViewById(R.id.waterfall);
        mWaterfall.setAdapter(mAdapter);
        mWaterfall.setOnItemClickListener(this);

        mImageLoader = ImageLoaderCompat.getInstance(this);
        mOptions = ImageLoaderCompat.getOptions();

        if(DeviceUtil.checkNet()) {
            initDatas();
        }else{
            errorLayout.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    private void initDatas(){
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

                        if (list.size()>0){
                            loadUrlSlow(list);
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

    private void loadUrlSlow(List<SceneryEntity> list) {
        long time = 0L;
        for (final SceneryEntity url : list) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageLoader.loadImage(url.getThumb(), mImageLoadingListener);
                }
            }, time);
            time += 1000L;
        }
    }

    ImageLoadingListener mImageLoadingListener = new ImageLoadingListener() {

        @Override
        public void onLoadingStarted(String imageUri, View view) {
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        }

        @Override
        public void onLoadingComplete(final String imageUri, View view, Bitmap loadedImage) {
            mAdapter.add(imageUri, loadedImage.getWidth(), loadedImage.getHeight());
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,SceneryDetailsActivity.class);
        intent.putExtra("list", new Gson().toJson(list));
        intent.putExtra("title", (position+1)+"/"+list.size());
        intent.putExtra("index", position);
        startActivity(intent);
    }

    class PhotoAdapter extends ArrayAdapter<String> {
        private LayoutInflater inflater;

        public PhotoAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.photo_item, parent, false);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            mImageLoader.displayImage((String) getItem(position), holder.imageView, mOptions);
            return convertView;
        }

        public void add(String object, int weight, int height) {
            super.add(object);
            // AddItem to waterfall in same time
            mWaterfall.addItem(object, weight, height);
        }

    }

    class ViewHolder {
        ImageView imageView;
    }

}
