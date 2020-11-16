package com.qingeng.fjjdoctor.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.acker.simplezxing.activity.RcodeUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.SimpleUtils;
import com.qingeng.fjjdoctor.util.WechatShareManager;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.List;

import io.reactivex.functions.Consumer;

public class MyExtensionActivity extends UI {


    private ImageView iv_my_code;
    private ImageView iv_save_pic;
    private ImageView iv_wx;
    private RelativeLayout layout_content;
    private TextView tv_message;

    WechatShareManager mShareManager;

    private String myUrl = "";

    public static void start(Context context) {
        Intent intent = new Intent(context, MyExtensionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_extension);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "全民兼职";
        options.titleColor = R.color.white;
        options.navigateId = R.drawable.actionbar_white_back_icon;
        options.backgrounpColor = R.color.topbar_backgroup_blue;
        setToolBar(R.id.toolbar, options);
        initUI();

        mShareManager = WechatShareManager.getInstance(this);
        getText();
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

        layout_content = findViewById(R.id.layout_content);
        tv_message = findViewById(R.id.tv_message);
        iv_wx = findViewById(R.id.iv_wx);
        iv_save_pic = findViewById(R.id.iv_save_pic);
        iv_my_code = findViewById(R.id.iv_my_code);
        HttpClient.sharePage(new BaseRequestBean(), new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                myUrl = (String) baseResponseData.getData();
                //base64编码
                String strBase64 = Base64.encodeToString(myUrl.getBytes(), Base64.DEFAULT);
//                RcodeUtil.createQRcodeImage(url,iv_my_code);
                RcodeUtil.createQRcodeImageWithLogo(myUrl, iv_my_code, BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo));

            }

            @Override
            public void onFailure(int requestCode, String message) {

            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID);

        iv_save_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermissions rxPermissions = new RxPermissions(MyExtensionActivity.this);
                rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    iv_save_pic.setVisibility(View.INVISIBLE);
                                    Bitmap bitmap = SimpleUtils.getCacheBitmapFromView(layout_content);
                                    SimpleUtils.saveBitmapToSdCard(MyExtensionActivity.this, bitmap, "my_extension_code" + System.currentTimeMillis());
                                    iv_save_pic.setVisibility(View.VISIBLE);
                                    ToastHelper.showToast(MyExtensionActivity.this, "保存图片成功");
                                } else {
                                    ToastHelper.showToast(MyExtensionActivity.this, "=没有文件读写权限，无法保存图片");
                                }
                            }
                        });
            }
        });


        iv_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isWeixinAvilible(MyExtensionActivity.this)) {
                    WechatShareManager.ShareContentWebpage mShareContentText =
                            (WechatShareManager.ShareContentWebpage) mShareManager.getShareContentWebpag("全名兼职","分享注册享好礼",myUrl,R.drawable.ic_logo);
                    mShareManager.shareByWebchat(mShareContentText, WechatShareManager.WECHAT_SHARE_TYPE_TALK);
                }else {
                    ToastHelper.showToast(MyExtensionActivity.this, "您还没有安装微信，请先安装微信客户端");
                }

            }
        });

    }


    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


    private void getText(){
        HttpClient.sharePageTitle(new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                if (!TextUtils.isEmpty(jsonObject.getString("commandVal"))){
                    tv_message.setText(jsonObject.getString("commandVal"));
                    tv_message.setVisibility(View.VISIBLE);
                }else {
                    tv_message.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(int requestCode, String message) {

            }

            @Override
            public void onComplete() {

            }
        }, 1);
    }

}
