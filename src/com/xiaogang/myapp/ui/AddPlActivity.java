package com.xiaogang.myapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.xiaogang.myapp.data.AddBaojiaData;
import com.xiaogang.myapp.model.Chengdu;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.SelectShiyongTuijianWindow;
import com.xiaogang.myapp.widget.SelectShiyongchengduWindow;
import com.xiaogang.myapp.widget.SelectShiyongshijianWindow;
import com.xiaogang.myapp.widget.SelectSmallTypeWindow;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/23.
 */
public class AddPlActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView hours;
    private TextView recommend;
    private TextView know;

    private EditText idea;
    private EditText youdian;
    private EditText quedian;
    private RatingBar rating_bar;

    private String id;
    private String name;
    private SelectShiyongshijianWindow selectShiyongshijianWindow;
    private SelectShiyongchengduWindow selectShiyongchengduWindow;
    private SelectShiyongTuijianWindow selectShiyongTuijianWindow;

    private String select_time_one = "";
    private String select_time_two = "";
    private String chengduId = "";//熟练程度
    private String tuijianId = "";//推荐程度
    private String uid;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();

        uid = getGson().fromJson(getSp().getString("uid", ""), String.class);
        access_token = getGson().fromJson(getSp().getString("access_token", ""), String.class);

        id = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");
        setContentView(R.layout.add_pl_activity);
        initView();
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        title.setText(name);
        hours = (TextView) this.findViewById(R.id.hours);
        know = (TextView) this.findViewById(R.id.know);
        recommend = (TextView) this.findViewById(R.id.recommend);

        idea = (EditText) this.findViewById(R.id.idea);
        youdian = (EditText) this.findViewById(R.id.youdian);
        quedian = (EditText) this.findViewById(R.id.quedian);
        rating_bar = (RatingBar) this.findViewById(R.id.rating_bar);
    }

    @Override
    public void onClick(View view) {

    }

    public void back(View view){
        finish();
    }

    public void submit(View view){
        //提交
        if(StringUtil.isNullOrEmpty(hours.getText().toString())){
            Toast.makeText(AddPlActivity.this, getResources().getString(R.string.qingxuanzeshiyongshijian), Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isNullOrEmpty(chengduId)){
            Toast.makeText(AddPlActivity.this, getResources().getString(R.string.qingxuanzeshiyongchengtdu), Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isNullOrEmpty(tuijianId)){
            Toast.makeText(AddPlActivity.this, getResources().getString(R.string.qingxuanzetuijianchengtdu), Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isNullOrEmpty(idea.getText().toString())){
            Toast.makeText(AddPlActivity.this, getResources().getString(R.string.qingshurushiyongyijian), Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isNullOrEmpty(youdian.getText().toString())){
            Toast.makeText(AddPlActivity.this, getResources().getString(R.string.qingshurushiyongyijianyoudian), Toast.LENGTH_SHORT).show();
            return;
        }
        if(StringUtil.isNullOrEmpty(quedian.getText().toString())){
            Toast.makeText(AddPlActivity.this, getResources().getString(R.string.qingshurushiyongyijianquedian), Toast.LENGTH_SHORT).show();
            return;
        }
        add();
    }
    void add(){
        String ideaStr = idea.getText().toString();
        try {
            ideaStr = URLEncoder.encode(ideaStr, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        String hoursStr = hours.getText().toString();
        try {
            hoursStr = URLEncoder.encode(hoursStr, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        String youdianStr = youdian.getText().toString();
        try {
            youdianStr = URLEncoder.encode(youdianStr, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        String quedianStr =quedian.getText().toString();
        try {
            quedianStr = URLEncoder.encode(quedianStr, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        String uri = InternetURL.ADD_COMMEND_URL
                + "?uid="+ uid
                + "&access_token=" +access_token
                +"&user=" + getGson().fromJson(getSp().getString("user", ""), String.class)
                +"&goodsid=" + id
                +"&hours=" + hoursStr
                +"&recommend=" + tuijianId
                +"&know=" + chengduId
                +"&grade=" + String.valueOf(rating_bar.getRating())
                +"&idea=" + ideaStr
                +"&merit=" + youdianStr
                +"&defect=" +quedianStr ;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                 uri ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    Toast.makeText(AddPlActivity.this, R.string.pinglunchenggong, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent("add_yongjiayijian_success");
                                    sendBroadcast(intent);
                                    finish();
                                }else {
                                    Toast.makeText(AddPlActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(AddPlActivity.this, R.string.pinglunshibai, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(AddPlActivity.this, R.string.pinglunshibai, Toast.LENGTH_SHORT).show();
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

    public void click_one(View view){
        ShowDialog();
    }
    public void click_two(View view){
        ShowDialogSycd();
    }
    public void click_three(View view){
        ShowDialogTjcd();
    }

    // 选择分类 listsType
    private void ShowDialog() {
        selectShiyongshijianWindow = new SelectShiyongshijianWindow(AddPlActivity.this, itemsOnClick);
        //显示窗口
        selectShiyongshijianWindow.showAtLocation(AddPlActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    //使用程度
    private void ShowDialogSycd() {
        selectShiyongchengduWindow = new SelectShiyongchengduWindow(AddPlActivity.this, itemsOnClickTwo);
        //显示窗口
        selectShiyongchengduWindow.showAtLocation(AddPlActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    //使用程度
    private void ShowDialogTjcd() {
        selectShiyongTuijianWindow = new SelectShiyongTuijianWindow(AddPlActivity.this, itemsOnClickThree);
        //显示窗口
        selectShiyongTuijianWindow.showAtLocation(AddPlActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            selectShiyongshijianWindow.dismiss();
            switch (v.getId()) {
                case R.id.sure: {
                    //确定按钮
                }
                break;
                default:
                    break;
            }
        }
    };
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClickTwo = new View.OnClickListener() {
        public void onClick(View v) {
            selectShiyongchengduWindow.dismiss();
            switch (v.getId()) {
                case R.id.sure: {
                    //确定按钮
                }
                break;
                default:
                    break;
            }
        }
    };
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClickThree = new View.OnClickListener() {
        public void onClick(View v) {
            selectShiyongTuijianWindow.dismiss();
            switch (v.getId()) {
                case R.id.sure: {
                    //确定按钮
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
            if(action.equals("select_time_one")){
                select_time_one = intent.getExtras().getString("time_one");
                hours.setText(select_time_one+select_time_two);
            }
            if(action.equals("select_time_two")){
                select_time_two = intent.getExtras().getString("time_two");
                hours.setText(select_time_one+select_time_two);
            }
            if(action.equals("select_shiyongchengdu")){
                Chengdu chengdu = (Chengdu) intent.getExtras().get("shiyongchengdu");
                know.setText(chengdu.getName());
                chengduId = chengdu.getId();
            }
            if(action.equals("select_tuijian")){
                Chengdu chengdu = (Chengdu) intent.getExtras().get("tuijian");
                recommend.setText(chengdu.getName());
                tuijianId = chengdu.getId();
            }

        }

    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("select_time_one");
        myIntentFilter.addAction("select_time_two");
        myIntentFilter.addAction("select_shiyongchengdu");
        myIntentFilter.addAction("select_tuijian");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}
