package com.qingeng.fjjdoctor.session.action;

import android.content.Intent;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.redpacket.SendSingleRedPacketActivity;
import com.qingeng.fjjdoctor.session.extension.RedPacketAttachment;

public class RedPacketAction extends BaseAction {

    public RedPacketAction() {
        super(R.drawable.dialogue_red, R.string.red_packet);
    }

    private static final int CREATE_GROUP_RED_PACKET = 51;
    private static final int CREATE_SINGLE_RED_PACKET = 10;

    @Override
    public void onClick() {
        int requestCode;
        if (getContainer().sessionType == SessionTypeEnum.Team) {
            requestCode = makeRequestCode(CREATE_GROUP_RED_PACKET);
        } else if (getContainer().sessionType == SessionTypeEnum.P2P) {
            requestCode = makeRequestCode(CREATE_SINGLE_RED_PACKET);
        } else {
            return;
        }
        //NIMRedPacketClient.startSendRpActivity(getActivity(), getContainer().sessionType, getAccount(), requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            sendRpMessage(data);
    }

    private void sendRpMessage(Intent data) {
        RedPacketAttachment attachment = (RedPacketAttachment) data.getExtras().getSerializable(SendSingleRedPacketActivity.RESULT_DATA);
        attachment.setOpened("0");
        // 红包id，红包信息，红包名称
        String content = getActivity().getString(R.string.rp_push_content);
        // 不存云消息历史记录
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;

        IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), content, attachment, config);

        sendMessage(message);
    }
}
