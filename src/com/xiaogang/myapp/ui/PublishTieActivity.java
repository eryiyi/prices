package com.xiaogang.myapp.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import com.xiaogang.myapp.R;
import com.xiaogang.myapp.adapter.Publish_mood_GridView_Adapter;
import com.xiaogang.myapp.bean.BaseActivity;
import com.xiaogang.myapp.util.upload.CommonDefine;
import com.xiaogang.myapp.util.upload.FileUtils;
import com.xiaogang.myapp.util.upload.ImageUtils;
import com.xiaogang.myapp.widget.NoScrollGridView;
import com.xiaogang.myapp.widget.SelectPhoTwoPopWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/28.
 * 发帖
 */
public class PublishTieActivity extends BaseActivity implements View.OnClickListener {
    private ImageView back;
    private ImageView add_pic;
    private SelectPhoTwoPopWindow popWindow;
    private final static int SELECT_LOCAL_PHOTO = 110;
    private NoScrollGridView publish_moopd_gridview_image;//图片
    private Publish_mood_GridView_Adapter adapter;
    private ArrayList<String> dataList = new ArrayList<String>();
    private ArrayList<String> tDataList = new ArrayList<String>();
    private List<String> uploadPaths = new ArrayList<String>();
    private ProgressDialog progressDialog;
    private static int REQUEST_CODE = 1;
    private Uri uri;
    public EditText et_sendmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_tie_xml);
        initView();
    }

    private void initView() {
        back = (ImageView) this.findViewById(R.id.back);
        add_pic = (ImageView) this.findViewById(R.id.add_pic);
        back.setOnClickListener(this);
        add_pic.setOnClickListener(this);
        publish_moopd_gridview_image = (NoScrollGridView) this.findViewById(R.id.publish_moopd_gridview_image);
        adapter = new Publish_mood_GridView_Adapter(this, dataList);
        publish_moopd_gridview_image.setAdapter(adapter);
        publish_moopd_gridview_image.setOnItemClickListener(new GridView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String path = dataList.get(position);
                if (path.contains("camera_default") && position == dataList.size() - 1 && dataList.size() - 1 != 9) {
                    showSelectImageDialog();
                } else {
                    Intent intent = new Intent(PublishTieActivity.this, ImageDelActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("path", dataList.get(position));
                    startActivityForResult(intent, CommonDefine.DELETE_IMAGE);
                }
            }
        });
        et_sendmessage = (EditText) findViewById(R.id.face_content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.add_pic:
                //添加
                showSelectImageDialog();
                break;
        }
    }
    // 选择相册，相机
    private void showSelectImageDialog() {
        popWindow = new SelectPhoTwoPopWindow(PublishTieActivity.this, itemsOnClick);
        //显示窗口
        popWindow.showAtLocation(PublishTieActivity.this.findViewById(R.id.main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent();
        cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件
        File file = new File(CommonDefine.FILE_PATH);
        if (file.exists()) {
            file.delete();
        }
        uri = Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        // 开启系统拍照的Activity
        startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
    }

    private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {

        ArrayList<String> tDataList = new ArrayList<String>();

        for (String s : dataList) {
//            if (!s.contains("camera_default")) {
            tDataList.add(s);
//            }
        }
        return tDataList;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SELECT_LOCAL_PHOTO:
                    tDataList = data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                        }
//                        if (dataList.size() < 9) {
//                            dataList.add("camera_default");
//                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        finish();
                    }
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_CAMERA:
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        return;
                    }
                    Bitmap bitmap = ImageUtils.getUriBitmap(this, uri, 400, 400);
                    String cameraImagePath = FileUtils.saveBitToSD(bitmap, System.currentTimeMillis() + ".jpg");

                    dataList.add(cameraImagePath);
                    adapter.notifyDataSetChanged();
                    break;
                case CommonDefine.TAKE_PICTURE_FROM_GALLERY:
                    tDataList = data.getStringArrayListExtra("datalist");
                    if (tDataList != null) {
                        dataList.clear();
                        for (int i = 0; i < tDataList.size(); i++) {
                            String string = tDataList.get(i);
                            dataList.add(string);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case CommonDefine.DELETE_IMAGE:
                    int position = data.getIntExtra("position", -1);
                    dataList.remove(position);
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            popWindow.dismiss();
            switch (v.getId()) {
                case R.id.camera: {
                    Intent cameraIntent = new Intent();
                    cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    // 根据文件地址创建文件
                    File file = new File(CommonDefine.FILE_PATH);
                    if (file.exists()) {
                        file.delete();
                    }
                    uri = Uri.fromFile(file);
                    // 设置系统相机拍摄照片完成后图片文件的存放地址
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    // 开启系统拍照的Activity
                    startActivityForResult(cameraIntent, CommonDefine.TAKE_PICTURE_FROM_CAMERA);
                }
                break;
                case R.id.mapstorage: {
                    Intent intent = new Intent(PublishTieActivity.this, AlbumActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("dataList", getIntentArrayList(dataList));
                    bundle.putString("editContent", et_sendmessage.getText().toString());
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CommonDefine.TAKE_PICTURE_FROM_GALLERY);
                }
                break;
                default:
                    break;
            }
        }

    };

}
