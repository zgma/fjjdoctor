package com.netease.nim.uikit.business.contact.core.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.contact.core.item.LabelItem;
import com.netease.nim.uikit.business.contact.core.model.ContactDataAdapter;

public class LabelHolder extends AbsContactViewHolder<LabelItem> {

    private TextView name;
    private View label_line;

    @Override
    public void refresh(ContactDataAdapter contactAdapter, int position, LabelItem item) {
        this.label_line.setVisibility(position==0?View.GONE:View.VISIBLE);
        this.name.setText(item.getText());
    }

    @Override
    public View inflate(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.nim_contacts_abc_item, null);
        this.name = view.findViewById(R.id.tv_nickname);
        this.label_line = view.findViewById(R.id.label_line);
        return view;
    }

}
