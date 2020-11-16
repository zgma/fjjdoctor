package com.qingeng.apilibrary.bean;

public class UserBean extends BaseBean {

    private VipBean vip;
    private UserInfoBean user;
    private String userArchivesDate;
    private int userArchivesDay;
    private int userArchivesCount;
    private int oblOrdersNum;
    private int saleOrdersNum;

    public VipBean getVip() {
        return vip;
    }

    public void setVip(VipBean vip) {
        this.vip = vip;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public String getUserArchivesDate() {
        return userArchivesDate;
    }

    public void setUserArchivesDate(String userArchivesDate) {
        this.userArchivesDate = userArchivesDate;
    }

    public int getUserArchivesDay() {
        return userArchivesDay;
    }

    public void setUserArchivesDay(int userArchivesDay) {
        this.userArchivesDay = userArchivesDay;
    }

    public int getUserArchivesCount() {
        return userArchivesCount;
    }

    public void setUserArchivesCount(int userArchivesCount) {
        this.userArchivesCount = userArchivesCount;
    }

    public int getOblOrdersNum() {
        return oblOrdersNum;
    }

    public void setOblOrdersNum(int oblOrdersNum) {
        this.oblOrdersNum = oblOrdersNum;
    }

    public int getSaleOrdersNum() {
        return saleOrdersNum;
    }

    public void setSaleOrdersNum(int saleOrdersNum) {
        this.saleOrdersNum = saleOrdersNum;
    }
}
