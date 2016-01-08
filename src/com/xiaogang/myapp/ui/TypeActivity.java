package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.TypeBigData;
import com.xiaogang.myapp.fragment.Fragment_pro_type;
import com.xiaogang.myapp.model.TypeBig;
import com.xiaogang.myapp.util.upload.StringUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/27.
 * 分类
 */
public class TypeActivity extends BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;
    private List<TypeBig> toolsList = new ArrayList<TypeBig>();
    private TextView toolsTextViews[];
    private View views[];
    private LayoutInflater inflaters;
    private ScrollView scrollView;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private ViewPager shop_pager;
    private int currentItem=0;
    private ShopAdapter shopAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.type_activity);
        scrollView=(ScrollView) this.findViewById(R.id.tools_scrlllview);
        shopAdapter=new ShopAdapter(this.getSupportFragmentManager());
        inflaters=LayoutInflater.from(this);
        initView();
        //获得大类
        getBigType();
    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.leftbutton:
                finish();
                break;
        }
    }


    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView() {
        LinearLayout toolsLayout=(LinearLayout) this.findViewById(R.id.tools);
        toolsTextViews=new TextView[toolsList.size()];
        views=new View[toolsList.size()];

        for (int i = 0; i < toolsList.size(); i++) {
            View view=inflaters.inflate(R.layout.item_b_top_nav_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView=(TextView) view.findViewById(R.id.text);
            textView.setText(toolsList.get(i).getNav_name());
            toolsLayout.addView(view);
            toolsTextViews[i]=textView;
            views[i]=view;
        }
        changeTextColor(0);

        initPager();
    }

    private View.OnClickListener toolsItemListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shop_pager.setCurrentItem(v.getId());
        }
    };



    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager() {
        shop_pager=(ViewPager) this.findViewById(R.id.goods_pager);
        shop_pager.setAdapter(shopAdapter);
        shop_pager.setOnPageChangeListener(onPageChangeListener);

    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
            if(shop_pager.getCurrentItem()!=arg0)shop_pager.setCurrentItem(arg0);
            if(currentItem!=arg0){
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem=arg0;
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * ViewPager 加载选项卡
     * @author Administrator
     *
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment =new Fragment_pro_type();
            Bundle bundle=new Bundle();
            TypeBig goodsTypeBig=toolsList.get(arg0);
            bundle.putString("typename", goodsTypeBig.getNav_name());
            bundle.putParcelable("goodsTypeBig", goodsTypeBig);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return toolsList.size();
        }
    }


    /**
     * 改变textView的颜色
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextViews.length; i++) {
            if(i!=id){
                toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
                toolsTextViews[i].setTextColor(0xff000000);
            }
        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(0xffff5d5e);
    }


    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        scrollView.smoothScrollTo(0, x);
    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     *
     * @param view
     * @return
     */
    private int getViewheight(View view) {
        return view.getBottom() - view.getTop();
    }

    //获得类别
    private void getBigType() {
        StringRequest request = new StringRequest(
                Request.Method.GET,
                InternetURL.NAV_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    TypeBigData data = getGson().fromJson(s, TypeBigData.class);
                                        toolsList.addAll(data.getData());
                                        showToolsView();
                                }else {
                                    Toast.makeText(TypeActivity.this, R.string.reg_error_four, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(TypeActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(TypeActivity.this, R.string.get_no_data, Toast.LENGTH_SHORT).show();
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

    public void findSearch(View view){
        //
        Intent intent = new Intent(TypeActivity.this, SearchActivity.class);
        startActivity(intent);
    }



}
