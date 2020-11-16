package com.qingeng.fjjdoctor.team.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.apilibrary.bean.RedPacketBean;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.UiUtils;

import java.util.ArrayList;
import java.util.List;


public class TeamLeftRedPacketAdapter extends RecyclerView.Adapter<TeamLeftRedPacketAdapter.ViewHolder> {

    private Context context;
    private OnClickLinsenter onClickLinsenter;
    private List<RedPacketBean> redPacketBeans = new ArrayList<>();


    public void setRedPacketBeans(List<RedPacketBean> redPacketBeans) {
        this.redPacketBeans = redPacketBeans;
    }

    public void setOnClickLinsenter(OnClickLinsenter onClickLinsenter) {
        this.onClickLinsenter = onClickLinsenter;
    }

    public TeamLeftRedPacketAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.red_packet_item_2, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RedPacketBean redPacketBean = redPacketBeans.get(position);
        holder.img_head.loadImgForUrl(redPacketBean.getHeadImage());
        holder.tv_user_name.setText(redPacketBean.getUsername());
        holder.revView.setVisibility(View.VISIBLE);
        holder.revContentText.setText(redPacketBean.getMsg());
        holder.revView.setBackground(UiUtils.getDrawable(context, R.drawable.red_packet_rev_bg));
        holder.revTargetText.setText("领取红包");
        holder.revIcon.setImageResource(R.drawable.red_packet_icon_normal);
        holder.revTitleText.setText("红包");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickLinsenter!=null)onClickLinsenter.onItemClick(redPacketBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return redPacketBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout  revView;
        TextView  revContentText;
        TextView revTitleText;
        TextView  revTargetText;
        ImageView  revIcon;
        TextView tv_user_name;
        HeadImageView img_head;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            img_head = itemView.findViewById(R.id.img_head);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            revContentText = itemView.findViewById(R.id.tv_bri_mess_rev);
            revTitleText = itemView.findViewById(R.id.tv_bri_name_rev);
            revTargetText = itemView.findViewById(R.id.tv_bri_target_rev);
            revView = itemView.findViewById(R.id.bri_rev);
            revIcon = itemView.findViewById(R.id.iv_icon_rev);
        }
    }

    public interface OnClickLinsenter {
        void onItemClick(RedPacketBean redPacketBean);
    }
}
