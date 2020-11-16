package com.qingeng.apilibrary.contact;

import android.os.Environment;

public class MainConstant {

    public final static String COMMON_FILE_PATH = "/fjjdoctor";
    public static final String WX_APP_ID = "wx3e3b2bf3adb7f121";

    //微信支付成功通知EventBus标识
    public static final String PAY_SUCCESS_WX = "PAY_SUCCESS_WX";
    public static final String LOGIN_SUCCESS_WX = "LOGIN_SUCCESS_WX";

    public static final String DOWNLOAD_DIRECTORY = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fastchat/download/";

    public final static int PAGE_NUMBER  = 10;

    /**
     * 用户类型 10=会员 20=医生 30=医生助手
     */
    public final static String USER_TYPE  = "20";


}
