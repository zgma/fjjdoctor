package com.netease.nim.uikit.business.team.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 高级群群资料页
 * Created by huangjun on 2015/3/17.
 */
public class AdvancedTeamManageActivity extends UI implements HttpInterface {

    private static final String EXTRA_ID = "EXTRA_ID";

    private String teamId;

    private View team_upgrade;
    private RelativeLayout layout_team_managers;
    private HeadImageView team_head_manage_1;
    private HeadImageView team_head_manage_2;
    private HeadImageView team_head_manage_3;

    private LinearLayout layout_manage;

    private View switchLayout1;
    private View switchLayout2;
    private View switchLayout3;
    private View switchLayout4;
    private View switchLayout5;

    private SwitchButton switchButton_1;
    private SwitchButton switchButton_2;
    private SwitchButton switchButton_3;
    private SwitchButton switchButton_4;
    private SwitchButton switchButton_5;

    private View team_transfer_layout;
    private View team_queren_layout;
    private View team_huoyuedu_layout;
    private View team_tuiqun_layout;
    private View team_hongbao_layout;

    private GroupDetailBean.WaHuHighGroup waHuHighGroup;
    private GroupDetailBean.GroupUser groupUser;
    private List<GroupDetailBean.GroupUser> adminList = new ArrayList<>();
    private GroupDetailBean.GroupUser owner;


    private String higherStatus;


    private boolean isOwner = false;

