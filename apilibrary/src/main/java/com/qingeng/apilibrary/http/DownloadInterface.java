package com.qingeng.apilibrary.http;




public interface DownloadInterface {

    void onSuccess(boolean isSuccess);
    void onFailure(String message);
    void onComplete();
}
