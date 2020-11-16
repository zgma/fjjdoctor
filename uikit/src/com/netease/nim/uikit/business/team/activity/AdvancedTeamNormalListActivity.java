package com.netease.nim.uikit.business.team.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

import java.util.ArrayList;
import java.util.List;

/**
 * 高级群群资料页
 * Created by huangjun on 2015/3/17.
 */
public class AdvancedTeamNormalListActivity extends UI implements HttpInterface, TeamManagerAdapter.OnClickLinsenter {

    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_IS_SET_MUTE = "EXTRA_IS_SET_MUTE";

    TeamManagerAdapter teamManagerAdapter;

    private String teamId;
    // view
    private RecyclerView recyclerView;

    private List<GroupDetailBean.GroupUser> userBeanList = new ArrayList<>();
    private List<GroupDetailBean.GroupUser> normalList = new ArrayList<>();
    private GroupDetailBean.WaHuHighGroup waHuHighGroup;


    private boolean msgLoaded = false;

    private boolean isSetMute = false;

    private View emptyBg;

    public static void start(Context context, String tid, boolean isSetMute) {
        Intent intent = new Intent();
        intent.setClass(context, AdvancedTeamNormalListActivity.class);
        intent.putExtra(EXTRA_ID, tid);
        intent.putExtra(EXTRA_IS_SET_MUTE, isSetMute);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_normal_list);
        parseIntentData();

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = isSetMute ? "设置群成员禁言" : "添加群管理";
        setToolBar(R.id.toolbar, options);

        findViews();
        getGroupDate();
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
        isSetMute = getIntent().getBooleanExtra(EXTRA_IS_SET_MUTE, false);
    }

    private void findViews() {
        teamManagerAdapter = new TeamManagerAdapter(this);
        teamManagerAdapter.setGroupUsers(normalList);
        teamManagerAdapter.setShowDeleteIcon(false);
        teamManagerAdapter.setShowMuteIcon(isSetMute);
        teamManagerAdapter.setOnClickLinsenter(this);
        recyclerView = findView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teamManagerAdapter);
        emptyBg = findView(R.id.emptyBg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupDate();
    }

    private void notifyDataSetChanged() {
        teamManagerAdapter.notifyDataSetChanged();
        boolean empty = userBeanList.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GROUP_DETAIL:
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);
                userBeanList = waHuHighGroup.getHighGroups();
                processData();
                teamManagerAdapter.setGroupUsers(normalList);
                notifyDataSetChanged();
                break;
            case RequestCommandCode.SET_ADMIN:
                ToastHelper.showToast(this, "设置成功");
                getGroupDate();
            case RequestCommandCode.FORBIDDEN_USER:
            case RequestCommandCode.CANCEL_MUTE:
                ToastHelper.showToast(this, "设置成功");
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

    @Override
    public void onItemClick(GroupDetailBean.GroupUser user) {
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(AdvancedTeamNormalListActivity.this, "提示",
                "确定添加" + user.getUsername() + "为管理员？", true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(AdvancedTeamNormalListActivity.this, "处理中...");
                        HttpClient.setAdmin(waHuHighGroup.getId() + "", user.getUserId() + "", AdvancedTeamNormalListActivity.this, RequestCommandCode.SET_ADMIN);
                    }
                });
        if (!isFinishing() && !isDestroyedCompatible()) {
            dialog.show();
        }
    }

    @Override
    public void onDeleteClick(int position) {

    }

    @Override
    public void onMuteClick(GroupDetailBean.GroupUser user) {
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(AdvancedTeamNormalListActivity.this, "提示",
                "确定将" + user.getUsername() + (user.getMuteStatus().equals("禁言")?"取消禁言？":"禁言？"), true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(AdvancedTeamNormalListActivity.this, "处理中...");
                        if (user.getMuteStatus().equals("禁言")){
                            BaseRequestBean baseRequestBean = new BaseRequestBean();
                            baseRequestBean.addParams("type", "2");
                            baseRequestBean.addParams("groupId", waHuHighGroup.getId() + "");
                            baseRequestBean.addParams("userId", user.getUserId()+"");
                            HttpClient.cancelMute(baseRequestBean, AdvancedTeamNormalListActivity.this, RequestCommandCode.CANCEL_MUTE);

                        }else {
                            BaseRequestBean baseRequestBean = new BaseRequestBean();
                            baseRequestBean.addParams("type", "2");
                            baseRequestBean.addParams("groupId", waHuHighGroup.getId() + "");
                            baseRequestBean.addParams("time", "999999999");
                            baseRequestBean.addParams("userId", user.getUserId()+"");
                            HttpClient.forbiddenUser(baseRequestBean, AdvancedTeamNormalListActivity.this, RequestCommandCode.FORBIDDEN_USER);
                        }
                      }
                });
        dialog.show();
    }


    private void processData() {
        normalList.clear();
        if (userBeanList == null) return;
        for (int i = 0; i < userBeanList.size(); i++) {
            if (userBeanList.get(i).getCurrentUserIdentity().equals("普通成员")) {
                normalList.add(userBeanList.get(i));
            }
        }
    }
}
