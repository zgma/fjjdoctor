package com.qingeng.fjjdoctor.session.viewholder;

import android.widget.TextView;

import com.qingeng.fjjdoctor.contact.activity.UserProfileActivity2;
import com.qingeng.fjjdoctor.session.extension.CardAttachment;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;

/**
 * Created by zhoujianghua on 2015/8/5.
 */
public class MsgViewHolderCard extends MsgViewHolderBase {

    private HeadImageView head;
    private TextView name;
    private CardAttachment cardAttachment;


    public MsgViewHolderCard(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    public int getContentResId() {
        return R.layout.nim_message_item_card;
    }

    @Override
    public void inflateContentView() {
        head = findViewById(R.id.head);
        name = findViewById(R.id.name);
    }

    @Override
    public void bindContentView() {
        cardAttachment = (CardAttachment) message.getAttachment();
        if (cardAttachment == null) {
            return;
        }
        head.loadImgForUrl(cardAttachment.getHeadUrl());
        name.setText(cardAttachment.getName());
    }

    @Override
    protected int leftBackground() {
        return 0;
    }

    @Override
    protected int rightBackground() {
        return 0;
    }

    @Override
    public void onItemClick() {
        super.onItemClick();
        UserProfileActivity2.start(context, cardAttachment.getUserId()+"");
    }
}
