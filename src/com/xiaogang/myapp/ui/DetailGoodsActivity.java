package com.xiaogang.myapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.xiaogang.myapp.PricesApplication;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.*;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.GoodDetailSalesBaojiaData;
import com.xiaogang.myapp.data.GoodsDetailData;
import com.xiaogang.myapp.data.SuccessData;
import com.xiaogang.myapp.db.DBHelper;
import com.xiaogang.myapp.db.LiulanSmart;
import com.xiaogang.myapp.model.GoodDetailSales;
import com.xiaogang.myapp.model.GoodDetailSalesBaojia;
import com.xiaogang.myapp.model.GoodsDetail;
import com.xiaogang.myapp.model.ItemGrid;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.ContentListView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/28.
 * 商品详情页
 */
public class DetailGoodsActivity extends com.xiaogang.myapp.bean.BaseActivity implements View.OnClickListener,ContentListView.OnRefreshListener,ContentListView.OnLoadListener,OnClickContentItemListener {
    private ImageView leftbutton;
    private ContentListView lstv;
    private LinearLayout header;//头部
    private int pageIndex = 1;
    List<ItemGrid> lists;
    ItemHoriGridViewAdapter adapterHori;
    GridView gridView;//横向滚动
    private String id;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    //lstv
    private ItemGoodsBaojiaAdapter adapter;
    private List<GoodDetailSalesBaojia> listsgoods;

    //商品详情
    private ImageView detail_cover;
    private TextView title;
    private TextView type;
    private TextView cont;
    private TextView content;
    private TextView money;
    private TextView favour_button;

    private GoodsDetail goodsDetail;
//    private String isLogin;//0未登录  1登陆

    Drawable img_one, img_two;
    Resources res;

    private ProgressDialog progressDialog;

    UMSocialService mController;
    String shareCont = "";//内容
//    String shareUrl = InternetURL.SHARE_GOODS;
//    String shareParams = "";
    String sharePic = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getExtras().getString("id");
        res = getResources();
        img_one = res.getDrawable(R.drawable.shoucang);
        img_one.setBounds(0, 0, img_one.getMinimumWidth(), img_one.getMinimumHeight());
        img_two = res.getDrawable(R.drawable.shoucan_press);
        img_two.setBounds(0, 0, img_two.getMinimumWidth(), img_two.getMinimumHeight());

        setContentView(R.layout.detail_goods_activity);
//        isLogin = getGson().fromJson(getSp().getString("isLogin", ""), String.class);
        initView();
        setGridView();

        Resources res = getBaseContext().getResources();
        String message = res.getString(R.string.check_new_version).toString();
        progressDialog = new ProgressDialog(DetailGoodsActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(message);
        progressDialog.show();
//        getData(ContentListView.REFRESH);
        getDetail();
        getData(ContentListView.REFRESH);

        mController = UMServiceFactory.getUMSocialService(DetailGoodsActivity.class.getName(), RequestType.SOCIAL);


    }

