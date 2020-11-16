package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.BuildConfig;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.VersionBean;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.apilibrary.util.DownloadUtil;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.ConfigInfoUtils;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.qingeng.fjjdoctor.widget.UpgradeDialog;

import java.io.File;

//import com.netease.nim.avchatkit.AVChatKit; 音频注释

/**
 * Created by hzxuwen on 2015/6/26.
 */
public class AboutUsActivity extends UI implements DownloadUtil.OnDownloadListener{

    private TextView tv_version;
    private TextView tv_1;
    private TextView tv_2;
    private TextView tv_3;

    RelativeLayout layout_3;


    public static void start(Context context) {
        Intent intent = new Intent(context, AboutUsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_activity);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "关于我们";
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
        tv_version = findView(R.id.tv_version);
        tv_1 = findView(R.id.tv_1);
        tv_2 = findView(R.id.tv_2);
        tv_3 = findView(R.id.tv_3);
        layout_3 = findView(R.id.layout_3);

        tv_1.setText("400-0896-2342");
        tv_2.setText("ahqg_adn@163.cn");

        tv_version.setText("v" + ConfigInfoUtils.getLocalVersionName(this) + "");


        layout_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastVersion();

            }
        });
    }


    UpgradeDialog upgradeDialog;


    private void getLastVersion(){
        DialogMaker.showProgressDialog(this, "检测中...");
        HttpClient.getLastVersion(new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                VersionBean versionBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), VersionBean.class);
                if (versionBean.getVersionCode() > BuildConfig.VERSION_CODE){
                    upgradeDialog = new UpgradeDialog(AboutUsActivity.this, R.style.tipDialog2);
                    upgradeDialog.setOnDownloadListener(AboutUsActivity.this);
                    String url = versionBean.getDownloadUrl().startsWith("http://") || versionBean.getDownloadUrl().startsWith("https://") ?  versionBean.getDownloadUrl() : "http://"+versionBean.getDownloadUrl();
                    upgradeDialog.init(url, versionBean.getVersionName(),false, versionBean.getUpdateContent());
                    upgradeDialog.show();
                }else {
                    ToastHelper.showToast(AboutUsActivity.this, "已是最新版本");
                }
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(AboutUsActivity.this, message);

            }

            @Override
            public void onComplete() {
                DialogMaker.dismissProgressDialog();
            }
        },1 );
    }


    @Override
    public void onDownloadSuccess(String filePath) {
        installApk(this , filePath);
    }

    @Override
    public void onDownloading(long sum, long total) {

    }

    @Override
    public void onDownloadFailed() {
        ToastHelper.showToast(this, "下载失败，请稍后重试");
        //upgradeDialog.dismiss();
    }


    public void installApk(Context context, String apkPath) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }

        if(Build.VERSION.SDK_INT>=24) {//判读版本是否在7.0以上
            File file= new File(apkPath);
            Uri apkUri = FileProvider.getUriForFile(context, "com.qingeng.fjjdoctor.fileprovider", file);//在AndroidManifest中的android:authorities值
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(install);
        } else{
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
        //upgradeDialog.dismiss();
    }

}
