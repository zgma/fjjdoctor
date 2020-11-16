package com.qingeng.fjjdoctor.contact.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.contact.viewholder.BlackListViewHolder;
import com.qingeng.fjjdoctor.util.PhoneDto;
import com.qingeng.fjjdoctor.util.PhoneUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.contact.core.item.ContactIdFilter;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.functions.Consumer;

public class PhoneActivity  extends UI implements TAdapterDelegate {


    private static final int PHONE_PERMISSION_REQUEST_CODE = 1011;

    private static final String[] PHONE_PERMISSIONS = new String[]{
            Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS
    };


    private List<PhoneDto> phoneDtos;

    private static final String TAG = "PhoneActivity";
    private static final int REQUEST_CODE_BLACK = 1;

    private ListView listView;
    private List<UserInfo> data = new ArrayList<>();
    private BlackListAdapter adapter;

    private RxPermissions rxPermissions;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PhoneActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_list_activity);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.black_list;
        setToolBar(R.id.toolbar, options);
        initData();
        findViews();
        initActionbar();

    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return BlackListViewHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    private void initData() {
        final List<String> accounts = NIMClient.getService(FriendService.class).getBlackList();
        List<String> unknownAccounts = new ArrayList<>();

        for (String account : accounts) {
            UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);
            if (userInfo == null) {
                unknownAccounts.add(account);
            } else {
                data.add(userInfo);
            }
        }

        if (!unknownAccounts.isEmpty()) {
            NimUIKit.getUserInfoProvider().getUserInfoAsync(unknownAccounts, new SimpleCallback<List<NimUserInfo>>() {

                @Override
                public void onResult(boolean success, List<NimUserInfo> result, int code) {
                    if (code == ResponseCode.RES_SUCCESS) {
                        data.addAll(result);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    private void initActionbar() {
        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText(R.string.add);
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactSelectActivity.Option option = new ContactSelectActivity.Option();
                option.title = "选择黑名单";
                option.maxSelectNum = 1;
                ArrayList<String> excludeAccounts = new ArrayList<>();
                for (UserInfo user : data) {
                    if (user != null) {
                        excludeAccounts.add(user.getAccount());
                    }
                }
                option.itemFilter = new ContactIdFilter(excludeAccounts, true);
                NimUIKit.startContactSelector(PhoneActivity.this, option, REQUEST_CODE_BLACK);
            }
        });
    }

    private void findViews() {
        TextView notifyText = ((TextView) findView(R.id.notify_bar).findViewById(R.id.status_desc_label));
        notifyText.setText(R.string.black_list_tip);
        notifyText.setBackgroundColor(getResources().getColor(R.color.color_yellow_fcf3cd));
        notifyText.setTextColor(getResources().getColor(R.color.color_yellow_796413));
        listView = findView(R.id.black_list_view);
        adapter = new BlackListAdapter(this, data, this, viewHolderEventListener);
        listView.setAdapter(adapter);

        rxPermissions = new RxPermissions(this);
        rxPermissions.request(PHONE_PERMISSIONS).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    PhoneUtil phoneUtil = new PhoneUtil(PhoneActivity.this);
                    phoneDtos = phoneUtil.getPhone();
                    String[] strings = new String[phoneDtos.size()];
                    for (int i = 0; i <phoneDtos.size() ; i++) {
                        String phone =  phoneDtos.get(i).getTelPhone().replace("-","").replace(" ","");
                        strings[i] = phone.startsWith("+86")?phone.substring(3):phone;
                    }
                    chengExists(Arrays.toString(strings));

                } else {
                    //只要有一个权限被拒绝，就会执行
                    Toast.makeText(PhoneActivity.this, "未授权定位权限，定位功能不能使用", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private BlackListAdapter.ViewHolderEventListener viewHolderEventListener = new BlackListAdapter.ViewHolderEventListener() {
        @Override
        public void onRemove(final UserInfo user) {
            NIMClient.getService(FriendService.class).removeFromBlackList(user.getAccount()).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    ToastHelper.showToast(PhoneActivity.this, "移出黑名单成功");
                    data.remove(user);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(int code) {
                    ToastHelper.showToast(PhoneActivity.this, "移出黑名单失败，错误码：" + code);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }

        @Override
        public void onItemClick(UserInfo userInfo) {
            Log.i(TAG, "onItemClick, user account=" + userInfo.getAccount());
        }
    };

    private void addUserToBlackList(ArrayList<String> selected) {
        for (final String account : selected) {
            NIMClient.getService(FriendService.class).addToBlackList(account).setCallback(new RequestCallback<Void>() {
                @Override
                public void onSuccess(Void param) {
                    data.add(NimUIKit.getUserInfoProvider().getUserInfo(account));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailed(int code) {
                    ToastHelper.showToast(PhoneActivity.this, "加入黑名单失败,code:" + code);
                }

                @Override
                public void onException(Throwable exception) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_BLACK:
                    final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                    if (selected != null && !selected.isEmpty()) {
                        addUserToBlackList(selected);
                    }
                    break;
            }
        }
    }

    private void chengExists(String strs){
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("contactInfo",strs);
        HttpClient.queryPhoneExists(baseRequestBean, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {

                System.out.println(11);
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