    private void initView() {
        lists = new ArrayList<>();
        listsgoods = new ArrayList<GoodDetailSalesBaojia>();
        lists.add(new ItemGrid(R.drawable.grid_one, "附近"));
        lists.add(new ItemGrid(R.drawable.grid_two, "用家意见"));
        lists.add(new ItemGrid(R.drawable.grid_three, "二手买卖"));
        lists.add(new ItemGrid(R.drawable.grid_four, "项目评分"));
        lists.add(new ItemGrid(R.drawable.grid_five, "我要报价"));
        lists.add(new ItemGrid(R.drawable.grid_six, "分享"));

        adapter = new ItemGoodsBaojiaAdapter(listsgoods, this);
        adapter.setOnClickContentItemListener(this);
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);
        lstv = (ContentListView) this.findViewById(R.id.lstv);
        header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.detail_activity_header, null);//头部
        gridView = (GridView) header.findViewById(R.id.grid);//横向滚动
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lstv.addHeaderView(header);//将头部添加到lstv
        lstv.setAdapter(adapter);
        lstv.setOnRefreshListener(this);
        lstv.setOnLoadListener(this);

        detail_cover = (ImageView) header.findViewById(R.id.detail_cover);
        title = (TextView) header.findViewById(R.id.title);
        type = (TextView) header.findViewById(R.id.type);
        money = (TextView) header.findViewById(R.id.money);
        cont = (TextView) header.findViewById(R.id.cont);
        content = (TextView) header.findViewById(R.id.content);
        favour_button = (TextView) header.findViewById(R.id.favour_button);
        favour_button.setOnClickListener(this);
    }

    /**设置GirdView参数，绑定数据*/
    private void setGridView() {
        int size = lists.size();
        int length = 100;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        adapterHori = new ItemHoriGridViewAdapter(getApplicationContext(),
                lists);
        gridView.setAdapter(adapterHori);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
                        //附近
                        Intent secondHandView = new Intent(DetailGoodsActivity.this, DetailGoodsFujin.class);
                        secondHandView.putExtra("id", id);
                        startActivity(secondHandView);
                    }
                    break;
                    case 1: {
                        if ("0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))) {
                            //未登录
                            Intent loginView = new Intent(DetailGoodsActivity.this, LoginActivity.class);
                            startActivity(loginView);
                        } else {
                            Intent intent = new Intent(DetailGoodsActivity.this, GoodsYjyjActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("name", goodsDetail.getName());
                            startActivity(intent);
                        }

                    }
                    break;
                    case 2: {
                        //二手
                        Intent secondHandView = new Intent(DetailGoodsActivity.this, SecondHandActivity.class);
                        startActivity(secondHandView);
                    }
                    break;
                    case 3: {

                        if ("0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))) {
                            //未登录
                            Intent loginView = new Intent(DetailGoodsActivity.this, LoginActivity.class);
                            startActivity(loginView);
                        } else {
                            //添加评论
                            Intent intent = new Intent(DetailGoodsActivity.this, AddPlActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("name", goodsDetail.getName());
                            startActivity(intent);
                        }

                    }
                    break;
                    case 4:
                        if ("0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))) {
                            //未登录
                            Intent loginView = new Intent(DetailGoodsActivity.this, LoginActivity.class);
                            startActivity(loginView);
                        } else {
                            Intent intent = new Intent(DetailGoodsActivity.this, AddBaojiaActivity.class);
                            intent.putExtra("name", goodsDetail.getName());
                            intent.putExtra("gid", goodsDetail.getId());
                            intent.putExtra("nav", goodsDetail.getNav());
                            startActivity(intent);
                        }

                        break;
                    case 5:
                        //fenxiang
                    {
                        mController.openShare(DetailGoodsActivity.this, false);
                    }
                        break;
                }
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
            case R.id.favour_button:
                //收藏
                if("0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class))){
                    //未登录
                    Intent login = new Intent(DetailGoodsActivity.this, LoginActivity.class);
                    startActivity(login);
                }else{
                    favour();
                }
                break;
        }
    }

    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        int ii = (int) object;
        switch (ii){
            case 1:
                //ItemGoodsAapter
                switch (flag){
                    case 1:
                        break;
                    //放心订购
                }
                break;
        }
    }

    @Override
    public void onLoad() {
        pageIndex++;
        getData(ContentListView.LOAD);

    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        getData(ContentListView.REFRESH);
    }

    //获得报价列表
    private void getData(final int currentid) {
        String uri = InternetURL.ALL_SALES_URL + "?id="+id;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code =  jo.getString("code");
                                if(Integer.parseInt(code) == 200){
                                    GoodDetailSalesBaojiaData data = getGson().fromJson(s, GoodDetailSalesBaojiaData.class);
                                    if (Integer.parseInt(data.getCode()) == 200 && data.getData()!=null) {
                                        if (ContentListView.REFRESH == currentid) {
                                            listsgoods.clear();
                                            listsgoods.addAll(data.getData());
                                            lstv.setResultSize(data.getData().size());
                                            adapter.notifyDataSetChanged();
                                        }
                                        if (ContentListView.LOAD == currentid) {
                                            listsgoods.addAll(data.getData());
                                            lstv.setResultSize(data.getData().size());
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                    else {
                                        if (ContentListView.REFRESH == currentid) {
                                            listsgoods.clear();
                                            lstv.setResultSize(0);
                                            adapter.notifyDataSetChanged();
                                        }
                                        if (ContentListView.LOAD == currentid) {
                                            listsgoods.clear();
                                            lstv.setResultSize(0);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }else if(Integer.parseInt(code) == 401){
                                    if (ContentListView.REFRESH == currentid) {
                                        listsgoods.clear();
                                        lstv.setResultSize(0);
                                        adapter.notifyDataSetChanged();
                                    }
                                    if (ContentListView.LOAD == currentid) {
                                        listsgoods.clear();
                                        lstv.setResultSize(0);
                                        adapter.notifyDataSetChanged();
                                    }
                                    Toast.makeText(DetailGoodsActivity.this, R.string.has_no_data, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if (ContentListView.REFRESH == currentid) {
                                        listsgoods.clear();
                                        lstv.setResultSize(0);
                                        adapter.notifyDataSetChanged();
                                    }
                                    if (ContentListView.LOAD == currentid) {
                                        listsgoods.clear();
                                        lstv.setResultSize(0);
                                        adapter.notifyDataSetChanged();
                                    }
                                    Toast.makeText(DetailGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (ContentListView.REFRESH == currentid) {
                                listsgoods.clear();
                                lstv.setResultSize(0);
                                adapter.notifyDataSetChanged();
                            }
                            if (ContentListView.LOAD == currentid) {
                                listsgoods.clear();
                                lstv.setResultSize(0);
                                adapter.notifyDataSetChanged();
                            }
                            Toast.makeText(DetailGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(DetailGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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

    private void getDetail() {
        String uri = InternetURL.GOODS_DETAIL_URL + "?id="+id;
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
                                    GoodsDetailData data = getGson().fromJson(s, GoodsDetailData.class);
                                    if (Integer.parseInt(data.getCode()) == 200 && data.getData()!=null) {
                                        goodsDetail = data.getData();
                                        if(goodsDetail!=null){
                                            initData();
                                        }

                                    }else {
                                        Toast.makeText(DetailGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(DetailGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(DetailGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        lstv.onRefreshComplete();
                        lstv.onLoadComplete();
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(DetailGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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

    public void searchGoods(View view){
        //
        Intent search = new Intent(DetailGoodsActivity.this, SearchActivity.class);
        startActivity(search);
    }

    void initData(){
        title.setText(goodsDetail.getName());
        content.setText(Html.fromHtml(goodsDetail.getContent()==null?"":goodsDetail.getContent()));
        type.setText(goodsDetail.getNav_name());
        money.setText(goodsDetail.getPrice_sale());
        cont.setText(goodsDetail.getAttr());
        imageLoader.displayImage(goodsDetail.getThumbnail(),detail_cover , PricesApplication.options, animateFirstListener);
        if ("0".equals(goodsDetail.getCollect())){
            favour_button.setCompoundDrawables(null, img_one, null, null);
        }else {
            favour_button.setCompoundDrawables(null, img_two, null, null);
        }
        //添加浏览记录
        //先查询是否已经存在该商品了
        if(DBHelper.getInstance(DetailGoodsActivity.this).isSaved(goodsDetail.getId())){
            //如果已经加入了，删除
            DBHelper.getInstance(DetailGoodsActivity.this).deleteShoppingByGoodsId(goodsDetail.getId());
        }
        LiulanSmart shoppingCart = new LiulanSmart();
        shoppingCart.setGoods_id(goodsDetail.getId());
        shoppingCart.setEmp_id("");
        shoppingCart.setGoods_name(goodsDetail.getName() == null ? "" : goodsDetail.getName());
        shoppingCart.setGoods_cover(goodsDetail.getThumbnail() == null ? "" : goodsDetail.getThumbnail());
        shoppingCart.setSell_price(goodsDetail.getPrice_sale());
        DBHelper.getInstance(DetailGoodsActivity.this).addShoppingToTable(shoppingCart);

        //设置分享内容
        shareCont = goodsDetail.getName();
        mController.setShareContent(shareCont);
//        sharePic = goodsDetail.getEmpCover();
        //设置分享图片
        mController.setShareMedia(new UMImage(DetailGoodsActivity.this, sharePic));
//        shareParams = "?goodsId=" + goods.getId();

    }

    void favour(){
        String uri = InternetURL.SAVE_FAVOUR_URL +  "?uid="+ getGson().fromJson(getSp().getString("uid", ""), String.class)
                +"&access_token="+ getGson().fromJson(getSp().getString("access_token", ""), String.class)
                +"&goodsid=" + goodsDetail.getId() ;
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
                                    if ("0".equals(goodsDetail.getCollect())){
                                        //如果未收藏
                                        goodsDetail.setCollect("1");
                                        favour_button.setCompoundDrawables(null, img_two, null, null);
                                    }else {
                                        goodsDetail.setCollect("0");
                                        favour_button.setCompoundDrawables(null, img_one, null, null);
                                    }
                                }else {
                                    Toast.makeText(DetailGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(DetailGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(DetailGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
