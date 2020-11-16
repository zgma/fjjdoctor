package com.qingeng.fjjdoctor.user.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.OrderBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.MyOrderAdapter;
import com.qingeng.fjjdoctor.util.DateUtils;
import com.qingeng.fjjdoctor.util.UiUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyOrderListActivity extends UI implements HttpInterface, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView rcl_list;
    private TextView load_more_text;

    private TextView tv_time;


    List<OrderBean> orderBeans = new ArrayList<>();
    MyOrderAdapter orderAdapter;
    int currPage = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, MyOrderListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    public static void startActivityForResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, MyOrderListActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_order);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "账单";
        setToolBar(R.id.toolbar, options);

        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        rcl_list = findViewById(R.id.rcl_list);
        load_more_text = findViewById(R.id.load_more_text);
        tv_time = findViewById(R.id.tv_time);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        rcl_list.setLayoutManager(linearLayoutManager);

        tv_time.setOnClickListener(this);

       /* rcl_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (UiUtils.isSlideToBottom(recyclerView) && orderBeans.size() >= MainConstant.PAGE_NUMBER) {
                    onLoadMoreData();
                }
            }
        });*/
        orderAdapter = new MyOrderAdapter(this);
        orderAdapter.setOrderBeans(orderBeans);
        rcl_list.setAdapter(orderAdapter);
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
        HttpClient.getMyBill(baseRequestBean, this, RequestCommandCode.GET_MY_BILL);
    }


    public void showload_more_text() {
        load_more_text.setVisibility(View.VISIBLE);
    }

    public void hideload_more_text() {
        load_more_text.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_time:
                showTimePicker();
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_MY_BILL:
                List<OrderBean> beans = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), OrderBean.class);
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
        swipe_refresh_layout.setRefreshing(false);
        hideload_more_text();
        DialogMaker.dismissProgressDialog();
    }

    public void setData(List<OrderBean> beans) {
        if (currPage == 1) {
            orderBeans.clear();
        }
        if (beans.size() > 0) {
            orderBeans.addAll(beans);
            orderAdapter.setOrderBeans(orderBeans);
            orderAdapter.notifyDataSetChanged();
        } else {
            if (currPage == 1) {
                orderBeans.clear();
                orderBeans.addAll(beans);
                orderAdapter.setOrderBeans(orderBeans);
                orderAdapter.notifyDataSetChanged();
            }
            if (currPage > 1) {
//                ToastHelper.showToast(this, "没有更多数据！");
                currPage--;
            }
        }
    }


    Date selectedDate = new Date();
    String selectedDateStr = "";
    private Calendar calendar = Calendar.getInstance();

    private void showTimePicker() {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                selectedDate = date;
                selectedDateStr = DateUtils.getDateString(selectedDate,DateUtils.YYYYMM_CHINA);
                tv_time.setText(selectedDateStr);
                MyOrderListActivity.this.onRefresh();
            }
        }).setDividerColor(UiUtils.getColor(this, R.color.colorAccent))
                .setTextColorCenter(UiUtils.getColor(this, R.color.colorPrimary)) //设置选中项文字颜色
                .setLineSpacingMultiplier(2.0f)
                .setContentTextSize(20)
                .setType(new boolean[]{true, true, false, false, false, false})
                .setSubmitColor(UiUtils.getColor(this, R.color.colorPrimary))
                .setCancelColor(UiUtils.getColor(this, R.color.colorPrimary))
                .build();

        pvTime.setTitleText("查询时间");
        calendar.setTime(selectedDate);
        pvTime.setDate(calendar);
        pvTime.show();
    }


}
