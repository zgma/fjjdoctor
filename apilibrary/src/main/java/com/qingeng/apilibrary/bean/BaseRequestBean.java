package com.qingeng.apilibrary.bean;

import com.qingeng.apilibrary.contact.MainConstant;

import java.util.HashMap;
import java.util.Map;

public class BaseRequestBean extends BaseBean {
    private Map<String,Object> body = new HashMap<>();

    /**
     * 添加请求数据
     * @param key
     * @param value
     */
    public void addParams(String key, String value) {
        if (body == null) {
            body = new HashMap<String, Object>();
        }
        body.put("userType", MainConstant.USER_TYPE);
        body.put(key, "" + value);
    }
    /**
     * 添加请求数据
     * @param key
     * @param value
     */
    public void addParams(String key, Object value) {
        if (body == null) {
            body = new HashMap<String, Object>();
        }
        body.put("userType", MainConstant.USER_TYPE);
        body.put(key, value);
    }


    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }
}
