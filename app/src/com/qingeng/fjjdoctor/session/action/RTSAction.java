package com.qingeng.fjjdoctor.session.action;


import com.netease.nim.uikit.common.ToastHelper;

import com.qingeng.fjjdoctor.R;
//import com.netease.nim.rtskit.RTSKit; rts注释
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;

/**
 * Created by huangjun on 2015/7/7.
 */
public class RTSAction extends BaseAction {

    public RTSAction() {
        super(R.drawable.message_plus_rts_selector, R.string.input_panel_RTS);
    }

    @Override
    public void onClick() {
        if (NetworkUtil.isNetAvailable(getActivity())) {
//            RTSKit.startRTSSession(getActivity(), getAccount()); //import com.netease.nim.rtskit.RTSKit; rts注释
        } else {
            ToastHelper.showToast(getActivity(), R.string.network_is_not_available);
        }

    }
}
