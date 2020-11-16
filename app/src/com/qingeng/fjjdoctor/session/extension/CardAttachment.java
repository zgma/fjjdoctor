package com.qingeng.fjjdoctor.session.extension;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhoujianghua on 2015/7/8.
 */
public class CardAttachment extends CustomAttachment {


    private final String KEY_NAME = "name";
    private final String KEY_HEADURL = "headUrl";
    private final String KEY_ACCID = "accid";
    private final String KEY_USERID = "userId";

    private String name;
    private String headUrl;
    private String accid;
    private String userId;

    public CardAttachment() {
        super(CustomAttachmentType.card);
    }

    public CardAttachment(int type, String name, String headUrl, String accid, String userId) {
        this();
        this.name = name;
        this.headUrl = headUrl;
        this.accid = accid;
        this.userId = userId;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.name = data.getString(KEY_NAME);
        this.headUrl = data.getString(KEY_HEADURL);
        this.accid = data.getString(KEY_ACCID);
        this.userId = data.getString(KEY_USERID);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_NAME, name);
        data.put(KEY_HEADURL, headUrl);
        data.put(KEY_ACCID, accid);
        data.put(KEY_USERID, userId);
        return data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
