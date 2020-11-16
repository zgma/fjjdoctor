package com.qingeng.apilibrary.bean;

public class ReportItemBean extends BaseBean {


    /**
     *  "id": 1,
     *             "content": "举报内容1",
     *             "createUser": null,
     *             "createDate": null,
     *             "updateUser": null,
     *             "updateDate": null,
     *             "type": null
     */

    private int id;
    private String content;
    private String createUser;
    private String createDate;
    private String updateUser;
    private String updateDate;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
