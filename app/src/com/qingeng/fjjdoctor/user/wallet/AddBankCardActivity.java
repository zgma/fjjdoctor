package com.qingeng.fjjdoctor.user.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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


public class AddBankCardActivity extends UI implements HttpInterface, View.OnClickListener {


    private EditText edit_content_name;
    private EditText edit_content_id_number;
    private EditText edit_content_bank_card_number;
    private EditText edit_content_bank_name;

    private Button btn_go_add;


    public static void start(Context context) {
        Intent intent = new Intent(context, AddBankCardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    public static void startActivityForResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, AddBankCardActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_bank_card);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "添加银行卡";
        setToolBar(R.id.toolbar, options);


        edit_content_name = findViewById(R.id.edit_content_name);
        edit_content_id_number = findViewById(R.id.edit_content_id_number);
        edit_content_bank_card_number = findViewById(R.id.edit_content_bank_card_number);
        edit_content_bank_name = findViewById(R.id.edit_content_bank_name);
        btn_go_add = findViewById(R.id.btn_go_add);

        btn_go_add.setOnClickListener(this);
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
            case 10086:
                ToastHelper.showToast(this, baseResponseData.getMsg());
                edit_content_bank_card_number.setText("");
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
            case R.id.tv_get_code:
                break;
            case R.id.btn_go_add:
                String idCard = edit_content_id_number.getText().toString().trim();
                String userName = edit_content_name.getText().toString().trim();
                String openBank = edit_content_bank_name.getText().toString().trim();
                String cardNumber = edit_content_bank_card_number.getText().toString().trim();

                if (TextUtils.isEmpty(idCard) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(openBank) || TextUtils.isEmpty(cardNumber)) {
                    ToastHelper.showToast(this, "请输入完整内容");
                    return;
                }

                DialogMaker.showProgressDialog(this, "提交中");
                BaseRequestBean baseRequestBean = new BaseRequestBean();
                baseRequestBean.addParams("idCard", idCard);
                baseRequestBean.addParams("userName", userName);
                baseRequestBean.addParams("openBank", openBank);
                baseRequestBean.addParams("cardNumber", cardNumber);
                HttpClient.addBankCard(baseRequestBean, this, 10086);
                break;
        }
    }
}
