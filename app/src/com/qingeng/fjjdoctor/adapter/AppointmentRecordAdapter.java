package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.AppointmentRecordBean;
import com.qingeng.apilibrary.bean.EducationBean;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.ImageFrameUtils;
import com.qingeng.fjjdoctor.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class AppointmentRecordAdapter extends RecyclerView.Adapter<AppointmentRecordAdapter.ViewHolder> {

    private Context context;

    public AppointmentRecordAdapter(Context context) {
        this.context = context;
    }

    private List<AppointmentRecordBean> dataList = new ArrayList<>();

    public void setDataList(List<AppointmentRecordBean> dataList) {
        this.dataList = dataList;
    }


    OnListItemClickListener onListItemClickListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_appointment_record, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AppointmentRecordBean recordBean = dataList.get(i);
        viewHolder.tv_name.setText(recordBean.getRealName());
        viewHolder.tv_sex.setText(recordBean.getGender());
        viewHolder.tv_phone.setText(recordBean.getPhonenumber());
        viewHolder.tv_content.setText(recordBean.getReviewContent());
        viewHolder.root.setOnClickListener(v -> {
            if (onListItemClickListener != null)
                onListItemClickListener.onItemClick(i, recordBean);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;
        private TextView tv_sex;
        private TextView tv_phone;
        private TextView tv_content;
        private RelativeLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }

}
