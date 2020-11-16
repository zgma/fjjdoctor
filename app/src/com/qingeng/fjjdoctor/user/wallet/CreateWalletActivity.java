package com.qingeng.fjjdoctor.user.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.NetWorkUtils;
import com.qingeng.fjjdoctor.util.RegularUtils;



public class CreateWalletActivity extends UI implements HttpInterface, View.OnClickListener {


    private EditText edit_name;
    private EditText edit_id_number;
    private EditText edit_phone_number;
    private EditText edit_nickname;
    private Spinner spinner;

    private Button btn_go_add;


    String[] profession = new String[]{"A","B","C","D","E","F","G"};



    public static void start(Context context) {
        Intent intent = new Intent(context, CreateWalletActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    public static void startActivityForResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, CreateWalletActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_wallet);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "钱包开户";
        setToolBar(R.id.toolbar, options);


        edit_name = findViewById(R.id.edit_name);
        edit_id_number = findViewById(R.id.edit_id_number);
        edit_phone_number = findViewById(R.id.edit_phone_number);
        edit_nickname = findViewById(R.id.edit_nickname);
        spinner = findViewById(R.id.spinner);
        btn_go_add = findViewById(R.id.btn_go_add);

        btn_go_add.setOnClickListener(this);


        spinner.setSelection(0);

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
            case RequestCommandCode.CREATE_WALLET:
                ToastHelper.showToast(this, "操作成功");
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
            case R.id.btn_go_add:
                createWallet();
                break;
        }
    }

    private void createWallet() {
        if (!verifyInput()) return;

        String mac = NetWorkUtils.getMacFromHardware(this);
        String ip = NetWorkUtils.getIPAddress(this);

        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("name", edit_name.getText().toString().trim());
        baseRequestBean.addParams("idCardNo", edit_id_number.getText().toString().trim());
        baseRequestBean.addParams("mobile", edit_phone_number.getText().toString().trim());
        baseRequestBean.addParams("profession", profession[spinner.getSelectedItemPosition()]);
        baseRequestBean.addParams("ip", ip);
        baseRequestBean.addParams("mac", mac);
        baseRequestBean.addParams("nickName", edit_nickname.getText().toString().trim());
        DialogMaker.showProgressDialog(this, "提交中");
        HttpClient.createWallet(baseRequestBean, this, RequestCommandCode.CREATE_WALLET);
    }

    private boolean verifyInput() {
        if (TextUtils.isEmpty(edit_name.getText().toString().trim())) {
            ToastHelper.showToast(this, "请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(edit_id_number.getText().toString().trim())) {
            ToastHelper.showToast(this, "请输入身份证号");
            return false;
        }
        if (TextUtils.isEmpty(edit_nickname.getText().toString().trim())) {
            ToastHelper.showToast(this, "请输入昵称");
            return false;
        }
        if (!RegularUtils.isMobileNO(edit_phone_number.getText().toString().trim())) {
            ToastHelper.showToast(this, "请输入正确手机号码！");
            return false;
        }
        return true;
    }
}
