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
import com.xiaogang.myapp.adapter.ItemtypesAdapter;
import com.xiaogang.myapp.adapter.OnClickContentItemListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/19
 * Time: 20:58
 * 选择更多分类
 */
public class SelectShiyongshijianWindow extends PopupWindow {
    private TextView  cancel;
    private TextView  sure;
    private ListView lstv;
    private ListView lstvTwo;
    private View mMenuView;
    private ItemtypesAdapter adapter;
    private ItemtypesAdapter adapterTwo;
    private List<String> listsType = new ArrayList<>();
    private List<String> listsTypeTwo = new ArrayList<>();
    private String select_one;//数字
    private String select_two;//年月日

    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public SelectShiyongshijianWindow(final Activity context,  View.OnClickListener itemsOnClic) {
        super(context);
        initListTypeDay();
        listsTypeTwo.add(context.getResources().getString(R.string.year));
        listsTypeTwo.add(context.getResources().getString(R.string.geyue));
        listsTypeTwo.add(context.getResources().getString(R.string.day));
        adapter = new ItemtypesAdapter(listsType, context);
        adapterTwo = new ItemtypesAdapter(listsTypeTwo, context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_shiyongshijian, null);
        cancel = (TextView) mMenuView.findViewById(R.id.cancel);
        sure = (TextView) mMenuView.findViewById(R.id.sure);
        lstv = (ListView) mMenuView.findViewById(R.id.lstv);
        lstvTwo = (ListView) mMenuView.findViewById(R.id.lstvTwo);
        lstv.setAdapter(adapter);
        lstvTwo.setAdapter(adapterTwo);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                select_one = listsType.get(i);
                Intent intent = new Intent("select_time_one");
                intent.putExtra("time_one", listsType.get(i));
                context.sendBroadcast(intent);
            }
        });
        lstvTwo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int i, long l) {
                select_two =  listsTypeTwo.get(i);
                Intent intent = new Intent("select_time_two");
                intent.putExtra("time_two", listsTypeTwo.get(i));
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

    void initListTypeDay(){
        listsType.clear();
        listsType.add("1");
        listsType.add("2");
        listsType.add("3");
        listsType.add("4");
        listsType.add("5");
        listsType.add("6");
        listsType.add("7");
        listsType.add("8");
        listsType.add("9");
        listsType.add("10");
        listsType.add("11");
        listsType.add("12");
        listsType.add("13");
        listsType.add("14");
        listsType.add("15");
        listsType.add("16");
        listsType.add("17");
        listsType.add("18");
        listsType.add("19");
        listsType.add("20");
        listsType.add("21");
        listsType.add("22");
        listsType.add("23");
        listsType.add("24");
        listsType.add("25");
        listsType.add("26");
        listsType.add("27");
        listsType.add("28");
        listsType.add("29");
        listsType.add("30");
        listsType.add("31");
    }
    void initListTypeMonth(){
        listsType.clear();
        listsType.add("1");
        listsType.add("2");
        listsType.add("3");
        listsType.add("4");
        listsType.add("5");
        listsType.add("6");
        listsType.add("7");
        listsType.add("8");
        listsType.add("9");
        listsType.add("10");
        listsType.add("11");
        listsType.add("12");
    }
    void initListTypeYear(){
        listsType.clear();
        listsType.add("1");
        listsType.add("2");
        listsType.add("3");
        listsType.add("4");
        listsType.add("5");
        listsType.add("6");
        listsType.add("7");
        listsType.add("8");
        listsType.add("9");
        listsType.add("10");
        listsType.add("11");
        listsType.add("12");
        listsType.add("13");
        listsType.add("14");
        listsType.add("15");
        listsType.add("16");
        listsType.add("17");
        listsType.add("18");
        listsType.add("19");
        listsType.add("20");
        listsType.add("21");
        listsType.add("22");
        listsType.add("23");
        listsType.add("24");
        listsType.add("25");
        listsType.add("26");
        listsType.add("27");
        listsType.add("28");
        listsType.add("29");
        listsType.add("30");
        listsType.add("31");
    }
}