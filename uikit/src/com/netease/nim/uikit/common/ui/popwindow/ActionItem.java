package com.netease.nim.uikit.common.ui.popwindow;

import android.content.Context;

public class ActionItem {

    //定义图片对象
    public int mDrawableId;
    //定义图片对象
    public int mTextColor;
    //定义文本对象
    public CharSequence mTitle;


    public ActionItem(Context context, int titleId, int drawableId){
        this.mTitle = context.getResources().getText(titleId);
        this.mDrawableId = drawableId;
    }

    public ActionItem(Context context, CharSequence title, int drawableId,int txtColor) {
        this.mTitle = title;
        this.mDrawableId = drawableId;
        this.mTextColor = txtColor;
    }

    public ActionItem(Context context, CharSequence title, int drawableId) {
        this.mTitle = title;
        this.mDrawableId = drawableId;
        this.mTextColor = -1;
    }

    public ActionItem(Context context, CharSequence title) {
        this.mTitle = title;
        this.mDrawableId = -1;
        this.mTextColor = -1;
    }

    public ActionItem( CharSequence title) {
        this.mTitle = title;
        this.mDrawableId = -1;
        this.mTextColor = -1;
    }
}
