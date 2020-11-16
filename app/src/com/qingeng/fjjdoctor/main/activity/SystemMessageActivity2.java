package com.qingeng.fjjdoctor.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.qingeng.apilibrary.bean.ApplyBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.ApplyAdapter;
import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.qingeng.fjjdoctor.session.activity.FriendApplyInfoActivity;
import com.qingeng.fjjdoctor.util.LocalDataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统消息中心界面
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class SystemMessageActivity2 extends UI implements ApplyAdapter.ApplyListener, HttpInterface {

    private static final String TAG = "SystemMessageActivity2";

    // view
    // view
    private RecyclerView recyclerView;

    private View emptyBg;
    private boolean msgLoaded = false;
    // adapter
    private ApplyAdapter adapter;
    private List<ApplyBean> items = new ArrayList<>();

    public static void start(Context context) {
        start(context, null, true);
    }

    public static void start(Context context, Intent extras, boolean clearTop) {
        Intent intent = new Intent();
        intent.setClass(context, SystemMessageActivity2.class);
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
        options.titleId = R.string.verify_reminder;
        setToolBar(R.id.toolbar, options);
        initAdapter();
        loadMessages(); // load old data

        TextView toolbarView = findView(com.netease.nim.uikit.R.id.action_bar_right_clickable_textview);
        toolbarView.setText("清除记录");
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ids = "";
                if (items != null && items.size() > 0) {
                    for (int i = 0; i < items.size(); i++) {
                        ids = ids + "," + items.get(i).getId();
                    }
                    removeRecode(ids);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMessages(); // load old data
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notification_menu, menu);*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notification_menu_btn:
                deleteAllMessages();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initAdapter() {
        adapter = new ApplyAdapter(this);
        adapter.setListener(this);
        adapter.setApplyBeanList(items);

        recyclerView = findView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        emptyBg = findView(R.id.emptyBg);
    }

    /**
     * 加载历史消息
     */
    public void loadMessages() {
        HttpClient.queryFriendTodoList(this, RequestCommandCode.QUERY_FRIEND_TODO_LIST);
    }

    private ApplyBean currMessage;

    @Override
    public void onAgree(ApplyBean message) {
        currMessage = message;
        HttpClient.handleFriendApply(message.getId() + "", "3", this, 12138);
    }

    @Override
    public void onReject(ApplyBean message) {
        HttpClient.handleFriendApply(message.getId() + "", "4", this, RequestCommandCode.HANDLE_FRIEND_APPLY);
    }

    @Override
    public void onLongPressed(ApplyBean message) {
        removeRecode(message.getId() + "");
    }

    @Override
    public void onClick(ApplyBean message) {
        if (message.getType() == 3) {
            UserProfileActivity2.start(this, message.getUserId() + "");
        } else {
            FriendApplyInfoActivity.start(this, message);
        }
    }

    private void deleteAllMessages() {
        NIMClient.getService(SystemMessageService.class).clearSystemMessages();
        NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
        items.clear();
        ToastHelper.showToast(SystemMessageActivity2.this, R.string.clear_all_success);
    }

    private void showLongClickMenu(final SystemMessage message) {
        CustomAlertDialog alertDialog = new CustomAlertDialog(this);
        alertDialog.setTitle(R.string.delete_tip);
        String title = getString(R.string.delete_system_message);
        alertDialog.addItem(title, new CustomAlertDialog.onSeparateItemClickListener() {
            @Override
            public void onClick() {
                deleteSystemMessage(message);
            }
        });
        alertDialog.show();
    }

    private void deleteSystemMessage(final SystemMessage message) {
        NIMClient.getService(SystemMessageService.class).deleteSystemMessage(message.getMessageId());
        items.remove(message);
        ToastHelper.showToast(SystemMessageActivity2.this, R.string.delete_success);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.QUERY_FRIEND_TODO_LIST:
                items.clear();
                items = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), ApplyBean.class);
                refresh();
                break;
            case 12138:
                IMMessage msg = MessageBuilder.createTipMessage(currMessage.getFaccid(), SessionTypeEnum.P2P);
                msg.setContent(LocalDataUtils.getUserInfo().getNickName() + "通过好友验证");
                CustomMessageConfig config = new CustomMessageConfig();
                config.enablePush = true; // 不推送
                msg.setConfig(config);

                NIMClient.getService(MsgService.class).sendMessage(msg, false).setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {

                    }

                    @Override
                    public void onFailed(int code) {

                    }

                    @Override
                    public void onException(Throwable exception) {

                    }
                });
                ToastHelper.showToast(this, "操作成功");
                loadMessages(); // load old data
                break;
            case RequestCommandCode.HANDLE_FRIEND_APPLY:
            case RequestCommandCode.REMOVE_FRIEND_ADD_LIST:
                ToastHelper.showToast(this, "操作成功");
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
        DialogMaker.dismissProgressDialog();

    }

    private void refresh() {
        adapter.setApplyBeanList(items);
        adapter.notifyDataSetChanged();
        boolean empty = items.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }


    private void removeRecode(String id) {
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(SystemMessageActivity2.this, "提示",
                id.contains(",") ? "确定清除记录？" : "确定删除此记录？", true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(SystemMessageActivity2.this, "删除中...");
                        HttpClient.removeFriendAddList(id, SystemMessageActivity2.this, RequestCommandCode.REMOVE_FRIEND_ADD_LIST);
                    }
                });
        dialog.show();
    }

}
