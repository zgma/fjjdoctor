package com.qingeng.fjjdoctor.redpacket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.RedPacketReceiverBean;
import com.qingeng.apilibrary.bean.RedPacketResponseBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.RedPacketGrabAdapter;
import com.qingeng.fjjdoctor.util.MoneyUtils;

import java.util.List;

public class RedPacketDetailActivity extends UI implements HttpInterface {

    private HeadImageView user_photo;
    private TextView tv_name;
    private TextView tv_message;
    private TextView tv_show_amount;
    private TextView tv_desc;
    private TextView tv_process;
    private ConstraintLayout layout_money;

    private RedPacketResponseBean redPacketResponseBean;
    private RedPacketGrabAdapter redPacketGrabAdapter;
    private List<RedPacketReceiverBean> redPacketReceiverBeans;

    private RecyclerView rcl_list;

    private String orderNum;
    private String requestId;

    public static void start(Context context,String orderNum,String requestId) {
        Intent intent = new Intent(context, RedPacketDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("orderNum",orderNum);
        intent.putExtra("requestId",requestId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_packet_detail);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "";
        options.titleColor = R.color.white;
        options.navigateId = R.drawable.actionbar_white_back_icon;
        options.backgrounpColor = R.color.topbar_backgroup_red;
        setToolBar(R.id.toolbar, options);
        orderNum = getIntent().getExtras().getString("orderNum");
        requestId = getIntent().getExtras().getString("requestId");
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
        user_photo = findViewById(R.id.user_photo);
        tv_name = findViewById(R.id.tv_name);
        tv_process = findViewById(R.id.tv_process);
        tv_message = findViewById(R.id.tv_message);
        tv_show_amount = findViewById(R.id.tv_show_amount);
        tv_desc = findViewById(R.id.tv_desc);
        layout_money = findViewById(R.id.layout_money);
        layout_money.setVisibility(View.GONE);
        tv_desc.setText("领取成功，已存入钱包");


        rcl_list = findViewById(R.id.rcl_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.VERTICAL);
        rcl_list.setLayoutManager(linearLayoutManager);

        redPacketGrabAdapter = new RedPacketGrabAdapter(this);
        rcl_list.setAdapter(redPacketGrabAdapter);
        getData();

    }

    private void getData() {
        DialogMaker.showProgressDialog(this, "获取红包信息");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("requestId", requestId);
        baseRequestBean.addParams("queryType", "SIMPLE");
        HttpClient.redPackageQuery(baseRequestBean, this, RequestCommandCode.PACKAGE_DETAIL);
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.PACKAGE_DETAIL:
                redPacketResponseBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), RedPacketResponseBean.class);

                user_photo.loadAvatar(redPacketResponseBean.getObject().getHeadImage());
                tv_name.setText(redPacketResponseBean.getObject().getUsername()+"的红包");
                tv_message.setText(redPacketResponseBean.getObject().getMsg());
                redPacketReceiverBeans = redPacketResponseBean.getObject().getReceivers();
                boolean grabed = false;
                if (redPacketReceiverBeans!=null){
                    redPacketGrabAdapter.setRedPacketReceiverBeans(redPacketReceiverBeans);
                    redPacketGrabAdapter.notifyDataSetChanged();

                    for (int i = 0; i <redPacketReceiverBeans.size() ; i++) {
                        if (redPacketReceiverBeans.get(i).getAccid().equals(AppPreferences.getAccId())){
                            layout_money.setVisibility(View.VISIBLE);
                            tv_show_amount.setText(MoneyUtils.fenInt2YuanString((int)redPacketReceiverBeans.get(i).getAmount()));
                            tv_desc.setText("领取成功，已存入钱包");
                            grabed = true;
                            break;
                        }
                    }
                }

                if (!grabed){
                    tv_desc.setText("来迟了，没有抢到红包");
                }

                tv_process.setText("已领取"+redPacketResponseBean.getObject().getUsed()+"/"+redPacketResponseBean.getObject().getTotal()+"个");
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, "失败 " + message);
    }

    @Override
    public void onComplete() {
        DialogMaker.dismissProgressDialog();
    }


}
