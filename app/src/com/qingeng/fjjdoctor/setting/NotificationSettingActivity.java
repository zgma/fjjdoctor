package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;


/**
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class NotificationSettingActivity extends UI implements SwitchButton.OnChangedListener {

    private SwitchButton switchButton_1;
    private SwitchButton switchButton_2;
    private SwitchButton switchButton_3;


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, NotificationSettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);


        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "新消息提醒";
        setToolBar(R.id.toolbar, options);

        initUi();
        getUserInfo();


    }

    private void getUserInfo() {

        HttpClient.getLoginUserInfo(new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
               /* JSONObject data = (JSONObject) JSON.toJSON(baseResponseData.getData());
                UserInfo userInfo = data.toJavaObject(UserInfo.class);
                switchButton_1.setCheck(userInfo.getUserInfo().getMsgNotify() == 1);
                switchButton_2.setCheck(userInfo.getUserInfo().getSoundNotify() == 1);
                switchButton_3.setCheck(userInfo.getUserInfo().getShakeNotify() == 1);*/
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(NotificationSettingActivity.this, "查询用户信息失败");
            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID);

    }

    private void initUi() {

        ((TextView) findViewById(R.id.toggle_layout_1).findViewById(R.id.user_profile_title)).setText("接收新消息通知");
        ((TextView) findViewById(R.id.toggle_layout_2).findViewById(R.id.user_profile_title)).setText("新消息声音通知");
        ((TextView) findViewById(R.id.toggle_layout_3).findViewById(R.id.user_profile_title)).setText("新消息震动通知");

        switchButton_1 = (SwitchButton) findViewById(R.id.toggle_layout_1).findViewById(R.id.user_profile_toggle);
        switchButton_2 = (SwitchButton) findViewById(R.id.toggle_layout_2).findViewById(R.id.user_profile_toggle);
        switchButton_3 = (SwitchButton) findViewById(R.id.toggle_layout_3).findViewById(R.id.user_profile_toggle);

        switchButton_1.setOnChangedListener(this);
        switchButton_2.setOnChangedListener(this);
        switchButton_3.setOnChangedListener(this);

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
    public void OnChanged(View v, boolean checkState) {
        int type = -1;
        if (v == switchButton_1) {
            if (checkState) {
                type = 1;
            } else {
                type = 2;
            }
        }
        ;
        if (v == switchButton_2) {
            if (checkState) {
                type = 3;
            } else {
                type = 4;
            }
        }
        ;
        if (v == switchButton_3) {
            if (checkState) {
                type = 5;
            } else {
                type = 6;
            }
        }
        ;

        DialogMaker.showProgressDialog(this, "处理中...");
        HttpClient.updateNotifyType(type, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                ToastHelper.showToast(NotificationSettingActivity.this, "设置成功");
                getUserInfo();

            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(NotificationSettingActivity.this, "设置失败：" + message);
            }

            @Override
            public void onComplete() {
                DialogMaker.dismissProgressDialog();
            }
        }, RequestCommandCode.PRIVACY_SETTING);

    }
}
