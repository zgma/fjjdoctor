package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.ViewHolder> {

    private Context context;

    public SearchUserAdapter(Context context) {
        this.context = context;
    }

    private List<UserBean> userList;

    public void setUserList(List<UserBean> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_user,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        UserBean user = userList.get(i);
        /*viewHolder.headPic.loadImgForUrl(user.getHeadImage());
        viewHolder.tv_nickname.setText(user.getUsername());
        viewHolder.listItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileActivity2.start(context, user.getUserId()+"");
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        HeadImageView headPic;
        TextView tv_nickname;
        RelativeLayout listItemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headPic = itemView.findViewById(R.id.img_head);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            listItemLayout = itemView.findViewById(R.id.listItemLayout);
        }
    }
}
