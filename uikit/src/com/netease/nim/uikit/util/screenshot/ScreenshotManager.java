package com.netease.nim.uikit.util.screenshot;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * the manager of screenshot.
 * <p>
 * Please make sure you have the permission of reading external storage.
 * <p/>
 *
 * @author zhuanghongji
 */

public class ScreenshotManager {

    private static final String TAG = "ScreenshotManager";

    private FileObserverListenerManager mFileObserverListenerManager;

    private List<IListenerManager> mListenerManagers;

    private OnScreenshotListener mOnScreenshotListener;

    private String mAbsolutePathOfLastScreenshot;

    public ScreenshotManager(Context context) {
        mListenerManagers = new ArrayList<>();

        mFileObserverListenerManager = new FileObserverListenerManager();
        addCustomListenerManager(mFileObserverListenerManager);
        addCustomListenerManager(new ContentObserverListenerManager(context));
    }

    /**
     * start the listener for listening screenshots.
     * <p>
     * </p>
     */
    public void startListen() {
        for (IListenerManager manager : mListenerManagers) {
            manager.startListen();
        }
    }

    /**
     * stop the listener for listening screenshots.
     * <p>
     * </p>
     */
    public void stopListen() {
        for (IListenerManager manager : mListenerManagers) {
            manager.stopListen();
        }
    }

    /**
     * if you want to listen dir witch didn't add in
     * add it by this method.
     *
     * @param screenshotDir the dir you want to listen
     */
    public void addScreenshotDirectories(final String screenshotDir) {
        mFileObserverListenerManager.addScreenshotDirectories(screenshotDir);
    }

    /**
     * if the listener implemented by {@link android.os.FileObserver} or
     * {@link android.database.ContentObserver} can not meet your requirements. you can
     * custom your own {@link IListenerManager} just like {@link FileObserverListenerManager} or
     * {@link ContentObserverListenerManager} and add to {@link ScreenshotManager} by this method.
     *
     * @param manager your own custom {@link IListenerManager}
     */
    public void addCustomListenerManager(IListenerManager manager) {
        mListenerManagers.add(manager);
        manager.setListenerManagerCallback(new IListenerManagerCallback() {
            @Override
            public void notifyScreenshotEvent(String listenerManagerName, @Nullable String absolutePath) {
                MLog.v("listenerManagerName = %s, absolutePath = %s",
                        listenerManagerName, absolutePath);

                if (TextUtils.isEmpty(absolutePath)) {
                    MLog.v("there is something wrong because absolutePath is empty.");
                    return;
                }
                if (mOnScreenshotListener != null
                        && !absolutePath.equals(mAbsolutePathOfLastScreenshot)) {
                    mAbsolutePathOfLastScreenshot = absolutePath;
                    mOnScreenshotListener.onScreenshot(absolutePath);
                }
            }
        });
    }

    public void setOnScreenshotListener(OnScreenshotListener onScreenshotListener) {
        mOnScreenshotListener = onScreenshotListener;
    }

    public static void enableLog(boolean enable) {
        MLog.enableLog(enable);
    }
}
