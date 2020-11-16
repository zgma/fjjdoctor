package com.qingeng.apilibrary.bean;

import java.util.List;

public class RedPacketBean extends BaseBean {

    private List<RedPacketReceiverBean> receivers;
    private int total;
    private int used;
    private int id;
    private String requestId;
    private String serialNumber;
    private String username;
    private String headImage;
    private String accid;
    private String msg;


    public List<RedPacketReceiverBean> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<RedPacketReceiverBean> receivers) {
        this.receivers = receivers;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getAccid() {
        return accid;
    }

    public void setAccid(String accid) {
        this.accid = accid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
