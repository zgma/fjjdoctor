package com.qingeng.fjjdoctor.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.fjjdoctor.R;


/**
 * Created by ORIOC-MZG on 2018/7/9.
 */

public class RedPacketDialog extends Dialog {

    private Context context;

    private RedPacketDialogListener redPacketDialogListener;


    private HeadImageView user_photo;
    private TextView tv_name;
    private TextView tv_message;
    private TextView tv_open;

    private ImageView iv_close;
    private Button btn_open;
    private LinearLayout layout_bottom;


    private String orderNum;
    private String requestId;

    public RedPacketDialog(Context context) {
        super(context);
        this.context = context;
    }

    public RedPacketDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected RedPacketDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    public void setRedPacketDialogListener(RedPacketDialogListener redPacketDialogListener) {
        this.redPacketDialogListener = redPacketDialogListener;
    }

    public void init(boolean cancekable, String desc, String name, String icon, String orderNum, String requestId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_red_packet_layout, null, false);
        // View layout = inflater.inflate(R.layout.dialog_upgrade_layout, null);
        user_photo = contentView.findViewById(R.id.user_photo);
        tv_name = contentView.findViewById(R.id.tv_name);
        tv_message = contentView.findViewById(R.id.tv_message);
        iv_close = contentView.findViewById(R.id.iv_close);
        btn_open = contentView.findViewById(R.id.btn_open);
        tv_open = contentView.findViewById(R.id.tv_open);
        layout_bottom = contentView.findViewById(R.id.layout_bottom);

        iv_close.setOnClickListener(this::onClick);
        btn_open.setOnClickListener(this::onClick);
        tv_open.setOnClickListener(this::onClick);

        addContentView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setCanceledOnTouchOutside(cancekable);
        setCancelable(cancekable);

        layout_bottom.setVisibility(View.INVISIBLE);
        tv_message.setMovementMethod(ScrollingMovementMethod.getInstance());

        this.orderNum = orderNum;
        this.requestId = requestId;
        setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return true;
            } else {
                return false;
            }
        });
        user_photo.loadAvatar(icon);
        tv_name.setText(name+"的红包");
        tv_message.setText(desc);
        layout_bottom.setVisibility(View.INVISIBLE);
        btn_open.setVisibility(View.INVISIBLE);

        //判断是否领完
     /*   BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("requestId", requestId);
        baseRequestBean.addParams("queryType", "SIMPLE");
        HttpClient.redPackageQuery(baseRequestBean, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                RedPacketResponseBean redPacketResponseBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), RedPacketResponseBean.class);
                if (redPacketResponseBean.getObject().getTotal() == redPacketResponseBean.getObject().getUsed()){
                    layout_bottom.setVisibility(View.VISIBLE);
                    btn_open.setVisibility(View.INVISIBLE);
                }else {
                    layout_bottom.setVisibility(View.INVISIBLE);
                    btn_open.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(int requestCode, String message) {

            }

            @Override
            public void onComplete() {

            }
        }, 1);*/

        layout_bottom.setVisibility(View.INVISIBLE);
        btn_open.setVisibility(View.VISIBLE);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                if (redPacketDialogListener != null) {
                    redPacketDialogListener.onOpen(orderNum,requestId);
                }
                break;
            case R.id.iv_close:
                this.dismiss();
                break;
            case R.id.tv_open:
                if (redPacketDialogListener != null) {
                    redPacketDialogListener.onShowDetail(orderNum,requestId);
                }
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
            RedPacketDialog.this.dismiss();
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


    public interface RedPacketDialogListener {
        void onOpen(String orderNum,String requestId);

        void onShowDetail(String orderNum,String requestId);
    }

}


