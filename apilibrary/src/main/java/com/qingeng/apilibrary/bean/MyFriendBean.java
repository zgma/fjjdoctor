package com.qingeng.apilibrary.bean;

public class MyFriendBean extends BaseBean {

    /**
     *   "id": 42,
     *                 "accid": "516637131117187072",
     *                 "faccid": "518041187035402240",
     *                 "type": 3,
     *                 "msg": "加我吧",
     *                 "serverex": null,
     *                 "alias": null,
     *                 "ex": null,
     *                 "updatetime": null,
     *                 "createtime": null,
     *                 "notice": null,
     *                 "black": null,
     *                 "createDate": "2019-11-30 20:33:10",
     *                 "updateDate": null,
     *                 "username": "反反复复",
     *                 "headImage": "http://xulyqn.ahrjkf.com/upload/2019-11-30/b9c16b8a11fc4f3193af4cd693400164.jpg",
     *                 "firstChar": "F",
     *                 "mobile": "18656058698",
     *                 "userId": 29,
     *                 "relationShipDesc": "好友",
     *                 "remark": null,
     *                 "topUser": null,
     *                 "checkResult": null,
     *                 "topDesc": "非置顶",
     *                 "noticeDesc": "不静音",
     *                 "ugId": null,
     *                 "exists": "不在群"
     */


    private String accid;
    private String faccid;
    private String username;
    private String headImage;
    private String mobile;
    private int userId;

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
}
