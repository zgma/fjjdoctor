package com.qingeng.fjjdoctor.session.action;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.qingeng.apilibrary.bean.BaseResponseData;
import com.qingeng.apilibrary.bean.MyFriendBean;
import com.qingeng.apilibrary.contact.RequestCommandCode;
import com.qingeng.apilibrary.http.HttpClient;
import com.qingeng.apilibrary.http.HttpInterface;
import com.qingeng.fjjdoctor.session.extension.CardAttachment;
import com.qingeng.fjjdoctor.session.extension.CustomAttachmentType;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.business.team.helper.TeamHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hzxuwen on 2015/6/12.
 */
public class CardAction extends BaseAction {

    public CardAction() {
        super(R.drawable.dialogue_car, R.string.input_panel_car);
    }

    @Override
    public void onClick() {
        getSelect();

    }

    public void getSelect() {
        ContactSelectActivity.Option advancedOption = TeamHelper
                .getCreateContactSelectOption(null, 1);
        NimUIKit.startContactSelector(getActivity(), advancedOption,
                makeRequestCode(RequestCode.GET_LOCAL_FILE));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final ArrayList<String> selected = data.getStringArrayListExtra(
                ContactSelectActivity.RESULT_DATA);

        DialogMaker.showProgressDialog(getActivity(), "发送中...");
        HttpClient.queryMyFriends(null, new HttpInterface() {
            @Override
            public void onSuccess(int requestCode, BaseResponseData baseResponseData) {
                List<MyFriendBean> myFriendBeans = new ArrayList<>();
                JSONArray objects = JSON.parseArray(JSON.toJSONString(baseResponseData.getData()));
                for (int i = 0; i < objects.size(); i++) {
                    JSONArray array = JSON.parseArray(JSON.toJSONString(objects.get(i)));
                    List<MyFriendBean> users = JSON.parseArray(JSON.toJSONString(array), MyFriendBean.class);
                    myFriendBeans.addAll(users);
                }
                Map<String, MyFriendBean> userMaps = new HashMap<>();
                for (int i = 0; i < myFriendBeans.size(); i++) {
                    userMaps.put(myFriendBeans.get(i).getFaccid(), myFriendBeans.get(i));
                }
                ArrayList<MyFriendBean> selectedFriends = new ArrayList<>();
                for (int i = 0; i < selected.size(); i++) {
                    selectedFriends.add(userMaps.get(selected.get(i)));
                }
                if (selectedFriends.size() < 1) return;
                MyFriendBean myFriendBean = selectedFriends.get(0);
                if (myFriendBean == null){
                    ToastHelper.showToast(getActivity(), "此用户不是你的好友");
                    return;
                }
                CardAttachment attachment = new CardAttachment(CustomAttachmentType.card, myFriendBean.getUsername(), myFriendBean.getHeadImage(),
                        myFriendBean.getFaccid(), myFriendBean.getUserId()+"");
                IMMessage message;

                if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
                    message = ChatRoomMessageBuilder.createChatRoomCustomMessage(getAccount(), attachment);
                    sendMessage(message);
                } else {
                    List<String> accounts = Arrays.asList(getAccount().split(","));
                    for (String account : accounts) {
                        message = MessageBuilder.createCustomMessage(account, getSessionType(), attachment);
                        sendMessage(message);
                    }
                    if (accounts.size()>1){
                        ToastHelper.showToast(getActivity(), "发送成功");
                        getContainer().activity.finish();
                    }
                }


            }

            @Override
            public void onFailure(int requestCode, String message) {
                ToastHelper.showToast(getActivity(), message);
            }

            @Override
            public void onComplete() {
                DialogMaker.dismissProgressDialog();

            }
        }, RequestCommandCode.QUERY_MY_FRIENDS);
    }
}

