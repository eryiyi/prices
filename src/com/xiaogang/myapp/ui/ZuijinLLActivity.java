package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemZuijinllAdapter;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.db.DBHelper;
import com.xiaogang.myapp.db.LiulanSmart;
import com.xiaogang.myapp.model.Deng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/27.
 * 最近浏览
 */
public class ZuijinLLActivity extends BaseActivity implements View.OnClickListener {
    private ImageView leftbutton;
    private ListView lstv;
    private List<LiulanSmart> lists =  new ArrayList<>();
    private ItemZuijinllAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zuijinll_activity);
        lists = DBHelper.getInstance(ZuijinLLActivity.this).getShoppingList();
        initView();
    }

    private void initView() {
        adapter = new ItemZuijinllAdapter(lists, ZuijinLLActivity.this);
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        lstv = (ListView) this.findViewById(R.id.lstv);
        lstv.setAdapter(adapter);
        leftbutton.setOnClickListener(this);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                LiulanSmart liulanSmart = lists.get(position);
                Intent detailView = new Intent(ZuijinLLActivity.this, DetailGoodsActivity.class);
                detailView.putExtra("id", liulanSmart.getGoods_id());
                startActivity(detailView);
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
        }
    }
}
