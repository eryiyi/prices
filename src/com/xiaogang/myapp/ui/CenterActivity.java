package com.xiaogang.myapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.myapp.PricesApplication;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.AnimateFirstDisplayListener;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.FavourCountData;
import com.xiaogang.myapp.data.GoodsArticelData;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/28.
 * 个人中心
 */
public class CenterActivity extends com.xiaogang.myapp.bean.BaseActivity implements View.OnClickListener {
    private LinearLayout edit_emp;//编辑用户
    private LinearLayout open;//申请开店
    private ImageView head;
    private TextView name;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    private ImageView back;
    private TextView favourCount;

    private LinearLayout top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.center_activity);
        initView();
        initData();
        getFavour();
    }

    private void initView() {
        edit_emp = (LinearLayout) this.findViewById(R.id.edit_emp);
        open = (LinearLayout) this.findViewById(R.id.open);
        edit_emp.setOnClickListener(this);
        open.setOnClickListener(this);
        head = (ImageView) this.findViewById(R.id.head);
        name = (TextView) this.findViewById(R.id.name);
        head.setOnClickListener(this);
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        favourCount = (TextView) this.findViewById(R.id.favourCount);
        top = (LinearLayout) this.findViewById(R.id.top);
        top.setOnClickListener(this);
    }

    void  initData(){
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("face", ""), String.class))){
            imageLoader.displayImage(getGson().fromJson(getSp().getString("face", ""), String.class), head,
                    PricesApplication.txOptions, animateFirstListener);
        }
        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("user", ""), String.class))){
            name.setText(getGson().fromJson(getSp().getString("user", ""), String.class));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.open:
                Intent openView = new Intent(CenterActivity.this, OpenActivity.class);
                startActivity(openView);
                break;
            case R.id.edit_emp:
                Intent profileView = new Intent(CenterActivity.this, ProfileActivity.class);
                startActivity(profileView);
                break;
            case R.id.head:
                //设定
                break;
            case R.id.back:
                finish();
                break;
            case R.id.top:
                Intent favour = new Intent(CenterActivity.this, MineFavourActivity.class);
                startActivity(favour);
                break;
        }
    }


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("set_face")){
                initData();
            }
            if(action.equals("set_user")){
                initData();
            }
        }

    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("set_face");
        myIntentFilter.addAction("set_user");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }


    private void getFavour() {
        String  uri=  InternetURL.FAVOUR_COUNT_URL +"?uid="+ getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class)
                ;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    FavourCountData data = getGson().fromJson(s, FavourCountData.class);
                                    favourCount.setText(String.valueOf(data.getData()));
                                }else if(Integer.parseInt(code1) == 201){
                                    favourCount.setText("0");
                                }
                                else {
                                    Toast.makeText(CenterActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(CenterActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(CenterActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }


}
