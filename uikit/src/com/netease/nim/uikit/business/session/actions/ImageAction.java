package com.netease.nim.uikit.business.session.actions;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class ImageAction extends PickImageAction {

    public ImageAction() {
        super(R.drawable.dialogue_picture, R.string.input_panel_photo, true);
    }

    @Override
    protected void onPicked(File file) {
        IMMessage message;
        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
            message = ChatRoomMessageBuilder.createChatRoomImageMessage(getAccount(), file, file.getName());
            sendMessage(message);
        } else {
            List<String> accounts = Arrays.asList(getAccount().split(","));
            for (String account : accounts) {
                message = MessageBuilder.createImageMessage(account, getSessionType(), file, file.getName());
                sendMessage(message);
            }
            if (accounts.size()>1){
                ToastHelper.showToast(getActivity(), "发送成功");
                getContainer().activity.finish();
            }
        }

    }
}

