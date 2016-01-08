package com.xiaogang.myapp.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xiaogang.myapp.PricesApplication;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.AnimateFirstDisplayListener;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.bean.InternetURL;
import com.xiaogang.myapp.data.NewsSingleData;
import com.xiaogang.myapp.model.News;
import com.xiaogang.myapp.util.upload.StringUtil;
import com.xiaogang.myapp.widget.URLImageParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/30.
 */
public class NewsDetailActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView jianjie;
    private ImageView pic;
    private TextView cont;
    private TextView dateline;
    private ImageView leftbutton;
    private String id;
    public static DisplayMetrics displayMetrics;
    private String htmlContent;//����

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//ͼƬ������

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayMetrics=getApplicationContext().getResources().getDisplayMetrics();
        id = getIntent().getExtras().getString("id");
        setContentView(R.layout.news_detail_activity);
        initView();

        getData();
    }

    private void initView() {
        title = (TextView) this.findViewById(R.id.title);
        jianjie = (TextView) this.findViewById(R.id.jianjie);
        pic = (ImageView) this.findViewById(R.id.pic);
        cont = (TextView) this.findViewById(R.id.cont);
        dateline = (TextView) this.findViewById(R.id.dateline);
        leftbutton = (ImageView) this.findViewById(R.id.leftbutton);
        leftbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.leftbutton:
                finish();
                break;
        }
    }

    public void getData() {
        String uri = InternetURL.GET_MESSAGE_DETAIL_URL + "?id=" + id ;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            try {
                                JSONObject jo = new JSONObject(s);
                                String code1 =  jo.getString("code");
                                if(Integer.parseInt(code1) == 200){
                                    NewsSingleData data = getGson().fromJson(s, NewsSingleData.class);
                                    News news =data.getData();
                                    title.setText(news.getTitle());
                                    jianjie.setText(news.getBrief());
//                                    cont.setText(news.getContent());
                                    htmlContent = ( news.getContent()==null?"":news.getContent());
//                                    if (htmlContent.indexOf("src=\"http") != -1) {//����https
//
//                                    }else{
//                                        if(htmlContent.indexOf("src=\"/ueditor") != -1){//��Ҫ���https
//                                            String str = "src=\"" + InternetURL.INTERNAL + "";
//                                            htmlContent = htmlContent.replaceAll("src=\"", str);
//                                        }
//                                    }
//                                    initContent(cont, htmlContent);
                                    cont.setText(Html.fromHtml(htmlContent));
                                    dateline.setText(
                                            getResources().getString(R.string.xinwengaoriqi)+ news.getDate());
                                    imageLoader.displayImage(news.getThumbnail(), pic,
                                            PricesApplication.options, animateFirstListener);
                                }else {
                                    Toast.makeText(NewsDetailActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }else {
                            Toast.makeText(NewsDetailActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(NewsDetailActivity.this, R.string.no_data, Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    /**
     * ����html����
     * @param tv
     * @param s
     */
    private void initContent(TextView tv, String s)
    {
        tv.setMovementMethod(ScrollingMovementMethod.getInstance());// ���ÿɹ���
        tv.setMovementMethod(LinkMovementMethod.getInstance());//���ó����ӿ��Դ���ҳ
        tv.setText(Html.fromHtml(s, new URLImageParser(cont, getApplicationContext()), null));
    }

}
