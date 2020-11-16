package com.qingeng.fjjdoctor.util;


import com.alibaba.fastjson.JSON;
import com.qingeng.apilibrary.bean.DoctorInfoBean;
import com.qingeng.apilibrary.bean.DoctorInfoResultBean;
import com.qingeng.apilibrary.bean.UserInfoBean;
import com.qingeng.apilibrary.bean.UserWalletBean;
import com.qingeng.apilibrary.config.AppPreferences;

public class LocalDataUtils {

/*    public static UserBean getLocalUser(){
        return JSON.parseObject(AppPreferences.getUserInfo(),UserBean.class);
    }

    public static void saveLocalUser(Object o){
        AppPreferences.saveUserInfo(JSON.toJSONString(o));
    }*/

    public static UserInfoBean getUserInfo() {
        return getLocalDoctor().getUser();
    }

    public static DoctorInfoBean getDoctorInfo() {
        return getLocalDoctor().getDoctorInfo();
    }

    public static UserWalletBean getWalletInfo() {
        return getLocalDoctor().getUserWallet();
    }

    private static DoctorInfoResultBean getLocalDoctor() {
        return JSON.parseObject(AppPreferences.getDoctorInfo(), DoctorInfoResultBean.class);
    }

    public static void saveLocalDoctor(Object o) {
        AppPreferences.saveDoctorInfo(JSON.toJSONString(o));
    }


}
