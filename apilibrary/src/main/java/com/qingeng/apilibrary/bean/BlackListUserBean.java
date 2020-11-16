package com.qingeng.apilibrary.bean;

public class BlackListUserBean extends BaseBean {

    private int id;
    private int userId;
    private int blackUserId;
    private String headImage;
    private String username;
    private String accid;
    private String firstChar;

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

    public int getBlackUserId() {
        return blackUserId;
    }

    public void setBlackUserId(int blackUserId) {
        this.blackUserId = blackUserId;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }
}
