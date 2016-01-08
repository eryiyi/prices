package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.AddBaojiaData;
import com.xiaogang.myapp.model.ShangdianType;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/30.
 */
public class AddBaojiaActivity extends com.xiaogang.myapp.bean.BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;
    private TextView title;
    private EditText prices;
    private EditText address;
    private EditText tel;
    private EditText info;
    private EditText goods_name;
    private EditText shuihuojiage;

    private String name;
    private String nav;
    private String gid;

    private Button sure;

    private String uid;
    private String access_token;
    private TextView goods_type;//商铺类型
    private ShangdianType shangdianType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_baojia_activity);
        name = getIntent().getExtras().getString("name");
        nav = getIntent().getExtras().getString("nav");
        gid = getIntent().getExtras().getString("gid");

        uid = getGson().fromJson(getSp().getString("uid", ""), String.class);
        access_token = getGson().fromJson(getSp().getString("access_token", ""), String.class);
        initView();
    }

    private void initView() {
        goods_type = (TextView) this.findViewById(R.id.goods_type);
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        prices = (EditText) this.findViewById(R.id.prices);
        tel = (EditText) this.findViewById(R.id.tel);
        address = (EditText) this.findViewById(R.id.address);
        shuihuojiage = (EditText) this.findViewById(R.id.shuihuojiage);
        info = (EditText) this.findViewById(R.id.info);
        goods_name = (EditText) this.findViewById(R.id.goods_name);
        sure = (Button) this.findViewById(R.id.sure);
        sure.setOnClickListener(this);
        goods_type.setOnClickListener(this);
        title.setText(name);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftbutton:
                finish();
                break;
            case R.id.sure:
                if(StringUtil.isNullOrEmpty(shuihuojiage.getText().toString()) || StringUtil.isNullOrEmpty(prices.getText().toString())){
                    Toast.makeText(AddBaojiaActivity.this, R.string.qingshurrubaojiaxinxi, Toast.LENGTH_SHORT).show();
                    return;
                }
                addBaojia();
                break;
            case R.id.goods_type:
            {
                Intent intent = new Intent(AddBaojiaActivity.this, GoodsTypeActivity.class);
                startActivityForResult(intent,0);
            }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 001){
            shangdianType = (ShangdianType) data.getExtras().get("shangdianType");
            if(shangdianType != null){
                goods_type.setText(shangdianType.getNav_name());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void addBaojia(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.ADD_BAOJIA_URL+"?uid=" +uid+"&gid="+gid +"&access_token="+access_token
                +"&hh="+prices.getText().toString()+"&sh=" + shuihuojiage.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
//                                    AddBaojiaData data = getGson().fromJson(s, AddBaojiaData.class);
                                    Toast.makeText(AddBaojiaActivity.this, R.string.baojia_success, Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(AddBaojiaActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(AddBaojiaActivity.this, R.string.baojia_fail, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(AddBaojiaActivity.this, R.string.baojia_fail, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("uid", uid);
//                params.put("gid", gid);
//                params.put("access_token", access_token);
//                params.put("name", goods_name.getText().toString());
//                params.put("navid", shangdianType.getId());
//                params.put("mobile", tel.getText().toString());
//                params.put("address", address.getText().toString());
//                params.put("info", info.getText().toString());
//                params.put("hh", prices.getText().toString());
//                params.put("sh", shuihuojiage.getText().toString());
//                params.put("goodsname", name);
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
