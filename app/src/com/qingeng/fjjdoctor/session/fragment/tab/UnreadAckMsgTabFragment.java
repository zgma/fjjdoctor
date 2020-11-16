package com.qingeng.fjjdoctor.session.fragment.tab;

import android.os.Bundle;

import com.qingeng.fjjdoctor.R;
import com.qingeng.fjjdoctor.session.fragment.UnreadAckMsgFragment;
import com.qingeng.fjjdoctor.session.model.AckMsgTab;

/**
 * Created by winnie on 2018/3/15.
 */

public class UnreadAckMsgTabFragment extends AckMsgTabFragment  {

    UnreadAckMsgFragment fragment;

    public UnreadAckMsgTabFragment() {
        this.setContainerId(AckMsgTab.UNREAD.fragmentId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onCurrent();
    }

    @Override
    protected void onInit() {
        findViews();
    }

    @Override
    public void onCurrent() {
        super.onCurrent();
    }

    private void findViews() {
        fragment = (UnreadAckMsgFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.unread_ack_msg_fragment);

    }
}