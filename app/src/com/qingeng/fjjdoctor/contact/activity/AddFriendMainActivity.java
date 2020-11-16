package com.qingeng.fjjdoctor.contact.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.acker.simplezxing.activity.CaptureActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.activity.AdvancedTeamUpgradeActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.bean.MyFriendBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.DemoCache;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.main.activity.ContactSearchActivity;
import com.qingeng.fjjdoctor.session.SessionHelper;
import com.qingeng.fjjdoctor.team.TeamCreateHelper;
import com.qingeng.fjjdoctor.user.MyCodeActivity;
import com.qingeng.fjjdoctor.user.MyPhoneActivity;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.qingeng.fjjdoctor.util.LocalDataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddFriendMainActivity extends UI implements View.OnClickListener {


    private static final int REQUEST_CODE_ADVANCED = 2;

    private String account;

    private ConstraintLayout layout_add_phone;
    private ConstraintLayout layout_add_invite;
    private ConstraintLayout layout_create_group;
    private ConstraintLayout layout_scan;
    private LinearLayout layout_my_info;
    private RelativeLayout layout_search;

    private TextView tv_my_id;

    private NimUserInfo userInfo;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, AddFriendMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend_main);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.add_friend_main;
        setToolBar(R.id.toolbar, options);

        account = DemoCache.getAccount();
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private void findViews() {
        layout_my_info = findViewById(R.id.layout_my_info);
        layout_add_phone = findViewById(R.id.layout_add_phone);
        layout_add_invite = findViewById(R.id.layout_add_invite);
        layout_create_group = findViewById(R.id.layout_create_group);
        layout_scan = findViewById(R.id.layout_scan);
        tv_my_id = findViewById(R.id.tv_my_id);
        layout_search = findViewById(R.id.layout_search);


        layout_create_group.setOnClickListener(this);
        layout_scan.setOnClickListener(this);
        layout_add_phone.setOnClickListener(this);
        layout_add_invite.setOnClickListener(this);
        layout_my_info.setOnClickListener(this);
        layout_search.setOnClickListener(this);
    }

    private void getUserInfo() {
        userInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo == null) {
            NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {

                @Override
                public void onResult(boolean success, NimUserInfo result, int code) {
                    if (success) {
                        userInfo = result;
                        updateUI();
                    } else {
                        ToastHelper.showToast(AddFriendMainActivity.this, "getUserInfoFromRemote failed:" + code);
                    }
                }
            });
        } else {
            updateUI();
        }
    }

    private void updateUI() {
        tv_my_id.setText("我的ID号：" + LocalDataUtils.getUserInfo().getUserId());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_search:
//                AddFriendActivity.start(AddFriendMainActivity.this);
                ContactSearchActivity.start(AddFriendMainActivity.this);
                break;
            case R.id.layout_add_phone:
                MyPhoneActivity.start(AddFriendMainActivity.this);
                break;
            case R.id.layout_add_invite:
                AddFriendSysActivity.start(this);
                break;
            case R.id.layout_my_info:
                MyCodeActivity.start(AddFriendMainActivity.this, AppPreferences.getAccId());
                break;
            case R.id.layout_create_group:
                ContactSelectActivity.Option advancedOption = TeamHelper
                        .getCreateContactSelectOption(null, 50);
                NimUIKit.startContactSelector(this, advancedOption,
                        REQUEST_CODE_ADVANCED);
                break;
            case R.id.layout_scan:
                startCaptureActivityForResult();
                break;
        }
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

            HttpClient.queryMyFriends(null, new HttpInterface() {
                @Override
                public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                    List<MyFriendBean> myFriendBeans = new ArrayList<>();
                    JSONArray objects = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()));
                    for (int i = 0; i < objects.size(); i++) {
                        JSONArray array = JSON.parseArray(JSON.toJSONString(objects.get(i)));
                        List<MyFriendBean> users = JSON.parseArray(JSON.toJSONString(array), MyFriendBean.class);
                        myFriendBeans.addAll(users);
                    }
                    Map<String, String> userMaps = new HashMap<>();
                    for (int i = 0; i < myFriendBeans.size(); i++) {
                        userMaps.put(myFriendBeans.get(i).getFaccid(), myFriendBeans.get(i).getUserId() + "");
                    }
                    ArrayList<String> userIds = new ArrayList<>();
                    for (int i = 0; i < selected.size(); i++) {
                        userIds.add(userMaps.get(selected.get(i)));
                    }
                    TeamCreateHelper.createAdvancedTeam(AddFriendMainActivity.this, userIds);
                }

                @Override
                public void onFailure(int requestCode, String message) {

                }

                @Override
                public void onComplete() {

                }
            }, RequestCommandCode.QUERY_MY_FRIENDS);
        } else if (requestCode == CaptureActivity.REQ_CODE) {
            switch (resultCode) {
                case RESULT_OK:
//                    tvResult.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                    try {
                        //base64解码
                        String str2 = new String(Base64.decode(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT).getBytes(), Base64.DEFAULT));
                        System.out.println("code===" + str2);


                        if (str2.startsWith("**sx_group")) {
                            String id = str2.replace("**sx_group", "");
                            HttpClient.groupDetail(id, new HttpInterface() {
                                @Override
                                public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                                    JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                                    GroupDetailBean.WaHuHighGroup waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);
                                    boolean joined = false;
                                    for (int i = 0; i < waHuHighGroup.getHighGroups().size(); i++) {
                                        if (waHuHighGroup.getHighGroups().get(i).getAccid().equals(AppPreferences.getAccId())) {
                                            joined = true;
                                            break;
                                        }
                                    }
                                    if (joined) {
                                        scanToInto(waHuHighGroup.getTid());
                                    } else {
                                        inviteNewUser(waHuHighGroup, AppPreferences.getUserId()+"");
                                    }
                                }

                                @Override
                                public void onFailure(int requestCode, String message) {
                                    ToastHelper.showToast(AddFriendMainActivity.this, "获取用户详情失败");
                                }

                                @Override
                                public void onComplete() {

                                }
                            }, RequestCommandCode.GROUP_DETAIL);
                        } else if (str2.startsWith("**sx")) {
                            String id = str2.replace("**sx", "");
                            HttpClient.userInfoByAccId(id, new HttpInterface() {
                                @Override
                                public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                                    UserProfileActivity2.start2(AddFriendMainActivity.this, id);
                                }

                                @Override
                                public void onFailure(int requestCode, String message) {
                                    ToastHelper.showToast(AddFriendMainActivity.this, "获取用户详情失败");
                                }

                                @Override
                                public void onComplete() {

                                }
                            }, 1);

                        } else {
                            ToastHelper.showToast(AddFriendMainActivity.this, "解析扫描结果失败");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastHelper.showToast(AddFriendMainActivity.this, "解析扫描结果失败");
                    }
                    break;
                case RESULT_CANCELED:
                    if (data != null) {
//                        tvResult.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                    }
                    break;
            }
        }
    }

    private void scanToInto(String tId) {
        HttpClient.groupDetail(tId, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                GroupDetailBean.GroupUser groupUser = JSONObject.parseObject(jsonObject.get("waUserHighGroup").toString(), GroupDetailBean.GroupUser.class);
                GroupDetailBean.WaHuHighGroup waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);

                if (groupUser.getCurrentUserIdentity().equals("未入群")) {
                    EasyAlertDialogHelper.showOneButtonDiolag(AddFriendMainActivity.this, "", "已退出群聊",
                            getString(com.netease.nim.uikit.R.string.ok),
                            true, null);
                    return;
                }

                if (groupUser.getGroupStatus().equals("正常")) {
                    SessionHelper.startTeamSession(AddFriendMainActivity.this, tId);
                } else {
                    if (groupUser.getGroupStatus().equals("到期")) {
                        groupExpireMessage(groupUser, tId);
                    } else {
                        groupStatusError(groupUser.getGroupStatus());
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(AddFriendMainActivity.this, message);
            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.GROUP_DETAIL);
    }

    private void groupExpireMessage(GroupDetailBean.GroupUser groupUser, String tId) {
        if (groupUser.getCurrentUserIdentity().equals("群主") || groupUser.getCurrentUserIdentity().equals("群管理")) {
            EasyAlertDialogHelper.createOkCancelDiolag(this, "", "该高级群已到期，请联系群主续费", "去续费", "取消",
                    true, new EasyAlertDialogHelper.OnDialogActionListener() {
                        @Override
                        public void doCancelAction() {
                        }

                        @Override
                        public void doOkAction() {
                            AdvancedTeamUpgradeActivity.start(AddFriendMainActivity.this, tId);
                        }
                    }).show();
        } else {
            EasyAlertDialogHelper.showOneButtonDiolag(AddFriendMainActivity.this, "", "该高级群已到期，请联系群主续费",
                    getString(com.netease.nim.uikit.R.string.ok),
                    true, null);
        }
    }

    private void groupStatusError(String errorStatusStr) {
        EasyAlertDialogHelper.showOneButtonDiolag(AddFriendMainActivity.this, "", "该群已" + errorStatusStr,
                getString(com.netease.nim.uikit.R.string.ok),
                true, null);
    }


    private void inviteNewUser(GroupDetailBean.WaHuHighGroup waHuHighGroup, String uId) {
        if (!waHuHighGroup.getInvitemodeDesc().equals("所有人")) {
            ToastHelper.showToast(AddFriendMainActivity.this, "禁止扫码进群");
            return;
        }

        HttpClient.inviteNewUser(waHuHighGroup.getTid(), uId, "scan", new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                if (waHuHighGroup.getBeinvitemode() == 0) {
                    ToastHelper.showToast(AddFriendMainActivity.this, "群组开启邀请确认，等确认后进入");
                } else {
                    ToastHelper.showToast(AddFriendMainActivity.this, "加入成功，进入群聊");
                    scanToInto(waHuHighGroup.getTid());
                }
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(AddFriendMainActivity.this, "邀请失败:" + message);
            }

            @Override
            public void onComplete() {
                DialogMaker.dismissProgressDialog();
            }
        }, RequestCommandCode.REMOVE_ANNO);
    }


    private void startCaptureActivityForResult() {
        Intent intent = new Intent(AddFriendMainActivity.this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }


}

