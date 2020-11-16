package com.qingeng.fjjdoctor.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.acker.simplezxing.activity.RcodeUtil;
import com.alibaba.fastjson.JSON;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.TargetUserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.DemoCache;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.user.MyCodeActivity;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

public class SendManyMessageActivity extends UI {

    private static final String EXTRA_ACCID = "EXTRA_ACCID";
    String accId;
    private HeadImageView userhead;

    private TextView tv_user_name;
    private TextView tv_user_des;
    private ImageView iv_my_code;

    private TargetUserBean targetUserBean;


    public static void start(Context context, String accId) {
        Intent intent = new Intent(context, MyCodeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_ACCID, accId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_code);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.my_code;
        setToolBar(R.id.toolbar, options);
        accId = getIntent().getStringExtra(EXTRA_ACCID);
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
        // 界面初始化
        userhead = findViewById(R.id.user_photo);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_des = findViewById(R.id.tv_user_des);
        iv_my_code = findViewById(R.id.iv_my_code);

        final UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(DemoCache.getAccount());
        tv_user_name.setText(userInfo.getName());
        tv_user_des.setText("ID：" + userInfo.getAccount());
        userhead.loadBuddyAvatar(DemoCache.getAccount());
        // **sx + accid   **sx_group
        //base64编码
        HttpClient.userInfoByAccId(accId, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                targetUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                tv_user_name.setText(targetUserBean.getTargetUsername());
                tv_user_des.setText("ID：" + targetUserBean.getUniqueId());
                userhead.loadImgForUrl(targetUserBean.getHeadImage());
                String url ="**sx"+targetUserBean.getTargetUserAccid();
                //base64编码
                String strBase64 = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
                RcodeUtil.createQRcodeImage(strBase64,iv_my_code);
            }

            @Override
            public void onFailure(int requestCode, String message) {

            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID);
    }



}
