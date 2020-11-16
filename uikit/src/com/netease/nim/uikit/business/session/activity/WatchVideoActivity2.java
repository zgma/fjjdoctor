package com.netease.nim.uikit.business.session.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;

import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.http.DownloadInterface;
import com.qingeng.apilibrary.http.HttpClient;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;

/**
 * 视频播放界面
 * <p/>
 * Created by huangjun on 2015/4/11.
 */
public class WatchVideoActivity2 extends UI implements Callback {
    public static final String INTENT_EXTRA_DATA = "EXTRA_DATA";
    public static final String INTENT_EXTRA_MENU = "EXTRA_MENU";

    // player

    private MediaPlayer mediaPlayer;

    // context

    private Handler handlerTimes = new Handler();

    private ActionBar actionBar;

    // view

    private SurfaceView surfaceView;

    private SurfaceHolder surfaceHolder;

    private View videoIcon;

    private View downloadLayout;

    private View downloadProgressBackground;

    private View downloadProgressForeground;

    private TextView downloadProgressText;

    protected TextView fileInfoTextView;

    private TextView playTimeTextView;

    String url;
    // state
    private boolean isShowMenu = true;

    private boolean isSurfaceCreated = false;

    protected String videoFilePath;

    protected long videoLength = 0;


    private int playState = PLAY_STATE_STOP;

    private final static int PLAY_STATE_PLAYING = 1;

    private final static int PLAY_STATE_STOP = 2;

    private final static int PLAY_STATE_PAUSE = 3;

    private boolean downloading;


    // download control
    private ImageView downloadBtn;

    public static void start(Context context, String url) {
        Intent intent = new Intent();
        intent.putExtra(WatchVideoActivity2.INTENT_EXTRA_DATA, url);
        intent.setClass(context, WatchVideoActivity2.class);
        context.startActivity(intent);
    }

    public static void start(Context context, IMMessage message, boolean isShowMenu) {
        Intent intent = new Intent();
        intent.putExtra(WatchVideoActivity2.INTENT_EXTRA_DATA, message);
        intent.putExtra(INTENT_EXTRA_MENU, isShowMenu);
        intent.setClass(context, WatchVideoActivity2.class);
        context.startActivity(intent);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nim_watch_video_activity);

        ToolBarOptions options = new NimToolBarOptions();
        options.navigateId = R.drawable.nim_actionbar_white_back_icon;
        setToolBar(R.id.toolbar, options);

        parseIntent();
        findViews();
        initActionbar();

        showVideoInfo();

