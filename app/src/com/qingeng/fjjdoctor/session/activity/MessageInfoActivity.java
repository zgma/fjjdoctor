package com.qingeng.fjjdoctor.session.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.ReportItemBean;
import com.qingeng.apilibrary.bean.TargetUserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;
import com.qingeng.fjjdoctor.contact.activity.UserProfileEditItemActivity;
import com.qingeng.fjjdoctor.contact.constant.UserConstant;
import com.qingeng.fjjdoctor.main.activity.HistorySearchActivity;
import com.qingeng.fjjdoctor.session.recent.RecentContactsFragment;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;

import com.qingeng.fjjdoctor.DemoCache;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.setting.SendFeedbackActivity2;
import com.qingeng.fjjdoctor.team.TeamCreateHelper;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.dialog.MenuDialog;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.CreateTeamResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzxuwen on 2015/10/13.
 */
public class MessageInfoActivity extends UI implements View.OnClickListener,HttpInterface{
    private final static String EXTRA_ACCOUNT = "EXTRA_ACCOUNT";
    private static final int REQUEST_CODE_NORMAL = 1;
    // data
    private String account;
    // view
    private SwitchButton switchButton_top;
    private SwitchButton switchButton_notice;
    private SwitchButton switchButton_black;
    private SwitchButton switchButton_delMsg;
    private SwitchButton switchButton_screen_shot;

    private RelativeLayout layout_alias;
    private RelativeLayout layout_userInfo;
    private RelativeLayout layout_search_history;
    private RelativeLayout layout_clear_history;
    private RelativeLayout layout_report;

    private TextView tv_top;
    private TextView tv_notice;
    private TextView tv_black;
    private TextView tv_delMsg;
    private TextView tv_alias;
    private TextView tv_user_name;
    private HeadImageView user_photo;

    private TargetUserBean targetUserBean;
    private List<ReportItemBean> reportItemBeans;

