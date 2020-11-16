package com.qingeng.apilibrary.bean;

public class UserInfoBean extends BaseBean {
    /**
     * "vip": null,
     * "user": {
     * "searchValue": null,
     * "createBy": "",
     * "createTime": "2020-04-02 11:22:52",
     * "updateBy": null,
     * "updateTime": null,
     * "remark": null,
     * "dataScope": null,
     * "params": {},
     * "userId": 13,
     * "deptId": null,
     * "userName": "18656058698",
     * "nickName": "186****8698",
     * "email": "",
     * "phonenumber": "186****8698",
     * "sex": "0",
     * "avatar": "",
     * "password": "$2a$10$m9o9VNimqBgORAKP4pa8jOOZxKRta88AJytS/ofmxpLwbOhTYzcLO",
     * "salt": null,
     * "status": "0",
     * "delFlag": "0",
     * "loginIp": "",
     * "loginDate": null,
     * "roles": [],
     * "roleIds": null,
     * "userType": "10",
     * "realName": null,
     * "nativePlace": null,
     * "birthDate": null,
     * "admin": false
     * }
     */

    private int userId;
    private String realName;
    private String nickName;
    private String userName;
    private String phonenumber;
    private String sex;
    private String avatar;
    private String status;
    private String userType;
    private String recheckDate;
    private String birthDate;
    private String nativePlace;
    private String accid;
    private String token;

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRecheckDate() {
        return recheckDate;
    }

    public void setRecheckDate(String recheckDate) {
        this.recheckDate = recheckDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
