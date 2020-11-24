package com.qingeng.fjjdoctor.user.appointment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.haibin.calendarview.CalendarView;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.AppointmentRecordBean;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.AppointmentRecordAdapter;
import com.qingeng.fjjdoctor.adapter.OnListItemClickListener;
import com.qingeng.fjjdoctor.base.BaseFragment;
import com.qingeng.fjjdoctor.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class HospitalAppointmentFragment extends BaseFragment implements HttpInterface, CalendarView.OnCalendarMultiSelectListener,
        CalendarView.OnCalendarSelectListener, OnListItemClickListener,
        CalendarView.OnMonthChangeListener {

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;

    int currPage = 1;
    List<AppointmentRecordBean> dataList = new ArrayList<>();
    AppointmentRecordAdapter adapter;

    public static HospitalAppointmentFragment getInstance() {
        HospitalAppointmentFragment hospitalAppointmentFragment = new HospitalAppointmentFragment();
        return hospitalAppointmentFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_appointment;
    }

    @Override
    protected void initView() {
        mCalendarView.setOnCalendarMultiSelectListener(this);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnMonthChangeListener(this);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_list.setLayoutManager(linearLayoutManager2);

        adapter = new AppointmentRecordAdapter(getContext());
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

//        toolbarView.setText(getDateStrByCalendar(lastCalendar));

        getDataList(getDateStrByCalendar(lastCalendar));

 /*       btn_set.setOnClickListener(v -> {
            MyAppointmentDateActivity.start(getContext());
        });*/
        getVisitOrderTime();
    }

    @Override
    protected void loadData() {

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
        ToastHelper.showToast(getContext(), message);
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
        ToastHelper.showToast(getContext(), calendar.getYear() + "日" + calendar.getMonth() + "月" + calendar.getDay() + "日");
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
        //toolbarView.setText(year + "-" + (month < 10 ? "0" + month : month));
    }
}

