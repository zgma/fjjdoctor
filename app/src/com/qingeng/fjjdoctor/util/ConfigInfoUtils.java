package com.qingeng.fjjdoctor.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.UUID;

/**
 * C/**
 * 作者： zy
 * 时间： 2016/7/5
 * 邮箱：270268361@qq.com
 * 描述：软件更新
 */
public class ConfigInfoUtils {

    /**
     * 获取版本号
     *
     * @param mContext
     * @return
     */
    public static int getLocalVersion(Context mContext) {
        try {
            PackageInfo packageInfo = mContext.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(
                            mContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取版本名
     *
     * @return
     */
    public static String getLocalVersionName(Context mContext) {
        try {
            PackageInfo packageInfo = mContext.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    /**
     * Pseudo-Unique ID, 这个在任何Android手机中都有效
     * 有一些特殊的情况，一些如平板电脑的设置没有通话功能，或者你不愿加入READ_PHONE_STATE许可。
     * 而你仍然想获得唯一序列号之类的东西。这时你可以通过取出ROM版本、制造商、CPU型号、以及其他硬件信息来实现这一点。
     * 这样计算出来的ID不是唯一的（因为如果两个手机应用了同样的硬件以及Rom 镜像）。
     * 但应当明白的是，出现类似情况的可能性基本可以忽略。
     * 要实现这一点，你可以使用Build类:
     **/
    public static String getDecidShort() {
        String serial = null;
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
