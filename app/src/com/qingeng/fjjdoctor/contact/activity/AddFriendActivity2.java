package com.qingeng.fjjdoctor.contact.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.TargetUserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.main.model.Extras;
import com.qingeng.fjjdoctor.util.LocalDataUtils;

/**
 * 用户资料页面
 * Created by huangjun on 2015/8/11.
 */
public class AddFriendActivity2 extends UI implements HttpInterface {

    private static final String TAG = AddFriendActivity2.class.getSimpleName();

    private final boolean FLAG_ADD_FRIEND_DIRECTLY = false; // 是否直接加为好友开关，false为需要好友申请
    private final String KEY_BLACK_LIST = "black_list";
    private final String KEY_MSG_NOTICE = "msg_notice";
    private final String KEY_RECENT_STICKY = "recent_contacts_sticky";

    private String account;
    private TargetUserBean targetUserBean;

    // 基本信息
    private HeadImageView user_photo;
    private TextView tv_user_name;
    private TextView tv_user_des;

    private EditText edit_msg;
    private EditText edit_alis;
    private Button btn_send;


    private String accountType = "";


    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, AddFriendActivity2.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void start2(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, AddFriendActivity2.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT_ACCID, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend_activity2);
        if (getIntent().getStringExtra(Extras.EXTRA_ACCOUNT) != null) {
            account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
            accountType = Extras.EXTRA_ACCOUNT;
        }
        if (getIntent().getStringExtra(Extras.EXTRA_ACCOUNT_ACCID) != null) {
            account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT_ACCID);
            accountType = Extras.EXTRA_ACCOUNT_ACCID;
        }

        if (TextUtils.isEmpty(account)) {
            ToastHelper.showToast(AddFriendActivity2.this, "传入的帐号为空");
            finish();
            return;
        }

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "添加好友";
        setToolBar(R.id.toolbar, options);
        findViews();
        getUserData();
    }

    private void getUserData() {

        if (accountType == Extras.EXTRA_ACCOUNT) {
            HttpClient.getOtherUserInfo(account, this, RequestCommandCode.GET_OTHER_USER_INFO);
        }
        if (accountType == Extras.EXTRA_ACCOUNT_ACCID) {
            HttpClient.userInfoByAccId(account, this, RequestCommandCode.GET_OTHER_USER_INFO);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void findViews() {

        // 基本信息
        user_photo = findViewById(R.id.user_photo);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_des = findViewById(R.id.tv_user_des);
        edit_msg = findViewById(R.id.edit_msg);
        edit_alis = findViewById(R.id.edit_alis);
        btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(onClickListener);
    }


    private void updateUserInfoView() {
        // 基本信息
        user_photo.loadImgForUrl(targetUserBean.getHeadImage());
        tv_user_name.setText(TextUtils.isEmpty(targetUserBean.getAlias()) ? targetUserBean.getTargetUsername() : targetUserBean.getAlias());
        tv_user_des.setText("ID：" + targetUserBean.getUniqueId());

        if (targetUserBean.getRelationShip() == 2) {
            ToastHelper.showToast(this, "请勿重复添加");
            finish();
        }

    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btn_send) {
                String msg = edit_msg.getText().toString().trim();
                String alis = edit_alis.getText().toString().trim();
           /*     if (TextUtils.isEmpty(msg)) {
                    ToastHelper.showToast(AddFriendActivity2.this, "请输入验证消息");
                    return;
                }*/
                doAddFriend(msg, alis); // 发起好友验证请求
            }
        }
    };


    private void doAddFriend(String msg, String alis) {
        if (!NetworkUtil.isNetAvailable(this)) {
            ToastHelper.showToast(this, R.string.network_is_not_available);
            return;
        }
        DialogMaker.showProgressDialog(this, "", true);
        HttpClient.addFriends(targetUserBean.getTargetUserId() + "", msg, alis, this, RequestCommandCode.ADD_FRIENDS);
        Log.i(TAG, "onAddFriendByVerify");
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_OTHER_USER_INFO:
                targetUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                updateUserInfoView();
                break;
            case RequestCommandCode.ADD_FRIENDS:
                ToastHelper.showToast(this, "请求已发送");
                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()));
                if (jsonObject.containsKey("needCheck") && jsonObject.getInteger("needCheck") == 0) {
                    IMMessage msg = MessageBuilder.createTipMessage(targetUserBean.getTargetUserAccid(), SessionTypeEnum.P2P);
                    msg.setContent(LocalDataUtils.getUserInfo().getNickName() + "通过好友验证");
                    CustomMessageConfig config = new CustomMessageConfig();
                    config.enablePush = true; // 不推送
                    msg.setConfig(config);

                    NIMClient.getService(MsgService.class).sendMessage(msg, false).setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {

                        }

                        @Override
                        public void onFailed(int code) {

                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                }
                finish();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {

    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }
}
