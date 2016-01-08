package com.xiaogang.myapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.SuccessData;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/31.
 * �޸�����
 */
public class ResetPwrActivity extends com.xiaogang.myapp.bean.BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;
    private Button button_sub;
    private EditText pwr_one;
    private EditText pwr_two;
    private EditText pwr_three;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpwr_activity);
        initView();
    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);
        button_sub = (Button) this.findViewById(R.id.button_sub);
        button_sub.setOnClickListener(this);
        pwr_one = (EditText) this.findViewById(R.id.pwr_one);
        pwr_two = (EditText) this.findViewById(R.id.pwr_two);
        pwr_three = (EditText) this.findViewById(R.id.pwr_three);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftbutton:
                finish();
                break;
            case R.id.button_sub:
                //�ύ
                if (StringUtil.isNullOrEmpty(pwr_one.getText().toString()) || StringUtil.isNullOrEmpty(pwr_two.getText().toString())
                    || StringUtil.isNullOrEmpty(pwr_three.getText().toString())){
                    Toast.makeText(ResetPwrActivity.this, R.string.please_pwr_error_one, Toast.LENGTH_SHORT).show();
                    return;
                }
                reset();
                break;
        }
    }


     /**
      * 更改密码
    */
    private void reset() {
        String uri =  InternetURL.UPDATE_PWR_URL + "?uid="+getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token="+ getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&pass=" + pwr_one.getText().toString()
                +"&newpass=" + pwr_two.getText().toString()
                +"&notpass=" + pwr_three.getText().toString();
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
                                    Toast.makeText(ResetPwrActivity.this, R.string.update_pwr_success, Toast.LENGTH_SHORT).show();
                                    save("password" , pwr_two.getText().toString());
                                    finish();
                                }else if (Integer.parseInt(code1) == 401){
                                    Toast.makeText(ResetPwrActivity.this, R.string.msg_pwr_error_one, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ResetPwrActivity.this, R.string.please_pwr_error_two, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(ResetPwrActivity.this, R.string.please_pwr_error_two, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(ResetPwrActivity.this, R.string.please_pwr_error_two, Toast.LENGTH_SHORT).show();
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
