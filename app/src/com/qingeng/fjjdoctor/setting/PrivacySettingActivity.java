package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.qingeng.fjjdoctor.user.MyBlackListActivity;


/**
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class PrivacySettingActivity extends UI implements SwitchButton.OnChangedListener {

    private SwitchButton switchButton_1;
    private SwitchButton switchButton_2;
    private SwitchButton switchButton_3;
    private SwitchButton switchButton_4;
    private ConstraintLayout layout_my_service;


    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PrivacySettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_setting);


        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "隐私设置";
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
                if (userInfo.getUserInfo().getNeedCheck()==1)
                    switchButton_1.setCheck(userInfo.getUserInfo().getNeedCheck()==1);
                    switchButton_2.setCheck(userInfo.getUserInfo().getPhoneSearch()==1);
                    switchButton_3.setCheck(userInfo.getUserInfo().getAppSearch()==1);
                    switchButton_4.setCheck(userInfo.getUserInfo().getIdentitySearch()==1);*/
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(PrivacySettingActivity.this, "查询用户信息失败");
            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID);

    }

    private void initUi() {

        ((TextView) findViewById(R.id.toggle_layout_1).findViewById(R.id.user_profile_title)).setText("添加我为好友时需要验证");
        ((TextView) findViewById(R.id.toggle_layout_2).findViewById(R.id.user_profile_title)).setText("可以通过手机号搜索");
        ((TextView) findViewById(R.id.toggle_layout_3).findViewById(R.id.user_profile_title)).setText("可以通过APP号搜索");
        ((TextView) findViewById(R.id.toggle_layout_4).findViewById(R.id.user_profile_title)).setText("可以通过名片添加");

        switchButton_1 = (SwitchButton) findViewById(R.id.toggle_layout_1).findViewById(R.id.user_profile_toggle);
        switchButton_2 = (SwitchButton) findViewById(R.id.toggle_layout_2).findViewById(R.id.user_profile_toggle);
        switchButton_3 = (SwitchButton) findViewById(R.id.toggle_layout_3).findViewById(R.id.user_profile_toggle);
        switchButton_4 = (SwitchButton) findViewById(R.id.toggle_layout_4).findViewById(R.id.user_profile_toggle);
        layout_my_service = findViewById(R.id.layout_my_service);

        switchButton_1.setOnChangedListener(this);
        switchButton_2.setOnChangedListener(this);
        switchButton_3.setOnChangedListener(this);
        switchButton_4.setOnChangedListener(this);
        layout_my_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyBlackListActivity.start(PrivacySettingActivity.this);
            }
        });

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
        int number = 0;
        int type = -1;
        if (v == switchButton_1) number = 1;
        if (v == switchButton_2) number = 2;
        if (v == switchButton_3) number = 3;
        if (v == switchButton_4) number = 4;
        if (checkState) {
            type = 1;
        } else {
            type = 0;
        }
        DialogMaker.showProgressDialog(this, "处理中...");
        HttpClient.privacySetting(type, number, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                ToastHelper.showToast(PrivacySettingActivity.this, "设置成功");
                getUserInfo();

            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(PrivacySettingActivity.this, "设置失败：" + message);
            }

            @Override
            public void onComplete() {
                DialogMaker.dismissProgressDialog();
            }
        }, RequestCommandCode.PRIVACY_SETTING);

    }
}
