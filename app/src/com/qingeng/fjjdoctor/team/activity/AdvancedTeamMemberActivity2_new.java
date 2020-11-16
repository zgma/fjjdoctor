package com.qingeng.fjjdoctor.team.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.team.adapter.TeamMemberAdapter;
import com.netease.nim.uikit.business.team.adapter.TeamMemberAdapter.TeamMemberItem;
import com.netease.nim.uikit.business.team.ui.TeamInfoGridView;
import com.netease.nim.uikit.business.team.viewholder.TeamMemberHolder;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;

import java.util.ArrayList;
import java.util.List;

/**
 * 群成员列表界面
 * Created by hzxuwen on 2015/3/17.
 */
public class AdvancedTeamMemberActivity2_new extends UI implements TAdapterDelegate,
        TeamMemberAdapter.RemoveMemberCallback, TeamMemberAdapter.AddMemberCallback, TeamMemberHolder.TeamMemberHolderEventListener,
        HttpInterface {

    // constant
    private static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_DATA = "EXTRA_DATA";

    // data source
    private String teamId;
    private List<TeamMember> members;
    private TeamMemberAdapter adapter;
    private List<String> memberAccounts;
    private List<TeamMemberItem> dataSource;
    private List<GroupDetailBean.GroupUser> userList = new ArrayList<>();
    private GroupDetailBean.WaHuHighGroup waHuHighGroup;
    private String creator;
    private List<String> managerList;

    // state
    private boolean isSelfAdmin = false;
    private boolean isSelfManager = false;
    private boolean isMemberChange = false;
    private UserInfoObserver userInfoObserver;

    public static void startActivityForResult(Activity context, String tid, int resCode) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, tid);
        intent.setClass(context, AdvancedTeamMemberActivity2_new.class);
        context.startActivityForResult(intent, resCode);
    }


    public static void start(Activity context, String tid) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, tid);
        intent.setClass(context, AdvancedTeamMemberActivity2_new.class);
        context.startActivity(intent);
    }

    /**
     * *********************************lifeCycle*******************************************
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_team_member_grid_layout);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.team_member;
        setToolBar(R.id.toolbar, options);

        parseIntentData();
        loadTeamInfo();
        initAdapter();
        findViews();
        requestData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATA, isMemberChange);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }

    private void loadTeamInfo() {
        Team team = NimUIKit.getTeamProvider().getTeamById(teamId);
        if (team != null) {
            creator = team.getCreator();
        }
    }

    private void findViews() {
        TeamInfoGridView teamInfoGridView = (TeamInfoGridView) findViewById(R.id.team_member_grid);
        teamInfoGridView.setSelector(R.color.transparent);
        teamInfoGridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        teamInfoGridView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && adapter.getMode() == TeamMemberAdapter.Mode.DELETE) {
                    adapter.setMode(TeamMemberAdapter.Mode.NORMAL);
                    adapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        teamInfoGridView.setAdapter(adapter);

        adapter.setMode(TeamMemberAdapter.Mode.DELETE);
        adapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        memberAccounts = new ArrayList<>();
        members = new ArrayList<>();
        dataSource = new ArrayList<>();
        managerList = new ArrayList<>();
        adapter = new TeamMemberAdapter(this, dataSource, this, this, this);
        adapter.setEventListener(this);
    }

    /**
     * 初始化成员身份
     *
     * @param account 帐号
     * @return String
     */
    private String initMemberIdentity(String account) {
        String identity;
        if (creator.equals(account)) {
            identity = TeamMemberHolder.OWNER;
        } else if (managerList.contains(account)) {
            identity = TeamMemberHolder.ADMIN;
        } else {
            identity = null;
        }
        return identity;
    }


    @Override
    public void onAddMember() {

    }

    @Override
    public void onRemoveMember(String account) {
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, "提示",
                "确定移出该名成员", true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }
                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(AdvancedTeamMemberActivity2_new.this, "处理中...");
                        HttpClient.kickUser(waHuHighGroup.getId()+"",account, AdvancedTeamMemberActivity2_new.this,RequestCommandCode.KICK_USER);
                    }
                });
        dialog.show();
    }

    /**
     * ******************************加载数据*******************************
     */
    private void requestData() {
        getGroupData();
    }

    /**
     * ************************ TAdapterDelegate **************************
     */

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return TeamMemberHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    @Override
    public void onHeadImageViewClick(String account) {
        UserProfileActivity2.start2(AdvancedTeamMemberActivity2_new.this, account);
//        AdvancedTeamMemberInfoActivity.startActivityForResult(AdvancedTeamMemberActivity2_new.this, account, teamId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

            }
        }
    }

    /**
     * 移除群成员成功后，删除列表中的群成员
     */
    private void removeMember(String account) {
        if (TextUtils.isEmpty(account)) {
            return;
        }
        for (TeamMemberItem item : dataSource) {
            if (item.getAccount() != null && item.getAccount().equals(account)) {
                dataSource.remove(item);
                isMemberChange = true;
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }


    private void getGroupData() {
        HttpClient.groupDetail(teamId, this, RequestCommandCode.GROUP_DETAIL);
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GROUP_DETAIL:
                userList.clear();
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);
                userList  = waHuHighGroup.getHighGroups();
                updateTeamInfo2();
                break;
            case RequestCommandCode.KICK_USER:
                ToastHelper.showToast(this, "移除成功");
                getGroupData();
                break;
        }

    }

    private void updateTeamInfo2() {
        dataSource.clear();
        for (GroupDetailBean.GroupUser groupUser : userList) {
            dataSource.add(new TeamMemberItem(TeamMemberAdapter.TeamMemberItemTag
                    .NORMAL, teamId, groupUser.getAccid(), initMemberIdentity(groupUser.getAccid()),groupUser.getUserId()+""));
        }

        // refresh
        adapter.notifyDataSetChanged();
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
