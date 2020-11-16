package com.netease.nim.uikit.util.screenshot;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;


/**
 * the {@link IListenerManager} Impl.
 * <br>It listens screenshot event by {@link ContentObserver}.
 *
 * @author zhuanghongji
 */

public class ContentObserverListenerManager implements IListenerManager {

    private static final String TAG = "ContentObserver";

    private static final String HANDLER_THREAD_NAME = "ContentObserver";

    private Context mContext;

    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap"
    };

    /** 读取媒体数据库时需要读取的列 */
    private static final String[] MEDIA_PROJECTIONS =  {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
    };


    /** 内部存储器内容观察者 */
    private ContentObserver mInternalObserver;

    /** 外部存储器内容观察者 */
    private ContentObserver mExternalObserver;

    private IListenerManagerCallback mOnListenerManagerCallback;

    public ContentObserverListenerManager(Context context) {
        mContext = context;

        HandlerThread handlerThread = new HandlerThread(HANDLER_THREAD_NAME);
        handlerThread.start();

        Handler handler = new Handler(handlerThread.getLooper());

        // 初始化
        mInternalObserver = new MediaContentObserver(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI, handler);
        mExternalObserver = new MediaContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, handler);
    }

    @Override
    public void startListen() {
        mContext.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                false,
                mInternalObserver
        );
        mContext.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false,
                mExternalObserver
        );
    }

    @Override
    public void stopListen() {
        mContext.getContentResolver().unregisterContentObserver(mInternalObserver);
        mContext.getContentResolver().unregisterContentObserver(mExternalObserver);
    }

    @Override
    public void setListenerManagerCallback(IListenerManagerCallback callback) {
        mOnListenerManagerCallback = callback;
    }

    private void handleMediaContentChange(Uri contentUri) {
        Cursor cursor = null;
        try {
            // 数据改变时查询数据库中最后加入的一条数据
            cursor = mContext.getContentResolver().query(
                    contentUri,
                    MEDIA_PROJECTIONS,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
            );

            if (cursor == null) {
                return;
            }
            if (!cursor.moveToFirst()) {
                return;
            }

            // 获取各列的索引
            int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);

            // 获取行数据
            String data = cursor.getString(dataIndex);
            long dateTaken = cursor.getLong(dateTakenIndex);

            // 处理获取到的第一行数据
            handleMediaRowData(data, dateTaken);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * 处理监听到的资源
     */
    private void handleMediaRowData(String data, long dateTaken) {
        if (checkScreenShot(data, dateTaken)) {
            MLog.v("data = %s, dateTaken = %s", data, dateTaken);
            if (mOnListenerManagerCallback != null) {
                mOnListenerManagerCallback.notifyScreenshotEvent(HANDLER_THREAD_NAME, data);
            }
        } else {
            MLog.v("Not screenshot event");
        }
    }

    /**
     * 判断是否是截屏
     */
    private boolean checkScreenShot(String data, long dateTaken) {
        data = data.toLowerCase();

        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        for (String keyWork : KEYWORDS) {
            if (data.contains(keyWork)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 媒体内容观察者(观察媒体数据库的改变)
     */
    private class MediaContentObserver extends ContentObserver {

        private Uri mContentUri;

        MediaContentObserver(Uri contentUri, Handler handler) {
            super(handler);
            mContentUri = contentUri;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            MLog.v("mContentUri = %s", mContentUri);
            handleMediaContentChange(mContentUri);
        }
    }
}
