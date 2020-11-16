package com.qingeng.fjjdoctor.redpacket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
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
import com.qingeng.fjjdoctor.session.extension.TransferAttachment;
import com.qingeng.fjjdoctor.widget.PayPassDialog;



public class TransferActivity extends UI implements HttpInterface,PayPassDialog.OnFinishListener {

    private final static String TARGET_USER_ID = "TARGET_USER_ID";
    public final static String RESULT_DATA = "RESULT_DATA";

    private HeadImageView user_photo;
    private TextView tv_user_name;
    private EditText edit_amount;
    private EditText edit_memo;

    private TextView tv_show_memo;
    private LinearLayout layout_memo;
    private Button btn_send;

    private String targetUserId;
    private TargetUserBean targetUserBean;
    private TargetUserBean currentUserBean;

    private BaseRequestBean  baseRequestBean;

    private UpayResponseBean upayResponseBean;

    public static void startActivityForResult(Context context, String targetUserId, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(TARGET_USER_ID, targetUserId);
        intent.setClass(context, TransferActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "转账";
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
        user_photo = findViewById(R.id.user_photo);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_show_memo = findViewById(R.id.tv_show_memo);

        layout_memo = findViewById(R.id.layout_memo);

        layout_memo.setVisibility(View.GONE);
        tv_show_memo.setVisibility(View.VISIBLE);

        tv_show_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_show_memo.setVisibility(View.GONE);
                layout_memo.setVisibility(View.VISIBLE);
            }
        });
        btn_send = findViewById(R.id.btn_send);
        edit_amount.addTextChangedListener(textWatcher);
        btn_send.setOnClickListener(view -> {
            if (TextUtils.isEmpty(edit_amount.getText().toString().trim())) {
                ToastHelper.showToast(this, "请输入转账金额");
                return;
            }
            send();
        });

        getUerById();

    }

    private void getUerById() {
        HttpClient.userInfoByAccId(targetUserId, this, RequestCommandCode.GET_OTHER_USER_INFO);
        HttpClient.userInfoByAccId(AppPreferences.getAccId(), this, 1000004);
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.CREATE_RED_PACKAGE:
             /*   JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));

                TransferAttachment attachment = new TransferAttachment();

                attachment.setAccid((String) currentUserBean.getTargetUserAccid());
                attachment.setUserId( currentUserBean.getTargetUserId()+"");
                attachment.setUserName(currentUserBean.getTargetUsername());
                attachment.setTransferMmoeny(edit_amount.getText().toString().trim());
                attachment.setToUserId(targetUserBean.getTargetUserAccid());
                attachment.setToUserName(targetUserBean.getTargetUsername());
                attachment.setTransferMmoeny(edit_amount.getText().toString().trim());
                attachment.setOrderNum((String) jsonObject.get("orderNo"));
                attachment.setCreateDate((String) jsonObject.get("createDate"));

                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA, attachment);
                setResult(Activity.RESULT_OK, intent);
                this.finish();*/
                break;
            case RequestCommandCode.GET_OTHER_USER_INFO:
                targetUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);

                user_photo.loadAvatar(targetUserBean.getHeadImage());
                tv_user_name.setText(targetUserBean.getTargetUsername());

                break;
            case 1000004:
                currentUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                break;
            case RequestCommandCode.UPAY_TRANSFORM_CREATE:
                upayResponseBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                goUPayRecharge();
                break;
            case RequestCommandCode.UPAY_TRANSFORM_CONFIRM:

                TransferAttachment attachment = new TransferAttachment();
                attachment.setAccid((String) currentUserBean.getTargetUserAccid());
                attachment.setUserId( currentUserBean.getTargetUserId()+"");
                attachment.setUserName(currentUserBean.getTargetUsername());
                attachment.setTransferMmoeny(edit_amount.getText().toString().trim());
                attachment.setToUserId(targetUserBean.getTargetUserAccid());
                attachment.setToUserName(targetUserBean.getTargetUsername());
                attachment.setTransferMmoeny(edit_amount.getText().toString().trim());
                attachment.setOrderNum(upayResponseBean.getObject().getSerialNumber());
                attachment.setRequestId(upayResponseBean.getObject().getRequestId());

                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA, attachment);
                setResult(Activity.RESULT_OK, intent);
                this.finish();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, "失败 " + message);
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
        /*PayPassDialog payPassDialog = new PayPassDialog(this);
        payPassDialog.init(true,"转账",edit_amount.getText().toString().trim());
        payPassDialog.setOnFinishListener(this);
        payPassDialog.show();*/
        //首信 转账预下单
        String moneyStr = edit_amount.getText().toString().trim();
        if (TextUtils.isEmpty(moneyStr)) {
            ToastHelper.showToast(this, "请输入充值金额");
            return;
        }
        DialogMaker.showProgressDialog(this, "获取订单中...");
        Double moneyDouble = Double.parseDouble(moneyStr) * 100;
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("amount",moneyDouble.intValue()+"");//总金额，单位分
        baseRequestBean.addParams("targetWalletId",targetUserBean.getTargetWalletId());
        HttpClient.transformCreate(baseRequestBean, this, RequestCommandCode.UPAY_TRANSFORM_CREATE);
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
                    DialogMaker.showProgressDialog(this, "转账中...");
                    baseRequestBean = new BaseRequestBean();
                    baseRequestBean.addParams("userId", targetUserBean.getTargetUserId() + "");
                    //        baseRequestBean.addParams("msg",edit_memo.getText().toString().trim());
                    baseRequestBean.addParams("money", edit_amount.getText().toString().trim());
                    HttpClient.tran2Wallet(baseRequestBean, this, RequestCommandCode.CREATE_RED_PACKAGE);
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

    private void transformConfirm(){
        DialogMaker.showProgressDialog(this, "处理中...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("serialNumber",upayResponseBean.getObject().getSerialNumber());
        baseRequestBean.addParams("targetWalletId",targetUserBean.getTargetWalletId());
        HttpClient.transformConfirm(baseRequestBean, this, RequestCommandCode.UPAY_TRANSFORM_CONFIRM);
    }
}
