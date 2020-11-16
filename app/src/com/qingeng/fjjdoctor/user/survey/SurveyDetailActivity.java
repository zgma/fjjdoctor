package com.qingeng.fjjdoctor.user.survey;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.SurveyBean;
import com.qingeng.apilibrary.bean.SurveySubjectBean;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.OnListItemClickListener;
import com.qingeng.fjjdoctor.adapter.SurveyAdapter;
import com.qingeng.fjjdoctor.adapter.SurveySubjectAdapter;
import com.qingeng.fjjdoctor.adapter.SurveySubjectAdapter.OnSubjectActionListener;
import com.qingeng.fjjdoctor.util.UiUtils;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ezy.ui.layout.LoadingLayout;


public class SurveyDetailActivity extends UI implements HttpInterface, SurveySubjectAdapter.OnSubjectActionListener {

    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;

    @BindView(R.id.edit_title)
    EditText edit_title;
    @BindView(R.id.edit_content)
    EditText edit_content;


    @BindView(R.id.btn_add_model)
    Button btn_add_model;
    @BindView(R.id.btn_add_subject)
    Button btn_add_subject;


    List<SurveySubjectBean> dataList = new ArrayList<>();
    SurveyBean surveyBean;
    SurveySubjectAdapter adapter;

    private int modelId = -1;


    public static void start(Context context) {
        Intent intent = new Intent(context, SurveyDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void start(Context context, int modelId) {
        Intent intent = new Intent(context, SurveyDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("modelId", modelId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_detail);
        ButterKnife.bind(this);

        modelId = getIntent().getIntExtra("modelId", -1);


        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText("完成");
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_title.getText().toString().trim().equals(surveyBean.getModelName()) || edit_content.getText().toString().trim().equals(surveyBean.getModelAgreement())) {
                    ToastHelper.showToast(SurveyDetailActivity.this, "标题或者协议未改动");
                    return;
                }
                updateSurveyModel();
            }
        });
        toolbarView.setVisibility(modelId == -1 ? View.GONE : View.VISIBLE);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "患者调研";
        setToolBar(R.id.toolbar, options);
        initUI();


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (modelId != -1) {
            getDataList();
        }
        rcv_list.setVisibility(modelId != -1 ? View.VISIBLE : View.GONE);
        btn_add_subject.setVisibility(modelId != -1 ? View.VISIBLE : View.GONE);
        btn_add_model.setVisibility(modelId != -1 ? View.GONE : View.VISIBLE);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initUI() {
        btn_add_subject.setOnClickListener(v -> AddSurveySubjectOptionActivity.start(SurveyDetailActivity.this, modelId));
        btn_add_model.setOnClickListener(v -> updateSurveyModel());

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_list.setLayoutManager(linearLayoutManager2);

        adapter = new SurveySubjectAdapter(this);
        adapter.setOnSubjectActionListener(this);
        rcv_list.setAdapter(adapter);
        if (modelId != -1) getDataList();
        rcv_list.setVisibility(modelId != -1 ? View.VISIBLE : View.GONE);
        btn_add_subject.setVisibility(modelId != -1 ? View.VISIBLE : View.GONE);
        btn_add_model.setVisibility(modelId != -1 ? View.GONE : View.VISIBLE);
    }

    public void setData(List<SurveySubjectBean> beans) {
        dataList.clear();
        if (beans != null) {
            dataList.addAll(beans);
        }
        adapter.setDataList(dataList);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case 100016:
                surveyBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), SurveyBean.class);
                edit_title.setText(surveyBean.getModelName());
                edit_content.setText(surveyBean.getModelAgreement());
                setData(surveyBean.getSurveySubjectList());
                break;
            case 100017:
                ToastHelper.showToast(this, modelId == -1 ? "新模板发布成功" : "模板修改成功");
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


    private void getDataList() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("modelId", modelId);
        HttpClient.selectSubjectByModelId(baseRequestBean, this, 100016);
    }


    @Override
    public void onDelete(SurveySubjectBean surveySubjectBean) {
        ToastHelper.showToast(this, surveySubjectBean.getSubjectName() + "onDelete");
    }

    @Override
    public void onEdit(SurveySubjectBean surveySubjectBean) {
        AddSurveySubjectOptionActivity.start(this, modelId, surveySubjectBean.getId());
    }

    private void updateSurveyModel() {

        if (TextUtils.isEmpty(edit_title.getText().toString().trim()) || TextUtils.isEmpty(edit_content.getText().toString().trim())) {
            ToastHelper.showToast(SurveyDetailActivity.this, "标题或者协议未输入");
            return;
        }


        DialogMaker.showProgressDialog(this, "提交中...");
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("modelName", edit_title.getText().toString().trim());
        baseRequestBean.addParams("modelAgreement", edit_content.getText().toString().trim());
        if (modelId == -1) {
            HttpClient.addNewSurveyModel(baseRequestBean, this, 100017);
        } else {
            baseRequestBean.addParams("id", modelId);
            HttpClient.updateSurveyModel(baseRequestBean, this, 100017);
        }
    }
}

