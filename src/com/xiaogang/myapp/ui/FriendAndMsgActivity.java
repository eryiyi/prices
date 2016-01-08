package com.xiaogang.myapp.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.fragment.MsgFragment;
import com.xiaogang.myapp.fragment.PersonFragment;

/**
 * Created by Administrator on 2015/7/28.
 */
public class FriendAndMsgActivity extends BaseActivity implements View.OnClickListener {
    protected static final String TAG = "FriendAndMsgActivity";
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fm;

    private MsgFragment oneFragment;//xiaoxi
    private PersonFragment twoFragment;//person

    private RelativeLayout foot_msg;
    private RelativeLayout foot_person;

    private TextView foot_msg_img;
    private TextView foot_person_img;;

    //设置底部图标
    Drawable img_one, img_two,
            img_one_pressed, img_two_pressed;
    Resources res;
    private ImageView leftbutton;
    private TextView fatie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendandmsg_activity);
        fatie = (TextView) this.findViewById(R.id.fatie);
        fatie.setOnClickListener(this);
        res = getResources();
        img_one = res.getDrawable(R.drawable.msg_normal);
        img_one.setBounds(0, 0, img_one.getMinimumWidth(), img_one.getMinimumHeight());
        img_two = res.getDrawable(R.drawable.person_normal);
        img_two.setBounds(0, 0, img_two.getMinimumWidth(), img_two.getMinimumHeight());
        img_one_pressed = res.getDrawable(R.drawable.msg_press);
        img_one_pressed.setBounds(0, 0, img_one_pressed.getMinimumWidth(), img_one_pressed.getMinimumHeight());
        img_two_pressed = res.getDrawable(R.drawable.person_press);
        img_two_pressed.setBounds(0, 0, img_two_pressed.getMinimumWidth(), img_two_pressed.getMinimumHeight());
        fm = getSupportFragmentManager();


        foot_msg = (RelativeLayout) this.findViewById(R.id.foot_msg);
        foot_person = (RelativeLayout) this.findViewById(R.id.foot_person);

        foot_msg.setOnClickListener(this);
        foot_person.setOnClickListener(this);

        foot_msg_img = (TextView) this.findViewById(R.id.foot_msg_img);
        foot_person_img = (TextView) this.findViewById(R.id.foot_person_img);

        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);
        switchFragment(R.id.foot_msg);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.leftbutton){
            finish();
            return;
        }
        if(v.getId() == R.id.fatie){
            Intent fatieView = new Intent(FriendAndMsgActivity.this, PublishTieActivity.class);
            startActivity(fatieView);
            return;
        }
        switchFragment(v.getId());
    }

    public void switchFragment(int id) {
        fragmentTransaction = fm.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (id) {
            case R.id.foot_msg:
                if (oneFragment == null) {
                    oneFragment = new MsgFragment();
                    fragmentTransaction.add(R.id.content_frame, oneFragment);
                } else {
                    fragmentTransaction.show(oneFragment);
                }
                foot_msg_img.setCompoundDrawables(null, img_one_pressed, null, null);
                foot_person_img.setCompoundDrawables(null, img_two, null, null);

                foot_msg_img.setTextColor(this.getResources().getColor(R.color.msg_color_press));
                foot_person_img.setTextColor(this.getResources().getColor(R.color.textcolortwo));

                break;
            case R.id.foot_person:
                if (twoFragment == null) {
                    twoFragment = new PersonFragment();
                    fragmentTransaction.add(R.id.content_frame, twoFragment);
                } else {
                    fragmentTransaction.show(twoFragment);
                }

                foot_msg_img.setCompoundDrawables(null, img_one, null, null);
                foot_person_img.setCompoundDrawables(null, img_two_pressed, null, null);

                foot_msg_img.setTextColor(this.getResources().getColor(R.color.textcolortwo));
                foot_person_img.setTextColor(this.getResources().getColor(R.color.msg_color_press));

                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragments(FragmentTransaction ft) {
        if (oneFragment != null) {
            ft.hide(oneFragment);
        }
        if (twoFragment != null) {
            ft.hide(twoFragment);
        }

    }
}
