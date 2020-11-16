package com.qingeng.fjjdoctor.user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.media.imagepicker.Constants;
import com.netease.nim.uikit.common.media.imagepicker.ImagePickerLauncher;
import com.netease.nim.uikit.common.media.model.GLImage;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.dialog.EasyEditDialog;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.AbortableFuture;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.UserInfoBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.contact.activity.UserProfileSettingActivity;
import com.qingeng.fjjdoctor.main.model.Extras;
import com.qingeng.fjjdoctor.setting.ChangePhoneActivity;
import com.qingeng.fjjdoctor.util.LocalDataUtils;

import java.io.File;
import java.util.ArrayList;


public class MyInfoActivity extends UI implements View.OnClickListener, HttpInterface {
    private final String TAG = UserProfileSettingActivity.class.getSimpleName();

    // constant
    private static final int PICK_AVATAR_REQUEST = 0x0E;
    private static final int AVATAR_TIME_OUT = 30000;

    private String account;
    private UserInfoBean userBean;

    // view

    private RelativeLayout layout_user_head_pic;
    private RelativeLayout layout_user_name;
    private RelativeLayout layout_user_code;
    private RelativeLayout layout_user_phone;
    private RelativeLayout layout_user_id;

//    private RelativeLayout signatureLayout;

    private HeadImageView user_photo;
    private TextView tv_user_name;
    private TextView tv_user_phone;
    private TextView tv_user_id;

//    private TextView phoneText;
//    private TextView emailText;
//    private TextView signatureText;

    // data
    AbortableFuture<String> uploadAvatarFuture;

    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, MyInfoActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.user_information;
        setToolBar(R.id.toolbar, options);

        account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void findViews() {
        user_photo = findViewById(R.id.user_photo);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_phone = findViewById(R.id.tv_user_phone);
        tv_user_id = findViewById(R.id.tv_user_id);

        layout_user_head_pic = findViewById(R.id.layout_user_head_pic);
        layout_user_name = findViewById(R.id.layout_user_name);
        layout_user_code = findViewById(R.id.layout_user_code);
        layout_user_phone = findViewById(R.id.layout_user_phone);
        layout_user_id = findViewById(R.id.layout_user_id);

        layout_user_head_pic.setOnClickListener(this);
        layout_user_name.setOnClickListener(this);
        layout_user_code.setOnClickListener(this);
        layout_user_phone.setOnClickListener(this);
        layout_user_id.setOnClickListener(this);
    }

    private void getUserInfo() {
        updateUI();
        HttpClient.getDoctorInfo(new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                LocalDataUtils.saveLocalDoctor(baseResponseData.getData());
                updateUI();
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(MyInfoActivity.this, "更新用户信息失败 " + message);
            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.LOGIN_USER_INFO);
    }

