package com.qingeng.fjjdoctor.contact.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.UpayResponseBean;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.NetWorkUtils;




public class AddFriendSysActivity extends UI  {



    private EditText edit_count;
    private Button btn_send;
    private UpayResponseBean upayResponseBean;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AddFriendSysActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_add);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "";
        setToolBar(R.id.toolbar, options);
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void findViews() {
        edit_count = findViewById(R.id.edit_count);
        btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = edit_count.getText().toString().trim();
                if (TextUtils.isEmpty(input)){
                    ToastHelper.showToast(AddFriendSysActivity.this, "输入正确的数量");
                    return;
                }
                try {

                    int count = Integer.parseInt(input);
                    if (count > 0){
                        DialogMaker.showProgressDialog(AddFriendSysActivity.this, "获取订单");
                        String clientIp = NetWorkUtils.getIPAddress(AddFriendSysActivity.this);
                        HttpClient.autoAddFriend(count,clientIp, new HttpInterface() {
                            @Override
                            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                                upayResponseBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                                goUPay();
                            }

                            @Override
                            public void onFailure(int requestCode, String message) {
                                ToastHelper.showToast(AddFriendSysActivity.this, message);
                            }

                            @Override
                            public void onComplete() {
                                DialogMaker.dismissProgressDialog();
                            }
                        }, 1);
                    }else {
                        ToastHelper.showToast(AddFriendSysActivity.this, "输入正确的数量");
                    }
                }catch (Exception e){
                    ToastHelper.showToast(AddFriendSysActivity.this, "输入正确的数量");
                }
            }
        });

    }

    void goUPay() {
    }



}

