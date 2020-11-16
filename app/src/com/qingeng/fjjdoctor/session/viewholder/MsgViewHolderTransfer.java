package com.qingeng.fjjdoctor.session.viewholder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.redpacket.TransferDetailActivity;
import com.qingeng.fjjdoctor.session.extension.TransferAttachment;

public class MsgViewHolderTransfer extends MsgViewHolderBase {

    private RelativeLayout sendView, revView;
    private TextView sendContentText, revContentText;
    private TextView sendTitleText, revTitleText;
    private TextView sendTargetText, revTargetText;

    public MsgViewHolderTransfer(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return R.layout.item_transfer;
    }

    @Override
    public void inflateContentView() {
        sendContentText = findViewById(R.id.tv_bri_mess_send);
        sendTitleText = findViewById(R.id.tv_bri_name_send);
        sendTargetText = findViewById(R.id.tv_bri_target_send);
        sendView = findViewById(R.id.bri_send);

        revContentText = findViewById(R.id.tv_bri_mess_rev);
        revTitleText = findViewById(R.id.tv_bri_name_rev);
        revTargetText = findViewById(R.id.tv_bri_target_rev);
        revView = findViewById(R.id.bri_rev);
    }

    @Override
    public void bindContentView() {
        TransferAttachment attachment = (TransferAttachment) message.getAttachment();
        System.out.println("bindContentView==="+JSON.toJSONString(message));
        if (!isReceivedMessage()) {// 消息方向，自己发送的
            sendView.setVisibility(View.VISIBLE);
            revView.setVisibility(View.GONE);
            sendContentText.setText(attachment.getTransferMmoeny()+"元");
            sendTargetText.setText("转账给"+attachment.getToUserName());
        } else {
            sendView.setVisibility(View.GONE);
            revView.setVisibility(View.VISIBLE);
            revContentText.setText(attachment.getTransferMmoeny()+"元");
            revTargetText.setText("转账给你");
        }

        sendTitleText.setText("转账");
        revTitleText.setText("转账");
    }

    @Override
    protected int leftBackground() {
        return R.color.transparent;
    }

    @Override
    protected int rightBackground() {
        return R.color.transparent;
    }

    @Override
    public void onItemClick() {
        // 拆红包
        TransferAttachment attachment = (TransferAttachment) message.getAttachment();
        System.out.println("拆转账---" + JSON.toJSONString(message));
        
        TransferDetailActivity.start(context, attachment);

    }
}
