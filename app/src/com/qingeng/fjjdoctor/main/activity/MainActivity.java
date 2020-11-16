package com.qingeng.fjjdoctor.main.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.acker.simplezxing.activity.CaptureActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.main.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.preference.UserPreferences;
import com.netease.nim.uikit.business.team.activity.AdvancedTeamUpgradeActivity;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.ui.popwindow.ActionItem;
import com.netease.nim.uikit.common.ui.popwindow.TitlePopup;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.support.permission.MPermission;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionDenied;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionGranted;
import com.netease.nim.uikit.support.permission.annotation.OnMPermissionNeverAskAgain;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.bean.MyFriendBean;
import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.bean.UserInfoBean;
import com.qingeng.apilibrary.bean.VersionBean;
import com.qingeng.apilibrary.config.AppPreferences;
import com.qingeng.apilibrary.contact.MainConstant;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.apilibrary.util.DownloadUtil;
import com.qingeng.fjjdoctor.BuildConfig;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.common.ui.viewpager.PagerSlidingTabStrip;
import com.qingeng.fjjdoctor.contact.activity.AddFriendMainActivity;
import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;
import com.qingeng.fjjdoctor.login.LoginActivity;
import com.qingeng.fjjdoctor.login.LogoutHelper;
import com.qingeng.fjjdoctor.main.adapter.MainTabPagerAdapter;
import com.qingeng.fjjdoctor.main.helper.CustomNotificationCache;
import com.qingeng.fjjdoctor.main.helper.SystemMessageUnreadManager;
import com.qingeng.fjjdoctor.main.model.MainTab;
import com.qingeng.fjjdoctor.main.reminder.ReminderItem;
import com.qingeng.fjjdoctor.main.reminder.ReminderManager;
import com.qingeng.fjjdoctor.notice.NoticeActivity;
import com.qingeng.fjjdoctor.session.SessionHelper;
import com.qingeng.fjjdoctor.team.TeamCreateHelper;
import com.qingeng.fjjdoctor.util.LocalDataUtils;
import com.qingeng.fjjdoctor.widget.NoteDialog;
import com.qingeng.fjjdoctor.widget.UpgradeDialog;
import com.qingeng.fjjdoctor.zoom.VipRechargeActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.reactivex.functions.Consumer;

