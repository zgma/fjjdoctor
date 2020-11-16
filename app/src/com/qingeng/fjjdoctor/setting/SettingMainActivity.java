package com.qingeng.fjjdoctor.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

//import com.netease.nim.avchatkit.AVChatKit; 音频注释
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.main.activity.MainActivity;
import com.qingeng.fjjdoctor.main.activity.NoDisturbActivity;
import com.qingeng.fjjdoctor.main.adapter.SettingsAdapter;
import com.qingeng.fjjdoctor.main.model.SettingTemplate;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.lucene.LuceneService;
import com.netease.nimlib.sdk.misc.DirCacheFileType;
import com.netease.nimlib.sdk.misc.MiscService;
import com.netease.nimlib.sdk.settings.SettingsServiceObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hzxuwen on 2015/6/26.
 */
public class SettingMainActivity extends UI implements SettingsAdapter.SwitchChangeListener {

    private static final int TAG_CHANE_PASSWORD = 1;

    private static final int TAG_FONT_SIZE = 2;

    private static final int TAG_APP_THEME = 3;

    private static final int TAG_CLEAR_CACHE = 4;

    private static final int TAG_ABOUT_US = 5;

    private static final int TAG_LOGOUT = 6;


    ListView listView;
    View space;


    SettingsAdapter adapter;

    private List<SettingTemplate> items = new ArrayList<>();

    private SettingTemplate clearSDKDirCacheItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.settings;
        setToolBar(R.id.toolbar, options);
        initData();
        initUI();
        registerObservers(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
    }

    private void registerObservers(boolean register) {
        NIMClient.getService(SettingsServiceObserver.class).observeMultiportPushConfigNotify(
                pushConfigObserver, register);
    }

    Observer<Boolean> pushConfigObserver = (Observer<Boolean>) aBoolean -> ToastHelper.showToast(
            SettingMainActivity.this, "收到multiport push config：" + aBoolean);

    private void initData() {
        getSDKDirCacheSize();
    }

    private void initUI() {
        initItems();
        space = findViewById(R.id.space);
        space.setVisibility(View.VISIBLE);
        listView = findViewById(R.id.settings_listview);
        View footer = LayoutInflater.from(this).inflate(R.layout.settings_logout_footer, null);
        listView.addFooterView(footer);
        initAdapter();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            SettingTemplate item = items.get(position);
            onListItemClick(item);
        });
        View logoutBtn = footer.findViewById(R.id.settings_button_logout);
        logoutBtn.setOnClickListener(v -> logout());

    }

    private void initAdapter() {
        adapter = new SettingsAdapter(this, this, items);
        listView.setAdapter(adapter);
    }

    private void initItems() {
        items.clear();

        items.add(new SettingTemplate(TAG_CHANE_PASSWORD, getString(R.string.change_password),true));
        items.add(new SettingTemplate(TAG_FONT_SIZE, getString(R.string.font_size),true));
        items.add(new SettingTemplate(TAG_APP_THEME, getString(R.string.app_theme),true));

        clearSDKDirCacheItem = new SettingTemplate(TAG_CLEAR_CACHE, getString(R.string.clear_sdk_cache), 0 + " M",true);
        items.add(clearSDKDirCacheItem);

        items.add(new SettingTemplate(TAG_ABOUT_US, getString(R.string.about_us),true));
        items.add(SettingTemplate.makeSeperator());//添加分割空白
        items.add(SettingTemplate.addLine());//添加分割线

    }

    private void onListItemClick(SettingTemplate item) {
        if (item == null) {
            return;
        }
        switch (item.getId()) {
            case TAG_CHANE_PASSWORD:
                ChangePasswordActivity.start(this);
                break;
            case TAG_FONT_SIZE:
                TextSizeShowActivity.start(this);
                break;
            case TAG_APP_THEME:
                ThemeListActivity.start(this);
                break;
            case TAG_CLEAR_CACHE:
                clearSDKDirCache();
                break;
            case TAG_ABOUT_US:
                AboutUsActivity.start(this);
                break;
            default:
                break;
        }
    }

    private void getSDKDirCacheSize() {
        List<DirCacheFileType> types = new ArrayList<>();
        types.add(DirCacheFileType.AUDIO);
        types.add(DirCacheFileType.THUMB);
        types.add(DirCacheFileType.IMAGE);
        types.add(DirCacheFileType.VIDEO);
        types.add(DirCacheFileType.OTHER);
        long size = NIMClient.getService(LuceneService.class).getCacheSize();
        NIMClient.getService(MiscService.class).getSizeOfDirCache(types, 0, 0).setCallback(
                new RequestCallbackWrapper<Long>() {

                    @Override
                    public void onResult(int code, Long result, Throwable exception) {
                       Long total = size + result;
                        clearSDKDirCacheItem.setDetail(
                                String.format("%.2f M", total / (1024.0f * 1024.0f)));
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void clearSDKDirCache() {
        NIMClient.getService(LuceneService.class).clearCache();
        List<DirCacheFileType> types = new ArrayList<>();
        types.add(DirCacheFileType.AUDIO);
        types.add(DirCacheFileType.THUMB);
        types.add(DirCacheFileType.IMAGE);
        types.add(DirCacheFileType.VIDEO);
        types.add(DirCacheFileType.OTHER);
        NIMClient.getService(MiscService.class).clearDirCache(types, 0, 0).setCallback(
                new RequestCallbackWrapper<Void>() {

                    @Override
                    public void onResult(int code, Void result, Throwable exception) {
                        clearSDKDirCacheItem.setDetail("0.00 M");
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    /**
     * 注销
     */
    private void logout() {
        MainActivity.logout(SettingMainActivity.this, false);
        finish();
        NIMClient.getService(AuthService.class).logout();
    }

    @Override
    public void onSwitchChange(SettingTemplate item, boolean checkState) {

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case NoDisturbActivity.NO_DISTURB_REQ:
                    break;
                default:
                    break;
            }
        }
    }


}
