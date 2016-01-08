package com.xiaogang.myapp.bean;

/**
 * Created by liuzwei on 2015/1/12.
 */
public class InternetURL {
    public static final String INTERNAL = "http://huangbin.wphl.net/index.php/";
    //register-MOBILE
    public static final String REG_MOBILE_URL = INTERNAL + "home/user/checkmobile";
    //register--emial
    public static final String REG_EMAIL_URL = INTERNAL + "home/user/checkemail";
   //获得手机验证码
    public static final String GET_CARD_URL = INTERNAL + "home/user/setcode";
    //获取验证码接口
    public static final String GET_CODE_URL = INTERNAL + "home/user/sendcode";
    //login
    public static final String LOGIN_URL = INTERNAL + "home/user/login";
    //5：访问商品主导航接口
    public static final String NAV_URL = INTERNAL + "home/nav";
    //6：访问商品子导航
    public static final String CHILD_NAV_URL = INTERNAL + "home/nav/getchildnav";
    //7：访问子导航下所有商品列表
    public static final String GOODS_URL = INTERNAL + "home/goods";
    //8:商品详情接口
    public static final String GOODS_DETAIL_URL = INTERNAL + "home/goods/getone";
    //9:商品评论页面
    public static final String COMMEND_URL = INTERNAL + "home/commend";
    //10:商品评论提交
    public static final String ADD_COMMEND_URL = INTERNAL + "home/commend/addcommend";
    //11:查看商品用家意见
    public static final String COMMEND_SHOW_URL = INTERNAL + "home/commend/show";
    //12:评论点赞
    public static final String COMMEND_PROVE_URL = INTERNAL + "home/commend/approve";
    //13:评论反对
    public static final String COMMEND_OPPOSE_URL = INTERNAL + "home/commend/oppose";
//    13:进入添加二手物品的页面
    public static final String USERD_URL = INTERNAL +"home/used";
    //13:添加二手物品
    public static final String ADD_USERD_URL = INTERNAL +"home/used/addused";
    //14:获取情报的分类
    public static final String GET_MESSAGE_TYPE_URL = INTERNAL + "home/sort";
    //15::获取一条分类下的所有文章
    public static final String GET_MESSAGE_LSTV_URL = INTERNAL + "home/sort/getlist";
    //16获取文章详情
    public static final String GET_MESSAGE_DETAIL_URL = INTERNAL + "home/sort/getonearticle";
    //16获取商品报价页面
    public static final String GET_BAOJIA_URL = INTERNAL + "home/sales";
    //17获取选定的商家的详细信息
    public static final String GET_SHANGJIA_URL = INTERNAL + "home/sales/getsales";
    //18添加报价
    public static final String ADD_BAOJIA_URL = INTERNAL + "home/sales/addsales";
    //19获取会员基本信息
    public static final String GET_MEMBER_URL = INTERNAL + "home/member";
    //20修改电子邮件
    public static final String UPDATE_EMAIL_URL = INTERNAL + "home/member/setemail";
    //21修改手机
    public static final String UPDATE_MOBILE_URL = INTERNAL + "home/member/setmobile";
    //22修改帐号名
    public static final String UPDATE_USER_URL = INTERNAL + "home/member/setuser";
    //23修改姓名
    public static final String UPDATE_NAME_URL = INTERNAL + "home/member/setname";
    //24修改性别
    public static final String UPDATE_SEX_URL = INTERNAL + "home/member/setsex";
    //24修改密码
    public static final String UPDATE_PWR_URL = INTERNAL + "home/member/setpass";
    //25修改头像
    public static final String UPDATE_FACE_URL = INTERNAL + "home/member/setface";
    //26会员退出
    public static final String LOGOUT_URL = INTERNAL + "home/member/loginout";
    //27筛选出此分类下的所有属性列表
    public static final String UPDATE_FILTERLIST_URL = INTERNAL + "home/goods/filtratelist";
    //28列出此项属性下的所有选项
    public static final String UPDATE_ALLATTR_URL = INTERNAL + "home/goods/getallattr";
    //29列出此类型下的所有品牌
    public static final String ALL_BRAND_URL = INTERNAL + "home/goods/getallbrand";
    //30列出此类型下的所有价格区间
    public static final String PRICES_ALL_URL = INTERNAL + "home/goods/getallprice";
    //31 二手商品列表
    public static final String SECOND_LSTV_URL = INTERNAL + "home/goods/usedlist";
    //32二手商品详情
    public static final String SECOND_DETAIL_URL = INTERNAL + "home/goods/oneused";
    //33商品搜索
    public static final String HOME_SEARCH_URL = INTERNAL + "home/goods/search";
    //34图片上传
    public static final String UPLOAD_FILE_URL = INTERNAL + "home/goods/photo";
    // 35打开价格主页面
//    public static final String FIND_PRICES_URL = INTERNAL + "home/goods/findprice";
    //36商品收藏
    public static final String SAVE_FAVOUR_URL = INTERNAL + "home/member/collect";

    //价格首页第一部份
    public static final String JIAGE_ONE_URL = INTERNAL + "home/goods/oneprice";
    //价格首页第二部份
    public static final String JIAGE_TWO_URL = INTERNAL + "home/goods/twoprice";
    //价格首页第三部份
    public static final String JIAGE_THREE_URL = INTERNAL + "home/goods/findprice";
    //显示收藏数量
    public static final String FAVOUR_COUNT_URL = INTERNAL + "home/member/allcollect";
    //显示收藏列表
    public static final String FAVOUR_LSTV_URL = INTERNAL + "home/member/collectlist";

    //1:上传证件照
//    public static final String SALES_UPLOAD_URL = INTERNAL + "home/sales/upload";
    //1:申请开店
    public static final String SALES_SHOP_URL = INTERNAL + "home/sales/shop";

    //返回商店类型
    public static final String GET_NAV_URL = INTERNAL + "home/nav/getnav";
    //2:进入报价页
    public static final String GET_HOMT_SALES_URL = INTERNAL + "homt/sales";
    //3:添加报价
    public static final String ADD_SALES_URL = INTERNAL + "homt/sales/addsales";

    //1:取得当前物品下面所有报价的商家列表
    public static final String ALL_SALES_URL = INTERNAL + "home/goods/allsales";
    //2:取得商店详情
    public static final String GET_DETAIL_SALES_URL = INTERNAL + "home/goods/onesales";
    //3:取得此商店下卖的所有物品
    public static final String GET_ALL_GOODS_URL = INTERNAL + "home/goods/allsg";
    //周围商家
    public static final String GET_ALL_SHANGJIA_NEARBY = INTERNAL + "home/index/nearby";
}
