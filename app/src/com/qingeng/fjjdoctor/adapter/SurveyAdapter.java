package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.lib.settingview.LSettingItem;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.apilibrary.bean.OrderBean;
import com.qingeng.apilibrary.bean.SurveyBean;
import com.qingeng.fjjdoctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.ViewHolder> {

    private Context context;

    public SurveyAdapter(Context context) {
        this.context = context;
    }

    private List<SurveyBean> dataList = new ArrayList<>();

    public void setDataList(List<SurveyBean> dataList) {
        this.dataList = dataList;
    }


    OnListItemClickListener onListItemClickListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_survey, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SurveyBean data = dataList.get(i);
        viewHolder.item_1.setLeftText(data.getModelName());

        viewHolder.item_1.setmOnLSettingItemClick((i1, b) -> {
            if (onListItemClickListener != null)
                onListItemClickListener.onItemClick(i, data);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private LSettingItem item_1;
        private RelativeLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            item_1 = itemView.findViewById(R.id.item_1);
        }
    }

}
