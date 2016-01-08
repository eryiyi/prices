package com.xiaogang.myapp.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.myapp.PricesApplication;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.AnimateFirstDisplayListener;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.SalesUploadData;
import com.xiaogang.myapp.data.SuccessData;
import com.xiaogang.myapp.model.ShangdianType;
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
 * Created by Administrator on 2015/7/28.
 * ��������
 */
public class OpenActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private EditText open_dianming;
    private EditText open_email;
    private EditText open_mobile;
    private EditText open_name;
    private TextView open_sex;
    private TextView open_address;
    private ImageView open_pic;
    private ImageView head;
    private ImageView open_file;
    private Button open_submit;
    private TextView open_type;

    private SexPopWindow sexPopWindow;
    private SelectPhoPopWindow deleteWindow;

    private String sexType = "";
    Bitmap photo;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();

    private String pics = "";
    private static final File PHOTO_CACHE_DIR = new File(Environment.getExternalStorageDirectory() + "/liangxun/PhotoCache");
    private ProgressDialog progressDialog;

    private String txpic = "";

    private LinearLayout open_map;
    private LocationClient mLocationClient;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor="gcj02";

    private ShangdianType shangdianType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_activity);
        mLocationClient = ((PricesApplication)getApplication()).mLocationClient;
        initView();
        initLocation();
        mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        mLocationClient.stop();
        super.onStop();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，

        option.setScanSpan(0);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(false);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        open_type = (TextView) this.findViewById(R.id.open_type);
        open_dianming = (EditText) this.findViewById(R.id.open_dianming);
        open_email = (EditText) this.findViewById(R.id.open_email);
        open_mobile = (EditText) this.findViewById(R.id.open_mobile);
        open_name = (EditText) this.findViewById(R.id.open_name);
        open_sex = (TextView) this.findViewById(R.id.open_sex);
        open_pic = (ImageView) this.findViewById(R.id.open_pic);
        open_file = (ImageView) this.findViewById(R.id.open_file);
        open_submit = (Button) this.findViewById(R.id.open_submit);
        open_map = (LinearLayout) this.findViewById(R.id.open_map);
        open_address = (TextView) this.findViewById(R.id.open_address);
        head = (ImageView) this.findViewById(R.id.head);

        open_submit.setOnClickListener(this);
        open_sex.setOnClickListener(this);
        open_pic.setOnClickListener(this);
        open_map.setOnClickListener(this);
        open_type.setOnClickListener(this);

        if(!StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("face", ""), String.class))){
            imageLoader.displayImage(getGson().fromJson(getSp().getString("face", ""), String.class), head,
                    PricesApplication.txOptions, animateFirstListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.open_type:
                //点此选择商店类型
            {
                Intent intent = new Intent(OpenActivity.this, GoodsTypeActivity.class);
                startActivityForResult(intent,0);
            }
            break;
            case R.id.back:
                finish();
                break;
            case R.id.open_pic:
                ShowPickDialog();
                break;
            case R.id.open_sex:
                /**隐藏软键盘**/
                View view = getWindow().peekDecorView();
                if (view != null) {
                    InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                ShowSexDialog();
                break;
            case R.id.open_submit:
                if(shangdianType == null || StringUtil.isNullOrEmpty(open_dianming.getText().toString()) || StringUtil.isNullOrEmpty(open_email.getText().toString()) || StringUtil.isNullOrEmpty(open_mobile.getText().toString()) ||
                        StringUtil.isNullOrEmpty(open_name.getText().toString()) || StringUtil.isNullOrEmpty(sexType) || StringUtil.isNullOrEmpty(txpic)){
                    Toast.makeText(OpenActivity.this, R.string.sales_error_one, Toast.LENGTH_SHORT).show();
                    return;
                }
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.please_wait).toString();
                progressDialog = new ProgressDialog(OpenActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage(message);
                progressDialog.show();
                sendFile();
                break;
            case R.id.open_map:
                break;
        }
    }


    private void ShowSexDialog() {
        sexPopWindow = new SexPopWindow(OpenActivity.this, itemsOnClick);
        sexPopWindow.showAtLocation(OpenActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            sexPopWindow.dismiss();
            switch (v.getId()) {
                case R.id.sex_man: {
                    sexType = "0";
                    open_sex.setText(getResources().getString(R.string.sex_man));
                }
                break;
                case R.id.sex_woman: {
                    sexType = "1";
                    open_sex.setText(getResources().getString(R.string.sex_woman));
                }
                break;
                default:
                    break;
            }
        }
    };


    private void ShowPickDialog() {
        deleteWindow = new SelectPhoPopWindow(OpenActivity.this, itemsOnClickPic);
        deleteWindow.showAtLocation(OpenActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private View.OnClickListener itemsOnClickPic = new View.OnClickListener() {
        public void onClick(View v) {
            deleteWindow.dismiss();
            switch (v.getId()) {
                case R.id.camera: {
                    Intent camera = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
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
            case 1:
                if (data != null) {
                    startPhotoZoom(data.getData());
                }
                break;
            case 2:
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/ppCover.jpg");
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case 3:
                if (data != null) {
                    setPicToView(data);
                }
                break;

            default:
                break;
        }
                if(resultCode == 001){
                    shangdianType = (ShangdianType) data.getExtras().get("shangdianType");
                    if(shangdianType != null){
                        open_type.setText(shangdianType.getNav_name());
                    }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
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
                    sendPic();
                }
            }
        }
    }

    public void sendPic(){
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.please_wait).toString();
        progressDialog = new ProgressDialog(OpenActivity.this);
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
                                    open_file.setVisibility(View.VISIBLE);
                                    open_file.setImageBitmap(photo);
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                } else {
                                    Toast.makeText(OpenActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(OpenActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
                    }
                },
                null);
    }

    //1:申请开店
    public void sendFile(){
        String open_nameStr = open_name.getText().toString();
        String open_dianmingStr = open_dianming.getText().toString();
        try {
            open_nameStr = URLEncoder.encode(open_nameStr, "UTF-8");
            open_dianmingStr = URLEncoder.encode(open_dianmingStr, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        String uri = InternetURL.SALES_SHOP_URL +"?uid="+ getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token=" + getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&email="+open_email.getText().toString()
                +"&mobile="+open_mobile.getText().toString()
                +"&photo=" +txpic
                +"&person=" + open_nameStr
                +"&sex="+ sexType
                +"&lat=" + (PricesApplication.lat == null ? "" : PricesApplication.lat).toString()
                +"&name=" + open_dianmingStr
                +"&id=" + shangdianType.getId()
                +"&lng=" +(PricesApplication.lon == null ? "" : PricesApplication.lon).toString();
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
                                    Toast.makeText(OpenActivity.this, R.string.shenqing_success, Toast.LENGTH_SHORT).show();
                                    finish();
                                }else if(Integer.parseInt(code1) == -5){
                                    Toast.makeText(OpenActivity.this, R.string.shenqing_fail_five, Toast.LENGTH_SHORT).show();
                                    finish();
                                }{
                                    Toast.makeText(OpenActivity.this, R.string.shenqing_fail, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(OpenActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(OpenActivity.this, R.string.set_error_one, Toast.LENGTH_SHORT).show();
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
