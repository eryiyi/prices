package com.xiaogang.myapp.util.upload;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;

public class FileUtils {

    /**
     * 保存图片到指定的目录
     *
     * @param bit
     * @param fileName 文件名
     * @return
     */
    public static String saveBitToSD(Bitmap bit, String fileName) {
        if (bit == null || bit.isRecycled()) return "";

        File file = new File(Environment.getExternalStorageDirectory(), "/uniapp/photoCache");
        File dirFile = new File(file.getAbsolutePath());
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File pathFile = new File(dirFile, fileName);
        if (pathFile.exists()) {
            return pathFile.getAbsolutePath();
        } else {
            ImageUtils.Bitmap2File(bit, pathFile.getAbsolutePath());
            return pathFile.getAbsolutePath();
        }
    }
}
