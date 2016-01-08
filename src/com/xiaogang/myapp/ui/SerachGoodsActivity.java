package com.xiaogang.myapp.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemSearchGoodsAdapter;
import com.xiaogang.myapp.adapter.ItemSearchGoodsByTypeAdapter;
import com.xiaogang.myapp.adapter.OnClickContentItemListener;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.GoodsTypeBeanData;
import com.xiaogang.myapp.data.GoodsTypeBeansData;
import com.xiaogang.myapp.data.SearchGoodsData;
import com.xiaogang.myapp.library.PullToRefreshBase;
import com.xiaogang.myapp.library.PullToRefreshListView;
import com.xiaogang.myapp.model.GoodsTypeBean;
import com.xiaogang.myapp.model.GoodsTypeBeans;
import com.xiaogang.myapp.model.SearchGoods;
import com.xiaogang.myapp.model.SortType;
import com.xiaogang.myapp.util.upload.BaseTools;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/29.
 * 7�������ӵ�����������Ʒ�б�
 */
public class SerachGoodsActivity extends BaseActivity implements View.OnClickListener{
    private String id;
    private String name;
    private ImageView leftbutton;
    private TextView title;

    private PullToRefreshListView lstv;
    private ItemSearchGoodsByTypeAdapter adapter;
    private List<SearchGoods> lists = new ArrayList<SearchGoods>();

    private int pageIndex = 0;
    private static boolean IS_REFRESH = true;

    List<GoodsTypeBean> listsGoodsTye = new ArrayList<>();
    List<GoodsTypeBeans> listsGoodsTyes = new ArrayList<>();
    ArrayList<String> companyList = new ArrayList<String>();

    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    LinearLayout mRadioGroup_content;
    LinearLayout ll_more_columns;
    RelativeLayout rl_column;

    private ImageView button_more_columns;
    private int columnSelectIndex = 0;
    public ImageView shade_left;
    public ImageView shade_right;
    private int mScreenWidth = 0;
    private int mItemWidth = 0;

//    private CustomerSpinner companySpinner;
    ArrayAdapter<String> adaptertype;

    private String attr = "";
    private RelativeLayout liner_one;//过滤筛选
//    sort 商品排序
//    1：从热门开始
//    2:从新到旧
//    3：型号按字母排
//    4：价格从低到高
//    5:价格从高到低
//    6：评价从高到低
    private int sort=1 ;//

