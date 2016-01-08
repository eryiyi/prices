package com.xiaogang.myapp.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.util.upload.DataCleanManager;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2015/7/27.
 * 设置
 */
public class SetActivity extends BaseActivity implements View.OnClickListener {
    private ImageView type;
    private ImageView leftbutton;
    private LinearLayout set_liner_four;
    private LinearLayout set_liner_five;
    private LinearLayout set_liner_six;
    private LinearLayout set_liner_seven;
    private LinearLayout set_liner_eight;

    private ImageView switch_baidu;//百度地图选择
    private ImageView switch_tuisong;//推送消息选择

    private ProgressDialog progressDialog;
    private static final String ATTR_PACKAGE_STATS="PackageStats";
    private TextView huncun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_activity);
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() throws Exception {
        TextView version = (TextView) this.findViewById(R.id.version);
        version.setText(getVersionName()==null?"V1.0":getVersionName());
        type = (ImageView) this.findViewById(R.id.type);
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);
        set_liner_four = (LinearLayout) this.findViewById(R.id.set_liner_four);
        set_liner_five = (LinearLayout) this.findViewById(R.id.set_liner_five);
        set_liner_six = (LinearLayout) this.findViewById(R.id.set_liner_six);
        set_liner_seven = (LinearLayout) this.findViewById(R.id.set_liner_seven);
        set_liner_eight = (LinearLayout) this.findViewById(R.id.set_liner_eight);
        set_liner_four.setOnClickListener(this);
        set_liner_five.setOnClickListener(this);
        set_liner_six.setOnClickListener(this);
        set_liner_seven.setOnClickListener(this);
        set_liner_eight.setOnClickListener(this);
        type.setOnClickListener(this);
        switch_baidu = (ImageView) this.findViewById(R.id.switch_baidu);
        switch_tuisong = (ImageView) this.findViewById(R.id.switch_tuisong);
        switch_baidu.setOnClickListener(this);
        switch_tuisong.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case   R.id.type:
            {
                Intent typeView = new Intent(SetActivity.this, TypeActivity.class);
                startActivity(typeView);
            }

            case R.id.leftbutton:
                finish();
                break;
            case R.id.set_liner_four:
                //语言
                Intent language = new Intent(SetActivity.this, SelectLanguageActivity.class);
                startActivity(language);
                break;
            case R.id.set_liner_five:
                //清除临时文件
                //缓存
                DataCleanManager.cleanInternalCache(SetActivity.this);
                getpkginfo("com.xiaogang.myapp");
                Toast.makeText(SetActivity.this, "清除成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_liner_six:
                //服务条款
            {
                Intent intentFw = new Intent(SetActivity.this, SettingFuwuActivity.class);
                startActivity(intentFw);
            }
                break;
            case R.id.set_liner_seven:
            {
                //隐私政策
                Intent intentys = new Intent(SetActivity.this, SettingYinsiActivity.class);
                startActivity(intentys);
            }
                break;
            case R.id.switch_baidu:
                //百度地图选择  is_select_baidu  0默认是  1 否
                String is_select_baidu = getGson().fromJson(getSp().getString("is_select_baidu", ""), String.class);
                if("1".equals(is_select_baidu)){
                    //不使用
                    switch_baidu.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
                    save("is_select_baidu", "0");
                }else {
                    //使用百度地图
                    switch_baidu.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
                    save("is_select_baidu", "1");
                }
                break;
            case R.id.switch_tuisong:
                //是否使用推送 is_select_tuisong 0默认是  1否
                String is_select_tuisong = getGson().fromJson(getSp().getString("is_select_tuisong", ""), String.class);
                if("1".equals(is_select_tuisong)){
                    //不使用
                    switch_tuisong.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
                    save("is_select_tuisong", "0");
                }else {
                    //使用
                    switch_tuisong.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
                    save("is_select_tuisong", "1");
                }
                break;
            case R.id.set_liner_eight:
                Resources res = getBaseContext().getResources();
                String message = res.getString(R.string.check_new_version).toString();
                progressDialog = new ProgressDialog(SetActivity.this);
                progressDialog.setMessage(message);
                progressDialog.show();

                UmengUpdateAgent.forceUpdate(this);

                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                        progressDialog.dismiss();
                        switch (i) {
                            case UpdateStatus.Yes:
//                                Toast.makeText(mContext, "有新版本发现", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.No:
                                Toast.makeText(SetActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.Timeout:
                                Toast.makeText(SetActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                break;
        }
    }

    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String infoString="";
                    PackageStats newPs = msg.getData().getParcelable(ATTR_PACKAGE_STATS);
                    if (newPs!=null) {
//                        infoString+="应用程序大小: "+formatFileSize(newPs.codeSize);
//                        infoString+="\n数据大小: "+formatFileSize(newPs.dataSize);
                        infoString+= formatFileSize(newPs.cacheSize);
                    }
                    huncun.setText(infoString);
                    break;
                default:
                    break;
            }
        }
    };
    public void getpkginfo(String pkg){
        PackageManager pm = getPackageManager();
        try {
            Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
            getPackageSizeInfo.invoke(pm, pkg,new PkgSizeObserver());
        } catch (Exception e) {
        }
    }
    class PkgSizeObserver extends IPackageStatsObserver.Stub {
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) {
            Message msg = mHandler.obtainMessage(1);
            Bundle data = new Bundle();
            data.putParcelable(ATTR_PACKAGE_STATS, pStats);
            msg.setData(data);
            mHandler.sendMessage(msg);

        }
    }

    /**
     * 获取文件大小
     *
     * @param length
     * @return
     */
    public static String formatFileSize(long length) {
        String result = null;
        int sub_string = 0;
        if (length >= 1073741824) {
            sub_string = String.valueOf((float) length / 1073741824).indexOf(
                    ".");
            result = ((float) length / 1073741824 + "000").substring(0,
                    sub_string + 3)
                    + "GB";
        } else if (length >= 1048576) {
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0,
                    sub_string + 3)
                    + "MB";
        } else if (length >= 1024) {
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0,
                    sub_string + 3)
                    + "KB";
        } else if (length < 1024)
            result = Long.toString(length) + "B";
        return result;
    }
}
