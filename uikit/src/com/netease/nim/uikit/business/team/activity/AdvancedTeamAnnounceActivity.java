package com.netease.nim.uikit.business.team.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qingeng.apilibrary.bean.AnnouncementBean;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.netease.nim.uikit.common.ToastHelper;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.wrapper.NimToolBarOptions;
import com.netease.nim.uikit.business.team.model.Announcement;
import com.netease.nim.uikit.business.team.viewholder.TeamAnnounceHolder;
import com.netease.nim.uikit.common.activity.ToolBarOptions;
import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.adapter.AnnouncementAdapter;
import com.netease.nim.uikit.common.adapter.TAdapterDelegate;
import com.netease.nim.uikit.common.adapter.TViewHolder;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialog;
import com.netease.nim.uikit.common.ui.dialog.EasyAlertDialogHelper;
import com.netease.nim.uikit.common.ui.listview.ListViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 群公告列表
 * Created by hzxuwen on 2015/3/18.
 */
public class AdvancedTeamAnnounceActivity extends UI implements TAdapterDelegate,AnnouncementAdapter.onItemClick {
    // constant
    private final static String EXTRA_CAN_ADD_NEW = "EXTRA_CAN_ADD_NEW";
    private final static String EXTRA_TID = "EXTRA_TID";
    private final static String EXTRA_AID = "EXTRA_AID";
    private final static int RES_ANNOUNCE_CREATE_CODE = 0x10;
    public final static String RESULT_ANNOUNCE_DATA = "RESULT_ANNOUNCE_DATA";

    // context
    private Handler uiHandler;

    // data
    private boolean canAddNew;
    private String teamId;
    private String announceId;
    private String announce;

    // view
    private TextView announceTips;
    private ListView announceListView;
    private AnnouncementAdapter mAdapter;

    private List<AnnouncementBean> announcementBeans;

    private boolean isMember = false;

    public static void start(Activity activity, String teamId,boolean canAddNew) {
        start(activity, teamId, canAddNew, null);
    }

    public static void start(Activity activity, String teamId,boolean canAddNew, String announceId) {
        Intent intent = new Intent();
        intent.setClass(activity, AdvancedTeamAnnounceActivity.class);
        intent.putExtra(EXTRA_TID, teamId);
        intent.putExtra(EXTRA_CAN_ADD_NEW, canAddNew);
        if (announceId != null) {
            intent.putExtra(EXTRA_AID, announceId);
        }
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nim_advanced_team_announce);

        ToolBarOptions options = new NimToolBarOptions();
        options.titleId = R.string.team_annourcement;
        setToolBar(R.id.toolbar, options);

        uiHandler = new Handler(getMainLooper());

        parseIntentData();
        findViews();
        initActionbar();
        initAdapter();
        requestTeamData();
    }

    /**
     * ************************ TAdapterDelegate **************************
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return TeamAnnounceHolder.class;
    }

    @Override
    public boolean enabled(int position) {
        return false;
    }

    /**
     * ******************************初始化*******************************
     */

    private void parseIntentData() {
        canAddNew = getIntent().getBooleanExtra(EXTRA_CAN_ADD_NEW,false);
        teamId = getIntent().getStringExtra(EXTRA_TID);
        announceId = getIntent().getStringExtra(EXTRA_AID);
    }

    private void findViews() {
        announceListView = (ListView) findViewById(R.id.team_announce_listview);
        announceTips = (TextView) findViewById(R.id.team_announce_tips);
    }

    private void initActionbar() {
        TextView toolbarView = findView(R.id.action_bar_right_clickable_textview);
        toolbarView.setText(R.string.create);
        toolbarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvancedTeamCreateAnnounceActivity.startActivityForResult(AdvancedTeamAnnounceActivity.this, teamId, RES_ANNOUNCE_CREATE_CODE);
            }
        });
        toolbarView.setVisibility(canAddNew?View.VISIBLE:View.GONE);
    }

    private void initAdapter() {
        announcementBeans = new ArrayList<>();
        mAdapter = new AnnouncementAdapter(this);
        mAdapter.setOnItemClick(this);
        mAdapter.setCanDelete(canAddNew);
        announceListView.setAdapter(mAdapter);
        announceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        announceListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void requestTeamData() {
        DialogMaker.showProgressDialog(this, "加载中...");
        HttpClient.groupAnnouncementList(teamId + "", new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                announcementBeans = JSONArray.parseArray(JSON.toJSONString(baseResponseData.getData()),AnnouncementBean.class);
                mAdapter.setmData(announcementBeans);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(AdvancedTeamAnnounceActivity.this, message);
            }

            @Override
            public void onComplete() {
                DialogMaker.dismissProgressDialog();
            }
        }, RequestCommandCode.GROUP_ANNOUNCEMENT_LIST);
    }




    /**
     * 跳转到选中的公告
     *
     * @param list 群公告列表
     */
    private void jumpToIndex(List<Announcement> list) {
        if (TextUtils.isEmpty(announceId)) {
            return;
        }

        int jumpIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(announceId)) {
                jumpIndex = i;
                break;
            }
        }

        if (jumpIndex >= 0) {
            final int position = jumpIndex;
            uiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ListViewUtil.scrollToPosition(announceListView, position, 0);
                }
            }, 200);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case RES_ANNOUNCE_CREATE_CODE:
                    requestTeamData();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(RESULT_ANNOUNCE_DATA, announce);
        setResult(Activity.RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onItemClick(AnnouncementBean bean) {
        EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(AdvancedTeamAnnounceActivity.this, "删除公告",
                "确定删除此公告？", true,
                new EasyAlertDialogHelper.OnDialogActionListener() {

                    @Override
                    public void doCancelAction() {

                    }
                    @Override
                    public void doOkAction() {
                        delete(bean);
                    }
                });
        dialog.show();
    }


    private void delete(AnnouncementBean bean){
        DialogMaker.showProgressDialog(this, "处理中...");
        HttpClient.removeAnno(bean.getGroupId() + "",bean.getId(), new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                requestTeamData();
            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(AdvancedTeamAnnounceActivity.this, message);
            }

            @Override
            public void onComplete() {
                DialogMaker.dismissProgressDialog();
            }
        }, RequestCommandCode.REMOVE_ANNO);
    }

}
