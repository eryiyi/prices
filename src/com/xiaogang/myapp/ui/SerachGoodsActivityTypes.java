package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemSearchGoodsByTypeAdapter;
import com.xiaogang.myapp.adapter.ItemtypesAdapter;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.GoodsTypeBeanData;
import com.xiaogang.myapp.data.GoodsTypeBeansData;
import com.xiaogang.myapp.data.SearchGoodsData;
import com.xiaogang.myapp.library.PullToRefreshBase;
import com.xiaogang.myapp.library.PullToRefreshListView;
import com.xiaogang.myapp.model.GoodsTypeBean;
import com.xiaogang.myapp.model.GoodsTypeBeans;
import com.xiaogang.myapp.model.SearchGoods;
import com.xiaogang.myapp.util.upload.BaseTools;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.ColumnHorizontalScrollView;
import com.xiaogang.myapp.widget.SelectPhoPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/29.
 */
public class SerachGoodsActivityTypes extends BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;

    private ListView lstv;
    private ItemtypesAdapter adapter;
    private List<String> lists = new ArrayList<>();
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_goods__by_type_activity);
        id =  getIntent().getExtras().getString("id");
        initView();
        getDataTypeById();
    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);
        lstv = (ListView) this.findViewById(R.id.lstv);

        adapter = new ItemtypesAdapter(lists, SerachGoodsActivityTypes.this);
        lstv.setAdapter(adapter);

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String str = lists.get(position);
                Intent intent = new Intent();
                intent.putExtra("str", str);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftbutton:
                finish();
                break;
        }
    }

    private void getDataTypeById() {
        String uri = InternetURL.UPDATE_ALLATTR_URL + "?id="+id ;
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
                                    GoodsTypeBeansData data = getGson().fromJson(s, GoodsTypeBeansData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(SerachGoodsActivityTypes.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SerachGoodsActivityTypes.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SerachGoodsActivityTypes.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
