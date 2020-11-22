package com.qingeng.fjjdoctor.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.ContactBean;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.adapter.ContactsAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 通讯录Fragment
 * <p/>
 * Created by huangjun on 2015/9/7.
 */
public class ContactsFragment2 extends TFragment implements HttpInterface, SwipeRefreshLayout.OnRefreshListener, ContactsAdapter.ContactListener {

    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recyclerView;

    private ContactsAdapter adapter;
    private List<ContactBean> contactBeanList = new ArrayList<ContactBean>();

    private View loadingFrame;
    private boolean msgLoaded = false;

    /**
     * ***************************************** 生命周期 *****************************************
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 界面初始化

        initAdapter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        this.onRefresh();
    }

    private void initAdapter() {
        adapter = new ContactsAdapter(getContext());
        adapter.setListener(this);
        adapter.setContactBeanList(contactBeanList);
        swipe_refresh = findView(com.qingeng.fjjdoctor.R.id.swipe_refresh);
        swipe_refresh.setOnRefreshListener(this);
        recyclerView = findView(com.qingeng.fjjdoctor.R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        loadingFrame = findView(com.qingeng.fjjdoctor.R.id.contact_loading_frame);

        this.onRefresh();
    }

    /**
     * 加载历史消息
     */
    public void loadData() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("pageNum", 1);
        baseRequestBean.addParams("pageSize", 1000);
        baseRequestBean.addParams("patientKind", 1);//患者类型【1 新患者 3 随访名单】
        HttpClient.selectMyPatientOther(baseRequestBean, this, 10001);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case 10001:
                contactBeanList = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), ContactBean.class);
                adapter.setContactBeanList(contactBeanList);
                refresh();
                break;
        }
    }


    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(getContext(), message);
    }

    @Override
    public void onComplete() {
        swipe_refresh.setRefreshing(false);
    }


    private void refresh() {
        adapter.notifyDataSetChanged();
        boolean empty = contactBeanList.isEmpty() && msgLoaded;
        loadingFrame.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(ContactBean contactBean) {

    }

    @Override
    public void more(ContactBean contactBean) {

    }

    @Override
    public void onRefresh() {
        loadData();
    }
}
