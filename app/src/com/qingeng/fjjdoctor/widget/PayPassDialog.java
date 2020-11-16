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
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qingeng.fjjdoctor.R;


/**
 * Created by ORIOC-MZG on 2018/7/9.
 */

public class PayPassDialog extends Dialog {

    private Context context;


    private TextView tv_pay_type;
    private TextView tv_money;
    private EditText edit_password;
    private TextView tv_cancel;
    private TextView tv_send;

    OnFinishListener onFinishListener;

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public PayPassDialog(Context context) {
        super(context);
        this.context = context;
    }

    public PayPassDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected PayPassDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }


    public void init(boolean cancekable,String type,String money) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_pay_pass_layout, null, false);
        // View layout = inflater.inflate(R.layout.dialog_upgrade_layout, null);
        tv_pay_type = contentView.findViewById(R.id.tv_pay_type);
        tv_money = contentView.findViewById(R.id.tv_money);
        edit_password = contentView.findViewById(R.id.edit_password);
        tv_cancel = contentView.findViewById(R.id.tv_cancel);
        tv_send = contentView.findViewById(R.id.tv_send);
        tv_pay_type.setText(type);
        tv_money.setText(money);

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
                String pass = edit_password.getText().toString().trim();
                if (TextUtils.isEmpty(pass) && pass.length() !=6){
                    ToastHelper.showToast(context, "请输入6位支付密码");
                    return;
                }
                if (onFinishListener !=null){
                    onFinishListener.onPassComplete(pass);
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
            PayPassDialog.this.dismiss();
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

    public interface OnFinishListener {
        void onPassComplete(String pass);
    }

}


