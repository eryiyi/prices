package com.xiaogang.myapp.util.upload;

import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.security.MessageDigest;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1.二进制转换为十六进制
 * 2.E-mail 检测
 * 3.URL检测
 * 4.text是否包含空字符串
 * 5.添加对象数组
 *
 * @author dds
 */
public class StringUtil {
    //判断是否为JSOn格式
    public static boolean isJson(String json) {
        if (StringUtil.isNullOrEmpty(json)) {
            return false;
        }
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            return false;
        }
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 添加对象数组
     *
     * @param spliter 间隔符
     * @param arr     数组
     * @return
     */
    public static String join(String spliter, Object[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        if (spliter == null) {
            spliter = "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i == arr.length - 1) {
                break;
            }
            if (arr[i] == null) {
                continue;
            }
            builder.append(arr[i].toString());
            builder.append(spliter);
        }
        return builder.toString();
    }

    /**
     * java去除字符串中的空格、回车、换行符、制表符
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public final static String md5(String plainText) {
// 返回字符串
        String md5Str = null;
        try {
// 操作字符串
            StringBuffer buf = new StringBuffer();

            MessageDigest md = MessageDigest.getInstance("MD5");

// 添加要进行计算摘要的信息,使用 plainText 的 byte 数组更新摘要。
            md.update(plainText.getBytes());
// 计算出摘要,完成哈希计算。
            byte b[] = md.digest();
            int i;
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
// 将整型 十进制 i 转换为16位，用十六进制参数表示的无符号整数值的字符串表示形式。
                buf.append(Integer.toHexString(i));
            }
// 32位的加密
            md5Str = buf.toString();
// 16位的加密
// md5Str = buf.toString().md5Strstring(8,24);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5Str;
    }

    /**
     * 生成32位编码
     * @return string
     */
    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return uuid;
    }


    //求百度地图两点之间的距离
    static double DEF_PI = 3.14159265359; // PI
    static double DEF_2PI= 6.28318530712; // 2*PI
    static double DEF_PI180= 0.01745329252; // PI/180.0
    static double DEF_R =6370693.5; // radius of earth
    public static double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
    {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }


}
