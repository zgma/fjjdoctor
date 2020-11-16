package com.netease.nim.uikit.util.screenshot;


import androidx.annotation.Nullable;

/**
 * the callback for {@link IListenerManager} Impl to notify
 * when screenshot event just happened.
 *
 * @author zhuanghongji
 */

public interface IListenerManagerCallback {

    /**
     * @param listenerManagerName the ListenManager which catch screenshot event first
     * @param absolutePath the absolutePath of screenshot image file
     */
    void notifyScreenshotEvent(String listenerManagerName, @Nullable String absolutePath);
}
