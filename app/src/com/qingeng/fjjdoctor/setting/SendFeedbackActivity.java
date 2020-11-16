package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SendFeedbackActivity extends UI implements HttpInterface {


    private EditText edit_input;
    private Button btn_send;
    private ImageView img_file;
    File file;
    String content = "";
    String headPicUrl = "";
    private List<File> files = new ArrayList<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, SendFeedbackActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "投诉/建议";
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
        btn_send = findViewById(R.id.btn_send);
        img_file = findViewById(R.id.img_file);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = edit_input.getText().toString().trim();
                if (content.isEmpty()) {
                    ToastHelper.showToast(SendFeedbackActivity.this, "请输入反馈内容");
                    return;
                }
                send();
            }
        });

        img_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                        ImagePickerOption.PickType.Image).setMultiMode(true).setSelectMax(1);
                ImagePickerLauncher.selectImage(SendFeedbackActivity.this, 10001, option);
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
                files.clear();
                img_file.setImageResource(R.drawable.plus);
                break;
            case RequestCommandCode.UPLOAD_HEAD_PIC:
                JSONObject data = (JSONObject) JSON.toJSON(baseResponseData.getData());
                headPicUrl = data.getString("url");
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
        files.add(file);

        Glide.with(this).asBitmap().load(Uri.fromFile(new File(file.getAbsolutePath())))
                .into(img_file);
    }

    private void send() {
        DialogMaker.showProgressDialog(this, "提交中...");
        HttpClient.addSuggest(content,files, SendFeedbackActivity.this, RequestCommandCode.ADD_SUGGEST);
    }
}

