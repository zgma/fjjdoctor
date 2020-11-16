package com.qingeng.apilibrary.bean;

public class UpayResponseBean extends BaseBean {

    private UpayInfoBean object;
    private UpayInfoBean data;
    private String PAY_STATUS;


    public UpayInfoBean getObject() {
        return object;
    }

    public void setObject(UpayInfoBean object) {
        this.object = object;
    }

    public UpayInfoBean getData() {
        return data;
    }

    public void setData(UpayInfoBean data) {
        this.data = data;
    }

    public String getPAY_STATUS() {
        return PAY_STATUS;
    }

    public void setPAY_STATUS(String PAY_STATUS) {
        this.PAY_STATUS = PAY_STATUS;
    }
}
