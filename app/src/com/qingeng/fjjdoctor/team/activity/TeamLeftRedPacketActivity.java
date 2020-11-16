package com.qingeng.fjjdoctor.team.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.RedPacketBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.redpacket.RedPacketDetailActivity;
import com.qingeng.fjjdoctor.team.adapter.TeamLeftRedPacketAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索加入群组界面
 * Created by hzxuwen on 2015/3/20.
 */
public class TeamLeftRedPacketActivity extends UI implements HttpInterface, TeamLeftRedPacketAdapter.OnClickLinsenter {

    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String LIST_TYPE = "LIST_TYPE";

    TeamLeftRedPacketAdapter teamLeftRedPacketAdapter;

    private String teamId;
    private RecyclerView recyclerView;

    private List<RedPacketBean> redPacketBeans = new ArrayList<>();


    private boolean msgLoaded = false;

    private View emptyBg;

    public static void start(Context context, String tid) {
        Intent intent = new Intent();
        intent.setClass(context, TeamLeftRedPacketActivity.class);
        intent.putExtra(EXTRA_ID, tid);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.netease.nim.uikit.R.layout.activity_group_normal_list);
        parseIntentData();

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "长时间未领取红包";
        setToolBar(com.netease.nim.uikit.R.id.toolbar, options);

        findViews();
        getGroupDate();
    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }

    private void findViews() {
        teamLeftRedPacketAdapter = new TeamLeftRedPacketAdapter(this);
        teamLeftRedPacketAdapter.setOnClickLinsenter(this);
        recyclerView = findView(com.netease.nim.uikit.R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(teamLeftRedPacketAdapter);
        emptyBg = findView(com.netease.nim.uikit.R.id.emptyBg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGroupDate();
    }

    private void notifyDataSetChanged() {
        teamLeftRedPacketAdapter.notifyDataSetChanged();
        boolean empty = redPacketBeans.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GROUP_DETAIL:
                redPacketBeans = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), RedPacketBean.class);
                teamLeftRedPacketAdapter.setRedPacketBeans(redPacketBeans);
                teamLeftRedPacketAdapter.notifyDataSetChanged();
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
        baseRequestBean.addParams("groupId", teamId);
        HttpClient.queryNoneAcceptRedPacket(baseRequestBean, this, RequestCommandCode.GROUP_DETAIL);

    }

    @Override
    public void onItemClick(RedPacketBean redPacketBean) {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("serialNumber", redPacketBean.getSerialNumber());
        baseRequestBean.addParams("requestId", redPacketBean.getRequestId());
        HttpClient.grabPacket(baseRequestBean, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(TeamLeftRedPacketActivity.this, message);
            }

            @Override
            public void onComplete() {
                RedPacketDetailActivity.start(TeamLeftRedPacketActivity.this, redPacketBean.getSerialNumber(), redPacketBean.getRequestId());
            }
        }, 1);
    }

}
