package com.netease.nim.uikit.business.team.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.netease.nim.uikit.business.team.adapter.TeamManagerAdapter;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 高级群群资料页
 * Created by huangjun on 2015/3/17.
 */
public class AdvancedTeamManageListActivity extends UI implements HttpInterface, TeamManagerAdapter.OnClickLinsenter {

    private static final String EXTRA_G_ID = "EXTRA_G_ID";
    private static final String EXTRA_T_ID = "EXTRA_T_ID";
    private static final String EXTRA_SHOW_NORMAL_LIST = "EXTRA_SHOW_NORMAL_LIST";


    TeamManagerAdapter teamManagerAdapter;
    TeamManagerAdapter teamManagerAdapter_normal;

    private String tId;
    private String gId;
    // view
    private RecyclerView recyclerView;
    private RecyclerView recycler_view_normal;
    private LinearLayout layout_normal;

    private List<GroupDetailBean.GroupUser> userBeanList = new ArrayList<>();
    private List<GroupDetailBean.GroupUser> adminList = new ArrayList<>();
    private List<GroupDetailBean.GroupUser> normalList = new ArrayList<>();
    private GroupDetailBean.WaHuHighGroup waHuHighGroup;
    private GroupDetailBean.GroupUser owner;

    private boolean isEditing = false;

    private boolean showNormalList = false;

    private LinearLayout add_manage_layout;
    private HeadImageView owner_head;
    private TextView tv_owner_name;

    public static void start(Context context, String tId, String gId, boolean showNormalList) {
        Intent intent = new Intent();
        intent.setClass(context, AdvancedTeamManageListActivity.class);
        intent.putExtra(EXTRA_T_ID, tId);
        intent.putExtra(EXTRA_G_ID, gId);
        intent.putExtra(EXTRA_SHOW_NORMAL_LIST, showNormalList);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    private void getGroupDate() {
        HttpClient.groupDetail(tId, this, RequestCommandCode.GROUP_DETAIL);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manage_list);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "管理员";
        setToolBar(R.id.toolbar, options);
        parseIntentData();
        findViews();
        getGroupDate();
    }

    private void parseIntentData() {
        tId = getIntent().getStringExtra(EXTRA_T_ID);
        gId = getIntent().getStringExtra(EXTRA_G_ID);
        showNormalList = getIntent().getBooleanExtra(EXTRA_SHOW_NORMAL_LIST, false);
    }

    private void findViews() {
        layout_normal = findView(R.id.layout_normal);

        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText("编辑");
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditing = !isEditing;
                toolbarView.setText(isEditing ? "完成" : "编辑");
                teamManagerAdapter.setShowDeleteIcon(isEditing);
                teamManagerAdapter.notifyDataSetChanged();
            }
        });

        teamManagerAdapter = new TeamManagerAdapter(this);
        teamManagerAdapter.setGroupUsers(adminList);
        teamManagerAdapter.setOnClickLinsenter(this);


        teamManagerAdapter_normal = new TeamManagerAdapter(this);
        teamManagerAdapter_normal.setGroupUsers(normalList);
        teamManagerAdapter_normal.setOnClickLinsenter(this);


        recyclerView = findView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teamManagerAdapter);

        recycler_view_normal = findView(R.id.recycler_view_normal);
        recycler_view_normal.setLayoutManager(new LinearLayoutManager(this));
        recycler_view_normal.setAdapter(teamManagerAdapter_normal);

        add_manage_layout = findView(R.id.add_manage_layout);
        add_manage_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamNormalListActivity.start(AdvancedTeamManageListActivity.this, tId,false);
            }
        });
        owner_head = findView(R.id.owner_head);
        tv_owner_name = findView(R.id.tv_owner_name);

        if (showNormalList) {
            layout_normal.setVisibility(View.VISIBLE);
            add_manage_layout.setVisibility(View.GONE);
            toolbarView.setVisibility(View.GONE);
        } else {
            layout_normal.setVisibility(View.GONE);
            add_manage_layout.setVisibility(View.VISIBLE);
            toolbarView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupDate();
    }

    private void notifyDataSetChanged() {
        teamManagerAdapter.setGroupUsers(adminList);
        teamManagerAdapter.notifyDataSetChanged();

        teamManagerAdapter_normal.setGroupUsers(normalList);
        teamManagerAdapter_normal.notifyDataSetChanged();

    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GROUP_DETAIL:
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);
                userBeanList = waHuHighGroup.getHighGroups();
                processData();
                notifyDataSetChanged();
                break;
            case RequestCommandCode.REMOVE_ADMIN:
                ToastHelper.showToast(this, "移出成功");
                getGroupDate();
            case RequestCommandCode.CHANGE_OWNER:
                ToastHelper.showToast(this, "转移成功");
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

    @Override
    public void onItemClick(GroupDetailBean.GroupUser user) {
        if (showNormalList) {
            EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(AdvancedTeamManageListActivity.this, "转移群",
                    "确认将群转移给" + user.getUsername() + "?", true,
                    new EasyAlertDialogHelper.OnDialogActionListener() {

                        @Override
                        public void doCancelAction() {

                        }

                        @Override
                        public void doOkAction() {
                            DialogMaker.showProgressDialog(AdvancedTeamManageListActivity.this, "处理中...");
                            BaseRequestBean baseRequestBean = new BaseRequestBean();
                            baseRequestBean.addParams("groupId", waHuHighGroup.getId() + "");
                            baseRequestBean.addParams("userId", user.getAccid());
                            HttpClient.changeOwner(baseRequestBean, AdvancedTeamManageListActivity.this, RequestCommandCode.CHANGE_OWNER);
                        }
                    });

            dialog.show();
        }
    }

    @Override
    public void onDeleteClick(int position) {
        DialogMaker.showProgressDialog(this, "处理中...");
        HttpClient.removeAdmin(gId, adminList.get(position).getUserId() + "", this, RequestCommandCode.REMOVE_ADMIN);
    }

    @Override
    public void onMuteClick(GroupDetailBean.GroupUser user) {

    }


    private void processData() {
        adminList.clear();
        normalList.clear();
        if (userBeanList == null) return;
        for (int i = 0; i < userBeanList.size(); i++) {
            if (userBeanList.get(i).getCurrentUserIdentity().equals("群主")) {
                owner = userBeanList.get(i);
            } else if (userBeanList.get(i).getCurrentUserIdentity().equals("管理员")) {
                adminList.add(userBeanList.get(i));
            } else {
                normalList.add(userBeanList.get(i));
            }
        }
        if (owner == null) return;
        owner_head.loadImgForUrl(owner.getHeadImage());
        tv_owner_name.setText(owner.getUsername());
    }
}
