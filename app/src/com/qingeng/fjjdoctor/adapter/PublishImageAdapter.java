package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.ImageBean;
import com.qingeng.apilibrary.bean.PublishBean;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.util.ImageFrameUtils;

import java.util.ArrayList;

/**
 * Created by huangjun on 2016/12/11.
 */

public class PublishImageAdapter extends RecyclerView.Adapter<PublishImageAdapter.ViewHolder> {

    private Context context;

    private PublishBean publishBean;

    public void setPublishBean(PublishBean publishBean) {
        this.publishBean = publishBean;
    }

    public PublishImageAdapter(Context context) {
        this.context = context;
    }

    private boolean canDelete = false;

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    private PublishImageAdapter.Listener listener;
    public void setListener(PublishImageAdapter.Listener listener) {
        this.listener = listener;
    }


    private ArrayList<ImageBean> imageBeans;
    public void setImageBeans(ArrayList<ImageBean> imageBeans) {
        this.imageBeans = imageBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_publish_image, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ImageBean imageBean = imageBeans.get(i);

        viewHolder.iv_delete.setVisibility(canDelete?View.VISIBLE:View.GONE);

        if (imageBean.getIconId() > 0){
            viewHolder.iv_content.setImageResource(imageBean.getIconId());
            viewHolder.iv_delete.setVisibility(View.GONE);
        }else {
            ImageFrameUtils.showImageToView_Round(viewHolder.iv_content,imageBean.getImage());
        }


        viewHolder.iv_content.post(() -> {
            int contentWidth = viewHolder.iv_content.getWidth();
            RelativeLayout.LayoutParams layoutParamsCard2 = (RelativeLayout.LayoutParams) viewHolder.iv_content.getLayoutParams();
            layoutParamsCard2.height = contentWidth;
            viewHolder.iv_content.setLayoutParams(layoutParamsCard2);

        });

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.onImageItemClick(i,imageBeans,imageBean,publishBean);
            }
        });
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null) listener.imageDelete(imageBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageBeans.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_content;
        private ImageView iv_delete;
        private RelativeLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_content = (ImageView) itemView.findViewById(R.id.iv_content);
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
            root = (RelativeLayout) itemView.findViewById(R.id.root);
        }
    }

    public interface Listener {
        void onImageItemClick(int position,ArrayList<ImageBean> imageBeans, ImageBean imageBean,PublishBean publishBean);
        void imageDelete(ImageBean imageBean);
    }



}
