package com.xiaogang.myapp.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemGoodsSuggestAdapter;
import com.xiaogang.myapp.adapter.OnClickContentItemListener;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.GoodsSuggestData;
import com.xiaogang.myapp.library.PullToRefreshBase;
import com.xiaogang.myapp.library.PullToRefreshListView;
import com.xiaogang.myapp.model.Chengdu;
import com.xiaogang.myapp.model.GoodsSuggest;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/2.
 * �ü����
 */
public class GoodsYjyjActivity extends com.xiaogang.myapp.bean.BaseActivity implements View.OnClickListener ,OnClickContentItemListener{
    private ImageView leftbutton;

    private PullToRefreshListView lstv;
    private ItemGoodsSuggestAdapter adapter;
    private List<GoodsSuggest> lists = new ArrayList<GoodsSuggest>();

    private int pageIndex = 0;
    private static boolean IS_REFRESH = true;
    private String id;
    private String name;

    private int positionTmp;
    private TextView goods_name;


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        id = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");
        setContentView(R.layout.goods_yongjiayijian_activity);
        initView();

        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.check_new_version).toString();
        progressDialog = new ProgressDialog(GoodsYjyjActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getData();
    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        goods_name = (TextView) this.findViewById(R.id.goods_name);
        leftbutton.setOnClickListener(this);
        goods_name.setText(name);
        adapter = new ItemGoodsSuggestAdapter(lists, GoodsYjyjActivity.this);
        adapter.setOnClickContentItemListener(this);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });

        lstv.setAdapter(adapter);

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                GoodsSuggest good = lists.get(position);
//                Intent detailView = new Intent(GoodsYjyjActivity.this, DetailGoodsActivity.class);
//                detailView.putExtra("id", good.getId());
//                startActivity(detailView);
            }
        });
    }

    private void getData() {
        String uri = InternetURL.COMMEND_SHOW_URL +"?uid="+getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token="+getGson().fromJson(getSp().getString("access_token", ""), String.class)+"&goodsid="+id;
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
                                    GoodsSuggestData data = getGson().fromJson(s, GoodsSuggestData.class);
                                    if (IS_REFRESH) {
                                        lists.clear();
                                    }
                                    lists.addAll(data.getData());
                                    lstv.onRefreshComplete();
                                    adapter.notifyDataSetChanged();
                                }else if(Integer.parseInt(code1) == 201){
                                    Toast.makeText(GoodsYjyjActivity.this, R.string.no_pingjia, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(GoodsYjyjActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(GoodsYjyjActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(GoodsYjyjActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftbutton:
                finish();
                break;
        }
    }

    GoodsSuggest goodsSuggest ;

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        goodsSuggest = lists.get(position);
        positionTmp = position;
        switch (flag){
            case 1:
                //支持
                zhichi(goodsSuggest.getId());
                break;
            case 2:
                //反对
                fandui(goodsSuggest.getId());
                break;
        }
    }

    private void zhichi(final String id) {
        String uri = InternetURL.COMMEND_PROVE_URL+"?uid="+getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token=" +getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&id=" + id;
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
                                    if(!StringUtil.isNullOrEmpty(lists.get(positionTmp).getApprove())){
                                        lists.get(positionTmp).setApprove(String.valueOf(Integer.parseInt(lists.get(positionTmp).getApprove())+1));
                                    }else {
                                        lists.get(positionTmp).setApprove("1");
                                    }
                                    adapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(GoodsYjyjActivity.this, R.string.comment_error, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(GoodsYjyjActivity.this, R.string.comment_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(GoodsYjyjActivity.this, R.string.comment_error, Toast.LENGTH_SHORT).show();
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

    private void fandui(final String id) {
        String uri = InternetURL.COMMEND_OPPOSE_URL+"?uid="+getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token=" +getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&id=" + id;
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
                                    if(!StringUtil.isNullOrEmpty(lists.get(positionTmp).getOppose())){
                                        lists.get(positionTmp).setOppose(String.valueOf(Integer.parseInt(lists.get(positionTmp).getOppose()) + 1));
                                    }else {
                                        lists.get(positionTmp).setOppose("1");
                                    }
                                    adapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(GoodsYjyjActivity.this, R.string.comment_error, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(GoodsYjyjActivity.this, R.string.comment_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(GoodsYjyjActivity.this, R.string.comment_error, Toast.LENGTH_SHORT).show();
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

    public void write(View view){
        //添加评论
        Intent intent = new Intent(GoodsYjyjActivity.this, AddPlActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        startActivity(intent);
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("add_yongjiayijian_success")){
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }
        }

    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("add_yongjiayijian_success");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
