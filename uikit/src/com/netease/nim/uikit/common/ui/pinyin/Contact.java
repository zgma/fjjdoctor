package com.netease.nim.uikit.common.ui.pinyin;


import android.text.TextUtils;

import com.netease.nim.uikit.common.ui.pinyin.cn.CN;

/**
 * Created by you on 2017/9/11.
 */

public class Contact implements CN {

    public String name;

    public String alias;

    public String id;

    public String imgUrl;

    public int rid;

    public String mobile;

    public Contact(String name, String id, String imgUrl, String alias) {
        this.name = name;
        this.id = id;
        this.imgUrl = imgUrl;
        this.alias = alias;
    }

    public Contact(String name, String id, int rid, String alias) {
        this.name = name;
        this.id = id;
        this.rid = rid;
        this.alias = alias;
    }
    public Contact(String name, String id, String imgUrl, String alias,String mobile) {
        this.name = name;
        this.id = id;
        this.imgUrl = imgUrl;
        this.alias = alias;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String chinese() {
        return TextUtils.isEmpty(alias)?name:alias;
    }






}
