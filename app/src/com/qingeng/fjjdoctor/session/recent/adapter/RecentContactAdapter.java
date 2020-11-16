package com.qingeng.fjjdoctor.session.recent.adapter;

import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.R;
import com.qingeng.fjjdoctor.session.recent.holder.CommonRecentViewHolder;
import com.qingeng.fjjdoctor.session.recent.holder.SuperTeamRecentViewHolder;
import com.qingeng.fjjdoctor.session.recent.holder.TeamRecentViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.qingeng.fjjdoctor.session.recent.RecentContactsCallback;


import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class RecentContactAdapter extends BaseMultiItemQuickAdapter<RecentContact, BaseViewHolder> {

    interface ViewType {
        int VIEW_TYPE_COMMON = 1;
        int VIEW_TYPE_TEAM = 2;
        int VIEW_TYPE_SUPER_TEAM = 3;
    }

    private RecentContactsCallback callback;

    public RecentContactAdapter(RecyclerView recyclerView, List<RecentContact> data) {
        super(recyclerView, data);
        addItemType(ViewType.VIEW_TYPE_COMMON, R.layout.nim_recent_contact_list_item, CommonRecentViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_TEAM, R.layout.nim_recent_contact_list_item, TeamRecentViewHolder.class);
        addItemType(ViewType.VIEW_TYPE_SUPER_TEAM, R.layout.nim_recent_contact_list_item, SuperTeamRecentViewHolder.class);
    }

    @Override
    protected int getViewType(RecentContact item) {
        return item.getSessionType() == SessionTypeEnum.Team ? ViewType.VIEW_TYPE_TEAM : ViewType.VIEW_TYPE_COMMON;
    }

    @Override
    protected String getItemKey(RecentContact item) {
        StringBuilder sb = new StringBuilder();
        sb.append(item.getSessionType().getValue()).append("_").append(item.getContactId());

        return sb.toString();
    }

    public RecentContactsCallback getCallback() {
        return callback;
    }

    public void setCallback(RecentContactsCallback callback) {
        this.callback = callback;
    }
}
