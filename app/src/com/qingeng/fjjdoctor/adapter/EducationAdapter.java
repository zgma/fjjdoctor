package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.EducationBean;
import com.qingeng.apilibrary.bean.OrderBean;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.ImageFrameUtils;
import com.qingeng.fjjdoctor.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {

    private Context context;

    public EducationAdapter(Context context) {
        this.context = context;
    }

    private List<EducationBean> dataList = new ArrayList<>();

    public void setDataList(List<EducationBean> dataList) {
        this.dataList = dataList;
    }


    OnListItemClickListener onListItemClickListener;

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_education, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        EducationBean educationBean = dataList.get(i);
        viewHolder.tv_qaa_title.setText(educationBean.getPropTitle());
        viewHolder.tv_qaa_desc.setText(educationBean.getPropContent());
        ImageFrameUtils.showImageToView_Round(viewHolder.iv_qaa_img,educationBean.getPropCover());

        LinearLayout.LayoutParams layoutParamsCard1 = (LinearLayout.LayoutParams) viewHolder.iv_qaa_img.getLayoutParams();
        layoutParamsCard1.height = UiUtils.getMaterialImgHeight(context);
        layoutParamsCard1.width = UiUtils.getMaterialImgHeight(context);
        viewHolder.iv_qaa_img.setLayoutParams(layoutParamsCard1);

        viewHolder.root.setOnClickListener(v -> {
            if (onListItemClickListener != null)
                onListItemClickListener.onItemClick(i, educationBean);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_qaa_title;
        private TextView tv_qaa_desc;
        private ImageView iv_qaa_img;
        private RelativeLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_qaa_title = itemView.findViewById(R.id.tv_qaa_title);
            tv_qaa_desc = itemView.findViewById(R.id.tv_qaa_desc);
            iv_qaa_img = itemView.findViewById(R.id.iv_qaa_img);
            root = (RelativeLayout) itemView.findViewById(R.id.root);
        }
    }

}
