package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemSmallTypeAdapter;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.TypeSmallData;
import com.xiaogang.myapp.model.GoodsTypeSmall;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/22.
 */
public class MineSmallTypeActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private ListView lstv;
    private String id;
    private String name;
    ItemSmallTypeAdapter adapter;
    private List<GoodsTypeSmall> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minesmalltypeactivity);
        id = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");
        initView();
        getSmallType();
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemSmallTypeAdapter(list, MineSmallTypeActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(MineSmallTypeActivity.this, SerachGoodsActivity.class);
                GoodsTypeSmall goodsTypeSmall = list.get(arg2);
                intent.putExtra("id", goodsTypeSmall.getId());
                intent.putExtra("name", goodsTypeSmall.getNav_name());
                startActivity(intent);
            }
        });
        title.setText(name);
    }

    @Override
    public void onClick(View view) {

    }

    private void getSmallType() {
        String uri = InternetURL.CHILD_NAV_URL +"?id=" + id;
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
                                    TypeSmallData data = getGson().fromJson(s, TypeSmallData.class);
                                    list.clear();
                                    list.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(MineSmallTypeActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(MineSmallTypeActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MineSmallTypeActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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


    public void back(View view){
        finish();
    }

}
