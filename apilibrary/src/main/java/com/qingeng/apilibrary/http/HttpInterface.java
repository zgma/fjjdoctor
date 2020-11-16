package com.qingeng.apilibrary.http;


import com.qingeng.apilibrary.bean.BaseResponseData;

public interface HttpInterface {

    void onSuccess(int requestCode, BaseResponseData baseResponseData);
    void onFailure(int requestCode, String message);
    void onComplete();
}
