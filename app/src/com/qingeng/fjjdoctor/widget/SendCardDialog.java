package com.qingeng.fjjdoctor.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qingeng.apilibrary.bean.TargetUserBean;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.session.extension.CardAttachment;
import com.qingeng.fjjdoctor.session.extension.CustomAttachmentType;


/**
 * Created by ORIOC-MZG on 2018/7/9.
 */

public class SendCardDialog extends Dialog {

    private Context context;


    private HeadImageView img_head;
    private TextView tv_name;
    private TextView tv_target_name;
    private EditText edit_message;
    private TextView tv_cancel;
    private TextView tv_send;
    private TargetUserBean cardUser;
    private TargetUserBean sendToUser;

    public SendCardDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SendCardDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected SendCardDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }


    public void init(boolean cancekable, TargetUserBean cardUser, TargetUserBean sendToUser) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_send_card_layout, null, false);
        // View layout = inflater.inflate(R.layout.dialog_upgrade_layout, null);
        this.cardUser = cardUser;
        this.sendToUser = sendToUser;
        img_head = contentView.findViewById(R.id.img_head);
        tv_name = contentView.findViewById(R.id.tv_name);
        tv_target_name = contentView.findViewById(R.id.tv_target_name);
        edit_message = contentView.findViewById(R.id.edit_message);
        tv_cancel = contentView.findViewById(R.id.tv_cancel);
        tv_send = contentView.findViewById(R.id.tv_send);

        img_head.loadAvatar(sendToUser.getHeadImage());
        tv_name.setText(sendToUser.getTargetUsername());
        tv_target_name.setText("[推荐好友]" + cardUser.getTargetUsername());

        addContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setCanceledOnTouchOutside(cancekable);
        setCancelable(cancekable);

        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        tv_cancel.setOnClickListener(this::onClick);
        tv_send.setOnClickListener(this::onClick);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                this.dismiss();
                break;
            case R.id.tv_send:
                CardAttachment attachment = new CardAttachment(CustomAttachmentType.card, cardUser.getTargetUsername(), cardUser.getHeadImage(),
                        cardUser.getTargetUserAccid(), cardUser.getTargetUserId() + "");
               // IMMessage message = ChatRoomMessageBuilder.createChatRoomCustomMessage(sendToUser.getTargetUserAccid(), attachment);
                IMMessage  message = MessageBuilder.createCustomMessage(sendToUser.getTargetUserAccid(), SessionTypeEnum.P2P, attachment);
                sendMessage(message);

                String msg = edit_message.getText().toString().trim();
                if (!TextUtils.isEmpty(msg)){
                    IMMessage message1 = MessageBuilder.createTextMessage(sendToUser.getTargetUserAccid(), SessionTypeEnum.P2P,msg);
                    sendMessage(message1);
                }
                this.dismiss();
                break;
        }
    }


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完orderNum全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            SendCardDialog.this.dismiss();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        setCanceledOnTouchOutside(true);
    }

    public void sendMessage(IMMessage message){
        NIMClient.getService(MsgService.class).sendMessage(message, false).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                ToastHelper.showToast(context, "发送成功");

            }

            @Override
            public void onFailed(int code) {
                ToastHelper.showToast(context, "失败：service code:" + code);
            }

            @Override
            public void onException(Throwable exception) {
                ToastHelper.showToast(context, "失败：exception:" + exception.getMessage());
            }
        });
    }

}