    public static void start(Context context, String tid) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, tid);
        intent.setClass(context, AdvancedTeamManageActivity.class);
        context.startActivity(intent);
    }


    /**
     * ***************************** Life cycle *****************************
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_advanced_team_manage);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "管理群";
        setToolBar(R.id.toolbar, options);
        parseIntentData();
        findViews();
        getData();

    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void findViews() {

        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setVisibility(View.GONE);

        team_upgrade = findViewById(R.id.team_upgrade);
        ((TextView) findViewById(R.id.team_upgrade).findViewById(R.id.item_detail)).setText("");
        team_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamUpgradeActivity.start(AdvancedTeamManageActivity.this, teamId);
            }
        });

        layout_manage = findViewById(R.id.layout_manage);

        switchLayout1 = findViewById(R.id.invite_confirm_layout);
        ((TextView) switchLayout1.findViewById(R.id.user_profile_title)).setText("群邀请确认");
        switchButton_1 = (SwitchButton) switchLayout1.findViewById(R.id.user_profile_toggle);
        switchButton_1.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(View v, boolean checkState) {
                updateGroupInfo("5", checkState ? "1" : "0");
            }
        });
        switchLayout2 = findViewById(R.id.p2p_can_layout);
        ((TextView) switchLayout2.findViewById(R.id.user_profile_title)).setText("禁止群成员互加好友");
        switchButton_2 = (SwitchButton) switchLayout2.findViewById(R.id.user_profile_toggle);
        switchButton_2.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(View v, boolean checkState) {
                updateGroupInfo("6", checkState ? "2" : "1");
            }
        });

        switchLayout3 = findViewById(R.id.user_can_invite_layout);
        ((TextView) switchLayout3.findViewById(R.id.user_profile_title)).setText("允许普通成员邀请好友");
        switchButton_3 = (SwitchButton) switchLayout3.findViewById(R.id.user_profile_toggle);
        switchButton_3.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(View v, boolean checkState) {
                updateGroupInfo("9", checkState ? "1" : "2");
            }
        });

        switchLayout4 = findViewById(R.id.p_down_notify_layout);
        ((TextView) switchLayout4.findViewById(R.id.user_profile_title)).setText("群组减员发送通知");
        switchButton_4 = (SwitchButton) switchLayout4.findViewById(R.id.user_profile_toggle);
        switchButton_4.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(View v, boolean checkState) {
                updateGroupInfo("8", checkState ? "1" : "2");
            }
        });

        switchLayout5 = findViewById(R.id.announcement_notify_layout);
        ((TextView) switchLayout5.findViewById(R.id.user_profile_title)).setText("公告强提醒");
        switchButton_5 = (SwitchButton) switchLayout5.findViewById(R.id.user_profile_toggle);
        switchButton_5.setOnChangedListener(new SwitchButton.OnChangedListener() {
            @Override
            public void OnChanged(View v, boolean checkState) {
                updateGroupInfo("7", checkState ? "1" : "2");
            }
        });

        team_transfer_layout = findViewById(R.id.team_transfer_layout);
        ((TextView) findViewById(R.id.team_transfer_layout).findViewById(R.id.item_title)).setText("转让群");
        ((TextView) findViewById(R.id.team_transfer_layout).findViewById(R.id.item_detail)).setText("");
        team_transfer_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamManageListActivity.start(AdvancedTeamManageActivity.this, waHuHighGroup.getTid(), waHuHighGroup.getId() + "", true);

            }
        });

        team_huoyuedu_layout = findViewById(R.id.team_huoyuedu_layout);
        ((TextView) findViewById(R.id.team_huoyuedu_layout).findViewById(R.id.item_title)).setText("群成员活跃度");
        ((TextView) findViewById(R.id.team_huoyuedu_layout).findViewById(R.id.item_detail)).setText("");
        team_huoyuedu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamActiveListActivity.start(AdvancedTeamManageActivity.this, waHuHighGroup.getId()+"");

            }
        });

        team_queren_layout = findViewById(R.id.team_queren_layout);
        ((TextView) findViewById(R.id.team_queren_layout).findViewById(R.id.item_title)).setText("群邀请确认");
        ((TextView) findViewById(R.id.team_queren_layout).findViewById(R.id.item_detail)).setText("");
        team_queren_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedInviteConfirmActivity.start(AdvancedTeamManageActivity.this, waHuHighGroup.getId()+"");
            }
        });

        team_tuiqun_layout = findViewById(R.id.team_tuiqun_layout);
        ((TextView) findViewById(R.id.team_tuiqun_layout).findViewById(R.id.item_title)).setText("退群成员列表");
        ((TextView) findViewById(R.id.team_tuiqun_layout).findViewById(R.id.item_detail)).setText("");
        team_tuiqun_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamActiveListActivity.start(AdvancedTeamManageActivity.this, waHuHighGroup.getId()+"",1);

            }
        });

        team_hongbao_layout = findViewById(R.id.team_hongbao_layout);
        ((TextView) findViewById(R.id.team_hongbao_layout).findViewById(R.id.item_title)).setText("禁止领取红包");
        ((TextView) findViewById(R.id.team_hongbao_layout).findViewById(R.id.item_detail)).setText("");
        team_hongbao_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamActiveListActivity.start(AdvancedTeamManageActivity.this, waHuHighGroup.getTid()+"",2);
            }
        });

        layout_team_managers = findViewById(R.id.layout_team_managers);
        layout_team_managers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamManageListActivity.start(AdvancedTeamManageActivity.this, waHuHighGroup.getTid(), waHuHighGroup.getId() + "", false);
            }
        });

        team_head_manage_1 = findViewById(R.id.team_head_manage_1);
        team_head_manage_2 = findViewById(R.id.team_head_manage_2);
        team_head_manage_3 = findViewById(R.id.team_head_manage_3);

    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GROUP_DETAIL:
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);
                groupUser = JSONObject.parseObject(jsonObject.get("waUserHighGroup").toString(), GroupDetailBean.GroupUser.class);
                updateTeamInfo();
                break;
            case RequestCommandCode.GET_OWNER_AND_ADMIN:
                // groupUsers = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()),GroupDetailBean.GroupUser.class);
                break;
            case RequestCommandCode.UPDATE_GROUP_INFO_CAN_INVITE:
                ToastHelper.showToast(this, "处理成功");
                getGroupDate();
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

    private void getGroupDate() {
        HttpClient.groupDetail(teamId, this, RequestCommandCode.GROUP_DETAIL);
    }


    private void getOwnerAndAdmin() {
        //HttpClient.getOwnerAndAdmin(teamId, this, RequestCommandCode.GET_OWNER_AND_ADMIN);
    }


    private void updateTeamInfo() {
        processData();
        getIsAdminOrIsManage();
        if (isOwner) {
            team_transfer_layout.setVisibility(View.VISIBLE);
            layout_team_managers.setVisibility(View.VISIBLE);
        } else {
            team_transfer_layout.setVisibility(View.GONE);
            layout_team_managers.setVisibility(View.GONE);
        }

        team_head_manage_1.loadImgForUrl(owner.getHeadImage()==null?"":owner.getHeadImage());

        team_head_manage_2.setVisibility(View.GONE);
        team_head_manage_3.setVisibility(View.GONE);

        if (adminList.size() > 0) {
            team_head_manage_2.setVisibility(View.VISIBLE);
            team_head_manage_2.loadImgForUrl(adminList.get(0).getHeadImage());
        }
        if (adminList.size() > 1) {
            team_head_manage_3.setVisibility(View.VISIBLE);
            team_head_manage_3.loadImgForUrl(adminList.get(1).getHeadImage());
        }


        ((TextView) findViewById(R.id.team_upgrade).findViewById(R.id.item_title)).setText(waHuHighGroup.getHigherStatus().equals("普通群") ? "升级群" : "高级群");
        switchButton_1.setCheck(waHuHighGroup.getBeinvitemode()==0 ? true : false);
        switchButton_2.setCheck(waHuHighGroup.getPrivateMode().equals("允许私聊") ? false : true);
        switchButton_3.setCheck(waHuHighGroup.getInvitemodeDesc().equals("所有人") ? true : false);
        switchButton_4.setCheck(waHuHighGroup.getSubstractMode().equals("减员提醒") ? true : false);
        switchButton_5.setCheck(waHuHighGroup.getNoticeMode().equals("强提醒") ? true : false);

        switchLayout2.setVisibility(waHuHighGroup.getHigherStatus().equals("普通群") ? View.VISIBLE : View.VISIBLE);
        switchLayout3.setVisibility(waHuHighGroup.getHigherStatus().equals("普通群") ? View.VISIBLE : View.VISIBLE);
        switchLayout4.setVisibility(waHuHighGroup.getHigherStatus().equals("普通群") ? View.VISIBLE : View.VISIBLE);
        switchLayout5.setVisibility(waHuHighGroup.getHigherStatus().equals("普通群") ? View.VISIBLE : View.VISIBLE);

        switchLayout2.setVisibility(View.VISIBLE);
        switchLayout3.setVisibility(View.GONE);
        switchLayout4.setVisibility(View.GONE);
        switchLayout5.setVisibility(View.GONE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        getGroupDate();
        getOwnerAndAdmin();
    }

    private void getIsAdminOrIsManage() {
    /*    if (waHuHighGroup==null || groupUser==null || groupUsers==null || groupUsers.isEmpty()){
            return;
        }*/

        if (groupUser.getCurrentUserIdentity().equals("群主")) {
            isOwner = true;
        } else {
            isOwner = false;
        }

   /*     for (GroupDetailBean.GroupUser groupUser :groupUsers){
            isManage = false;
            if (AppPreferences.getUserId().equals(groupUser.getUserId()) && groupUser.getCurrentUserIdentity().equals("管理员")){
                isManage = true;
            }
        }*/
    }

    private void updateGroupInfo(String type, String opeValue) {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("groupId", waHuHighGroup.getId() + "");
        baseRequestBean.addParams("ope", opeValue);
        baseRequestBean.addParams("type", type);
        HttpClient.updateGroupInfo(baseRequestBean, AdvancedTeamManageActivity.this, RequestCommandCode.UPDATE_GROUP_INFO_CAN_INVITE);
    }


    private void processData() {
        adminList.clear();
        if (waHuHighGroup.getHighGroups() == null) return;
        for (int i = 0; i < waHuHighGroup.getHighGroups().size(); i++) {
            if (waHuHighGroup.getHighGroups().get(i).getCurrentUserIdentity().equals("群主")) {
                owner = waHuHighGroup.getHighGroups().get(i);
            } else if (waHuHighGroup.getHighGroups().get(i).getCurrentUserIdentity().equals("管理员")) {
                adminList.add(waHuHighGroup.getHighGroups().get(i));
            }
        }
    }
}
