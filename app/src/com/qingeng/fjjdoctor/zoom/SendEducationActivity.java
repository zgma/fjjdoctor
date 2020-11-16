package com.qingeng.fjjdoctor.zoom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.media.imagepicker.Constants;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.imagepicker.option.DefaultImagePickerOption;
import com.netease.nim.uikit.common.media.imagepicker.option.ImagePickerOption;
import com.netease.nim.uikit.common.media.model.GLImage;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SendEducationActivity extends UI implements HttpInterface {


    private EditText edit_input;
    private EditText edit_title;
    private Button btn_send;
    private ImageView img_file;
    File file;
    String fileUploadUrl;
    String title = "";
    String content = "";

    public static void start(Context context) {
        Intent intent = new Intent(context, SendEducationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_education);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "新建宣教";
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
        edit_input = findViewById(R.id.edit_input);
        edit_title = findViewById(R.id.edit_title);
        btn_send = findViewById(R.id.btn_send);
        img_file = findViewById(R.id.img_file);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = edit_input.getText().toString().trim();
                title = edit_title.getText().toString().trim();
                if (content.isEmpty() || title.isEmpty()) {
                    ToastHelper.showToast(SendEducationActivity.this, "请输入完整内容");
                    return;
                }
                if (file == null) {
                    ToastHelper.showToast(SendEducationActivity.this, "请添加封面图片");
                    return;
                }
                uploadFile();
            }
        });

        img_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                        ImagePickerOption.PickType.Image).setMultiMode(true).setSelectMax(1);
                ImagePickerLauncher.selectImage(SendEducationActivity.this, 10001, option);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10001:
                onPicked(data);
                break;
        }
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.ADD_SUGGEST:
                ToastHelper.showToast(this, "提交成功");
                content = "";
                edit_input.setText("");
                file = null;
                img_file.setImageResource(R.drawable.plus);
                break;
            case RequestCommandCode.FILE_UPLOAD:
                fileUploadUrl = baseResponseData.getUrl();
                send();
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

    private void onPicked(Intent data) {
        if (data == null) {
            return;
        }
        ArrayList<GLImage> images = (ArrayList<GLImage>) data.getSerializableExtra(Constants.EXTRA_RESULT_ITEMS);
        if (images == null || images.isEmpty()) {
            return;
        }
        GLImage image = images.get(0);
        System.out.println("image.getPath()----" + image.getPath());
        file = new File(image.getPath());
        System.out.println("image.getPath()--file size--" + file.getTotalSpace());

        Glide.with(this).asBitmap().load(Uri.fromFile(new File(file.getAbsolutePath())))
                .into(img_file);
    }

    private void send() {
        DialogMaker.showProgressDialog(this, "提交中...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("propContent", content);
        baseRequestBean.addParams("propCover", fileUploadUrl);
        baseRequestBean.addParams("propTitle", title);
        HttpClient.addNewPropEduction(baseRequestBean, SendEducationActivity.this, RequestCommandCode.ADD_SUGGEST);
    }

    private void uploadFile() {
        if (file != null) {
            HttpClient.uploadFile(file, this, RequestCommandCode.FILE_UPLOAD);
        }
    }
}

