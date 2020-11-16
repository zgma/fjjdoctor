package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.qingeng.fjjdoctor.R;

import java.util.List;

/**
 * Created by huangjun on 2016/12/11.
 */

public class RedPacketUserAdapter extends RecyclerView.Adapter<RedPacketUserAdapter.ViewHolder> {

    private Context context;

    private Listener listener;
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public RedPacketUserAdapter(Context context) {
        this.context = context;
    }

    private List<String> userIds;

    public void setUsers(List<String> ids) {
        this.userIds = ids;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_red_packet_group_user,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String id = userIds.get(i);
        if (id.equals("def")){
            viewHolder.head_image.loadImgForId(R.mipmap.btn_add);
        }else {
            viewHolder.head_image.loadAvatar(UserInfoHelper.getUserHead(id));
        }

        viewHolder.head_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userIds.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private HeadImageView head_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            head_image = (HeadImageView) itemView.findViewById(R.id.head_image);
        }
    }


    public interface Listener {
        void onItemClick(String id);
    }


}
