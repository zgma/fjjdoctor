package com.qingeng.apilibrary.bean;

public class ApplyBean extends BaseBean {

    /**
     * "id": 38,
     *             "accid": "516637131117187072",
     *             "faccid": "516850842335465472",
     *             "type": 2,
     *             "msg": "加我吧",
     *             "serverex": null,
     *             "alias": null,
     *             "ex": null,
     *             "updatetime": null,
     *             "createtime": null,
     *             "notice": null,
     *             "black": null,
     *             "createDate": "2019-11-30 20:06:31",
     *             "updateDate": null,
     *             "username": "咳咳咳",
     *             "headImage": "http://xulyqn.ahrjkf.com/upload/2019-11-27/d98c8d70506748449f84a9542706bb6b.jpg",
     *             "firstChar": "K",
     *             "mobile": "18715064704",
     *             "userId": 27,
     *             "relationShipDesc": "请求待处理",
     *             "remark": null,
     *             "topUser": null,
     *             "checkResult": null,
     *             "topDesc": "非置顶",
     *             "noticeDesc": "不静音",
     *             "ugId": null,
     *             "exists": "不在群"
     */

    private int id;
    private String accid;
    private String faccid;
    private int type;
    private String msg;
    private String username;
    private String headImage;
    private String firstChar;
    private String mobile;
    private String createDate;
    private int userId;
    private String relationShipDesc;
    private String exists;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getFaccid() {
        return faccid;
    }

    public void setFaccid(String faccid) {
        this.faccid = faccid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRelationShipDesc() {
        return relationShipDesc;
    }

    public void setRelationShipDesc(String relationShipDesc) {
        this.relationShipDesc = relationShipDesc;
    }

    public String getExists() {
        return exists;
    }

    public void setExists(String exists) {
        this.exists = exists;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
