package com.qingeng.fjjdoctor.session.recent;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.team.TeamDataChangedObserver;
import com.netease.nim.uikit.api.model.team.TeamMemberDataChangedObserver;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.session.TeamMemberAitHelper;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.NoticeBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.contact.activity.AddFriendMainActivity;
import com.qingeng.fjjdoctor.main.activity.GlobalSearchActivity;
import com.qingeng.fjjdoctor.main.activity.MainActivity;
import com.qingeng.fjjdoctor.notice.NoticeActivity;
import com.qingeng.fjjdoctor.session.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.common.CommonUtil;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.badger.Badger;
import com.netease.nim.uikit.common.fragment.TFragment;
import com.netease.nim.uikit.common.ui.drop.DropCover;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.ui.popwindow.ActionItem;
import com.netease.nim.uikit.common.ui.popwindow.TitlePopup;
import com.netease.nim.uikit.common.ui.recyclerview.listener.SimpleClickListener;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.team.model.TeamMember;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.user.MyCodeActivity;
import com.qingeng.fjjdoctor.user.survey.SurveyListActivity;
import com.qingeng.fjjdoctor.util.ImageFrameUtils;
import com.qingeng.fjjdoctor.util.UiUtils;
import com.qingeng.fjjdoctor.zoom.EducationMainActivity;
import com.qingeng.fjjdoctor.zoom.VipRechargeActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 最近联系人列表(会话列表)
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class RecentContactsFragment extends TFragment implements HttpInterface, View.OnClickListener {

    // 置顶功能可直接使用，也可作为思路，供开发者充分利用RecentContact的tag字段
    public static final long RECENT_TAG_STICKY = 0x0000000000000001; // 联系人置顶tag


    @BindView(R.id.iv_banner)
    ImageView iv_banner;

    @BindView(R.id.layout_1)
    LinearLayout layout_1;
    @BindView(R.id.layout_2)
    LinearLayout layout_2;
    @BindView(R.id.layout_3)
    LinearLayout layout_3;
    @BindView(R.id.layout_4)
    RelativeLayout layout_4;

    @BindView(R.id.layout_survey)
    RelativeLayout layout_survey;

    @BindView(R.id.tv_notice)
    TextView tv_notice;
    @BindView(R.id.tv_notice_more)
    TextView tv_notice_more;

    RelativeLayout layout_title_2;
    ImageView iv_search;
    ImageView iv_notice;
    TextView tv_status;

    TitlePopup titlePopup;

    // view
    private RecyclerView recyclerView;

    private View emptyBg;

    // data
    private List<RecentContact> items;

    private Map<String, RecentContact> cached; // 暂缓刷上列表的数据（未读数红点拖拽动画运行时用）

    private RecentContactAdapter adapter;

    private boolean msgLoaded = false;

    private RecentContactsCallback callback;

    private UserInfoObserver userInfoObserver;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        initMessageList();
        requestMessages(true);
        registerObservers(true);
        registerDropCompletedListener(true);
        registerOnlineStateChangeListener(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nim_recent_contacts, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    private void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        boolean empty = items.isEmpty() && msgLoaded;
        emptyBg.setVisibility(empty ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        registerObservers(false);
        registerDropCompletedListener(false);
        registerOnlineStateChangeListener(false);
        DropManager.getInstance().setDropListener(null);
    }

    /**
     * 查找页面控件
     */
    private void findViews() {
        recyclerView = findView(R.id.recycler_view);
        emptyBg = findView(R.id.emptyBg);

        layout_survey.setOnClickListener(this);
        initPopData();
        getNoticeList();
    }


    /**
     * 初始化数据
     */
    private void initPopData() {
        tv_status = findView(R.id.tv_status);
        iv_search = findView(R.id.iv_search);
        iv_notice = findView(R.id.iv_notice);
        iv_notice.setVisibility(View.GONE);
        layout_title_2 = findView(R.id.layout_title_2);
        tv_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titlePopup.show(v);
            }
        });
        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalSearchActivity.start(getActivity());
            }
        });
        tv_notice_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoticeActivity.start(getActivity());
            }
        });
        titlePopup = new TitlePopup(getActivity(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, R.color.colorPrimary);

        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(getActivity(), "添加朋友", R.mipmap.home_add_friend, R.color.white));
        titlePopup.addAction(new ActionItem(getActivity(), "创建群聊", R.mipmap.home_create_group, R.color.white));
