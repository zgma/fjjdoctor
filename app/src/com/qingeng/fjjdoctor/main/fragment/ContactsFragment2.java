package com.qingeng.fjjdoctor.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.netease.nim.uikit.business.contact.core.item.ItemTypes;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;
import com.qingeng.fjjdoctor.main.activity.SystemMessageActivity2;
import com.qingeng.fjjdoctor.main.activity.TeamListActivity;
import com.qingeng.fjjdoctor.main.reminder.ReminderItem;
import com.qingeng.fjjdoctor.main.reminder.ReminderManager;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.pinyin.CharIndexView;
import com.netease.nim.uikit.common.ui.pinyin.Contact;
import com.netease.nim.uikit.common.ui.pinyin.adapter.ContactAdapter;
import com.netease.nim.uikit.common.ui.pinyin.cn.CNPinyin;
import com.netease.nim.uikit.common.ui.pinyin.cn.CNPinyinFactory;
import com.netease.nim.uikit.common.ui.pinyin.stickyheader.StickyHeaderDecoration;
import com.qingeng.fjjdoctor.user.MyPhoneActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 通讯录Fragment
 * <p/>
 * Created by huangjun on 2015/9/7.
 */
public class ContactsFragment2 extends TFragment implements HttpInterface, ContactAdapter.OnItemClickLisenter, ReminderManager.UnreadNumChangedCallback {

    // view
    // view
    private RecyclerView recyclerView;

    private ContactAdapter adapter;


    private View loadingFrame;
    private boolean msgLoaded = false;

    private CharIndexView iv_main;
    private TextView tv_index;

    private ArrayList<CNPinyin<Contact>> contactList = new ArrayList<>();

    /**
     * ***************************************** 生命周期 *****************************************
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.my_contacts, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 界面初始化

        initAdapter();
        loadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void initAdapter() {
        adapter = new ContactAdapter();
        adapter.setOnItemClickLisenter(this);
        adapter.setCnPinyinList(contactList);
        adapter.setShowBottomCount(true);
        recyclerView = findView(com.qingeng.fjjdoctor.R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        loadingFrame = findView(com.qingeng.fjjdoctor.R.id.contact_loading_frame);

        iv_main = (CharIndexView) findView(com.qingeng.fjjdoctor.R.id.iv_main);
        tv_index = (TextView) findView(com.qingeng.fjjdoctor.R.id.tv_index);
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
     * *********************************** 通讯录加载控制 *******************************
     */

    /**
     * 加载历史消息
     */
    public void loadData() {
        HttpClient.queryMyFriends(null, this, RequestCommandCode.QUERY_MY_FRIENDS);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.QUERY_MY_FRIENDS:
/*                JSONArray objects = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()));
                for (int i = 0; i < objects.size(); i++) {
                    JSONArray array = JSON.parseArray(JSON.toJSONString(objects.get(i)));
                    List<UserBean> users = JSON.parseArray(JSON.toJSONString(array), UserBean.class);
                    userBeans.addAll(users);
                }
                adapter.setmData(userBeans);
                notifyDataSetChanged();*/

                contactList.clear();
                List<Contact> contacts = new ArrayList<>();
                JSONArray objects = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()));
                if (objects != null){
                    for (int i = 0; i < objects.size(); i++) {
                        List<UserBean> items = JSON.parseArray(JSON.toJSONString(objects.get(i)), UserBean.class);
                        for (int j = 0; j < items.size(); j++) {
                            /*Contact contact = new Contact(items.get(j).getUsername(), items.get(j).getUserId() + "", items.get(j).getHeadImage(), items.get(j).getAlias());
                            contacts.add(contact);*/
                        }
                    }
                    contactList.addAll(CNPinyinFactory.createCNPinyinList(contacts));

                }

                Contact contactTop0 = new Contact("新的朋友", "123", R.drawable.head_img_new_friends, "新的朋友");
                Contact contactTop1 = new Contact("群聊", "123", R.drawable.head_img_group_chat, "群聊");
                Contact contactTop2 = new Contact("手机通讯录", "123", R.drawable.head_img_phone_book, "手机通讯录");

                Collections.sort(contactList, (a, b) -> {
                    if (a.equals(b)) return 0;
                    if (a.getFirstChar() > b.getFirstChar()) {
                        return 1;
                    } else if (a.getFirstChar() < b.getFirstChar()) {
                        return -1;
                    } else {
                        return a.compareTo(b);
                    }
                });
                contactList.add(0, new CNPinyin<>(contactTop0));
                contactList.add(1, new CNPinyin<>(contactTop1));
                contactList.add(2, new CNPinyin<>(contactTop2));

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
    }

    @Override
    public void onItemClick(int position) {
        if (position == 0) {
            SystemMessageActivity2.start(getContext());
        } else if (position == 1) {
            TeamListActivity.start(getContext(), ItemTypes.TEAMS.ADVANCED_TEAM);
        } else if (position == 2) {
            MyPhoneActivity.start(getContext());
        } else {
            UserProfileActivity2.start(getContext(), contactList.get(position).data.id + "");
        }
    }

    private void refresh() {
        adapter.setCnPinyinList(contactList);
        adapter.notifyDataSetChanged();
        boolean empty = contactList.isEmpty() && msgLoaded;
        loadingFrame.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onUnreadNumChanged(ReminderItem item) {

    }
}
