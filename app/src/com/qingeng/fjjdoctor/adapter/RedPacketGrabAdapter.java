package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.apilibrary.bean.RedPacketReceiverBean;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.MoneyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class RedPacketGrabAdapter extends RecyclerView.Adapter<RedPacketGrabAdapter.ViewHolder> {

    private Context context;


    public RedPacketGrabAdapter(Context context) {
        this.context = context;
    }

    private List<RedPacketReceiverBean> redPacketReceiverBeans = new ArrayList<>();

    public void setRedPacketReceiverBeans(List<RedPacketReceiverBean> redPacketReceiverBeans) {
        this.redPacketReceiverBeans = redPacketReceiverBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_red_packet_grab,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        RedPacketReceiverBean packetGrabBean = redPacketReceiverBeans.get(i);

        viewHolder.headImageView.loadImgForUrl(packetGrabBean.getHeadImage());
        viewHolder.operatorResultText.setText(MoneyUtils.fenInt2YuanString((int)packetGrabBean.getAmount())+"元");
        viewHolder.timeText.setText(packetGrabBean.getCompleteDateTime());
        viewHolder.fromAccountText.setText(packetGrabBean.getUsername());
    }

    @Override
    public int getItemCount() {
        return redPacketReceiverBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private HeadImageView headImageView;
        private TextView fromAccountText;
        private TextView timeText;
        private TextView operatorResultText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headImageView = (HeadImageView) itemView.findViewById(R.id.from_account_head_image);
            fromAccountText = (TextView) itemView.findViewById(R.id.from_account_text);
            timeText = (TextView) itemView.findViewById(R.id.notification_time);
            operatorResultText = (TextView) itemView.findViewById(R.id.operator_result);
        }
    }



}
