package com.qingeng.fjjdoctor.user.appointment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.base.BaseViewPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MultipleAppointmentActivity extends UI {


    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static void start(Context context) {
        Intent intent = new Intent(context, MultipleAppointmentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_appointment);
        ButterKnife.bind(this);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "出诊预约";
        setToolBar(R.id.toolbar, options);


        String[] titles = {"医院会诊", "多专家会诊"};


        BaseViewPagerAdapter pagerAdapter = new BaseViewPagerAdapter(getSupportFragmentManager(), titles);
        pagerAdapter.addFragment(HospitalAppointmentFragment.getInstance());
        pagerAdapter.addFragment(MultipleAppointmentFragment.getInstance());

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

