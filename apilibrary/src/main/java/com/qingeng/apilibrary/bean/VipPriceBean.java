package com.qingeng.apilibrary.bean;

public class VipPriceBean extends BaseBean {

    private int id;
    private int dayNum;
    private int vipSort;
    private String desc;
    private String name;
    private double price;
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public int getVipSort() {
        return vipSort;
    }

    public void setVipSort(int vipSort) {
        this.vipSort = vipSort;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
