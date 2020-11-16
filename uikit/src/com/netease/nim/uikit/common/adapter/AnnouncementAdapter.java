package com.netease.nim.uikit.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qingeng.apilibrary.bean.AnnouncementBean;
import com.netease.nim.uikit.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 通讯录数据适配器
 * <p/>
 * Created by huangjun on 2015/2/10.
 */
public class AnnouncementAdapter extends BaseAdapter {

    private Context context;

    List<AnnouncementBean> mData = new ArrayList<>();

    private onItemClick onItemClick;

    private boolean canDelete = false;

    public void setOnItemClick(AnnouncementAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public AnnouncementAdapter(Context context) {
        this.context = context;
    }

    public void setmData(List<AnnouncementBean> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            holder=new ViewHolder();
            view= LayoutInflater.from(context)
                    .inflate(R.layout.item_announcement,null,false);
            holder.content=view.findViewById(R.id.content);
            holder.user_name=view.findViewById(R.id.user_name);
            holder.time=view.findViewById(R.id.time);
            holder.delete=view.findViewById(R.id.delete);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }

        holder.delete.setVisibility(canDelete?View.VISIBLE:View.GONE);
        holder.content.setText(mData.get(i).getAnnouncement());
        holder.user_name.setText(mData.get(i).getUserName());
        holder.time.setText(mData.get(i).getCreateDate());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick!=null){
                    onItemClick.onItemClick(mData.get(i));
                }
            }
        });


        return view;
    }
    public final class ViewHolder{
        public TextView content;
        public TextView user_name;
        public TextView time;
        public TextView delete;
    }

   public interface onItemClick{
        void onItemClick(AnnouncementBean bean);
    }
}
