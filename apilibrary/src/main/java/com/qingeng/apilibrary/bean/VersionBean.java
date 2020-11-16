package com.qingeng.apilibrary.bean;

public class VersionBean extends BaseBean{

    /**
     * id: 3,
     * appName: "网易云信",
     * updateContent: "版本更新2.0",
     * versionCode: 73,
     * versionName: "7.0.0",
     * packageName: "com.qingeng.fjjdoctor",
     * downloadUrl: "xulyqn.ahrjkf.com/upload/2020-02-22/bbb46d5a9ebb44339dc789588fc82e32.apk",
     * fileName: "app-debug-0221.apk",
     * md5Value: "eadca77103151bf9a3965928794f94bf",
     * fileSize: "22808816",
     * createTime: "2020-02-22 10:56:40",
     * updateTime: null,
     * userId: 1,
     * isForce: null,
     * isIgnorable: null,
     * isSilent: null
     */


    private String appName;
    private String updateContent;
    private int versionCode;
    private String versionName;
    private String packageName;
    private String downloadUrl;
    private String fileName;
    private String md5Value;
    private long fileSize;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
