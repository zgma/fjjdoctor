package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingeng.apilibrary.bean.UserBean;
import com.qingeng.apilibrary.bean.UserInfoBean;
import com.qingeng.fjjdoctor.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;

import java.util.ArrayList;
import java.util.List;


/**
 * 通讯录数据适配器
 * <p/>
 * Created by huangjun on 2015/2/10.
 */
public class MyContactDataAdapter extends BaseAdapter {

    private Context context;

    List<UserInfoBean> mData = new ArrayList<>();


    public MyContactDataAdapter(Context context) {
        this.context = context;
    }

    public void setmData(List<UserInfoBean> mData) {
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
                    .inflate(R.layout.item_contacts_select,null,false);
            holder.img_head=view.findViewById(R.id.img_head);
            holder.tv_nickname=view.findViewById(R.id.tv_nickname);
            holder.imgSelect=view.findViewById(R.id.imgSelect);
            holder.listItemLayout=view.findViewById(R.id.listItemLayout);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        holder.img_head.loadImgForUrl(mData.get(i).getAvatar());
        holder.tv_nickname.setText(mData.get(i).getNickName());

        holder.listItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
    public final class ViewHolder{
        private HeadImageView img_head;
        public TextView tv_nickname;
        public ImageView imgSelect;
        public RelativeLayout listItemLayout;
    }
}
