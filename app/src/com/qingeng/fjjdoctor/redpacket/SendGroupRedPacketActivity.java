package com.qingeng.fjjdoctor.redpacket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.bean.TargetUserBean;
import com.qingeng.apilibrary.bean.UpayResponseBean;
import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.RedPacketUserAdapter;
import com.qingeng.fjjdoctor.session.extension.RedPacketAttachment;
import com.qingeng.fjjdoctor.util.MoneyUtils;
import com.qingeng.fjjdoctor.widget.PayPassDialog;



import java.util.ArrayList;
import java.util.List;

public class SendGroupRedPacketActivity extends UI implements HttpInterface, View.OnClickListener, RedPacketUserAdapter.Listener, PayPassDialog.OnFinishListener {

    private final static String TARGET_GROUP_ID = "TARGET_GROUP_ID";
    public final static String RESULT_DATA = "RESULT_DATA";

    private EditText edit_amount;
    private EditText edit_memo;
    private TextView tv_show_amount;
    private TextView tv_type;
    private Button btn_send;


    private EditText edit_count;
    private LinearLayout layout_count;

    private RecyclerView rcv_user;
    private LinearLayout layout_user;
    private RedPacketUserAdapter redPacketUserAdapter;
    private ArrayList<String> selectedUserAccid = new ArrayList<>();

    private List<GroupDetailBean.GroupUser> highGroups;

    private int queryStatusCount = 0;

    private int type = 0;
    private String targetGroupId;
    private TargetUserBean targetUserBean;
    private BaseRequestBean baseRequestBean;

    private UpayResponseBean upayResponseBean;
    String moneyStr;
    String memo;
    int count;

    public static void startActivityForResult(Context context, String targetGroupId, int requestCode) {
        Intent intent = new Intent();
        intent.putExtra(TARGET_GROUP_ID, targetGroupId);
        intent.setClass(context, SendGroupRedPacketActivity.class);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_group_red_packet);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "发红包";
        options.titleColor = R.color.white;
        options.navigateId = R.drawable.actionbar_white_back_icon;
        options.backgrounpColor = R.color.topbar_backgroup_red;
        setToolBar(R.id.toolbar, options);

        targetGroupId = getIntent().getStringExtra(TARGET_GROUP_ID);
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
        edit_amount = findViewById(R.id.edit_amount);
        edit_memo = findViewById(R.id.edit_memo);
        tv_show_amount = findViewById(R.id.tv_show_amount);
        btn_send = findViewById(R.id.btn_send);

        edit_count = findViewById(R.id.edit_count);
        layout_count = findViewById(R.id.layout_count);
        rcv_user = findViewById(R.id.rcv_user);
        layout_user = findViewById(R.id.layout_user);
        tv_type = findViewById(R.id.tv_type);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(linearLayoutManager.HORIZONTAL);
        rcv_user.setLayoutManager(linearLayoutManager);
        redPacketUserAdapter = new RedPacketUserAdapter(this);
        redPacketUserAdapter.setListener(this);
        rcv_user.setAdapter(redPacketUserAdapter);
        edit_amount.addTextChangedListener(textWatcher);
        btn_send.setOnClickListener(this);

        tv_type.setOnClickListener(this);

