package com.qingeng.fjjdoctor.session.extension;

import com.alibaba.fastjson.JSONObject;

public class TransferAttachment extends CustomAttachment {


    private String transferMmoeny;//  消息文本内容
    private String accid;// 红包名称
    private String userName;// 红包名称
    private String userId;// 红包名称
    private String desc;// 红包名称
    private String toUserName;// 红包名称
    private String toUserId;// 红包名称
    private String orderNum;// 红包名称
    private String createDate;// 红包名称
    private String requestId;//


    private static final String KEY_TRANSFER_MONEY = "transferMmoeny";
    private static final String KEY_ACCID = "accid";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USERID = "userId";
    private static final String KEY_DESC = "desc";
    private static final String KEY_TOUSERNAME = "toUserName";
    private static final String KEY_TOUSERID = "toUserId";
    private static final String KEY_ORDERNO = "orderNum";
    private static final String KEY_CREATEDATE = "createDate";
    private static final String KEY_REQUEST_ID = "requestId";

    public TransferAttachment() {
        super(CustomAttachmentType.Transfer);
    }

    @Override
    protected void parseData(JSONObject data) {
        transferMmoeny = data.getString(KEY_TRANSFER_MONEY);
        accid = data.getString(KEY_ACCID);
        userName = data.getString(KEY_USER_NAME);
        userId = data.getString(KEY_USERID);
        desc = data.getString(KEY_DESC);
        toUserName = data.getString(KEY_TOUSERNAME);
        toUserId = data.getString(KEY_TOUSERID);
        orderNum = data.getString(KEY_ORDERNO);
        createDate = data.getString(KEY_CREATEDATE);
        requestId = data.getString(KEY_REQUEST_ID);



    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(KEY_TRANSFER_MONEY, transferMmoeny);
        data.put(KEY_ACCID, accid);
        data.put(KEY_USER_NAME, userName);
        data.put(KEY_USERID, userId);
        data.put(KEY_DESC, desc);
        data.put(KEY_TOUSERNAME, toUserName);
        data.put(KEY_TOUSERID, toUserId);
        data.put(KEY_ORDERNO, orderNum);
        data.put(KEY_CREATEDATE, createDate);
        data.put(KEY_REQUEST_ID, requestId);

        return data;
    }

    public String getTransferMmoeny() {
        return transferMmoeny;
    }

    public void setTransferMmoeny(String transferMmoeny) {
        this.transferMmoeny = transferMmoeny;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
