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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.RegularUtils;
import com.qingeng.fjjdoctor.util.UiUtils;
import com.qingeng.fjjdoctor.widget.TopBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录/注册界面
 * <p/>
 */
public class ResetPasswordActivity extends UI implements View.OnClickListener, HttpInterface {


    private boolean sendCDing = false;

    @BindView(R.id.top_bar)
    TopBar top_bar;

    boolean show_password = false;

    @BindView(R.id.phone_layout)
    LinearLayout phone_layout;
    @BindView(R.id.password_layout)
    LinearLayout password_layout;

    @BindView(R.id.edit_reset_password)
    EditText edit_reset_password;

    @BindView(R.id.iv_reset_show_password)
    ImageView iv_reset_show_password;

    @BindView(R.id.edit_reset_account)
    EditText edit_reset_account;
    @BindView(R.id.edit_reset_code)
    EditText edit_reset_code;
    @BindView(R.id.tv_reset_code_send)
    TextView tv_reset_code_send;


    @BindView(R.id.btn_next)
    Button btn_next;


    public static void start(Context context,String title) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        top_bar.setTitle(getIntent().getStringExtra("title"));
        top_bar.setLeftButtonListener(R.drawable.actionbar_dark_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_password) {
                    show_password = false;
                    showPassword();
                } else {
                    finish();
                }
            }
        });
        tv_reset_code_send.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        iv_reset_show_password.setOnClickListener(this);
        show_password = false;
        showPassword();
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (show_password) {
                    goResetPassword();
                } else {
                    if (!verifyInput(false)) return;
                    show_password = true;
                    showPassword();
                }
                break;
            case R.id.tv_reset_code_send:
                sendCode();
                break;
            case R.id.iv_reset_show_password:
                if (edit_reset_password.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    edit_reset_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    iv_reset_show_password.setBackgroundResource(R.mipmap.password_show);
                } else {
                    edit_reset_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iv_reset_show_password.setBackgroundResource(R.mipmap.password_hide);
                }
                break;
        }
    }

    private void goResetPassword() {
        if (!verifyInput(true)) return;
        String phone = edit_reset_account.getText().toString().trim();
        String code = edit_reset_code.getText().toString().trim();
        String password = edit_reset_password.getText().toString().trim();
        DialogMaker.showProgressDialog(this, "设置中...");
        HttpClient.resetPassword(code, phone, password, this, RequestCommandCode.RESET_PASSWORD);
    }


    public void sendCode() {
        if (sendCDing) return;
        if (!checkPhone()) return;
        String phone = edit_reset_account.getText().toString().trim();

        ToastHelper.showToast(this, "已发送");
        HttpClient.sendCode("forget", phone, this, RequestCommandCode.SEND_CODE);
        sendCDing = true;
        CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_reset_code_send.setText(millisUntilFinished / 1000 + "S重新获取");
            }

            @Override
            public void onFinish() {
                tv_reset_code_send.setText("重新获取");
                sendCDing = false;
            }
        };
        countDownTimer.start();
    }

    private boolean checkPhone() {
        if (!RegularUtils.isMobileNO(edit_reset_account.getText().toString().trim())) {
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
    private boolean verifyInput(boolean needVerifyPass) {
        if (!RegularUtils.isMobileNO(edit_reset_account.getText().toString().trim())) {
            ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_phone));
            return false;
        }

        if (TextUtils.isEmpty(edit_reset_code.getText().toString().trim())) {
            ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_code));
            return false;
        }
        if (needVerifyPass) {
            if (TextUtils.isEmpty(edit_reset_password.getText().toString().trim()) || edit_reset_password.getText().toString().trim().length() < 6) {
                ToastHelper.showToast(this, UiUtils.getString(this, R.string.input_error_password));
                return false;
            }
        }
        return true;
    }


    private void showPassword() {
        if (show_password) {
            top_bar.setTitle("设置新登录密码");
            btn_next.setText("完成");
            password_layout.setVisibility(View.VISIBLE);
            phone_layout.setVisibility(View.GONE);
        } else {
            top_bar.setTitle("手机重置密码");
            btn_next.setText("下一步设置新密码");
            password_layout.setVisibility(View.GONE);
            phone_layout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        ToastHelper.showToast(this, baseResponseData.getMsg());
        switch (requestCode) {
            case RequestCommandCode.RESET_PASSWORD:
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
    public void onBackPressed() {
        //此处写退向后台的处理
        if (show_password) {
            show_password = false;
            showPassword();
        } else {
            finish();
        }
    }
}
