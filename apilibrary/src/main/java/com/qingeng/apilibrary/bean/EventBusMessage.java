package com.qingeng.apilibrary.bean;

public class EventBusMessage extends BaseBean{

    public final static String LOG_OUT = "LOG_OUT";
    public final static String MESSAGE_REFRESH_LIST = "MESSAGE_REFRESH_LIST";

    public EventBusMessage(String code) {
        this.code = code;
    }

    private String code;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
