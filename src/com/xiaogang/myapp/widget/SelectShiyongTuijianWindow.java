package com.xiaogang.myapp.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.ItemShiyongchengduAdapter;
import com.xiaogang.myapp.adapter.OnClickContentItemListener;
import com.xiaogang.myapp.model.Chengdu;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/19
 * Time: 20:58
 1:极力推荐
 2：推荐
 3:不推荐
 4:没有意见

 */
public class SelectShiyongTuijianWindow extends PopupWindow {
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
    public SelectShiyongTuijianWindow(final Activity context, View.OnClickListener itemsOnClic) {
        super(context);
        lists.add(new Chengdu("1", context.getResources().getString(R.string.jilituijian)));
        lists.add(new Chengdu("2",context.getResources().getString(R.string.tuijian)));
        lists.add(new Chengdu("3", context.getResources().getString(R.string.butuijian)));
        lists.add(new Chengdu("4", context.getResources().getString(R.string.meiyouyijian)));
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
                Intent intent = new Intent("select_tuijian");
                intent.putExtra("tuijian", lists.get(i));
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