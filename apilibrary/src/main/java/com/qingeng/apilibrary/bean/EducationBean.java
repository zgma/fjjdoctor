package com.qingeng.apilibrary.bean;

public class EducationBean extends BaseBean{
    private String createTime;
    private String propTitle;
    private String propCover;
    private String propContent;
    private int examineStat;
    private int doctorUserId;
    private int id;
    private int readNum;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPropTitle() {
        return propTitle;
    }

    public void setPropTitle(String propTitle) {
        this.propTitle = propTitle;
    }

    public String getPropCover() {
        return propCover;
    }

    public void setPropCover(String propCover) {
        this.propCover = propCover;
    }

    public String getPropContent() {
        return propContent;
    }

    public void setPropContent(String propContent) {
        this.propContent = propContent;
    }

    public int getExamineStat() {
        return examineStat;
    }

    public void setExamineStat(int examineStat) {
        this.examineStat = examineStat;
    }

    public int getDoctorUserId() {
        return doctorUserId;
    }

    public void setDoctorUserId(int doctorUserId) {
        this.doctorUserId = doctorUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }
}
