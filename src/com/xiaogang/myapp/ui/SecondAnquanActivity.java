package com.xiaogang.myapp.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.widget.MyTextView2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2015/10/4.
 */
public class SecondAnquanActivity extends BaseActivity implements View.OnClickListener{
    MyTextView2 view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_anquan_activity);
        view = (MyTextView2) findViewById(R.id.view);
        view.setText(getAssetsString(this,"3.txt"));
    }

    public String getAssetsString(Context context,String fileName){
        StringBuffer sb = new StringBuffer();
        try {
            AssetManager am = context.getAssets();
            InputStream in = am.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine())!=null){
                line += ("\n");
                sb.append(line);
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void onClick(View view) {

    }
    public void back(View view){
        finish();
    }
}
