package com.xiaogang.myapp.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import com.xiaogang.myapp.R;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/19
 * Time: 20:58
 * 类的功能、说明写在此处.
 */
public class SelectDistanceWindow extends PopupWindow {
    private View mMenuView;
    private SeekBar seekBar;
    private SeekBar seekBar_two;
    private Context context;


    public SelectDistanceWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.item_dialog_select_distance, null);
        this.context = context;
        seekBar = (SeekBar) mMenuView.findViewById(R.id.seekBar);
        seekBar_two = (SeekBar) mMenuView.findViewById(R.id.seekBar_two);
        seekBar.setOnSeekBarChangeListener(seekbarChangeListener);
        seekBar.setOnSeekBarChangeListener(seekbarChangeListenerTwo);
        //设置按钮监听
//        camera.setOnClickListener(itemsOnClick);
//        mapstorage.setOnClickListener(itemsOnClick);

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

    private SeekBar.OnSeekBarChangeListener seekbarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        // 停止拖动时执行
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
        }

        // 在进度开始改变时执行
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
//            textView_two.setText("进度开始改变");
        }

        // 当进度发生改变时执行
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
//            textView_two.setText("正在进行拖动操作，还没有停下来一直再拖动");
            Message message = new Message();

            Bundle bundle = new Bundle();// 存放数据

            float pro = seekBar.getProgress();

            float num = seekBar.getMax();

            float result = (pro / num) * 100;
            bundle.putFloat("key", result);

            message.setData(bundle);

            message.what = 0;

            handler.sendMessage(message);
//
//            Toast.makeText(context,
//                    String.valueOf(seekBar.getProgress()),
//                    Toast.LENGTH_SHORT).show();


        }
    };

    /**
     * 用Handler来更新UI
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            textView_one.setText(msg.getData().getFloat("key") + "/100");
            Toast.makeText(context, msg.getData().getFloat("key")+"/100" , Toast.LENGTH_SHORT).show();
        }
    };

    private SeekBar.OnSeekBarChangeListener seekbarChangeListenerTwo = new SeekBar.OnSeekBarChangeListener() {

        // 停止拖动时执行
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
//            textView_two.setText("停止拖动了！");

        }

        // 在进度开始改变时执行
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
//            textView_two.setText("进度开始改变");
        }

        // 当进度发生改变时执行
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
//            textView_two.setText("正在进行拖动操作，还没有停下来一直再拖动");
            Message message = new Message();

            Bundle bundle = new Bundle();// 存放数据

            float pro = seekBar.getProgress();

            float num = seekBar.getMax();

            float result = (pro / num) * 100;
            bundle.putFloat("key", result);

            message.setData(bundle);

            message.what = 0;

            handlertwo.sendMessage(message);

        }
    };

    /**
     * 用Handler来更新UI
     */
    private Handler handlertwo = new Handler() {
        @Override
        public void handleMessage(Message msg) {

//            Toast.makeText(context, msg.getData().getFloat("key")+"/1000" , Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent("send_seek");
            intent1.putExtra("key_seek", msg.getData().getFloat("key"));
            context.sendBroadcast(intent1);
        }

    };
}