package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.netease.nim.uikit.common.ui.dialog.MenuDialog;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.ReportItemBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SendFeedbackActivity2 extends UI implements HttpInterface {


    private EditText edit_input;
    private Button btn_send;
    private ImageView img_file;
    private TextView tv_type;

    private RelativeLayout layout_report;

    File file;
    String content = "";
    String headPicUrl = "";

    private List<ReportItemBean> reportItemBeans;
    private List<File> files = new ArrayList<>();
    private int type = -1;
    private String id;

    private int selectIndex = -1;


    public static void start(Context context, int type, String id) {
        Intent intent = new Intent(context, SendFeedbackActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback2);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "投诉/建议";
        setToolBar(R.id.toolbar, options);
        type = getIntent().getIntExtra("type", -1);
        id = getIntent().getStringExtra("id");
        if (type < 1 || TextUtils.isEmpty(id)) {
            ToastHelper.showToast(this, "投诉对象id获取失败");
            finish();
        }
        initUI();
        getReportItem();
    }

    private void getReportItem() {
        HttpClient.getReportItem(type, this, RequestCommandCode.GET_REPORT_ITEM);
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
        tv_type = findViewById(R.id.tv_type);
        layout_report = findViewById(R.id.layout_report);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = edit_input.getText().toString().trim();
                if (content.isEmpty()) {
                    ToastHelper.showToast(SendFeedbackActivity2.this, "请输入举报内容");
                    return;
                }
                if (selectIndex == -1){
                    ToastHelper.showToast(SendFeedbackActivity2.this, "请选择举报类型");
                    return;
                }
                send();
            }
        });

        layout_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportItemBeans != null && !reportItemBeans.isEmpty()) {
                    List<String> messages = new ArrayList<>();
                    for (ReportItemBean reportItemBean : reportItemBeans) {
                        messages.add(reportItemBean.getContent());
                    }
                    showReportMenu(messages);
                }
            }
        });

        img_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                        ImagePickerOption.PickType.Image).setMultiMode(true).setSelectMax(1);
                ImagePickerLauncher.selectImage(SendFeedbackActivity2.this, 10001, option);
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
                send();
                break;
            case RequestCommandCode.GET_REPORT_ITEM:
                reportItemBeans = JSONArray.parseArray(JSON.toJSONString(baseResponseData.getData()), ReportItemBean.class);
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
        HttpClient.chatReport(reportItemBeans.get(selectIndex).getContent(),
                reportItemBeans.get(selectIndex).getId(),
                type,
                id,
                content, files, SendFeedbackActivity2.this, RequestCommandCode.ADD_SUGGEST);
    }

    private MenuDialog reportMenu;

    private void showReportMenu(List<String> messages) {
        if (reportMenu == null) {
            reportMenu = new MenuDialog(SendFeedbackActivity2.this, messages, selectIndex, -1,
                    new MenuDialog.MenuDialogOnButtonClickListener() {
                        @Override
                        public void onButtonClick(int index, String name) {
                            reportMenu.dismiss();
                            if (name.equals(getString(com.netease.nim.uikit.R.string.cancel))) {
                                return; // 取消不处理
                            }
                            selectIndex = index;
                            tv_type.setText(reportItemBeans.get(selectIndex).getContent());
                        }
                    });
        }
        reportMenu.show();
    }
}

