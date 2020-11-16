package com.qingeng.fjjdoctor.contact.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.TargetUserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.DemoCache;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.contact.constant.UserConstant;
import com.qingeng.fjjdoctor.main.model.Extras;
import com.qingeng.fjjdoctor.session.SessionHelper;
import com.qingeng.fjjdoctor.widget.SendCardDialog;

import java.util.ArrayList;

/**
 * 用户资料页面
 * Created by huangjun on 2015/8/11.
 */
public class UserProfileActivity2 extends UI implements HttpInterface {

    private static final String TAG = UserProfileActivity2.class.getSimpleName();

    private static final int REQUEST_CODE_ADVANCED = 2;


    private final boolean FLAG_ADD_FRIEND_DIRECTLY = false; // 是否直接加为好友开关，false为需要好友申请
    private final String KEY_BLACK_LIST = "black_list";
    private final String KEY_MSG_NOTICE = "msg_notice";
    private final String KEY_RECENT_STICKY = "recent_contacts_sticky";

    private String account;
    private TargetUserBean targetUserBean;

    // 基本信息
    private HeadImageView user_photo;
    private TextView tv_user_name;
    private TextView tv_user_des;
    private RelativeLayout layout_memo_name;

    private Button btn_remove_buddy;

    private RelativeLayout layout_add_black_list;
    private RelativeLayout layout_recommend;
    private RelativeLayout layout_send_message;
    private RelativeLayout layout_add_buddy;
    private RelativeLayout layout_remove_black_list;

    private RelativeLayout layout_apply_agree;
    private RelativeLayout layout_apply_reject;

    private TextView tv_black_list_status_desc;