    public static void startActivity(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, MessageInfoActivity.class);
        intent.putExtra(EXTRA_ACCOUNT, account);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_info_activity);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.message_info;
        options.navigateId = R.drawable.actionbar_dark_back_icon;
        setToolBar(R.id.toolbar, options);

        account = getIntent().getStringExtra(EXTRA_ACCOUNT);
        findViews();


        layout_alias = findView(R.id.recycler_view);
        tv_alias = findView(R.id.tv_alias);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateSwitchBtn();
        getReportItem();
    }

    private void getReportItem() {
        HttpClient.getReportItem(1,this,RequestCommandCode.GET_REPORT_ITEM);
    }

    private void findViews() {
        HeadImageView userHead = (HeadImageView) findViewById(R.id.user_layout).findViewById(R.id.imageViewHeader);
        TextView userName = (TextView) findViewById(R.id.user_layout).findViewById(R.id.textViewName);
        userHead.loadBuddyAvatar(account);
        userName.setText(UserInfoHelper.getUserDisplayName(account));
        userHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUserProfile();
            }
        });

        ((TextView) findViewById(R.id.create_team_layout).findViewById(R.id.textViewName)).setText(R.string.create_normal_team);
        HeadImageView addImage = (HeadImageView) findViewById(R.id.create_team_layout).findViewById(R.id.imageViewHeader);
        addImage.setBackgroundResource(com.netease.nim.uikit.R.drawable.nim_team_member_add_selector);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTeamMsg();
            }
        });


        layout_userInfo = findViewById(R.id.layout_userInfo);
        tv_user_name = findViewById(R.id.tv_user_name);
        user_photo = findViewById(R.id.user_photo);

        tv_top = ((TextView) findViewById(R.id.toggle_layout).findViewById(R.id.user_profile_title));
        tv_notice =((TextView) findViewById(R.id.toggle_layout2).findViewById(R.id.user_profile_title));
        tv_black = ((TextView) findViewById(R.id.toggle_layout3).findViewById(R.id.user_profile_title));
        tv_delMsg = ((TextView) findViewById(R.id.toggle_layout4).findViewById(R.id.user_profile_title));

        tv_top.setText(R.string.message_info_switch_tv_1);
        tv_notice.setText(R.string.message_info_switch_tv_2);
        tv_black.setText(R.string.message_info_switch_tv_3);
        tv_delMsg.setText("消息阅后即焚");

        switchButton_top = (SwitchButton) findViewById(R.id.toggle_layout).findViewById(R.id.user_profile_toggle);
        switchButton_top.setOnChangedListener(onChangedListener);
        switchButton_notice = (SwitchButton) findViewById(R.id.toggle_layout2).findViewById(R.id.user_profile_toggle);
        switchButton_notice.setOnChangedListener(onChangedListener);
        switchButton_black = (SwitchButton) findViewById(R.id.toggle_layout3).findViewById(R.id.user_profile_toggle);
        switchButton_black.setOnChangedListener(onChangedListener);
        switchButton_delMsg = (SwitchButton) findViewById(R.id.toggle_layout4).findViewById(R.id.user_profile_toggle);
        switchButton_delMsg.setOnChangedListener(onChangedListener);


        ((TextView) findViewById(R.id.screen_shot_layout).findViewById(com.netease.nim.uikit.R.id.user_profile_title)).setText("截屏通知");
        switchButton_screen_shot = (SwitchButton) findViewById(com.netease.nim.uikit.R.id.screen_shot_layout).findViewById(com.netease.nim.uikit.R.id.user_profile_toggle);
        switchButton_screen_shot.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(View v, boolean checkState) {
                BaseRequestBean baseRequestBean = new BaseRequestBean();
                baseRequestBean.addParams("targetAccid", targetUserBean.getTargetUserAccid() + "");
                baseRequestBean.addParams("status", checkState ? 1 : 0);
                baseRequestBean.addParams("type", 2);
                HttpClient.updateScreenNotify(baseRequestBean, MessageInfoActivity.this, RequestCommandCode.FORBIDDEN_USER);
            }
        });

        layout_alias = findViewById(R.id.layout_alias);
        layout_search_history = findViewById(R.id.layout_search_history);
        layout_clear_history = findViewById(R.id.layout_clear_history);
        layout_report = findViewById(R.id.layout_report);

        layout_alias.setOnClickListener(this);
        layout_search_history.setOnClickListener(this);
        layout_clear_history.setOnClickListener(this);
        layout_report.setOnClickListener(this);
        layout_userInfo.setOnClickListener(this);

    }

    private void updateSwitchBtn() {
/*        boolean notice = NIMClient.getService(FriendService.class).isNeedMessageNotify(account);
        switchButton.setCheck(notice);*/
        tv_top.setText(R.string.message_info_switch_tv_1);
        tv_notice.setText(R.string.message_info_switch_tv_2);
        tv_black.setText(R.string.message_info_switch_tv_3);
        HttpClient.userInfoByAccId(account, this, RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID);
        HttpClient.queryTopOrNotice(account, 2,this, RequestCommandCode.QUERY_TOP_OR_NOTICE);
    }

    private boolean topState = false;

    private SwitchButton.OnChangedListener onChangedListener = new SwitchButton.OnChangedListener() {
        @Override
        public void OnChanged(View v, final boolean checkState) {
            if (!NetworkUtil.isNetAvailable(MessageInfoActivity.this)) {
                ToastHelper.showToast(MessageInfoActivity.this, R.string.network_is_not_available);
                return;
            }
            if (v == switchButton_top){
                topState = checkState;
                HttpClient.setUserTop(targetUserBean.getTargetUserAccid(),checkState?1:2,MessageInfoActivity.this, RequestCommandCode.SET_USER_TOP);
            }
            if (v == switchButton_notice){
                HttpClient.setUserNotice(targetUserBean.getTargetUserId()+"",checkState?1:2,MessageInfoActivity.this, RequestCommandCode.SET_USER_NOTICE);
            }
            if (v == switchButton_black){
                HttpClient.updateBlackList(targetUserBean.getTargetUserId()+"",checkState?1:2,MessageInfoActivity.this,RequestCommandCode.UPDATE_BLACK);
            }
            if (v == switchButton_delMsg){
                HttpClient.setDelMsg(targetUserBean.getTargetUserAccid()+"",checkState?1:2,MessageInfoActivity.this,RequestCommandCode.UPDATE_BLACK);
            }
         /*   NIMClient.getService(FriendService.class).setMessageNotify(account, checkState).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    if (checkState) {
                        ToastHelper.showToast(MessageInfoActivity.this, "开启消息提醒成功");
                    } else {
                        ToastHelper.showToast(MessageInfoActivity.this, "关闭消息提醒成功");
                    }
                }

                @Override
                public void onFailed(int code) {
                    if (code == 408) {
                        ToastHelper.showToast(MessageInfoActivity.this, R.string.network_is_not_available);
                    } else {
                        ToastHelper.showToast(MessageInfoActivity.this, "on failed:" + code);
                    }
                    switchButton.setCheck(!checkState);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });*/
        }
    };

    private void openUserProfile() {
        UserProfileActivity2.start(this, targetUserBean.getTargetUserId()+"");
    }

    /**
     * 创建群聊
     */
    private void createTeamMsg() {
        ArrayList<String> memberAccounts = new ArrayList<>();
        memberAccounts.add(account);
        ContactSelectActivity.Option option = TeamHelper.getCreateContactSelectOption(memberAccounts, 50);
        NimUIKit.startContactSelector(this, option, REQUEST_CODE_NORMAL);// 创建群
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_NORMAL) {
                final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                if (selected != null && !selected.isEmpty()) {
                    TeamCreateHelper.createNormalTeam(MessageInfoActivity.this, selected, true, new RequestCallback<CreateTeamResult>() {
                        @Override
                        public void onSuccess(CreateTeamResult param) {
                            finish();
                        }

                        @Override
                        public void onFailed(int code) {

                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });
                } else {
                    ToastHelper.showToast(DemoCache.getContext(), "请选择至少一个联系人！");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_alias:
                UserProfileEditItemActivity.startActivity(MessageInfoActivity.this, UserConstant.KEY_ALIAS, targetUserBean.getTargetUserId()+"");
                break;
            case R.id.layout_search_history:
                HistorySearchActivity.start(this,targetUserBean.getTargetUserAccid(),targetUserBean.getTargetUsername());
                break;
            case R.id.layout_clear_history:
                EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(MessageInfoActivity.this, "提示",
                        "删除聊天记录", true,
                        new EasyAlertDialogHelper.OnDialogActionListener() {

                            @Override
                            public void doCancelAction() {

                            }
                            @Override
                            public void doOkAction() {
                                NIMClient.getService(MsgService.class).clearChattingHistory(account, SessionTypeEnum.P2P);
                            }
                        });
                if (!isFinishing() && !isDestroyedCompatible()) {
                    dialog.show();
                }


                break;
            case R.id.layout_report:
 /*               if (reportItemBeans!=null && !reportItemBeans.isEmpty()){
                    List<String> messages = new ArrayList<>();
                    for (ReportItemBean reportItemBean : reportItemBeans){
                        messages.add(reportItemBean.getContent());
                    }
                    showTeamInviteeAuthenMenu(messages);
                }*/
                SendFeedbackActivity2.start(MessageInfoActivity.this,1,targetUserBean.getTargetUserId()+"");

                break;
            case R.id.layout_userInfo:
                UserProfileActivity2.start2(this, account);
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode){
            case RequestCommandCode.QUERY_TOP_OR_NOTICE:
                JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()));
                System.out.println(jsonObject.toJSONString());
                String relationShipDesc = jsonObject.getString("relationShipDesc"); //好友
                String topDesc = jsonObject.getString("topDesc"); //非置顶
                String noticeDesc = jsonObject.getString("noticeDesc");//不静音
                Integer delMsg = jsonObject.getInteger("delMsg");//不静音
                Integer targetScreenNotify = jsonObject.getInteger("targetScreenNotify");//不静音

                switchButton_top.setCheck(!topDesc.equals("非置顶"));
                switchButton_notice.setCheck(!noticeDesc.equals("不静音"));
                switchButton_delMsg.setCheck(delMsg==1);
                switchButton_screen_shot.setCheck(targetScreenNotify == 1);

/*                tv_top.setText(tv_top.getText()+" "+topDesc);
                tv_notice.setText(tv_notice.getText()+" "+noticeDesc);
                tv_black.setText(tv_black.getText()+" "+relationShipDesc);*/

                break;
            case RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID:
                targetUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                tv_user_name.setText(targetUserBean.getTargetUsername());
                user_photo.loadAvatar(targetUserBean.getHeadImage());
                switchButton_black.setCheck(targetUserBean.getRelationShip()==5);
//                tv_black.setText(tv_black.getText()+" "+targetUserBean.getRelationShip());
                tv_alias.setText(targetUserBean.getAlias());
                System.out.println("onSuccess---"+JSON.toJSONString(targetUserBean));

                break;
            case RequestCommandCode.SET_USER_TOP:
                ToastHelper.showToast(this, "操作成功");
                //查询之前是不是存在会话记录
                RecentContact recentContact = NIMClient.getService(MsgService.class).queryRecentContact(account, SessionTypeEnum.P2P);
                //置顶
                if (topState) {
                    //如果之前不存在，创建一条空的会话记录
                    if (recentContact == null) {
                        // RecentContactsFragment 的 MsgServiceObserve#observeRecentContact 观察者会收到通知
                        NIMClient.getService(MsgService.class).createEmptyRecentContact(account,
                                SessionTypeEnum.P2P,
                                RecentContactsFragment.RECENT_TAG_STICKY,
                                System.currentTimeMillis(),
                                true);
                    }
                    // 之前存在，更新置顶flag
                    else {
                        CommonUtil.addTag(recentContact, RecentContactsFragment.RECENT_TAG_STICKY);
                        NIMClient.getService(MsgService.class).updateRecentAndNotify(recentContact);
                    }
                }
                //取消置顶
                else {
                    if (recentContact != null) {
                        CommonUtil.removeTag(recentContact, RecentContactsFragment.RECENT_TAG_STICKY);
                        NIMClient.getService(MsgService.class).updateRecentAndNotify(recentContact);
                    }
                }
                updateSwitchBtn();
                break;
            case RequestCommandCode.SET_USER_NOTICE:
                ToastHelper.showToast(this, "操作成功");
                updateSwitchBtn();
            case RequestCommandCode.UPDATE_BLACK:
            case RequestCommandCode.FORBIDDEN_USER:
                ToastHelper.showToast(this, "操作成功");
                updateSwitchBtn();
                break;
            case RequestCommandCode.ADD_REPORT:
                ToastHelper.showToast(this, "举报成功");
                updateSwitchBtn();
                break;
            case RequestCommandCode.GET_REPORT_ITEM:
                reportItemBeans = JSONArray.parseArray(JSON.toJSONString(baseResponseData.getData()),ReportItemBean.class);
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

    private MenuDialog reportMenu;


    // 显示被邀请人身份验证菜单
    private void showTeamInviteeAuthenMenu(List<String> messages) {
        if (reportMenu == null) {
            reportMenu = new MenuDialog(MessageInfoActivity.this, messages, -1, 2,
                    new MenuDialog.MenuDialogOnButtonClickListener() {

                        @Override
                        public void onButtonClick(int index,String name) {
                            reportMenu.dismiss();
                            if (name.equals(getString(com.netease.nim.uikit.R.string.cancel))) {
                                return; // 取消不处理
                            }
                            DialogMaker.showProgressDialog(MessageInfoActivity.this,"发送中...");
                            //HttpClient.addReport(targetUserBean.getTargetUserId()+"",name,1,MessageInfoActivity.this,RequestCommandCode.ADD_REPORT);
                        /*    if (type != null) {
                                updateBeInvitedMode(type);
                            }*/
                        }
                    });
        }
        reportMenu.show();
    }

    private String[] reportMessage = new String[]{
            "发布不适当内容对我造成骚扰","发布色情内容对我造成骚扰","发布违法违禁内容对我造成骚扰",
            "发布赌博内容对我造成骚扰","存在欺诈骗钱行为","此账号可能被盗用","存在侵权行为","取消"
    };

}
