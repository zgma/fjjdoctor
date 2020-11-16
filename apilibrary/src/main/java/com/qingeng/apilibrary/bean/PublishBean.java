package com.qingeng.apilibrary.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

public class PublishBean extends BaseBean {


    /**
     * "imageModels": null,
     *             "top": null,
     *             "logCode": null,
     *             "createDate": "2020-06-17 15:01:05",
     *             "createUser": 899,
     *             "userId": null,
     *             "status": 1,
     *             "updateUser": null,
     *             "id": 14,
     *             "tasCode": null,
     *             "updateDate": null,
     *             "username": "高V吃饭",
     *             "headImage": "http://xulyqn.ahrjkf.com/upload/2020-06-09/b58c252bf63a4221b4719ea3432eb984.jpg",
     *             "uniqueId": "sx_i3b8rmp3",
     *             "title": "Zdj",
     *             "images": [
     *                 {
     *                     "url": "http://baidu.com",
     *                     "image": "http://xulyqn.ahrjkf.com/upload/2020-06-17/622cbf74840841da95599b967b9e533bjpg",
     *                     "happyId": null,
     *                     "id": 99
     *                 },
     *                 {
     *                     "url": "http://baidu.com",
     *                     "image": "http://xulyqn.ahrjkf.com/upload/2020-06-17/5b871dba1ac44e88bd18c4529c08a18cjpg",
     *                     "happyId": null,
     *                     "id": 100
     *                 }
     *             ],
     *             "createUserName": null,
     *             "updateUserName": null,
     *             "fileList": null,
     *             "url": "http://baidu.com"
     */

    private int id;
    private String username;
    private String headImage;
    private String uniqueId;
    private String title;
    private String url;
    private int createUser;
    private int status;
    private int likeCount;
    private ArrayList<ImageBean> images;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<ImageBean> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImageBean> images) {
        this.images = images;
    }

    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
