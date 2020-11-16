package com.qingeng.fjjdoctor.session.action;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

import com.qingeng.apilibrary.bean.CollectBean;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.http.DownloadInterface;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.fjjdoctor.user.MyCollectActivity;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class CollectAction extends BaseAction {

    public CollectAction() {
        super(R.drawable.dialogue_collection, R.string.input_panel_collection);
    }

    @Override
    public void onClick() {
        getSelect();

    }

    public void getSelect() {
//        ContactSelectActivity.Option advancedOption = TeamHelper
//                .getCreateContactSelectOption(null, 1);
//        NimUIKit.startContactSelector(getActivity(), advancedOption,
//                makeRequestCode(RequestCode.GET_LOCAL_FILE));
        MyCollectActivity.startActivityForResult(getActivity(), makeRequestCode(RequestCode.PICK_IMAGE));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final CollectBean collectBean = (CollectBean) data.getSerializableExtra(
                MyCollectActivity.RESULT_DATA);
        Double id = Double.parseDouble(collectBean.getTargetUserId());
        BigDecimal bd2 = new BigDecimal(id);
        String account = bd2.toPlainString();

        downloadFileSend(collectBean);
    }

    private void goSendMessage(CollectBean collectBean, File file,String account) {
        IMMessage message = null;
        if (collectBean.getTypeDesc().equals("图片")) {
            message = MessageBuilder.createImageMessage(account, getSessionType(), file, file.getName());
        } else if (collectBean.getTypeDesc().equals("文字")) {
            message = MessageBuilder.createTextMessage(account, getSessionType(), collectBean.getContent());
        } else if (collectBean.getTypeDesc().equals("语音")) {
            message = MessageBuilder.createAudioMessage(account, getSessionType(), file, 0);
        } else if (collectBean.getTypeDesc().equals("视频")) {
            MediaPlayer mediaPlayer = null;
            try {
                mediaPlayer = MediaPlayer.create(getActivity(), Uri.fromFile(file));
            } catch (Exception e) {
                e.printStackTrace();
            }
// 视频文件持续时间
            long duration = mediaPlayer == null ? 0 : mediaPlayer.getDuration();
// 视频高度
            int height = mediaPlayer == null ? 0 : mediaPlayer.getVideoHeight();
// 视频宽度
            int width = mediaPlayer == null ? 0 : mediaPlayer.getVideoWidth();
// 创建视频消息
            message = MessageBuilder.createVideoMessage(account, getSessionType(), file, duration, width, height, null);
        }
        sendMessage(message);
    }

    private void goSendMessage(CollectBean collectBean,File file){
        List<String> accounts = Arrays.asList(getAccount().split(","));
        for (String account : accounts) {
            goSendMessage(collectBean,file,account);
        }
        if (accounts.size()>1){
            ToastHelper.showToast(getActivity(), "发送成功");
            getContainer().activity.finish();
        }
    }

    private void downloadFileSend(CollectBean collectBean) {
        if (collectBean.getTypeDesc().equals("文字")) {
            goSendMessage(collectBean, null);
            return;
        }
        String url = collectBean.getContent();
        String path = "";
        if (url.endsWith("mp4")) {
            path = url.replace("mp4", ".mp4");
        } else if (url.endsWith("aac")) {
            path = url.replace("aac", ".aac");
        } else if (url.endsWith("jpg")) {
            path = url.replace("jpg", ".jpg");
        }


        String fileName = path.substring(path.lastIndexOf("/") + 1);
        File file = new File(MainConstant.DOWNLOAD_DIRECTORY + fileName);
        if (file.exists()) {
            goSendMessage(collectBean, file);
        } else {
            HttpClient.download(url, file.getPath(), new DownloadInterface() {
                @Override
                public void onSuccess(boolean isSuccess) {
                    if (isSuccess) {
                        goSendMessage(collectBean, file);
                    }
                }

                @Override
                public void onFailure(String message) {
                    ToastHelper.showToast(getActivity(), "下载文件失败！");
                }

                @Override
                public void onComplete() {
                }
            });
        }
    }


}

