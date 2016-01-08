package com.xiaogang.myapp.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.*;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.myapp.PricesApplication;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.AnimateFirstDisplayListener;
import com.xiaogang.myapp.bean.ActivityTack;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.SuccessData;
import com.xiaogang.myapp.util.upload.CommonUtil;
import com.xiaogang.myapp.util.upload.CompressPhotoUtil;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.SelectPhoPopWindow;
import com.xiaogang.myapp.widget.SexPopWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/27.
 * 编辑个人资料
 * save("email",data.getData().getEmail());
 save("name",data.getData().getName());
 save("mobile",data.getData().getMobile());
 save("face",data.getData().getFace());
 save("sex",data.getData().getSex());
 save("reg_time",data.getData().getReg_time());
 */
public class ProfileActivity extends com.xiaogang.myapp.bean.BaseActivity implements View.OnClickListener {
    private int tmpNum = 0;
    private ImageView switch_button;//切换
    private ImageView head;
    private EditText profile_email;
    private EditText profile_mobile;
    private EditText profile_name;
    private TextView profile_sex;
    private TextView user_title;
    Bitmap photo;

    private String email;
    private String name;
    private String mobile;
    private String face;
//    private String sex;//0男 1女
    private String reg_time;
    private String user;

    private Button exit;
    private ImageView edit_button;
    private SexPopWindow sexPopWindow;
    private SelectPhoPopWindow deleteWindow;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类


    private String pics = "";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");
    private ProgressDialog progressDialog;

    private String txpic = "";
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.profile_activity);
        email = getGson().fromJson(getSp().getString("email", ""), String.class);
        name = getGson().fromJson(getSp().getString("name", ""), String.class);
        mobile = getGson().fromJson(getSp().getString("mobile", ""), String.class);
        face = getGson().fromJson(getSp().getString("face", ""), String.class);
        user = getGson().fromJson(getSp().getString("user", ""), String.class);
