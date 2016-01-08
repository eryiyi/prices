package com.xiaogang.myapp.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.easemob.EMCallBack;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.xiaogang.myapp.PricesApplication;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.MemberData;
import com.xiaogang.myapp.model.LoginData;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录
 */
public class LoginActivity extends com.xiaogang.myapp.bean.BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;
    private Button reg_button;//注册按钮
    private EditText mobile;
    private EditText password;
    private ProgressDialog progressDialog;

    private String username;
    private String pwr;
    private Button button_login;


    private static final String TAG = "LoginActivity";
    public static final int REQUEST_CODE_SETNICK = 1;
    private boolean progressShow;
    private boolean autoLogin = false;
    private String currentUsername;
    private String currentPassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initView();
        if (mobile != null) {
            username = getGson().fromJson(getSp().getString("mobile", ""), String.class);
            mobile.setText(username);
        }
        if (password != null) {
            pwr = getGson().fromJson(getSp().getString("password", ""), String.class);
            password.setText(pwr);
        }
        if (!StringUtil.isNullOrEmpty(username) && !StringUtil.isNullOrEmpty(pwr)) {
            Resources res = getBaseContext().getResources();
            String message = res.getString(R.string.check_new_version).toString();
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(message);
            progressDialog.show();
            login();
        }
    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        reg_button = (Button) this.findViewById(R.id.reg_button);
        button_login = (Button) this.findViewById(R.id.button_login);
        reg_button.setOnClickListener(this);
        leftbutton.setOnClickListener(this);
        button_login.setOnClickListener(this);
        mobile = (EditText) this.findViewById(R.id.mobile);
        password = (EditText) this.findViewById(R.id.password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.leftbutton:
                finish();
                break;
            case R.id.reg_button:
                Intent regView = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(regView);
                break;
            case R.id.button_login:
                if(StringUtil.isNullOrEmpty(mobile.getText().toString())){
                    Toast.makeText(LoginActivity.this, R.string.login_error_one, Toast.LENGTH_SHORT).show();
                    return;
                }if(StringUtil.isNullOrEmpty(password.getText().toString())){
                Toast.makeText(LoginActivity.this, R.string.login_error_two, Toast.LENGTH_SHORT).show();
                    return;
                }
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.check_new_version).toString();
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();
                login();
                break;
        }
    }

    private void login() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.LOGIN_URL+"?name=" + mobile.getText().toString() +"&pass="+password.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    LoginData data = getGson().fromJson(s, LoginData.class);
                                    save("mobile", mobile.getText().toString());
                                    save("password", password.getText().toString());
                                    save("uid", data.getData().getUid());
                                    save("user", data.getData().getUser());
                                    save("access_token", data.getData().getAccess_token());
                                    save("isLogin", "1");//0未登录  1登陆
                                    //获得个人信息
                                    getMember(data.getData().getUid(), data.getData().getAccess_token());
                                }else {
                                    Toast.makeText(LoginActivity.this,  jo.getString("message"), Toast.LENGTH_SHORT).show();
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                        } else {
                            Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(LoginActivity.this,  R.string.login_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", mobile.getText().toString());
                params.put("pass", password.getText().toString());
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

    //获得个人信息
    void getMember(String uid, String access_token)
    {
        String uri = InternetURL.GET_MEMBER_URL + "?uid="+uid+"&access_token="+access_token;
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
                                MemberData data = getGson().fromJson(s, MemberData.class);
                                if (Integer.parseInt(data.getCode()) == 200) {
                                    save("email",data.getData().getEmail());
                                    save("name",data.getData().getName());
                                    save("mobile",data.getData().getMobile());
                                    save("face",data.getData().getFace());
                                    save("sex",data.getData().getSex());
                                    save("reg_time",data.getData().getReg_time());
                                    Intent intent1 = new Intent("login_success");
                                    sendBroadcast(intent1);
                                    //环信登陆
                                    if (DemoHXSDKHelper.getInstance().isLogined()) {
                                        autoLogin = true;
                                        // 如果用户名密码都有，直接进入主页面
                                        finish();
                                    }else {
                                        loginhx();
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                }
                            }else {
                                Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, R.string.login_error, Toast.LENGTH_SHORT).show();
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(LoginActivity.this,  R.string.login_error, Toast.LENGTH_SHORT).show();
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



    /**
     * 登录
     *
     */
    public void loginhx() {
        if (!CommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        currentUsername = mobile.getText().toString().trim();
        currentPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        progressShow = true;
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();

        final long start = System.currentTimeMillis();
        // 调用sdk登陆方法登陆聊天服务器
        EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

            @Override
            public void onSuccess() {
                if (!progressShow) {
                    return;
                }
                // 登陆成功，保存用户名密码
                PricesApplication.getInstance().setUserName(currentUsername);
                PricesApplication.getInstance().setPassword(currentPassword);

                try {
                    // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                    // ** manually load all local groups and
                    EMGroupManager.getInstance().loadAllGroups();
                    EMChatManager.getInstance().loadAllConversations();
                    // 处理好友和群组
                    initializeContacts();
                } catch (Exception e) {
                    e.printStackTrace();
                    // 取好友或者群聊失败，不让进入主页面
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            DemoHXSDKHelper.getInstance().logout(true,null);
                            Toast.makeText(getApplicationContext(), R.string.login_failure_failed, Toast.LENGTH_SHORT).show();
                        }
                    });
                    return;
                }
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        PricesApplication.currentUserNick.trim());
                if (!updatenick) {
                    Log.e("LoginActivity", "update current user nick fail");
                }
                if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
                    pd.dismiss();
                }
                // 进入主页面
//                Intent intent = new Intent(LoginActivity.this,
//                        HxMainActivity.class);
//                startActivity(intent);

                finish();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(final int code, final String message) {
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initializeContacts() {
        Map<String, User> userlist = new HashMap<String, User>();
        // 添加user"申请与通知"
        User newFriends = new User();
        newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
        String strChat = getResources().getString(
                R.string.Application_and_notify);
        newFriends.setNick(strChat);

        userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
        // 添加"群聊"
        User groupUser = new User();
        String strGroup = getResources().getString(R.string.group_chat);
        groupUser.setUsername(Constant.GROUP_USERNAME);
        groupUser.setNick(strGroup);
        groupUser.setHeader("");
        userlist.put(Constant.GROUP_USERNAME, groupUser);

        // 添加"Robot"
        User robotUser = new User();
        String strRobot = getResources().getString(R.string.robot_chat);
        robotUser.setUsername(Constant.CHAT_ROBOT);
        robotUser.setNick(strRobot);
        robotUser.setHeader("");
        userlist.put(Constant.CHAT_ROBOT, robotUser);

        // 存入内存
        ((DemoHXSDKHelper) HXSDKHelper.getInstance()).setContactList(userlist);
        // 存入db
        UserDao dao = new UserDao(LoginActivity.this);
        List<User> users = new ArrayList<User>(userlist.values());
        dao.saveContactList(users);
    }

//    /**
//     * 注册
//     *
//     * @param view
//     */
//    public void register(View view) {
//        startActivityForResult(new Intent(this, RegisterActivity.class), 0);
//    }

    @Override
    public void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }


}
