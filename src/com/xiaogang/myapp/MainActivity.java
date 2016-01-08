package com.xiaogang.myapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mapapi.SDKInitializer;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.chatuidemo.activity.HxMainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.update.UmengUpdateAgent;
import com.xiaogang.myapp.adapter.*;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.ArticelData;
import com.xiaogang.myapp.data.GoodsArticelData;
import com.xiaogang.myapp.data.TwoObjData;
import com.xiaogang.myapp.model.*;
import com.xiaogang.myapp.ui.*;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.PictureGridview;
import org.bitlet.weupnp.Main;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends com.xiaogang.myapp.bean.BaseActivity implements View.OnClickListener,OnClickContentItemListener ,AMapLocationListener {
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private SlideMenu slideMenu;
    private Button login_button;//登录按钮
    private Button reg_button;//注册按钮
    private ImageView logo;//logo
    private LinearLayout liner_secondhand;//二手
    private LinearLayout liner_near;//附近
    private LinearLayout liner_money;//价格
    private LinearLayout liner_zjll;//最近浏览
    private LinearLayout liner_set;//设置
    private LinearLayout liner_news;//最新情报
    private LinearLayout liner_type;//分类
    private LinearLayout liner_msg;//消息
    private LinearLayout liner_sao;//扫一扫
    private LinearLayout left_anquan;//anxindinggou

    private TextView name;
    private TextView typeName;

    private static final String LTAG = MainActivity.class.getSimpleName();

    private LocationManagerProxy mLocationManagerProxy;

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            Log.d(LTAG, "action: " + s);

            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Toast.makeText(MainActivity.this, "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置", Toast.LENGTH_LONG).show();
            } else if (s
                    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Toast.makeText(MainActivity.this, "网络出错", Toast.LENGTH_LONG).show();
            }
        }
    }
    private SDKReceiver mReceiver;

    //价格
    private ImageView leftbutton;
    private ViewPager viewpager;
    private ViewPagerAdapter adapter;
    List<Articel> listSlide = new ArrayList<>();
    private ImageView dot, dots[];
    private Runnable runnable;
    private int autoChangeTime = 5000;
    private LinearLayout viewGroup;
    private PictureGridview detail_grideview;//灯具grideview
    private List<TwoObjGoods> listsdeng;
    MoneyDengGridViewAdapter adapterDeng;
    private List<GoodsArticel> listslv;
    private ItemLvAdapter adapterTwo;
    private PictureGridview lv_grideview;//旅游grideview
    private ImageView type;

    private TwoObj twoObj;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        init();
        UmengUpdateAgent.update(this);
        save("isLogin", "0");
        setContentView(R.layout.main);
        initView();
        initViewPager();
        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);
        //获得商品数据
        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.check_new_version).toString();
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
        getOne();
        getTwo();
        getThree();


        if (DemoHXSDKHelper.getInstance().isLogined()) {
            // ** 免登陆情况 加载所有本地群和会话
            //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
            //加上的话保证进了主页面会话和群组都已经load完毕
            EMGroupManager.getInstance().loadAllGroups();
            EMChatManager.getInstance().loadAllConversations();
        }
    }

    private void initView() {

        listsdeng = new ArrayList<>();
        listslv = new ArrayList<>();

//        header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.money_activity_header, null);
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        type = (ImageView) this.findViewById(R.id.type);
        type.setOnClickListener(this);
        slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
        login_button = (Button) slideMenu.findViewById(R.id.login_button);
        reg_button = (Button) slideMenu.findViewById(R.id.reg_button);
        logo = (ImageView) slideMenu.findViewById(R.id.logo);
        name = (TextView) slideMenu.findViewById(R.id.name);
        left_anquan = (LinearLayout) slideMenu.findViewById(R.id.left_anquan);
        liner_secondhand = (LinearLayout) this.findViewById(R.id.liner_secondhand);
        liner_near = (LinearLayout) this.findViewById(R.id.liner_near);
        liner_money = (LinearLayout) this.findViewById(R.id.liner_money);
        liner_zjll = (LinearLayout) this.findViewById(R.id.liner_zjll);
        liner_set = (LinearLayout) this.findViewById(R.id.liner_set);
        liner_news = (LinearLayout) this.findViewById(R.id.liner_news);
        liner_type = (LinearLayout) this.findViewById(R.id.liner_type);
        liner_msg = (LinearLayout) this.findViewById(R.id.liner_msg);
        typeName = (TextView) this.findViewById(R.id.typeName);
        liner_sao = (LinearLayout) this.findViewById(R.id.liner_sao);

        detail_grideview = (PictureGridview) this.findViewById(R.id.gridview_detail_picture);
        detail_grideview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapterDeng = new MoneyDengGridViewAdapter(listsdeng, this);
        detail_grideview.setAdapter(adapterDeng);
        detail_grideview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                startImageActivity(pics, position);
                TwoObjGoods goodsArticel = listsdeng.get(position);
                Intent detailView = new Intent(MainActivity.this, DetailGoodsActivity.class);
                detailView.putExtra("id", goodsArticel.getGoodsid());
                startActivity(detailView);
            }
        });

        liner_type.setOnClickListener(this);
        liner_msg.setOnClickListener(this);
        liner_news.setOnClickListener(this);
        liner_zjll.setOnClickListener(this);
        liner_near.setOnClickListener(this);
        liner_secondhand.setOnClickListener(this);
        logo.setOnClickListener(this);
        leftbutton.setOnClickListener(this);
        login_button.setOnClickListener(this);
        reg_button.setOnClickListener(this);
        liner_money.setOnClickListener(this);
        liner_set.setOnClickListener(this);
        liner_sao.setOnClickListener(this);
        left_anquan.setOnClickListener(this);

        adapterTwo = new ItemLvAdapter(listslv,MainActivity.this);
        lv_grideview = (PictureGridview) this.findViewById(R.id.lv_grideview);
        lv_grideview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv_grideview.setAdapter(adapterTwo);
        lv_grideview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsArticel goodsArticel = listslv.get(position);
                Intent detailView = new Intent(MainActivity.this, DetailGoodsActivity.class);
                detailView.putExtra("id", goodsArticel.getId());
                startActivity(detailView);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.type:
            {
                Intent typeView = new Intent(MainActivity.this, TypeActivity.class);
                startActivity(typeView);
            }
                break;
            case R.id.leftbutton:
                if (slideMenu.isMainScreenShowing()) {
                    slideMenu.openMenu();
                } else {
                    slideMenu.closeMenu();
                }
                break;
            case R.id.login_button:
            {
                Intent loginView = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginView);
            }
                break;
            case R.id.reg_button:
            {
                Intent regView = new Intent(MainActivity.this, RegistActivity.class);
                startActivity(regView);
            }
                break;
            case R.id.logo:
                if("0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
                    //未登录
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }else{
                    Intent profileView = new Intent(MainActivity.this, CenterActivity.class);
                    startActivity(profileView);
                }
                break;
            case R.id.liner_secondhand:
                if("0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
                    //未登录
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }else{
                    Intent secondView = new Intent(MainActivity.this, SecondHandActivity.class);
                    startActivity(secondView);
                }
                break;
            case R.id.liner_near:
            {
                Intent nearView = new Intent(MainActivity.this, OverlayDemo.class);
                startActivity(nearView);
            }
                break;
            case R.id.liner_money:
                if (slideMenu.isMainScreenShowing()) {
                    slideMenu.openMenu();
                } else {
                    slideMenu.closeMenu();
                }
                break;
            case R.id.liner_zjll:
            {
                Intent zjView = new Intent(MainActivity.this, ZuijinLLActivity.class);
                startActivity(zjView);
            }
                break;
            case R.id.liner_set:
            {
                Intent setView = new Intent(MainActivity.this, SetActivity.class);
                startActivity(setView);
            }
                break;
            case R.id.liner_news:
            {
                Intent newsView = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(newsView);
            }
                break;
            case R.id.liner_type:
            {
                Intent typeView = new Intent(MainActivity.this, TypeActivity.class);
                startActivity(typeView);
            }
                break;
            case R.id.liner_msg:
            {
                if("0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
                    //未登录
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                }else{
                    Intent msgView = new Intent(MainActivity.this, HxMainActivity.class);
                    startActivity(msgView);
                }
//                Intent msgView = new Intent(MainActivity.this, FriendAndMsgActivity.class);

            }
                break;
            case R.id.liner_sao:
            {
                //扫一扫
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, 1);
            }
                break;
            case R.id.left_anquan:
            {
                Intent intent = new Intent(MainActivity.this, AnquanActivity.class);
                startActivity(intent);
            }
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消监听 SDK 广播
        unregisterReceiver(mReceiver);
        unregisterReceiver(mBroadcastReceiver);
    }
    private void initViewPager() {
        adapter = new ViewPagerAdapter(this);
        adapter.change(listSlide);
        adapter.setOnClickContentItemListener(this);
        viewpager = (ViewPager) this.findViewById(R.id.viewpager);
        viewpager.setAdapter(adapter);
        viewpager.setOnPageChangeListener(myOnPageChangeListener);
        initDot();
        runnable = new Runnable() {
            @Override
            public void run() {
                int next = viewpager.getCurrentItem() + 1;
                if (next >= adapter.getCount()) {
                    next = 0;
                }
                viewHandler.sendEmptyMessage(next);
            }
        };
        viewHandler.postDelayed(runnable, autoChangeTime);
    }

    // 初始化dot视图
    private void initDot() {
        viewGroup = (LinearLayout) slideMenu.findViewById(R.id.viewGroup);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                20, 20);
        layoutParams.setMargins(4, 3, 4, 3);

        dots = new ImageView[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {

            dot = new ImageView(this);
            dot.setLayoutParams(layoutParams);
            dots[i] = dot;
            dots[i].setTag(i);
            dots[i].setOnClickListener(onClick);

            if (i == 0) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }

            viewGroup.addView(dots[i]);
        }
    }

    ViewPager.OnPageChangeListener myOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            setCurDot(arg0);
            viewHandler.removeCallbacks(runnable);
            viewHandler.postDelayed(runnable, autoChangeTime);
        }

    };
    // 实现dot点击响应功能,通过点击事件更换页面
    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag();
            setCurView(position);
        }

    };

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position > adapter.getCount()) {
            return;
        }
        viewpager.setCurrentItem(position);
