package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.OrderBean;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.UiUtils;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private Context context;

    private MyOrderAdapter.Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    public MyOrderAdapter(Context context) {
        this.context = context;
    }

    private List<OrderBean> orderBeans;

    public void setOrderBeans(List<OrderBean> orderBeans) {
        this.orderBeans = orderBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_order, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        OrderBean orderBean = orderBeans.get(i);
        viewHolder.tv_type.setText(orderBean.getOperation());
        viewHolder.tv_time.setText(orderBean.getCreateDate());
        if (TextUtils.isEmpty(orderBean.getFkName())) {
            viewHolder.tv_fkName.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.tv_fkName.setVisibility(View.VISIBLE);
            viewHolder.tv_fkName.setText(orderBean.getFkName());
        }
        if (orderBean.getMoney() < 0) {
            viewHolder.tv_money.setText(orderBean.getMoney() + "");
            viewHolder.tv_money.setTextColor(UiUtils.getColor(context, R.color.red));
        } else {
            viewHolder.tv_money.setText("+" + orderBean.getMoney() + "");
            viewHolder.tv_money.setTextColor(UiUtils.getColor(context, R.color.grey));
        }


        viewHolder.root.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(orderBean);
        });
    }

    @Override
    public int getItemCount() {
        return orderBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_type;
        private TextView tv_time;
        private TextView tv_money;
        private TextView tv_fkName;
        private LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);
            tv_fkName = (TextView) itemView.findViewById(R.id.tv_fkName);
            root = (LinearLayout) itemView.findViewById(R.id.root);
        }
    }

    public interface Listener {
        void onItemClick(OrderBean orderBean);
    }

}
