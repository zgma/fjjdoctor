package com.qingeng.fjjdoctor.redpacket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.TargetUserBean;
import com.qingeng.apilibrary.bean.UpayResponseBean;
import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.session.extension.RedPacketAttachment;
import com.qingeng.fjjdoctor.util.MoneyUtils;
import com.qingeng.fjjdoctor.widget.PayPassDialog;



public class SendSingleRedPacketActivity extends UI implements HttpInterface,PayPassDialog.OnFinishListener {

    private final static String TARGET_USER_ID = "TARGET_USER_ID";
    public final static String RESULT_DATA = "RESULT_DATA";

    private EditText edit_amount;
    private EditText edit_memo;
    private TextView tv_show_amount;
    private Button btn_send;

    private String targetUserId;
    private TargetUserBean targetUserBean;
    private TargetUserBean currentUserBean;

    private BaseRequestBean  baseRequestBean;
    private String moneyStr = "";
    private String memo = "";
    private UpayResponseBean upayResponseBean;
    private int queryStatusCount = 0;

    public static void startActivityForResult(Context context, String targetUserId, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(TARGET_USER_ID, targetUserId);
        intent.setClass(context, SendSingleRedPacketActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_single_red_packet);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "发红包";
        options.titleColor = R.color.white;
        options.navigateId = R.drawable.actionbar_white_back_icon;
        options.backgrounpColor = R.color.topbar_backgroup_red;
        setToolBar(R.id.toolbar, options);

        targetUserId = getIntent().getStringExtra(TARGET_USER_ID);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initUI() {
        edit_amount = findViewById(R.id.edit_amount);
        edit_memo = findViewById(R.id.edit_memo);
        tv_show_amount = findViewById(R.id.tv_show_amount);
        btn_send = findViewById(R.id.btn_send);
        edit_amount.addTextChangedListener(textWatcher);
        btn_send.setOnClickListener(view -> {
            moneyStr = edit_amount.getText().toString().trim();
            memo = edit_memo.getText().toString().trim();
            if (TextUtils.isEmpty(moneyStr) || TextUtils.isEmpty(memo)) {
                ToastHelper.showToast(this, "请输入完整内容");
                return;
            }

            send();
        });

        getUerById();
        getTargetUserById();

    }

    private void getUerById() {
        HttpClient.userInfoByAccId(AppPreferences.getAccId(), this, RequestCommandCode.LOGIN_USER_INFO);
    }

    private void getTargetUserById() {
        HttpClient.userInfoByAccId(targetUserId, this, RequestCommandCode.GET_OTHER_USER_INFO);
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.CREATE_RED_PACKAGE:
                RedPacketAttachment attachment = new RedPacketAttachment();
                // 红包id，红包信息，红包名称
                attachment.setRedMmoeny((String) baseRequestBean.getBody().get("total"));
                attachment.setRedNum((String) baseRequestBean.getBody().get("num"));
                attachment.setAccid(targetUserBean.getTargetUserAccid());
                attachment.setUserName(targetUserBean.getTargetUsername());
                attachment.setUserIcon(targetUserBean.getHeadImage());
                attachment.setUserId(targetUserBean.getTargetUserId()+"");
                attachment.setMessage((String) baseRequestBean.getBody().get("msg"));
                attachment.setOrderNum((String)baseResponseData.getData());

                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA, attachment);
                setResult(Activity.RESULT_OK, intent);
                this.finish();
                break;
            case RequestCommandCode.GET_OTHER_USER_INFO:
                targetUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                break;
            case RequestCommandCode.LOGIN_USER_INFO:
                currentUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                break;
            case RequestCommandCode.UPAY_TRANSFORM_CREATE:
                upayResponseBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                goUPayRecharge();
                break;
            case RequestCommandCode.UPAY_RED_PACKET_STATUS:
     /*           upayResponseBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                goUPayRecharge();
                sendRedPacket();*/
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                if (jsonObject.containsKey("orderStatus") && jsonObject.getString("orderStatus").equals("SUCCESS")){
                    sendRedPacket();
                }else {
                    packetStatus();
                }
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this,  message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tv_show_amount.setText(edit_amount.getText().toString().trim());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String editStr = editable.toString().trim();
            int posDot = editStr.indexOf(".");
            //不允许输入3位小数,超过三位就删掉
            if (posDot < 0) {
                return;
            }
            if (editStr.length() - posDot - 1 > 2) {
                editable.delete(posDot + 3, posDot + 4);
            }
        }
    };


    private void send(){
    /*    PayPassDialog payPassDialog = new PayPassDialog(this);
        payPassDialog.init(true,"发红包",edit_amount.getText().toString().trim());
        payPassDialog.setOnFinishListener(this);
        payPassDialog.show();*/
        DialogMaker.showProgressDialog(this, "获取订单中...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("packetType","ONE_TO_ONE");
        baseRequestBean.addParams("singleAmount", MoneyUtils.yuanString2FenInt(moneyStr));
        baseRequestBean.addParams("packetCount",1);
        baseRequestBean.addParams("msg",memo);
//        baseRequestBean.addParams("targetUserId",targetUserBean.getTargetUserId());
        HttpClient.redPackageCreate(baseRequestBean, this, RequestCommandCode.UPAY_TRANSFORM_CREATE);
    }

    @Override
    public void onPassComplete(String pass) {
       /* UserBean userBean = Preferences.getUserBean();
        if (userBean !=null){
            if (TextUtils.isEmpty(userBean.getPayPwd())){
                ToastHelper.showToast(this, "未设置支付密码，请先设置");
                //ForgetPasswordActivity.start(this,1);
            }else {
                if (pass.equals(userBean.getPayPwd())){
                    DialogMaker.showProgressDialog(this, "发送中...");
                    baseRequestBean = new BaseRequestBean();
                    baseRequestBean.addParams("targetUserId",targetUserId);
                    baseRequestBean.addParams("num","1");
                    baseRequestBean.addParams("packageType","1");
                    baseRequestBean.addParams("msg",edit_memo.getText().toString().trim());
                    baseRequestBean.addParams("total",edit_amount.getText().toString().trim());
                    HttpClient.createRedPackage(baseRequestBean, this, RequestCommandCode.CREATE_RED_PACKAGE);
                }else {
                    ToastHelper.showToast(this, "密码错误");

                }
            }
        }else {
            ToastHelper.showToast(this, "用户信息获取失败");
            finish();
        }*/
    }

    void goUPayRecharge(){
    }

    private void sendRedPacket(){
        RedPacketAttachment attachment = new RedPacketAttachment();
        // 红包id，红包信息，红包名称
        attachment.setRedNum(1+"");
        attachment.setRedMmoeny(moneyStr);
        attachment.setAccid(currentUserBean.getTargetUserAccid());
        attachment.setUserName(currentUserBean.getTargetUsername());
        attachment.setUserIcon(currentUserBean.getHeadImage());
        attachment.setUserId(currentUserBean.getTargetUserId()+"");
        attachment.setMessage(memo);
        attachment.setOrderNum(upayResponseBean.getObject().getSerialNumber());
        attachment.setRequestId(upayResponseBean.getObject().getRequestId());

        Intent intent = new Intent();
        intent.putExtra(RESULT_DATA, attachment);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }


    private void packetStatus() {
        queryStatusCount++;
        if (queryStatusCount > 5){
            ToastHelper.showToast(SendSingleRedPacketActivity.this, "查询红包状态重试5次失败，请重新发送");
            return;
        }
        DialogMaker.showProgressDialog(this, "获取红包状态...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("requestId", upayResponseBean.getObject().getRequestId());
        HttpClient.packetStatus(baseRequestBean, SendSingleRedPacketActivity.this, RequestCommandCode.UPAY_RED_PACKET_STATUS);
    }
}
