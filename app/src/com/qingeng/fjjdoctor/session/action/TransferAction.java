package com.qingeng.fjjdoctor.session.action;

import android.content.Intent;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.redpacket.SendSingleRedPacketActivity;
import com.qingeng.fjjdoctor.redpacket.TransferActivity;
import com.qingeng.fjjdoctor.session.extension.TransferAttachment;

public class TransferAction extends BaseAction {

    public TransferAction() {
        super(R.drawable.dialogue_transfer, R.string.transfer);
    }

    private static final int CREATE_TRANSFER = 52;

    @Override
    public void onClick() {
        int requestCode = makeRequestCode(CREATE_TRANSFER);
        TransferActivity.startActivityForResult(getActivity(),getContainer().account,requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            sendRpMessage(data);
    }

    private void sendRpMessage(Intent data) {
        TransferAttachment attachment = (TransferAttachment) data.getExtras().getSerializable(SendSingleRedPacketActivity.RESULT_DATA);
        // 红包id，红包信息，红包名称
        String content = getActivity().getString(R.string.transfer);
        // 不存云消息历史记录
        CustomMessageConfig config = new CustomMessageConfig();
        config.enableHistory = false;

        IMMessage message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), content, attachment, config);

        sendMessage(message);
    }
}
