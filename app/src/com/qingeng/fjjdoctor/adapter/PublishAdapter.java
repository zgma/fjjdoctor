package com.qingeng.fjjdoctor.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.popwindow.ActionItem;
import com.netease.nim.uikit.common.ui.popwindow.TitlePopup;
import com.previewlibrary.GPreviewBuilder;
import com.qingeng.apilibrary.bean.ImageBean;
import com.qingeng.apilibrary.bean.PublishBean;
import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;
import com.qingeng.fjjdoctor.zoom.PreviewImageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class PublishAdapter extends RecyclerView.Adapter<PublishAdapter.ViewHolder> {

    private Context context;

    private PublishAdapter.Listener listener;
    private PublishAdapter.MoreListener moreListener;
    private PublishImageAdapter.Listener imageItemListener;

    private ArrayList<PreviewImageBean> mThumbViewInfoList = new ArrayList<>();


    private boolean isMy = false;

    public void setIsMy(boolean my) {
        isMy = my;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setMoreListener(MoreListener moreListener) {
        this.moreListener = moreListener;
    }

    public PublishAdapter(Context context) {
        this.context = context;
    }

    public void setImageItemListener(PublishImageAdapter.Listener imageItemListener) {
        this.imageItemListener = imageItemListener;
    }

    private List<PublishBean> publishBeans;

    public void setPublishBeans(List<PublishBean> publishBeans) {
        this.publishBeans = publishBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_publish, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PublishBean publishBean = publishBeans.get(i);
        viewHolder.tv_content.setText(publishBean.getTitle());
        viewHolder.tv_name.setText(publishBean.getUsername());
        viewHolder.tv_like_count.setText(publishBean.getLikeCount() + "");
        viewHolder.tv_id.setText("ID:  " + publishBean.getUniqueId());
        viewHolder.img_head.loadAvatar(publishBean.getHeadImage());
        viewHolder.img_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileActivity2.start(context, publishBean.getCreateUser() + "");
            }
        });

        if (!(publishBean.getId() + "").equals(viewHolder.rcv_image.getTag())) {


            PublishImageAdapter publishImageAdapter;
            publishImageAdapter = new PublishImageAdapter(context);
            publishImageAdapter.setPublishBean(publishBean);
            //初始化标签
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3) {
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }

                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };


            viewHolder.rcv_image.setLayoutManager(gridLayoutManager);
            viewHolder.rcv_image.setAdapter(publishImageAdapter);

//        publishImageAdapter.setListener(imageItemListener);
            publishImageAdapter.setListener(new PublishImageAdapter.Listener() {
                @Override
                public void onImageItemClick(int position, ArrayList<ImageBean> imageBeans, ImageBean imageBean, PublishBean publishBean) {
                    mThumbViewInfoList.clear();
                    for (ImageBean imageBean1 : imageBeans) {
                        mThumbViewInfoList.add(new PreviewImageBean(imageBean1.getImage()));
                    }
                    computeBoundsBackward(gridLayoutManager, gridLayoutManager.findFirstVisibleItemPosition());
                    GPreviewBuilder.from((Activity) context)
                            .setData(mThumbViewInfoList)
                            .setCurrentIndex(position)
                            .setSingleFling(true)
                            .setType(GPreviewBuilder.IndicatorType.Number)
                            .start();
                }

                @Override
                public void imageDelete(ImageBean imageBean) {

                }
            });


            publishImageAdapter.setImageBeans(publishBean.getImages());
            publishImageAdapter.notifyDataSetChanged();
            viewHolder.rcv_image.setTag(publishBean.getId() + "");
        }

        viewHolder.iv_more.setVisibility(View.GONE);
        if (isMy) {
            viewHolder.iv_more.setVisibility(View.VISIBLE);
            viewHolder.iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TitlePopup titlePopup = new TitlePopup(v.getContext(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    initTitlePopup(titlePopup, context, moreListener, publishBean);
                    titlePopup.show(viewHolder.iv_more);
                }
            });
        }

        if (TextUtils.isEmpty(publishBean.getUrl())) {
            viewHolder.tv_go_detail.setVisibility(View.GONE);
        } else {
            viewHolder.tv_go_detail.setVisibility(View.VISIBLE);
        }

        viewHolder.tv_go_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onItemClick(publishBean);
            }
        });
        viewHolder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) listener.onLikeClick(i, publishBean);
            }
        });

        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (listener!=null) listener.onItemClick(publishBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return publishBeans.size();
    }

    /**
     * 查找信息
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(GridLayoutManager mGridLayoutManager, int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < mThumbViewInfoList.size(); i++) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();
            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.iv_content);
                thumbView.getGlobalVisibleRect(bounds);
            }
            mThumbViewInfoList.get(i).setBounds(bounds);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        private HeadImageView img_head;
        private TextView tv_name;
        private TextView tv_id;
        private TextView tv_content;
        private TextView tv_go_detail;
        private TextView tv_like_count;
        private ImageView iv_more;
        private ImageView iv_like;
        private RecyclerView rcv_image;
        private LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_head = (HeadImageView) itemView.findViewById(R.id.img_head);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            rcv_image = (RecyclerView) itemView.findViewById(R.id.rcv_image);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
            root = (LinearLayout) itemView.findViewById(R.id.root);
            tv_go_detail = (TextView) itemView.findViewById(R.id.tv_go_detail);
            tv_like_count = (TextView) itemView.findViewById(R.id.tv_like_count);
            iv_like = (ImageView) itemView.findViewById(R.id.iv_like);
        }
    }

    public interface Listener {
        void onItemClick(PublishBean bankCardBean);

        void onLikeClick(int position, PublishBean bankCardBean);
    }


    public interface MoreListener {
        void onDelete(PublishBean bankCardBean);

        void onRePublish(PublishBean bankCardBean);

        void onChange(PublishBean bankCardBean);
    }


    private void initTitlePopup(TitlePopup titlePopup, Context context, PublishAdapter.MoreListener moreListener, PublishBean publishBean) {
        // 给标题栏弹窗添加子类
        titlePopup.addAction(new ActionItem(context, "修改"));
        if (publishBean.getStatus() == 0) {
            titlePopup.addAction(new ActionItem(context, "重新上架"));
        }
        titlePopup.addAction(new ActionItem(context, "删除"));

        titlePopup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                if (moreListener == null) return;
                switch (position) {
                    case 0:
                        moreListener.onChange(publishBean);
                        break;
                    case 1:
                        if (publishBean.getStatus() == 0) {
                            moreListener.onRePublish(publishBean);
                        } else {
                            moreListener.onDelete(publishBean);
                        }
                        break;
                    case 2:
                        moreListener.onDelete(publishBean);
                        break;
                }
            }
        });
    }


}
