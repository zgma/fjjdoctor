package com.qingeng.fjjdoctor.user.appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.DateUtils;
import com.qingeng.fjjdoctor.util.UiUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyAppointmentDateActivity extends UI implements LSettingItem.OnLSettingItemClick, HttpInterface, CalendarView.OnCalendarMultiSelectListener,
        CalendarView.OnCalendarSelectListener {

    private final int TIME_SET_TYPE_AM = 1;
    private final int TIME_SET_TYPE_PM = 2;

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    @BindView(R.id.item_set_am)
    LSettingItem item_set_am;
    @BindView(R.id.item_set_pm)
    LSettingItem item_set_pm;
    @BindView(R.id.btn_save)
    Button btn_save;

    private List<AppointmentDayBean> selectedDayList = new ArrayList<>();
    private List<String> selectedDay = new ArrayList<>();
    private Map<String, AppointmentDayBean> selectedDayServerMap = new HashMap<>();

    public static void start(Context context) {
        Intent intent = new Intent(context, MyAppointmentDateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_appointment_date);
        ButterKnife.bind(this);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "设置可预约时间";
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
        item_set_am.setmOnLSettingItemClick(this);
        item_set_pm.setmOnLSettingItemClick(this);

        mCalendarView.setOnCalendarMultiSelectListener(this);
        mCalendarView.setOnCalendarSelectListener(this);

        btn_save.setOnClickListener(v -> {
            if (DateUtils.compareDate("00:00", amDateStr_start) > 0
                    || DateUtils.compareDate(amDateStr_start, "12:00") > 0
                    || DateUtils.compareDate("00:00", amDateStr_end) > 0
                    || DateUtils.compareDate(amDateStr_end, "12:00") > 0

                    || DateUtils.compareDate("12:00", pmDateStr_start) > 0
                    || DateUtils.compareDate(pmDateStr_start, "23:59") > 0
                    || DateUtils.compareDate("12:00", pmDateStr_end) > 0
                    || DateUtils.compareDate(pmDateStr_end, "23:59") > 0
            ) {
                ToastHelper.showToast(this, "上下午时间请设置合理");
                return;
            }


            if (DateUtils.compareDate(amDateStr_start, amDateStr_end) > 0) {
                ToastHelper.showToast(this, "上午开始时间 不可比 上午结束时间 大");
                return;
            }
            if (DateUtils.compareDate(pmDateStr_start, pmDateStr_end) > 0) {
                ToastHelper.showToast(this, "下午开始时间 不可比 下午结束时间 大");
                return;
            }
            selectedDayList.clear();
            List<com.haibin.calendarview.Calendar> list = mCalendarView.getMultiSelectCalendars();

            for (int i = 0; i < list.size(); i++) {
                AppointmentDayBean appointmentDay = new AppointmentDayBean();
                appointmentDay.setOrderDate(getDateStrByCalendar(list.get(i)));
                appointmentDay.setMina(amDateStr_start);
                appointmentDay.setMaxa(amDateStr_end);
                appointmentDay.setMinp(pmDateStr_start);
                appointmentDay.setMaxp(pmDateStr_end);
                appointmentDay.setAm(DateUtils.getHours(amDateStr_start, amDateStr_end, DateUtils.HHMM) > 0);
                appointmentDay.setPm(DateUtils.getHours(pmDateStr_start, pmDateStr_end, DateUtils.HHMM) > 0);
                selectedDayList.add(appointmentDay);
            }

            System.out.println("===================" + JSON.toJSONString(selectedDayList));
            DialogMaker.showProgressDialog(this, "提交中...");
            doctorSetSelfOrderTime(JSON.toJSONString(selectedDayList));
        });
        showDateStr();
        getVisitOrderTime();
    }

    Date ap_date_start = new Date();
    Date ap_date_end = new Date();
    String amDateStr_start = "08:00";
    String amDateStr_end = "12:00";

    Date pm_date_start = new Date();
    Date pm_date_end = new Date();
    String pmDateStr_start = "14:00";
    String pmDateStr_end = "18:00";

    int startFlag = 0;
    private Calendar calendar = Calendar.getInstance();
    Calendar rangDateStart = Calendar.getInstance();
    Calendar rangDateEnd = Calendar.getInstance();


    private void showTimePicker(int type) {
        getRangDate(type);
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (startFlag == 0) {
                    if (type == TIME_SET_TYPE_AM) {
                        ap_date_start = date;
                        amDateStr_start = DateUtils.getDateString(ap_date_start, DateUtils.HHMM);
                    }
                    if (type == TIME_SET_TYPE_PM) {
                        pm_date_start = date;
                        pmDateStr_start = DateUtils.getDateString(pm_date_start, DateUtils.HHMM);
                    }
                    startFlag = 1;
                    showTimePicker(type);
                }

                if (startFlag == 1) {
                    if (type == TIME_SET_TYPE_AM) {
                        ap_date_end = date;
                        amDateStr_end = DateUtils.getDateString(ap_date_end, DateUtils.HHMM);
                        showDateStr();
                    }
                    if (type == TIME_SET_TYPE_PM) {
                        pm_date_end = date;
                        pmDateStr_end = DateUtils.getDateString(pm_date_end, DateUtils.HHMM);
                        showDateStr();
                    }
                }
            }
        }).setDividerColor(UiUtils.getColor(this, R.color.colorAccent))
                .setTextColorCenter(UiUtils.getColor(this, R.color.colorPrimary)) //设置选中项文字颜色
                .setLineSpacingMultiplier(2.0f)
                .setContentTextSize(20)
                .setSubmitColor(UiUtils.getColor(this, R.color.colorPrimary))
                .setCancelColor(UiUtils.getColor(this, R.color.colorPrimary))
                .setType(new boolean[]{false, false, false, true, true, false})
                .setRangDate(rangDateStart, rangDateEnd)
                .build();

        if (type == TIME_SET_TYPE_AM) {
            pvTime.setTitleText(startFlag == 0 ? "上午开始时间" : "上午结束时间");
            calendar.setTime(ap_date_start);
            pvTime.setDate(calendar);
        }
        if (type == TIME_SET_TYPE_PM) {
            pvTime.setTitleText(startFlag == 0 ? "下午开始时间" : "下午结束时间");
            calendar.setTime(pm_date_start);
            pvTime.setDate(calendar);
        }
        pvTime.show();
    }

    @Override
    public void click(int id, boolean isChecked) {
        switch (id) {
            case R.id.item_set_am:
                startFlag = 0;
                showTimePicker(TIME_SET_TYPE_AM);
                break;
            case R.id.item_set_pm:
                startFlag = 0;
                showTimePicker(TIME_SET_TYPE_PM);
                break;
        }
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case 10016:
                ToastHelper.showToast(this, "操作成功");
                break;
            case 10017:
                selectedDayServerMap.clear();
                JSONArray objects = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()));
                Map<String, com.haibin.calendarview.Calendar> map = new HashMap<>();
                for (int i = 0; i < objects.size(); i++) {
                    AppointmentDayBean appointmentDayBean = JSON.parseObject(objects.getJSONObject(i).getString("jsonRemake"), AppointmentDayBean.class);
                    selectedDayServerMap.put(appointmentDayBean.getOrderDate(), appointmentDayBean);
                    String[] arr = appointmentDayBean.getOrderDate().split("-");
                    map.put(getSchemeCalendar(arr[0], arr[1], arr[2], 0xFFbc13f0, "记").toString(),
                            getSchemeCalendar(arr[0], arr[1], arr[2], 0xFFbc13f0, "记"));
                }
                //此方法在巨大的数据量上不影响遍历性能，推荐使用
                mCalendarView.clearSchemeDate();
                mCalendarView.setSchemeDate(map);
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

    private void getRangDate(int type) {
        rangDateStart = Calendar.getInstance();
        rangDateEnd = Calendar.getInstance();
        if (type == TIME_SET_TYPE_AM) {
            rangDateStart.setTime(DateUtils.parseToDate("00:00", DateUtils.HHMM));
            rangDateEnd.setTime(DateUtils.parseToDate("12:00", DateUtils.HHMM));
        }
        if (type == TIME_SET_TYPE_PM) {
            rangDateStart.setTime(DateUtils.parseToDate("12:00", DateUtils.HHMM));
            rangDateEnd.setTime(DateUtils.parseToDate("23:59", DateUtils.HHMM));
        }
    }


    @Override
    public void onCalendarMultiSelectOutOfRange(com.haibin.calendarview.Calendar calendar) {

    }

    @Override
    public void onMultiSelectOutOfSize(com.haibin.calendarview.Calendar calendar, int maxSize) {

    }

    @Override
    public void onCalendarMultiSelect(com.haibin.calendarview.Calendar calendar, int curSize, int maxSize) {
/*        lastSelectedDay = new AppointmentDay();
        lastSelectedDay.setOrderDate(getDateStrByCalendar(calendar));
        lastSelectedCalendar = calendar;*/
        String dateStr = getDateStrByCalendar(calendar);
        if (selectedDayServerMap.containsKey(dateStr)) {
            amDateStr_start = selectedDayServerMap.get(dateStr).getMina();
            amDateStr_end = selectedDayServerMap.get(dateStr).getMaxa();
            pmDateStr_start = selectedDayServerMap.get(dateStr).getMinp();
            pmDateStr_end = selectedDayServerMap.get(dateStr).getMaxp();

            showDateStr();
        }
    }

    @Override
    public void onCalendarOutOfRange(com.haibin.calendarview.Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(com.haibin.calendarview.Calendar calendar, boolean isClick) {
        ToastHelper.showToast(this, calendar.getYear() + "日" + calendar.getMonth() + "月" + calendar.getDay() + "日");
    }

    private String getDateStrByCalendar(com.haibin.calendarview.Calendar calendar) {
        return calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
    }

    private void doctorSetSelfOrderTime(String data) {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("orderTimeInput", data);
        HttpClient.doctorSetSelfOrderTime(baseRequestBean, this, 10016);
    }

    private void getVisitOrderTime() {
        HttpClient.getVisitOrderTime(this, 10017);
    }

    private void showDateStr() {
        item_set_am.setRightText(amDateStr_start + " ~ " + amDateStr_end);
        item_set_pm.setRightText(pmDateStr_start + " ~ " + pmDateStr_end);
    }


}

