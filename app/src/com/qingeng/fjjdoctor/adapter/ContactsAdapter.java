package com.qingeng.fjjdoctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.qingeng.apilibrary.bean.ContactBean;
import com.qingeng.fjjdoctor.R;

import java.util.List;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private Context context;

    private ContactListener listener;

    public void setListener(ContactListener listener) {
        this.listener = listener;
    }

    public ContactsAdapter(Context context) {
        this.context = context;
    }

    private List<ContactBean> contactBeanList;

    public void setContactBeanList(List<ContactBean> contactBeanList) {
        this.contactBeanList = contactBeanList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ContactBean contactBean = contactBeanList.get(i);
        viewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(contactBean);
                }
            }
        });
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.more(contactBean);
                }
            }
        });

        viewHolder.headImageView.loadImgForUrl(contactBean.getAvatar());
        viewHolder.fromAccountText.setText(contactBean.getNickName());
        viewHolder.contentText.setText(contactBean.getNewestAskContent());
        viewHolder.timeText.setText(TimeUtil.getTimeShowString(TimeUtil.getDateFromFormatString(TimeUtil.dealDateFormat(contactBean.getAskBeginTime()), TimeUtil.DATAFORMATSTRING_yyyyMMddHHmmss), false));
        viewHolder.bottom_line.setVisibility(i == contactBeanList.size() - 1 ? View.GONE : View.VISIBLE);


    }

    @Override
    public int getItemCount() {
        return contactBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private HeadImageView headImageView;
        private TextView fromAccountText;
        private TextView timeText;
        private TextView contentText;
        private TextView more;
        private View bottom_line;
        private LinearLayout root;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            headImageView = (HeadImageView) itemView.findViewById(R.id.from_account_head_image);
            fromAccountText = (TextView) itemView.findViewById(R.id.from_account_text);
            contentText = (TextView) itemView.findViewById(R.id.content_text);
            timeText = (TextView) itemView.findViewById(R.id.from_time);
            more = (TextView) itemView.findViewById(R.id.more);
            bottom_line = itemView.findViewById(R.id.bottom_line);
            root = itemView.findViewById(R.id.root);
        }
    }

    public interface ContactListener {
        void onClick(ContactBean contactBean);

        void more(ContactBean contactBean);
    }
}
