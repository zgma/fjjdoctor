package com.qingeng.apilibrary.bean;

public class GroupBean extends BaseBean {
    /**
     * "currentUserIdentity": "群主",
     *             "id": 39,
     *             "userId": null,
     *             "groupId": 23,
     *             "topTalk": 0,
     *             "tname": "咳咳咳",
     *             "groupType": null,
     *             "createDate": "2019-12-01 16:52:05",
     *             "identity": 1,
     *             "headImage": null,
     *             "username": null,
     *             "status": 0,
     *             "expireDate": null,
     *             "icon": null,
     *             "notice": null,
     *             "noticeDesc": "静音",
     *             "topDesc": "非置顶",
     *             "tid": "2727273784",
     *             "accid": null,
     *             "expireStatus": "未到期",
     *             "talkName": null,
     *             "firstChar": null
     *
     *
     */

    private String currentUserIdentity;
    private int id;
    private String groupId;
    private String topTalk;
    private String tname;
    private String groupType;
    private int identity;
    private String topDesc;
    private String tid;
    private String expireStatus;
    private String headImage;
    private String icon;
    private String join;

    public String getCurrentUserIdentity() {
        return currentUserIdentity;
    }

    public void setCurrentUserIdentity(String currentUserIdentity) {
        this.currentUserIdentity = currentUserIdentity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTopTalk() {
        return topTalk;
    }

    public void setTopTalk(String topTalk) {
        this.topTalk = topTalk;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public String getTopDesc() {
        return topDesc;
    }

    public void setTopDesc(String topDesc) {
        this.topDesc = topDesc;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getExpireStatus() {
        return expireStatus;
    }

    public void setExpireStatus(String expireStatus) {
        this.expireStatus = expireStatus;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }
}
