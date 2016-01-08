package com.xiaogang.myapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemFavourAdapter;
import com.xiaogang.myapp.adapter.ItemZuijinllAdapter;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.FavourCountData;
import com.xiaogang.myapp.data.FavourData;
import com.xiaogang.myapp.model.Deng;
import com.xiaogang.myapp.model.Favour;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/20.
 */
public class MineFavourActivity extends BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;
    private ListView lstv;
    private List<Favour> lists;
    private ItemFavourAdapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favour_activity);
        initView();
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.check_new_version).toString();
        progressDialog = new ProgressDialog(MineFavourActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getFavour();
    }

    private void initView() {
        lists = new ArrayList<>();
        adapter = new ItemFavourAdapter(lists, MineFavourActivity.this);
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        lstv = (ListView) this.findViewById(R.id.lstv);
        lstv.setAdapter(adapter);
        leftbutton.setOnClickListener(this);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Favour favour  = lists.get(i);
                Intent intent = new Intent(MineFavourActivity.this, DetailGoodsActivity.class);
                intent.putExtra("id", favour.getId());
                startActivity(intent);
            }
        });
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

    private void getFavour() {
        String  uri=  InternetURL.FAVOUR_LSTV_URL +"?uid="+ getGson().fromJson(getSp().getString("uid", ""), String.class)
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
                                    FavourData data = getGson().fromJson(s, FavourData.class);
                                    lists.clear();
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                } else if(Integer.parseInt(code1) == 201){
                                    Toast.makeText(MineFavourActivity.this, R.string.has_no_data, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(MineFavourActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(MineFavourActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MineFavourActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
