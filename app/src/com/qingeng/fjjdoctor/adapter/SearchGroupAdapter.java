package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.apilibrary.bean.GroupBean;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class SearchGroupAdapter extends RecyclerView.Adapter<SearchGroupAdapter.ViewHolder> {

    private Context context;

    public SearchGroupAdapter(Context context) {
        this.context = context;
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private List<GroupBean> groupBeans;

    public void setGroupBeans(List<GroupBean> groupBeans) {
        this.groupBeans = groupBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_group,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        GroupBean groupBean = groupBeans.get(i);
        if (TextUtils.isEmpty(groupBean.getHeadImage())){
            viewHolder.headPic.loadImgForId(R.drawable.nim_avatar_group);
        }else {
            viewHolder.headPic.loadImgForUrl(groupBean.getHeadImage());
        }
        viewHolder.tv_nickname.setText(groupBean.getTname());
        if (groupBean.getJoin().equals("不在群")){
            viewHolder.btn_add.setVisibility(View.VISIBLE);
        }else {
            viewHolder.btn_add.setVisibility(View.GONE);
        }
        viewHolder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.add(groupBean);
                }
            }
        });
        viewHolder.listItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // UserProfileActivity2.start(context, user.getUserId()+"");
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupBeans.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        HeadImageView headPic;
        TextView tv_nickname;
        Button btn_add;
        RelativeLayout listItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headPic = itemView.findViewById(R.id.img_head);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            btn_add = itemView.findViewById(R.id.btn_add);
            listItemLayout = itemView.findViewById(R.id.listItemLayout);
        }
    }

    public interface Listener{
        void add(GroupBean groupBean);
    }
}
