package com.qingeng.apilibrary.bean;

public class RedPacketResponseBean extends BaseBean {

    private int businessCode;
    private RedPacketReceiverBean receiver;
    private RedPacketBean object;

    public int getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }

    public RedPacketReceiverBean getReceiver() {
        return receiver;
    }

    public void setReceiver(RedPacketReceiverBean receiver) {
        this.receiver = receiver;
    }

    public RedPacketBean getObject() {
        return object;
    }

    public void setObject(RedPacketBean object) {
        this.object = object;
    }
}
