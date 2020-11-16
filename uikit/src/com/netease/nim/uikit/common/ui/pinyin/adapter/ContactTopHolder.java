package com.netease.nim.uikit.common.ui.pinyin.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;


/**
 * Created by you on 2017/9/11.
 */

public class ContactTopHolder extends RecyclerView.ViewHolder {

    public final HeadImageView iv_header;

    public final TextView tv_name;
    public final TextView tv_phone;

    public final View root;

    public ContactTopHolder(View itemView) {
        super(itemView);
        iv_header = (HeadImageView) itemView.findViewById(R.id.iv_head);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
        root = itemView;
    }
}