        selectedUserAccid.add("def");
        redPacketUserAdapter.setUsers(selectedUserAccid);
        redPacketUserAdapter.notifyDataSetChanged();
        changeType(type = 0);
        getUerById();
        getGroupDetail();

    }

    private void getUerById() {
        HttpClient.userInfoByAccId(AppPreferences.getAccId(), this, RequestCommandCode.GET_OTHER_USER_INFO);
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.CREATE_RED_PACKAGE:
                RedPacketAttachment attachment = new RedPacketAttachment();
                // 红包id，红包信息，红包名称
                attachment.setRedMmoeny((String) baseRequestBean.getBody().get("total"));
                attachment.setRedNum((String) baseRequestBean.getBody().get("num"));
                attachment.setAccid(targetUserBean.getTargetUserAccid());
                attachment.setUserName(targetUserBean.getTargetUsername());
                attachment.setUserIcon(targetUserBean.getHeadImage());
                attachment.setUserId(targetUserBean.getTargetUserId() + "");
                attachment.setMessage((String) baseRequestBean.getBody().get("msg"));
                attachment.setOrderNum((String) baseResponseData.getData());

                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA, attachment);
                setResult(Activity.RESULT_OK, intent);
                this.finish();
                break;
            case RequestCommandCode.GET_OTHER_USER_INFO:
                targetUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                System.out.println("onSuccess---" + JSON.toJSONString(targetUserBean));
                break;
            case RequestCommandCode.GROUP_DETAIL:
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                GroupDetailBean.GroupUser groupUser = JSONObject.parseObject(jsonObject.get("waUserHighGroup").toString(), GroupDetailBean.GroupUser.class);
                GroupDetailBean.WaHuHighGroup waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);
                highGroups = waHuHighGroup.getHighGroups();
                if (highGroups == null || highGroups.size() == 0) {
                    ToastHelper.showToast(this, "群成员未空");
                    finish();
                }
                break;
            case RequestCommandCode.UPAY_TRANSFORM_CREATE:
                upayResponseBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                goUPayRecharge();
                break;
            case RequestCommandCode.UPAY_RED_PACKET_STATUS:
     /*           upayResponseBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UpayResponseBean.class);
                goUPayRecharge();
                sendRedPacket();*/
                JSONObject jsonObject2 = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                if (jsonObject2.containsKey("orderStatus") && jsonObject2.getString("orderStatus").equals("SUCCESS")){
                    sendRedPacket();
                }else {
                    packetStatus();
                }
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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            tv_show_amount.setText(edit_amount.getText().toString().trim());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String editStr = editable.toString().trim();
            int posDot = editStr.indexOf(".");
            //不允许输入3位小数,超过三位就删掉
            if (posDot < 0) {
                return;
            }
            if (editStr.length() - posDot - 1 > 2) {
                editable.delete(posDot + 3, posDot + 4);
            }


        }
    };

    private void getGroupDetail() {
        HttpClient.groupDetail(targetGroupId, this, RequestCommandCode.GROUP_DETAIL);
    }


    @Override
    public void onPassComplete(String pass) {
        /*UserBean userBean = Preferences.getUserBean();
        if (userBean != null) {
            if (TextUtils.isEmpty(userBean.getPayPwd())) {
                ToastHelper.showToast(this, "未设置支付密码，请先设置");
                //ForgetPasswordActivity.start(this, 1);
            } else {
                if (pass.equals(userBean.getPayPwd())) {
                    DialogMaker.showProgressDialog(this, "发送中...");
                    String targetUserIds = "";
                    int count = 0;
                    if (type == 0) {
                        count = Integer.parseInt(edit_count.getText().toString().trim());
                    } else if (type == 1) {
                        selectedUserAccid.remove("def");
                        for (int i = 0; i < selectedUserAccid.size(); i++) {
                            for (int j = 0; j < highGroups.size(); j++) {
                                if (highGroups.get(j).getAccid().equals(selectedUserAccid.get(i))) {
                                    if (i == 0) {
                                        targetUserIds = highGroups.get(j).getUserId() + "";
                                    } else {
                                        targetUserIds = targetUserIds + "," + highGroups.get(j).getUserId() + "";
                                    }
                                }
                            }

                        }
                        count = selectedUserAccid.size();
                    }
                    baseRequestBean = new BaseRequestBean();
                    baseRequestBean.addParams("groupId", targetGroupId);
                    baseRequestBean.addParams("num", count);
                    if (!TextUtils.isEmpty(targetUserIds)) {
                        baseRequestBean.addParams("targetUserId", targetUserIds);
                    }
                    baseRequestBean.addParams("packageType", "2");
                    baseRequestBean.addParams("msg", edit_memo.getText().toString().trim());
                    baseRequestBean.addParams("total", edit_amount.getText().toString().trim());
                    HttpClient.createRedPackage(baseRequestBean, this, RequestCommandCode.CREATE_RED_PACKAGE);
                } else {
                    ToastHelper.showToast(this, "密码错误");

                }
            }
        } else {
            ToastHelper.showToast(this, "用户信息获取失败");
            finish();
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_type:
                changeType(type = type == 0 ? 1 : 0);
                break;
            case R.id.btn_send:
                moneyStr = edit_amount.getText().toString().trim();
                memo = edit_memo.getText().toString().trim();
                if (TextUtils.isEmpty(moneyStr) || TextUtils.isEmpty(memo)) {
                    ToastHelper.showToast(this, "请输入完整内容");
                    return;
                }
                if (type == 0) {
                    count = Integer.parseInt(edit_count.getText().toString().trim());
                    if (count <= 0 || count >= 100) {
                        ToastHelper.showToast(this, "请输入最大未100的红包数量");
                        return;
                    }
                }
                if (type == 1) {
                    if (selectedUserAccid.size() == 0) {
                        ToastHelper.showToast(this, "请选择领取人");
                        return;
                    }
                }
                send();
                break;
        }
    }


    private void send() {
/*        PayPassDialog payPassDialog = new PayPassDialog(this);
        payPassDialog.init(true,"发红包",edit_amount.getText().toString().trim());
        payPassDialog.setOnFinishListener(this);
        payPassDialog.show();*/


        DialogMaker.showProgressDialog(this, "获取订单中...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("packetType", "GROUP_LUCK");
        baseRequestBean.addParams("amount", MoneyUtils.yuanString2FenInt(moneyStr));
        if (type == 1) {
            String targetUserIds = "";
            selectedUserAccid.remove("def");
            for (int i = 0; i < selectedUserAccid.size(); i++) {
                for (int j = 0; j < highGroups.size(); j++) {
                    if (highGroups.get(j).getAccid().equals(selectedUserAccid.get(i))) {
                        if (i == 0) {
                            targetUserIds = highGroups.get(j).getUserId() + "";
                        } else {
                            targetUserIds = targetUserIds + "," + highGroups.get(j).getUserId() + "";
                        }
                    }
                }

            }
            count = selectedUserAccid.size();
            baseRequestBean.addParams("targetUserId", targetUserIds);
        }
        baseRequestBean.addParams("packetCount", count);
        baseRequestBean.addParams("msg", memo);
        baseRequestBean.addParams("groupId", targetGroupId);
        HttpClient.redPackageCreate(baseRequestBean, this, RequestCommandCode.UPAY_TRANSFORM_CREATE);
    }


    void goUPayRecharge(){
       }

    private void sendRedPacket(){
        RedPacketAttachment attachment = new RedPacketAttachment();
        // 红包id，红包信息，红包名称
        attachment.setRedNum(count+"");
        attachment.setRedMmoeny(moneyStr);
        attachment.setAccid(targetUserBean.getTargetUserAccid());
        attachment.setUserName(targetUserBean.getTargetUsername());
        attachment.setUserIcon(targetUserBean.getHeadImage());
        attachment.setUserId(targetUserBean.getTargetUserId()+"");
        attachment.setMessage(memo);
        attachment.setOrderNum(upayResponseBean.getObject().getSerialNumber());
        attachment.setRequestId(upayResponseBean.getObject().getRequestId());

        Intent intent = new Intent();
        intent.putExtra(RESULT_DATA, attachment);
        setResult(Activity.RESULT_OK, intent);
        this.finish();
    }

    private static final int REQUEST_CODE_CONTACT_SELECT = 103;


    @Override
    public void onItemClick(String id) {
        selectedUserAccid.remove("def");
        ContactSelectActivity.Option option = TeamHelper.getContactSelectOptionWhiteSelected(selectedUserAccid);
        option.type = ContactSelectActivity.ContactSelectType.TEAM_MEMBER;
        option.title = "选择领取人";
        option.teamId = targetGroupId;
        option.allowSelectEmpty = true;
        NimUIKit.startContactSelector(SendGroupRedPacketActivity.this, option, REQUEST_CODE_CONTACT_SELECT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        selectedUserAccid.clear();
        selectedUserAccid = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
        selectedUserAccid.add("def");
        redPacketUserAdapter.setUsers(selectedUserAccid);
        redPacketUserAdapter.notifyDataSetChanged();
    }

    private void changeType(int type) {
        if (type == 0) {
            layout_count.setVisibility(View.VISIBLE);
            layout_user.setVisibility(View.GONE);
            tv_type.setText("专属红包");
        }
        if (type == 1) {
            layout_count.setVisibility(View.GONE);
            layout_user.setVisibility(View.VISIBLE);
            tv_type.setText("普通红包");
        }
    }


    private void packetStatus() {
        queryStatusCount++;
        if (queryStatusCount > 5){
            ToastHelper.showToast(SendGroupRedPacketActivity.this, "查询红包状态重试5次失败，请重新发送");
            return;
        }
        DialogMaker.showProgressDialog(this, "获取红包状态...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("requestId", upayResponseBean.getObject().getRequestId());
        HttpClient.packetStatus(baseRequestBean, SendGroupRedPacketActivity.this, RequestCommandCode.UPAY_RED_PACKET_STATUS);
    }
}
