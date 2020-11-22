package com.qingeng.fjjdoctor.main.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qingeng.apilibrary.bean.ApplyBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.main.activity.ContactMainSearchActivity;
import com.qingeng.fjjdoctor.main.activity.SystemMessageActivity2;
import com.qingeng.fjjdoctor.main.helper.SystemMessageUnreadManager;
import com.qingeng.fjjdoctor.main.model.MainTab;
import com.qingeng.fjjdoctor.main.reminder.ReminderItem;
import com.qingeng.fjjdoctor.main.reminder.ReminderManager;
import com.qingeng.fjjdoctor.main.viewholder.FuncViewHolder;
import com.qingeng.fjjdoctor.session.SessionHelper;
import com.qingeng.fjjdoctor.user.MyBlackListActivity;
import com.qingeng.fjjdoctor.user.MyGroupActivity;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.SystemMessageService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


/**
 * 集成通讯录列表
 * <p/>
 * Created by huangjun on 2015/9/7.
 */
public class ContactListFragment extends MainTabFragment implements View.OnClickListener, ReminderManager.UnreadNumChangedCallback {

//    private ContactsFragment fragment;
    private ContactsFragment2 fragment;

    private TextView unreadNum;
    private LinearLayout cardAdd;
    private LinearLayout cardGroup;
    private LinearLayout cardService;
    private CardView cardBlackList;
    private ConstraintLayout layout_my_black_list;

    private RelativeLayout search_layout;


    public ContactListFragment() {
        setContainerId(MainTab.CONTACT.fragmentId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent(); // 触发onInit，提前加载

        registerMsgUnreadInfoObserver(true);
        requestSystemMessageUnreadCount();

    }

    @Override
    protected void onInit() {
        addContactFragment();  // 集成通讯录页面
    }

    // 将通讯录列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
    private void addContactFragment() {

        unreadNum = getView().findViewById(R.id.tab_new_msg_label);
        cardAdd = getView().findViewById(R.id.card_add);
        cardGroup = getView().findViewById(R.id.card_group);
        cardService = getView().findViewById(R.id.card_setvice);
        cardBlackList = getView().findViewById(R.id.card_blacklist);
        layout_my_black_list = getView().findViewById(R.id.layout_my_black_list);

        cardAdd.setOnClickListener(this);
        cardGroup.setOnClickListener(this);
        cardService.setOnClickListener(this);
        cardBlackList.setOnClickListener(this);
        layout_my_black_list.setOnClickListener(this);


        fragment = new ContactsFragment2();
//        fragment = new ContactsFragment();

        fragment.setContainerId(R.id.contact_fragment);
        //fragment.setDataChangeLinstenter(this);

        UI activity = (UI) getActivity();

        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
        fragment = (ContactsFragment2) activity.addFragment(fragment);

/*        fragment = (ContactsFragment) activity.addFragment(fragment);
        // 功能项定制
        fragment.setContactsCustomization(new ContactsCustomization() {
            @Override
            public Class<? extends AbsContactViewHolder<? extends AbsContactItem>> onGetFuncViewHolderClass() {
                return FuncViewHolder.class;
            }

            @Override
            public List<AbsContactItem> onGetFuncItems() {
                return FuncViewHolder.FuncItem.provide();
            }

            @Override
            public void onFuncItemClick(AbsContactItem item) {
                FuncViewHolder.FuncItem.handle(getActivity(), item);
            }
        });*/

        search_layout = getView().findViewById(R.id.search_layout);
        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactMainSearchActivity.start(getActivity());
            }
        });

    }

    @Override
    public void onCurrentTabClicked() {
        // 点击切换到当前TAB
       /* if (fragment != null) {
            fragment.scrollToTop();
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FuncViewHolder.unRegisterUnreadNumChangedCallback();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.card_add:
//                SystemMessageActivity.start(getActivity());
                SystemMessageActivity2.start(getActivity());
                break;
            case R.id.card_group:
                MyGroupActivity.start(getActivity());
                //TeamListActivity.start(getActivity(), ItemTypes.TEAMS.ADVANCED_TEAM);
                break;
            case R.id.card_setvice:
                DialogMaker.showProgressDialog(getActivity(), "获取客服账号...");
                HttpClient.getServiceInfo(new HttpInterface() {
                    @Override
                    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                        UserBean userBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), UserBean.class);
                       /* if (userBean != null && userBean.getAccid() != null) {
                            SessionHelper.startP2PSession(getActivity(), userBean.getAccid());
                        } else {
                            ToastHelper.showToast(getActivity(), "获取客服账号失败");
                        }*/
                    }

                    @Override
                    public void onFailure(int requestCode, String message) {
                        ToastHelper.showToast(getActivity(), "获取客服账号失败 " + message);

                    }

                    @Override
                    public void onComplete() {
                        DialogMaker.dismissProgressDialog();

                    }
                }, RequestCommandCode.GET_SERVICE_INFO);

                break;
            case R.id.card_blacklist:
            case R.id.layout_my_black_list:
//                BlackListActivity.start(getActivity());
                MyBlackListActivity.start(getActivity());
                break;
        }
    }

    @Subscribe
    public void onSidOutA(String message) {
        if (message.equals("sysMsgUnreadCountChangedObserver")) {
            int unreadCount = SystemMessageUnreadManager.getInstance().getSysMsgUnreadCount();
            if (unreadCount > 0) {
                unreadNum.setVisibility(View.VISIBLE);
                unreadNum.setText("" + unreadCount);
            } else {
                unreadNum.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }



    /**
     * 注册未读消息数量观察者
     */
    private void registerMsgUnreadInfoObserver(boolean register) {
        if (register) {
            ReminderManager.getInstance().registerUnreadNumChangedCallback(this);
        } else {
            ReminderManager.getInstance().unregisterUnreadNumChangedCallback(this);
        }
    }

    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class)
                .querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    @Override
    public void onUnreadNumChanged(ReminderItem item) {

    }


    /**
     * 加载历史消息
     */
    public void loadMessages() {
        HttpClient.queryFriendTodoList(new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                List<ApplyBean> items = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), ApplyBean.class);

            }

            @Override
            public void onFailure(int requestCode, String message) {

            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.QUERY_FRIEND_TODO_LIST);
    }

}
