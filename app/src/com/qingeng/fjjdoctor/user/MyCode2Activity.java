package com.qingeng.fjjdoctor.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.acker.simplezxing.activity.RcodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.DoctorInfoResultBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
import com.qingeng.fjjdoctor.util.OSUtil;


public class MyCode2Activity extends UI {

    private static final String EXTRA_ACCID = "EXTRA_ACCID";
    String accId;
    private HeadImageView userhead;

    private TextView tv_user_name;
    private ImageView iv_my_code;


    public static void start(Context context) {
        Intent intent = new Intent(context, MyCode2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_code2);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "邀约医生";
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
        iv_my_code = findViewById(R.id.iv_my_code);
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("channelType", 2);
        baseRequestBean.addParams("systemType", 1);
        //base64编码
        HttpClient.getDoctorUploadUrl(baseRequestBean, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                String url = "";
                if (jsonObject != null) {
                    String brand = OSUtil.getDeviceBrand();
                    if (brand.toUpperCase().equals("HUAWEI") || brand.toUpperCase().equals("HONOR")) {
                        url = jsonObject.getString("huaweiUrl");
                    } else if (brand.toUpperCase().contains("XIAOMI")) {
                        url = jsonObject.getString("xiaomiUrl");
                    } else if (brand.toUpperCase().contains("OPPO")) {
                        url = jsonObject.getString("oppoUrl");
                    } else if (brand.toUpperCase().contains("VIVO")) {
                        url = jsonObject.getString("vivoUrl");
                    } else {
                        url = jsonObject.getString("otherUrl");
                    }
                }
                RcodeUtil.createQRcodeImage(url, iv_my_code);
            }

            @Override
            public void onFailure(int requestCode, String message) {

            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID);

        tv_user_name.setText(LocalDataUtils.getDoctorInfo().getRealName());
        userhead.loadImgForUrl(LocalDataUtils.getUserInfo().getAvatar());
    }


}

