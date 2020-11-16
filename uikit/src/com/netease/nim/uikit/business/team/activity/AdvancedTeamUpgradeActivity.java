package com.netease.nim.uikit.business.team.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.bean.PayWayBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.team.adapter.CostMoneyAdapter;
import com.netease.nim.uikit.business.team.ui.PayDialog;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

import java.util.List;

/**
 * 高级群群资料页
 * Created by huangjun on 2015/3/17.
 */
public class AdvancedTeamUpgradeActivity extends UI implements HttpInterface,CostMoneyAdapter.OnClickListener, PayDialog.OnDialogListener {

    private static final String EXTRA_ID = "EXTRA_ID";

    private String teamId;


    private ImageView iv_go_back;
    private TextView tv_title;
    private HeadImageView head_group;
    private TextView tv_group_name;
    private TextView tv_group_desc;
    private Button btn_upgrade;
    private TextView tv_title_2;

   private RecyclerView rcv_cost;
   private CostMoneyAdapter costMoneyAdapter;


    private GroupDetailBean.WaHuHighGroup waHuHighGroup;
    private List<PayWayBean> payWayBeans;

    private PayDialog payDialog;

    private PayWayBean payWayBean;



    public static void start(Context context, String tid) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_ID, tid);
        intent.setClass(context, AdvancedTeamUpgradeActivity.class);
        context.startActivity(intent);
    }


    /**
     * ***************************** Life cycle *****************************
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_upgrade);
        parseIntentData();
        findViews();
        getData();

    }

    private void parseIntentData() {
        teamId = getIntent().getStringExtra(EXTRA_ID);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void findViews() {

        iv_go_back = findView(R.id.iv_go_back);
        tv_title= findView(R.id.tv_title);
        head_group= findView(R.id.head_group);
        tv_group_name= findView(R.id.tv_group_name);
        tv_group_desc= findView(R.id.tv_group_desc);
        rcv_cost= findView(R.id.rcv_cost);
        tv_title_2= findView(R.id.tv_title_2);
        btn_upgrade= findView(R.id.btn_upgrade);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        rcv_cost.setLayoutManager(gridLayoutManager);
        costMoneyAdapter = new CostMoneyAdapter(this);
        costMoneyAdapter.setOnClickListener(this);
        rcv_cost.setAdapter(costMoneyAdapter);

        iv_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPayDialog();
            }
        });

    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GROUP_DETAIL:
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);
                updateTeamInfo();
                break;
            case RequestCommandCode.QUERY_PAY_WAY:
                payWayBeans = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()),PayWayBean.class);
                costMoneyAdapter.setPayWayBeans(payWayBeans);
                costMoneyAdapter.notifyDataSetChanged();
                break;
            case RequestCommandCode.UPDATE_GROUP_INFO_CAN_INVITE:
                ToastHelper.showToast(this, "处理成功");
                updateTeamInfo();
                break;
            case RequestCommandCode.GET_ALI_PAY_ORDER_INFO:
/*                JSONObject jsonObject1 = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()));
                String aliStr = (String) jsonObject1.get("body");
                PayHelper.getInstance().setIPayListener(this);
                PayHelper.getInstance().AliPay(this, aliStr);
                ToastHelper.showToast(this, "处理成功");*/
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

    private void getGroupDate() {
        HttpClient.groupDetail(teamId, this, RequestCommandCode.GROUP_DETAIL);
    }

    private void getUpgradeCost() {
        HttpClient.queryPayWay(teamId, this, RequestCommandCode.QUERY_PAY_WAY);
    }



    private void updateTeamInfo() {
        if (payWayBeans!=null && payWayBeans.size()>1){
            payWayBean = payWayBeans.get(0);
        }

        tv_title.setText(waHuHighGroup.getHigherStatus().equals("普通群")?"升级群":"续费群");
        tv_group_name.setText(waHuHighGroup.getTname());
        tv_group_desc.setText(waHuHighGroup.getHigherStatus().equals("普通群")?"当前群为普通群":("高级群将于"+waHuHighGroup.getExpireDate()+"过期"));
        head_group.loadImgForUrl("");
        btn_upgrade.setText(waHuHighGroup.getHigherStatus().equals("普通群")?"立即升级":"立即续费");
        tv_title_2.setText(waHuHighGroup.getHigherStatus().equals("普通群")?"升级群聊为高级群聊":"续费高级群聊");


    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        getGroupDate();
        getUpgradeCost();
    }


    @Override
    public void onItemClick(int position) {
        payWayBean = payWayBeans.get(position);
        costMoneyAdapter.setPressIndex(position);
        costMoneyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDismiss(Object o) {

    }

    @Override
    public void onPay(int payBy, String moneyStr) {
        try {
            if (payBy == PayDialog.PAY_MODE_BY_WX) {

            }else if(payBy == PayDialog.PAY_MODE_BY_ZFB){
                BaseRequestBean baseRequestBean = new BaseRequestBean();
                baseRequestBean.addParams("total",moneyStr);
                baseRequestBean.addParams("groupId",waHuHighGroup.getTid()+"");
                baseRequestBean.addParams("num",payWayBean.getTimeNum());
                baseRequestBean.addParams("unit",payWayBean.getUnit());
                HttpClient.getPayOrderInfo_ali(baseRequestBean,this, RequestCommandCode.GET_ALI_PAY_ORDER_INFO);
            }
            payDialog.dismiss();
        } catch (Exception e) {

        }
    }

    public void showPayDialog() {
        if (payWayBean==null) return;
        payDialog = new PayDialog(this, payWayBean.getTimeNum()+payWayBean.getUnit()+"高级群特权升级",payWayBean.getMoney()+"");
        payDialog.setOnDismissListener(this);
        payDialog.show();
    }

/*    @Override
    public void onSuccess() {
        ToastHelper.showToast(this, "支付成功");
        getData();
    }

    @Override
    public void onFail() {
        ToastHelper.showToast(this, "支付失败");
    }*/
}
