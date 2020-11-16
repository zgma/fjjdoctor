package com.netease.nim.uikit.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.recyclerview.widget.RecyclerView;

public class UiUtils {
    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static Drawable getDrawable(Context context,int id) {
        return context.getResources().getDrawable(id);
    }

    public static int getColor(Context context,int id) {
        return context.getResources().getColor(id);
    }

    public static int getDimension(Context context,int id) {
        return context.getResources().getDimensionPixelSize(id);
    }

    public static Integer getInteger(Context context,int id) {
        return context.getResources().getInteger(id);
    }

    public static String getString(Context context,int id) {
        return context.getResources().getString(id);
    }


    public static String[] getStringArray(Context context,int id) {
        return context.getResources().getStringArray(id);
    }




    /**
     * 按设计比例设置banner高度
     * @return
     */
    public static int getBannerHeight(Context context)  {
        return (int) (getScreenWidth(context) * (0.42668));
    }

    public static int getHomeCardMargin(Context context)  {
        return (int) (getScreenWidth(context) * (0.067));
    }

    public static int getHomeCardSize(Context context)  {
        return (int) (getScreenWidth(context) * (0.4));
    }

    public static int getMaterialImgWidth(Context context){
        return (int) (getScreenWidth(context) * (0.32));
    }

    public static int getMaterialImgHeight(Context context){
        return (int) (getMaterialImgWidth(context) * (0.64));
    }


    public static int getScreenWidth(Context context){
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }



    public static int dpTpPx(Context context,float value) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }


    public static int getPublishImageWidth(Context context){
        int a = (int) ((getScreenWidth(context)  / 3));
        System.out.println("getPublishImageWidth--1---"+dpTpPx(context,16 * 2 + 10 * 2));
        System.out.println("getPublishImageWidth--2---"+getScreenWidth(context)+"-----"+a);
        return a;
    }

    /**
     * 收起软键盘
     *
     * @param context 上下文对象
     * @param v       视图
     */
    public static void hideKeyforard(View v, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }


    /**
     * 判断是否到底部了
     *
     * @param recyclerView
     * @return
     * */
    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }



}
