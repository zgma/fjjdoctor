package com.qingeng.fjjdoctor.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.PublishBean;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.PublishAdapter;
import com.qingeng.fjjdoctor.util.UiUtils;
import com.qingeng.fjjdoctor.zoom.HtmlContentActivity;
import com.qingeng.fjjdoctor.zoom.SendPublishActivity;

import java.util.ArrayList;
import java.util.List;

public class MyPublishListActivity extends UI implements HttpInterface, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener,PublishAdapter.Listener, PublishAdapter.MoreListener {


    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView rcl_list;
    private TextView load_more_text;
    private LinearLayout layout_top;


    List<PublishBean> publishBeans = new ArrayList<>();
    PublishAdapter publishAdapter;
    int currPage = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, MyPublishListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    public static void startActivityForResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, MyPublishListActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zoom_main);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "我的发布";
        setToolBar(R.id.toolbar, options);

        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        rcl_list = findViewById(R.id.rcl_list);
        load_more_text = findViewById(R.id.load_more_text);
        layout_top = findViewById(R.id.layout_top);
        layout_top.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        rcl_list.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator)rcl_list.getItemAnimator()).setSupportsChangeAnimations(false);

        rcl_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (UiUtils.isSlideToBottom(recyclerView) && publishBeans.size() >= MainConstant.PAGE_NUMBER) {
                    onLoadMoreData();
                }
            }
        });
        publishAdapter = new PublishAdapter(this);
        publishAdapter.setIsMy(true);
        publishAdapter.setMoreListener(this);
        publishAdapter.setListener(this);
        publishAdapter.setPublishBeans(publishBeans);
        rcl_list.setAdapter(publishAdapter);
        swipe_refresh_layout.setColorSchemeResources(R.color.colorPrimary);
        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setRefreshing(true);
        this.onRefresh();


    }

    @Override
    public void onRefresh() {
        currPage = 1;
        getListData();
    }


    private void onLoadMoreData() {
        currPage++;
        getListData();
        showload_more_text();
    }

    private void getListData() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("page", currPage);
        baseRequestBean.addParams("limit", MainConstant.PAGE_NUMBER);
        baseRequestBean.addParams("sidx", "create_date");
        baseRequestBean.addParams("order", "desc");
        HttpClient.getMyNewsList(baseRequestBean, this, RequestCommandCode.GET_MY_NEWS_LIST);
    }


    public void showload_more_text() {
        load_more_text.setVisibility(View.VISIBLE);
    }

    public void hideload_more_text() {
        load_more_text.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_MY_NEWS_LIST:
                List<PublishBean> beans = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), PublishBean.class);
                setData(beans);
                break;
            case RequestCommandCode.DELETE_MY_NEW:
                ToastHelper.showToast(this, "操作成功");
                this.onRefresh();
                break;
            case 1234:
                ToastHelper.showToast(this, "点赞成功");
                currBankCardBean.setLikeCount(currBankCardBean.getLikeCount() + 1);
//                publishAdapter.notifyDataSetChanged();
                publishAdapter.notifyItemRangeChanged(currPosition,currPosition==0?1:currPosition);

                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, message);
    }

    @Override
    public void onComplete() {
        swipe_refresh_layout.setRefreshing(false);
        hideload_more_text();
        DialogMaker.dismissProgressDialog();
    }

    public void setData(List<PublishBean> beans) {
        if (currPage == 1) {
            publishBeans.clear();
        }
        if (beans.size() > 0) {
            publishBeans.addAll(beans);
            publishAdapter.setPublishBeans(publishBeans);
            publishAdapter.notifyDataSetChanged();
        } else {
            if (currPage == 1) {
                publishBeans.clear();
                publishBeans.addAll(beans);
                publishAdapter.setPublishBeans(publishBeans);
                publishAdapter.notifyDataSetChanged();
            }
            if (currPage > 1) {
//                ToastHelper.showToast(this, "没有更多数据！");
                currPage--;
            }
        }
    }

    @Override
    public void onDelete(PublishBean bankCardBean) {
        final EasyAlertDialog alertDialog = new EasyAlertDialog(this);
        alertDialog.setMessage("确定删除？");
        alertDialog.addNegativeButton("取消", EasyAlertDialog.NO_TEXT_COLOR, EasyAlertDialog.NO_TEXT_SIZE,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.addPositiveButton("确定", EasyAlertDialog.NO_TEXT_COLOR, EasyAlertDialog.NO_TEXT_SIZE,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        deletePublish(bankCardBean);
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onRePublish(PublishBean bankCardBean) {
        final EasyAlertDialog alertDialog = new EasyAlertDialog(this);
        alertDialog.setMessage("确定重新上架？");
        alertDialog.addNegativeButton("取消", EasyAlertDialog.NO_TEXT_COLOR, EasyAlertDialog.NO_TEXT_SIZE,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.addPositiveButton("确定", EasyAlertDialog.NO_TEXT_COLOR, EasyAlertDialog.NO_TEXT_SIZE,
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        rePublish(bankCardBean);
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onChange(PublishBean bankCardBean) {
        SendPublishActivity.start(this, bankCardBean);
    }



    private void rePublish(PublishBean publishBean) {
        DialogMaker.showProgressDialog(this, "处理中...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("id", publishBean.getId());
        HttpClient.rePublishMyNew(baseRequestBean, this, RequestCommandCode.DELETE_MY_NEW);
    }


    private void deletePublish(PublishBean publishBean) {
        DialogMaker.showProgressDialog(this, "处理中...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("id", publishBean.getId());
        HttpClient.deleteMyNew(baseRequestBean, this, RequestCommandCode.DELETE_MY_NEW);
    }

    @Override
    public void onItemClick(PublishBean bankCardBean) {
        if (!TextUtils.isEmpty(bankCardBean.getUrl())){
            HtmlContentActivity.start(this, bankCardBean.getUrl());
        }
    }

    private PublishBean currBankCardBean;
    private int currPosition;


    @Override
    public void onLikeClick(int position,PublishBean publishBean) {
        currBankCardBean = publishBean;
        currPosition = position;
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("type", "1");
        baseRequestBean.addParams("id", publishBean.getId());
        HttpClient.likeActive(baseRequestBean, this, 1234);
    }
}
