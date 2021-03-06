package com.qingeng.fjjdoctor.main.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.SearchGroupAdapter;

import java.util.ArrayList;
import java.util.List;

public class GroupSearchActivity extends UI implements SearchGroupAdapter.Listener{

    // view
    private RecyclerView recyclerView;

    private List<GroupBean> groupBeans = new ArrayList<>();

    private SearchGroupAdapter adapter;

    private boolean msgLoaded = false;

    private View emptyBg;

    private EditText input;
    private ImageView clear_input;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, GroupSearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "找群";
        setToolBar(R.id.toolbar, options);

        findViews();
    }

    private void findViews() {
        adapter = new SearchGroupAdapter(this);
        adapter.setGroupBeans(groupBeans);
        adapter.setListener(this);
        recyclerView = findView(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        emptyBg = findView(R.id.emptyBg);
        clear_input = findView(R.id.clear_input);
        input = findView(R.id.input);
        input.addTextChangedListener(watcherInput);
        clear_input.setVisibility(View.GONE);
        clear_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input.setText("");
            }
        });



        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //添加自己的方法
                    goSearch(input.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        boolean empty = groupBeans.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    private TextWatcher watcherInput = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && s.length() > 0) {
                clear_input.setVisibility(View.VISIBLE);
                //goSearch(input.getText().toString().trim());
            } else {
                clear_input.setVisibility(View.GONE);
            }
            input.setSelection(input.getText().length());//将光标移至文字末尾
        }
    };

    private void goSearch(String name){
        if (name.isEmpty())return;
        HttpClient.queryGroupList(name, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                System.out.println("onSuccess");
                groupBeans = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()),GroupBean.class);
                adapter.setGroupBeans(groupBeans);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(int requestCode, String message) {
                System.out.println("onFailure");

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");

            }
        }, RequestCommandCode.QUERY_USERS);
    }

    @Override
    public void add(GroupBean groupBean) {
        DialogMaker.showProgressDialog(this, "处理中...");
        HttpClient.inviteNewUser(groupBean.getId()+"", AppPreferences.getUserId()+"", "", new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                ToastHelper.showToast(GroupSearchActivity.this, "操作成功");
                goSearch(input.getText().toString().trim());
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(GroupSearchActivity.this, "邀请失败:"+message);
            }

            @Override
            public void onComplete() {
                DialogMaker.dismissProgressDialog();
            }
        }, RequestCommandCode.REMOVE_ANNO);
    }
}
