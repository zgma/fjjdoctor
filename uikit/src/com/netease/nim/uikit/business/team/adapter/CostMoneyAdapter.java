package com.netease.nim.uikit.business.team.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.PayWayBean;
import com.netease.nim.uikit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class CostMoneyAdapter extends RecyclerView.Adapter<CostMoneyAdapter.ViewHolder> {

    private Context context;

    public CostMoneyAdapter(Context context) {
        this.context = context;
    }

    private List<PayWayBean> payWayBeans = new ArrayList<>();

    public void setPayWayBeans(List<PayWayBean> payWayBeans) {
        this.payWayBeans = payWayBeans;
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private int pressIndex = 0;

    public void setPressIndex(int pressIndex) {
        this.pressIndex = pressIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_cost,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PayWayBean payWayBean = payWayBeans.get(i);
        viewHolder.tv_title.setText(payWayBean.getTimeNum()+payWayBean.getUnit());
        viewHolder.tv_content.setText("¥"+payWayBean.getMoney());
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (onClickListener!=null){
                   onClickListener.onItemClick(i);
               }
            }
        });

        if (pressIndex == i){
            viewHolder.root.setBackground(context.getResources().getDrawable(R.drawable.bg_cost_press));
            viewHolder.tv_title.setBackground(context.getResources().getDrawable(R.drawable.bg_cost_title_press));
            viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.white));
        }else {
            viewHolder.root.setBackground(context.getResources().getDrawable(R.drawable.bg_cost_normal));
            viewHolder.tv_title.setBackground(context.getResources().getDrawable(R.drawable.bg_cost_title_normal));
            viewHolder.tv_title.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return payWayBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_content;
        LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            root = itemView.findViewById(R.id.root);
        }
    }

    public interface OnClickListener {
        void onItemClick(int position);
    }
}
