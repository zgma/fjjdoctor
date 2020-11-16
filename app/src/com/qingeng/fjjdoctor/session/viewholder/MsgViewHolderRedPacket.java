package com.qingeng.fjjdoctor.session.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.EventBusMessage;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.redpacket.RedPacketDetailActivity;
import com.qingeng.fjjdoctor.session.extension.RedPacketAttachment;
import com.qingeng.fjjdoctor.util.UiUtils;
import com.qingeng.fjjdoctor.widget.RedPacketDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

public class MsgViewHolderRedPacket extends MsgViewHolderBase {

    private RelativeLayout sendView, revView;
    private TextView sendContentText, revContentText;
    private TextView sendTitleText, revTitleText;
    private TextView sendTargetText, revTargetText;
    private ImageView sendIcon, revIcon;

    public MsgViewHolderRedPacket(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return R.layout.red_packet_item;
    }

    @Override
    public void inflateContentView() {
        sendContentText = findViewById(R.id.tv_bri_mess_send);
        sendTitleText = findViewById(R.id.tv_bri_name_send);
        sendTargetText = findViewById(R.id.tv_bri_target_send);
        sendView = findViewById(R.id.bri_send);
        sendIcon = findViewById(R.id.iv_icon_send);

        revContentText = findViewById(R.id.tv_bri_mess_rev);
        revTitleText = findViewById(R.id.tv_bri_name_rev);
        revTargetText = findViewById(R.id.tv_bri_target_rev);
        revView = findViewById(R.id.bri_rev);
        revIcon = findViewById(R.id.iv_icon_rev);
    }

    @Override
    public void bindContentView() {
        RedPacketAttachment attachment = (RedPacketAttachment) message.getAttachment();

        if (!isReceivedMessage()) {// 消息方向，自己发送的
            sendView.setVisibility(View.VISIBLE);
            revView.setVisibility(View.GONE);
            sendContentText.setText(attachment.getMessage());
            sendView.setBackground(UiUtils.getDrawable(context, R.drawable.red_packet_send_bg));
            sendIcon.setImageResource(R.drawable.red_packet_icon_normal);
//            sendTargetText.setText("领取红包");
            sendTargetText.setText("¥ "+attachment.getRedMmoeny());
            if (message.getStatus() == MsgStatusEnum.read) {
                sendView.setBackground(UiUtils.getDrawable(context, R.drawable.red_packet_send_bg_opened));
//                sendTargetText.setText("已领取");
                sendTargetText.setText("¥ "+attachment.getRedMmoeny());
                sendIcon.setImageResource(R.drawable.red_packet_icon_open);
            }

        } else {
            sendView.setVisibility(View.GONE);
            revView.setVisibility(View.VISIBLE);
            revContentText.setText(attachment.getMessage());
            revView.setBackground(UiUtils.getDrawable(context, R.drawable.red_packet_rev_bg));
//            revTargetText.setText("领取红包");
            revTargetText.setText("¥ "+attachment.getRedMmoeny());
            revIcon.setImageResource(R.drawable.red_packet_icon_normal);
            if (message.getStatus() == MsgStatusEnum.read) {
                revView.setBackground(UiUtils.getDrawable(context, R.drawable.red_packet_rev_bg_opened));
//                revTargetText.setText("已领取");
                revTargetText.setText("¥ "+attachment.getRedMmoeny());
                revIcon.setImageResource(R.drawable.red_packet_icon_open);
            }
        }

        sendTitleText.setText("红包");
        revTitleText.setText("红包");
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
        RedPacketAttachment attachment = (RedPacketAttachment) message.getAttachment();
        if (Objects.equals(message.getStatus(),MsgStatusEnum.read)){
            RedPacketDetailActivity.start(context, attachment.getOrderNum(), attachment.getRequestId());
        }else {
            RedPacketDialog upgradeDialog = new RedPacketDialog((Activity) context, R.style.tipDialog2);
            upgradeDialog.init(false, attachment.getMessage(), attachment.getUserName(), attachment.getUserIcon(), attachment.getOrderNum(), attachment.getRequestId());
            upgradeDialog.show();
            upgradeDialog.setRedPacketDialogListener(new RedPacketDialog.RedPacketDialogListener() {
                @Override
                public void onOpen(String orderNum,String requestId) {
                    BaseRequestBean baseRequestBean = new BaseRequestBean();
                    baseRequestBean.addParams("serialNumber", attachment.getOrderNum());
                    baseRequestBean.addParams("requestId", attachment.getRequestId());
                    HttpClient.grabPacket(baseRequestBean, new HttpInterface() {
                        @Override
                        public void onSuccess(int requestCode, BaseResponseData baseResponseData) {

                            message.setStatus(MsgStatusEnum.read);
                            NIMClient.getService(MsgService.class).updateIMMessageStatus(message);
                            EventBus.getDefault().post(new EventBusMessage(EventBusMessage.MESSAGE_REFRESH_LIST));

                            RedPacketDetailActivity.start(context, orderNum,requestId);
                            upgradeDialog.dismiss();
                        }

                        @Override
                        public void onFailure(int requestCode, String message) {
                            ToastHelper.showToast(context, message);
                        }

                        @Override
                        public void onComplete() {

                        }
                    }, 1);
                }

                @Override
                public void onShowDetail(String orderNum,String requestId) {
                    RedPacketDetailActivity.start(context, orderNum,requestId);
                    upgradeDialog.dismiss();
                }
            });
        }
    }
}
