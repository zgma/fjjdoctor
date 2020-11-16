package com.qingeng.fjjdoctor.user;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.BlackListUserBean;
import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;
import com.qingeng.fjjdoctor.util.PhoneDto;
import com.qingeng.fjjdoctor.util.PhoneUtil;
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
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * 我的群组
 * <p/>
 * Created by huangjun on 2015/3/18.
 */
public class MyPhoneActivity extends UI implements HttpInterface, OnItemClickLisenter {

    private static final String TAG = "SystemMessageActivity2";

    private static final String[] PHONE_PERMISSIONS = new String[]{
            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS
    };

    private List<PhoneDto> phoneDtos;
    private RxPermissions rxPermissions;


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
        intent.setClass(context, MyPhoneActivity.class);
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
        options.titleString = "已注册的手机联系人";
        setToolBar(R.id.toolbar, options);
        initAdapter();
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


        rxPermissions = new RxPermissions(this);
        rxPermissions.request(PHONE_PERMISSIONS).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    PhoneUtil phoneUtil = new PhoneUtil(MyPhoneActivity.this);
                    phoneDtos = phoneUtil.getPhone();
                    String strings = "";
                    for (int i = 0; i < phoneDtos.size(); i++) {
                        String phone = phoneDtos.get(i).getTelPhone().replace("-", "").replace(" ", "");
                        phone = phone.startsWith("+86") ? phone.substring(3) : phone;
                        strings = strings + "," + phone;
                    }
                    chengExists(strings);

                } else {
                    //只要有一个权限被拒绝，就会执行
                    Toast.makeText(MyPhoneActivity.this, "未授权定位权限，定位功能不能使用", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.QUERY_BLACK_LIST:
                contactList.clear();
                List<Contact> contacts = new ArrayList<>();
                JSONArray objects = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()));
                if (objects == null) return;
                for (int i = 0; i < objects.size(); i++) {
                    List<BlackListUserBean> items = JSON.parseArray(JSON.toJSONString(objects.get(i)), BlackListUserBean.class);

                    for (int j = 0; j < items.size(); j++) {
                        Contact contact = new Contact(items.get(j).getUsername(), items.get(j).getUserId() + "", items.get(j).getHeadImage(), "");
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
        adapter.setPhone(true);
        adapter.notifyDataSetChanged();
        boolean empty = contactList.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onItemClick(int position) {
        UserProfileActivity2.start(this, contactList.get(position).data.id + "");
    }


    private void chengExists(String strs) {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("contactInfo", strs);
        HttpClient.queryPhoneExists(baseRequestBean, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                contactList.clear();
                List<Contact> contacts = new ArrayList<>();
                List<UserBean> items = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), UserBean.class);
                /*for (int i = 0; i < items.size(); i++) {
                    for (int j = 0; j < phoneDtos.size(); j++) {
                        String phone = phoneDtos.get(j).getTelPhone().replace("-", "").replace(" ", "");
                        phone = phone.startsWith("+86") ? phone.substring(3) : phone;
                        if (items.get(i).getMobile().equals(phone)) {
                            items.get(i).setUsername(phoneDtos.get(j).getName());
                            if (items.get(i).getHeadImage()!=null){
                                Contact contact = new Contact(items.get(i).getUsername(), items.get(i).getUserId()+"", items.get(i).getHeadImage(), items.get(i).getMobile());
                                contacts.add(contact);
                            }
                            break;
                        }
                    }
                }
                contactList.addAll(CNPinyinFactory.createCNPinyinList(contacts));
                refresh();*/
     /*           for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getUserId() !=0 && items.get(i).getUserId() != Preferences.getUserBean().getUserId()){
                        Contact contact = new Contact(items.get(i).getUsername(), items.get(i).getUserId() + "",items.get(i).getHeadImage(),items.get(i).getAlias() ,items.get(i).getMobile());
                        contacts.add(contact);
                    }
                }*/
                if (contacts.size()>0){
                    contactList.addAll(CNPinyinFactory.createCNPinyinList(contacts));
                    refresh();
                }
            }

            @Override
            public void onFailure(int requestCode, String message) {

            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.QUERY_PHONE_EXISTS);
    }
}
