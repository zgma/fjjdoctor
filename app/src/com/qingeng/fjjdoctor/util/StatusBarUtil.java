package com.qingeng.fjjdoctor.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.qingeng.fjjdoctor.widget.StatusBarView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import qiu.niorgai.StatusBarCompat;

import static com.qingeng.fjjdoctor.util.UiUtils.getStatusBarHeight;


public class StatusBarUtil {

    /**
     * 设置状态栏模式
     * @param activity
     * @param isTextDark 文字、图标是否为黑色 （false为默认的白色）
     * @param colorId 状态栏颜色
     * @return
     */
    public static void setStatusBarMode(Activity activity, boolean isTextDark, int colorId) {
        if(!isTextDark) {
            //文字、图标颜色不变，只修改状态栏颜色
            StatusBarCompat.setStatusBarColor(activity, colorId);
        } else {
            //修改状态栏颜色和文字图标颜色
            StatusBarCompat.setStatusBarColor(activity, colorId);
            //4.4以上才可以改文字图标颜色
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if(OSUtil.isMIUI()) {
                    //小米MIUI系统
                    setMIUIStatusBarTextMode(activity, isTextDark);
                } else if(OSUtil.isFlyme()) {
                    //魅族flyme系统
                    setFlymeStatusBarTextMode(activity, isTextDark);
                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //6.0以上，调用系统方法
                    Window window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    //4.4以上6.0以下的其他系统，暂时没有修改状态栏的文字图标颜色的方法，有可以加上
//                    StatusBarCompat.setStatusBarColor(activity,Color.argb(60, 0, 0, 0));
                    //如果以上都不符合就只能加个半透明的背景色了
                    setTranslucentForCoordinatorLayout(activity, 50);
                }
            }
        }
    }

    public static void setStatusBarTranslucentMode(Activity activity, boolean isTextDark){
        if(!isTextDark) {
            //文字、图标颜色不变，只修改状态栏颜色
            StatusBarCompat.translucentStatusBar(activity);
        } else {
            //修改状态栏颜色和文字图标颜色
            StatusBarCompat.translucentStatusBar(activity);
            //4.4以上才可以改文字图标颜色
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if(OSUtil.isMIUI()) {
                    //小米MIUI系统
                    setMIUIStatusBarTextMode(activity, isTextDark);
                } else if(OSUtil.isFlyme()) {
                    //魅族flyme系统
                    setFlymeStatusBarTextMode(activity, isTextDark);
                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //6.0以上，调用系统方法
                    Window window = activity.getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    //4.4以上6.0以下的其他系统，暂时没有修改状态栏的文字图标颜色的方法，有可以加上
//                    StatusBarCompat.setStatusBarColor(activity,Color.argb(60, 0, 0, 0));
                    //如果以上都不符合就只能加个半透明的背景色了
                    setTranslucentForCoordinatorLayout(activity, 50);
                }
            }
        }
    }


    //带有透明颜色的状态栏
    public static void setTranslucentForCoordinatorLayout(Activity activity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
//        StatusBarCompat.translucentStatusBar(activity);
        transparentStatusBar(activity);//先将状态栏设置为完全透明
        addTranslucentView(activity, statusBarAlpha);//添加一个自定义透明度的矩形状态栏
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 添加半透明矩形条
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private static void addTranslucentView(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        // 移除半透明矩形,以免叠加
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }
    }


    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private static StatusBarView createTranslucentStatusBarView(Activity activity, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }




    /**
     * 设置Flyme系统状态栏的文字图标颜色
     * @param activity
     * @param isDark 状态栏文字及图标是否为深色
     * @return
     */
    public static boolean setFlymeStatusBarTextMode(Activity activity, boolean isDark) {
        Window window = activity.getWindow();
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (isDark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置MIUI系统状态栏的文字图标颜色（MIUIV6以上）
     * @param activity
     * @param isDark 状态栏文字及图标是否为深色
     * @return
     */
    public static boolean setMIUIStatusBarTextMode(Activity activity, boolean isDark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (isDark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (isDark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View
                                .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View
                                .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View
                                .SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {

            }
        }
        return result;
    }



    /**
     * Android 6.0 以上设置状态栏颜色
     */
    public static void setStatusBarDark(Activity activity, boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 如果亮色，设置状态栏文字为黑色
            if (isDark) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }

    }


}
