package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.HelpBean;
import com.qingeng.fjjdoctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class HelpCenterAdapter extends RecyclerView.Adapter<HelpCenterAdapter.ViewHolder> {

    private Context context;

    private HelpCenterAdapter.Listener listener;

    public void setListener(HelpCenterAdapter.Listener listener) {
        this.listener = listener;
    }

    public HelpCenterAdapter(Context context) {
        this.context = context;
    }

    private List<HelpBean> helpBeans = new ArrayList<>();

    public void setHelpBeans(List<HelpBean> helpBeans) {
        this.helpBeans = helpBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_help_center,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        HelpBean helpBean = helpBeans.get(i);
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(helpBean);
                }
            }
        });
        viewHolder.title.setText(helpBean.getTitle());
    }

    @Override
    public int getItemCount() {
        return helpBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            root = (LinearLayout) itemView.findViewById(R.id.root);
        }
    }


    public interface Listener {
        void onClick(HelpBean helpBean);
    }
}
