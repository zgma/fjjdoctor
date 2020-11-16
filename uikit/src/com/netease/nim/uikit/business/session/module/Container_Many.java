package com.netease.nim.uikit.business.session.module;

import android.app.Activity;

import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.util.ArrayList;

/**
 * Created by zhoujianghua on 2015/7/6.
 */
public class Container_Many {

    public final Activity activity;

    public final ArrayList<String> accounts;

    public final SessionTypeEnum sessionType;

    public final ModuleProxy proxy;

    public final boolean proxySend;

    public Container_Many(Activity activity, ArrayList<String> account, SessionTypeEnum sessionType, ModuleProxy proxy,
                          boolean proxySend) {
        this.activity = activity;
        this.accounts = account;
        this.sessionType = sessionType;
        this.proxy = proxy;
        this.proxySend = proxySend;
    }
}
