package com.netease.nim.uikit.business.team.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.team.adapter.TeamInviteConfirmAdapter;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupInviteBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * 高级群群资料页
 * Created by huangjun on 2015/3/17.
 */
public class AdvancedInviteConfirmActivity extends UI implements HttpInterface, TeamInviteConfirmAdapter.OnClickListener {

    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String LIST_TYPE = "LIST_TYPE";

    TeamInviteConfirmAdapter teamInviteConfirmAdapter;

    private String teamId;
    // view
    private RecyclerView recyclerView;

    private List<GroupInviteBean> groupInviteBeans = new ArrayList<>();

    private boolean msgLoaded = false;

    private View emptyBg;

    public static void start(Context context, String tid) {
        Intent intent = new Intent();
        intent.setClass(context, AdvancedInviteConfirmActivity.class);
        intent.putExtra(EXTRA_ID, tid);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_normal_list);
        parseIntentData();

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "群邀请确认";
        setToolBar(R.id.toolbar, options);

        findViews();
        getGroupDate();
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }

    private void findViews() {
        teamInviteConfirmAdapter = new TeamInviteConfirmAdapter(this);
        teamInviteConfirmAdapter.setInviteBeans(groupInviteBeans);
        teamInviteConfirmAdapter.setOnClickListener(this);
        recyclerView = findView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teamInviteConfirmAdapter);
        emptyBg = findView(R.id.emptyBg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupDate();
    }

    private void notifyDataSetChanged() {
        teamInviteConfirmAdapter.setInviteBeans(groupInviteBeans);
        teamInviteConfirmAdapter.notifyDataSetChanged();
        boolean empty = groupInviteBeans.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GROUP_DETAIL:
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                groupInviteBeans = JSON.parseArray(jsonObject.getJSONArray("list").toJSONString(), GroupInviteBean.class);
                notifyDataSetChanged();
                break;
            case 1001:
                ToastHelper.showToast(this, "操作成功");
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
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("page", 1);
        baseRequestBean.addParams("limit", 5000);
        baseRequestBean.addParams("sidx", "create_date");
        baseRequestBean.addParams("order", "desc");
        baseRequestBean.addParams("groupId", teamId);
        HttpClient.pendingUserList(baseRequestBean, this, RequestCommandCode.GROUP_DETAIL);
    }


    @Override
    public void onOperator(GroupInviteBean groupInviteBean, int status) {
        if (status == 1) {
            DialogMaker.showProgressDialog(AdvancedInviteConfirmActivity.this, "处理中", true);
            handleInvite(groupInviteBean, status);
            return;
        }
        String title = "处理邀请";
        String content = "确定拒绝？";
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, title,
                content, true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(AdvancedInviteConfirmActivity.this, "", true);
                        handleInvite(groupInviteBean, status);
                    }
                });
        dialog.show();
    }


    private void handleInvite(GroupInviteBean groupInviteBean, int status) {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("groupId", groupInviteBean.getGroupId());
        baseRequestBean.addParams("userId", groupInviteBean.getUserId());
        baseRequestBean.addParams("id", groupInviteBean.getId());
        baseRequestBean.addParams("msg", groupInviteBean.getMsg());
        baseRequestBean.addParams("type", status);
        HttpClient.handlePending(baseRequestBean, this, 1001);
    }
}
