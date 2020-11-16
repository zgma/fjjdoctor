package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;

import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.widget.ClearableEditTextWithIcon;

import org.greenrobot.eventbus.EventBus;


/**
 * 登录/注册界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class ChangePasswordActivity extends UI implements OnKeyListener, View.OnClickListener, HttpInterface {


    private ClearableEditTextWithIcon oldPass;

    private ClearableEditTextWithIcon newPass;

    private ClearableEditTextWithIcon reNewPass;

    private Button btn_ok;

    private int type = 0;


    public static void start(Context context) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type",0);
        context.startActivity(intent);
    }

    public static void start(Context context,int type) {
        Intent intent = new Intent(context, ChangePasswordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type",type);
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
        setContentView(R.layout.activity_change_pass);
        ToolBarOptions options = new NimToolBarOptions();
        type = getIntent().getExtras().getInt("type",0);

        options.titleString = type == 0?"修改登录密码":"修改支付密码";
        setToolBar(R.id.toolbar, options);
        setupLoginPanel();
    }


    /**
     * 登录面板
     */
    private void setupLoginPanel() {
        oldPass = findView(R.id.edit_old_pass);
        newPass = findView(R.id.edit_new_pass);
        reNewPass = findView(R.id.edit_re_new_pass);
        btn_ok = findView(R.id.btn_ok);

        btn_ok.setOnClickListener(this);

        if (type == 0){
            oldPass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //最大输入长度
            oldPass.setInputType( InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
            newPass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //最大输入长度
            newPass.setInputType( InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
            reNewPass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)}); //最大输入长度
            reNewPass.setInputType( InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
            oldPass.setHint("旧登录密码");
            newPass.setHint("新登录密码");
        }else {
            oldPass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
            oldPass.setInputType( InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            newPass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
            newPass.setInputType( InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            reNewPass.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
            reNewPass.setInputType( InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            oldPass.setHint("旧支付密码");
            newPass.setHint("新支付密码");
        }
        reNewPass.setHint("确认新密码");

    }


    private boolean checkInput() {

        // 密码检查
        String newPass1 = newPass.getText().toString().trim();


        if (type == 0){
            if (newPass1.length() < 6 || newPass1.length() > 20) {
                ToastHelper.showToast(ChangePasswordActivity.this, R.string.register_password_tip);
                return false;
            }
        }else {
            if (newPass1.length() != 6 ) {
                ToastHelper.showToast(ChangePasswordActivity.this, "请输入6位数字密码");
                return false;
            }
        }


        // 密码检查
        String newPass2 = reNewPass.getText().toString().trim();
        if (!newPass2.equals(newPass1)) {
            ToastHelper.showToast(ChangePasswordActivity.this, "两次密码不一致");
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (checkInput()){
                    BaseRequestBean baseRequestBean = new BaseRequestBean();
                    baseRequestBean.addParams("oldPassword", oldPass.getText().toString().trim());
                    baseRequestBean.addParams("newPassword", newPass.getText().toString());
                    DialogMaker.showProgressDialog(this, "提交中...");
                    if (type == 0){
                        HttpClient.modifyPassword(baseRequestBean, this,RequestCommandCode.MODIFY_PASSWORD);
                    }else {
                        HttpClient.modifyPayPassword(baseRequestBean, this,RequestCommandCode.MODIFY_PASSWORD);
                    }

                }
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.MODIFY_PASSWORD:
                if (type == 0){
                    ToastHelper.showToast(this, "设置成功，请重新登录");
                    finish();
                    EventBus.getDefault().post("402");
                }else {
                    finish();
                }
                break;
        }
        ToastHelper.showToast(ChangePasswordActivity.this, baseResponseData.getMsg());
    }


    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(ChangePasswordActivity.this, message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