public class MainActivity extends UI implements ViewPager.OnPageChangeListener,
        ReminderManager.UnreadNumChangedCallback, DownloadUtil.OnDownloadListener, NoteDialog.OnNoteDialogListener {

    private static final String EXTRA_APP_QUIT = "APP_QUIT";

    private static final int REQUEST_CODE_NORMAL = 1;

    private static final int REQUEST_CODE_ADVANCED = 2;

    private static final int BASIC_PERMISSION_REQUEST_CODE = 100;

    private static final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    private PagerSlidingTabStrip tabs;

    private ViewPager pager;

    private int scrollState;

    private MainTabPagerAdapter adapter;


    private boolean isFirstIn;

    UpgradeDialog upgradeDialog;

    private RxPermissions rxPermissions;


    private Observer<Integer> sysMsgUnreadCountChangedObserver = (Observer<Integer>) unreadCount -> {
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
        ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        EventBus.getDefault().post("sysMsgUnreadCountChangedObserver");
    };


    public static void start(Context context) {
        start(context, null);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    // 注销
    public static void logout(Context context, boolean quit) {
        Intent extra = new Intent();
        extra.putExtra(EXTRA_APP_QUIT, quit);
        start(context, extra);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        EventBus.getDefault().register(this);
        setToolBar(R.id.toolbar, R.string.empty);
        setTitle(R.string.empty);
        isFirstIn = true;
        //不保留后台活动，从厂商推送进聊天页面，会无法退出聊天页面
        if (savedInstanceState == null && parseIntent()) {
            return;
        }
        UserPreferences.setEarPhoneModeEnable(false);
        init();

        if (!AppPreferences.getReadAgreement()) {
            NoteDialog noteDialog = new NoteDialog(this);
            noteDialog.init("http://gyl.ahqgxx.com/kllb/xieyi.html", false);
            noteDialog.setOnNoteDialogListener(this);
            noteDialog.show();
        }

    }

    private void init() {
        observerSyncDataComplete();
        findViews();
        setupPager();
        setupTabs();
        registerMsgUnreadInfoObserver(true);
        registerSystemMessageObservers(true);
        registerCustomMessageObservers(true);
        requestSystemMessageUnreadCount();
        initUnreadCover();
        requestBasicPermission();

        getUserInfo();

    }

    private boolean parseIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_APP_QUIT)) {
            intent.removeExtra(EXTRA_APP_QUIT);
            onLogout();
            return true;
        }
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            IMMessage message = (IMMessage) intent.getSerializableExtra(
                    NimIntent.EXTRA_NOTIFY_CONTENT);
            intent.removeExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
            switch (message.getSessionType()) {
                case P2P:
                    SessionHelper.startP2PSession(this, message.getSessionId());
                    break;
                case Team:
                    SessionHelper.startTeamSession(this, message.getSessionId());
                    break;
            }
            return true;
        }
        return false;
    }

    private void observerSyncDataComplete() {
        boolean syncCompleted = LoginSyncDataStatusObserver.getInstance()
                .observeSyncDataCompletedEvent(
                        (Observer<Void>) v -> DialogMaker
                                .dismissProgressDialog());
        //如果数据没有同步完成，弹个进度Dialog
        if (!syncCompleted) {
            DialogMaker.showProgressDialog(MainActivity.this, getString(R.string.prepare_data))
                    .setCanceledOnTouchOutside(false);
        }
    }

    private void findViews() {
        tabs = findView(R.id.tabs);
        pager = findView(R.id.main_tab_pager);
    }

    private void setupPager() {
        adapter = new MainTabPagerAdapter(getSupportFragmentManager(), this, pager);
        pager.setOffscreenPageLimit(adapter.getCacheCount());
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(this);
    }

    private void setupTabs() {
        tabs.setOnCustomTabListener(new PagerSlidingTabStrip.OnCustomTabListener() {

            @Override
            public int getTabLayoutResId(int position) {
                return R.layout.tab_layout_main;
            }

            @Override
            public boolean screenAdaptation() {
                return true;
            }
        });
        tabs.setViewPager(pager);
        tabs.setOnTabClickListener(adapter);
        tabs.setOnTabDoubleTapListener(adapter);
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
     * 注册/注销系统消息未读数变化
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(
                sysMsgUnreadCountChangedObserver, register);
    }

    // sample
    Observer<CustomNotification> customNotificationObserver = (Observer<CustomNotification>) notification -> {
        // 处理自定义通知消息
        LogUtil.i("demo", "receive custom notification: " + notification.getContent() + " from :" +
                notification.getSessionId() + "/" + notification.getSessionType() +
                "unread=" + notification.getConfig().enableUnreadCount + " " + "push=" +
                notification.getConfig().enablePush + " nick=" +
                notification.getConfig().enablePushNick);
        try {
            JSONObject obj = JSONObject.parseObject(notification.getContent());
            if (obj != null && obj.getIntValue("id") == 2) {
                // 加入缓存中
                CustomNotificationCache.getInstance().addCustomNotification(notification);
                // Toast
                String content = obj.getString("content");
                String tip = String.format("自定义消息[%s]：%s", notification.getFromAccount(), content);
                ToastHelper.showToast(MainActivity.this, tip);
            }
        } catch (JSONException e) {
            LogUtil.e("demo", e.getMessage());
        }
    };

    private void registerCustomMessageObservers(boolean register) {
        NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(
                customNotificationObserver, register);
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

    //初始化未读红点动画
    private void initUnreadCover() {
        DropManager.getInstance().init(this, findView(R.id.unread_cover), (id, explosive) -> {
            if (id == null || !explosive) {
                return;
            }
            if (id instanceof RecentContact) {
                RecentContact r = (RecentContact) id;
                NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(),
                        r.getSessionType());
                return;
            }
            if (id instanceof String) {
                if (((String) id).contentEquals("0")) {
                    NIMClient.getService(MsgService.class).clearAllUnreadCount();
                } else if (((String) id).contentEquals("1")) {
                    NIMClient.getService(SystemMessageService.class)
                            .resetSystemMessageUnreadCount();
                }
            }
        });
    }

    private void requestBasicPermission() {

        rxPermissions = new RxPermissions(this);
        rxPermissions.request(BASIC_PERMISSIONS).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {

                    File files = new File(MainConstant.DOWNLOAD_DIRECTORY);//跟目录一个文件夹
                    if (!files.exists()) {
                        //不存在就创建出来
                        files.mkdirs();
                    }

//                    ToastHelper.showToast(MainActivity.this, "授权成功！");
                } else {
                    //只要有一个权限被拒绝，就会执行
                    ToastHelper.showToast(MainActivity.this, "未全部授权，部分功能可能无法正常运行！");
                }
            }
        });


    }

    private void onLogout() {
        AppPreferences.saveHttpToken("");
        // 清理缓存&注销监听
        LogoutHelper.logout();
        LoginActivity.start(this);
        finish();
    }        // 启动登录


    private void selectPage() {
        if (scrollState == ViewPager.SCROLL_STATE_IDLE) {
            adapter.onPageSelected(pager.getCurrentItem());
        }
    }

    /**
     * 设置最近联系人的消息为已读
     * <p>
     * account, 聊天对象帐号，或者以下两个值：
     * {@link MsgService#MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
     * {@link MsgService#MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
     */
    private void enableMsgNotification(boolean enable) {
        boolean msg = (pager.getCurrentItem() != MainTab.RECENT_CONTACTS.tabIndex);
        if (enable | msg) {
            NIMClient.getService(MsgService.class).setChattingAccount(
                    MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(
                    MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 第一次 ， 三方通知唤起进会话页面之类的，不会走初始化过程
        boolean temp = isFirstIn;
        isFirstIn = false;
        if (pager == null && temp) {
            return;
        }
        //如果不是第一次进 ， eg: 其他页面back
        if (pager == null) {
            init();
        }
        enableMsgNotification(false);
        getLastVersion();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (pager == null) {
            return;
        }
        enableMsgNotification(true);
    }

    @Override
    public void onDestroy() {
        registerMsgUnreadInfoObserver(false);
        registerSystemMessageObservers(false);
        registerCustomMessageObservers(false);
        DropManager.getInstance().destroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_NORMAL) {
            final ArrayList<String> selected = data.getStringArrayListExtra(
                    ContactSelectActivity.RESULT_DATA);
            if (selected != null && !selected.isEmpty()) {
                TeamCreateHelper.createNormalTeam(MainActivity.this, selected, false, null);
            } else {
                ToastHelper.showToast(MainActivity.this, "请选择至少一个联系人！");
            }
        } else if (requestCode == REQUEST_CODE_ADVANCED) {
            final ArrayList<String> selected = data.getStringArrayListExtra(
                    ContactSelectActivity.RESULT_DATA);

            HttpClient.queryMyFriends(null, new HttpInterface() {
                @Override
                public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                    List<MyFriendBean> myFriendBeans = new ArrayList<>();
                    JSONArray objects = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()));
                    for (int i = 0; i < objects.size(); i++) {
                        JSONArray array = JSON.parseArray(JSON.toJSONString(objects.get(i)));
                        List<MyFriendBean> users = JSON.parseArray(JSON.toJSONString(array), MyFriendBean.class);
                        myFriendBeans.addAll(users);
                    }
                    Map<String, String> userMaps = new HashMap<>();
                    for (int i = 0; i < myFriendBeans.size(); i++) {
                        userMaps.put(myFriendBeans.get(i).getFaccid(), myFriendBeans.get(i).getUserId() + "");
                    }
                    ArrayList<String> userIds = new ArrayList<>();
                    for (int i = 0; i < selected.size(); i++) {
                        userIds.add(userMaps.get(selected.get(i)));
                    }
                    TeamCreateHelper.createAdvancedTeam(MainActivity.this, userIds);
                }

                @Override
                public void onFailure(int requestCode, String message) {

                }

                @Override
                public void onComplete() {

                }
            }, RequestCommandCode.QUERY_MY_FRIENDS);
        } else if (requestCode == CaptureActivity.REQ_CODE) {
            switch (resultCode) {
                case RESULT_OK:
//                    tvResult.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                    try {
                        //base64解码
                        String str2 = new String(Base64.decode(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT).getBytes(), Base64.DEFAULT));
                        System.out.println("code===" + str2);


                        if (str2.startsWith("**sx_group")) {
                            String id = str2.replace("**sx_group", "");
                            HttpClient.groupDetail(id, new HttpInterface() {
                                @Override
                                public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                                    JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                                    GroupDetailBean.WaHuHighGroup waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);
                                    boolean joined = false;
                                    for (int i = 0; i < waHuHighGroup.getHighGroups().size(); i++) {
                                        if (waHuHighGroup.getHighGroups().get(i).getAccid().equals(AppPreferences.getAccId())) {
                                            joined = true;
                                            break;
                                        }
                                    }
                                    if (joined) {
                                        scanToInto(waHuHighGroup.getTid());
                                    } else {
                                        inviteNewUser(waHuHighGroup, AppPreferences.getUserId()+"");
                                    }
                                }

                                @Override
                                public void onFailure(int requestCode, String message) {
                                    ToastHelper.showToast(MainActivity.this, "获取用户详情失败");
                                }

                                @Override
                                public void onComplete() {

                                }
                            }, RequestCommandCode.GROUP_DETAIL);
                        } else if (str2.startsWith("**sx")) {
                            String id = str2.replace("**sx", "");
                            HttpClient.userInfoByAccId(id, new HttpInterface() {
                                @Override
                                public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                                    UserProfileActivity2.start2(MainActivity.this, id);
                                }

                                @Override
                                public void onFailure(int requestCode, String message) {
                                    ToastHelper.showToast(MainActivity.this, "获取用户详情失败");
                                }

                                @Override
                                public void onComplete() {

                                }
                            }, 1);

                        } else {
                            ToastHelper.showToast(MainActivity.this, "解析扫描结果失败");

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastHelper.showToast(MainActivity.this, "解析扫描结果失败");
                    }
                    break;
                case RESULT_CANCELED:
                    if (data != null) {
//                        tvResult.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                    }
                    break;
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        tabs.onPageScrolled(position, positionOffset, positionOffsetPixels);
        adapter.onPageScrolled(position);
    }

    @Override
    public void onPageSelected(int position) {
        tabs.onPageSelected(position);
        selectPage();
        enableMsgNotification(false);
        setToolBar(R.id.toolbar, position == 0 ? R.string.empty : position == 1 ? R.string.main_tab_patient : R.string.empty);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        tabs.onPageScrollStateChanged(state);
        scrollState = state;
        selectPage();
    }

    //未读消息数量观察者实现
    @Override
    public void onUnreadNumChanged(ReminderItem item) {
        MainTab tab = MainTab.fromReminderId(item.getId());
        if (tab != null) {
            tabs.updateTab(tab.tabIndex, item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        try {
//            ToastHelper.showToast(this, "授权成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    @OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        try {
            ToastHelper.showToast(this, "未全部授权，部分功能可能无法正常运行！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
    }

    @Override
    protected boolean displayHomeAsUpEnabled() {
        return false;
    }

    private void startCaptureActivityForResult() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    @Subscribe
    public void onSidOut(String message) {
        if (message.equals("401")) {
            Toast.makeText(this, "token失效，请重新登录！", Toast.LENGTH_SHORT).show();
            onLogout();
        }
        if (message.equals("402")) {
            Toast.makeText(this, "设置成功，请重新登录！", Toast.LENGTH_SHORT).show();
            onLogout();
        }
        if (message.equals("403")) {
            Toast.makeText(this, "退出成功！", Toast.LENGTH_SHORT).show();
            onLogout();
        }
    }

    private void getUserInfo() {
       /* HttpClient.getLoginUserInfo(new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                JSONObject data = (JSONObject) JSON.toJSON(baseResponseData.getData());
                UserInfo userInfo = data.toJavaObject(UserInfo.class);
                Preferences.saveUserBean(userInfo.getUserInfo());
                Preferences.saveWalletInfo(userInfo.getWallet());
                AppPreferences.saveUserId(userInfo.getUserInfo().getUserId() + "");
                AppPreferences.saveThemeId(userInfo.getUserInfo().getThemeId());

                setAlias(userInfo.getUserInfo().getUserId() + "");
            }

            @Override
            public void onFailure(int requestCode, String message) {
            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.LOGIN_USER_INFO);
*/
    }


    private void inviteNewUser(GroupDetailBean.WaHuHighGroup waHuHighGroup, String uId) {
        if (!waHuHighGroup.getInvitemodeDesc().equals("所有人")) {
            ToastHelper.showToast(MainActivity.this, "禁止扫码进群");
            return;
        }

        HttpClient.inviteNewUser(waHuHighGroup.getTid(), uId, "scan", new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                if (waHuHighGroup.getBeinvitemode() == 0) {
                    ToastHelper.showToast(MainActivity.this, "群组开启邀请确认，等确认后进入");
                } else {
                    ToastHelper.showToast(MainActivity.this, "加入成功，进入群聊");
                    scanToInto(waHuHighGroup.getTid());
                }
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(MainActivity.this, "邀请失败:" + message);
            }

            @Override
            public void onComplete() {
                DialogMaker.dismissProgressDialog();
            }
        }, RequestCommandCode.REMOVE_ANNO);
    }


    private void scanToInto(String tId) {
        HttpClient.groupDetail(tId, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(baseResponseData.getData()));
                GroupDetailBean.GroupUser groupUser = JSONObject.parseObject(jsonObject.get("waUserHighGroup").toString(), GroupDetailBean.GroupUser.class);
                GroupDetailBean.WaHuHighGroup waHuHighGroup = JSONObject.parseObject(jsonObject.get("waHuHighGroup").toString(), GroupDetailBean.WaHuHighGroup.class);

                if (groupUser.getCurrentUserIdentity().equals("未入群")) {
                    EasyAlertDialogHelper.showOneButtonDiolag(MainActivity.this, "", "已退出群聊",
                            getString(com.netease.nim.uikit.R.string.ok),
                            true, null);
                    return;
                }

                if (groupUser.getGroupStatus().equals("正常")) {
                    SessionHelper.startTeamSession(MainActivity.this, tId);
                } else {
                    if (groupUser.getGroupStatus().equals("到期")) {
                        groupExpireMessage(groupUser, tId);
                    } else {
                        groupStatusError(groupUser.getGroupStatus());
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(MainActivity.this, message);
            }

            @Override
            public void onComplete() {

            }
        }, RequestCommandCode.GROUP_DETAIL);
    }

    private void groupExpireMessage(GroupDetailBean.GroupUser groupUser, String tId) {
        if (groupUser.getCurrentUserIdentity().equals("群主") || groupUser.getCurrentUserIdentity().equals("群管理")) {
            EasyAlertDialogHelper.createOkCancelDiolag(this, "", "该高级群已到期，请联系群主续费", "去续费", "取消",
                    true, new EasyAlertDialogHelper.OnDialogActionListener() {
                        @Override
                        public void doCancelAction() {
                        }

                        @Override
                        public void doOkAction() {
                            AdvancedTeamUpgradeActivity.start(MainActivity.this, tId);
                        }
                    }).show();
        } else {
            EasyAlertDialogHelper.showOneButtonDiolag(MainActivity.this, "", "该高级群已到期，请联系群主续费",
                    getString(com.netease.nim.uikit.R.string.ok),
                    true, null);
        }
    }

    private void groupStatusError(String errorStatusStr) {
        EasyAlertDialogHelper.showOneButtonDiolag(MainActivity.this, "", "该群已" + errorStatusStr,
                getString(com.netease.nim.uikit.R.string.ok),
                true, null);
    }

    @Override
    public void onDownloadSuccess(String filePath) {
        installApk(this, filePath);
    }

    @Override
    public void onDownloading(long sum, long total) {

    }

    @Override
    public void onDownloadFailed() {
        ToastHelper.showToast(this, "下载失败，请稍后重试");
        //upgradeDialog.dismiss();
    }


    public void installApk(Context context, String apkPath) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }

        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            File file = new File(apkPath);
            Uri apkUri = FileProvider.getUriForFile(context, "com.qingeng.fjjdoctor.fileprovider", file);//在AndroidManifest中的android:authorities值
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            context.startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(new File(apkPath)), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(install);
        }
        //upgradeDialog.dismiss();
    }


    private void getLastVersion() {
        HttpClient.getLastVersion(new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                VersionBean versionBean = JSONObject.parseObject(JSON.toJSONString(baseResponseData.getData()), VersionBean.class);
                if (versionBean.getVersionCode() > BuildConfig.VERSION_CODE) {
                    if (upgradeDialog == null) {
                        upgradeDialog = new UpgradeDialog(MainActivity.this, R.style.tipDialog2);
                        upgradeDialog.setOnDownloadListener(MainActivity.this);
                    }
                    String url = versionBean.getDownloadUrl().startsWith("http://") || versionBean.getDownloadUrl().startsWith("https://") ? versionBean.getDownloadUrl() : "http://" + versionBean.getDownloadUrl();
                    upgradeDialog.init(url, versionBean.getVersionName(), false, versionBean.getUpdateContent());
                    if (upgradeDialog.isShowing()) return;
                    upgradeDialog.show();
                }
            }

            @Override
            public void onFailure(int requestCode, String message) {

            }

            @Override
            public void onComplete() {

            }
        }, 1);
    }

    @Override
    public void onOk() {
        AppPreferences.saveReadAgreement(true);
    }

    @Override
    public void onCancel() {
        this.finish();
    }


    public static final String TAG = "jiguang";

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    private void setAlias(String alias) {
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
    private static final int MSG_SET_ALIAS = 1001;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };


    public static void saveUserInfoToPreferences(){
        UserInfoBean userBean = LocalDataUtils.getUserInfo();
        AppPreferences.saveImToken(userBean.getToken());
        AppPreferences.saveUserId(userBean.getUserId());
    }
}
