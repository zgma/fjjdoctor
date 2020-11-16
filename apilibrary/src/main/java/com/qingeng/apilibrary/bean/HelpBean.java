package com.qingeng.apilibrary.bean;

public class HelpBean extends BaseBean {

    /**
     *  "id": 1,
     *             "title": "1111",
     *             "answer": "&lt;p&gt;1&lt;img src=\"http://xulyqn.ahrjkf.com/upload/2019-10-09/edc60b31edb84473940c245e3071fa4a.jpg\"&gt;11&lt;img src=\"http://xulyqn.ahrjkf.com/upload/2019-10-09/fc0b757d6b5f48999cfbe493c48ce650.jpeg\"&gt;&lt;/p&gt;",
     *             "createDate": "2019-10-09 22:01:33",
     *             "updateDate": "2019-10-09 22:25:47"
     */

    private String id;
    private String title;
    private String answer;
    private String createDate;
    private String updateDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