        startPlay(url);
    }



    public void onResume() {
        super.onResume();
        mediaPlayer = new MediaPlayer();

        if (isSurfaceCreated) {
            playVideo();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        stopMediaPlayer();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void parseIntent() {
        url = getIntent().getStringExtra(INTENT_EXTRA_DATA);
        setTitle("收藏视频");
        isShowMenu = getIntent().getBooleanExtra(INTENT_EXTRA_MENU, true);
    }

    private void findViews() {
        downloadLayout = findViewById(R.id.layoutDownload);
        downloadProgressBackground = findViewById(R.id.downloadProgressBackground);
        downloadProgressForeground = findViewById(R.id.downloadProgressForeground);
        downloadProgressText = (TextView) findViewById(R.id.downloadProgressText);
        videoIcon = findViewById(R.id.videoIcon);

        surfaceView = (SurfaceView) findViewById(R.id.videoView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(this);

        playTimeTextView = (TextView) findViewById(R.id.lblVideoTimes);
        playTimeTextView.setVisibility(View.INVISIBLE);
        fileInfoTextView = (TextView) findViewById(R.id.lblVideoFileInfo);
        playTimeTextView.setVisibility(View.INVISIBLE);

        downloadBtn = (ImageView) findViewById(R.id.control_download_btn);
        downloadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlay(url);
                downloadBtn.setImageResource(downloading ? R.drawable.nim_icon_download_pause : R.drawable.nim_icon_download_resume);
            }
        });

        actionBar = getSupportActionBar();
    }

    private void initActionbar() {
        TextView menuBtn = findView(R.id.actionbar_menu);
        if (isShowMenu) {
            menuBtn.setVisibility(View.VISIBLE);
            menuBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    WatchPicAndVideoMenuActivity.startActivity(WatchVideoActivity2.this, message);
                }
            });
        } else {
            menuBtn.setVisibility(View.GONE);
        }
    }

    private void initVideoSize() {
        if (mediaPlayer == null) {
            return;
        }
        // 视频宽高
        int width = mediaPlayer.getVideoWidth();
        int height = mediaPlayer.getVideoHeight();

        if (width <= 0 || height <= 0) {
            return;
        }

        // 屏幕宽高
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        int videoRatio = width / height;
        int screenRatio = screenWidth / screenHeight;

        if (screenRatio > videoRatio) {
            int newHeight = screenHeight;
            int newWidth = screenHeight * width / height;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    newWidth, newHeight);
            int margin = (screenWidth - newWidth) / 2;
            layoutParams.setMargins(margin, 0, margin, 0);
            surfaceView.setLayoutParams(layoutParams);
        } else {
            int newWidth = screenWidth;
            int newHeight = screenWidth * height / width;
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    newWidth, newHeight);
            int margin = (screenHeight - newHeight) / 2;
            layoutParams.setMargins(0, margin, 0, margin);
            surfaceView.setLayoutParams(layoutParams);
        }
    }

    /**
     * ****************************** MediaPlayer Start ********************************
     */
    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            actionBar.show();

        }
    }

    /**
     * 处理视频播放时间
     */
    private Runnable timeRunnable = new Runnable() {
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                playState = PLAY_STATE_PLAYING;

                if (videoLength <= 0) {
                    playTimeTextView.setVisibility(View.INVISIBLE);
                } else {
                    // 由于mediaPlayer取到的时间不统一,采用消息体中的时间
                    int leftTimes = (int) (videoLength * 1000 - mediaPlayer.getCurrentPosition());
                    if (leftTimes < 0) {
                        leftTimes = 0;
                    }

                    playTimeTextView.setVisibility(View.VISIBLE);
                    long seconds = TimeUtil.getSecondsByMilliseconds(leftTimes);
                    playTimeTextView.setText(TimeUtil.secToTime((int) seconds));
                    handlerTimes.postDelayed(this, 1000);
                }

            }
        }
    };

    protected void pauseVideo() {
        videoIcon.setVisibility(View.VISIBLE);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            handlerTimes.removeCallbacks(timeRunnable);
            playState = PLAY_STATE_PAUSE;
            actionBar.show();
        }
    }

    protected void resumeVideo() {
        videoIcon.setVisibility(View.GONE);
        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                playState = PLAY_STATE_PLAYING;
                handlerTimes.postDelayed(timeRunnable, 100);
                actionBar.hide();
            }
        }
    }

    protected void playVideo() {
        videoIcon.setVisibility(View.GONE);
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            } else {
                if (isSurfaceCreated) {
                    mediaPlayer.setDisplay(surfaceHolder);
                } else {
                    ToastHelper.showToast(WatchVideoActivity2.this, R.string.surface_has_not_been_created);
                    return;
                }
            }
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(videoFilePath);
            } catch (Exception e) {
                ToastHelper.showToast(WatchVideoActivity2.this, R.string.look_video_fail_try_again);
                e.printStackTrace();
                return;
            }

            setMediaPlayerListener();
            mediaPlayer.prepareAsync();
            actionBar.hide();
        }
    }

    private void setMediaPlayerListener() {
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoIcon.setVisibility(View.VISIBLE);

                playState = PLAY_STATE_STOP;
                playTimeTextView.setText("00:00");
                handlerTimes.removeCallbacks(timeRunnable);
                actionBar.show();
            }
        });

        mediaPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    String type = "video/3gp";
                    Uri name = Uri.parse("file://" + videoFilePath);
                    intent.setDataAndType(name, type);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    ToastHelper.showToastLong(WatchVideoActivity2.this, R.string.look_video_fail);
                }
                return true;
            }
        });

        mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp)// 缓冲完毕
            {
                mediaPlayer.start();// 播放视频
                initVideoSize();//根据视频宽高，调整视频显示
                // if (position > 0) {
                // mediaPlayer.seekTo(position);
                // mediaPlayer.start();
                // }
                handlerTimes.postDelayed(timeRunnable, 100);
            }
        });
    }

    /**
     * **************************** 下载视频 *********************************
     */
    private void showVideoInfo() {
    }

    private void startPlay(String url) {
        String path = "";
        if (url.endsWith(".mp4")) {
            path = url;
        } else if (url.endsWith("mp4")) {
            path = url.replace("mp4", ".mp4");
        } else {
            return;
        }


        String fileName = path.substring(path.lastIndexOf("/") + 1);
        File file = new File(MainConstant.DOWNLOAD_DIRECTORY + fileName);
        if (file.exists()) {
            playVideo(file.getPath());
        } else {
            download(url,file.getPath());
        }
    }


    private void download(String url, String filePath) {
        downloading = true;
        HttpClient.download(url, filePath, new DownloadInterface() {
            @Override
            public void onSuccess(boolean isSuccess) {
                if (isSuccess) {
                    playVideo(filePath);
                }
            }

            @Override
            public void onFailure(String message) {
                ToastHelper.showToast(WatchVideoActivity2.this, "下载视频失败！");
            }

            @Override
            public void onComplete() {
                downloading=false;
            }
        });
    }

    private void playVideo(String filePath) {

        downloadLayout.setVisibility(View.GONE);

        videoFilePath =filePath;

        surfaceView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (playState == PLAY_STATE_PAUSE) {
                    resumeVideo();
                } else if (playState == PLAY_STATE_PLAYING) {
                    pauseVideo();
                } else if (playState == PLAY_STATE_STOP) {
                    playVideo();
                }
            }
        });
        playVideo();
    }



    /**
     * ***************************** SurfaceHolder Callback **************************************
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        ToastHelper.showToast(this, R.string.surface_created);
        if (!isSurfaceCreated) {
            isSurfaceCreated = true;
            playVideo();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isSurfaceCreated = false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initVideoSize();// 屏幕旋转后，改变视频显示布局
    }


}
