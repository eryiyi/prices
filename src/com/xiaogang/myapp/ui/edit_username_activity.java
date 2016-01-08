package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/31.
 */
public class edit_username_activity extends BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;
    private TextView publish_user;
    private EditText content;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = getGson().fromJson(getSp().getString("user", ""), String.class);
        setContentView(R.layout.edit_username_activity);
        initView();
        if(!StringUtil.isNullOrEmpty(user) && content !=null){
            content.setText(user);
        }
    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        publish_user = (TextView) this.findViewById(R.id.publish_user);
        content = (EditText) this.findViewById(R.id.content);
        leftbutton.setOnClickListener(this);
        publish_user.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftbutton:
                finish();
                break;
            case R.id.publish_user:
                if(StringUtil.isNullOrEmpty(content.getText().toString())){
                    Toast.makeText(edit_username_activity.this, R.string.user_error_one, Toast.LENGTH_SHORT).show();
                    return;
                }
                updateName();
                break;
        }
    }

    private void updateName() {
        String userStr = content.getText().toString();
        try {
            userStr = URLEncoder.encode(userStr ,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        String  uri=  InternetURL.UPDATE_USER_URL +"?uid="+ getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token="+ getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&user="+ userStr;
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
                                    save("user", content.getText().toString());
                                    Intent intent1 = new Intent("set_user");
                                    sendBroadcast(intent1);
                                    Toast.makeText(edit_username_activity.this, R.string.set_user_success, Toast.LENGTH_SHORT).show();
                                    finish();
                                }else if(Integer.parseInt(code1) == 401){
                                    Toast.makeText(edit_username_activity.this, R.string.set_user_error_one, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(edit_username_activity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(edit_username_activity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(edit_username_activity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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
