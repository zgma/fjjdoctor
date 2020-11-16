package com.qingeng.fjjdoctor.main.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qingeng.fjjdoctor.session.recent.RecentContactsCallback;
import com.qingeng.fjjdoctor.session.recent.RecentContactsFragment;
import com.netease.nim.uikit.business.team.activity.AdvancedTeamUpgradeActivity;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.qingeng.apilibrary.bean.BaseRequestBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.bean.NoticeBean;
import com.qingeng.apilibrary.bean.TargetUserBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.login.LoginActivity;
import com.qingeng.fjjdoctor.login.LogoutHelper;
import com.qingeng.fjjdoctor.main.activity.GlobalSearchActivity;
import com.qingeng.fjjdoctor.main.activity.MainActivity;
import com.qingeng.fjjdoctor.main.activity.MultiportActivity;
import com.qingeng.fjjdoctor.main.model.MainTab;
import com.qingeng.fjjdoctor.main.reminder.ReminderManager;
import com.qingeng.fjjdoctor.notice.NoticeActivity;
import com.qingeng.fjjdoctor.session.SessionHelper;
import com.qingeng.fjjdoctor.session.extension.GuessAttachment;
import com.qingeng.fjjdoctor.session.extension.MultiRetweetAttachment;
import com.qingeng.fjjdoctor.session.extension.RTSAttachment;
import com.qingeng.fjjdoctor.session.extension.RedPacketAttachment;
import com.qingeng.fjjdoctor.session.extension.RedPacketOpenedAttachment;
import com.qingeng.fjjdoctor.session.extension.SnapChatAttachment;
import com.qingeng.fjjdoctor.session.extension.StickerAttachment;
import com.qingeng.fjjdoctor.session.extension.TransferAttachment;
import com.qingeng.fjjdoctor.user.MyExtensionActivity;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
import com.qingeng.fjjdoctor.zoom.PartnerMainActivity;
import com.qingeng.fjjdoctor.zoom.ZoomMainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoujianghua on 2015/8/17.
 */
public class SessionListFragment extends MainTabFragment {

    private static final String TAG = SessionListFragment.class.getSimpleName();
    private View notifyBar;

    private TextView notifyBarText;

    // 同时在线的其他端的信息
    private List<OnlineClient> onlineClients;

    private View multiportBar;

    private RecentContactsFragment fragment;

    private RelativeLayout search_layout;


    protected HeadImageView imgHead;

    protected TextView tvNickname;
    protected TextView tv_new_unread;

    protected TextView tvMessage;