    private void updateUI() {
        userBean = LocalDataUtils.getUserInfo();
        if (userBean != null && tv_user_name != null) {
            tv_user_name.setText(userBean.getNickName());
            tv_user_id.setText(userBean.getUserId());
            user_photo.loadImgForUrl(userBean.getAvatar());
            tv_user_phone.setText(userBean.getPhonenumber());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_user_head_pic:
                ImagePickerLauncher.pickImage(MyInfoActivity.this, PICK_AVATAR_REQUEST, R.string.set_head_image);
                break;
           case R.id.layout_user_name:
               inputData(1,"输入昵称");
                break;
            case R.id.layout_user_phone:
                ChangePhoneActivity.start(this);
                break;
            case R.id.layout_user_id:
 /*               if (userBean.getIdLock() == 1){
                    ToastHelper.showToast(this, "ID号只能修改一次");
                    return;
                }*/
                inputData(3,"输入ID号");
                break;
             /*case R.id.gender_layout:
                UserProfileEditItemActivity.startActivity(MyInfoActivity.this, UserConstant.KEY_GENDER,
                        String.valueOf(userInfo.getGenderEnum().getValue()));
                break;
            case R.id.birth_layout:
                UserProfileEditItemActivity.startActivity(MyInfoActivity.this, UserConstant.KEY_BIRTH,
                        birthText.getText().toString());
                break;

                break;
            case R.id.email_layout:
                UserProfileEditItemActivity.startActivity(MyInfoActivity.this, UserConstant.KEY_EMAIL,
                        emailText.getText().toString());
                break;
            case R.id.signature_layout:
                UserProfileEditItemActivity.startActivity(MyInfoActivity.this, UserConstant.KEY_SIGNATURE,
                        signatureText.getText().toString());
                break;*/
            case R.id.layout_user_code:
                MyCodeActivity.start(MyInfoActivity.this, AppPreferences.getAccId());
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
        HttpClient.uploadHeadPic(file, this, RequestCommandCode.UPLOAD_HEAD_PIC);
    }


    private void cancelUpload(int resId) {
        if (uploadAvatarFuture != null) {
            uploadAvatarFuture.abort();
            ToastHelper.showToast(MyInfoActivity.this, resId);
            onUpdateDone();
        }
    }

    private Runnable outimeTask = new Runnable() {
        @Override
        public void run() {
            cancelUpload(R.string.user_info_update_failed);
        }
    };

    private void onUpdateDone() {
        uploadAvatarFuture = null;
        DialogMaker.dismissProgressDialog();
        getUserInfo();
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.UPLOAD_HEAD_PIC:
                JSONObject data = (JSONObject) JSON.toJSON(baseResponseData.getData());
                String headPicUrl = data.getString("url");
                HttpClient.updateHeadImage(headPicUrl, this, RequestCommandCode.UPDATE_HEAD_IMAGE);
                break;
            case RequestCommandCode.UPDATE_HEAD_IMAGE:
            case RequestCommandCode.MODIFY_NICK_NAME:
            case RequestCommandCode.CHANGE_USER_ID:
                ToastHelper.showToast(this, "设置成功");
                getUserInfo();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, "失败 "+message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }

    /**
     * 通过验证方式添加好友
     */
    private void inputData(int type,String title) {
        final EasyEditDialog requestDialog = new EasyEditDialog(this);
        requestDialog.setEditTextMaxLength(32);
        requestDialog.setTitle(title);
        requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
            }
        });
        requestDialog.addPositiveButtonListener(R.string.set_ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = requestDialog.getEditMessage();
                if(type == 3){
                    if(checkIDString(msg)){
                        EasyAlertDialogHelper.createOkCancelDiolag(MyInfoActivity.this, null, "ID只能设置一次，确定使用此ID吗？", true,
                                new EasyAlertDialogHelper.OnDialogActionListener() {
                                    @Override
                                    public void doCancelAction() {

                                    }

                                    @Override
                                    public void doOkAction() {
                                        updateInfo(type,msg);
                                        requestDialog.dismiss();
                                    }
                                }).show();


                    }else {
                        ToastHelper.showToast(MyInfoActivity.this,"字母开头+数字 最低6位数的ID号");
                    }
                }else {
                    updateInfo(type,msg);
                    requestDialog.dismiss();
                }

            }
        });
        requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        requestDialog.show();
    }

    private void updateInfo(int type,String data){
        if (type == 1){
            HttpClient.modifyNickname(data,this,RequestCommandCode.MODIFY_NICK_NAME);
        }
        if (type == 3){
            HttpClient.changeUserId(data,this,RequestCommandCode.CHANGE_USER_ID);
        }
        if (type == 2){
            HttpClient.changeUserId(data,this,RequestCommandCode.CHANGE_USER_ID);
        }
    }


    private static boolean checkIDString(String idStr) {
        return idStr.matches("^[a-zA-Z]+[0-9]+$") && idStr.length() > 5 && idStr.length() < 13;
    }


}

