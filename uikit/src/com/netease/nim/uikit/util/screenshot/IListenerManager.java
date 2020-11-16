package com.netease.nim.uikit.util.screenshot;

/**
 * the interface of ListenerManager
 */

public interface IListenerManager {

    /**
     * start listening for screenshots.
     */
    void startListen();

    /**
     * stop listening for screenshots.
     */
    void stopListen();

    /**
     * set the callback to notify {@link com.zhuanghongji.screenshot.lib.ScreenshotManager}
     * when screenshot event just happened.
     * @param callback the callback
     */
    void setListenerManagerCallback(IListenerManagerCallback callback);
}
