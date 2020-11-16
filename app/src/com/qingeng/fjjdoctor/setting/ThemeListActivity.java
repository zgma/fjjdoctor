package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.ThemeBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.ThemeAdapter;
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
public class ThemeListActivity extends UI implements HttpInterface, ThemeAdapter.Listener {

    private static final String TAG = "SystemMessageActivity2";

    // view
    // view
    private RecyclerView recyclerView;

    private ThemeAdapter themeAdapter;

    List<ThemeBean> themeBeanList;
    ThemeBean themeBean;

    private View emptyBg;


    private int myThemeId = 0;


    public static void start(Context context) {
        start(context, null, true);
    }

    public static void start(Context context, Intent extras, boolean clearTop) {
        Intent intent = new Intent();
        intent.setClass(context, ThemeListActivity.class);
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
        options.titleString = "主题";
        setToolBar(R.id.toolbar, options);
        initAdapter();
        loadMessages(); // load old data

       // myThemeId = Preferences.getUserBean()==null?1:Preferences.getUserBean().getThemeId();
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

        recyclerView = findView(R.id.recycler_view);
        emptyBg = findView(R.id.emptyBg);

        themeAdapter = new ThemeAdapter(this);
        themeAdapter.setListener(this);
        themeAdapter.setMySetId(myThemeId);

        recyclerView.setAdapter(themeAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * 加载历史消息
     */
    public void loadMessages() {
        DialogMaker.showProgressDialog(this, "加载中...");
        HttpClient.themeList(this, RequestCommandCode.THEME_LIST);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode){
            case RequestCommandCode.THEME_LIST:
                themeBeanList = JSONArray.parseArray(JSON.toJSONString(baseResponseData.getData()),ThemeBean.class);
                if (themeBeanList!=null){
                    refresh();
                }
                break;
            case RequestCommandCode.SET_THEME:
                ToastHelper.showToast(this, "设置成功");
                myThemeId = themeBean.getId();
                refresh();
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

        themeAdapter.setMySetId(myThemeId);
        themeAdapter.setThemeBeanList(themeBeanList);
        themeAdapter.notifyDataSetChanged();
        boolean empty = themeBeanList.isEmpty();
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }




    @Override
    public void onClick(ThemeBean themeBean) {
        this.themeBean= themeBean;
        DialogMaker.showProgressDialog(this, "设置中...");
        HttpClient.setTheme(themeBean.getId(),this, RequestCommandCode.SET_THEME);
    }
}