    private String accountType = "";


    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileActivity2.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void start2(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileActivity2.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT_ACCID, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity2);
        if (getIntent().getStringExtra(Extras.EXTRA_ACCOUNT) !=null){
            account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
            accountType = Extras.EXTRA_ACCOUNT;
        }
        if (getIntent().getStringExtra(Extras.EXTRA_ACCOUNT_ACCID) !=null){
            account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT_ACCID);
            accountType = Extras.EXTRA_ACCOUNT_ACCID;
        }

        if (TextUtils.isEmpty(account)) {
            ToastHelper.showToast(UserProfileActivity2.this, "传入的帐号为空");
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
        if (accountType == Extras.EXTRA_ACCOUNT){
            HttpClient.getOtherUserInfo(account, this, RequestCommandCode.GET_OTHER_USER_INFO);
        }
        if (accountType == Extras.EXTRA_ACCOUNT_ACCID){
            HttpClient.userInfoByAccId(account, this, RequestCommandCode.GET_OTHER_USER_INFO);
        }
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
        layout_memo_name = findViewById(R.id.layout_memo_name);

        layout_send_message = findViewById(R.id.layout_send_message);
        btn_remove_buddy = findViewById(R.id.btn_remove_buddy);
        layout_add_buddy = findViewById(R.id.layout_add_buddy);
        layout_add_black_list = findViewById(R.id.layout_add_black_list);
        layout_recommend = findViewById(R.id.layout_recommend);

        layout_apply_agree = findViewById(R.id.layout_apply_agree);
        layout_apply_reject = findViewById(R.id.layout_apply_reject);
        layout_remove_black_list = findViewById(R.id.layout_remove_black_list);

        tv_black_list_status_desc = findViewById(R.id.tv_black_list_status_desc);


        layout_send_message.setOnClickListener(onClickListener);
        btn_remove_buddy.setOnClickListener(onClickListener);
        layout_add_buddy.setOnClickListener(onClickListener);
        layout_add_black_list.setOnClickListener(onClickListener);
        layout_recommend.setOnClickListener(onClickListener);
        layout_apply_agree.setOnClickListener(onClickListener);
        layout_apply_reject.setOnClickListener(onClickListener);
        layout_remove_black_list.setOnClickListener(onClickListener);

        layout_memo_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileEditItemActivity.startActivity(UserProfileActivity2.this, UserConstant.KEY_ALIAS, account,targetUserBean.getTargetUserId()+"");
            }
        });

    }


    private void updateUserInfoView() {
        // 基本信息
        user_photo.loadImgForUrl(targetUserBean.getHeadImage());
        tv_user_name.setText(TextUtils.isEmpty(targetUserBean.getAlias())?targetUserBean.getTargetUsername():targetUserBean.getAlias());
        tv_user_des.setText("ID："+targetUserBean.getUniqueId());


        //relationShip 2 请求待处理 《添加好友，同意 378EF1  、拒绝 A1A1A1  拉黑  FF4949》
        //relationShip 3 好友 《设置备注 发消息 删除好友 F4F4F4 拉黑》
        //relationShip 4 陌生人 《添加好友， 拉黑  FF4949》
        //relationShip 5 黑名单 《移出黑名单 F4F4F4》

        resetBtnShow();
        if (targetUserBean.getRelationShip() == 2){
            layout_add_buddy.setVisibility(View.VISIBLE);
            layout_add_black_list.setVisibility(View.VISIBLE);
            layout_apply_agree.setVisibility(View.GONE);
            layout_apply_reject.setVisibility(View.GONE);
        }else if(targetUserBean.getRelationShip() == 3){
            layout_memo_name.setVisibility(View.VISIBLE);
            layout_send_message.setVisibility(View.VISIBLE);
            btn_remove_buddy.setVisibility(View.VISIBLE);
            layout_add_black_list.setVisibility(View.VISIBLE);
        }else if(targetUserBean.getRelationShip() == 4 || targetUserBean.getRelationShip() == -1 ){
            layout_add_buddy.setVisibility(View.VISIBLE);
            layout_add_black_list.setVisibility(View.VISIBLE);
        }else if(targetUserBean.getRelationShip() == 5){
            layout_remove_black_list.setVisibility(View.VISIBLE);
            tv_black_list_status_desc.setVisibility(View.VISIBLE);
        }


    }

    private void resetBtnShow(){
        layout_send_message.setVisibility(View.GONE);
        btn_remove_buddy.setVisibility(View.GONE);
        layout_add_buddy.setVisibility(View.GONE);
        layout_add_black_list.setVisibility(View.GONE);
        layout_apply_agree.setVisibility(View.GONE);
        layout_apply_reject.setVisibility(View.GONE);
        layout_add_black_list.setVisibility(View.GONE);
        layout_remove_black_list.setVisibility(View.GONE);
        tv_black_list_status_desc.setVisibility(View.GONE);
        layout_memo_name.setVisibility(View.GONE);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == layout_add_buddy) {
                onAddFriendByVerify(); // 发起好友验证请求
            } else if (v == btn_remove_buddy) {
                onRemoveFriend();
            } else if (v == layout_send_message) {
                onChat();
            }else if (v == layout_apply_agree) {
                onHandleApply("3");
            }else if (v == layout_apply_reject) {
                onHandleApply("4");
            }else if (v == layout_add_black_list) {
                EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(UserProfileActivity2.this, getString(R.string.remove_friend),
                        getString(R.string.remove_friend_tip), true,
                        new EasyAlertDialogHelper.OnDialogActionListener() {

                            @Override
                            public void doCancelAction() {

                            }
                            @Override
                            public void doOkAction() {
                                DialogMaker.showProgressDialog(UserProfileActivity2.this, "处理中...", true);
                                onAddBlackList(1);
                            }
                        });
                if (!isFinishing() && !isDestroyedCompatible()) {
                    dialog.show();
                }

            }else if (v == layout_remove_black_list) {
                DialogMaker.showProgressDialog(UserProfileActivity2.this, "处理中...", true);
                onAddBlackList(2);
            } else if (v == layout_recommend) {

                ContactSelectActivity.Option advancedOption = TeamHelper
                        .getCreateContactSelectOption(null, 1);
                NimUIKit.startContactSelector(UserProfileActivity2.this, advancedOption,
                        REQUEST_CODE_ADVANCED);
            }
        }
    };

    private void onHandleApply(String type) {
//        HttpClient.handleFriendApply(message.getId()+"","3",this,RequestCommandCode.HANDLE_FRIEND_APPLY);
    }

    private void onAddBlackList(int type) {
        HttpClient.updateBlackList(targetUserBean.getTargetUserId()+"",type,this,RequestCommandCode.UPDATE_BLACK);
    }

    /**
     * 通过验证方式添加好友
     */
    private void onAddFriendByVerify() {
        if (!TextUtils.isEmpty(account) && account.equals(DemoCache.getAccount())) {
            ToastHelper.showToast(UserProfileActivity2.this, "不能加自己为好友");
            return;
        }

       /* final EasyEditDialog requestDialog = new EasyEditDialog(this);
        requestDialog.setEditTextMaxLength(32);
        requestDialog.setTitle(getString(R.string.add_friend_verify_tip));
        requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
            }
        });
        requestDialog.addPositiveButtonListener(R.string.send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
                String msg = requestDialog.getEditMessage();
                doAddFriend(msg, false);
            }
        });
        requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        requestDialog.show();*/

        AddFriendActivity2.start(this, targetUserBean.getTargetUserId()+"");
    }

    private void doAddFriend(String msg, boolean addDirectly) {
        if (!NetworkUtil.isNetAvailable(this)) {
            ToastHelper.showToast(UserProfileActivity2.this, R.string.network_is_not_available);
            return;
        }
        DialogMaker.showProgressDialog(this, "", true);
        HttpClient.addFriends(targetUserBean.getTargetUserId() + "", "加我吧", this, RequestCommandCode.ADD_FRIENDS);
        Log.i(TAG, "onAddFriendByVerify");
    }

    private void onRemoveFriend() {
        Log.i(TAG, "onRemoveFriend");
        if (!NetworkUtil.isNetAvailable(this)) {
            ToastHelper.showToast(UserProfileActivity2.this, R.string.network_is_not_available);
            return;
        }
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, getString(R.string.remove_friend),
                getString(R.string.remove_friend_tip), true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }
                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(UserProfileActivity2.this, "", true);
                        HttpClient.delFriends(targetUserBean.getTargetUserId() + "", UserProfileActivity2.this, RequestCommandCode.DEL_FRIENDS);
                    }
                });
        if (!isFinishing() && !isDestroyedCompatible()) {
            dialog.show();
        }
    }

    private void onChat() {
        Log.i(TAG, "onChat");
        SessionHelper.startP2PSession(this, targetUserBean.getTargetUserAccid());
    }




    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_OTHER_USER_INFO:
                targetUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                System.out.println("onSuccess---"+JSON.toJSONString(targetUserBean));
                updateUserInfoView();

                break;
            case RequestCommandCode.ADD_FRIENDS:
                ToastHelper.showToast(this, "请求已发送");
                break;
            case RequestCommandCode.DEL_FRIENDS:
                ToastHelper.showToast(this, "删除成功");
                getUserData();
            case RequestCommandCode.UPDATE_BLACK:
            case RequestCommandCode.FORBIDDEN_USER:
                ToastHelper.showToast(this, "操作成功");
                getUserData();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {

    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_ADVANCED) {
            final ArrayList<String> selected = data.getStringArrayListExtra(
                    ContactSelectActivity.RESULT_DATA);
            HttpClient.userInfoByAccId(selected.get(0), new HttpInterface() {
                @Override
                public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                    TargetUserBean toUser   = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                    SendCardDialog sendCardDialog = new SendCardDialog(UserProfileActivity2.this);
                    sendCardDialog.init(true, targetUserBean, toUser);
                    sendCardDialog.show();
                }

                @Override
                public void onFailure(int requestCode, String message) {
                    ToastHelper.showToast(UserProfileActivity2.this, message);
                }

                @Override
                public void onComplete() {

                }
            }, RequestCommandCode.QUERY_MY_FRIENDS);
        }
    }
}
