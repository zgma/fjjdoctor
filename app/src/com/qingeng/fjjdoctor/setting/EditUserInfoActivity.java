package com.qingeng.fjjdoctor.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.CustomAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.qingeng.apilibrary.bean.ApplyBean;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.ApplyAdapter;
import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;
import com.qingeng.fjjdoctor.enums.EditEnum;
import com.qingeng.fjjdoctor.session.activity.FriendApplyInfoActivity;
import com.qingeng.fjjdoctor.util.LocalDataUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 系统消息中心界面
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class EditUserInfoActivity extends UI implements HttpInterface {

    String typeValue;
    String oldData;

    @BindView(R.id.edit_input)
    EditText edit_input;
    @BindView(R.id.btn_save)
    Button btn_save;

    public static void start(Context context, EditEnum type, String data) {
        Intent intent = new Intent();
        intent.setClass(context, EditUserInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("TYPE", type.getValue());
        intent.putExtra("DATA", data);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_user_info);
        ButterKnife.bind(this);
        typeValue = getIntent().getStringExtra("TYPE");
        oldData = getIntent().getStringExtra("DATA");


        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "编辑" + EditEnum.name(typeValue);
        setToolBar(R.id.toolbar, options);

        edit_input.setHint("请输入" + EditEnum.name(typeValue));
        edit_input.setText(oldData);

        btn_save.setOnClickListener(v -> update());

    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID:
                ToastHelper.showToast(this, "操作成功");
                finish();
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

    private void update() {
        String inputStr = edit_input.getText().toString().trim();
        if (TextUtils.isEmpty(inputStr) || oldData.equals(inputStr)) {
            ToastHelper.showToast(this, "请输入内容");
            return;
        }
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams(typeValue, edit_input.getText().toString().trim());
        HttpClient.updateUserInfo(baseRequestBean, this, RequestCommandCode.GET_OTHER_USER_INFO_BY_ACCID);
    }

}
