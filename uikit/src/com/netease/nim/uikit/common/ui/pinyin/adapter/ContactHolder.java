package com.netease.nim.uikit.common.ui.pinyin.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;


/**
 * Created by you on 2017/9/11.
 */

public class ContactHolder extends RecyclerView.ViewHolder {

    public final RelativeLayout layout_contact;
    public final HeadImageView iv_header;

    public final TextView tv_name;
    public final TextView tv_phone;
    public final TextView contact_count;


    public final View root;

    public ContactHolder(View itemView) {
        super(itemView);
        layout_contact = (RelativeLayout) itemView.findViewById(R.id.layout_contact);
        iv_header = (HeadImageView) itemView.findViewById(R.id.iv_head);
        tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        tv_phone = (TextView) itemView.findViewById(R.id.tv_phone);
        contact_count = (TextView) itemView.findViewById(R.id.contact_count);
        root = itemView;
    }
}
