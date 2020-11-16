package com.qingeng.fjjdoctor.session.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.apilibrary.bean.ApplyBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.TargetUserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.main.model.Extras;

/**
 * Created by hzxuwen on 2015/10/13.
 */
public class FriendApplyInfoActivity extends UI implements HttpInterface {

    private static final String TAG = FriendApplyInfoActivity.class.getSimpleName();


    private TargetUserBean targetUserBean;

    // 基本信息
    private HeadImageView user_photo;
    private TextView tv_user_name;
    private TextView tv_user_des;

    private Button reject;
    private Button agree;

    private RelativeLayout layout_add_black_list;
    private RelativeLayout layout_remove_black_list;

    private ApplyBean applyBean;


    public static void start(Context context, ApplyBean applyBean) {
        Intent intent = new Intent();
        intent.setClass(context, FriendApplyInfoActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, applyBean);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_apply_info_activity);
        if (getIntent().getSerializableExtra(Extras.EXTRA_ACCOUNT) != null) {
            applyBean = (ApplyBean) getIntent().getSerializableExtra(Extras.EXTRA_ACCOUNT);
        }

        if (applyBean == null) {
            ToastHelper.showToast(FriendApplyInfoActivity.this, "传入的申请数据为空");
            finish();
            return;
        }

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.user_profile;
        setToolBar(R.id.toolbar, options);
        findViews();
        getUserData();
    }

    private void getUserData() {
        HttpClient.getOtherUserInfo(applyBean.getUserId()+"", this, RequestCommandCode.GET_OTHER_USER_INFO);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void findViews() {

        // 基本信息
        user_photo = findViewById(R.id.user_photo);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_des = findViewById(R.id.tv_user_des);
        reject = findViewById(R.id.reject);
        agree = findViewById(R.id.agree);

        layout_add_black_list = findViewById(R.id.layout_add_black_list);
        layout_remove_black_list = findViewById(R.id.layout_remove_black_list);

        layout_add_black_list.setOnClickListener(onClickListener);
        layout_remove_black_list.setOnClickListener(onClickListener);
        reject.setOnClickListener(onClickListener);
        agree.setOnClickListener(onClickListener);
    }


    private void updateUserInfoView() {
        // 基本信息
        user_photo.loadImgForUrl(targetUserBean.getHeadImage());
        tv_user_name.setText(TextUtils.isEmpty(targetUserBean.getAlias()) ? targetUserBean.getTargetUsername() : targetUserBean.getAlias());
        tv_user_des.setText("ID：" + targetUserBean.getUniqueId());

        resetBtnShow();
        if (targetUserBean.getRelationShip() == 2) {
            layout_add_black_list.setVisibility(View.VISIBLE);
        } else if (targetUserBean.getRelationShip() == 3) {
            layout_add_black_list.setVisibility(View.VISIBLE);
        } else if (targetUserBean.getRelationShip() == 4 || targetUserBean.getRelationShip() == -1) {
            layout_add_black_list.setVisibility(View.VISIBLE);
        } else if (targetUserBean.getRelationShip() == 5) {
            layout_remove_black_list.setVisibility(View.VISIBLE);
        }


    }

    private void resetBtnShow() {
        layout_add_black_list.setVisibility(View.GONE);
        layout_remove_black_list.setVisibility(View.GONE);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == layout_add_black_list) {
                EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(FriendApplyInfoActivity.this, getString(R.string.remove_friend),
                        getString(R.string.remove_friend_tip), true,
                        new EasyAlertDialogHelper.OnDialogActionListener() {

                            @Override
                            public void doCancelAction() {

                            }

                            @Override
                            public void doOkAction() {
                                DialogMaker.showProgressDialog(FriendApplyInfoActivity.this, "处理中...", true);
                                onAddBlackList(1);
                            }
                        });
                if (!isFinishing() && !isDestroyedCompatible()) {
                    dialog.show();
                }

            } else if (v == layout_remove_black_list) {
                DialogMaker.showProgressDialog(FriendApplyInfoActivity.this, "处理中...", true);
                onAddBlackList(2);
            }else if (v == agree) {
                onAgree(applyBean);
            }else if (v == reject) {
                DialogMaker.showProgressDialog(FriendApplyInfoActivity.this, "处理中...", true);
                onReject(applyBean);
            }
        }
    };


    private void onAddBlackList(int type) {
        HttpClient.updateBlackList(targetUserBean.getTargetUserId() + "", type, this, RequestCommandCode.UPDATE_BLACK);
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_OTHER_USER_INFO:
                targetUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                System.out.println("onSuccess---" + JSON.toJSONString(targetUserBean));
                updateUserInfoView();
                break;
            case RequestCommandCode.HANDLE_FRIEND_APPLY:
            case RequestCommandCode.REMOVE_FRIEND_ADD_LIST:
                ToastHelper.showToast(this, "操作成功");
                finish();
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

    public void onAgree(ApplyBean message) {
        HttpClient.handleFriendApply(message.getId() + "", "3", this, RequestCommandCode.HANDLE_FRIEND_APPLY);
    }

    public void onReject(ApplyBean message) {
        HttpClient.handleFriendApply(message.getId() + "", "4", this, RequestCommandCode.HANDLE_FRIEND_APPLY);
    }


}
