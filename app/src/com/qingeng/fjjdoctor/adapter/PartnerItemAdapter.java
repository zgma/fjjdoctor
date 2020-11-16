package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.PartnerBean;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.ImageFrameUtils;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class PartnerItemAdapter extends RecyclerView.Adapter<PartnerItemAdapter.ViewHolder> {

    private Context context;

    public PartnerItemAdapter(Context context) {
        this.context = context;
    }

    private boolean canDelete = false;

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    private PartnerItemAdapter.Listener listener;
    public void setListener(PartnerItemAdapter.Listener listener) {
        this.listener = listener;
    }


    private List<PartnerBean> partnerBeans;

    public void setPartnerBeans(List<PartnerBean> partnerBeans) {
        this.partnerBeans = partnerBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_partner, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PartnerBean partnerBean = partnerBeans.get(i);

        viewHolder.iv_name.setText(partnerBean.getName());
        ImageFrameUtils.showImageToView_Round(viewHolder.iv_icon,partnerBean.getLogo());

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onItemClick(partnerBean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return partnerBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView iv_icon;
        private TextView iv_name;
        private LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            iv_name = itemView.findViewById(R.id.tv_name);
            root = itemView.findViewById(R.id.root);
        }
    }

    public interface Listener {
        void onItemClick(PartnerBean partnerBean);
    }



}
