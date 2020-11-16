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

public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> implements StickyHeaderAdapter<HeaderHolder> {

    private boolean showBottomCount = false;
    private List<CNPinyin<Contact>> cnPinyinList;

    private OnItemClickLisenter onItemClickLisenter;

    public void setOnItemClickLisenter(OnItemClickLisenter onItemClickLisenter) {
        this.onItemClickLisenter = onItemClickLisenter;
    }

    public ContactAdapter() {
    }

    public void setCnPinyinList(List<CNPinyin<Contact>> cnPinyinList) {
        this.cnPinyinList = cnPinyinList;
    }

    public void setShowBottomCount(boolean showBottomCount) {
        this.showBottomCount = showBottomCount;
    }

    private boolean isPhone = false;

    public void setPhone(boolean phone) {
        isPhone = phone;
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
        if (contact.rid > 0) {
            holder.iv_header.loadImgForId(contact.rid);
        } else {
            holder.iv_header.loadImgForUrl(contact.imgUrl);
        }
        holder.tv_name.setText(TextUtils.isEmpty(contact.getAlias()) ? contact.name : contact.getAlias());
        if (isPhone && contact.getMobile() != null) {
            holder.tv_phone.setVisibility(View.VISIBLE);
            holder.tv_phone.setText(contact.getMobile());
        } else {
            holder.tv_phone.setVisibility(View.GONE);
        }

        if (showBottomCount && position == cnPinyinList.size() - 1) {
            holder.contact_count.setVisibility(View.VISIBLE);
            int count = (cnPinyinList.size() - 3);
            holder.contact_count.setText((count < 0 ? 0 : count)+"位联系人");
        } else {
            holder.contact_count.setVisibility(View.GONE);
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickLisenter != null) {
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

    public interface OnItemClickLisenter {
        void onItemClick(int position);
    }

}
