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

import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.team.ui.PayDialog;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.UpayResponseBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.PayHelper;


public class RechargeActivity extends UI implements View.OnClickListener, HttpInterface, PayHelper.IPayListener {

    private ConstraintLayout layout_ali;
    private ConstraintLayout layout_wx;
    private ImageView iv_selected_ali;
    private ImageView iv_selected_wx;
    private EditText edit_money;

    private TextView title_medal;
    private Button btn_send;


    private int payType = -1;

    public static void start(Context context) {
        Intent intent = new Intent(context, RechargeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "充值";
        setToolBar(R.id.toolbar, options);
        initUI();
    }


    private void initUI() {
        // 界面初始化
        layout_ali = findView(R.id.layout_ali);
        layout_wx = findView(R.id.layout_wx);
        iv_selected_ali = findView(R.id.iv_selected_ali);
        iv_selected_wx = findView(R.id.iv_selected_wx);
        title_medal = findView(R.id.title_medal);
        edit_money = findView(R.id.edit_money);
        btn_send = findView(R.id.btn_send);
        edit_money.addTextChangedListener(textWatcher);
        initSelected();
        payType = PayDialog.PAY_MODE_BY_ZFB;
        iv_selected_ali.setVisibility(View.VISIBLE);

        layout_ali.setOnClickListener(this);
        layout_wx.setOnClickListener(this);
        btn_send.setOnClickListener(this);

    }

    private void getPayOrder() {
        String moneyStr = edit_money.getText().toString().trim();
        if (TextUtils.isEmpty(moneyStr)) {
            ToastHelper.showToast(this, "请输入充值金额");
            return;
        }


        try {
//            Double money = Double.parseDouble(moneyStr) * 100;
            Double money = Double.parseDouble(moneyStr);
           /* if (payType == PayDialog.PAY_MODE_BY_WX) {

            } else if (payType == PayDialog.PAY_MODE_BY_ZFB) {
                BaseRequestBean baseRequestBean = new BaseRequestBean();
                baseRequestBean.addParams("total",money);//总金额，单位分
                baseRequestBean.addParams("type", 1);//1：充值 2：发放红包
                HttpClient.getPayOrderInfo_ali(baseRequestBean, this, RequestCommandCode.GET_ALI_PAY_ORDER_INFO);
            }*/

            DialogMaker.showProgressDialog(this, "获取订单中...");
            Double moneyDouble = Double.parseDouble(moneyStr) * 100;
            BaseRequestBean baseRequestBean = new BaseRequestBean();
            baseRequestBean.addParams("amount", moneyDouble.intValue());//总金额，单位分
            baseRequestBean.addParams("type", 1);//1：充值 2：发放红包
            HttpClient.uPayPrepareOrder(baseRequestBean, this, RequestCommandCode.UPAY_PREPARE_ORDER);


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
            case R.id.layout_ali:
                initSelected();
                payType = PayDialog.PAY_MODE_BY_ZFB;
                iv_selected_ali.setVisibility(View.VISIBLE);
                break;
            case R.id.layout_wx:
                initSelected();
                payType = PayDialog.PAY_MODE_BY_WX;
                iv_selected_wx.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_send:
                getPayOrder();
                break;
        }

    }

    private void initSelected() {
        payType = -1;
        iv_selected_ali.setVisibility(View.GONE);
        iv_selected_wx.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_ALI_PAY_ORDER_INFO:
                JSONObject jsonObject1 = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()));
                String aliStr = (String) jsonObject1.get("body");
                PayHelper.getInstance().setIPayListener(this);
                PayHelper.getInstance().AliPay(this, aliStr);
                ToastHelper.showToast(this, "处理成功");
                break;
            case RequestCommandCode.UPAY_PREPARE_ORDER:
                UpayResponseBean upayResponseBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                goUPayRecharge(upayResponseBean);
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

    void goUPayRecharge(UpayResponseBean upayResponseBean) {

    }

}
