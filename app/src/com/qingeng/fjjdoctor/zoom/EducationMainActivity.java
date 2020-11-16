package com.qingeng.fjjdoctor.zoom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.EducationBean;
import com.qingeng.apilibrary.bean.ImageBean;
import com.qingeng.apilibrary.bean.PublishBean;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.EducationAdapter;
import com.qingeng.fjjdoctor.adapter.OnListItemClickListener;
import com.qingeng.fjjdoctor.adapter.PublishAdapter;
import com.qingeng.fjjdoctor.adapter.PublishImageAdapter;
import com.qingeng.fjjdoctor.notice.HtmlContentActivity;
import com.qingeng.fjjdoctor.util.UiUtils;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ezy.ui.layout.LoadingLayout;

public class EducationMainActivity extends UI implements HttpInterface, View.OnClickListener, OnRefreshLoadMoreListener, OnListItemClickListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;

    @BindView(R.id.edit_search_name)
    EditText edit_search_name;

    List<EducationBean> educationBeans = new ArrayList<>();
    EducationAdapter educationAdapter;
    int currPage = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, EducationMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    public static void startActivityForResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, EducationMainActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comm_list);
        ButterKnife.bind(this);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "宣教";
        setToolBar(R.id.toolbar, options);

        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText("新建");
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendEducationActivity.start(EducationMainActivity.this);
            }
        });

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_list.setLayoutManager(linearLayoutManager2);

        educationAdapter = new EducationAdapter(this);
        educationAdapter.setOnListItemClickListener(this);
        rcv_list.setAdapter(educationAdapter);

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        //设置 Header 为 Material风格
        mRefreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(false).setColorSchemeColors(UiUtils.getColor(this, R.color.colorPrimary)));
        mLoadingLayout.showEmpty();

        edit_search_name.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onRefresh(mRefreshLayout);
            }
            return false;
        });
        this.onRefresh(mRefreshLayout);

        String str2 = new String(Base64.decode("KipzeGhJNEM2TmE1dVBrRXlWQTU=".getBytes(), Base64.DEFAULT));
        System.out.println("code===" + str2);
    }


    private void getListData() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("pageNum", currPage);
        baseRequestBean.addParams("pageSize", MainConstant.PAGE_NUMBER);
        String name = edit_search_name.getText().toString().trim();
        if (!TextUtils.isEmpty(name)) baseRequestBean.addParams("searchWords", name);
        HttpClient.getPropEducationList(baseRequestBean, this, RequestCommandCode.GET_NEWS_LIST);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_NEWS_LIST:
                List<EducationBean> beans = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), EducationBean.class);
                setData(beans);
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
        mRefreshLayout.finishRefresh();
        mRefreshLayout.finishLoadMore();
    }

    public void setData(List<EducationBean> beans) {
        if (currPage == 1) {
            educationBeans.clear();
        }
        if (beans.size() > 0) {
            educationBeans.addAll(beans);
            educationAdapter.setDataList(educationBeans);
            educationAdapter.notifyDataSetChanged();
        } else {
            if (currPage == 1) {
                educationBeans.clear();
                educationBeans.addAll(beans);
                educationAdapter.setDataList(educationBeans);
                educationAdapter.notifyDataSetChanged();
            }
            if (currPage > 1) {
//                ToastHelper.showToast(this, "没有更多数据！");
                currPage--;
            }
        }

        showLoad();
    }

    private void showLoad() {
        if (educationBeans.size() == 0) {
            mLoadingLayout.showEmpty();
        } else {
            mLoadingLayout.showContent();
        }
    }


    private TextWatcher watcherInput = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() > 0) {
                //  clear_input.setVisibility(View.VISIBLE);
            } else {
                // clear_input.setVisibility(View.GONE);
            }
            edit_search_name.setSelection(edit_search_name.getText().length());//将光标移至文字末尾
        }
    };


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currPage++;
        getListData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currPage = 1;
        getListData();
    }

    @Override
    public void onItemClick(int position, Object baseBean) {
        HtmlContentActivity.start(this, ((EducationBean) baseBean).getPropTitle(), (((EducationBean) baseBean).getPropContent()));
    }


}