//        if (!StringUtil.isNullOrEmpty(lists.get(position).getNewsTitle())){
//            titleSlide = lists.get(position).getNewsTitle();
//            if(titleSlide.length() > 13){
//                titleSlide = titleSlide.substring(0,12);
//                article_title.setText(titleSlide);//当前新闻标题显示
//            }else{
//                article_title.setText(titleSlide);//当前新闻标题显示
//            }
//        }

    }

    /**
     * 选中当前引导小点
     */
    private void setCurDot(int position) {
        for (int i = 0; i < dots.length; i++) {
            if (position == i) {
                dots[i].setBackgroundResource(R.drawable.dotc);
            } else {
                dots[i].setBackgroundResource(R.drawable.dotn);
            }
        }
    }

    /**
     * 每隔固定时间切换广告栏图片
     */
    @SuppressLint("HandlerLeak")
    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setCurView(msg.what);
        }

    };
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag) {
            case 1:
                //说明点击的是viewpage
                Articel articel = listSlide.get(position);
                Intent detailView = new Intent(MainActivity.this, DetailGoodsActivity.class);
                detailView.putExtra("id", articel.getId());
                startActivity(detailView);
                break;
        }
    }

    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("login_success")) {
                login_button.setVisibility(View.GONE);
                reg_button.setVisibility(View.GONE);
                logo.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                name.setText(getGson().fromJson(getSp().getString("user", ""), String.class));
                imageLoader.displayImage(getGson().fromJson(getSp().getString("face", ""), String.class), logo, PricesApplication.txOptions, animateFirstListener);
            }
            if(action.equals("set_face")){
                logo.setVisibility(View.VISIBLE);
                imageLoader.displayImage(getGson().fromJson(getSp().getString("face", ""), String.class), logo, PricesApplication.txOptions, animateFirstListener);
            }
        }
    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("login_success");
        myIntentFilter.addAction("set_face");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }


    public void search(View view){
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    void getOne(){
        String uri = InternetURL.JIAGE_ONE_URL;
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
                                    ArticelData data = getGson().fromJson(s, ArticelData.class);
                                    listSlide.clear();
                                    listSlide.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(MainActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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


    void getTwo(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.JIAGE_TWO_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    TwoObjData data = getGson().fromJson(s, TwoObjData.class);
                                    typeName.setText(data.getData().getTitle());
                                    twoObj = data.getData();
                                    listsdeng.clear();
                                    listsdeng.addAll(twoObj.getGoods());
                                    adapterDeng.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(MainActivity.this, jo.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MainActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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

    void getThree(){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.JIAGE_THREE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    GoodsArticelData data = getGson().fromJson(s, GoodsArticelData.class);
                                    listslv.clear();
                                    listslv.addAll(data.getData().getGoods());
                                    adapterTwo.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(MainActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(MainActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == MainActivity.RESULT_OK){
                    Bundle bundle = data.getExtras();
                    //显示扫描到的内容
//                    mTextView.setText(bundle.getString("result"));
                    String result = bundle.getString("result");

                    //显示
                    // mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
                }
                break;
        }
    }

    /**
     * 初始化定位
     */
    private void init() {
        // 初始化定位，只采用网络定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.setGpsEnable(false);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);

    }

    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {

        if (amapLocation != null
                && amapLocation.getAMapException().getErrorCode() == 0) {
            // 定位成功回调信息，设置相关消息
            PricesApplication.lat = amapLocation.getLatitude();
            PricesApplication.lon = amapLocation.getLongitude();
//            mLocationLatlngTextView.setText(amapLocation.getLatitude() + "  "
//                    + amapLocation.getLongitude());
//            mLocationAccurancyTextView.setText(String.valueOf(amapLocation
//                    .getAccuracy()));
//            mLocationMethodTextView.setText(amapLocation.getProvider());

//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date date = new Date(amapLocation.getTime());
//
//            mLocationTimeTextView.setText(df.format(date));
//            mLocationDesTextView.setText(amapLocation.getAddress());
//            mLocationCountryTextView.setText(amapLocation.getCountry());
//            if (amapLocation.getProvince() == null) {
//                mLocationProvinceTextView.setText("null");
//            } else {
//                mLocationProvinceTextView.setText(amapLocation.getProvince());
//            }
//            mLocationCityTextView.setText(amapLocation.getCity());
//            mLocationCountyTextView.setText(amapLocation.getDistrict());
//            mLocationRoadTextView.setText(amapLocation.getRoad());
//            mLocationPOITextView.setText(amapLocation.getPoiName());
//            mLocationCityCodeTextView.setText(amapLocation.getCityCode());
//            mLocationAreaCodeTextView.setText(amapLocation.getAdCode());
        } else {
            Log.e("AmapErr","Location ERR:" + amapLocation.getAMapException().getErrorCode());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        // 移除定位请求
        mLocationManagerProxy.removeUpdates(this);
        // 销毁定位
        mLocationManagerProxy.destroy();
    }


}
