package com.netease.nim.uikit.business.team.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.acker.simplezxing.activity.RcodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.bean.TargetUserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;


public class TeamCodeActivity extends UI  {

    private static final String EXTRA_ACCID = "EXTRA_ACCID";
    String accId;
    private HeadImageView userhead;

    private TextView tv_user_name;
    private TextView tv_user_des;
    private ImageView iv_my_code;

    private TargetUserBean targetUserBean;


    public static void start(Context context,String accId) {
        Intent intent = new Intent(context, TeamCodeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(EXTRA_ACCID, accId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_code);
        accId = getIntent().getStringExtra(EXTRA_ACCID);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString ="群二维码";
        setToolBar(R.id.toolbar, options);
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

        // **sx + accid   **sx_group
        //base64编码
        HttpClient.groupDetail(accId, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                GroupDetailBean.WaHuHighGroup  waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);
                tv_user_name.setText(waHuHighGroup.getTname());
                tv_user_des.setText("ID：" + waHuHighGroup.getTid());
                userhead.loadImgForUrl(waHuHighGroup.getIcon());
                String url ="**sx_group"+waHuHighGroup.getTid();
                //base64编码
                String strBase64 = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
                RcodeUtil.createQRcodeImage(strBase64,iv_my_code);
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(TeamCodeActivity.this, "获取详情失败："+message);

            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.GROUP_DETAIL);

    }



}