//        titlePopup.addAction(new ActionItem(this, "群发助手", R.mipmap.menu_icon_send));
        titlePopup.addAction(new ActionItem(getActivity(), "扫 一 扫  ", R.mipmap.home_scan, R.color.white));
        titlePopup.addAction(new ActionItem(getActivity(), "会员充值  ", R.mipmap.vip_recharge, R.color.white));

        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                switch (position) {
                    case 0:
                        AddFriendMainActivity.start(getActivity());
                        break;
                    case 1:
   /*                     ContactSelectActivity.Option advancedOption = TeamHelper
                                .getCreateContactSelectOption(null, 50);
                        NimUIKit.startContactSelector(getActivity(), advancedOption,
                                REQUEST_CODE_ADVANCED);*/
                        break;
                    case 2:
                        break;
                    case 3:
                        VipRechargeActivity.start(getActivity());
                        break;
                }
            }
        });
    }


    /**
     * 初始化消息列表
     */
    private void initMessageList() {

        //设置banner
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) iv_banner.getLayoutParams();
        layoutParams.height = UiUtils.getBannerHeight(getActivity());
        iv_banner.setLayoutParams(layoutParams);
        ImageFrameUtils.showImageToView(iv_banner, R.mipmap.home_banner);


        items = new ArrayList<>();
        cached = new HashMap<>(3);
        // adapter
        adapter = new RecentContactAdapter(recyclerView, items);
        initCallBack();
        adapter.setCallback(callback);
        // recyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(touchListener);
        // drop listener
        DropManager.getInstance().setDropListener(new DropManager.IDropListener() {

            @Override
            public void onDropBegin() {
                touchListener.setShouldDetectGesture(false);
            }

            @Override
            public void onDropEnd() {
                touchListener.setShouldDetectGesture(true);
            }
        });

        layout_1.setOnClickListener(this);
        layout_2.setOnClickListener(this);
        layout_3.setOnClickListener(this);
        layout_4.setOnClickListener(this);
    }

    private void initCallBack() {
        if (callback != null) {
            return;
        }
        callback = new RecentContactsCallback() {

            @Override
            public void onRecentContactsLoaded() {
            }

            @Override
            public void onUnreadCountChange(int unreadCount) {
            }

            @Override
            public void onItemClick(RecentContact recent) {
                if (recent.getSessionType() == SessionTypeEnum.SUPER_TEAM) {
                    ToastHelper.showToast(getActivity(), getString(R.string.super_team_impl_by_self));
                } else if (recent.getSessionType() == SessionTypeEnum.Team) {
                    NimUIKit.startTeamSession(getActivity(), recent.getContactId());
                } else if (recent.getSessionType() == SessionTypeEnum.P2P) {
                    NimUIKit.startP2PSession(getActivity(), recent.getContactId());
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                return null;
            }
        };
    }

    private SimpleClickListener<RecentContactAdapter> touchListener = new SimpleClickListener<RecentContactAdapter>() {

        @Override
        public void onItemClick(RecentContactAdapter adapter, View view, int position) {
            if (callback != null) {
                RecentContact recent = adapter.getItem(position);
                callback.onItemClick(recent);
            }
        }

        @Override
        public void onItemLongClick(RecentContactAdapter adapter, View view, int position) {
            showLongClickMenu(adapter.getItem(position), position, view);
        }

        @Override
        public void onItemChildClick(RecentContactAdapter adapter, View view, int position) {
        }

        @Override
        public void onItemChildLongClick(RecentContactAdapter adapter, View view, int position) {
        }
    };

    OnlineStateChangeObserver onlineStateChangeObserver = accounts -> notifyDataSetChanged();

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver,
                register);
    }

    private void showLongClickMenu(final RecentContact recent, final int position, View view) {
        /*CustomAlertDialog alertDialog = new CustomAlertDialog(getActivity());
        alertDialog.setTitle(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));
        String title = getString(R.string.main_msg_list_delete_chatting);
        alertDialog.addItem(title, () -> {
            // 删除会话，删除后，消息历史被一起删除
            NIMClient.getService(MsgService.class).deleteRecentContact(recent);
            NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
            adapter.remove(position);
            postRunnable(() -> refreshMessages(true));
        });
        title = (CommonUtil.isTagSet(recent, RECENT_TAG_STICKY) ? getString(
                R.string.main_msg_list_clear_sticky_on_top) : getString(R.string.main_msg_list_sticky_on_top));
        alertDialog.addItem(title, () -> {
            if (CommonUtil.isTagSet(recent, RECENT_TAG_STICKY)) {
                CommonUtil.removeTag(recent, RECENT_TAG_STICKY);
                setTop(recent.getContactId(),recent.getSessionType(),false);
            } else {
                CommonUtil.addTag(recent, RECENT_TAG_STICKY);
                setTop(recent.getContactId(),recent.getSessionType(),true);
            }
            NIMClient.getService(MsgService.class).updateRecent(recent);
            refreshMessages(false);
        });
        String itemText = getString(R.string.delete_chat_only_server);
        alertDialog.addItem(itemText, () -> NIMClient.getService(MsgService.class)
                                                     .deleteRoamingRecentContact(recent.getContactId(),
                                                                                 recent.getSessionType())
                                                     .setCallback(new RequestCallback<Void>() {

                                                         @Override
                                                         public void onSuccess(Void param) {
                                                             ToastHelper.showToast(getActivity(), "delete success");
                                                         }

                                                         @Override
                                                         public void onFailed(int code) {
                                                             ToastHelper.showToast(getActivity(),
                                                                                   "delete failed, code:" + code);
                                                         }

                                                         @Override
                                                         public void onException(Throwable exception) {
                                                         }
                                                     }));
        alertDialog.show();*/
        TitlePopup titlePopup = new TitlePopup(view.getContext(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        initTitlePopup(titlePopup, view.getContext(), recent, position);
        titlePopup.show(view);

    }


    private void initTitlePopup(TitlePopup titlePopup, Context context, final RecentContact recent, final int recentPosition) {
        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(context, CommonUtil.isTagSet(recent, RECENT_TAG_STICKY) ? "取消置顶" : "置顶"));
        titlePopup.addAction(new ActionItem(context, "删除"));

        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                switch (position) {
                    case 0:
                        if (CommonUtil.isTagSet(recent, RECENT_TAG_STICKY)) {
                            CommonUtil.removeTag(recent, RECENT_TAG_STICKY);
                            setTop(recent.getContactId(), recent.getSessionType(), false);
                        } else {
                            CommonUtil.addTag(recent, RECENT_TAG_STICKY);
                            setTop(recent.getContactId(), recent.getSessionType(), true);
                        }
                        NIMClient.getService(MsgService.class).updateRecent(recent);
                        refreshMessages(false);

                        break;
                    case 1:

                        // 删除会话，删除后，消息历史被一起删除
                        NIMClient.getService(MsgService.class).deleteRecentContact(recent);
                        NIMClient.getService(MsgService.class).clearChattingHistory(recent.getContactId(), recent.getSessionType());
                        adapter.remove(recentPosition);
                        postRunnable(() -> refreshMessages(true));
                        break;
                }
            }
        });
    }

    private List<RecentContact> loadedRecents;

    private void requestMessages(boolean delay) {
        if (msgLoaded) {
            return;
        }
        getHandler().postDelayed(() -> {
            if (msgLoaded) {
                return;
            }
            // 查询最近联系人列表数据
            NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(
                    new RequestCallbackWrapper<List<RecentContact>>() {

                        @Override
                        public void onResult(int code, List<RecentContact> recents, Throwable exception) {
                            if (code != ResponseCode.RES_SUCCESS || recents == null) {
                                return;
                            }
                            loadedRecents = recents;
                            // 初次加载，更新离线的消息中是否有@我的消息
                            for (RecentContact loadedRecent : loadedRecents) {
                                if (loadedRecent.getSessionType() == SessionTypeEnum.Team) {
                                    updateOfflineContactAited(loadedRecent);
                                }
                            }
                            // 此处如果是界面刚初始化，为了防止界面卡顿，可先在后台把需要显示的用户资料和群组资料在后台加载好，然后再刷新界面
                            //
                            msgLoaded = true;
                            if (isAdded()) {
                                onRecentContactsLoaded();
                            }
                        }
                    });
        }, delay ? 250 : 0);
    }

    private void onRecentContactsLoaded() {
        items.clear();
        if (loadedRecents != null) {
            items.addAll(loadedRecents);
            loadedRecents = null;
        }
        refreshMessages(true);
        if (callback != null) {
            callback.onRecentContactsLoaded();
        }
    }

    private void refreshMessages(boolean unreadChanged) {
        sortRecentContacts(items);
        notifyDataSetChanged();
        if (unreadChanged) {
            // 方式一：累加每个最近联系人的未读（快）
            int unreadNum = 0;
            for (RecentContact r : items) {
                unreadNum += r.getUnreadCount();
            }
            // 方式二：直接从SDK读取（相对慢）
            //int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
            if (callback != null) {
                callback.onUnreadCountChange(unreadNum);
            }
            Badger.updateBadgerCount(unreadNum);
        }
    }

    /**
     * **************************** 排序 ***********************************
     */
    private void sortRecentContacts(List<RecentContact> list) {
        if (list.size() == 0) {
            return;
        }
        Collections.sort(list, comp);
    }

    private static Comparator<RecentContact> comp = (o1, o2) -> {
        // 先比较置顶tag
        long sticky = (o1.getTag() & RECENT_TAG_STICKY) - (o2.getTag() & RECENT_TAG_STICKY);
        if (sticky != 0) {
            return sticky > 0 ? -1 : 1;
        } else {
            long time = o1.getTime() - o2.getTime();
            return time == 0 ? 0 : (time > 0 ? -1 : 1);
        }
    };

    /**
     * ********************** 收消息，处理状态变化 ************************
     */
    private void registerObservers(boolean register) {
        MsgServiceObserve service = NIMClient.getService(MsgServiceObserve.class);
        service.observeReceiveMessage(messageReceiverObserver, register);
        service.observeRecentContact(messageObserver, register);
        service.observeMsgStatus(statusObserver, register);
        service.observeRecentContactDeleted(deleteObserver, register);
        registerTeamUpdateObserver(register);
        registerTeamMemberUpdateObserver(register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
    }

    /**
     * 注册群信息&群成员更新监听
     */
    private void registerTeamUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamDataChangedObserver(teamDataChangedObserver, register);
    }

    private void registerTeamMemberUpdateObserver(boolean register) {
        NimUIKit.getTeamChangedObservable().registerTeamMemberDataChangedObserver(teamMemberDataChangedObserver,
                register);
    }

    private void registerDropCompletedListener(boolean register) {
        if (register) {
            DropManager.getInstance().addDropCompletedListener(dropCompletedListener);
        } else {
            DropManager.getInstance().removeDropCompletedListener(dropCompletedListener);
        }
    }

    // 暂存消息，当RecentContact 监听回来时使用，结束后清掉
    private Map<String, Set<IMMessage>> cacheMessages = new HashMap<>();

    //监听在线消息中是否有@我
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {

        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                for (IMMessage imMessage : imMessages) {
                    if (!TeamMemberAitHelper.isAitMessage(imMessage)) {
                        continue;
                    }
                    Set<IMMessage> cacheMessageSet = cacheMessages.get(imMessage.getSessionId());
                    if (cacheMessageSet == null) {
                        cacheMessageSet = new HashSet<>();
                        cacheMessages.put(imMessage.getSessionId(), cacheMessageSet);
                    }
                    cacheMessageSet.add(imMessage);
                }
            }
        }
    };

    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {

        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            if (!DropManager.getInstance().isTouchable()) {
                // 正在拖拽红点，缓存数据
                for (RecentContact r : recentContacts) {
                    cached.put(r.getContactId(), r);
                }
                return;
            }
            onRecentContactChanged(recentContacts);
        }
    };

    private void onRecentContactChanged(List<RecentContact> recentContacts) {
        int index;
        for (RecentContact r : recentContacts) {
            index = -1;
            for (int i = 0; i < items.size(); i++) {
                if (r.getContactId().equals(items.get(i).getContactId()) && r.getSessionType() == (items.get(i)
                        .getSessionType())) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                items.remove(index);
            }
            items.add(r);
            if (r.getSessionType() == SessionTypeEnum.Team && cacheMessages.get(r.getContactId()) != null) {
                TeamMemberAitHelper.setRecentContactAited(r, cacheMessages.get(r.getContactId()));
            }
        }
        cacheMessages.clear();
        refreshMessages(true);
    }

    DropCover.IDropCompletedListener dropCompletedListener = new DropCover.IDropCompletedListener() {

        @Override
        public void onCompleted(Object id, boolean explosive) {
            if (cached != null && !cached.isEmpty()) {
                // 红点爆裂，已经要清除未读，不需要再刷cached
                if (explosive) {
                    if (id instanceof RecentContact) {
                        RecentContact r = (RecentContact) id;
                        cached.remove(r.getContactId());
                    } else if (id instanceof String && ((String) id).contentEquals("0")) {
                        cached.clear();
                    }
                }
                // 刷cached
                if (!cached.isEmpty()) {
                    List<RecentContact> recentContacts = new ArrayList<>(cached.size());
                    recentContacts.addAll(cached.values());
                    cached.clear();
                    onRecentContactChanged(recentContacts);
                }
            }
        }
    };

    Observer<IMMessage> statusObserver = new Observer<IMMessage>() {

        @Override
        public void onEvent(IMMessage message) {
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                RecentContact item = items.get(index);
                item.setMsgStatus(message.getStatus());
                refreshViewHolderByIndex(index);
            }
        }
    };

    Observer<RecentContact> deleteObserver = new Observer<RecentContact>() {

        @Override
        public void onEvent(RecentContact recentContact) {
            if (recentContact != null) {
                for (RecentContact item : items) {
                    if (TextUtils.equals(item.getContactId(), recentContact.getContactId()) &&
                            item.getSessionType() == recentContact.getSessionType()) {
                        items.remove(item);
                        refreshMessages(true);
                        break;
                    }
                }
            } else {
                items.clear();
                refreshMessages(true);
            }
        }
    };

    TeamDataChangedObserver teamDataChangedObserver = new TeamDataChangedObserver() {

        @Override
        public void onUpdateTeams(List<Team> teams) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeam(Team team) {
        }
    };

    TeamMemberDataChangedObserver teamMemberDataChangedObserver = new TeamMemberDataChangedObserver() {

        @Override
        public void onUpdateTeamMember(List<TeamMember> members) {
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onRemoveTeamMember(List<TeamMember> member) {
        }
    };

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            RecentContact item = items.get(i);
            if (TextUtils.equals(item.getRecentMessageId(), uuid)) {
                return i;
            }
        }
        return -1;
    }

    protected void refreshViewHolderByIndex(final int index) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyItemChanged(index);
            }
        });
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }

    private void registerUserInfoObserver() {
        if (userInfoObserver == null) {
            userInfoObserver = new UserInfoObserver() {

                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    refreshMessages(false);
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, true);
    }

    private void unregisterUserInfoObserver() {
        if (userInfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(userInfoObserver, false);
        }
    }

    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {

        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            refreshMessages(false);
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            refreshMessages(false);
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            refreshMessages(false);
        }
    };

    private void updateOfflineContactAited(final RecentContact recentContact) {
        if (recentContact == null || recentContact.getSessionType() != SessionTypeEnum.Team ||
                recentContact.getUnreadCount() <= 0) {
            return;
        }
        // 锚点
        List<String> uuid = new ArrayList<>(1);
        uuid.add(recentContact.getRecentMessageId());
        List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);
        if (messages == null || messages.size() < 1) {
            return;
        }
        final IMMessage anchor = messages.get(0);
        // 查未读消息
        NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD,
                recentContact.getUnreadCount() - 1, false)
                .setCallback(new RequestCallbackWrapper<List<IMMessage>>() {

                    @Override
                    public void onResult(int code, List<IMMessage> result, Throwable exception) {
                        if (code == ResponseCode.RES_SUCCESS && result != null) {
                            result.add(0, anchor);
                            Set<IMMessage> messages = null;
                            // 过滤存在的@我的消息
                            for (IMMessage msg : result) {
                                if (TeamMemberAitHelper.isAitMessage(msg)) {
                                    if (messages == null) {
                                        messages = new HashSet<>();
                                    }
                                    messages.add(msg);
                                }
                            }
                            // 更新并展示
                            if (messages != null) {
                                TeamMemberAitHelper.setRecentContactAited(recentContact, messages);
                                notifyDataSetChanged();
                            }
                        }
                    }
                });

    }

    /**
     * @param id
     * @param state
     */
    private void setTop(String id, SessionTypeEnum sessionTypeEnum, boolean state) {
        if (sessionTypeEnum == SessionTypeEnum.P2P) {
            HttpClient.setUserTop(id, state ? 1 : 2, this, RequestCommandCode.SET_USER_TOP);
        } else if (sessionTypeEnum == SessionTypeEnum.Team) {
            HttpClient.setGroupTop(id, state ? 1 : 2, this, RequestCommandCode.SET_USER_TOP);
        }
    }

    private void getSysNotice() {
        // HttpClient.lookSystemNotice(this, RequestCommandCode.SYSTEM_NOTICE);
    }

    @Override
    public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
        switch (requestCode) {
            case RequestCommandCode.SYSTEM_NOTICE:
                List<NoticeBean> dataList = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()), NoticeBean.class);
                if (dataList != null && dataList.size() > 0) {
                    tv_notice.setText(dataList.get(0).getNoticeTitle());
                }
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, String message) {

    }

    @Override
    public void onComplete() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_1:
                MyCodeActivity.start(getActivity(), AppPreferences.getAccId());
                break;
            case R.id.layout_2:
                break;
            case R.id.layout_3:
                EducationMainActivity.start(getContext());
                break;
            case R.id.layout_4:
                break;
            case R.id.layout_survey:
                SurveyListActivity.start(getContext());
                break;
        }
    }

    private void getNoticeList() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("pageNum", 1);
        baseRequestBean.addParams("pageSize", MainConstant.PAGE_NUMBER);
        HttpClient.lookSystemNotice(baseRequestBean, this, RequestCommandCode.SYSTEM_NOTICE);
    }
}
