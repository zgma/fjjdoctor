package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
import com.qingeng.fjjdoctor.util.RegularUtils;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.widget.ClearableEditTextWithIcon;

import butterknife.ButterKnife;


/**
 * 登录/注册界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class ChangePhoneActivity extends UI implements OnKeyListener, View.OnClickListener, HttpInterface {

    private static final int PICK_AVATAR_REQUEST = 0x0E;


    private ClearableEditTextWithIcon editPhone;

    private ClearableEditTextWithIcon editCode;

    private Button btn_ok;

    private TextView tv_get_code;

    public boolean sendCDing = false;

    public static void start(Context context) {
        Intent intent = new Intent(context, ChangePhoneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    @Override
    protected boolean displayHomeAsUpEnabled() {
        return false;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "修改手机号";
        setToolBar(R.id.toolbar, options);
        setupLoginPanel();
    }


    /**
     * 登录面板
     */
    private void setupLoginPanel() {
        //editPhone = findView(R.id.edit_phone);
        //editCode = findView(R.id.edit_code);
        tv_get_code = findView(R.id.tv_get_code);

        btn_ok = findView(R.id.btn_ok);

        tv_get_code.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

    }


    private boolean checkRegisterContentValid() {
        if (!checkPhone()) {
            return false;
        }

        // 验证码检查
        String code = editCode.getText().toString().trim();
        if (code.length() <= 0 || code.length() > 4) {
            ToastHelper.showToast(ChangePhoneActivity.this, R.string.register_code_tip);
            return false;
        }
        return true;
    }


    private boolean checkPhone() {
        // 帐号检查
        String phone = editPhone.getText().toString().trim();

        if (LocalDataUtils.getUserInfo().getPhonenumber().equals(phone)){
            ToastHelper.showToast(ChangePhoneActivity.this, "不能修改当前使用的手机号码！");
            return false;
        }

        if (!RegularUtils.isMobileNO(phone)) {
            ToastHelper.showToast(ChangePhoneActivity.this, "请输入正确手机号码！");
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                checkRegisterContentValid();
                BaseRequestBean baseRequestBean = new BaseRequestBean();
                baseRequestBean.addParams("phone", editPhone.getText().toString().trim());
                baseRequestBean.addParams("code", editCode.getText().toString());
                DialogMaker.showProgressDialog(this, "提交中...");
                HttpClient.changePhoneNumber(baseRequestBean, this,RequestCommandCode.CHANGE_PHONE_NUMBER);
                break;
            case R.id.tv_get_code:
                sendCode();
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.SEND_CODE:
                JSONObject code = (JSONObject) JSON.toJSON(baseResponseData.getData());
                ToastHelper.showToast(this, "发送成功");
                break;
            case RequestCommandCode.CHANGE_PHONE_NUMBER:
                ToastHelper.showToast(this, "设置成功，返回登陆");
                finish();
                break;
        }
        ToastHelper.showToast(ChangePhoneActivity.this, baseResponseData.getMsg());
    }


    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(ChangePhoneActivity.this, message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }

    public void sendCode() {
        if (sendCDing) return;
        if (!checkPhone()) return;
        String phone = editPhone.getText().toString().trim();

        DialogMaker.showProgressDialog(this, getString(R.string.sending), false);
       // HttpClient.sendCode(phone, this, RequestCommandCode.SEND_CODE);

        sendCDing = true;
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_get_code.setText(millisUntilFinished / 1000 + "S重新获取");
            }

            @Override
            public void onFinish() {
                tv_get_code.setText("重新获取");
                sendCDing = false;
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