//        sex = getGson().fromJson(getSp().getString("sex", ""), String.class);
        reg_time = getGson().fromJson(getSp().getString("reg_time", ""), String.class);
        initView();
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        initData();
    }

    private void initData() {
        if (!StringUtil.isNullOrEmpty(user)){
            user_title.setText(user + getResources().getString(R.string.yonghuzhongxin));
        }
        if (!StringUtil.isNullOrEmpty(email)){
            profile_email.setText(email);
        }
        if (!StringUtil.isNullOrEmpty(name)){
            profile_name.setText(name);
        }
        if (!StringUtil.isNullOrEmpty(mobile)){
            profile_mobile.setText(mobile);
        }
        if (!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("sex", ""), String.class))){
            if ("0".equals(getGson().fromJson(getSp().getString("sex", ""), String.class))){
                profile_sex.setText(getResources().getString(R.string.sex_man));
            }else {
                profile_sex.setText(getResources().getString(R.string.sex_woman));
            }
        }

        if(!StringUtil.isNullOrEmpty(face)){
            imageLoader.displayImage( face, head,
                    PricesApplication.txOptions, animateFirstListener);
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        exit = (Button) this.findViewById(R.id.exit);
        exit.setOnClickListener(this);
        switch_button = (ImageView) this.findViewById(R.id.switch_button);
        head = (ImageView) this.findViewById(R.id.head);
        switch_button.setOnClickListener(this);
        profile_email = (EditText) this.findViewById(R.id.profile_email);
        profile_mobile = (EditText) this.findViewById(R.id.profile_mobile);
        profile_name = (EditText) this.findViewById(R.id.profile_name);
        profile_sex = (TextView) this.findViewById(R.id.profile_sex);
        edit_button = (ImageView) this.findViewById(R.id.edit_button);
        user_title = (TextView) this.findViewById(R.id.user_title);
        edit_button.setOnClickListener(this);
        profile_email.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!StringUtil.isNullOrEmpty(profile_email.getText().toString())) {
                        if (!profile_email.getText().toString().equals(getGson().fromJson(getSp().getString("email", ""), String.class))) {
                            Resources res = getBaseContext().getResources();
                            String message = res.getString(R.string.please_wait).toString();
                            progressDialog = new ProgressDialog(ProfileActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage(message);
                            progressDialog.show();
                            resetEmail();
                        }

                    }
                }
            }
        });
        profile_mobile.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(!StringUtil.isNullOrEmpty(profile_mobile.getText().toString())){
                        if(!profile_mobile.getText().toString().equals(getGson().fromJson(getSp().getString("mobile", ""), String.class))){
                            Resources res = getBaseContext().getResources();
                            String message = res.getString(R.string.please_wait).toString();
                            progressDialog = new ProgressDialog(ProfileActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage(message);
                            progressDialog.show();
                            resetMobile();
                        }

                    }
                }
            }
        });

        profile_name.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!StringUtil.isNullOrEmpty(profile_name.getText().toString())) {
                        if (!profile_name.getText().toString().equals(getGson().fromJson(getSp().getString("name", ""), String.class))) {
                            Resources res = getBaseContext().getResources();
                            String message = res.getString(R.string.please_wait).toString();
                            progressDialog = new ProgressDialog(ProfileActivity.this);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.setMessage(message);
                            progressDialog.show();
                            resetNickName();
                        }

                    }
                }
            }
        });

        profile_sex.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.switch_button:
                if(tmpNum == 0){
                    switch_button.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
                    tmpNum =1;
                }else {
                    switch_button.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
                    tmpNum =0;
                }
                break;
            case R.id.exit:
                logout();
                break;
            case R.id.edit_button:
                //编辑用户名
                Intent intent =  new Intent(ProfileActivity.this, edit_username_activity.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.profile_sex:
                ShowSexDialog();
                break;
        }
    }
    //修改密码
    public void resetPwr(View view){
        //
        Intent reset = new Intent(ProfileActivity.this, ResetPwrActivity.class);
        startActivity(reset);
    }

    void logout() {
        AlertDialog dialog = new AlertDialog.Builder(ProfileActivity.this)
                .setIcon(R.drawable.ic_launcher)
                .setTitle(getResources().getString(R.string.is_quite))
                .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog pd = new ProgressDialog(ProfileActivity.this);
                        String st = getResources().getString(R.string.Are_logged_out);
                        pd.setMessage(st);
                        pd.setCanceledOnTouchOutside(false);
                        pd.dismiss();
                        save("password", "");
                        ActivityTack.getInstanse().exit(ProfileActivity.this);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();
        dialog.show();
    }

    private void resetEmail() {
        String  uri=  InternetURL.UPDATE_EMAIL_URL +"?uid="+ getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token="+ getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&email="+ profile_email.getText().toString();
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
                                    save("email", profile_email.getText().toString());
                                }else if(Integer.parseInt(code1) == 400){
                                    Toast.makeText(ProfileActivity.this, R.string.email_error_one, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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


    private void resetMobile() {
        String uri = InternetURL.UPDATE_MOBILE_URL + "?uid="+ getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token="+ getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&mobilel=" + profile_mobile.getText().toString() ;
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
                                    save("mobile", profile_mobile.getText().toString());
                                }else if(Integer.parseInt(code1) == 400){
                                    Toast.makeText(ProfileActivity.this, R.string.mobile_error_one, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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


    private void resetNickName() {
        String userStr = profile_name.getText().toString();
        try {
            userStr = URLEncoder.encode(userStr, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        String uri = InternetURL.UPDATE_NAME_URL + "?uid="+ getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token="+ getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&name=" + userStr ;
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
                                    save("name", profile_name.getText().toString());
                                }else if(Integer.parseInt(code1) == 400){
                                    Toast.makeText(ProfileActivity.this, R.string.mobile_error_one, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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

    private void resetSex(final String sex) {
        String uri = InternetURL.UPDATE_SEX_URL+"?uid="+getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token=" +getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&sex=" + sex;
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
                                    save("sex", sex);
                                }else {
                                    Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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

    // 性别选择
    private void ShowSexDialog() {
        sexPopWindow = new SexPopWindow(ProfileActivity.this, itemsOnClick);
        //显示窗口
        sexPopWindow.showAtLocation(ProfileActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            sexPopWindow.dismiss();
            switch (v.getId()) {
                case R.id.sex_man: {
                    resetSex("0");
                    profile_sex.setText(getResources().getString(R.string.sex_man));
                }
                break;
                case R.id.sex_woman: {
                    resetSex("1");
                    profile_sex.setText(getResources().getString(R.string.sex_woman));
                }
                break;
                default:
                    break;
            }
        }
    };


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("set_user")){
                user_title.setText(getGson().fromJson(getSp().getString("user", ""), String.class)
                        + getResources().getString(R.string.yonghuzhongxin));
            }
        }

    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("set_user");//设置下拉按钮的广播事件
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }


    // 选择相册，相机
    private void ShowPickDialog() {
        deleteWindow = new SelectPhoPopWindow(ProfileActivity.this, itemsOnClickPic);
        //显示窗口
        deleteWindow.showAtLocation(ProfileActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClickPic = new View.OnClickListener() {

        public void onClick(View v) {
            deleteWindow.dismiss();
            switch (v.getId()) {
                case R.id.camera: {
                    Intent camera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                            .fromFile(new File(Environment
                                    .getExternalStorageDirectory(),
                                    "ppCover.jpg")));
                    startActivityForResult(camera, 2);
                }
                break;
                case R.id.mapstorage: {
                    Intent mapstorage = new Intent(Intent.ACTION_PICK, null);
                    mapstorage.setDataAndType(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    startActivityForResult(mapstorage, 1);
                }
                break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            // 如果是调用相机拍照时
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/ppCover.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            // 取得裁剪后的图片
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            if (photo != null) {
                pics = CompressPhotoUtil.saveBitmap2file(photo, System.currentTimeMillis() + ".jpg", PHOTO_CACHE_DIR);
                if(!StringUtil.isNullOrEmpty(pics)){
                    sendCover();
//                    head.setImageBitmap(photo);
                }
            }
        }
    }

    public void selectpic(View view){
        ShowPickDialog();
    }

    public void sendCover(){
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        File file = new File(pics);
        Map<String, File> files = new HashMap<String, File>();
        files.put("photo", file);
        Map<String, String> params = new HashMap<String, String>();
        CommonUtil.addPutUploadFileRequest(
                this,
                InternetURL.UPLOAD_FILE_URL,
                files,
                params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 = jo.getString("code");
                                if (Integer.parseInt(code1) == 200) {
                                    SuccessData data = getGson().fromJson(s, SuccessData.class);
                                    txpic = data.getData();
                                    resetFace(txpic);
                                } else {
                                    Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                        Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                    }
                },
                null);
    }


    private void resetFace(final String face) {
        String uri = InternetURL.UPDATE_FACE_URL+"?uid="+getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token=" +getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&photo=" + face;
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
                                    save("face", txpic);
                                    head.setImageBitmap(photo);
                                    Intent intent1 = new Intent("set_face");
                                    sendBroadcast(intent1);
                                }else {
                                    Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfileActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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
