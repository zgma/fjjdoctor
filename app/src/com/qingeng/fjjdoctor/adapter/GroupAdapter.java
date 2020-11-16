package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.GroupBean;
import com.qingeng.fjjdoctor.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private Context context;

    private GroupAdapter.Listener listener;

    public void setListener(GroupAdapter.Listener listener) {
        this.listener = listener;
    }

    public GroupAdapter(Context context) {
        this.context = context;
    }

    private List<GroupBean> groupBeans;

    public void setGroupBeans(List<GroupBean> groupBeans) {
        this.groupBeans = groupBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_layout,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GroupBean groupBean = groupBeans.get(i);

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(groupBean);
                }
            }
        });

        viewHolder.group_head.loadTeamHead(groupBean.getIcon());
        viewHolder.group_name.setText(groupBean.getTname());
        viewHolder.top_line.setVisibility(i==0?View.GONE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return groupBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private HeadImageView group_head;
        private TextView group_name;
        private LinearLayout root;
        private View top_line;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group_head = (HeadImageView) itemView.findViewById(R.id.group_head);
            group_name = (TextView) itemView.findViewById(R.id.group_name);
            root = (LinearLayout) itemView.findViewById(R.id.root);
            top_line =  itemView.findViewById(R.id.top_line);
        }
    }

    public interface Listener {
        void onItemClick(GroupBean groupBean);
    }
}
