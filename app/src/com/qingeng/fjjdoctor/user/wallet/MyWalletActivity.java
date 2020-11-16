package com.qingeng.fjjdoctor.user.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.UpayResponseBean;
import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.bean.WalletInfo;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;



/**
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class MyWalletActivity extends UI implements HttpInterface ,View.OnClickListener{


    private TextView tv_wallet_money;
    private ConstraintLayout layout_2;
    private ConstraintLayout layout_5;
    private ConstraintLayout layout_4;
    private ConstraintLayout layout_my_order;

    private ImageView iv_recharge;
    private ImageView iv_withdrawal;

    public static void start(Context context) {
        Intent intent = new Intent(context, MyWalletActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }


    public static void startActivityForResult(Context context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, MyWalletActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wallet);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString ="我的钱包";
        options.titleColor = R.color.white;
        options.navigateId = R.drawable.actionbar_white_back_icon;
        options.backgrounpColor = R.color.colorPrimary;
        setToolBar(R.id.toolbar, options);

        tv_wallet_money = findViewById(R.id.tv_wallet_money);
        layout_2 = findViewById(R.id.layout_2);
        layout_5 = findViewById(R.id.layout_5);
        layout_4 = findViewById(R.id.layout_4);
        layout_my_order = findViewById(R.id.layout_my_order);
        iv_recharge = findViewById(R.id.iv_recharge);
        iv_withdrawal = findViewById(R.id.iv_withdrawal);

        layout_2.setOnClickListener(this);
        layout_5.setOnClickListener(this);
        layout_4.setOnClickListener(this);
        layout_my_order.setOnClickListener(this);
        iv_recharge.setOnClickListener(this);
        iv_withdrawal.setOnClickListener(this);

        loadData(); // load old data
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     */
    public void loadData() {
        showDataToUI();
        /*HttpClient.getLoginUserInfo(new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                JSONObject data = (JSONObject) JSON.toJSON(baseResponseData.getData());
                UserInfo userInfo = data.toJavaObject(UserInfo.class);
                Preferences.saveUserBean(userInfo.getUserInfo());
                Preferences.saveWalletInfo(userInfo.getWallet());
                AppPreferences.saveUserId(userInfo.getUserInfo().getUserId() + "");
                AppPreferences.saveThemeId(userInfo.getUserInfo().getThemeId());
                showDataToUI();
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(MyWalletActivity.this, "更新用户信息失败 " + message);
            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.LOGIN_USER_INFO);*/

    }

    private void showDataToUI() {

      /*  WalletInfo walletInfo = Preferences.getWalletInfo();
        if (walletInfo != null && tv_wallet_money != null) {
            tv_wallet_money.setText(walletInfo.getMoney()+"");
        }*/

    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.MY_FAVORITE:
                break;
            case RequestCommandCode.UPAY_CREATE_TOKEN:
                UpayResponseBean upayResponseBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                goUPayRecharge(upayResponseBean);
                break;
            case RequestCommandCode.UPAY_CREATE_TOKEN_2:
                UpayResponseBean upayResponseBean2 = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                goUPayRecharge2(upayResponseBean2);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_2:
//                MyBankCardListActivity.start(this);


                DialogMaker.showProgressDialog(this, "加载中...");
                BaseRequestBean baseRequestBean = new BaseRequestBean();
                baseRequestBean.addParams("businessType", "ACCESS_CARDlIST");//总金额，单位分
                HttpClient.uPayCreateToken(baseRequestBean, this, RequestCommandCode.UPAY_CREATE_TOKEN);

                break;
            case R.id.layout_5:
                //ChangePasswordActivity.start(this,1);
                DialogMaker.showProgressDialog(this, "加载中...");
                BaseRequestBean baseRequestBean2 = new BaseRequestBean();
                baseRequestBean2.addParams("businessType", "ACCESS_SAFETY");//总金额，单位分
                HttpClient.uPayCreateToken(baseRequestBean2, this, RequestCommandCode.UPAY_CREATE_TOKEN_2);
                break;
            case R.id.iv_recharge:
                RechargeActivity.start(this);
                break;
            case R.id.iv_withdrawal:
                WithdrawalActivity.start(this);
                break;
            case R.id.layout_my_order:
                MyOrderListActivity.start(this);
                break;
            case R.id.layout_4:
              /*  UserBean userBean = Preferences.getUserBean();
                if (userBean != null){
                    if (TextUtils.isEmpty(userBean.getPayPwd())) {
                       // ForgetPasswordActivity.start(this,1);
                    }else {
                       // ForgetPasswordActivity.start(this,2);
                    }
                }*/
                break;
        }
    }

    void goUPayRecharge(UpayResponseBean upayResponseBean) {
       }

    void goUPayRecharge2(UpayResponseBean upayResponseBean) {
       }

}
