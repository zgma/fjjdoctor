package com.qingeng.fjjdoctor.redpacket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.TransferBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.session.extension.TransferAttachment;

public class TransferDetailActivity extends UI implements HttpInterface {

    private TextView tv_transfer_money;
    private TextView tv_transfer_time;
    private TextView tv_transfer_name;
    private TextView tv_desc;


    private TransferAttachment transferAttachment;

    public static void start(Context context, TransferAttachment attachment) {
        Intent intent = new Intent(context, TransferDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("attachment",attachment);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_detail);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "";
        setToolBar(R.id.toolbar, options);
        transferAttachment = (TransferAttachment) getIntent().getExtras().getSerializable("attachment");
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initUI() {

        tv_transfer_money = findViewById(R.id.tv_transfer_money);
        tv_transfer_time = findViewById(R.id.tv_transfer_time);
        tv_transfer_name = findViewById(R.id.tv_transfer_name);
        tv_desc = findViewById(R.id.tv_desc);
        tv_desc.setText("钱已存入钱包");

        getData();

    }

    private void getData() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("orderNo", transferAttachment.getOrderNum());
        HttpClient.queryTranByOrderNo(baseRequestBean, this, RequestCommandCode.QUERY_TRAN_BY_ORDER_NO);
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.QUERY_TRAN_BY_ORDER_NO:
                TransferBean transferBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TransferBean.class);
                if (transferBean == null){
                    tv_transfer_money.setText(transferAttachment.getTransferMmoeny().replace("+",""));
                    tv_transfer_time.setText(transferAttachment.getCreateDate());
                    tv_transfer_name.setText(transferAttachment.getUserName());
                }else {
                    tv_transfer_money.setText(transferBean.getMoney()+"");
                    tv_transfer_time.setText(transferBean.getCreateDateTime());
                    tv_transfer_name.setText(transferBean.getUsername());
                }
                tv_desc.setText("钱已存入钱包");
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, "失败 " + message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }


}
