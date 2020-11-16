package com.netease.nim.uikit.util.screenshot;

import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;


import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * the {@link IListenerManager} Impl.
 * <br>It listens screenshot event by {@link FileObserver}.
 *
 * @author zhuanghongji
 */

public class FileObserverListenerManager implements IListenerManager {

    private static final String TAG = "FileObserver";

    private static final String LISTENER_MANAGER_NAME = "FileObserver";

    /**
     * it make the {@link OnScreenshotListener#onScreenshot(String)} execute on ui thread.
     */
    private Handler mHandler;

    private List<Integer> mScreenshotEvents;

    /**
     * the collection of screenshot dir on most brand mobile-phone.
     */
    private List<String> mScreenshotDirectories;

    /**
     * the collection of {@link FileObserver} witch you are listened
     * and it's created by {@link #mScreenshotDirectories}.
     */
    private List<FileObserver> mFileObservers;

    private IListenerManagerCallback mOnListenerManagerCallback;

    public FileObserverListenerManager() {
        mHandler = new Handler();
        mScreenshotEvents = new ArrayList<>();
        mScreenshotDirectories = new ArrayList<>();
        mFileObservers = new ArrayList<>();

        initScreenshotEvents();
        initScreenshotDirectories();
    }

    @Override
    public void startListen() {
        // Monitors the screenshot directories
        for (FileObserver observer : mFileObservers) {
            observer.startWatching();
        }
    }

    @Override
    public void stopListen() {
        for (FileObserver observer : mFileObservers) {
            observer.stopWatching();
        }
    }

    @Override
    public void setListenerManagerCallback(IListenerManagerCallback callback) {
        mOnListenerManagerCallback = callback;
    }

    /**
     * here we assume CLOSE_WRITE/CLOSE_NOWRITE event is screenshot event
     */
    private void initScreenshotEvents() {
        mScreenshotEvents.add(FileObserver.CLOSE_WRITE);
        mScreenshotEvents.add(FileObserver.CLOSE_NOWRITE);
    }

    private void initScreenshotDirectories() {
        File pictures = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File sdCard = Environment.getExternalStorageDirectory();

        // common dir
        File screenshots = new File(pictures, "Screenshots");
        addScreenshotDirectories(screenshots.getPath());

        // common dir
        File screenCapture = new File(sdCard, "ScreenCapture");
        addScreenshotDirectories(screenCapture.getPath());

        // xiaomi
        File xiaomi = new File(dcim, "Screenshots");
        addScreenshotDirectories(xiaomi.getPath());
    }

    /**
     * if you want to listen dir witch didn't add in {@link #initScreenshotDirectories()}, you can
     * add it by this method.
     *
     * @param screenshotDir the dir you want to listen
     */
    public void addScreenshotDirectories(final String screenshotDir) {
        mScreenshotDirectories.add(screenshotDir);
        mFileObservers.add(new FileObserver(screenshotDir, FileObserver.ALL_EVENTS) {
            @Override
            public void onEvent(int event, @Nullable final String path) {
                final String desc = getEventDesc(event);
                final String absolutePath = screenshotDir + File.separator + path;
                MLog.v("event = %s, desc = %s, absolutePath = %s", event, desc, absolutePath);

                if (mScreenshotEvents.contains(event)) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnListenerManagerCallback != null) {
                                mOnListenerManagerCallback.notifyScreenshotEvent(
                                        LISTENER_MANAGER_NAME, absolutePath);
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * get the description of FileObserver event
     * @param event the event for match description
     * @return the description string of event
     */
    private String getEventDesc(int event) {
        String desc;
        switch (event) {
            case FileObserver.ACCESS:
                desc = "ACCESS: Data was read from a file";
                break;
            case FileObserver.MODIFY:
                desc = "MODIFY: Data was written to a file";
                break;
            case FileObserver.ATTRIB:
                desc = "ATTRIB: Metadata (permissions, owner, timestamp) was changed explicitly";
                break;
            case FileObserver.CLOSE_WRITE:
                desc = "CLOSE_WRITE: Someone had a file or directory open for writing, and closed it";
                break;
            case FileObserver.CLOSE_NOWRITE:
                desc = "CLOSE_NOWRITE: Someone had a file or directory open read-only, and closed it";
                break;
            case FileObserver.OPEN:
                desc = "OPEN: A file or directory was opened";
                break;
            case FileObserver.MOVED_FROM:
                desc = "MOVED_FROM: A file or subdirectory was moved from the monitored directory";
                break;
            case FileObserver.MOVED_TO:
                desc = "MOVED_TO: A file or subdirectory was moved to the monitored directory";
                break;
            case FileObserver.CREATE:
                desc = "CREATE: A new file or subdirectory was created under the monitored directory";
                break;
            case FileObserver.DELETE:
                desc = "DELETE: A file was deleted from the monitored directory";
                break;
            case FileObserver.DELETE_SELF:
                desc = "DELETE_SELF: The monitored file or directory was deleted; monitoring effectively stops";
                break;
            case FileObserver.MOVE_SELF:
                desc = "MOVE_SELF: The monitored file or directory was moved; monitoring continues";
                break;
            default:
                desc = "No match event";
                break;

        }
        return desc;
    }

    /**
     * add the event of {@link FileObserver} which you think it's screenshot event but not init
     * in {@link #initScreenshotEvents()}
     * @param event the event you want to add
     */
    public void addScreenshotEvent(int event) {
        mScreenshotEvents.add(event);
    }

    public List<String> getScreenshotDirectories() {
        return mScreenshotDirectories;
    }
}
