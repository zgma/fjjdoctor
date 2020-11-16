package com.netease.nim.uikit.common.ui.pinyin.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.R;


/**
 * Created by you on 2017/9/12.
 */

public class HeaderHolder extends RecyclerView.ViewHolder {
    public final TextView tv_header;
    public HeaderHolder(View itemView) {
        super(itemView);
        tv_header = (TextView) itemView.findViewById(R.id.tv_header);
    }
}
