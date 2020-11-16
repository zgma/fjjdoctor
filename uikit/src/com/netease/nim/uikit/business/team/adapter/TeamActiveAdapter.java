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

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.UiUtils;
import com.qingeng.apilibrary.bean.GroupDetailBean;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class TeamActiveAdapter extends RecyclerView.Adapter<TeamActiveAdapter.ViewHolder> {

    private Context context;


    private int listType;

    public void setListType(int listType) {
        this.listType = listType;
    }

    public TeamActiveAdapter(Context context) {
        this.context = context;
    }

    private List<GroupDetailBean.GroupUser> groupUsers;

    public void setGroupUsers(List<GroupDetailBean.GroupUser> groupUsers) {
        this.groupUsers = groupUsers;
    }


    private OnClickLinsenter onClickLinsenter;

    public void setOnClickLinsenter(OnClickLinsenter onClickLinsenter) {
        this.onClickLinsenter = onClickLinsenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_active,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GroupDetailBean.GroupUser user = groupUsers.get(i);
        viewHolder.headPic.loadImgForUrl(user.getHeadImage());
        viewHolder.tv_nickname.setText(user.getUsername());
        if (listType == 1){
            viewHolder.tv_memo.setVisibility(View.GONE);
        }else {
            if (listType == 0){
                viewHolder.tv_memo.setText(user.getLevel());
            }else if (listType == 2){
                viewHolder.tv_memo.setText(user.getBlack()==0?"正常":"已禁止");
                if (user.getBlack()==0){
                    viewHolder.tv_memo.setTextColor(UiUtils.getColor(context, R.color.gray7));
                }else {
                    viewHolder.tv_memo.setTextColor(UiUtils.getColor(context, R.color.topbar_backgroup_red));
                }
            }
            viewHolder.tv_memo.setVisibility(View.VISIBLE);
        }
        viewHolder.listItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (onClickLinsenter!=null){
                   onClickLinsenter.onItemClick(user);
               }
            }
        });
        viewHolder.headPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
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
        TextView tv_memo;
        RelativeLayout listItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headPic = itemView.findViewById(R.id.img_head);
            img_delete = itemView.findViewById(R.id.img_delete);
            img_mute = itemView.findViewById(R.id.img_mute);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_memo = itemView.findViewById(R.id.tv_memo);
            listItemLayout = itemView.findViewById(R.id.listItemLayout);
        }
    }

    public interface OnClickLinsenter {
        void onItemClick(GroupDetailBean.GroupUser user);
    }
}
