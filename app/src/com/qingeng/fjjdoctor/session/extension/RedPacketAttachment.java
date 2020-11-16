package com.qingeng.fjjdoctor.session.extension;

import com.alibaba.fastjson.JSONObject;

public class RedPacketAttachment extends CustomAttachment {




    private String redMmoeny;//
    private String redNum;//
    private String tid;//
    private String accid;//
    private String userName;//
    private String userIcon;//
    private String userId;//
    private String message;//
    private String orderNum;//
    private String opened;//
    private String requestId;//

    private static final String KEY_REDMMOENY = "redMmoeny";
    private static final String KEY_REDNUM = "redNum";
    private static final String KEY_TID = "tid";
    private static final String KEY_ACCID = "accid";
    private static final String KEY_USERICON = "userIcon";
    private static final String KEY_USERID = "userId";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_ORDERNUM = "orderNum";
    private static final String KEY_OPENED = "opened";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_REQUEST_ID = "requestId";

    public RedPacketAttachment() {
        super(CustomAttachmentType.RedPacket);
    }

    @Override
    protected void parseData(JSONObject data) {
        redMmoeny = data.getString(KEY_REDMMOENY);
        redNum = data.getString(KEY_REDNUM);
        tid = data.getString(KEY_TID);
        accid = data.getString(KEY_ACCID);
        userIcon = data.getString(KEY_USERICON);
        userId = data.getString(KEY_USERID);
        message = data.getString(KEY_MESSAGE);
        orderNum = data.getString(KEY_ORDERNUM);
        opened = data.getString(KEY_OPENED);
        userName = data.getString(KEY_USER_NAME);
        requestId = data.getString(KEY_REQUEST_ID);

    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_REDMMOENY, redMmoeny);
        data.put(KEY_REDNUM, redNum);
        data.put(KEY_TID, tid);
        data.put(KEY_ACCID, accid);
        data.put(KEY_USERICON, userIcon);
        data.put(KEY_USERID, userId);
        data.put(KEY_MESSAGE, message);
        data.put(KEY_ORDERNUM, orderNum);
        data.put(KEY_OPENED, opened);
        data.put(KEY_USER_NAME, userName);
        data.put(KEY_REQUEST_ID, requestId);


        return data;
    }

    public String getRedMmoeny() {
        return redMmoeny;
    }

    public void setRedMmoeny(String redMmoeny) {
        this.redMmoeny = redMmoeny;
    }

    public String getRedNum() {
        return redNum;
    }

    public void setRedNum(String redNum) {
        this.redNum = redNum;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }


    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
