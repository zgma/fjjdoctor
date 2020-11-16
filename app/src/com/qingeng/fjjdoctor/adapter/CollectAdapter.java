package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qingeng.apilibrary.bean.CollectBean;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.http.DownloadInterface;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.fjjdoctor.R;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.imageview.MsgThumbImageView;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.media.player.AudioPlayer;
import com.netease.nimlib.sdk.media.player.OnPlayListener;

import java.io.File;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.ViewHolder> {

    private Context context;

    private CollectAdapter.Listener listener;

    private boolean isSendCollect = false;

    public void setSendCollect(boolean sendCollect) {
        isSendCollect = sendCollect;
    }

    public void setListener(CollectAdapter.Listener listener) {
        this.listener = listener;
    }

    public CollectAdapter(Context context) {
        this.context = context;
    }

    private List<CollectBean> collectBeans;

    public void setCollectBeans(List<CollectBean> collectBeans) {
        this.collectBeans = collectBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_collect, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CollectBean collectBean = collectBeans.get(i);
        if (collectBean.getTypeDesc().equals("图片")) {
            viewHolder.content.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.containerView.setVisibility(View.GONE);
            viewHolder.video.setVisibility(View.GONE);

            Glide.with(context.getApplicationContext()).asBitmap().load(collectBean.getContent()).into(viewHolder.imageView);
        } else if (collectBean.getTypeDesc().equals("文字")) {
            viewHolder.content.setVisibility(View.VISIBLE);
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.containerView.setVisibility(View.GONE);
            viewHolder.video.setVisibility(View.GONE);

            viewHolder.content.setText(collectBean.getContent());
        } else if (collectBean.getTypeDesc().equals("语音")) {
            viewHolder.content.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.containerView.setVisibility(View.VISIBLE);
            viewHolder.video.setVisibility(View.GONE);

            viewHolder.durationLabel.setText("...\"");
            viewHolder.content.setText(collectBean.getContent());
            layoutByDirection(viewHolder);
        } else if (collectBean.getTypeDesc().equals("视频")) {
            viewHolder.content.setVisibility(View.GONE);
            viewHolder.imageView.setVisibility(View.GONE);
            viewHolder.containerView.setVisibility(View.GONE);
            viewHolder.video.setVisibility(View.VISIBLE);

            showVideoImg(collectBean.getContent(),viewHolder);
        }
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectBean.getTypeDesc().equals("语音") && !isSendCollect){
                    startAudio(collectBean.getContent(),viewHolder);
                }else {
                    if (listener != null) {
                        listener.onItemClick(collectBean);
                    }
                }
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDelete(collectBean);
                }
            }
        });

        viewHolder.time.setText(TimeUtil.getTimeShowString(TimeUtil.getDateFromFormatString(collectBean.getCreateDate(), TimeUtil.DATAFORMATSTRING_yyyyMMddHHmmss), false));
    }

    @Override
    public int getItemCount() {
        return collectBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView content;
        private TextView delete;
        private TextView time;
        private ImageView imageView;
        private RelativeLayout root;
        private MsgThumbImageView thumbnail;

        private TextView durationLabel;
        private View containerView;
        private ImageView animationView;
        private FrameLayout video;
        private RelativeLayout audio;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
            delete = (TextView) itemView.findViewById(R.id.delete);
            time = (TextView) itemView.findViewById(R.id.time);
            root = (RelativeLayout) itemView.findViewById(R.id.root);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            audio = (RelativeLayout) itemView.findViewById(R.id.audio);
            video = (FrameLayout) itemView.findViewById(R.id.video);

            durationLabel = itemView.findViewById(R.id.message_item_audio_duration);
            containerView = itemView.findViewById(R.id.message_item_audio_container);
            animationView = itemView.findViewById(R.id.message_item_audio_playing_animation);
            animationView.setBackgroundResource(0);

            thumbnail = itemView.findViewById(R.id.message_item_thumb_thumbnail);
        }
    }

    public interface Listener {
        void onItemClick(CollectBean collectBean);

        void onDelete(CollectBean collectBean);
    }


    private void layoutByDirection(ViewHolder viewHolder) {

        setGravity(viewHolder.animationView, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        setGravity(viewHolder.durationLabel, Gravity.LEFT | Gravity.CENTER_VERTICAL);

        viewHolder.containerView.setBackgroundResource(NimUIKitImpl.getOptions().messageRightBackground);
        viewHolder.containerView.setPadding(ScreenUtil.dip2px(10), ScreenUtil.dip2px(8), ScreenUtil.dip2px(15), ScreenUtil.dip2px(8));
        viewHolder.animationView.setBackgroundResource(com.netease.nim.uikit.R.drawable.nim_audio_animation_list_right_3);
        viewHolder.durationLabel.setTextColor(Color.WHITE);

    }

    // 设置FrameLayout子控件的gravity参数
    protected final void setGravity(View view, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = gravity;
    }

    private void startAudio(String url,ViewHolder viewHolder) {
        String path = "";
        if (url.endsWith(".aac")) {
            path = url;
        } else if (url.endsWith("aac")) {
            path = url.replace("aac", ".aac");
        } else {
            return;
        }


        String fileName = path.substring(path.lastIndexOf("/") + 1);
        File file = new File(MainConstant.DOWNLOAD_DIRECTORY + fileName);
        if (file.exists()) {
            playAudio(file.getPath() ,viewHolder);
        } else {
            download(url,file.getPath(),viewHolder);
        }
    }

    private void download(String url, String filePath,ViewHolder viewHolder) {
        HttpClient.download(url, filePath, new DownloadInterface() {
            @Override
            public void onSuccess(boolean isSuccess) {
                if (isSuccess) {
                    playAudio(filePath,viewHolder);
                }
            }

            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void playAudio(String path,ViewHolder viewHolder) {

        // 定义一个播放进程回调类
        OnPlayListener listener = new OnPlayListener() {

            // 音频转码解码完成，会马上开始播放了
            public void onPrepared() {

            }

            // 播放结束
            public void onCompletion() {
            }

            // 播放被中断了
            public void onInterrupt() {
            }

            // 播放过程中出错。参数为出错原因描述
            public void onError(String error) {
            }

            // 播放进度报告，每隔 500ms 会回调一次，告诉当前进度。 参数为当前进度，单位为毫秒，可用于更新 UI
            public void onPlaying(long curPosition) {
            }
        };
        // 构造播放器对象
        AudioPlayer player = new AudioPlayer(context, path, listener);

        // 开始播放。需要传入一个 Stream Type 参数，表示是用听筒播放还是扬声器。取值可参见
        // android.media.AudioManager#STREAM_***
        // AudioManager.STREAM_VOICE_CALL 表示使用听筒模式
        // AudioManager.STREAM_MUSIC 表示使用扬声器模式
        player.start(AudioManager.STREAM_MUSIC);
        // 如果中途切换播放设备，重新调用 start，传入指定的 streamType 即可。player 会自动停止播放，然后再以新的 streamType 重新开始播放。
        // 如果需要从中断的地方继续播放，需要外面自己记住已经播放过的位置，然后在 onPrepared 回调中调用 seekTo
        //player.seekTo(pausedPosition);
        // 主动停止播放
        //player.stop();

    }

    private void updateTime(TextView text,long milliseconds) {
        long seconds = TimeUtil.getSecondsByMilliseconds(milliseconds);
        if (seconds >= 0) {
            text.setText(seconds + "\"");
        } else {
            text.setText("");
        }
    }


    private void loadThumbnailImage(String path, boolean isOriginal, String ext,ViewHolder viewHolder) {
        if (path != null) {
            viewHolder.thumbnail.loadAsPath(path, getImageMaxEdge(), getImageMaxEdge(), maskBg(), ext);
        } else {
            viewHolder.thumbnail.loadAsResource(com.netease.nim.uikit.R.drawable.nim_image_default, maskBg());
        }
    }

    public static int getImageMaxEdge() {
        return (int) (165.0 / 320.0 * ScreenUtil.screenWidth);
    }

    public static int getImageMinEdge() {
        return (int) (76.0 / 320.0 * ScreenUtil.screenWidth);
    }

    private int maskBg() {
        return com.netease.nim.uikit.R.drawable.nim_message_item_round_bg;
    }

    private void showVideoImg(String url,ViewHolder viewHolder){
        viewHolder.thumbnail.loadAsResource(com.netease.nim.uikit.R.drawable.nim_image_default, maskBg());

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
            loadThumbnailImage(file.getPath(),false,"???",viewHolder);
        } else {
            downloadVideo(url,file.getPath(),viewHolder);
        }
    }

    private void downloadVideo(String url, String filePath,ViewHolder viewHolder) {
        HttpClient.download(url, filePath, new DownloadInterface() {
            @Override
            public void onSuccess(boolean isSuccess) {
                if (isSuccess) {
                    loadThumbnailImage(filePath,false,"???",viewHolder);
                }
            }

            @Override
            public void onFailure(String message) {
                ToastHelper.showToast(context, "下载视频失败！");
            }

            @Override
            public void onComplete() {
            }
        });
    }

}
