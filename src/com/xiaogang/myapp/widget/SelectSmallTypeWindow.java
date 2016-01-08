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

import java.util.List;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/19
 * Time: 20:58
 * 选择更多分类
 */
public class SelectSmallTypeWindow extends PopupWindow {
    private TextView  cancel;
    private ListView lstv;
    private ImageView no_goods;
    private View mMenuView;
    private ItemtypesAdapter adapter;

    private OnClickContentItemListener onClickContentItemListener;
    public void setOnClickContentItemListener(OnClickContentItemListener onClickContentItemListener) {
        this.onClickContentItemListener = onClickContentItemListener;
    }

    public SelectSmallTypeWindow(final Activity context,final List<String> listsType) {
        super(context);
        adapter = new ItemtypesAdapter(listsType, context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_small_type, null);
        cancel = (TextView) mMenuView.findViewById(R.id.cancel);
        lstv = (ListView) mMenuView.findViewById(R.id.lstv);
        lstv.setAdapter(adapter);
        lstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int i, long l) {
                Intent intent = new Intent("select_attr");
                intent.putExtra("attr", listsType.get(i));
                context.sendBroadcast(intent);
            }
        });
        no_goods = (ImageView) mMenuView.findViewById(R.id.no_goods);

        if(listsType !=null && listsType.size()>0){
            no_goods.setVisibility(View.GONE);
            lstv.setVisibility(View.VISIBLE);
        }else{
            no_goods.setVisibility(View.VISIBLE);
            lstv.setVisibility(View.GONE);
        }
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