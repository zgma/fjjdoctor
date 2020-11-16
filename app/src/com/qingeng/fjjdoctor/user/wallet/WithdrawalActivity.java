package com.qingeng.fjjdoctor.user.wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leon.lib.settingview.LSettingItem;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.team.ui.PayDialog;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BankCardBean;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.UpayResponseBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
import com.qingeng.fjjdoctor.util.PayHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WithdrawalActivity extends UI implements View.OnClickListener, HttpInterface, PayHelper.IPayListener {

    public static final int REQUEST_CODE_PICK = 10002;


    @BindView(R.id.tv_money)
    TextView tv_money;


    @BindView(R.id.edit_money)
    EditText edit_money;

    @BindView(R.id.btn_send)
    Button btn_send;
    @BindView(R.id.tv_set_all)
    TextView tv_set_all;

    @BindView(R.id.item_1)
    LSettingItem item_1;

    BankCardBean bankCardBean;

    public static void start(Context context) {
        Intent intent = new Intent(context, WithdrawalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        ButterKnife.bind(this);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "提现";
        setToolBar(R.id.toolbar, options);
        initUI();
    }


    private void initUI() {
        // 界面初始化
        edit_money.addTextChangedListener(textWatcher);
        initSelected();

        btn_send.setOnClickListener(this);
        tv_set_all.setOnClickListener(this);
        item_1.setmOnLSettingItemClick((id, isChecked) -> {
            MyBankCardListActivity.startForResult(this, 1, REQUEST_CODE_PICK);
        });
        item_1.setRightText("");

        getUserData();
        getBankList();

    }

    private void getUserData() {
        HttpClient.getDoctorInfo(this, RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID);
    }

    private void getPayOrder() {
        String moneyStr = edit_money.getText().toString().trim();
        if (TextUtils.isEmpty(moneyStr)) {
            ToastHelper.showToast(this, "请输入充值金额");
            return;
        }

        if (bankCardBean == null || bankCardBean.getId() < 0) {
            ToastHelper.showToast(this, "请选择银行卡");
            return;
        }

        try {
            Double money = Double.parseDouble(moneyStr);

            DialogMaker.showProgressDialog(this, "获取订单中...");
            BaseRequestBean baseRequestBean = new BaseRequestBean();
            baseRequestBean.addParams("cardId", bankCardBean.getId());
            baseRequestBean.addParams("withdrawalMoney", money);
            HttpClient.userWithdrawal(baseRequestBean, this, RequestCommandCode.UPAY_WITHDREW_CREATE);


        } catch (Exception e) {
            ToastHelper.showToast(this, e.getMessage());
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                getPayOrder();
                break;
            case R.id.tv_set_all:
                edit_money.setText(LocalDataUtils.getWalletInfo().getCurrentPrice() + "");
                break;
        }

    }

    private void initSelected() {
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID:
                LocalDataUtils.saveLocalDoctor(baseResponseData.getData());
                tv_money.setText(LocalDataUtils.getWalletInfo().getCurrentPrice() + "");
                break;
            case RequestCommandCode.UPAY_WITHDREW_CREATE:
                ToastHelper.showToast(this, baseResponseData.getMsg());
                getUserData();
                break;
            case 100016:
                List<BankCardBean> dataList = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), BankCardBean.class);
                if (dataList.size() > 0) bankCardBean = dataList.get(0);
                item_1.setRightText(bankCardBean.getOpenBank());
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
    public void onSuccess() {
        ToastHelper.showToast(this, "支付成功");
        finish();
    }

    @Override
    public void onFail() {
        ToastHelper.showToast(this, "支付失败");
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK && resultCode == RESULT_OK) {
            bankCardBean = (BankCardBean) data.getSerializableExtra("data");
            item_1.setRightText(bankCardBean.getOpenBank());
        }
    }

    private void getBankList() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpClient.selectUserBankList(baseRequestBean, this, 100016);
    }

}
