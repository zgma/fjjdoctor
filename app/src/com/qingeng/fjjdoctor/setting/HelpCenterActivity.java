package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.HelpBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.HelpCenterAdapter;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;

import java.util.List;

/**
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class HelpCenterActivity extends UI implements HttpInterface, HelpCenterAdapter.Listener {

    private static final String TAG = "SystemMessageActivity2";

    // view
    // view
    private RecyclerView recyclerView;

    private HelpCenterAdapter helpCenterAdapter;

    List<HelpBean> helpBeans;

    private View emptyBg;



    public static void start(Context context) {
        start(context, null, true);
    }

    public static void start(Context context, Intent extras, boolean clearTop) {
        Intent intent = new Intent();
        intent.setClass(context, HelpCenterActivity.class);
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

        setContentView(R.layout.activity_help_center);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "帮助中心";
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
        helpCenterAdapter = new HelpCenterAdapter(this);
        helpCenterAdapter.setListener(this);

        recyclerView = findView(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(helpCenterAdapter);
        emptyBg = findView(R.id.emptyBg);
    }

    /**
     * 加载历史消息
     */
    public void loadMessages() {
        DialogMaker.showProgressDialog(this, "加载中...");
        HttpClient.helpCenter(this, RequestCommandCode.HELP_CENTER);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode){
            case RequestCommandCode.HELP_CENTER:
                helpBeans = JSONArray.parseArray(JSON.toJSONString(baseResponseData.getData()),HelpBean.class);
                if (helpBeans!=null){
                    refresh();
                }
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
        helpCenterAdapter.setHelpBeans(helpBeans);
        helpCenterAdapter.notifyDataSetChanged();
        boolean empty = helpBeans.isEmpty();
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }




    @Override
    public void onClick(HelpBean helpBean) {
        HelpContentActivity.start(this, helpBean.getAnswer(),helpBean.getTitle());
    }
}
