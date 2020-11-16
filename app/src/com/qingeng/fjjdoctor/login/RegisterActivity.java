package com.qingeng.fjjdoctor.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.RegularUtils;
import com.qingeng.fjjdoctor.util.UiUtils;
import com.qingeng.fjjdoctor.widget.TopBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends UI implements HttpInterface, View.OnClickListener {


    private boolean sendCDing = false;

    @BindView(R.id.top_bar)
    TopBar top_bar;

    @BindView(R.id.edit_register_phone)
    EditText edit_register_phone;
    @BindView(R.id.edit_register_code)
    EditText edit_register_code;
    @BindView(R.id.edit_register_password)
    EditText edit_register_password;
    @BindView(R.id.edit_register_password_re)
    EditText edit_register_password_re;

    @BindView(R.id.tv_register_send_code)
    TextView tv_register_send_code;
    @BindView(R.id.btn_register)
    Button btn_register;
    @BindView(R.id.iv_register_show_password)
    ImageView iv_register_show_password;
    @BindView(R.id.iv_register_show_password_re)
    ImageView iv_register_show_password_re;

    @BindView(R.id.layout_agreement)
    LinearLayout layout_agreement;
    @BindView(R.id.cb_agreement)
    CheckBox cb_agreement;

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        top_bar.setTitle("注册");
        top_bar.setLeftButtonListener(R.drawable.actionbar_dark_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_register_send_code.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        iv_register_show_password.setOnClickListener(this);
        iv_register_show_password_re.setOnClickListener(this);
        layout_agreement.setOnClickListener(this);

        cb_agreement.setChecked(true);
    }




    public void sendCode() {
        if (sendCDing) return;
        if (!checkPhone()) return;
        String phone = edit_register_phone.getText().toString().trim();

        ToastHelper.showToast(this, "已发送");
        HttpClient.sendCode("register", phone, this, RequestCommandCode.SEND_CODE);

        sendCDing = true;
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_register_send_code.setText(millisUntilFinished / 1000 + "S重新获取");
            }

            @Override
            public void onFinish() {
                tv_register_send_code.setText("重新获取");
                sendCDing = false;
            }
        };
        countDownTimer.start();
    }

    public void register() {
        if (!verifyInput()) return;
        DialogMaker.showProgressDialog(this, "注册中");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("code", edit_register_code.getText().toString().trim());
        baseRequestBean.addParams("phone", edit_register_phone.getText().toString().trim());
        baseRequestBean.addParams("password", edit_register_password.getText().toString().trim());
        HttpClient.register(baseRequestBean, this, RequestCommandCode.REGISTER);
    }


    private boolean checkPhone() {
        if (!RegularUtils.isMobileNO(edit_register_phone.getText().toString().trim())) {
            ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_phone));
            return false;
        }
        return true;
    }

    /**
     * 验证输入信息
     *
     * @return
     */
    private boolean verifyInput() {
        if (!RegularUtils.isMobileNO(edit_register_phone.getText().toString().trim())) {
            ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_phone));
            return false;
        }

        if (TextUtils.isEmpty(edit_register_code.getText().toString().trim())) {
            ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_code));
            return false;
        }

        if (TextUtils.isEmpty(edit_register_password.getText().toString().trim()) || edit_register_password.getText().toString().trim().length() < 6) {
            ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_password));
            return false;
        }

        if (!edit_register_password.getText().toString().trim().equals(edit_register_password_re.getText().toString().trim())) {
            ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_repeat_password));
            return false;
        }

        if (!cb_agreement.isChecked()) {
            ToastHelper.showToast(this, "请同意用户协议");
            return false;
        }
        return true;
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.SEND_CODE:
                ToastHelper.showToast(this, "发送成功");
                break;
            case RequestCommandCode.REGISTER:
                ToastHelper.showToast(this, "注册成功，跳转登录");
                finish();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register_send_code:
                sendCode();
                break;
            case R.id.btn_register:
                register();
                break;
            case R.id.iv_register_show_password:
                if (edit_register_password.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    edit_register_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_register_show_password.setBackgroundResource(R.mipmap.password_show);
                } else {
                    edit_register_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_register_show_password.setBackgroundResource(R.mipmap.password_hide);
                }
                break;
            case R.id.iv_register_show_password_re:
                if (edit_register_password_re.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    edit_register_password_re.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_register_show_password_re.setBackgroundResource(R.mipmap.password_show);
                } else {
                    edit_register_password_re.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_register_show_password_re.setBackgroundResource(R.mipmap.password_hide);
                }
                break;
            case R.id.layout_agreement:
                HtmlUrlActivity.start(this);

                break;
        }
    }
}
