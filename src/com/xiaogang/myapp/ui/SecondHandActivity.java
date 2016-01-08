package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemSecondAdapter;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.SecondGoodsData;
import com.xiaogang.myapp.library.PullToRefreshBase;
import com.xiaogang.myapp.library.PullToRefreshGridView;
import com.xiaogang.myapp.model.SecondGoods;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/27.
 * 二手
 */
public class SecondHandActivity extends BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;
    private PullToRefreshGridView lv_grideview;
    private ItemSecondAdapter adapter;
    private List<SecondGoods> lists = new ArrayList<>();
    private int pageIndex = 0;
    private static boolean IS_REFRESH = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondhand_activity);
        initView();
        getData();
    }

    private void initView() {

        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        lv_grideview = (PullToRefreshGridView) this.findViewById(R.id.lstv);
        adapter = new ItemSecondAdapter(lists, SecondHandActivity.this);

        lv_grideview.setMode(PullToRefreshBase.Mode.BOTH);
        lv_grideview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });

        lv_grideview.setAdapter(adapter);
        lv_grideview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SecondGoods secondGoods = lists.get(position);
                Intent detailView = new Intent(SecondHandActivity.this, SecondHandDetailActivity.class);
                detailView.putExtra("id", secondGoods.getId());
                startActivity(detailView);
            }
        });
        leftbutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.leftbutton:
                finish();
                break;
        }
    }

    public void getData() {
        String uri = InternetURL.SECOND_LSTV_URL+"?p=" + pageIndex;
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
                                    SecondGoodsData data = getGson().fromJson(s, SecondGoodsData.class);
                                    if (Integer.parseInt(data.getCode()) == 200) {
                                        if (IS_REFRESH) {
                                            lists.clear();
                                        }
                                        lists.addAll(data.getData());
                                        lv_grideview.onRefreshComplete();
                                        adapter.notifyDataSetChanged();
                                    }else if(Integer.parseInt(code1) == 201){
                                        Toast.makeText(SecondHandActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                                    }else if(Integer.parseInt(code1) == 402){
                                        Toast.makeText(SecondHandActivity.this, "没有数据了", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(SecondHandActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(SecondHandActivity.this, R.string.reg_error_four, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(SecondHandActivity.this, R.string.reg_error_four, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SecondHandActivity.this, R.string.reg_error_four, Toast.LENGTH_SHORT).show();
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

    public void findSearch(View view){
        Intent intent = new Intent(SecondHandActivity.this, SearchActivity.class);
        startActivity(intent);
    }
}
