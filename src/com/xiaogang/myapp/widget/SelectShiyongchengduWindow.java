package com.xiaogang.myapp.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemShiyongchengduAdapter;
import com.xiaogang.myapp.adapter.ItemtypesSortAdapter;
import com.xiaogang.myapp.adapter.OnClickContentItemListener;
import com.xiaogang.myapp.model.Chengdu;
import com.xiaogang.myapp.model.SortType;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/19
 * Time: 20:58
 商品熟悉程度
 1:专业
 2：进阶
 3:一般
 4:新手
 */
public class SelectShiyongchengduWindow extends PopupWindow {
    private TextView cancel;
    private TextView sure;
    private ListView lstv;
    private View mMenuView;
    private ItemShiyongchengduAdapter adapter;
    private List<Chengdu> lists = new ArrayList<>();
    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }
    public SelectShiyongchengduWindow(final Activity context, View.OnClickListener itemsOnClic) {
        super(context);
        lists.add(new Chengdu("1", context.getResources().getString(R.string.zhuanye)));
        lists.add(new Chengdu("2", context.getResources().getString(R.string.jinjie)));
        lists.add(new Chengdu("3", context.getResources().getString(R.string.yiban)));
        lists.add(new Chengdu("4", context.getResources().getString(R.string.xinshou)));
        adapter = new ItemShiyongchengduAdapter(lists, context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_shiyongchengdutype, null);
        sure = (TextView) mMenuView.findViewById(R.id.sure);
        cancel = (TextView) mMenuView.findViewById(R.id.cancel);
        lstv = (ListView) mMenuView.findViewById(R.id.lstv);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Intent intent = new Intent("select_shiyongchengdu");
                intent.putExtra("shiyongchengdu", lists.get(i));
                context.sendBroadcast(intent);
            }
        });
        sure.setOnClickListener(itemsOnClic);

        //取消按钮
        cancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}