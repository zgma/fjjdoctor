package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.VipPriceBean;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.ImageFrameUtils;
import com.qingeng.fjjdoctor.util.UiUtils;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class VipPriceAdapter extends RecyclerView.Adapter<VipPriceAdapter.ViewHolder> {

    private Context context;
    private int selectedPosition = 0;

    private VipPriceAdapter.VipPriceListener listener;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void setListener(VipPriceAdapter.VipPriceListener listener) {
        this.listener = listener;
    }

    public VipPriceAdapter(Context context) {
        this.context = context;
    }

    private List<VipPriceBean> list;

    public void setList(List<VipPriceBean> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vip_price, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        VipPriceBean vipPriceBean = list.get(i);

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) viewHolder.root.getLayoutParams();
        layoutParams.width = (int) (UiUtils.getScreenWidth(context) * 0.28);
        layoutParams.height = (int) (UiUtils.getScreenWidth(context) * 0.28 * 1.442);
        viewHolder.root.setLayoutParams(layoutParams);
        if (i == selectedPosition) {
            ImageFrameUtils.showImageToView(viewHolder.iv_bg, R.mipmap.vip_selected_bg);
        } else {
            ImageFrameUtils.showImageToView(viewHolder.iv_bg, R.mipmap.vip_normal_bg);
        }

        viewHolder.tv_price.setText(vipPriceBean.getPrice() + "");
        viewHolder.tv_type.setText(vipPriceBean.getName() + "");
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onClick(i, vipPriceBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_type;
        private TextView tv_price;
        private CardView root;
        private ImageView iv_bg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            root = (CardView) itemView.findViewById(R.id.root);
            iv_bg = (ImageView) itemView.findViewById(R.id.iv_bg);
        }
    }


    public interface VipPriceListener {
        void onClick(int position, VipPriceBean vipPriceBean);
    }
}
