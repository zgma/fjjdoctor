package com.netease.nim.uikit.business.session.actions;

import android.app.Activity;
import android.content.Intent;

import com.netease.nim.uikit.business.session.module.Container;
import com.netease.nim.uikit.business.session.module.Container_Many;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Action基类。<br>
 * 注意：在子类中调用startActivityForResult时，requestCode必须用makeRequestCode封装一遍，否则不能再onActivityResult中收到结果。
 * requestCode仅能使用最低8位。
 */
public abstract class BaseAction implements Serializable {

    private int iconResId;

    private int titleId;

    private transient int index;

    // Container持有activity ， 防止内存泄露
    private transient WeakReference<Container> containerRef;
    // Container持有activity ， 防止内存泄露
    private transient WeakReference<Container_Many> containerRef_many;

    /**
     * 构造函数
     *
     * @param iconResId 图标 res id
     * @param titleId   图标标题的string res id
     */
    protected BaseAction(int iconResId, int titleId) {
        this.iconResId = iconResId;
        this.titleId = titleId;
    }

    public Activity getActivity() {
        return getContainer().activity;
    }

    public String getAccount() {
        return getContainer().account;
    }

    public SessionTypeEnum getSessionType() {
        return getContainer().sessionType;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getTitleId() {
        return titleId;
    }

    public Container getContainer() {
        Container container = null;
        if (containerRef ==null){
            Container_Many container_many = containerRef_many.get();
            if (container_many!=null){
                String accounts = String.join(",", container_many.accounts);
                container = new Container(container_many.activity,accounts,container_many.sessionType,container_many.proxy);
            }
        }else {
            container = containerRef.get();
        }
        if (container == null){
            throw new RuntimeException("container be recycled by vm ");
        }
        return container;
    }

    public abstract void onClick();

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // default: empty
    }

    protected void sendMessage(IMMessage message) {
        getContainer().proxy.sendMessage(message);
    }

    protected int makeRequestCode(int requestCode) {
        if ((requestCode & 0xffffff00) != 0) {
            throw new IllegalArgumentException("Can only use lower 8 bits for requestCode");
        }
        return ((index + 1) << 8) + (requestCode & 0xff);
    }

    public void setContainer(Container container) {
        this.containerRef = new WeakReference<>(container);
    }

    public void setContainer_many(Container_Many container) {
        this.containerRef_many = new WeakReference<>(container);
    }

    public void setIndex(int index) {
        this.index = index;
    }

}
