package com.qingeng.fjjdoctor.user.wallet;

import android.app.Activity;
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
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.qingeng.apilibrary.bean.BankCardBean;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.BankCardAdapter;
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
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class MyBankCardListActivity extends UI implements HttpInterface, OnRefreshLoadMoreListener, BankCardAdapter.Listener {



    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;


    int currPage = 1;
    List<BankCardBean> dataList = new ArrayList<>();
    BankCardAdapter adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, MyBankCardListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void startForResult(Context context, int type, int requestCode) {
        Intent intent = new Intent(context, MyBankCardListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("TYPE", type);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_consult_record);
        ButterKnife.bind(this);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "银行卡";
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

        adapter = new BankCardAdapter(this);
        adapter.setListener(this);
        rcv_list.setAdapter(adapter);

        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        //设置 Header 为 Material风格
        mRefreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(false).setColorSchemeColors(UiUtils.getColor(this, R.color.colorPrimary)));
        mLoadingLayout.showEmpty();
        this.onRefresh(mRefreshLayout);

    }

    public void setData(List<BankCardBean> beans) {
        if (currPage == 1) {
            dataList.clear();
        }
        if (beans.size() > 0) {
            dataList.addAll(beans);
            adapter.setBankCardBeans(dataList);
            adapter.notifyDataSetChanged();
        } else {
            if (currPage == 1) {
                dataList.clear();
                dataList.addAll(beans);
                adapter.setBankCardBeans(dataList);
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
            case 100016:
                List<BankCardBean> dataList = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), BankCardBean.class);
                dataList.add(new BankCardBean());
                setData(dataList);
                break;
            case 10087:
                ToastHelper.showToast(this, "删除成功");
                this.onRefresh(mRefreshLayout);
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
        currPage = 1;
        getDataList();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        currPage = 1;
        getDataList();
    }

    private void getDataList() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpClient.selectUserBankList(baseRequestBean, this, 100016);
    }


    @Override
    public void onItemClick(BankCardBean bankCardBean) {
        Intent intent = new Intent();
        intent.putExtra("data", bankCardBean);
        //设置返回数据
        this.setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void addCard() {
        AddBankCardActivity.start(this);
    }

    @Override
    public void deleteCard(BankCardBean bankCardBean) {
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(this, "提示",
                "确定删除该银行卡？", true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }

                    @Override
                    public void doOkAction() {
                        DialogMaker.showProgressDialog(MyBankCardListActivity.this, "删除中...");
                        HttpClient.delBankCard(bankCardBean.getId(), MainConstant.USER_TYPE, MyBankCardListActivity.this, 10087);
                    }
                });
        dialog.show();
    }
}
