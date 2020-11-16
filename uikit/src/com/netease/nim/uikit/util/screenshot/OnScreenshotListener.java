package com.netease.nim.uikit.util.screenshot;


import androidx.annotation.Nullable;

/**
 * the listener for listen screenshot event.
 *
 * @author zhuanghongji
 */
public interface OnScreenshotListener{

    /**
     * it will called if screenshot event happened.
     *
     * @param absolutePath the absolutePath of screenshot image file
     */
    void onScreenshot(@Nullable String absolutePath);
}
