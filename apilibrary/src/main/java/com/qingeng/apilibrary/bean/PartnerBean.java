package com.qingeng.apilibrary.bean;

public class PartnerBean extends BaseBean {
    /**
     *   "createDate":null,
     *                 "createUser":null,
     *                 "id":4,
     *                 "logo":"http://xulyqn.ahrjkf.com/upload/2020-05-01/186376fea4774a2d972e536554ed1b9e.jpg",
     *                 "name":"脉脉",
     *                 "partnerId":null,
     *                 "url":"http://47.97.207.101/"
     */

    private String logo;
    private String name;
    private String url;
    private int id;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
