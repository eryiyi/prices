package com.xiaogang.myapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemTypeAdapter;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.FavourData;
import com.xiaogang.myapp.data.ShangdianTypeData;
import com.xiaogang.myapp.model.ShangdianType;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/30.
 * …ÃµÍ¿‡–Õ
 */
public class GoodsTypeActivity extends BaseActivity implements View.OnClickListener {
    private ListView lstv;
    private ItemTypeAdapter adapter;
    List<ShangdianType>  lists = new ArrayList<>();

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangdiantype_activity);

        lstv = (ListView) this.findViewById(R.id.lstv);
        adapter = new ItemTypeAdapter(lists, GoodsTypeActivity.this);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ShangdianType shangdianType = lists.get(i);
                Intent intent = new Intent(GoodsTypeActivity.this, OpenActivity.class);
                intent.putExtra("shangdianType", shangdianType);
                setResult(001,intent);
                finish();
            }
        });

        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.check_new_version).toString();
        progressDialog = new ProgressDialog(GoodsTypeActivity.this);
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

    private void getData() {
        String  uri=  InternetURL.GET_NAV_URL
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
                                    ShangdianTypeData data = getGson().fromJson(s, ShangdianTypeData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                } else if(Integer.parseInt(code1) == 201){
                                    Toast.makeText(GoodsTypeActivity.this, R.string.has_no_data, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(GoodsTypeActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(GoodsTypeActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(GoodsTypeActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
