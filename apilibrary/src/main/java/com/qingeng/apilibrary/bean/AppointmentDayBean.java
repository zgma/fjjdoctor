package com.qingeng.apilibrary.bean;

public class AppointmentDayBean extends BaseBean {

    /**
     * {"orderDate":"2020-11-17","mina":"12:00","maxp":"12:00","pm":"0","minp":"12:00","maxa":"00:00","am":"0"}
     */
    private String orderDate;
    private String mina;
    private String maxa;
    private String minp;
    private String maxp;
    private boolean am;
    private boolean pm;

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getMina() {
        return mina;
    }

    public void setMina(String mina) {
        this.mina = mina;
    }

    public String getMaxa() {
        return maxa;
    }

    public void setMaxa(String maxa) {
        this.maxa = maxa;
    }

    public String getMinp() {
        return minp;
    }

    public void setMinp(String minp) {
        this.minp = minp;
    }

    public String getMaxp() {
        return maxp;
    }

    public void setMaxp(String maxp) {
        this.maxp = maxp;
    }

    public boolean isAm() {
        return am;
    }

    public void setAm(boolean am) {
        this.am = am;
    }

    public boolean isPm() {
        return pm;
    }

    public void setPm(boolean pm) {
        this.pm = pm;
    }
}
