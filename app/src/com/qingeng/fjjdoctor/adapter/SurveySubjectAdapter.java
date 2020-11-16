package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leon.lib.settingview.LSettingItem;
import com.qingeng.apilibrary.bean.SurveyBean;
import com.qingeng.apilibrary.bean.SurveySubjectBean;
import com.qingeng.fjjdoctor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class SurveySubjectAdapter extends RecyclerView.Adapter<SurveySubjectAdapter.ViewHolder> {

    private Context context;

    public SurveySubjectAdapter(Context context) {
        this.context = context;
    }

    private List<SurveySubjectBean> dataList = new ArrayList<>();

    public void setDataList(List<SurveySubjectBean> dataList) {
        this.dataList = dataList;
    }


    OnListItemClickListener onListItemClickListener;
    OnSubjectActionListener onSubjectActionListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    public void setOnSubjectActionListener(OnSubjectActionListener onSubjectActionListener) {
        this.onSubjectActionListener = onSubjectActionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_survey_subject, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SurveySubjectBean data = dataList.get(i);
        viewHolder.item_1.setLeftText(data.getSubjectName());

        viewHolder.item_1.setmOnLSettingItemViewClick(new LSettingItem.OnLSettingItemViewClick() {
            @Override
            public void onLeftIconClick(int id) {
                if (onSubjectActionListener != null) onSubjectActionListener.onDelete(data);
            }

            @Override
            public void onRightTextClick(int id) {
                if (onSubjectActionListener != null) onSubjectActionListener.onEdit(data);

            }
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

    public interface OnSubjectActionListener {
        void onDelete(SurveySubjectBean surveySubjectBean);

        void onEdit(SurveySubjectBean surveySubjectBean);
    }

}
