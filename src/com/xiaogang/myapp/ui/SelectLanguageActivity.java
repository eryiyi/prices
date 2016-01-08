package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.BaseActivity;

import java.util.Locale;

/**
 * Created by Administrator on 2015/9/21.
 */
public class SelectLanguageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;
    private LinearLayout set_liner_one;
    private LinearLayout set_liner_two;
    private ImageView select_language_one;
    private ImageView select_language_two;
    private String is_language = "0";// 1繁体
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_language_activity);
        initView();
        is_language =  getGson().fromJson(getSp().getString("is_language", ""), String.class);

        initData();
    }

    private void initData() {
        if("1".endsWith(is_language==null?"":is_language)){
            select_language_one.setVisibility(View.GONE);
            select_language_two.setVisibility(View.VISIBLE);
        }else {
            select_language_one.setVisibility(View.VISIBLE);
            select_language_two.setVisibility(View.GONE);
        }
    }

    private void initView() {
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);
        set_liner_one = (LinearLayout) this.findViewById(R.id.set_liner_one);
        set_liner_two = (LinearLayout) this.findViewById(R.id.set_liner_two);
        set_liner_two.setOnClickListener(this);
        set_liner_one.setOnClickListener(this);

        select_language_one = (ImageView) this.findViewById(R.id.select_language_one);
        select_language_two = (ImageView) this.findViewById(R.id.select_language_two);
        select_language_one.setVisibility(View.GONE);
        select_language_two.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.leftbutton:
                finish();
                break;
            case R.id.set_liner_one:
            {
//                Locale locale = new Locale("zh_CN");
//                save("is_language", "0");
//                switchLanguage(locale);
            }
                break;
            case R.id.set_liner_two:
            {
//                Locale locale = new Locale("zh_TW");
//                save("is_language", "1");
//                switchLanguage(locale);
            }
                break;
        }
    }

    public void switchLanguage(Locale locale) {
        Configuration config = getResources().getConfiguration();// 获得设置对象
        Resources resources = getResources();// 获得res资源对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = locale; // 简体中文
        resources.updateConfiguration(config, dm);
    }

}
