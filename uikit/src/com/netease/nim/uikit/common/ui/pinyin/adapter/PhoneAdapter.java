package com.netease.nim.uikit.common.ui.pinyin.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.common.ui.pinyin.Contact;
import com.netease.nim.uikit.common.ui.pinyin.cn.CNPinyin;
import com.netease.nim.uikit.common.ui.pinyin.stickyheader.StickyHeaderAdapter;

import java.util.List;


/**
 * Created by you on 2017/9/11.
 */

public class PhoneAdapter extends RecyclerView.Adapter<ContactHolder> implements StickyHeaderAdapter<HeaderHolder> {

    private List<CNPinyin<Contact>> cnPinyinList;

    private OnItemClickLisenter onItemClickLisenter;

    public void setOnItemClickLisenter(OnItemClickLisenter onItemClickLisenter) {
        this.onItemClickLisenter = onItemClickLisenter;
    }

    public PhoneAdapter() {
    }

    public void setCnPinyinList(List<CNPinyin<Contact>> cnPinyinList) {
        this.cnPinyinList = cnPinyinList;
    }

    @Override
    public int getItemCount() {
        return cnPinyinList.size();
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pinyin_contact, parent, false));
    }


    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        Contact contact = cnPinyinList.get(position).data;
        holder.iv_header.loadImgForUrl(contact.imgUrl);
        holder.tv_name.setText(TextUtils.isEmpty(contact.getAlias())?contact.name:contact.getAlias());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickLisenter!=null){
                    onItemClickLisenter.onItemClick(position);
                }
            }
        });

    }

    @Override
    public long getHeaderId(int childAdapterPosition) {
        return cnPinyinList.get(childAdapterPosition).getFirstChar();
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder holder, int childAdapterPosition) {
        holder.tv_header.setText(String.valueOf(cnPinyinList.get(childAdapterPosition).getFirstChar()));
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return new HeaderHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pinyin_header, parent, false));
    }

    public interface OnItemClickLisenter{
        void onItemClick(int position);
    }

}
