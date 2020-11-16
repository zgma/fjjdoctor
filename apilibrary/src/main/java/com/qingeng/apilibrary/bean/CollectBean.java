package com.qingeng.apilibrary.bean;

public class CollectBean extends BaseBean {

    /**
     *  "id": 25,
     *             "msgId": null,
     *             "type": 1,
     *             "content": "吃饭饭",
     *             "createDate": "2019-12-08 19:53:28",
     *             "userId": 26,
     *             "targetUserId": 516637131117187072,
     *             "typeDesc": "文字"
     */

    private int id;
    private String msgId;
    private String type;
    private String content;
    private String createDate;
    private String userId;
    private String targetUserId;
    private String typeDesc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }
}
