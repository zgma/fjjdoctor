package com.qingeng.apilibrary.bean;

public class ContactBean extends BaseBean {

    private int id;
    private int userId;
    private String nickName;
    private String avatar;
    private String phonenumber;
    private String newestAskContent;
    private String askBeginTime;
    private String handleAskTime;
    private String groupNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getNewestAskContent() {
        return newestAskContent;
    }

    public void setNewestAskContent(String newestAskContent) {
        this.newestAskContent = newestAskContent;
    }

    public String getAskBeginTime() {
        return askBeginTime;
    }

    public void setAskBeginTime(String askBeginTime) {
        this.askBeginTime = askBeginTime;
    }

    public String getHandleAskTime() {
        return handleAskTime;
    }

    public void setHandleAskTime(String handleAskTime) {
        this.handleAskTime = handleAskTime;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }
}
