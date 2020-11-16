package com.qingeng.fjjdoctor.notice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.qingeng.apilibrary.bean.NoticeBean;
import com.qingeng.apilibrary.bean.OrderBean;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.NoticeAdapter;
import com.qingeng.fjjdoctor.adapter.OnListItemClickListener;
import com.qingeng.fjjdoctor.user.consult.MyConsultRecordActivity;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
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

/**
 * 搜索加入群组界面
 * Created by hzxuwen on 2015/3/20.
 */
public class NoticeActivity extends UI implements HttpInterface, OnRefreshLoadMoreListener, OnListItemClickListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;


    int currPage = 1;
    List<NoticeBean> dataList = new ArrayList<>();
    NoticeAdapter adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, NoticeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_consult_record);
        ButterKnife.bind(this);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "系统通知";
        setToolBar(R.id.toolbar, options);
        initUI();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initUI() {


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_list.setLayoutManager(linearLayoutManager2);

        adapter = new NoticeAdapter(this);
        adapter.setOnListItemClickListener(this);
        rcv_list.setAdapter(adapter);

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        //设置 Header 为 Material风格
        mRefreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(false).setColorSchemeColors(UiUtils.getColor(this, R.color.colorPrimary)));
        mLoadingLayout.showEmpty();

        this.onRefresh(mRefreshLayout);

    }

    public void setData(List<NoticeBean> beans) {
        if (currPage == 1) {
            dataList.clear();
        }
        if (beans.size() > 0) {
            dataList.addAll(beans);
            adapter.setDataList(dataList);
            adapter.notifyDataSetChanged();
        } else {
            if (currPage == 1) {
                dataList.clear();
                dataList.addAll(beans);
                adapter.setDataList(dataList);
                adapter.notifyDataSetChanged();
            }
            if (currPage > 1) {
//                ToastHelper.showToast(getActivity(), "没有更多数据！");
                currPage--;
            }
        }
        showLoad();
    }

    private void showLoad() {
        if (dataList.size() == 0) {
            mLoadingLayout.showEmpty();
        } else {
            mLoadingLayout.showContent();
        }
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.SYSTEM_NOTICE:
                List<NoticeBean> dataList = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), NoticeBean.class);
                setData(dataList);
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


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        currPage++;
        getDataList();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currPage = 1;
        getDataList();
    }

    private void getDataList() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("pageNum", currPage);
        baseRequestBean.addParams("pageSize", MainConstant.PAGE_NUMBER);
        HttpClient.lookSystemNotice(baseRequestBean, this, RequestCommandCode.SYSTEM_NOTICE);
    }

    @Override
    public void onItemClick(int position, Object baseBean) {
        NoticeBean noticeBean = (NoticeBean) baseBean;
        HtmlContentActivity.start(this, noticeBean.getNoticeContent(), noticeBean.getNoticeTitle());
    }
}
