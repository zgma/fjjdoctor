package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.apilibrary.bean.AppointmentRecordBean;
import com.qingeng.apilibrary.bean.OrderBean;
import com.qingeng.fjjdoctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class ConsultRecordAdapter extends RecyclerView.Adapter<ConsultRecordAdapter.ViewHolder> {

    private Context context;

    public ConsultRecordAdapter(Context context) {
        this.context = context;
    }

    private List<OrderBean> dataList = new ArrayList<>();

    public void setDataList(List<OrderBean> dataList) {
        this.dataList = dataList;
    }


    OnListItemClickListener onListItemClickListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_consult_record, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        OrderBean data = dataList.get(i);
        viewHolder.tv_order_no.setText(data.getOrdersNo() + "");
        viewHolder.tv_time.setText(data.getCreateTime());
        viewHolder.tv_name.setText(data.getIllnessAge() + "岁");
        viewHolder.tv_content.setText(data.getIllnessDesc());
        viewHolder.user_phone.loadAvatar(data.getAvatar());

        viewHolder.root.setOnClickListener(v -> {
            if (onListItemClickListener != null)
                onListItemClickListener.onItemClick(i, data);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_order_no;
        private TextView tv_time;
        private HeadImageView user_phone;
        private TextView tv_name;
        private TextView tv_content;
        private RelativeLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_order_no = itemView.findViewById(R.id.tv_order_no);
            tv_time = itemView.findViewById(R.id.tv_time);
            user_phone = itemView.findViewById(R.id.user_phone);
        }
    }

}
