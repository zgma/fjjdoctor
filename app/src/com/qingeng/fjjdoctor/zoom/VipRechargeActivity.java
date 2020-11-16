package com.qingeng.fjjdoctor.zoom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.UpayResponseBean;
import com.qingeng.apilibrary.bean.UserInfoBean;
import com.qingeng.apilibrary.bean.VipPriceBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.VipPriceAdapter;
import com.qingeng.fjjdoctor.main.activity.MainActivity;
import com.qingeng.fjjdoctor.util.HtmlUtils;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
import com.qingeng.fjjdoctor.util.NetWorkUtils;

import java.util.ArrayList;
import java.util.List;

public class VipRechargeActivity extends UI implements HttpInterface, View.OnClickListener, VipPriceAdapter.VipPriceListener {

    private ImageView iv_back;
    private HeadImageView userhead;

    private TextView tv_user_name;
    private TextView tv_user_des;
    private Button btn_next;

    private VipPriceAdapter vipPriceAdapter;
    private RecyclerView rcl_price;
    private WebView webView;
    private List<VipPriceBean> vipPriceBeans = new ArrayList<>();
    private int selectedPosition = 0;


    public static void start(Context context) {
        Intent intent = new Intent(context, VipRechargeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vip_recharge);
        iv_back = findViewById(R.id.iv_back);
        userhead = findViewById(R.id.user_photo);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_des = findViewById(R.id.tv_user_des);
        btn_next = findViewById(R.id.btn_next);

        rcl_price = findViewById(R.id.rcl_price);
        webView = findViewById(R.id.webView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
        rcl_price.setLayoutManager(linearLayoutManager);

        vipPriceAdapter = new VipPriceAdapter(this);
        vipPriceAdapter.setListener(this);
        vipPriceAdapter.setList(vipPriceBeans);
        rcl_price.setAdapter(vipPriceAdapter);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });
        showUserDataToUI();
        getUserData();
        getListData();

        btn_next.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }


    private void getListData() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpClient.getVipList(baseRequestBean, this, RequestCommandCode.GET_PARTNER_DATA);
    }

    private void getUserData() {
        HttpClient.getLoginUserInfo(this, RequestCommandCode.GET_OTHER_USER_INFO);
    }


    private void showUserDataToUI() {
        UserInfoBean userBean = LocalDataUtils.getUserInfo();
        if (userBean != null && tv_user_name != null) {
            tv_user_name.setText(userBean.getNickName());
            userhead.loadImgForUrl(userBean.getAvatar());
        }
        /*VipInfo vipInfo = Preferences.getVipInfo();
        if (vipInfo != null) {
            tv_user_des.setText(vipInfo.getEndDate() + " 到期");
            if (vipPriceBeans != null) {
                for (int i = 0; i < vipPriceBeans.size(); i++) {
                    VipPriceBean vipPriceBean = vipPriceBeans.get(i);
                    if (vipPriceBean.getId() == vipInfo.getVipId()) {
                        selectedPosition = i;
                        vipPriceAdapter.setSelectedPosition(selectedPosition);
                        vipPriceAdapter.notifyDataSetChanged();
                        webView.loadDataWithBaseURL(null, HtmlUtils.getNewContent(vipPriceBean.getDesc()), "text/html", "utf-8", null);
                        break;
                    }
                }
            }
        } else {
            tv_user_des.setText("未开通会员");
            if (vipPriceBeans != null && vipPriceBeans.size()>0) {
                selectedPosition = 0;
                vipPriceAdapter.setSelectedPosition(selectedPosition);
                vipPriceAdapter.notifyDataSetChanged();
                webView.loadDataWithBaseURL(null, HtmlUtils.getNewContent(vipPriceBeans.get(selectedPosition).getDesc()), "text/html", "utf-8", null);

            }
        }*/
    }

    @Override
    public void onClick(View v) {
        if (v == btn_next) {
            DialogMaker.showProgressDialog(this, "加载中");
            BaseRequestBean baseRequestBean = new BaseRequestBean();
            baseRequestBean.addParams("vipId", vipPriceBeans.get(selectedPosition).getId());
            baseRequestBean.addParams("clientIp", NetWorkUtils.getIPAddress(this));
            baseRequestBean.addParams("payStatus", "0");
            HttpClient.publishVip(baseRequestBean, this, 10000002);
        }
        if (v == iv_back) {
            finish();
        }
    }

    private UpayResponseBean upayResponseBean;

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case 10000002:
                upayResponseBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                goUPay();
                break;
            case RequestCommandCode.GET_PARTNER_DATA:
                vipPriceBeans = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), VipPriceBean.class);
                vipPriceAdapter.setList(vipPriceBeans);
                vipPriceAdapter.notifyDataSetChanged();
                showUserDataToUI();
                break;
            case RequestCommandCode.GET_OTHER_USER_INFO:
                LocalDataUtils.saveLocalDoctor(baseResponseData.getData());
                MainActivity.saveUserInfoToPreferences();
                showUserDataToUI();
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
    }

    private void showDesc() {

    }

    @Override
    public void onClick(int i, VipPriceBean vipPriceBean) {
        vipPriceAdapter.setSelectedPosition(i);
        vipPriceAdapter.notifyDataSetChanged();
        webView.loadDataWithBaseURL(null, HtmlUtils.getNewContent(vipPriceBean.getDesc()), "text/html", "utf-8", null);
    }

    void goUPay() {
      }

    private void sendSuccess(String message) {
        ToastHelper.showToast(this, message);
        getUserData();
    }
}
