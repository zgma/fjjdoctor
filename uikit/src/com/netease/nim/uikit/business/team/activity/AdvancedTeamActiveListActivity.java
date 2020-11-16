package com.netease.nim.uikit.business.team.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.team.adapter.TeamActiveAdapter;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * 高级群群资料页
 * Created by huangjun on 2015/3/17.
 */
public class AdvancedTeamActiveListActivity extends UI implements HttpInterface, TeamActiveAdapter.OnClickLinsenter {

    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String LIST_TYPE = "LIST_TYPE";

    TeamActiveAdapter teamActiveAdapter;

    private String teamId;
    private int groupId;
    // view
    private RecyclerView recyclerView;

    private List<GroupDetailBean.GroupUser> userBeanList = new ArrayList<>();
    private List<GroupDetailBean.GroupUser> normalList = new ArrayList<>();


    private boolean msgLoaded = false;

    //0 活跃度  1退群  2禁止领取红包
    private int listType = 0;

    private View emptyBg;

    public static void start(Context context, String tid) {
        Intent intent = new Intent();
        intent.setClass(context, AdvancedTeamActiveListActivity.class);
        intent.putExtra(EXTRA_ID, tid);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    public static void start(Context context, String tid,int listType) {
        Intent intent = new Intent();
        intent.setClass(context, AdvancedTeamActiveListActivity.class);
        intent.putExtra(EXTRA_ID, tid);
        intent.putExtra(LIST_TYPE, listType);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_normal_list);
        parseIntentData();

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString =listType == 0?"群活跃度":listType == 1?"退群成员列表":"群禁止领取红包";
        setToolBar(R.id.toolbar, options);

        findViews();
        getGroupDate();
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
        listType = getIntent().getIntExtra(LIST_TYPE, 0);
    }

    private void findViews() {
        teamActiveAdapter = new TeamActiveAdapter(this);
        teamActiveAdapter.setGroupUsers(normalList);
        teamActiveAdapter.setListType(listType);
        teamActiveAdapter.setOnClickLinsenter(this);
        recyclerView = findView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teamActiveAdapter);
        emptyBg = findView(R.id.emptyBg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupDate();
    }

    private void notifyDataSetChanged() {
        teamActiveAdapter.notifyDataSetChanged();
        boolean empty = userBeanList.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GROUP_DETAIL:
                userBeanList.clear();
                if (listType == 2){

                    JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                    GroupDetailBean.WaHuHighGroup waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);
                    groupId = waHuHighGroup.getId();
                    userBeanList.addAll(waHuHighGroup.getHighGroups());
                }else {
                    JSONArray jsonArray = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()));
                    if (jsonArray == null) return;
                    for (int i = 0; i <jsonArray.size() ; i++) {
                        JSONArray jsonArray2 = JSON.parseArray(JSON.toJSONString(jsonArray.get(i)));
                        for (int j = 0; j <jsonArray2.size() ; j++) {
                            GroupDetailBean.GroupUser user = JSONObject.parseObject(JSON.toJSONString(jsonArray2.get(j)), GroupDetailBean.GroupUser.class);
                            userBeanList.add(user);
                        }
                    }
                }

                //processData();
                teamActiveAdapter.setGroupUsers(userBeanList);
                notifyDataSetChanged();
                break;
            case RequestCommandCode.ADD_FRIENDS:
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
        if (listType == 1){
            HttpClient.queryExitUserList(teamId, "",this, RequestCommandCode.GROUP_DETAIL);
        }else  if (listType == 0){
            HttpClient.getActiveUser(teamId, this, RequestCommandCode.GROUP_DETAIL);
        }else if (listType == 2){
            HttpClient.groupDetail(teamId, this, RequestCommandCode.GROUP_DETAIL);
        }
    }

    @Override
    public void onItemClick(GroupDetailBean.GroupUser user) {
        if (listType == 1) return;
        String title = listType==0?"重置活跃度":"禁止领取红包";
        String content = listType==0?"确定重置"+user.getUsername()+"的活跃？"
                :"确定"+(user.getBlack()==0?"禁止":"恢复")+user.getUsername()+"领取红包？";
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, title    ,
                content, true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(AdvancedTeamActiveListActivity.this, "", true);
                        if (listType == 0){
                            HttpClient.resetUserActive(teamId,user.getUserId()+"", AdvancedTeamActiveListActivity.this, RequestCommandCode.ADD_FRIENDS);
                        }else if (listType == 2){
                            HttpClient.add2BlackList(groupId+"",user.getUserId()+"", user.getBlack()==0?"1":"2",AdvancedTeamActiveListActivity.this, RequestCommandCode.ADD_FRIENDS);
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
