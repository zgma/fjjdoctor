package com.qingeng.fjjdoctor.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.leon.lib.settingview.LSettingItem;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.media.imagepicker.Constants;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.model.GLImage;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.lucene.LuceneService;
import com.netease.nimlib.sdk.misc.DirCacheFileType;
import com.netease.nimlib.sdk.misc.MiscService;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.DoctorInfoBean;
import com.qingeng.apilibrary.bean.DoctorInfoResultBean;
import com.qingeng.apilibrary.bean.UserInfoBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.enums.EditEnum;
import com.qingeng.fjjdoctor.login.ResetPasswordActivity;
import com.qingeng.fjjdoctor.main.activity.MainActivity;
import com.qingeng.fjjdoctor.util.ImageFrameUtils;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
import com.qingeng.fjjdoctor.login.ChangePhoneActivity;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class SettingMain2Activity extends UI implements View.OnClickListener, LSettingItem.OnLSettingItemClick, HttpInterface {

    private static final int PICK_AVATAR_REQUEST = 0x0E;


    @BindView(R.id.item_1)
    LSettingItem item_1;
    @BindView(R.id.item_2)
    LSettingItem item_2;
    @BindView(R.id.item_3)
    LSettingItem item_3;
    @BindView(R.id.item_4)
    LSettingItem item_4;
    @BindView(R.id.item_5)
    LSettingItem item_5;

    @BindView(R.id.layout_1)
    ConstraintLayout layout_1;
    @BindView(R.id.layout_5)
    ConstraintLayout layout_5;

    @BindView(R.id.user_photo)
    HeadImageView user_photo;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingMain2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_main_2);
        ButterKnife.bind(this);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "设置";
        setToolBar(R.id.toolbar, options);

        initUi();
        showUserDataToUI();
        getUserInfo();

    }

    private void getUserInfo() {
        HttpClient.getDoctorInfo(this, RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID);
    }


    private void showUserDataToUI() {
        UserInfoBean userBean = LocalDataUtils.getUserInfo();
        if (userBean != null) {
            user_photo.loadImgForUrl(userBean.getAvatar());

        }
        DoctorInfoBean doctorInfoBean = LocalDataUtils.getDoctorInfo();
        if (doctorInfoBean != null) {
            item_1.setRightText(doctorInfoBean.getRealName());
            item_2.setRightText(doctorInfoBean.getAskPrice() + "元");
            item_3.setRightText(doctorInfoBean.getRiwPrice() + "元");
        }

    }

    private void initUi() {
        item_1.setmOnLSettingItemClick(this);
        item_2.setmOnLSettingItemClick(this);
        item_3.setmOnLSettingItemClick(this);
        item_4.setmOnLSettingItemClick(this);
        item_5.setmOnLSettingItemClick(this);

        user_photo.setOnClickListener(this);
        layout_5.setOnClickListener(this);
        getSDKDirCacheSize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_photo:
                ImagePickerLauncher.pickImage(this, PICK_AVATAR_REQUEST, R.string.set_head_image);
                break;
            case R.id.layout_5:
                MainActivity.logout(this, false);
                finish();
                NIMClient.getService(AuthService.class).logout();
                break;
        }
    }

    private void clearSDKDirCache() {
        NIMClient.getService(LuceneService.class).clearCache();
        List<DirCacheFileType> types = new ArrayList<>();
        types.add(DirCacheFileType.AUDIO);
        types.add(DirCacheFileType.THUMB);
        types.add(DirCacheFileType.IMAGE);
        types.add(DirCacheFileType.VIDEO);
        types.add(DirCacheFileType.OTHER);
        NIMClient.getService(MiscService.class).clearDirCache(types, 0, 0).setCallback(
                new RequestCallbackWrapper<Void>() {

                    @Override
                    public void onResult(int code, Void result, Throwable exception) {
//                        tv_cache.setText("0.00 M");
                    }
                });
    }

    private void getSDKDirCacheSize() {
        List<DirCacheFileType> types = new ArrayList<>();
        types.add(DirCacheFileType.AUDIO);
        types.add(DirCacheFileType.THUMB);
        types.add(DirCacheFileType.IMAGE);
        types.add(DirCacheFileType.VIDEO);
        types.add(DirCacheFileType.OTHER);
        long size = NIMClient.getService(LuceneService.class).getCacheSize();
        NIMClient.getService(MiscService.class).getSizeOfDirCache(types, 0, 0).setCallback(
                new RequestCallbackWrapper<Long>() {

                    @Override
                    public void onResult(int code, Long result, Throwable exception) {
                        Long total = size + result;
//                        tv_cache.setText(String.format("%.2f M", total / (1024.0f * 1024.0f)));
                    }
                });
    }

    @Override
    public void click(int id, boolean isChecked) {
        DoctorInfoBean doctorInfoBean = LocalDataUtils.getDoctorInfo();
        switch (id) {
            case R.id.item_1:
                if (doctorInfoBean != null) {
                    EditUserInfoActivity.start(this, EditEnum.DOCTOR_REAL_NAME, doctorInfoBean.getRealName());
                } else {
                    ToastHelper.showToast(this, "信息获取失败，请重新进入页面");
                }
                break;
            case R.id.item_2:
                if (doctorInfoBean != null) {
                    EditUserInfoActivity.start(this, EditEnum.DOCTOR_ASK_PRICE, doctorInfoBean.getAskPrice() + "");
                } else {
                    ToastHelper.showToast(this, "信息获取失败，请重新进入页面");
                }
                break;
            case R.id.item_3:
                if (doctorInfoBean != null) {
                    EditUserInfoActivity.start(this, EditEnum.DOCTOR_RIW_PRICE, doctorInfoBean.getRiwPrice() + "");
                } else {
                    ToastHelper.showToast(this, "信息获取失败，请重新进入页面");
                }
                break;
            case R.id.item_4:
                ResetPasswordActivity.start(this, "修改密码");
                break;
            case R.id.item_5:
                ChangePhoneActivity.start(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_AVATAR_REQUEST) {
            onPicked(data);
        }
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
        File file = new File(image.getPath());
        System.out.println("image.getPath()--file size--" + file.getTotalSpace());

        DialogMaker.showProgressDialog(this, getString(R.string.uploading_pic), false);

        ImageFrameUtils.showImageToView(user_photo, image.getPath());
        DialogMaker.showProgressDialog(this, "上传头像...");
        HttpClient.uploadFile(new File(image.getPath()), this, RequestCommandCode.FILE_UPLOAD);
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID:
                LocalDataUtils.saveLocalDoctor(baseResponseData.getData());
                showUserDataToUI();
                break;
            case RequestCommandCode.FILE_UPLOAD:
                String url = baseResponseData.getUrl();
                BaseRequestBean baseRequestBean = new BaseRequestBean();
                baseRequestBean.addParams("avatar", url);
                HttpClient.updateUserInfo(baseRequestBean, this, RequestCommandCode.UPDATE_ALIAS);
                break;
            case RequestCommandCode.UPDATE_ALIAS:
                ToastHelper.showToast(this, "操作成功");
                getUserInfo();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }
}