    public SessionListFragment() {
        this.setContainerId(MainTab.RECENT_CONTACTS.fragmentId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent();
    }

    @Override
    public void onDestroy() {
        registerObservers(false);
        super.onDestroy();
    }

    @Override
    protected void onInit() {
        findViews();
        registerObservers(true);

        addRecentContactsFragment();

        getListData();
    }

    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOtherClients(clientsObserver, register);
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    private void findViews() {

        imgHead = getView().findViewById(R.id.img_head);
        tvNickname = getView().findViewById(R.id.tv_nickname);
        tv_new_unread = getView().findViewById(R.id.tv_new_unread);
        tvMessage = getView().findViewById(R.id.tv_message);

        search_layout = getView().findViewById(R.id.search_layout);
        search_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalSearchActivity.start(getActivity());
            }
        });

        notifyBar = getView().findViewById(R.id.status_notify_bar);
        notifyBarText = getView().findViewById(R.id.status_desc_label);
        notifyBar.setVisibility(View.GONE);

        multiportBar = getView().findViewById(R.id.multiport_notify_bar);
        multiportBar.setVisibility(View.GONE);
        multiportBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiportActivity.startActivity(getActivity(), onlineClients);
            }
        });
    }

    /**
     * 用户状态变化
     */
    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {

        @Override
        public void onEvent(StatusCode code) {
            if (code.wontAutoLogin()) {
                kickOut(code);
            } else {
                if (code == StatusCode.NET_BROKEN) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.net_broken);
                } else if (code == StatusCode.UNLOGIN) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.nim_status_unlogin);
                } else if (code == StatusCode.CONNECTING) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.nim_status_connecting);
                } else if (code == StatusCode.LOGINING) {
                    notifyBar.setVisibility(View.VISIBLE);
                    notifyBarText.setText(R.string.nim_status_logining);
                } else {
                    notifyBar.setVisibility(View.GONE);
                }

                notifyBar.setVisibility(View.GONE);

            }
        }
    };

    Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
        @Override
        public void onEvent(List<OnlineClient> onlineClients) {
            SessionListFragment.this.onlineClients = onlineClients;
            if (onlineClients == null || onlineClients.size() == 0) {
                multiportBar.setVisibility(View.GONE);
            } else {
                multiportBar.setVisibility(View.VISIBLE);
                TextView status = multiportBar.findViewById(R.id.multiport_desc_label);
                OnlineClient client = onlineClients.get(0);

                for (OnlineClient temp : onlineClients) {
                    Log.d(TAG, "type : " + temp.getClientType() + " , customTag : " + temp.getCustomTag());
                }

                switch (client.getClientType()) {
                    case ClientType.Windows:
                    case ClientType.MAC:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.computer_version));
                        break;
                    case ClientType.Web:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.web_version));
                        break;
                    case ClientType.iOS:
                    case ClientType.Android:
                        status.setText(getString(R.string.multiport_logging) + getString(R.string.mobile_version));
                        break;
                    default:
                        multiportBar.setVisibility(View.GONE);
                        break;
                }
            }
        }
    };

    private void kickOut(StatusCode code) {
        AppPreferences.saveHttpToken("");
        if (code == StatusCode.PWD_ERROR) {
            LogUtil.e("Auth", "user password error");
            ToastHelper.showToast(getActivity(), R.string.login_failed);
        } else {
            LogUtil.i("Auth", "Kicked!");
        }
        onLogout();
    }

    // 注销
    private void onLogout() {
        // 清理缓存&注销监听&清除状态
        LogoutHelper.logout();

        LoginActivity.start(getActivity(), true);
        getActivity().finish();
    }

    // 将最近联系人列表fragment动态集成进来。 开发者也可以使用在xml中配置的方式静态集成。
    private void addRecentContactsFragment() {
        fragment = new RecentContactsFragment();
        fragment.setContainerId(R.id.messages_fragment);

        final UI activity = (UI) getActivity();

        // 如果是activity从堆栈恢复，FM中已经存在恢复而来的fragment，此时会使用恢复来的，而new出来这个会被丢弃掉
        fragment = (RecentContactsFragment) activity.addFragment(fragment);

        fragment.setCallback(new RecentContactsCallback() {
            @Override
            public void onRecentContactsLoaded() {
                // 最近联系人列表加载完毕
            }

            @Override
            public void onUnreadCountChange(int unreadCount) {
                ReminderManager.getInstance().updateSessionUnreadNum(unreadCount);
            }

            @Override
            public void onItemClick(RecentContact recent) {
                // 回调函数，以供打开会话窗口时传入定制化参数，或者做其他动作
                switch (recent.getSessionType()) {
                    case P2P:
                        HttpClient.userInfoByAccId(recent.getContactId(), new HttpInterface() {
                            @Override
                            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {

                                TargetUserBean targetUserBean = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()), TargetUserBean.class);
                                if (targetUserBean.getRelationDesc().equals("好友")) {
                                    SessionHelper.startP2PSession(getActivity(), recent.getContactId());
                                } else {
                                    EasyAlertDialogHelper.showOneButtonDiolag(getActivity(), "", "【非好友】发送消息失败",
                                            getString(com.netease.nim.uikit.R.string.ok),
                                            true, null);
                                }
                            }

                            @Override
                            public void onFailure(int requestCode, String message) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }, RequestCommandCode.GROUP_DETAIL);
                        break;
                    case Team:
                        SessionHelper.startTeamSession(getActivity(), recent.getContactId());
                       /* BaseRequestBean baseRequestBean = new BaseRequestBean();
                        baseRequestBean.addParams("tId", recent.getContactId());
                        HttpClient.hasExpire(baseRequestBean, new HttpInterface() {
                            @Override
                            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                                String identity = jsonObject.getString("identity");
                                String expireStatus = jsonObject.getString("expireStatus");
                                String forbid = jsonObject.getString("forbid");
                                String groupStatus = jsonObject.getString("groupStatus");

                                if (forbid.equals("正常") && groupStatus.equals("正常")) {
                                    SessionHelper.startTeamSession(getActivity(), recent.getContactId());
                                } else {
                                    if (!forbid.equals("正常")) {
                                        EasyAlertDialogHelper.showOneButtonDiolag(getActivity(), "", forbid,
                                                getString(com.netease.nim.uikit.R.string.ok),
                                                true, null);
                                        return;
                                    }
                                    if (!groupStatus.equals("正常")) {
                                        EasyAlertDialogHelper.showOneButtonDiolag(getActivity(), "", "已被" + groupStatus,
                                                getString(com.netease.nim.uikit.R.string.ok),
                                                true, null);
                                        return;
                                    }

                                }
                            }

                            @Override
                            public void onFailure(int requestCode, String message) {
                                ToastHelper.showToast(getActivity(), message);
                            }

                            @Override
                            public void onComplete() {

                            }
                        }, RequestCommandCode.GROUP_DETAIL);*/
                        break;
                    case SUPER_TEAM:
                        ToastHelper.showToast(getActivity(), "超大群开发者按需实现");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public String getDigestOfAttachment(RecentContact recentContact, MsgAttachment attachment) {
                // 设置自定义消息的摘要消息，展示在最近联系人列表的消息缩略栏上
                // 当然，你也可以自定义一些内建消息的缩略语，例如图片，语音，音视频会话等，自定义的缩略语会被优先使用。
                if (attachment instanceof GuessAttachment) {
                    GuessAttachment guess = (GuessAttachment) attachment;
                    return guess.getValue().getDesc();
                } else if (attachment instanceof RTSAttachment) {
                    return "[白板]";
                } else if (attachment instanceof StickerAttachment) {
                    return "[贴图]";
                } else if (attachment instanceof SnapChatAttachment) {
                    return "[阅后即焚]";
                } else if (attachment instanceof RedPacketAttachment) {
                    return "[红包]";
                } else if (attachment instanceof RedPacketOpenedAttachment) {
                    return ((RedPacketOpenedAttachment) attachment).getDesc(recentContact.getSessionType(), recentContact.getContactId());
                } else if (attachment instanceof MultiRetweetAttachment) {
                    return "[聊天记录]";
                } else if (attachment instanceof TransferAttachment) {
                    return "[转账]";
                }

                return null;
            }

            @Override
            public String getDigestOfTipMsg(RecentContact recent) {
                String msgId = recent.getRecentMessageId();
                List<String> uuids = new ArrayList<>(1);
                uuids.add(msgId);
                List<IMMessage> msgs = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (msgs != null && !msgs.isEmpty()) {
                    IMMessage msg = msgs.get(0);
                    Map<String, Object> content = msg.getRemoteExtension();
                    if (content != null && !content.isEmpty()) {
                        return (String) content.get("content");
                    }
                }

                return null;
            }
        });
    }

    private void groupExpireMessage(GroupDetailBean.GroupUser groupUser, String tId) {
        if (groupUser.getCurrentUserIdentity().equals("群主")) {
            EasyAlertDialogHelper.createOkCancelDiolag(getActivity(), "", "该高级群已到期，请联系群主续费", "去续费", "取消",
                    true, new EasyAlertDialogHelper.OnDialogActionListener() {
                        @Override
                        public void doCancelAction() {
                        }

                        @Override
                        public void doOkAction() {
                            AdvancedTeamUpgradeActivity.start(getActivity(), tId);

                        }
                    }).show();
        } else {
            EasyAlertDialogHelper.showOneButtonDiolag(getActivity(), "", "该高级群已到期，请联系群主续费",
                    getString(com.netease.nim.uikit.R.string.ok),
                    true, null);
        }
    }

    private void groupStatusError(String errorStatusStr) {
        EasyAlertDialogHelper.showOneButtonDiolag(getActivity(), "", "该群已" + errorStatusStr,
                getString(com.netease.nim.uikit.R.string.ok),
                true, null);


    }

    private void getListData() {
        BaseRequestBean baseRequestBean = new BaseRequestBean();
        baseRequestBean.addParams("page", 1);
        baseRequestBean.addParams("limit", MainConstant.PAGE_NUMBER);
        baseRequestBean.addParams("sidx", "create_date");
        baseRequestBean.addParams("order", "desc");
        HttpClient.msgList(baseRequestBean, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                List<NoticeBean> beans = JSON.parseArray(jsonObject.getJSONArray("list").toJSONString(), NoticeBean.class);
                imgHead.loadImgForId(R.drawable.ic_logo);

  /*              if (beans!=null && beans.size()>0){
                    tvNickname.setText(beans.get(0).getTitle());
                    tvMessage.setText(beans.get(0).getBody());
                }else {
                    tvNickname.setText("聊吧助手");
                    tvMessage.setText("暂时消息");
                }*/

            }

            @Override
            public void onFailure(int requestCode, String message) {

            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.GET_NEWS_LIST);

        HttpClient.getDoctorInfo(new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                LocalDataUtils.saveLocalDoctor(baseResponseData.getData());
                MainActivity.saveUserInfoToPreferences();

            /*    if (data.containsKey("msgCount") && data.getInteger("msgCount")>0){
                    tv_new_unread.setVisibility(View.VISIBLE);
                    tv_new_unread.setText(data.getInteger("msgCount")+"");
                }else {
                    tv_new_unread.setVisibility(View.GONE);
                }*/

            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(getContext(), "更新用户信息失败 " + message);
            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.LOGIN_USER_INFO);

    }

    @Override
    public void onResume() {
        super.onResume();
        getListData();
    }
}
