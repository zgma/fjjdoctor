package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.lib.settingview.LSettingItem;
import com.qingeng.apilibrary.bean.SurveySubjectOptionBean;
import com.qingeng.fjjdoctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class SurveySubjectOptionAdapter extends RecyclerView.Adapter<SurveySubjectOptionAdapter.ViewHolder> {

    private Context context;

    public SurveySubjectOptionAdapter(Context context) {
        this.context = context;
    }

    private List<SurveySubjectOptionBean> dataList = new ArrayList<>();

    public void setDataList(List<SurveySubjectOptionBean> dataList) {
        this.dataList = dataList;
    }


    OnListItemClickListener onListItemClickListener;
    OnActionListener onActionListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_survey_subject_option, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SurveySubjectOptionBean data = dataList.get(i);
        viewHolder.item_1.setLeftText(data.getOptionContent());
        viewHolder.item_1.setmOnLSettingItemViewClick(new LSettingItem.OnLSettingItemViewClick() {
            @Override
            public void onLeftIconClick(int id) {
                if (onActionListener != null) onActionListener.onDelete(data);
            }

            @Override
            public void onRightTextClick(int id) {

            }
        });

        if (i == getItemCount() - 1) {
            viewHolder.layout_add.setVisibility(View.VISIBLE);
            viewHolder.item_1.setVisibility(View.GONE);
        } else {
            viewHolder.layout_add.setVisibility(View.GONE);
            viewHolder.item_1.setVisibility(View.VISIBLE);
        }

        viewHolder.layout_add.setOnClickListener(v -> {
                    if (onActionListener != null) onActionListener.add();
                }
        );

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private LSettingItem item_1;
        private RelativeLayout root;
        private LinearLayout layout_add;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            item_1 = itemView.findViewById(R.id.item_1);
            layout_add = itemView.findViewById(R.id.layout_add);
        }
    }

    public interface OnActionListener {
        void onDelete(SurveySubjectOptionBean surveySubjectBean);

        void add();
    }


}
