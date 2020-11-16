package com.qingeng.fjjdoctor.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupBean;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.GroupAdapter;
import com.qingeng.fjjdoctor.session.SessionHelper;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.team.activity.AdvancedTeamUpgradeActivity;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class MyGroupActivity extends UI implements GroupAdapter.Listener, HttpInterface {

    private static final String TAG = "SystemMessageActivity2";

    // view
    // view
    private RecyclerView recyclerView;

    private View emptyBg;
    private boolean msgLoaded = false;
    // adapter
    private GroupAdapter adapter;
    private List<GroupBean> items = new ArrayList<>();

    public static void start(Context context) {
        start(context, null, true);
    }

    public static void start(Context context, Intent extras, boolean clearTop) {
        Intent intent = new Intent();
        intent.setClass(context, MyGroupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (clearTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_apply_list);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.my_group;
        setToolBar(R.id.toolbar, options);
        initAdapter();
        loadMessages(); // load old data
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void initAdapter() {
        adapter = new GroupAdapter(this);
        adapter.setListener(this);
        adapter.setGroupBeans(items);


        recyclerView = findView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        emptyBg = findView(R.id.emptyBg);
    }

    /**
     * 加载历史消息
     */
    public void loadMessages() {
        HttpClient.queryMyGroupList(this, RequestCommandCode.MY_GROUP_LIST);
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode){
            case RequestCommandCode.MY_GROUP_LIST:
                items.clear();
                items = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), GroupBean.class);
                refresh();
                break;
            case RequestCommandCode.HANDLE_FRIEND_APPLY:
                ToastHelper.showToast(this, baseResponseData.getMsg());
                loadMessages(); // load old data
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, message);
    }

    @Override
    public void onComplete() {

    }

    private void refresh() {
        adapter.setGroupBeans(items);
        adapter.notifyDataSetChanged();
        boolean empty = items.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(GroupBean groupBean) {

        HttpClient.groupDetail(groupBean.getTid(), new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                GroupDetailBean.GroupUser groupUser = JSONObject.parseObject(jsonObject.get("waUserHighGroup").toString(), GroupDetailBean.GroupUser.class);
                GroupDetailBean.WaHuHighGroup waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);

                if (groupUser.getCurrentUserIdentity().equals("未入群")){
                    EasyAlertDialogHelper.showOneButtonDiolag(MyGroupActivity.this, "", "已退出群聊",
                            getString(com.netease.nim.uikit.R.string.ok),
                            true, null);
                    return;
                }

                if (groupUser.getGroupStatus().equals("正常")) {
                    SessionHelper.startTeamSession(MyGroupActivity.this,  groupBean.getTid());
                } else {
                    if (groupUser.getGroupStatus().equals("到期")) {
                        groupExpireMessage(groupUser,  groupBean.getTid());
                    } else {
                        groupStatusError(groupUser.getGroupStatus());
                    }
                }

            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(MyGroupActivity.this, message);
            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.GROUP_DETAIL);


       // SessionHelper.startTeamSession(this,groupBean.getTid());
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
                            AdvancedTeamUpgradeActivity.start(MyGroupActivity.this, tId);
                        }
                    }).show();
        } else {
            EasyAlertDialogHelper.showOneButtonDiolag(MyGroupActivity.this, "", "该高级群已到期，请联系群主续费",
                    getString(com.netease.nim.uikit.R.string.ok),
                    true, null);
        }
    }

    private void groupStatusError(String errorStatusStr) {
        EasyAlertDialogHelper.showOneButtonDiolag(MyGroupActivity.this, "", "该群已"+errorStatusStr,
                getString(com.netease.nim.uikit.R.string.ok),
                true, null);
    }
}
