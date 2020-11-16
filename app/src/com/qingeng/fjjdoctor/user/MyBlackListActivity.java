package com.qingeng.fjjdoctor.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.BlackListUserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.pinyin.CharIndexView;
import com.netease.nim.uikit.common.ui.pinyin.Contact;
import com.netease.nim.uikit.common.ui.pinyin.adapter.ContactAdapter;
import com.netease.nim.uikit.common.ui.pinyin.adapter.ContactAdapter.OnItemClickLisenter;
import com.netease.nim.uikit.common.ui.pinyin.cn.CNPinyin;
import com.netease.nim.uikit.common.ui.pinyin.cn.CNPinyinFactory;
import com.netease.nim.uikit.common.ui.pinyin.stickyheader.StickyHeaderDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class MyBlackListActivity extends UI implements HttpInterface, OnItemClickLisenter {

    private static final String TAG = "SystemMessageActivity2";

    // view
    // view
    private RecyclerView recyclerView;

    private ContactAdapter adapter;


    private View emptyBg;
    private boolean msgLoaded = false;

    private CharIndexView iv_main;
    private TextView tv_index;

    private ArrayList<CNPinyin<Contact>> contactList = new ArrayList<>();


    public static void start(Context context) {
        start(context, null, true);
    }

    public static void start(Context context, Intent extras, boolean clearTop) {
        Intent intent = new Intent();
        intent.setClass(context, MyBlackListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (clearTop) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_black_list_list);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleString = "黑名单管理";
        setToolBar(R.id.toolbar, options);
        initAdapter();
        loadMessages(); // load old data
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private void initAdapter() {
        adapter = new ContactAdapter();
        adapter.setOnItemClickLisenter(this);
        adapter.setCnPinyinList(contactList);

        recyclerView = findView(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        emptyBg = findView(R.id.emptyBg);

        iv_main = (CharIndexView) findViewById(R.id.iv_main);
        tv_index = (TextView) findViewById(R.id.tv_index);
        iv_main.setOnCharIndexChangedListener(new CharIndexView.OnCharIndexChangedListener() {
            @Override
            public void onCharIndexChanged(char currentIndex) {
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).getFirstChar() == currentIndex) {
                        linearLayoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }

            @Override
            public void onCharIndexSelected(String currentIndex) {
                if (currentIndex == null) {
                    tv_index.setVisibility(View.INVISIBLE);
                } else {
                    tv_index.setVisibility(View.VISIBLE);
                    tv_index.setText(currentIndex);
                }
            }
        });

        recyclerView.addItemDecoration(new StickyHeaderDecoration(adapter));
    }

    /**
     * 加载历史消息
     */
    public void loadMessages() {
        HttpClient.queryBlackList("",this, RequestCommandCode.QUERY_BLACK_LIST);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode){
            case RequestCommandCode.QUERY_BLACK_LIST:
                contactList.clear();
                List<Contact> contacts = new ArrayList<>();
                JSONArray objects = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()));
                if (objects==null) return;
                for (int i = 0; i < objects.size(); i++) {
                    List<BlackListUserBean> items = JSON.parseArray(JSON.toJSONString(objects.get(i)), BlackListUserBean.class);

                    for (int j = 0; j <items.size() ; j++) {
                        Contact contact = new Contact(items.get(j).getUsername(),items.get(j).getUserId()+"",items.get(j).getHeadImage(),"");
                        contacts.add(contact);
                    }
                }

                contactList.addAll(CNPinyinFactory.createCNPinyinList(contacts));
                refresh();
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {
        ToastHelper.showToast(this, message);
    }

    @Override
    public void onComplete() {

    }

    private void refresh() {
        adapter.setCnPinyinList(contactList);
        adapter.notifyDataSetChanged();
        boolean empty = contactList.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }



    @Override
    public void onItemClick(int position) {
        UserProfileActivity2.start(this, contactList.get(position).data.id+"");
    }
}
