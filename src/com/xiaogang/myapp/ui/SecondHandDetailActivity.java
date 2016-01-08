package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
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
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.GooderData;
import com.xiaogang.myapp.data.SecondGoodsSingleData;
import com.xiaogang.myapp.model.Gooder;
import com.xiaogang.myapp.model.SecondGoods;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.MainPopMenu;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/27.
 * 二手详情
 */
public class SecondHandDetailActivity extends BaseActivity implements View.OnClickListener ,MainPopMenu.OnItemClickListener {
    private ImageView leftbutton;
    MainPopMenu mainPopMenu;
    private String id;
    private SecondGoods secondGoods;
    private ImageView second_detail_cover;
    private TextView title;
    private TextView one_prices;
    private TextView money;
    private TextView cont;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    Resources res;

    private Button button_goods;
    private Button button_user;
    private TextView user_name;
    private TextView user_cont;
    private LinearLayout one;
    private LinearLayout two;
    private TextView tel_click;//电话联系

    private Gooder gooder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        id = getIntent().getExtras().getString("id");
        setContentView(R.layout.second_detail_activity);
        initView();
        getData();
    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);

        mainPopMenu = new MainPopMenu(this);
        mainPopMenu.setOnItemClickListener(this);
        second_detail_cover = (ImageView) this.findViewById(R.id.second_detail_cover);
        title = (TextView) this.findViewById(R.id.title);
        one_prices = (TextView) this.findViewById(R.id.one_prices);
        money = (TextView) this.findViewById(R.id.money);
        cont = (TextView) this.findViewById(R.id.cont);
        user_name = (TextView) this.findViewById(R.id.user_name);
        user_cont = (TextView) this.findViewById(R.id.user_cont);
        button_goods = (Button) this.findViewById(R.id.button_goods);
        button_user = (Button) this.findViewById(R.id.button_user);
        button_goods.setOnClickListener(this);
        button_user.setOnClickListener(this);
        one = (LinearLayout) this.findViewById(R.id.one);
        two = (LinearLayout) this.findViewById(R.id.two);
        one.setVisibility(View.VISIBLE);
        two.setVisibility(View.GONE);
        tel_click = (TextView) this.findViewById(R.id.tel_click);
        tel_click.setOnClickListener(this);
    }


    public void getData() {
        String uri = InternetURL.SECOND_DETAIL_URL +"?id=" +id;
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
                                    SecondGoodsSingleData data = getGson().fromJson(s, SecondGoodsSingleData.class);
                                    secondGoods =  data.getData();
                                    initData();
                                }else {
                                    Toast.makeText(SecondHandDetailActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(SecondHandDetailActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SecondHandDetailActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.leftbutton:
                finish();
                break;
            case R.id.button_goods:
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.GONE);
                button_goods.setTextColor(getResources().getColor(R.color.black));
                button_user.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.button_user:
                one.setVisibility(View.GONE);
                two.setVisibility(View.VISIBLE);
                button_goods.setTextColor(getResources().getColor(R.color.white));
                button_user.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.tel_click:
                //电话
                if(gooder!=null && !StringUtil.isNullOrEmpty(gooder.getMobile())){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + gooder.getMobile()));
                    startActivity(intent);
                }

                break;
        }
    }
    //弹出顶部主菜单
    public void onTopMenuPopupButtonClick(View view) {
        mainPopMenu.showAsDropDown(view);
    }
    @Override
    public void onItemClick(int index) {
        switch (index) {
            case 0:
                break;
            case 1:
                break;
            case 2:
            {
                //常见问题
                Intent wenti = new Intent(SecondHandDetailActivity.this, SecondQuestActivity.class);
                startActivity(wenti);
            }
                break;
            case 3:
                //二手买卖守则
            {
                Intent shouze = new Intent(SecondHandDetailActivity.this, SecondShouzeActivity.class);
                startActivity(shouze);
            }
                break;
            case 4:
            {
                //交易按去哪贴士
                Intent shouze = new Intent(SecondHandDetailActivity.this, SecondAnquanActivity.class);
                startActivity(shouze);
            }
                break;
            case 5:
                break;
        }
    }

    void initData(){
        imageLoader.displayImage(secondGoods.getThumbnail(), second_detail_cover,
                PricesApplication.options, animateFirstListener);
        title.setText(secondGoods.getName());
        String rmb  =  res.getString(R.string.rmb);
        money.setText(rmb + secondGoods.getPrice());
        one_prices.setText(rmb+ secondGoods.getPrice_cost());
        String state = res.getString(R.string.state);
        String status = res.getString(R.string.status);
        String reg_time = res.getString(R.string.reg_time);
        String last_time = res.getString(R.string.last_time);
        String stateStr = "";
        if ("1".equals(secondGoods.getState())){
            stateStr = res.getString(R.string.state_one);
        }else {
            stateStr = res.getString(R.string.state_two);
        }
        cont.setText(state + stateStr+"\n"+status+secondGoods.getStatus()+"\n" + reg_time+secondGoods.getReg_time()
        +"\n" + last_time +  secondGoods.getLast_time());


        user_name.setText(secondGoods.getUname());
//        user_cont.setText(secondGoods.getMessage().getEmail());
        getUser();
    }

    public void findSearch(View view){
        Intent intent = new Intent(SecondHandDetailActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    public void getUser() {
        String uri = InternetURL.GET_SHANGJIA_URL +"?uid="+getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token=" +getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&id=" + secondGoods.getUid();
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
                                    GooderData data =  getGson().fromJson(s, GooderData.class);
                                    gooder = data.getData();
                                    initDataGooder();
                                }else {
                                    Toast.makeText(SecondHandDetailActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(SecondHandDetailActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SecondHandDetailActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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

    public void initDataGooder(){
        //商家gooder
        user_name.setText("店名："+gooder.getName() +"\n" +"电话："+gooder.getMobile() +"\n"+ "地址："+gooder.getAddress());
        user_cont.setText("简介："+gooder.getInfo());
    }
}
