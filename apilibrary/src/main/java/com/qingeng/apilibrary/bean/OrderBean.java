package com.qingeng.apilibrary.bean;

public class OrderBean extends BaseBean {

    /**
     *   "createDate": "2020-06-17 15:01:05",
     *             "id": 85,
     *             "money": 1.00,
     *             "operation": "发布创业乐园",
     *             "userId": 899
     */

    private String createDate;
    private int id;
    private double money;
    private String operation;
    private String fkName;
    private int userId;

    private String avatar;
    private Long ordersNo;
    private int doctorId;
    private String realName;
    private int illnessAge;
    private String illnessDesc;
    private String createTime;


    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFkName() {
        return fkName;
    }

    public void setFkName(String fkName) {
        this.fkName = fkName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getOrdersNo() {
        return ordersNo;
    }

    public void setOrdersNo(Long ordersNo) {
        this.ordersNo = ordersNo;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public int getIllnessAge() {
        return illnessAge;
    }

    public void setIllnessAge(int illnessAge) {
        this.illnessAge = illnessAge;
    }

    public String getIllnessDesc() {
        return illnessDesc;
    }

    public void setIllnessDesc(String illnessDesc) {
        this.illnessDesc = illnessDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
