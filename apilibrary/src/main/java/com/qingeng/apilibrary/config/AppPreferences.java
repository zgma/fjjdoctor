package com.qingeng.apilibrary.config;

import android.content.Context;
import android.content.SharedPreferences;


public class AppPreferences {


    private static final String KEY_USER_HTTP_TOKEN = "http_token";
    private static final String KEY_USER_IM_TOKEN = "im_token";

    private static final String KEY_USER_ID = "api_key_user_id";
    private static final String KEY_USER_ACCID = "api_key_user_accid";

    private static final String KEY_LOGIN_PHONE = "api_key_login_phone";
    private static final String KEY_USER_THEME_ID = "key_user_theme_id";
    private static final String KEY_READ_AGREEMENT = "read_agreement";
    public static final String TEXT_SIZE_CURRENT_INDEX = "textSizeCurrentIndex";
    private static final String KEY_USER_INFO = "USER_INFO";
    private static final String KEY_DOCTOR_INFO = "DOCTOR_INFO";
    private static final String KEY_QUIET_MODE = "KEY_QUIET_MODE";
    private static final String KEY_WX_LOGIN_CODE = "KEY_WX_LOGIN_CODE";




    private static Context context;

    public static void init(Context context){
        AppPreferences.context = context.getApplicationContext();
    }


    static SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("fjjdoctor", Context.MODE_PRIVATE);
    }


    private static boolean getBoolean(String key, boolean value) {
        return getSharedPreferences().getBoolean(key, value);
    }

    private static boolean getBoolean(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }


    private static void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }



    private static void saveLong(String key, long value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(key, value);
        editor.commit();
    }

    private static long getLong(String key, long value) {
        return getSharedPreferences().getLong(key, value);
    }




    private static void saveInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private static int getInt(String key, int value) {
        return getSharedPreferences().getInt(key, value);
    }


    private static int getInt(String key) {
        return getSharedPreferences().getInt(key, 0);
    }



    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }








    public static void saveHttpToken(String token) {
        saveString(KEY_USER_HTTP_TOKEN, token);
    }

    public static String getHttpToken() {
        return getString(KEY_USER_HTTP_TOKEN);
    }


    public static void saveImToken(String token) {
        saveString(KEY_USER_IM_TOKEN, token);
    }

    public static String getImToken() {
        return getString(KEY_USER_IM_TOKEN);
    }


    public static void saveUserId(int userId) {
        saveInt(KEY_USER_ID, userId);
    }

    public static int getUserId() {
        return getInt(KEY_USER_ID);
    }


    public static void saveLoginPhone(String phone) {
        saveString(KEY_LOGIN_PHONE, phone);
    }

    public static String getLoginPhone() {
        return getString(KEY_LOGIN_PHONE);
    }

    public static void saveAccId(String token) {
        saveString(KEY_USER_ACCID, token);
    }

    public static String getAccId() {
        return getString(KEY_USER_ACCID);
    }



    public static void saveThemeId(int themeId) {
        saveInt(KEY_USER_THEME_ID, themeId);
    }

    public static int getThemeId() {
        return getInt(KEY_USER_THEME_ID)==0?1:getInt(KEY_USER_THEME_ID);
    }


    public static void saveReadAgreement(boolean o) {
        saveBoolean(KEY_READ_AGREEMENT, o);
    }

    public static boolean getReadAgreement() {
        return getBoolean(KEY_READ_AGREEMENT);
    }


    public static void setTextSizeSetting(int size) {
        saveInt(TEXT_SIZE_CURRENT_INDEX, size);
    }

    public static int getTextSizeSetting() {
        return getInt(TEXT_SIZE_CURRENT_INDEX, 0);
    }


    public static void saveUserInfo(String userInfoString) {
        saveString(KEY_USER_INFO, userInfoString);
    }

    public static String getUserInfo() {
        return getString(KEY_USER_INFO);
    }

    public static void saveDoctorInfo(String userInfoString) {
        saveString(KEY_DOCTOR_INFO, userInfoString);
    }

    public static String getDoctorInfo() {
        return getString(KEY_DOCTOR_INFO);
    }


}
