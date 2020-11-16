package com.netease.nim.uikit.business.team.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.UiUtils;
import com.qingeng.apilibrary.bean.GroupDetailBean;
import com.qingeng.apilibrary.bean.GroupInviteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class TeamInviteConfirmAdapter extends RecyclerView.Adapter<TeamInviteConfirmAdapter.ViewHolder> {

    private Context context;

    public TeamInviteConfirmAdapter(Context context) {
        this.context = context;
    }

    private List<GroupInviteBean> inviteBeans = new ArrayList<>();

    public void setInviteBeans(List<GroupInviteBean> inviteBeans) {
        this.inviteBeans = inviteBeans;
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_invite_confirm, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GroupInviteBean groupInviteBean = inviteBeans.get(i);
        viewHolder.headPic.loadImgForUrl(groupInviteBean.getHeadImage());
        viewHolder.from_account_text.setText(groupInviteBean.getUsername());
        viewHolder.content_text.setText(groupInviteBean.getMsg());
        if (groupInviteBean.getStatus() == -1) {
            viewHolder.agree.setVisibility(View.VISIBLE);
            viewHolder.reject.setVisibility(View.VISIBLE);
            viewHolder.operator_result.setVisibility(View.GONE);
        } else {
            viewHolder.agree.setVisibility(View.GONE);
            viewHolder.reject.setVisibility(View.GONE);
            viewHolder.operator_result.setVisibility(View.VISIBLE);
            viewHolder.operator_result.setText(groupInviteBean.getStatus() == 0 ? "已拒绝" : "已同意");
        }
        viewHolder.agree.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onOperator(groupInviteBean, 1);
            }
        });
        viewHolder.reject.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onOperator(groupInviteBean, 0);
            }
        });


    }

    @Override
    public int getItemCount() {
        return inviteBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        HeadImageView headPic;
        TextView from_account_text;
        TextView content_text;
        LinearLayout operator_layout;
        Button agree;
        Button reject;
        TextView operator_result;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headPic = itemView.findViewById(R.id.from_account_head_image);
            from_account_text = itemView.findViewById(R.id.from_account_text);
            content_text = itemView.findViewById(R.id.content_text);
            operator_layout = itemView.findViewById(R.id.operator_layout);
            agree = itemView.findViewById(R.id.agree);
            reject = itemView.findViewById(R.id.reject);
            operator_result = itemView.findViewById(R.id.operator_result);
        }
    }

    public interface OnClickListener {
        void onOperator(GroupInviteBean groupInviteBean, int status);
    }
}
