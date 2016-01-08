package com.xiaogang.myapp.ui;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemGoodsBaojiaAdapter;
import com.xiaogang.myapp.adapter.OnClickContentItemListener;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.GoodDetailSalesBaojiaData;
import com.xiaogang.myapp.model.GoodDetailSalesBaojia;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.ContentListView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/6.
 * 附近商品
 */
public class DetailGoodsFujin extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private ListView lstv;
    private ItemGoodsBaojiaAdapter adapter;
    private List<GoodDetailSalesBaojia> listsgoods;
    private ProgressDialog progressDialog;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fujin_activity);
        id = getIntent().getExtras().getString("id");
        listsgoods = new ArrayList<GoodDetailSalesBaojia>();
        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemGoodsBaojiaAdapter(listsgoods, this);
        adapter.setOnClickContentItemListener(this);
        lstv.setAdapter(adapter);

        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.check_new_version).toString();
        progressDialog = new ProgressDialog(DetailGoodsFujin.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getData();
    }

    @Override
    public void onClick(View view) {

    }
    public void back(View view){
        finish();
    }

    //获得报价列表
    private void getData() {
        String uri = InternetURL.ALL_SALES_URL + "?id="+"2";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    GoodDetailSalesBaojiaData data = getGson().fromJson(s, GoodDetailSalesBaojiaData.class);
                                    if (Integer.parseInt(data.getCode()) == 200 && data.getData()!=null) {
                                            listsgoods.clear();
                                            listsgoods.addAll(data.getData());
                                            adapter.notifyDataSetChanged();
                                    }
                                }else if(Integer.parseInt(code) == 401){
                                    Toast.makeText(DetailGoodsFujin.this, R.string.has_no_data, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(DetailGoodsFujin.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DetailGoodsFujin.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(DetailGoodsFujin.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
    public void onClickContentItem(int position, int flag, Object object) {

    }
}
