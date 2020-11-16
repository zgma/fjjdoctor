package com.qingeng.apilibrary.bean;

public class VipCardBean extends BaseBean {

    private String vipDesc;
    private int vipId;
    private int vipLevel;
    private String vipName;
    private int vipPrice;
    private int vipTime;
    private String vipService;
    private String vipImg;
    private VipCardBean next;

    public String getVipDesc() {
        return vipDesc;
    }

    public void setVipDesc(String vipDesc) {
        this.vipDesc = vipDesc;
    }

    public int getVipId() {
        return vipId;
    }

    public void setVipId(int vipId) {
        this.vipId = vipId;
    }

    public int getVipLevel() {
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    public String getVipName() {
        return vipName;
    }

    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    public int getVipPrice() {
        return vipPrice;
    }

    public void setVipPrice(int vipPrice) {
        this.vipPrice = vipPrice;
    }

    public int getVipTime() {
        return vipTime;
    }

    public void setVipTime(int vipTime) {
        this.vipTime = vipTime;
    }

    public String getVipService() {
        return vipService;
    }

    public void setVipService(String vipService) {
        this.vipService = vipService;
    }

    public String getVipImg() {
        return vipImg;
    }

    public void setVipImg(String vipImg) {
        this.vipImg = vipImg;
    }

    public VipCardBean getNext() {
        return next;
    }

    public void setNext(VipCardBean next) {
        this.next = next;
    }
}
