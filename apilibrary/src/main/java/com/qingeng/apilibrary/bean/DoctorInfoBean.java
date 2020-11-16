package com.qingeng.apilibrary.bean;

public class DoctorInfoBean extends BaseBean {

    private int doctorId;
    private String realName;
    private String doctorPic;
    private String jobTitle;
    private String workAddress;
    private float askPrice;
    private float riwPrice;

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

    public String getDoctorPic() {
        return doctorPic;
    }

    public void setDoctorPic(String doctorPic) {
        this.doctorPic = doctorPic;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(String workAddress) {
        this.workAddress = workAddress;
    }

    public float getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(float askPrice) {
        this.askPrice = askPrice;
    }

    public float getRiwPrice() {
        return riwPrice;
    }

    public void setRiwPrice(float riwPrice) {
        this.riwPrice = riwPrice;
    }
}
