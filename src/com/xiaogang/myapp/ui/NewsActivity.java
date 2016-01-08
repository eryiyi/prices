package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
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
import com.xiaogang.myapp.adapter.ItemNewsAdapter;
import com.xiaogang.myapp.adapter.OnClickContentItemListener;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.NewsData;
import com.xiaogang.myapp.data.NewsTypeData;
import com.xiaogang.myapp.library.PullToRefreshBase;
import com.xiaogang.myapp.library.PullToRefreshGridView;
import com.xiaogang.myapp.model.News;
import com.xiaogang.myapp.model.NewsType;
import com.xiaogang.myapp.util.upload.BaseTools;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.ColumnHorizontalScrollView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/27.
 * 最新情报
 */
public class NewsActivity extends BaseActivity implements View.OnClickListener,OnClickContentItemListener {
    private ImageView leftbutton;
    private PullToRefreshGridView lv_grideview;
    private ItemNewsAdapter adapter;
    private List<News> lists;
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;

    List<NewsType> listType = new ArrayList<>();
    int tmpposition = 0;

    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    LinearLayout mRadioGroup_content;
    LinearLayout ll_more_columns;
    RelativeLayout rl_column;
    private int columnSelectIndex = 0;
    public ImageView shade_left;
    public ImageView shade_right;
    private int mScreenWidth = 0;
    private int mItemWidth = 0;

    private ImageView type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        mScreenWidth = BaseTools.getWindowsWidth(this);
        mItemWidth = mScreenWidth / 7;
        initView();
        getNewsType();//获得新闻类别
    }

    private void initView() {
        lists = new ArrayList<>();
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);

        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        type = (ImageView) this.findViewById(R.id.type);
        type.setOnClickListener(this);
        leftbutton.setOnClickListener(this);

        lv_grideview = (PullToRefreshGridView) this.findViewById(R.id.lv_grideview);
        adapter = new ItemNewsAdapter(lists, NewsActivity.this);
        adapter.setOnClickContentItemListener(this);
        lv_grideview.setMode(PullToRefreshBase.Mode.BOTH);
            lv_grideview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                if (listType !=null && listType.size() > 0){
                    getNews();
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                String label = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                if (listType !=null && listType.size() > 0){
                    getNews();
                }
            }
        });
        lv_grideview.setAdapter(adapter);
        lv_grideview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
                News news = lists.get(position);
                intent.putExtra("id", news.getId());
                startActivity(intent);
            }
        });
        adapter.setOnClickContentItemListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case   R.id.type:
            {
                Intent typeView = new Intent(NewsActivity.this, TypeActivity.class);
                startActivity(typeView);
            }
                break;
            case R.id.leftbutton:
                finish();
                break;
        }
    }
    @Override
    public void onClickContentItem(int position, int flag, Object object) {
        switch (flag) {
            case 1:
                Intent detailView = new Intent(NewsActivity.this, PublishTieActivity.class);
                startActivity(detailView);
                break;
        }
    }

    public void getNewsType() {
        String uri = InternetURL.GET_MESSAGE_TYPE_URL;
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
                                    NewsTypeData data = getGson().fromJson(s, NewsTypeData.class);
                                    listType =  data.getData();
                                    initTabColumn();
                                    if(listType!=null && listType.size() >0){
                                        //获取默认第一条下的新闻列表
                                        getNews();
                                    }
                                }else {
                                    Toast.makeText(NewsActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(NewsActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(NewsActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
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

    public void getNews() {
        String uri = InternetURL.GET_MESSAGE_LSTV_URL + "?id=" + listType.get(tmpposition).getId() +"&p="+ pageIndex;
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
                                    lists.clear();
                                    NewsData data = getGson().fromJson(s, NewsData.class);
                                    lists.addAll(data.getData());
                                    adapter.notifyDataSetChanged();
                                }else if(Integer.parseInt(code1) == 201){
                                    Toast.makeText(NewsActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                                }else if(Integer.parseInt(code1) == 402){
                                    Toast.makeText(NewsActivity.this, "没有数据了", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(NewsActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(NewsActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(NewsActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
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


    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = listType.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 0;
            params.rightMargin = 0;
            TextView columnTextView = new TextView(this);
            columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
            columnTextView.setBackgroundResource(R.drawable.button_selector_yellow);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setTextSize(10);
            columnTextView.setText(listType.get(i).getName());
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
                            tmpposition = i;
                            getNews();
                        }
                    }
//                    Toast.makeText(getApplicationContext(), newsClassify.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);
        }
    }

    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }


}
