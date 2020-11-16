package com.qingeng.fjjdoctor.zoom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
import com.qingeng.apilibrary.bean.ImageBean;
import com.qingeng.apilibrary.bean.PublishBean;
import com.qingeng.apilibrary.bean.UpayResponseBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.PublishImageAdapter;
import com.qingeng.fjjdoctor.util.NetWorkUtils;


import java.util.ArrayList;


public class SendPublishActivity extends UI implements HttpInterface, PublishImageAdapter.Listener {

    public final static String PUBLISH_DATA = "PUBLISH_DATA";


    private EditText edit_content;
    private EditText edit_link;
    private RecyclerView rcv_image;
    private Button btn_send;
    private String content = "";
    private String link = "";

    private PublishImageAdapter publishImageAdapter;
    private ArrayList<ImageBean> imageBeans = new ArrayList<>();
    private ArrayList<GLImage> selectedImages = new ArrayList<>();
    private ImageBean imageBeanDef;


    private PublishBean oldPublishBean;
    private ArrayList<ImageBean> oldImageList = new ArrayList<>();
    private UpayResponseBean upayResponseBean;


    public static void start(Context context) {
        Intent intent = new Intent(context, SendPublishActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void start(Context context, PublishBean publishBean) {
        Intent intent = new Intent(context, SendPublishActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(PUBLISH_DATA, publishBean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_publish);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "发布";
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

        oldPublishBean = (PublishBean) getIntent().getSerializableExtra(PUBLISH_DATA);
        // 界面初始化
        edit_content = findViewById(R.id.edit_content);
        edit_link = findViewById(R.id.edit_link);
        btn_send = findViewById(R.id.btn_send);
        rcv_image = findViewById(R.id.rcv_image);

        publishImageAdapter = new PublishImageAdapter(this);
        publishImageAdapter.setListener(this);
        publishImageAdapter.setCanDelete(true);
        //初始化标签
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rcv_image.setLayoutManager(gridLayoutManager);
        rcv_image.setAdapter(publishImageAdapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = edit_content.getText().toString().trim();
                link = edit_link.getText().toString().trim();
                if (content.isEmpty()) {
                    ToastHelper.showToast(SendPublishActivity.this, "请输入要发布的内容");
                    return;
                }
                if (oldPublishBean != null) {
                    updateMyNew();
                } else {
                    send();
                }
            }
        });


        if (oldPublishBean != null) {
            showPublishData();
        }


        addDefImage();
        publishImageAdapter.setImageBeans(imageBeans);
        publishImageAdapter.notifyDataSetChanged();

    }

    private void showPublishData() {
        if (oldPublishBean != null) {
            edit_content.setText(oldPublishBean.getTitle());
            edit_link.setText(oldPublishBean.getUrl());
            oldImageList = (ArrayList<ImageBean>) oldPublishBean.getImages();
            for (int i = 0; i < oldImageList.size(); i++) {
                ImageBean imageBean = new ImageBean();
                imageBean.setUrl(oldImageList.get(i).getUrl());
                imageBean.setImage(oldImageList.get(i).getImage());
                imageBeans.add(imageBean);
            }
        }

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
            case RequestCommandCode.PUBLISH_NEWS:
                upayResponseBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                if (!TextUtils.isEmpty(upayResponseBean.getPAY_STATUS()) && upayResponseBean.getPAY_STATUS().equals("SUCCESS")){
                    sendSuccess("发布成功");
                }else {
                    goUPay();
                }
                break;
            case RequestCommandCode.UPDATE_MY_NEW:
                ToastHelper.showToast(this, "修改成功");
                content = "";
                this.finish();
                break;
            case RequestCommandCode.UPAY_QUERY_ORDER_PAY_STATUS:

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
        imageBeans.clear();
        selectedImages.clear();
        for (int i = 0; i < oldImageList.size(); i++) {
            ImageBean imageBean = new ImageBean();
            imageBean.setUrl(oldImageList.get(i).getUrl());
            imageBean.setImage(oldImageList.get(i).getImage());
            imageBeans.add(imageBean);
        }

        for (int i = 0; i < images.size(); i++) {
            GLImage image = images.get(i);
            ImageBean imageBean = new ImageBean();
            imageBean.setImage(image.getPath());
            imageBeans.add(imageBean);
            selectedImages.add(image);

        }
        addDefImage();
        publishImageAdapter.notifyDataSetChanged();
    }

    private void queryOrderPayStatus() {
        DialogMaker.showProgressDialog(this, "获取订单状态...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("requestId", upayResponseBean.getData().getRequestId());
        HttpClient.queryOrderPayStatus(baseRequestBean, SendPublishActivity.this, RequestCommandCode.UPAY_QUERY_ORDER_PAY_STATUS);
    }


    private void send() {
        DialogMaker.showProgressDialog(this, "发布中...");
        String clientIp = NetWorkUtils.getIPAddress(this);
        HttpClient.publishNews(link, "", "", content, imageBeans, clientIp, SendPublishActivity.this, RequestCommandCode.PUBLISH_NEWS);
    }

    private void updateMyNew() {
        ArrayList<ImageBean> sendImageBeans = new ArrayList<>();
        for (int i = 0; i < selectedImages.size(); i++) {
            ImageBean imageBean = new ImageBean();
            imageBean.setImage(selectedImages.get(i).getPath());
            sendImageBeans.add(imageBean);
        }
        DialogMaker.showProgressDialog(this, "修改中...");
        HttpClient.updateMyNew(oldPublishBean.getId(), link, "", "", content, JSON.toJSONString(oldImageList), sendImageBeans, SendPublishActivity.this, RequestCommandCode.UPDATE_MY_NEW);
    }

    @Override
    public void onImageItemClick(int position1, ArrayList<ImageBean> imageBeans, ImageBean imageBean, PublishBean publishBean) {
        if (imageBean.getIconId() == R.mipmap.btn_add) {
            ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                    ImagePickerOption.PickType.Image).setMultiMode(true).setSelectMax(9 - oldImageList.size()).setSelectedImages(selectedImages);
            ImagePickerLauncher.selectImage(SendPublishActivity.this, 10001, option);
        } else {
            int position = -1;
            for (int i = 0; i < selectedImages.size(); i++) {
                if (selectedImages.get(i).getPath().equals(imageBean.getImage())) {
                    position = i;
                    break;
                }
            }
            if (position == -1) return;
            ImagePickerOption option = DefaultImagePickerOption.getInstance().setShowCamera(true).setPickType(
                    ImagePickerOption.PickType.Image).setMultiMode(true).setSelectMax(9 - oldImageList.size()).setSelectedImages(selectedImages).setShowPrePosition(position);
            ImagePickerLauncher.selectImage(SendPublishActivity.this, 10001, option);

        }
    }

    @Override
    public void imageDelete(ImageBean imageBean) {
        imageBeans.remove(imageBean);
        publishImageAdapter.notifyDataSetChanged();
        for (int i = 0; i < selectedImages.size(); i++) {
            if (selectedImages.get(i).getPath().equals(imageBean.getImage())) {
                selectedImages.remove(i);
                break;
            }
        }
        for (int i = 0; i < oldImageList.size(); i++) {
            if (oldImageList.get(i).getImage().equals(imageBean.getImage())) {
                oldImageList.remove(i);
                break;
            }
        }
        addDefImage();
    }

    private void addDefImage() {
        if (imageBeans.size() < 9) {
            if (imageBeanDef == null) {
                imageBeanDef = new ImageBean();
                imageBeanDef.setIconId(R.mipmap.btn_add);
            }
            if (!imageBeans.contains(imageBeanDef)) {
                imageBeans.add(imageBeanDef);
            }
        }
    }


    void goUPay() {
         }

    private void sendSuccess(String message) {
        ToastHelper.showToast(this, message);
        content = "";
        this.finish();
    }

}

