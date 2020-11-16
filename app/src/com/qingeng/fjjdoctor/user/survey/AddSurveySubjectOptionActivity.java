package com.qingeng.fjjdoctor.user.survey;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leon.lib.settingview.LSettingItem;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyEditDialog;
import com.netease.nim.uikit.common.ui.dialog.MenuDialog;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.SurveyBean;
import com.qingeng.apilibrary.bean.SurveySubjectBean;
import com.qingeng.apilibrary.bean.SurveySubjectOptionBean;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.SurveySubjectAdapter;
import com.qingeng.fjjdoctor.adapter.SurveySubjectAdapter.OnSubjectActionListener;
import com.qingeng.fjjdoctor.adapter.SurveySubjectOptionAdapter;
import com.qingeng.fjjdoctor.setting.SendFeedbackActivity2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddSurveySubjectOptionActivity extends UI implements HttpInterface, SurveySubjectOptionAdapter.OnActionListener {

    @BindView(R.id.rcv_list)
    RecyclerView rcv_list;
    @BindView(R.id.item_type)
    LSettingItem item_type;

    @BindView(R.id.edit_title)
    EditText edit_title;
    @BindView(R.id.btn_set)
    Button btn_set;


    List<SurveySubjectOptionBean> dataList = new ArrayList<>();
    SurveySubjectBean surveySubjectBean;
    SurveySubjectOptionAdapter adapter;
    List<String> messages = new ArrayList<>();
    private int subjectId = -1;
    private int modelId = -1;


    public static void start(Context context, int modelId) {
        Intent intent = new Intent(context, AddSurveySubjectOptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("modelId", modelId);
        context.startActivity(intent);
    }

    public static void start(Context context, int modelId, int subjectId) {
        Intent intent = new Intent(context, AddSurveySubjectOptionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("modelId", modelId);
        intent.putExtra("subjectId", subjectId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_survey_subject_option);
        ButterKnife.bind(this);

        modelId = getIntent().getIntExtra("modelId", -1);
        subjectId = getIntent().getIntExtra("subjectId", -1);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = subjectId == -1 ? "添加题目" : "编辑题目";
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

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_list.setLayoutManager(linearLayoutManager2);

        adapter = new SurveySubjectOptionAdapter(this);
        adapter.setOnActionListener(this);
        rcv_list.setAdapter(adapter);

        messages.add("填空");
        messages.add("单选");
        messages.add("多选");

        if (subjectId != -1) {
            getDataList();
        }
        showUI();
        item_type.setmOnLSettingItemClick((id1, isChecked) -> showReportMenu());

        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_title.getText().toString().trim())) {
                    ToastHelper.showToast(AddSurveySubjectOptionActivity.this, "请输入题目名称");
                    return;
                }
                saveSubject();
            }
        });
    }

    public void setData(List<SurveySubjectOptionBean> beans) {
        List<SurveySubjectOptionBean> surveySubjectOptionBeans = new ArrayList<>();
        if (beans != null) {
            surveySubjectOptionBeans.addAll(beans);
        }
        surveySubjectOptionBeans.add(new SurveySubjectOptionBean());
        adapter.setDataList(surveySubjectOptionBeans);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case 100016:
                surveySubjectBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), SurveySubjectBean.class);
                selectTypeIndex = surveySubjectBean.getSubjectType() - 1;
                showUI();
                dataList = surveySubjectBean.getSurveyOptionList();
                setData(dataList);
                break;
            case 100018:
                ToastHelper.showToast(AddSurveySubjectOptionActivity.this, "保存成功");
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
        baseRequestBean.addParams("id", subjectId);
        HttpClient.lookSurveySubjectDetilById(baseRequestBean, this, 100016);
    }


    @Override
    public void onDelete(SurveySubjectOptionBean surveySubjectBean) {
        dataList.remove(surveySubjectBean);
        setData(dataList);
    }

    @Override
    public void add() {
        final EasyEditDialog requestDialog = new EasyEditDialog(this);
        requestDialog.setTitle("添加选择项");
        requestDialog.addNegativeButtonListener(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
            }
        });
        requestDialog.addPositiveButtonListener(R.string.send, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDialog.dismiss();
                String msg = requestDialog.getEditMessage();
                addOption(msg);
            }
        });
        requestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        requestDialog.show();

    }

    private void addOption(String s) {
        SurveySubjectOptionBean surveySubjectOptionBean = new SurveySubjectOptionBean();
        surveySubjectOptionBean.setOptionContent(s);
        dataList.add(surveySubjectOptionBean);
        setData(dataList);
    }

    private MenuDialog reportMenu;
    private int selectTypeIndex = 0;

    private void showReportMenu() {

        if (reportMenu == null) {
            reportMenu = new MenuDialog(this, messages, selectTypeIndex, -1,
                    new MenuDialog.MenuDialogOnButtonClickListener() {
                        @Override
                        public void onButtonClick(int index, String name) {
                            reportMenu.dismiss();
                            if (name.equals(getString(com.netease.nim.uikit.R.string.cancel))) {
                                return; // 取消不处理
                            }
                            selectTypeIndex = index;
                            showUI();
                        }
                    });
        }
        reportMenu.show();
    }

    private void showUI() {
        item_type.setRightText(messages.get(selectTypeIndex));
        rcv_list.setVisibility(selectTypeIndex == 0 ? View.GONE : View.VISIBLE);
        if (surveySubjectBean != null) {
            edit_title.setText(surveySubjectBean.getSubjectName());
        }
    }


    private void saveSubject() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("subjectName", edit_title.getText().toString().trim());
        baseRequestBean.addParams("subjectType", selectTypeIndex + 1);
        baseRequestBean.addParams("modelId", modelId);

        JSONArray objects = new JSONArray();
        for (int i = 0; i < dataList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", dataList.get(i).getOptionContent());
            objects.add(jsonObject);
        }
        baseRequestBean.addParams("subjectOptions", objects.toJSONString());
        DialogMaker.showProgressDialog(this, "提交中...");

        if (subjectId == -1) {
            HttpClient.addSurveySubject(baseRequestBean, this, 100018);
        } else {
            baseRequestBean.addParams("id", subjectId);
            HttpClient.updateSurveySubject(baseRequestBean, this, 100018);
        }
    }

}