    private SelectSmallTypeWindow selectWindow;
    private SelectSortWindow selectSortWindow;
    private List<String> listsType = new ArrayList<>();
    private List<SortType> listsTypeSort = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerBoradcastReceiver();
        setContentView(R.layout.search_goods_type_activity);
        mScreenWidth = BaseTools.getWindowsWidth(this);
        mItemWidth = mScreenWidth / 7;
        id = getIntent().getExtras().getString("id");
        name = getIntent().getExtras().getString("name");
        title = (TextView)this.findViewById(R.id.title);
        title.setText(name);
        initView();
        listsTypeSort.add(new SortType("1", "从热门开始"));
        listsTypeSort.add(new SortType("2", "从新到旧"));
        listsTypeSort.add(new SortType("3", "型号按字母排"));
        listsTypeSort.add(new SortType("4", "价格从低到高"));
        listsTypeSort.add(new SortType("5", "价格从高到低"));
        listsTypeSort.add(new SortType("6", "评价从高到低"));
        getData();
        getDataType();
    }

    private void initView() {
        liner_one = (RelativeLayout) this.findViewById(R.id.liner_one);
        liner_one.setOnClickListener(this);
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);

        adapter = new ItemSearchGoodsByTypeAdapter(lists, SerachGoodsActivity.this);
        lstv = (PullToRefreshListView) this.findViewById(R.id.lstv);
        lstv.setMode(PullToRefreshBase.Mode.BOTH);
        lstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                getData();
            }
        });

        lstv.setAdapter(adapter);

        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SearchGoods good = lists.get(position - 1);
                Intent detailView = new Intent(SerachGoodsActivity.this, DetailGoodsActivity.class);
                detailView.putExtra("id", good.getId());
                startActivity(detailView);
            }
        });

        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);

        adaptertype = new ArrayAdapter<String>(SerachGoodsActivity.this, android.R.layout.simple_spinner_item, companyList);


    }
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = listsGoodsTye.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 2;
            params.rightMargin = 2;
            TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
            columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(0, 0, 0, 0);
            columnTextView.setId(i);
            columnTextView.setText(listsGoodsTye.get(i).getName());
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v)
                            localView.setSelected(false);
                        else {
                            localView.setSelected(true);
                        }
                    }
                    getDataTypeById(listsGoodsTye.get(v.getId()).getId());
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);
        }
    }

    private void getDataTypeById(String id) {
        String uri = InternetURL.UPDATE_ALLATTR_URL + "?id="+id ;
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
                                    GoodsTypeBeansData data = getGson().fromJson(s, GoodsTypeBeansData.class);
                                    listsType.clear();
                                    listsType.addAll(data.getData());
                                    ShowDialog();
//                                    adapter.notifyDataSetChanged();
                                }else {
                                    Toast.makeText(SerachGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SerachGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SerachGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1000:
                    attr = data.getExtras().getString("str");
                    IS_REFRESH = true;
                    pageIndex = 1;
                    getData();
                    break;
            }

        }
    }

    private void getData() {
        //brand 品牌
        String uri = InternetURL.GOODS_URL + "?id="+id +"&brand="+"&price="+"&attr=" + attr +"&sort="+sort+"&p="+pageIndex;
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
                                    SearchGoodsData data = getGson().fromJson(s, SearchGoodsData.class);
                                    if (IS_REFRESH) {
                                        lists.clear();
                                    }
                                    lists.addAll(data.getData());
                                    lstv.onRefreshComplete();
                                    adapter.notifyDataSetChanged();
                                }else if(Integer.parseInt(code1) == 201){
                                    Toast.makeText(SerachGoodsActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
                                }else if(Integer.parseInt(code1) == 402){
                                    Toast.makeText(SerachGoodsActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(SerachGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SerachGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SerachGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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

    private void getDataType() {
        String uri = InternetURL.UPDATE_FILTERLIST_URL + "?id="+id ;
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
                                    GoodsTypeBeanData data = getGson().fromJson(s, GoodsTypeBeanData.class);
                                    listsGoodsTye = data.getData();
                                    initTabColumn();
                                }else if(Integer.parseInt(code1) == 400){
                                    Toast.makeText(SerachGoodsActivity.this, R.string.no_data_type, Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(SerachGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SerachGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(SerachGoodsActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftbutton:
                finish();
                break;
            case R.id.liner_one:
                //选择排序
                ShowDialogSort();
                break;
        }
    }


    // 选择分类 listsType
    private void ShowDialog() {
        selectWindow = new SelectSmallTypeWindow(SerachGoodsActivity.this, listsType);
        //显示窗口
        selectWindow.showAtLocation(SerachGoodsActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }
    private void ShowDialogSort() {
        selectSortWindow = new SelectSortWindow(SerachGoodsActivity.this, listsTypeSort);
        //显示窗口
        selectSortWindow.showAtLocation(SerachGoodsActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    //广播接收动作
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("select_attr")){
                selectWindow.dismiss();
                attr = intent.getExtras().getString("attr");
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }
            if(action.equals("select_sort")){
                selectSortWindow.dismiss();
                sort = Integer.parseInt(intent.getExtras().getString("sort"));
                IS_REFRESH = true;
                pageIndex = 1;
                getData();
            }
        }

    };

    //注册广播
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("select_attr");
        myIntentFilter.addAction("select_sort");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }


}
