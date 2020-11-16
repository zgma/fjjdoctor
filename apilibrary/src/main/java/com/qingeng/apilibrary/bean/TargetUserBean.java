package com.qingeng.apilibrary.bean;

public class TargetUserBean extends BaseBean {
//
//             "relationDesc": "陌生人",
//            "targetUsername": "小阿木木-186",
//            "currentUserAccid": "516637131117187072",
//            "headImage": "http://xulyqn.ahrjkf.com/upload/2019-11-25/8e8769bbb50e4c33960c3a02868cf900.jpg",
//            "mobile": "18656562665",
//            "targetUserAccid": "513360810299641856",
//            "targetUserId": 22,
//            "relationShip": -1,
//            "uniqueId": null


    private String relationDesc;
    private String targetUsername;
    private String currentUserAccid;
    private String headImage;
    private String mobile;
    private String targetUserAccid;
    private int targetUserId;
    private int relationShip;
    private String uniqueId;
    private String alias;
    private String targetWalletId;

    public String getRelationDesc() {
        return relationDesc;
    }

    public void setRelationDesc(String relationDesc) {
        this.relationDesc = relationDesc;
    }

    public String getTargetUsername() {
        return targetUsername;
    }

    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
    }

    public String getCurrentUserAccid() {
        return currentUserAccid;
    }

    public void setCurrentUserAccid(String currentUserAccid) {
        this.currentUserAccid = currentUserAccid;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTargetUserAccid() {
        return targetUserAccid;
    }

    public void setTargetUserAccid(String targetUserAccid) {
        this.targetUserAccid = targetUserAccid;
    }


    public int getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(int targetUserId) {
        this.targetUserId = targetUserId;
    }

    public int getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(int relationShip) {
        this.relationShip = relationShip;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTargetWalletId() {
        return targetWalletId;
    }

    public void setTargetWalletId(String targetWalletId) {
        this.targetWalletId = targetWalletId;
    }
}
