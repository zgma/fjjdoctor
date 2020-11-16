package com.qingeng.fjjdoctor.zoom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.PartnerBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.PartnerItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class PartnerMainActivity extends UI implements HttpInterface, View.OnClickListener
        , PartnerItemAdapter.Listener {


    private RecyclerView rcv_list1;
    private RecyclerView rcv_list2;

    private TextView tv_notice_title;
    private TextView tv_notice_desc;
    private TextView tv_notice_more;

    private String url;


    List<PartnerBean> partnerBeans1 = new ArrayList<>();
    List<PartnerBean> partnerBeans2 = new ArrayList<>();
    PartnerItemAdapter partnerItemAdapter1;
    PartnerItemAdapter partnerItemAdapter2;

    public static void start(Context context) {
        Intent intent = new Intent(context, PartnerMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    public static void startActivityForResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, ZoomMainActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_partner_main);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "合作联盟";
        setToolBar(R.id.toolbar, options);


        rcv_list1 = findViewById(R.id.rcv_list1);
        rcv_list2 = findViewById(R.id.rcv_list2);
        tv_notice_title = findViewById(R.id.tv_notice_title);
        tv_notice_desc = findViewById(R.id.tv_notice_desc);
        tv_notice_more = findViewById(R.id.tv_notice_more);

        partnerItemAdapter1 = new PartnerItemAdapter(this);
        partnerItemAdapter1.setPartnerBeans(partnerBeans1);
        partnerItemAdapter2 = new PartnerItemAdapter(this);
        partnerItemAdapter2.setPartnerBeans(partnerBeans2);

        partnerItemAdapter1.setListener(this);
        partnerItemAdapter2.setListener(this);

        //初始化标签
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rcv_list1.setLayoutManager(gridLayoutManager);
        rcv_list1.setAdapter(partnerItemAdapter1);
        //初始化标签
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 4);
        rcv_list2.setLayoutManager(gridLayoutManager2);
        rcv_list2.setAdapter(partnerItemAdapter2);

        tv_notice_more.setOnClickListener(this);
        getListData();
    }


    private void getListData() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpClient.getPartnerData(baseRequestBean, this, RequestCommandCode.GET_PARTNER_DATA);
    }


    @Override
    public void onClick(View v) {
        if (v == tv_notice_more) {
            if (!TextUtils.isEmpty(url)) {
                HtmlContentActivity.start(this, url);
            }
        }
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_PARTNER_DATA:
                JSONObject data = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                partnerBeans1 = JSON.parseArray(data.getJSONArray("userCommand").toJSONString(), PartnerBean.class);
                partnerBeans2 = JSON.parseArray(data.getJSONArray("hotCommand").toJSONString(), PartnerBean.class);
                tv_notice_title.setText(data.getString("title"));
                tv_notice_desc.setText(data.getString("content"));
                url = data.getString("url");
                partnerItemAdapter1.setPartnerBeans(partnerBeans1);
                partnerItemAdapter2.setPartnerBeans(partnerBeans2);
                partnerItemAdapter1.notifyDataSetChanged();
                partnerItemAdapter2.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, message);
    }

    @Override
    public void onComplete() {
    }


    @Override
    public void onItemClick(PartnerBean partnerBean) {
        if (partnerBean!=null && !TextUtils.isEmpty(partnerBean.getUrl())){
            Intent intent = new Intent();
            //Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(partnerBean.getUrl());
            intent.setData(content_url);
            startActivity(intent);
        }else {
            ToastHelper.showToast(this, "跳转链接为空，请联系管理员");
        }
    }
}
