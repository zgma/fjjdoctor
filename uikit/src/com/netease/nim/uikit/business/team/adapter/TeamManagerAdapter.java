package com.netease.nim.uikit.business.team.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class TeamManagerAdapter extends RecyclerView.Adapter<TeamManagerAdapter.ViewHolder> {

    private Context context;

    private boolean showDeleteIcon = false;
    private boolean showMuteIcon = false;

    public TeamManagerAdapter(Context context) {
        this.context = context;
    }

    private List<GroupDetailBean.GroupUser> groupUsers;

    public void setGroupUsers(List<GroupDetailBean.GroupUser> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public void setShowDeleteIcon(boolean showDeleteIcon) {
        this.showDeleteIcon = showDeleteIcon;
    }

    public void setShowMuteIcon(boolean showMuteIcon) {
        this.showMuteIcon = showMuteIcon;
    }

    private OnClickLinsenter onClickLinsenter;

    public void setOnClickLinsenter(OnClickLinsenter onClickLinsenter) {
        this.onClickLinsenter = onClickLinsenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_manager,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GroupDetailBean.GroupUser user = groupUsers.get(i);
        viewHolder.headPic.loadImgForUrl(user.getHeadImage());
        viewHolder.tv_nickname.setText(user.getUsername());
        viewHolder.listItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (onClickLinsenter!=null){
                   if (showMuteIcon){
                       onClickLinsenter.onMuteClick(user);
                   }else {
                       onClickLinsenter.onItemClick(user);
                   }

               }
            }
        });
        viewHolder.img_delete.setVisibility(showDeleteIcon?View.VISIBLE:View.GONE);
        viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickLinsenter!=null){
                    onClickLinsenter.onDeleteClick(i);
                }
            }
        });
        viewHolder.img_mute.setVisibility(showMuteIcon&&user.getMuteStatus().equals("禁言")?View.VISIBLE:View.GONE);
    }

    @Override
    public int getItemCount() {
        return groupUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        HeadImageView headPic;
        ImageView img_delete;
        ImageView img_mute;
        TextView tv_nickname;
        RelativeLayout listItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headPic = itemView.findViewById(R.id.img_head);
            img_delete = itemView.findViewById(R.id.img_delete);
            img_mute = itemView.findViewById(R.id.img_mute);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            listItemLayout = itemView.findViewById(R.id.listItemLayout);
        }
    }

    public interface OnClickLinsenter {
        void onItemClick(GroupDetailBean.GroupUser user);
        void onDeleteClick(int position);
        void onMuteClick(GroupDetailBean.GroupUser user);
    }
}
