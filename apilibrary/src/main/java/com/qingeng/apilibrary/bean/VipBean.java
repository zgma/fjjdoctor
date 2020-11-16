package com.qingeng.apilibrary.bean;

public class VipBean extends BaseBean {
    private int vipId;
    private String vipNo;
    private String expireDate;
    private String getDate;
    private VipCardBean vip;

    public int getVipId() {
        return vipId;
    }

    public void setVipId(int vipId) {
        this.vipId = vipId;
    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public VipCardBean getVip() {
        return vip;
    }

    public void setVip(VipCardBean vip) {
        this.vip = vip;
    }

    public String getGetDate() {
        return getDate;
    }

    public void setGetDate(String getDate) {
        this.getDate = getDate;
    }
}
