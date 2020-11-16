package com.netease.nim.uikit.impl.customization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.api.model.recent.RecentCustomization;
import com.netease.nim.uikit.business.session.helper.TeamNotificationHelper;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjun on 2017/9/29.
 */

public class DefaultRecentCustomization extends RecentCustomization {

    /**
     * 最近联系人列表项文案定制
     *
     * @param recent 最近联系人
     * @return 默认文案
     */
    public String getDefaultDigest(RecentContact recent) {
        switch (recent.getMsgType()) {
            case text:
                return recent.getContent();
            case image:
                return "[图片]";
            case video:
                return "[视频]";
            case audio:
                return "[语音消息]";
            case location:
                return "[位置]";
            case file:
                return "[文件]";
            case tip:
                List<String> uuids = new ArrayList<>();
                uuids.add(recent.getRecentMessageId());
                List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuids);
                if (messages != null && messages.size() > 0) {
                    return messages.get(0).getContent();
                }
                return "[通知提醒]";
            case notification:
                return TeamNotificationHelper.getTeamNotificationText(recent.getContactId(),
                        recent.getFromAccount(),
                        (NotificationAttachment) recent.getAttachment());
            case robot:
                return "[机器人消息]";
        }
        String msgType = "[自定义消息]";
        JSONObject jsonObject = JSON.parseObject(recent.getAttachment().toJson(true));
        if ((int) jsonObject.get("type") == 16) msgType = "[个人名片]";
        if ((int) jsonObject.get("type") == 17) msgType = "[红包]";
        if ((int) jsonObject.get("type") == 18) msgType = "[转账]";
        return msgType;
    }
}
