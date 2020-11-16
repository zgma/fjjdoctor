package com.qingeng.fjjdoctor.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.bean.UserInfoBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
import com.qingeng.fjjdoctor.util.RegularUtils;
import com.qingeng.fjjdoctor.widget.TopBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录/注册界面
 * <p/>
 */
public class ChangePhoneActivity extends UI implements View.OnClickListener, HttpInterface {


    private boolean sendCDing_old = false;
    private boolean sendCDing_new = false;

    @BindView(R.id.top_bar)
    TopBar top_bar;

    boolean show_new = false;

    @BindView(R.id.layout_1)
    LinearLayout layout_1;
    @BindView(R.id.layout_2)
    LinearLayout layout_2;

    @BindView(R.id.edit_old_code)
    EditText edit_old_code;


    @BindView(R.id.edit_new_phone)
    EditText edit_new_phone;
    @BindView(R.id.edit_new_code)
    EditText edit_new_code;

    @BindView(R.id.tv_code_send_old)
    TextView tv_code_send_old;
    @BindView(R.id.tv_send_code_new)
    TextView tv_send_code_new;

    @BindView(R.id.btn_next)
    Button btn_next;

    UserInfoBean userInfoBean;
    private String auth;
    private String code_old;
    private String code;
    private String phone;
    private CountDownTimer countDownTimer_old;
    private CountDownTimer countDownTimer_new;


    public static void start(Context context) {
        Intent intent = new Intent(context, ChangePhoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);
        top_bar.setTitle("更换手机号");
        top_bar.setLeftButtonListener(R.drawable.actionbar_dark_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (show_new) {
                    shownew(false);
                } else {
                    finish();
                }
            }
        });
        tv_code_send_old.setOnClickListener(this);
        tv_send_code_new.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        getUserData();
        shownew(false);
    }

    private void getUserData() {
        UserInfoBean userBean = LocalDataUtils.getUserInfo();
        if (userBean != null) {
            userInfoBean = userBean;
        } else {
            ToastHelper.showToast(this, "本地用户信息失效");
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (show_new) {
                    if (!verifyInput(3)) return;
                    goChangePhone();
                } else {
                    goVerifyOldCode();
                }
                break;
            case R.id.tv_code_send_old:
                sendCode();
                break;
            case R.id.tv_send_code_new:
                if (!verifyInput(2)) return;
                sendCode();
                break;

        }
    }

    private void goChangePhone() {
        code = edit_new_code.getText().toString().trim();
        DialogMaker.showProgressDialog(this, "修改中...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("auth", auth);
        baseRequestBean.addParams("code", code);
        baseRequestBean.addParams("phone", phone);
        HttpClient.updatePhone(baseRequestBean, this, RequestCommandCode.UPDATE_PHONE);
    }

    private void goVerifyOldCode() {
        if (!verifyInput(1)) return;
        code_old = edit_old_code.getText().toString().trim();
        DialogMaker.showProgressDialog(this, "验证中...");
        HttpClient.verifyCode(code_old, this, RequestCommandCode.VERIFY_CODE);
    }


    public void sendCode() {

        if (show_new) {
            if (sendCDing_new) return;
            if (!verifyInput(2)) return;
            phone = edit_new_phone.getText().toString().trim();
            ToastHelper.showToast(this, "已发送");
            HttpClient.sendCode("updPhone", phone, this, RequestCommandCode.SEND_CODE);
            sendCDing_new = true;
            countDownTimer_new = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tv_send_code_new.setText(millisUntilFinished / 1000 + "S重新获取");
                }

                @Override
                public void onFinish() {
                    sendCDing_new = false;
                    tv_send_code_new.setText("重新获取");

                }
            };
            countDownTimer_new.start();
        } else {
            if (sendCDing_old) return;
            phone = "";
            ToastHelper.showToast(this, "已发送");
            HttpClient.sendCode("verify", phone, this, RequestCommandCode.SEND_CODE);
            sendCDing_old = true;
            countDownTimer_old = new CountDownTimer(60000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tv_code_send_old.setText(millisUntilFinished / 1000 + "S重新获取");
                }

                @Override
                public void onFinish() {
                    sendCDing_old = false;
                    tv_code_send_old.setText("重新获取");

                }
            };
            countDownTimer_old.start();
        }
    }


    /**
     * 验证输入信息
     *
     * @return
     */
    private boolean verifyInput(int type) {
        if (type == 1) {
            if (TextUtils.isEmpty(edit_old_code.getText().toString().trim())) {
                ToastHelper.showToast(this, "请输入原手机号验证码");
                return false;
            }

        }

        if (type == 2) {
            if (!RegularUtils.isMobileNO(edit_new_phone.getText().toString().trim())) {
                ToastHelper.showToast(this, "请输入正确的新手机号");
                return false;
            }

        }

        if (type == 3) {
            if (!RegularUtils.isMobileNO(edit_new_phone.getText().toString().trim())) {
                ToastHelper.showToast(this, "请输入正确的新手机号");
                return false;
            }
            if (TextUtils.isEmpty(edit_new_phone.getText().toString().trim())) {
                ToastHelper.showToast(this, "请输入新手机号验证码");
                return false;
            }

        }

        return true;
    }


    private void shownew(boolean showNew) {
        show_new = showNew;
        if (show_new) {
            top_bar.setTitle("更换手机号码");
            btn_next.setText("更换手机号码");
            layout_2.setVisibility(View.VISIBLE);
            layout_1.setVisibility(View.GONE);
        } else {
            top_bar.setTitle("验证手机号");
            btn_next.setText("更换手机号码");
            layout_2.setVisibility(View.GONE);
            layout_1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        ToastHelper.showToast(this, baseResponseData.getMsg());
        switch (requestCode) {
            case RequestCommandCode.VERIFY_CODE:
                ToastHelper.showToast(this, "验证通过");
                auth = JSON.parseObject(JSON.toJSONString(baseResponseData.getData())).getString("auth");
                shownew(true);
                break;
            case RequestCommandCode.UPDATE_PHONE:
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
        if (show_new) {
            shownew(show_new);
        } else {
            finish();
        }
    }
}
