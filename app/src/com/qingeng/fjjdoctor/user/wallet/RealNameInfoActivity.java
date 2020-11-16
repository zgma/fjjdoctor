package com.qingeng.fjjdoctor.user.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyEditDialog;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.RegularUtils;


public class RealNameInfoActivity extends UI implements HttpInterface, View.OnClickListener {


    private TextView tv_status;
    private TextView tv_name;
    private TextView tv_id_number;
    private TextView tv_phone_number;
    private TextView tv_work;


    public static void start(Context context) {
        Intent intent = new Intent(context, RealNameInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    public static void startActivityForResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, RealNameInfoActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_real_name_info);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "实名认证";
        setToolBar(R.id.toolbar, options);

        tv_status = findViewById(R.id.tv_status);
        tv_name = findViewById(R.id.tv_name);
        tv_id_number = findViewById(R.id.tv_id_number);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_work = findViewById(R.id.tv_work);

        tv_phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EasyEditDialog requestDialog = new EasyEditDialog(RealNameInfoActivity.this);
                requestDialog.setEditTextMaxLength(32);
                requestDialog.setTitle("修改手机号");
                requestDialog.setInputType(InputType.TYPE_CLASS_PHONE);
                requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestDialog.dismiss();
                    }
                });
                requestDialog.addPositiveButtonListener(R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String msg = requestDialog.getEditMessage();
                        if (checkPhone(msg)) {
                            DialogMaker.showProgressDialog(RealNameInfoActivity.this, "提交中");
                            BaseRequestBean baseRequestBean = new BaseRequestBean();
                            baseRequestBean.addParams("mobile", msg);
                            HttpClient.updateUpayMobile(baseRequestBean, RealNameInfoActivity.this, 1);
                            requestDialog.dismiss();
                        }
                    }
                });
                requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
                requestDialog.show();
            }
        });

        getUserData();
    }

    private void getUserData(){
        DialogMaker.showProgressDialog(this, "获取信息");
        HttpClient.getLoginUserInfo(this, 2);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case 2:
               /* JSONObject data = (JSONObject) JSON.toJSON(baseResponseData.getData());
                UserInfo userInfo = data.toJavaObject(UserInfo.class);
                Preferences.saveUserBean(userInfo.getUserInfo());
                Preferences.saveWalletInfo(userInfo.getWallet());
                Preferences.saveUPayInfo(userInfo.getUpay());

                AppPreferences.saveUserId(userInfo.getUserInfo().getUserId() + "");
                AppPreferences.saveThemeId(userInfo.getUserInfo().getThemeId());


                WalletInfo walletInfo = Preferences.getWalletInfo();
                tv_status.setText("认证成功");
                tv_name.setText(walletInfo.getUsername());
                tv_id_number.setText(CommonUtils.idEncrypt(walletInfo.getIdCard()));
                tv_phone_number.setText(CommonUtils.mobileEncrypt(walletInfo.getMobile()));
                tv_work.setText(walletInfo.getCareerValue());*/

                break;
            case 1:
                ToastHelper.showToast(this, "修改成功");
                getUserData();
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
            case R.id.btn_go_add:
                break;
        }
    }

    private boolean checkPhone(String phone) {
        // 帐号检查
        if (!RegularUtils.isMobileNO(phone)) {
            ToastHelper.showToast(RealNameInfoActivity.this, "请输入正确手机号码！");
            return false;
        }
        return true;
    }

}
