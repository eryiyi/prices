package com.xiaogang.myapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.xiaogang.myapp.PricesApplication;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.CardData;
import com.xiaogang.myapp.data.CodeData;
import com.xiaogang.myapp.data.RegData;
import com.xiaogang.myapp.util.upload.HttpUtils;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/27.
 * 注册
 */
public class RegistActivity extends BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;//左侧返回按钮
    private TextView mobile_button;//手机注册按钮
    private TextView email_button;//youxiang注册按钮
    private ImageView mobile_line;
    private ImageView email_line;

    private LinearLayout reg_mobile;
    private LinearLayout reg_emial;
    int selectMethod = 0;//0是手机注册  1是邮箱注册

    private EditText mobile;
    private EditText card;
    private EditText pass;

    private EditText email;
    private EditText pwr;
    private EditText password;

    private Button button_login;
    private Button code_button;//获得验证码按钮
    boolean isMobileNet, isWifiNet;
    private ProgressDialog progressDialog;
    private String mobile_str;
    private String password_str;
    private String card_str;
    private TextView code;
    private String cid;
    private String cidMobile;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_activity);
        res = getResources();
        initView();
        getCode();
    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        mobile_button = (TextView) this.findViewById(R.id.mobile_button);
        email_button = (TextView) this.findViewById(R.id.email_button);
        code = (TextView) this.findViewById(R.id.code);
        mobile_line = (ImageView) this.findViewById(R.id.mobile_line);
        email_line = (ImageView) this.findViewById(R.id.email_line);

        reg_mobile = (LinearLayout) this.findViewById(R.id.reg_mobile);
        reg_emial = (LinearLayout) this.findViewById(R.id.reg_emial);

        reg_emial.setVisibility(View.GONE);
        leftbutton.setOnClickListener(this);
        email_button.setOnClickListener(this);
        mobile_button.setOnClickListener(this);

        mobile = (EditText) this.findViewById(R.id.mobile);
        card = (EditText) this.findViewById(R.id.card);
        pass = (EditText) this.findViewById(R.id.pass);
        email = (EditText) this.findViewById(R.id.email);
        pwr = (EditText) this.findViewById(R.id.pwr);
        password = (EditText) this.findViewById(R.id.password);
        button_login = (Button) this.findViewById(R.id.button_login);
        code_button = (Button) this.findViewById(R.id.code_button);
        button_login.setOnClickListener(this);
        code_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.leftbutton:
                finish();
                break;
            case R.id.code_button:
                //获得验证码
                try {
                    isMobileNet = HttpUtils.isMobileDataEnable(getApplicationContext());
                    isWifiNet = HttpUtils.isWifiDataEnable(getApplicationContext());
                    if (!isMobileNet && !isWifiNet) {
                        Toast.makeText(this, R.string.network_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mobile_str = mobile.getText().toString();
                if(StringUtil.isNullOrEmpty(mobile_str)){
                    Toast.makeText(this, R.string.reg_error_one, Toast.LENGTH_SHORT).show();
                    return;
                }
                code_button.setClickable(false);//不可点击
                MyTimer myTimer = new MyTimer(60000,1000);
                myTimer.start();
                getCard();
                break;
            case R.id.mobile_button:
                //手机注册按钮
                mobile_line.setBackgroundColor(getResources().getColor(R.color.yellow));
                email_line.setBackgroundColor(getResources().getColor(R.color.gray));
                reg_mobile.setVisibility(View.VISIBLE);
                reg_emial.setVisibility(View.GONE);
                selectMethod = 0;
                break;
            case R.id.email_button:
                //邮箱注册按钮
                mobile_line.setBackgroundColor(getResources().getColor(R.color.gray));
                email_line.setBackgroundColor(getResources().getColor(R.color.yellow));
                reg_mobile.setVisibility(View.GONE);
                reg_emial.setVisibility(View.VISIBLE);
                selectMethod = 1;
                break;
            case R.id.button_login:
                if(selectMethod == 0){
                    //手机注册
                    if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                        Toast.makeText(this, R.string.reg_error_one, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if( StringUtil.isNullOrEmpty(card.getText().toString())){
                        Toast.makeText(this, R.string.reg_error_five, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(StringUtil.isNullOrEmpty(pass.getText().toString())){
                        Toast.makeText(this, R.string.reg_error_three, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pass.getText().toString().length() >32 || pass.getText().toString().length()<6){
                        Toast.makeText(this, R.string.weimima, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendMobile();
                }
                if(selectMethod == 1){
                   //邮箱注册
                    if(StringUtil.isNullOrEmpty(email.getText().toString())){
                        Toast.makeText(this, R.string.reg_error_one_one, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if( StringUtil.isNullOrEmpty(pwr.getText().toString())){
                        Toast.makeText(this, R.string.reg_error_three, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(pwr.getText().toString().length() >32 || pwr.getText().toString().length()<6){
                        Toast.makeText(this, R.string.weimima, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(StringUtil.isNullOrEmpty(password.getText().toString())){
                        Toast.makeText(this, R.string.reg_error_five, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    sendEmail();
                }
                break;
        }
    }


    class MyTimer extends CountDownTimer {

        public MyTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            code_button.setText(res.getString(R.string.daojishi_three));
            code_button.setClickable(true);//可点击
        }

        @Override
        public void onTick(long millisUntilFinished) {
            code_button.setText(res.getString(R.string.daojishi_one) + millisUntilFinished / 1000 + res.getString(R.string.daojishi_two));
        }
    }

    //获得手机验证码
    private void getCard() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.GET_CARD_URL + "?mobile=" + mobile.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 1){
                                    CardData data = getGson().fromJson(s, CardData.class);
                                    Toast.makeText(RegistActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(RegistActivity.this, R.string.get_card_error, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(RegistActivity.this, R.string.get_card_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegistActivity.this, R.string.get_card_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("mobile", mobile.getText().toString());
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

    //获得验证码
    private void getCode() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.GET_CODE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    CodeData data = getGson().fromJson(s, CodeData.class);
                                    if (Integer.parseInt(data.getCode()) == 200) {
                                        code.setText(data.getData().getCode());
                                        cid = data.getData().getCid();
                                    }else {
                                        Toast.makeText(RegistActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(RegistActivity.this, R.string.reg_error_two, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(RegistActivity.this, R.string.reg_error_two, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegistActivity.this, R.string.reg_error_two, Toast.LENGTH_SHORT).show();
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

    //手机注册
    private void sendMobile() {
//        params.put("mobile", mobile.getText().toString());
//        params.put("pass", pass.getText().toString());
//        params.put("code", card.getText().toString());
        String uri = InternetURL.REG_MOBILE_URL +"?mobile=" + mobile.getText().toString() +"&pass=" + pass.getText().toString()+"&code="+card.getText().toString();
        StringRequest request = new StringRequest(
                Request.Method.GET,
               uri ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    RegData data = getGson().fromJson(s, RegData.class);
                                    if (Integer.parseInt(data.getCode()) == 200) {
                                        Toast.makeText(RegistActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                                        save("uid", data.getData().getUid());
                                        save("access_token", data.getData().getAccess_token());
                                        registerHx(mobile.getText().toString(),pass.getText().toString());
//                                        Intent login = new Intent(RegistActivity.this, LoginActivity.class);
//                                        startActivity(login);
                                    }else {
                                        Toast.makeText(RegistActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }else if(Integer.parseInt(code)==400){
                                    Toast.makeText(RegistActivity.this, R.string.reg_error_six, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(RegistActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(RegistActivity.this, R.string.reg_error_four, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegistActivity.this, R.string.reg_error_four, Toast.LENGTH_SHORT).show();
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

    //邮箱注册
    private void sendEmail() {
//        params.put("email", email.getText().toString());
//        params.put("pass", pwr.getText().toString());
//        params.put("code", code.getText().toString());
//        params.put("cid", cid);
        String uri = InternetURL.REG_EMAIL_URL +"?email=" +email.getText().toString()+"&pass="+pwr.getText().toString()
                +"&code=" +code.getText().toString()+"&cid=" + cid;
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
                                    RegData data = getGson().fromJson(s, RegData.class);
                                        Toast.makeText(RegistActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                                        save("uid", data.getData().getUid());
                                        save("access_token", data.getData().getAccess_token());
                                        registerHx(email.getText().toString(), pwr.getText().toString());
//                                        Intent login = new Intent(RegistActivity.this, LoginActivity.class);
//                                        startActivity(login);
//                                        finish();
                                }else {
                                    Toast.makeText(RegistActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {
                            Toast.makeText(RegistActivity.this, R.string.reg_error_four, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegistActivity.this, R.string.reg_error_four, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", email.getText().toString());
//                params.put("pass", pwr.getText().toString());
//                params.put("code", code.getText().toString());
//                params.put("cid", cid);
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



    /**
     * 注册
     *
     */
    public void registerHx(final String username, final String pwd) {
//        final String username = userNameEditText.getText().toString().trim();
//        final String pwd = passwordEditText.getText().toString().trim();
//        String confirm_pwd = confirmPwdEditText.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, getResources().getString(R.string.User_name_cannot_be_empty), Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, getResources().getString(R.string.Password_cannot_be_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage(getResources().getString(R.string.Is_the_registered));
            pd.show();

            new Thread(new Runnable() {
                public void run() {
                    try {
                        // 调用sdk注册方法
                        EMChatManager.getInstance().createAccountOnServer(username, pwd);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegistActivity.this.isFinishing())
                                    pd.dismiss();
                                // 保存用户名
                                PricesApplication.getInstance().setUserName(username);
//                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_successfully), Toast.LENGTH_SHORT).show();
                                Intent login = new Intent(RegistActivity.this, LoginActivity.class);
                                startActivity(login);
                                finish();
                            }
                        });
                    } catch (final EaseMobException e) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (!RegistActivity.this.isFinishing())
                                    pd.dismiss();
                                int errorCode=e.getErrorCode();
                                if(errorCode== EMError.NONETWORK_ERROR){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_anomalies), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.USER_ALREADY_EXISTS){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.User_already_exists), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.UNAUTHORIZED){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.registration_failed_without_permission), Toast.LENGTH_SHORT).show();
                                }else if(errorCode == EMError.ILLEGAL_USER_NAME){
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.illegal_user_name),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registration_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }).start();

        }
    }
}
