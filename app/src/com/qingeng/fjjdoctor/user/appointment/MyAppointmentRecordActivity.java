package com.qingeng.fjjdoctor.user.appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.haibin.calendarview.CalendarView;
import com.leon.lib.settingview.LSettingItem;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.AppointmentDayBean;
import com.qingeng.apilibrary.bean.AppointmentRecordBean;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.AppointmentRecordAdapter;
import com.qingeng.fjjdoctor.adapter.OnListItemClickListener;
import com.qingeng.fjjdoctor.util.DateUtils;
import com.qingeng.fjjdoctor.util.UiUtils;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ezy.ui.layout.LoadingLayout;


public class MyAppointmentRecordActivity extends UI implements HttpInterface, CalendarView.OnCalendarMultiSelectListener,
        CalendarView.OnCalendarSelectListener, OnListItemClickListener,
        CalendarView.OnMonthChangeListener {

    TextView toolbarView;

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;


    @BindView(R.id.btn_set)
    Button btn_set;

    int currPage = 1;
    List<AppointmentRecordBean> dataList = new ArrayList<>();
    AppointmentRecordAdapter adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, MyAppointmentRecordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment_record);
        ButterKnife.bind(this);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "出诊预约情况";
        setToolBar(R.id.toolbar, options);

        toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText("");
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

        mCalendarView.setOnCalendarMultiSelectListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_list.setLayoutManager(linearLayoutManager2);

        adapter = new AppointmentRecordAdapter(this);
        adapter.setOnListItemClickListener(this);
        rcv_list.setAdapter(adapter);

/*        mRefreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        //设置 Header 为 Material风格
        mRefreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(false).setColorSchemeColors(UiUtils.getColor(this, R.color.colorPrimary)));
        mLoadingLayout.showEmpty();

        this.onRefresh(mRefreshLayout);*/

        String[] dayStrArr = DateUtils.getTimeString(DateUtils.YYYYMMDD).split("-");
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(Integer.parseInt(dayStrArr[0]));
        calendar.setMonth(Integer.parseInt(dayStrArr[1]));
        calendar.setDay(Integer.parseInt(dayStrArr[2]));

        lastCalendar = calendar;
        mCalendarView.putMultiSelect(lastCalendar);

        toolbarView.setText(getDateStrByCalendar(lastCalendar));

        getDataList(getDateStrByCalendar(lastCalendar));

        btn_set.setOnClickListener(v -> {
            MyAppointmentDateActivity.start(this);
        });
        getVisitOrderTime();
    }

    public void setData(List<AppointmentRecordBean> beans) {
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
/*        if (dataList.size() == 0) {
            mLoadingLayout.showEmpty();
        } else {
            mLoadingLayout.showContent();
        }*/
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case 10016:
                List<AppointmentRecordBean> dataList = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), AppointmentRecordBean.class);
                setData(dataList);
                break;
            case 10017:

                break;
        }
    }

    private com.haibin.calendarview.Calendar getSchemeCalendar(String year, String month, String day, int color, String text) {
        com.haibin.calendarview.Calendar calendar = new com.haibin.calendarview.Calendar();
        calendar.setYear(Integer.parseInt(year));
        calendar.setMonth(Integer.parseInt(month));
        calendar.setDay(Integer.parseInt(day));
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        calendar.addScheme(new com.haibin.calendarview.Calendar.Scheme());
        calendar.addScheme(0xFF008800, "假");
        calendar.addScheme(0xFF008800, "节");
        return calendar;
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }


    @Override
    public void onCalendarMultiSelectOutOfRange(com.haibin.calendarview.Calendar calendar) {

    }

    @Override
    public void onMultiSelectOutOfSize(com.haibin.calendarview.Calendar calendar, int maxSize) {

    }

    com.haibin.calendarview.Calendar lastCalendar;

    @Override
    public void onCalendarMultiSelect(com.haibin.calendarview.Calendar calendar, int curSize, int maxSize) {
        if (lastCalendar != null) mCalendarView.removeMultiSelect(lastCalendar);
        lastCalendar = calendar;
        getDataList(getDateStrByCalendar(calendar));

    }

    @Override
    public void onCalendarOutOfRange(com.haibin.calendarview.Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(com.haibin.calendarview.Calendar calendar, boolean isClick) {
        ToastHelper.showToast(this, calendar.getYear() + "日" + calendar.getMonth() + "月" + calendar.getDay() + "日");
    }

    private String getDateStrByCalendar(com.haibin.calendarview.Calendar calendar) {
        return calendar.getYear() + "-" + calendar.getMonth() + "-" + (calendar.getDay() < 10 ? "0" + calendar.getDay() : calendar.getDay());
    }


    private void getVisitOrderTime() {
        HttpClient.getVisitOrderTime(this, 10017);
    }


    private void getDataList(String dateStr) {
        currPage = 1;
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("dateStr", dateStr);
        HttpClient.findReviewList(baseRequestBean, this, 10016);
    }

    @Override
    public void onItemClick(int position, Object baseBean) {

    }

    @Override
    public void onMonthChange(int year, int month) {
        toolbarView.setText(year + "-" + (month < 10 ? "0" + month : month));
    }
}

